package com.minquoad.framework.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import com.minquoad.framework.dao.entityMember.EntityMember;
import com.minquoad.framework.dao.entityMember.EntityMemberGetter;
import com.minquoad.framework.dao.entityMember.EntityMemberSetter;

public abstract class DaoImpl<Entity> {

	private DaoFactoryImpl daoFactory;

	private ConnectionManager connectionManager;

	private boolean initialised;

	private Collection<EntityMember<Entity, ?>> entityMembers;

	private DaoImpl<? super Entity> superClassDao;

	private Collection<DaoImpl<? extends Entity>> subClassDaos;

	private EntityMember<? super Entity, ?> primaryKeyEntityMember;

	private DaoInventory<Entity> inventory;

	private Collection<StatementListener> statementListeners;

	protected abstract String getTableName();

	protected abstract Entity instantiateBlank();

	public DaoImpl(DaoFactoryImpl daoFactory) {
		this.daoFactory = daoFactory;
		connectionManager = new ConnectionManager(this);
	}

	protected abstract void initSuperClass();

	protected void setSuperClass(Class<? super Entity> superClass) {
		setSuperClassDao(getDaoFactory().getDao(superClass));
	}

	private void setSuperClassDao(DaoImpl<? super Entity> superClassDao) {
		this.superClassDao = superClassDao;
	}

	private boolean hasSuperClassDao() {
		return getSuperClassDao() != null;
	}

	private DaoImpl<? super Entity> getSuperClassDao() {
		return superClassDao;
	}

	protected abstract void initSubClasses();

	protected <SubClass extends Entity> void addSubClass(Class<SubClass> subClass) {
		getSubClassDaos().add(getDaoFactory().getDao(subClass));
	}

	private Collection<DaoImpl<? extends Entity>> getSubClassDaos() {
		return subClassDaos;
	}

	private void setSubClassDaos(Collection<DaoImpl<? extends Entity>> subClassDaos) {
		this.subClassDaos = subClassDaos;
	}

	protected abstract void initEntityMembers();

	protected boolean isPrimaryKeyRandomlyGenerated() {
		if (hasSuperClassDao()) {
			return getSuperClassDao().isPrimaryKeyRandomlyGenerated();
		}
		return false;
	}

	public void clear() {
		getInventory().clear();
	}

	protected Connection getConnection() throws SQLException {
		return getDaoFactory().getConnection();
	}

	public Entity getByPk(Object pk) {
		@SuppressWarnings("unchecked")
		EntityMember<? super Entity, Object> primaryKeyEntityMember = (EntityMember<? super Entity, Object>) this.getPrimaryKeyEntityMember();
		return getByPk(pk, primaryKeyEntityMember, true);
	}

	private <PrimaryKey> Entity getByPk(
			PrimaryKey pk,
			EntityMember<? super Entity, PrimaryKey> primaryKeyEntityMember,
			boolean lookInInventory) {

		if (pk == null) {
			return null;
		}

		if (lookInInventory) {
			Entity entity = getInventory().getByPrimaryKey(pk);
			if (entity != null) {
				return entity;
			}
		}

		for (DaoImpl<? extends Entity> subClassDao : getSubClassDaos()) {
			Entity entity = subClassDao.getByPk(pk, primaryKeyEntityMember, false);
			if (entity != null) {
				return entity;
			}
		}

		return connectionManager.runWithAutoCommit((connection) -> {

			String statement = getIdBasedSelectAllStatement();
			triggerStatementListener(statement);
			PreparedStatement preparedStatement = connection.prepareStatement(statement);
			primaryKeyEntityMember.setValueInPreparedStatement(preparedStatement, 1, pk);
			ResultSet resultSet = preparedStatement.executeQuery();

			Entity entity = null;
			if (resultSet.next()) {
				entity = instantiateFromResultSet(resultSet);
			}

			preparedStatement.close();

			return entity;
		});
	}

	public void persist(Entity entity) {
		if (getPrimaryKeyEntityMember().getValue(entity) == null || !getInventory().contains(entity)) {
			insert(entity);
		} else {
			update(entity);
		}
	}

	public void insert(Entity entity) {

		if (isPrimaryKeyRandomlyGenerated() && getPrimaryKeyEntityMember().getValue(entity) == null) {
			getPrimaryKeyEntityMember().setRandomValue(entity);
		}

		connectionManager.runInSingleTransaction((connection) -> {
			insertRecursivelyFromSuper(connection, entity);
			return null;
		});

		putInInventories(entity);
	}

	private void insertRecursivelyFromSuper(Connection connection, Entity entity) throws SQLException {

		if (hasSuperClassDao()) {
			getSuperClassDao().insertRecursivelyFromSuper(connection, entity);
		}

		String statement = "INSERT INTO \""
				+ getTableName()
				+ "\"";
		boolean first = true;
		// if the entity has a superclass entity then the primary key member is not in
		// the list returned by getEntityMembers()
		if (hasSuperClassDao()) {
			statement += " (\"" + this.getPrimaryKeyEntityMember().getName() + "\"";
			first = false;
		}
		for (EntityMember<Entity, ?> entityMember : getEntityMembers()) {
			if (entityMember.getValue(entity) != null) {
				if (first) {
					statement += " (";
				} else {
					statement += ", ";
				}
				statement += "\"" + entityMember.getName() + "\"";
				first = false;
			}
		}
		// if no none null values
		if (first) {
			statement += " DEFAULT VALUES";
		} else {
			statement += ") VALUES";
			first = true;
			if (hasSuperClassDao()) {
				statement += " (?";
				first = false;
			}
			for (EntityMember<Entity, ?> entityMember : getEntityMembers()) {
				if (entityMember.getValue(entity) != null) {
					if (first) {
						statement += " (";
					} else {
						statement += ", ";
					}
					statement += "?";
					first = false;
				}
			}
			statement += ")";
		}
		statement += " RETURNING *;";

		triggerStatementListener(statement);
		PreparedStatement preparedStatement = connection.prepareStatement(statement);

		int i = 1;
		if (hasSuperClassDao()) {
			this.getPrimaryKeyEntityMember().setValueOfEntityInPreparedStatement(preparedStatement, i, entity);
			i++;
		}
		for (EntityMember<Entity, ?> entityMember : getEntityMembers()) {
			if (entityMember.getValue(entity) != null) {
				entityMember.setValueOfEntityInPreparedStatement(preparedStatement, i, entity);
				i++;
			}
		}

		ResultSet resultSet = preparedStatement.executeQuery();
		resultSet.next();

		getPrimaryKeyEntityMember().setValueOfResultSetInEntity(entity, resultSet);
		this.hydrate(entity, resultSet, false);

		preparedStatement.close();
	}

	public void delete(Entity entity) {

		// the deepest subclass is in charged
		for (DaoImpl<? extends Entity> subClassDao : getSubClassDaos()) {
			if (subClassDao.deleteSuperEntity(entity, this)) {
				return;
			}
		}

		// if there is no subclass that handled the entity

		connectionManager.runInSingleTransaction((connection) -> {
			deleteRecursivelyToSuper(connection, entity);
			return null;
		});

		deleteFromInventories(entity);
	}

	private <SuperEntity> boolean deleteSuperEntity(SuperEntity superEntity, DaoImpl<SuperEntity> superDao) {
		Entity entity = getFromSuperClassIfInstanceof(superEntity, superDao);
		if (entity == null) {
			return false;
		}
		delete(entity);
		return true;
	}

	private void deleteRecursivelyToSuper(Connection connection, Entity entity) throws SQLException {

		String statement = "DELETE FROM \"" + getTableName() + "\" WHERE \"" + getPrimaryKeyEntityMember().getName() + "\"=?;";

		triggerStatementListener(statement);
		PreparedStatement preparedStatement = connection.prepareStatement(statement);
		getPrimaryKeyEntityMember().setValueOfEntityInPreparedStatement(preparedStatement, 1, entity);
		preparedStatement.executeUpdate();
		preparedStatement.close();

		if (hasSuperClassDao()) {
			getSuperClassDao().deleteRecursivelyToSuper(connection, entity);
		}
	}

	public void update(Entity entity) {
		getInventory().checkPrimaryKeyUnchanged(entity);
		updateChecked(entity);
	}

	private void updateChecked(Entity entity) {

		// the deepest subclass is in charged
		for (DaoImpl<? extends Entity> subClassDao : getSubClassDaos()) {
			if (subClassDao.updateSuperEntity(entity, this)) {
				return;
			}
		}

		connectionManager.runInSingleTransaction((connection) -> {
			updateRecursivelyToSuper(connection, entity);
			return null;
		});
	}

	private <SuperEntity> boolean updateSuperEntity(SuperEntity superEntity, DaoImpl<SuperEntity> superDao) {
		Entity entity = getFromSuperClassIfInstanceof(superEntity, superDao);
		if (entity == null) {
			return false;
		}
		updateChecked(entity);
		return true;
	}

	private void updateRecursivelyToSuper(Connection connection, Entity entity) throws SQLException {

		String statement = "UPDATE \""
				+ getTableName()
				+ "\" SET \""
				+ getColumnNamesInSingleString("\"=?, \"", false)
				+ "\"=? WHERE \""
				+ getPrimaryKeyEntityMember().getName()
				+ "\"=?;";

		triggerStatementListener(statement);
		PreparedStatement preparedStatement = connection.prepareStatement(statement);

		int i = 1;
		for (EntityMember<Entity, ?> entityMember : getEntityMembers()) {
			if (entityMember != getPrimaryKeyEntityMember()) {
				entityMember.setValueOfEntityInPreparedStatement(preparedStatement, i, entity);
				i++;
			}
		}

		getPrimaryKeyEntityMember().setValueOfEntityInPreparedStatement(preparedStatement, i, entity);
		preparedStatement.executeUpdate();

		preparedStatement.close();

		if (hasSuperClassDao()) {
			getSuperClassDao().updateRecursivelyToSuper(connection, entity);
		}
	}

	public Collection<Entity> getAll() {
		return this.getAllMatching(new HashMap<String, Object>());
	}

	public Collection<Entity> getAllMatching(String memberName, Object value) {
		Map<String, Object> criteria = new HashMap<String, Object>();
		criteria.put(memberName, value);
		return getAllMatching(criteria);
	}

	public Collection<Entity> getAllMatching(Map<String, Object> criteria) {

		return connectionManager.runWithAutoCommit((connection) -> {

			String statement = "SELECT";
			if (getSubClassDaos().isEmpty()) {
				statement += " *";
			} else {
				statement += " \"" + getTableName() + "\".\"" + getPrimaryKeyEntityMember().getName() + "\"";
			}
			statement += " FROM \"" + getTableName() + "\"";
			String superTables = getSuperTableNamesInSingleString("\", \"");
			if (superTables != null) {
				statement += ", \"" + superTables + "\"";
			}
			boolean first = true;
			for (Map.Entry<String, Object> criterion : criteria.entrySet()) {
				if (first) {
					statement += " WHERE";
				} else {
					statement += " AND";
				}
				statement += " \"";
				// if potentially ambiguous
				if (criterion.getKey().equals(getPrimaryKeyEntityMember().getName())) {
					statement += getTableName() + "\".\"" + criterion.getKey();
				} else {
					statement += criterion.getKey();
				}
				statement += "\"";
				if (criterion.getValue() == null) {
					statement += " IS NULL";
				} else {
					statement += "=?";
				}
				first = false;
			}
			String superTableJoinClause = getSuperTableJoinClause();
			if (superTableJoinClause != null) {
				if (first) {
					statement += " WHERE ";
				} else {
					statement += " AND ";
				}
				statement += superTableJoinClause;
				first = false;
			}
			statement += ";";

			triggerStatementListener(statement);
			PreparedStatement preparedStatement = connection.prepareStatement(statement);

			int i = 1;
			for (Map.Entry<String, Object> criterion : criteria.entrySet()) {
				if (criterion.getValue() != null) {
					EntityMember<? super Entity, ?> entityMember = getEntityMember(criterion.getKey(), true);
					entityMember.setValueOfCriterionInPreparedStatement(preparedStatement, i, criterion.getValue());
					i++;
				}
			}

			ResultSet resultSet = preparedStatement.executeQuery();

			while (resultSet.next()) {
				if (getSubClassDaos().isEmpty()) {
					toUnifiedInstance(resultSet);
				} else {
					getByPk(resultSet, getPrimaryKeyEntityMember().getName());
				}
			}

			preparedStatement.close();

			Collection<Entity> entities = new ArrayList<Entity>();
			for (Entity instantiatedEntity : getInstantiatedEntyties()) {
				if (isEntityMachingCriteria(instantiatedEntity, criteria)) {
					entities.add(instantiatedEntity);
				}
			}
			return entities;
		});
	}

	public Entity getOneMatching(String memberName, Object value) {
		Map<String, Object> criteria = new HashMap<String, Object>();
		criteria.put(memberName, value);
		return getOneMatching(criteria);
	}

	public Entity getOneMatching(Map<String, Object> criteria) {
		for (Entity instantiatedEntity : getInstantiatedEntyties()) {
			if (isEntityMachingCriteria(instantiatedEntity, criteria)) {
				return instantiatedEntity;
			}
		}

		return connectionManager.runWithAutoCommit((connection) -> {

			String statement = "SELECT";
			if (getSubClassDaos().isEmpty()) {
				statement += " *";
			} else {
				statement += " \"" + getTableName() + "\".\"" + getPrimaryKeyEntityMember().getName() + "\"";
			}
			statement += " FROM \"" + getTableName() + "\"";
			String superTables = getSuperTableNamesInSingleString("\", \"");
			if (superTables != null) {
				statement += ", \"" + superTables + "\"";
			}
			boolean first = true;
			for (Map.Entry<String, Object> criterion : criteria.entrySet()) {
				if (first) {
					statement += " WHERE";
				} else {
					statement += " AND";
				}
				statement += " \"";
				// if potentially ambiguous
				if (criterion.getKey().equals(getPrimaryKeyEntityMember().getName())) {
					statement += getTableName() + "\".\"" + criterion.getKey();
				} else {
					statement += criterion.getKey();
				}
				statement += "\"";
				if (criterion.getValue() == null) {
					statement += " IS NULL";
				} else {
					statement += "=?";
				}
				first = false;
			}
			String superTableJoinClause = getSuperTableJoinClause();
			if (superTableJoinClause != null) {
				if (first) {
					statement += " WHERE ";
				} else {
					statement += " AND ";
				}
				statement += superTableJoinClause;
				first = false;
			}
			statement += " LIMIT 1;";

			triggerStatementListener(statement);
			PreparedStatement preparedStatement = connection.prepareStatement(statement);

			int i = 1;
			for (Map.Entry<String, Object> criterion : criteria.entrySet()) {
				if (criterion.getValue() != null) {
					EntityMember<? super Entity, ?> entityMember = getEntityMember(criterion.getKey(), true);
					entityMember.setValueOfCriterionInPreparedStatement(preparedStatement, i, criterion.getValue());
					i++;
				}
			}

			ResultSet resultSet = preparedStatement.executeQuery();

			Entity entity = null;
			if (resultSet.next()) {
				if (getSubClassDaos().isEmpty()) {
					Entity dbEntity = toUnifiedInstance(resultSet);
					if (isEntityMachingCriteria(dbEntity, criteria)) {
						entity = dbEntity;
					}
				} else {
					Entity dbEntity = getByPk(resultSet, getPrimaryKeyEntityMember().getName());
					if (isEntityMachingCriteria(dbEntity, criteria)) {
						entity = dbEntity;
					}
				}
			}

			preparedStatement.close();

			return entity;
		});
	}

	public boolean isEntityMachingCriteria(Entity entity, Map<String, Object> criteria) {
		for (Map.Entry<String, Object> criterion : criteria.entrySet()) {
			EntityMember<? super Entity, ?> member = getEntityMember(criterion.getKey(), true);
			if (criterion.getValue() == null) {
				if (member.getValue(entity) != null) {
					return false;
				}
			} else {
				if (!criterion.getValue().equals(member.getValue(entity))) {
					return false;
				}
			}
		}
		return true;
	}

	private Entity toUnifiedInstance(ResultSet resultSet) throws SQLException {
		Entity entity = getInventory().getByPrimaryKey(resultSet);
		if (entity != null) {
			return entity;
		}
		return instantiateFromResultSet(resultSet);
	}

	private void hydrate(Entity entity, ResultSet resultSet, boolean resultSetContainingSuperMemberResults) throws SQLException {
		for (EntityMember<Entity, ?> entityMember : getEntityMembers()) {
			// the primary key should be set before hydration to exist in inventory
			// and avoid infinite loop on self referencing entities
			if (entityMember != getPrimaryKeyEntityMember()) {
				entityMember.setValueOfResultSetInEntity(entity, resultSet);
			}
		}
		if (resultSetContainingSuperMemberResults && hasSuperClassDao()) {
			getSuperClassDao().hydrate(entity, resultSet, true);
		}
	}

	private Entity instantiateFromResultSet(ResultSet resultSet) throws SQLException {
		Entity entity = instantiateBlank();
		getPrimaryKeyEntityMember().setValueOfResultSetInEntity(entity, resultSet);
		putInInventories(entity);
		hydrate(entity, resultSet, true);
		return entity;
	}

	private Entity getByPk(ResultSet pkResultSet, String keyColumnName) throws SQLException {
		return getByPk(pkResultSet, keyColumnName, true);
	}

	private Entity getByPk(ResultSet pkResultSet, String keyColumnName, boolean lookInInventory) throws SQLException {

		if (getPrimaryKeyEntityMember().getValueOfResultSet(pkResultSet, keyColumnName) == null) {
			return null;
		}

		if (lookInInventory) {
			Entity entity = getInventory().getByPrimaryKey(pkResultSet, keyColumnName);
			if (entity != null) {
				return entity;
			}
		}

		for (DaoImpl<? extends Entity> subClassDao : getSubClassDaos()) {
			Entity entity = subClassDao.getByPk(pkResultSet, keyColumnName, false);
			if (entity != null) {
				return entity;
			}
		}

		return connectionManager.runWithAutoCommit((connection) -> {

			String statement = getIdBasedSelectAllStatement();
			triggerStatementListener(statement);
			PreparedStatement preparedStatement = connection.prepareStatement(statement);
			getPrimaryKeyEntityMember().setValueOfResultSetInPreparedStatement(preparedStatement, 1, pkResultSet, keyColumnName);
			ResultSet resultSet = preparedStatement.executeQuery();

			Entity entity = null;
			if (resultSet.next()) {
				entity = instantiateFromResultSet(resultSet);
			}

			preparedStatement.close();

			return entity;
		});
	}

	private <SuperEntity> Entity getFromSuperClassIfInstanceof(SuperEntity superEntity, DaoImpl<SuperEntity> superDao) {
		return getInventory().getByPrimaryKey(superDao.getPrimaryKeyEntityMember().getValue(superEntity));
	}

	public void initIfneeded() {
		if (!initialised) {
			initialised = true;

			initSuperClass();
			if (hasSuperClassDao()) {

				DaoImpl<? super Entity> superClassDao = getSuperClassDao();
				superClassDao.initIfneeded();

				setPrimaryKeyEntityMember(superClassDao.getPrimaryKeyEntityMember());

				if (superClassDao.isPrimaryKeyRandomlyGenerated() != this.isPrimaryKeyRandomlyGenerated()) {
					throw new DaoException("Only super class's dao shoud override isPrimaryKeyRandomlyGenerated().");
				}
			}

			setEntityMembers(new ArrayList<EntityMember<Entity, ?>>());
			initEntityMembers();

			setInventory(new DaoInventory<Entity>(getPrimaryKeyEntityMember()));

			setSubClassDaos(new ArrayList<DaoImpl<? extends Entity>>());
			initSubClasses();
		}
	}

	private boolean hasEntityMember(String name, boolean lookInSuper) {
		for (EntityMember<Entity, ?> entityMember : getEntityMembers()) {
			if (entityMember.getName().equals(name)) {
				return true;
			}
		}
		if (lookInSuper && hasSuperClassDao()) {
			return getSuperClassDao().hasEntityMember(name, true);
		}
		return false;
	}

	private EntityMember<? super Entity, ?> getEntityMember(String name, boolean lookInSuper) {
		for (EntityMember<Entity, ?> entityMember : getEntityMembers()) {
			if (entityMember.getName().equals(name)) {
				return entityMember;
			}
		}
		if (lookInSuper && hasSuperClassDao()) {
			return getSuperClassDao().getEntityMember(name, true);
		}

		String error = "EntityMember with name " + name + " do not exist in " + this.getClass().getSimpleName();
		if (lookInSuper) {
			error += " or in its super class daos";
		}
		error += ".";
		throw new DaoException(error);
	}

	private Collection<EntityMember<Entity, ?>> getEntityMembers() {
		return entityMembers;
	}

	private void setEntityMembers(Collection<EntityMember<Entity, ?>> entityMembers) {
		this.entityMembers = entityMembers;
	}

	protected DaoFactoryImpl getDaoFactory() {
		return daoFactory;
	}

	private void putInInventories(Entity entity) {
		getInventory().put(entity);
		if (hasSuperClassDao()) {
			getSuperClassDao().putInInventories(entity);
		}
	}

	private void deleteFromInventories(Entity entity) {
		getInventory().delete(entity);
		if (hasSuperClassDao()) {
			getSuperClassDao().deleteFromInventories(entity);
		}
	}

	protected Collection<Entity> getInstantiatedEntyties() {
		return this.getInventory().values();
	}

	private DaoInventory<Entity> getInventory() {
		return inventory;
	}

	private void setInventory(DaoInventory<Entity> inventory) {
		this.inventory = inventory;
	}

	private EntityMember<? super Entity, ?> getPrimaryKeyEntityMember() {
		return primaryKeyEntityMember;
	}

	private void setPrimaryKeyEntityMember(EntityMember<? super Entity, ?> primaryKeyEntityMember) {
		this.primaryKeyEntityMember = primaryKeyEntityMember;
	}

	private void triggerStatementListener(String statement) {
		if (statementListeners != null) {
			for (StatementListener statementListener : statementListeners) {
				statementListener.listenStatement(statement);
			}
		}
	}

	public void addStatementListener(StatementListener statementListener) {
		if (statementListeners == null) {
			statementListeners = new LinkedList<StatementListener>();
		}
		statementListeners.add(statementListener);
	}

	public interface StatementListener {
		public void listenStatement(String statement);
	}

	private String getColumnNamesInSingleString(String separator, boolean withPrimaryKey) {
		String string = null;
		for (EntityMember<Entity, ?> entityMember : getEntityMembers()) {
			if (withPrimaryKey || entityMember != getPrimaryKeyEntityMember()) {
				if (string == null) {
					string = entityMember.getName();
				} else {
					string += separator + entityMember.getName();
				}
			}
		}
		return string;
	}

	private String getIdBasedSelectAllStatement() {
		String statement = "SELECT * FROM \"" + getTableName() + "\"";
		String superTables = getSuperTableNamesInSingleString("\", \"");
		if (superTables != null) {
			statement += ", \"" + superTables + "\"";
		}
		statement += " WHERE \"" + getTableName() + "\".\"" + getPrimaryKeyEntityMember().getName() + "\"=?";
		String superTableJoinClause = getSuperTableJoinClause();
		if (superTableJoinClause != null) {
			statement += " AND " + superTableJoinClause;
		}
		statement += " LIMIT 1;";
		return statement;
	}

	private String getSuperTableJoinClause() {
		if (getSuperClassDao() == null) {
			return null;
		}
		String start = "\"";
		String end = "\".\"" + getPrimaryKeyEntityMember().getName() + "\"=\"" + getTableName() + "\".\"" + getPrimaryKeyEntityMember().getName() + "\"";
		String separator = end + " AND \"";

		return start + getSuperTableNamesInSingleString(separator) + end;
	}

	private String getSuperTableNamesInSingleString(String separator) {
		String string = null;

		DaoImpl<? super Entity> superClassDao = getSuperClassDao();
		while (superClassDao != null) {

			if (string == null) {
				string = superClassDao.getTableName();
			} else {
				string += separator + superClassDao.getTableName();
			}

			superClassDao = superClassDao.getSuperClassDao();
		}
		return string;
	}

	// EntityMember adders

	/**
	 * postgreSQL equivalent type : integer
	 * 
	 * @param name
	 * @param valueGetter
	 * @param valueSetter
	 */
	public void addIntegerEntityMember(String name,
			EntityMemberGetter<Entity, Integer> valueGetter,
			EntityMemberSetter<Entity, Integer> valueSetter) {

		this.addEntityMember(new EntityMember<Entity, Integer>(
				name,
				valueGetter,
				valueSetter,
				ResultSet::getInt,
				PreparedStatement::setInt,
				() -> Math.abs(new Random().nextInt())));
	}

	/**
	 * postgreSQL equivalent type : boolean
	 * 
	 * @param name
	 * @param valueGetter
	 * @param valueSetter
	 */
	public void addBooleanEntityMember(String name,
			EntityMemberGetter<Entity, Boolean> valueGetter,
			EntityMemberSetter<Entity, Boolean> valueSetter) {

		this.addEntityMember(new EntityMember<Entity, Boolean>(
				name,
				valueGetter,
				valueSetter,
				ResultSet::getBoolean,
				PreparedStatement::setBoolean));
	}

	/**
	 * postgreSQL equivalent type : bytea
	 * 
	 * @param name
	 * @param valueGetter
	 * @param valueSetter
	 */
	public void addByteaEntityMember(String name,
			EntityMemberGetter<Entity, byte[]> valueGetter,
			EntityMemberSetter<Entity, byte[]> valueSetter) {

		this.addEntityMember(new EntityMember<Entity, byte[]>(
				name,
				valueGetter,
				valueSetter,
				ResultSet::getBytes,
				PreparedStatement::setBytes));
	}

	/**
	 * postgreSQL equivalent type : char
	 * 
	 * @param name
	 * @param valueGetter
	 * @param valueSetter
	 */
	public void addByteEntityMember(String name,
			EntityMemberGetter<Entity, Byte> valueGetter,
			EntityMemberSetter<Entity, Byte> valueSetter) {

		this.addEntityMember(new EntityMember<Entity, Byte>(
				name,
				valueGetter,
				valueSetter,
				ResultSet::getByte,
				PreparedStatement::setByte));
	}

	/**
	 * postgreSQL equivalent type : double precision
	 * 
	 * @param name
	 * @param valueGetter
	 * @param valueSetter
	 */
	public void addDoubleEntityMember(String name,
			EntityMemberGetter<Entity, Double> valueGetter,
			EntityMemberSetter<Entity, Double> valueSetter) {

		this.addEntityMember(new EntityMember<Entity, Double>(
				name,
				valueGetter,
				valueSetter,
				ResultSet::getDouble,
				PreparedStatement::setDouble));
	}

	/**
	 * postgreSQL equivalent type : real
	 * 
	 * @param name
	 * @param valueGetter
	 * @param valueSetter
	 */
	public void addFloatEntityMember(String name,
			EntityMemberGetter<Entity, Float> valueGetter,
			EntityMemberSetter<Entity, Float> valueSetter) {

		this.addEntityMember(new EntityMember<Entity, Float>(
				name,
				valueGetter,
				valueSetter,
				ResultSet::getFloat,
				PreparedStatement::setFloat));
	}

	/**
	 * postgreSQL equivalent type : bigint
	 * 
	 * @param name
	 * @param valueGetter
	 * @param valueSetter
	 */
	public void addLongEntityMember(String name,
			EntityMemberGetter<Entity, Long> valueGetter,
			EntityMemberSetter<Entity, Long> valueSetter) {

		this.addEntityMember(new EntityMember<Entity, Long>(
				name,
				valueGetter,
				valueSetter,
				ResultSet::getLong,
				PreparedStatement::setLong,
				() -> Math.abs(new Random().nextLong())));
	}

	/**
	 * postgreSQL equivalent type : smallint
	 * 
	 * @param name
	 * @param valueGetter
	 * @param valueSetter
	 */
	public void addShortEntityMember(String name,
			EntityMemberGetter<Entity, Short> valueGetter,
			EntityMemberSetter<Entity, Short> valueSetter) {

		this.addEntityMember(new EntityMember<Entity, Short>(
				name,
				valueGetter,
				valueSetter,
				ResultSet::getShort,
				PreparedStatement::setShort));
	}

	/**
	 * postgreSQL equivalent type : text
	 * 
	 * @param name
	 * @param valueGetter
	 * @param valueSetter
	 */
	public void addStringEntityMember(String name,
			EntityMemberGetter<Entity, String> valueGetter,
			EntityMemberSetter<Entity, String> valueSetter) {

		this.addEntityMember(new EntityMember<Entity, String>(
				name,
				valueGetter,
				valueSetter,
				ResultSet::getString,
				PreparedStatement::setString,
				() -> UUID.randomUUID().toString()));
	}

	/**
	 * postgreSQL equivalent type : timestamp with time zone
	 * 
	 * @param name
	 * @param valueGetter
	 * @param valueSetter
	 */
	public void addDateEntityMember(String name,
			EntityMemberGetter<Entity, Date> valueGetter,
			EntityMemberSetter<Entity, Date> valueSetter) {

		this.addEntityMember(new EntityMember<Entity, Date>(
				name,
				valueGetter,
				valueSetter,
				ResultSet::getTimestamp,
				(preparedStatement, parameterIndex, value) -> preparedStatement.setTimestamp(parameterIndex,
						new Timestamp(value.getTime()))));
	}

	/**
	 * postgreSQL equivalent type : timestamp with time zone
	 * 
	 * @param name
	 * @param valueGetter
	 * @param valueSetter
	 */
	public void addInstantEntityMember(String name,
			EntityMemberGetter<Entity, Instant> valueGetter,
			EntityMemberSetter<Entity, Instant> valueSetter) {

		this.addEntityMember(new EntityMember<Entity, Instant>(
				name,
				valueGetter,
				valueSetter,
				(resultSet, thisName) -> {
					Timestamp timestamp = resultSet.getTimestamp(thisName);
					if (timestamp == null) {
						return null;
					} else {
						return timestamp.toInstant();
					}
				},
				(preparedStatement, parameterIndex, value) -> preparedStatement.setTimestamp(parameterIndex,
						new Timestamp(value.toEpochMilli()))));
	}

	/**
	 * @param name
	 * @param referencedEntitySubclass
	 * @param valueGetter
	 * @param valueSetter
	 */
	public <ReferencedEntitySubclass> void addForeingKeyEntityMember(String name,
			Class<ReferencedEntitySubclass> referencedEntitySubclass,
			EntityMemberGetter<Entity, ReferencedEntitySubclass> valueGetter,
			EntityMemberSetter<Entity, ReferencedEntitySubclass> valueSetter) {

		DaoImpl<ReferencedEntitySubclass> referencedEntityDao = getDaoFactory().getDao(referencedEntitySubclass);

		this.addEntityMember(new EntityMember<Entity, ReferencedEntitySubclass>(
				name,
				valueGetter,
				valueSetter,
				(resultSet, thisName) -> referencedEntityDao.getByPk(resultSet, thisName),
				(preparedStatement, parameterIndex, value) -> referencedEntityDao.getPrimaryKeyEntityMember().setValueOfEntityInPreparedStatement(preparedStatement, parameterIndex, value)));
	}

	public void addEntityMember(EntityMember<Entity, ?> entityMember) {
		if (hasEntityMember(entityMember.getName(), true)) {
			throw new DaoException("Dao implementation of class " + getClass() + " has duplicated EntityMember (name=\"" + entityMember.getName() + "\").");
		}
		getEntityMembers().add(entityMember);

		if (getPrimaryKeyEntityMember() == null) {
			setPrimaryKeyEntityMember(entityMember);
		}
	}

}
