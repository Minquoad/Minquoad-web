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
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import com.minquoad.framework.dao.entityMember.EntityMember;
import com.minquoad.framework.dao.entityMember.EntityMemberGetter;
import com.minquoad.framework.dao.entityMember.EntityMemberSetter;

public abstract class DaoImpl<Entity> {

	private List<EntityMember<Entity, ?>> entityMembers;

	private List<DaoImpl<? extends Entity>> subClassDaos;

	// a primary-key must be an Integer, a Long or a String

	private EntityMember<? super Entity, Integer> integerPrimaryKeyEntityMember;
	private EntityMember<? super Entity, Long> longPrimaryKeyEntityMember;
	private EntityMember<? super Entity, String> stringPrimaryKeyEntityMember;

	private DaoInventory<Integer, Entity> integerInventory;
	private DaoInventory<Long, Entity> longInventory;
	private DaoInventory<String, Entity> stringInventory;

	private List<StatementListener> statementListeners;

	protected abstract void initEntityMembers();

	public abstract Connection getConnection() throws SQLException;

	public abstract Entity instantiateBlank();

	public abstract String getTableName();

	protected void initSubClassDaos() {
	};

	public DaoImpl<? super Entity> getSuperClassDao() {
		return null;
	}

	public boolean isPrimaryKeyRandomlyGenerated() {
		if (hasSuperClassDao()) {
			return getSuperClassDao().isPrimaryKeyRandomlyGenerated();
		}
		return false;
	}

	public Entity getByPk(Integer pk) {
		return getByPk(pk, DaoImpl::getIntegerPrimaryKeyEntityMember, DaoImpl::getIntegerInventory);
	}

	public Entity getByPk(Long pk) {
		return getByPk(pk, DaoImpl::getLongPrimaryKeyEntityMember, DaoImpl::getLongInventory);
	}

	public Entity getByPk(String pk) {
		return getByPk(pk, DaoImpl::getStringPrimaryKeyEntityMember, DaoImpl::getStringInventory);
	}

	private interface PrimaryKeyMemberGetter<PrimaryKey> {
		public <DaoEntity> EntityMember<? super DaoEntity, PrimaryKey> getPrimaryKeyMember(DaoImpl<DaoEntity> dao);
	}

	private interface InventoryGetter<PrimaryKey> {
		public <DaoEntity> DaoInventory<PrimaryKey, DaoEntity> getInventory(DaoImpl<DaoEntity> dao);
	}

	private <PrimaryKey> Entity getByPk(
			PrimaryKey pk,
			PrimaryKeyMemberGetter<PrimaryKey> primaryKeyMemberGetter,
			InventoryGetter<PrimaryKey> inventorygetter) {

		return getByPk(pk, primaryKeyMemberGetter, inventorygetter, true);
	}

	private <PrimaryKey> Entity getByPk(
			PrimaryKey pk,
			PrimaryKeyMemberGetter<PrimaryKey> primaryKeyMemberGetter,
			InventoryGetter<PrimaryKey> inventorygetter,
			boolean lookInInventory) {

		EntityMember<? super Entity, PrimaryKey> primaryKeyMember = primaryKeyMemberGetter.getPrimaryKeyMember(this);
		if (primaryKeyMember == null) {
			throw new DaoException("The wrong PK type has been used on Dao " + this.getClass().getSimpleName() + ".");
		}

		if (pk == null) {
			return null;
		}

		if (lookInInventory) {
			Entity entity = inventorygetter.getInventory(this).getByPrimaryKey(pk);
			if (entity != null) {
				return entity;
			}
		}

		for (DaoImpl<? extends Entity> subClassDao : getSubClassDaos()) {
			Entity entity = subClassDao.getByPk(pk, primaryKeyMemberGetter, inventorygetter, false);
			if (entity != null) {
				return entity;
			}
		}

		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;

		try {
			connection = getConnection();
			String statement = getIdBasedSelectAllStatement();
			triggerStatementListener(statement);
			preparedStatement = connection.prepareStatement(statement);
			primaryKeyMember.setValueInPreparedStatement(preparedStatement, 1, pk);
			resultSet = preparedStatement.executeQuery();

			Entity entity = null;
			if (resultSet.next()) {
				entity = instantiateFromResultSet(resultSet);
			}

			ConnectionManager.close(resultSet, preparedStatement, connection);

			return entity;

		} catch (Exception e) {
			ConnectionManager.safeClose(resultSet, preparedStatement, connection);
			throw new DaoException(e);
		}
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

	public void persist(Entity entity) {
		if (getPrimaryKeyEntityMember().getValue(entity) == null || !getInventory().isInside(entity)) {
			this.insert(entity);
		} else {
			this.update(entity);
		}
	}

	public void insert(Entity entity) {

		if (isPrimaryKeyRandomlyGenerated() && getPrimaryKeyEntityMember().getValue(entity) == null) {
			setRandomPrimaryKey(entity);
		}

		insertRecursivelyFromSuper(entity);
		putInInventories(entity);
	}

	protected void setRandomPrimaryKey(Entity entity) {
		if (isPrimaryKeyInteger()) {
			getIntegerPrimaryKeyEntityMember().setValue(entity, Math.abs(new Random().nextInt()));
		} else if (isPrimaryKeyLong()) {
			getLongPrimaryKeyEntityMember().setValue(entity, Math.abs(new Random().nextLong()));
		} else if (isPrimaryKeyString()) {
			getStringPrimaryKeyEntityMember().setValue(entity, UUID.randomUUID().toString());
		} else {
			throw new DaoException("Not all primary key types are handeled in setRandomPrimaryKey()");
		}
	}

	private void insertRecursivelyFromSuper(Entity entity) {

		if (hasSuperClassDao()) {
			getSuperClassDao().insertRecursivelyFromSuper(entity);
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

		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;

		try {
			connection = getConnection();

			triggerStatementListener(statement);
			preparedStatement = connection.prepareStatement(statement);

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

			resultSet = preparedStatement.executeQuery();
			resultSet.next();

			getPrimaryKeyEntityMember().setValueOfResultSetInEntity(entity, resultSet);
			this.hydrate(entity, resultSet, false);

			ConnectionManager.close(resultSet, preparedStatement, connection);

		} catch (Exception e) {
			ConnectionManager.safeClose(resultSet, preparedStatement, connection);
			throw new DaoException(e);
		}
	}

	public void delete(Entity entity) {

		// the deepest subclass is in charged
		for (DaoImpl<? extends Entity> subClassDao : getSubClassDaos()) {
			if (subClassDao.deleteSuperEntity(entity, this)) {
				return;
			}
		}

		// if there is no subclass that handled the entity

		deleteRecursivelyToSuper(entity);
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

	private void deleteRecursivelyToSuper(Entity entity) {

		Connection connection = null;
		PreparedStatement preparedStatement = null;

		try {

			String statement = "DELETE FROM \"" + getTableName() + "\" WHERE \"" + getPrimaryKeyEntityMember().getName() + "\"=?;";

			connection = getConnection();
			triggerStatementListener(statement);
			preparedStatement = connection.prepareStatement(statement);
			getPrimaryKeyEntityMember().setValueOfEntityInPreparedStatement(preparedStatement, 1, entity);
			preparedStatement.executeUpdate();
			ConnectionManager.close(preparedStatement, connection);

			if (hasSuperClassDao()) {
				getSuperClassDao().deleteRecursivelyToSuper(entity);
			}

		} catch (Exception e) {
			ConnectionManager.safeClose(preparedStatement, connection);
			throw new DaoException(e);
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

		updateRecursivelyToSuper(entity);
	}

	private <SuperEntity> boolean updateSuperEntity(SuperEntity superEntity, DaoImpl<SuperEntity> superDao) {
		Entity entity = getFromSuperClassIfInstanceof(superEntity, superDao);
		if (entity == null) {
			return false;
		}
		updateChecked(entity);
		return true;
	}

	private void updateRecursivelyToSuper(Entity entity) {

		String statement = "UPDATE \""
				+ getTableName()
				+ "\" SET \""
				+ getColumnNamesInSingleString("\"=?, \"", false)
				+ "\"=? WHERE \""
				+ getPrimaryKeyEntityMember().getName()
				+ "\"=?;";

		Connection connection = null;
		PreparedStatement preparedStatement = null;

		try {
			connection = getConnection();
			triggerStatementListener(statement);
			preparedStatement = connection.prepareStatement(statement);

			int i = 1;
			for (EntityMember<Entity, ?> entityMember : getEntityMembers()) {
				if (entityMember != getPrimaryKeyEntityMember()) {
					entityMember.setValueOfEntityInPreparedStatement(preparedStatement, i, entity);
					i++;
				}
			}

			getPrimaryKeyEntityMember().setValueOfEntityInPreparedStatement(preparedStatement, i, entity);
			preparedStatement.executeUpdate();

			ConnectionManager.close(preparedStatement, connection);

			if (hasSuperClassDao()) {
				getSuperClassDao().updateRecursivelyToSuper(entity);
			}

		} catch (Exception e) {
			ConnectionManager.safeClose(preparedStatement, connection);
			throw new DaoException(e);
		}
	}

	public List<Entity> getAll() {
		return this.getAllMatching(new HashMap<String, Object>());
	}

	public List<Entity> getAllMatching(String memberName, Object value) {
		Map<String, Object> criteria = new HashMap<String, Object>();
		criteria.put(memberName, value);
		return getAllMatching(criteria);
	}

	public List<Entity> getAllMatching(Map<String, Object> criteria) {
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

		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;

		try {
			connection = getConnection();
			triggerStatementListener(statement);
			preparedStatement = connection.prepareStatement(statement);

			int i = 1;
			for (Map.Entry<String, Object> criterion : criteria.entrySet()) {
				if (criterion.getValue() != null) {
					EntityMember<? super Entity, ?> entityMember = getEntityMember(criterion.getKey(), true);
					entityMember.setValueOfCriterionInPreparedStatement(preparedStatement, i, criterion.getValue());
					i++;
				}
			}

			resultSet = preparedStatement.executeQuery();

			while (resultSet.next()) {
				if (getSubClassDaos().isEmpty()) {
					toUnifiedInstance(resultSet);
				} else {
					getByPk(resultSet, getPrimaryKeyEntityMember().getName());
				}
			}

			ConnectionManager.close(resultSet, preparedStatement, connection);

			List<Entity> entities = new ArrayList<Entity>();
			for (Entity instantiatedEntity : getInstantiatedEntyties()) {
				if (isEntityMachingCriteria(instantiatedEntity, criteria)) {
					entities.add(instantiatedEntity);
				}
			}
			return entities;

		} catch (Exception e) {
			ConnectionManager.safeClose(resultSet, preparedStatement, connection);
			throw new DaoException(e);
		}
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

		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;

		try {
			connection = getConnection();
			triggerStatementListener(statement);
			preparedStatement = connection.prepareStatement(statement);

			int i = 1;
			for (Map.Entry<String, Object> criterion : criteria.entrySet()) {
				if (criterion.getValue() != null) {
					EntityMember<? super Entity, ?> entityMember = getEntityMember(criterion.getKey(), true);
					entityMember.setValueOfCriterionInPreparedStatement(preparedStatement, i, criterion.getValue());
					i++;
				}
			}

			resultSet = preparedStatement.executeQuery();

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

			ConnectionManager.close(resultSet, preparedStatement, connection);

			return entity;

		} catch (SQLException e) {
			ConnectionManager.safeClose(resultSet, preparedStatement, connection);
			throw new DaoException(e);
		}
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

		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;

		try {
			connection = getConnection();
			String statement = getIdBasedSelectAllStatement();
			triggerStatementListener(statement);
			preparedStatement = connection.prepareStatement(statement);
			getPrimaryKeyEntityMember().setValueOfResultSetInPreparedStatement(preparedStatement, 1, pkResultSet, keyColumnName);
			resultSet = preparedStatement.executeQuery();

			Entity entity = null;
			if (resultSet.next()) {
				entity = instantiateFromResultSet(resultSet);
			}

			ConnectionManager.close(resultSet, preparedStatement, connection);

			return entity;

		} catch (Exception e) {
			ConnectionManager.safeClose(resultSet, preparedStatement, connection);
			throw new DaoException(e);
		}
	}

	private <SuperEntity> Entity getFromSuperClassIfInstanceof(SuperEntity superEntity, DaoImpl<SuperEntity> superDao) {
		if (isPrimaryKeyInteger()) {
			return getIntegerInventory().getByPrimaryKey(superDao.getIntegerPrimaryKeyEntityMember().getValue(superEntity));
		}
		if (isPrimaryKeyLong()) {
			return getLongInventory().getByPrimaryKey(superDao.getLongPrimaryKeyEntityMember().getValue(superEntity));
		}
		if (isPrimaryKeyString()) {
			return getStringInventory().getByPrimaryKey(superDao.getStringPrimaryKeyEntityMember().getValue(superEntity));
		}
		throw new DaoException("Not all primary key types are handeled in getFromSuperClassIfInstanceof()");
	}

	private void initIfneeded() {
		if (entityMembers == null) {
			entityMembers = new ArrayList<EntityMember<Entity, ?>>();

			if (hasSuperClassDao()) {
				DaoImpl<? super Entity> superClassDao = getSuperClassDao();
				setIntegerPrimaryKeyEntityMember(superClassDao.getIntegerPrimaryKeyEntityMember());
				setLongPrimaryKeyEntityMember(superClassDao.getLongPrimaryKeyEntityMember());
				setStringPrimaryKeyEntityMember(superClassDao.getStringPrimaryKeyEntityMember());
				if (!hasPrimaryKey()) {
					throw new DaoException("Not all primary key types are handeled in initIfneeded()");
				}

				if (superClassDao.isPrimaryKeyRandomlyGenerated() != this.isPrimaryKeyRandomlyGenerated()) {
					throw new DaoException("Only super class's dao shoud override isPrimaryKeyRandomlyGenerated().");
				}
			}

			this.initEntityMembers();

			if (!hasPrimaryKey()) {
				throw new DaoException("No primary key defined in " + this.getClass().getSimpleName());
			}

			if (isPrimaryKeyInteger()) {
				this.setIntegerInventory(new DaoInventory<Integer, Entity>(
						getIntegerPrimaryKeyEntityMember()));
			} else if (isPrimaryKeyLong()) {
				setLongInventory(new DaoInventory<Long, Entity>(
						getLongPrimaryKeyEntityMember()));
			} else if (isPrimaryKeyString()) {
				setStringInventory(new DaoInventory<String, Entity>(
						getStringPrimaryKeyEntityMember()));
			} else {
				throw new DaoException("Not all primary key types are handeled in initIfneeded()");
			}

		}
	}

	private List<EntityMember<Entity, ?>> getEntityMembers() {
		initIfneeded();
		return entityMembers;
	}

	private EntityMember<? super Entity, ?> getEntityMember(String name, boolean lookInSuper) {
		for (EntityMember<Entity, ?> entityMember : getEntityMembers()) {
			if (entityMember.getName().equals(name)) {
				return entityMember;
			}
		}
		try {
			if (lookInSuper) {
				return getSuperClassDao().getEntityMember(name, true);
			}
		} catch (NullPointerException e) {
		}
		String error = "EntityMember with name " + name + " do not exist in " + this.getClass().getSimpleName();
		if (lookInSuper) {
			error += " or in its super class daos";
		}
		error += ".";
		throw new DaoException(error);
	}

	private boolean hasEntityMember(String name, boolean includeSuper) {
		try {
			getEntityMember(name, includeSuper);
			return true;
		} catch (DaoException e) {
			return false;
		}
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

	private DaoInventory<?, Entity> getInventory() {
		if (isPrimaryKeyInteger()) {
			return getIntegerInventory();
		}
		if (isPrimaryKeyLong()) {
			return getLongInventory();
		}
		if (isPrimaryKeyString()) {
			return getStringInventory();
		}
		throw new DaoException("Not all primary key types are handeled in getInventory()");
	}

	private DaoInventory<Integer, Entity> getIntegerInventory() {
		initIfneeded();
		return integerInventory;
	}

	private DaoInventory<Long, Entity> getLongInventory() {
		initIfneeded();
		return longInventory;
	}

	private DaoInventory<String, Entity> getStringInventory() {
		initIfneeded();
		return stringInventory;
	}

	private void setIntegerInventory(DaoInventory<Integer, Entity> integerInventory) {
		this.integerInventory = integerInventory;
	}

	private void setLongInventory(DaoInventory<Long, Entity> longInventory) {
		this.longInventory = longInventory;
	}

	private void setStringInventory(DaoInventory<String, Entity> stringInventory) {
		this.stringInventory = stringInventory;
	}

	private boolean hasPrimaryKey() {
		try {
			getPrimaryKeyEntityMember();
			return true;
		} catch (DaoException e) {
			return false;
		}
	}

	private EntityMember<? super Entity, ?> getPrimaryKeyEntityMember() {
		if (isPrimaryKeyInteger()) {
			return getIntegerPrimaryKeyEntityMember();
		}
		if (isPrimaryKeyLong()) {
			return getLongPrimaryKeyEntityMember();
		}
		if (isPrimaryKeyString()) {
			return getStringPrimaryKeyEntityMember();
		}
		throw new DaoException("Not all primary key types are handeled in getPrimaryKeyEntityMember()");
	}

	public boolean isPrimaryKeyInteger() {
		return getIntegerPrimaryKeyEntityMember() != null;
	}

	private boolean isPrimaryKeyLong() {
		return getLongPrimaryKeyEntityMember() != null;
	}

	private boolean isPrimaryKeyString() {
		return getStringPrimaryKeyEntityMember() != null;
	}

	private EntityMember<? super Entity, Integer> getIntegerPrimaryKeyEntityMember() {
		initIfneeded();
		return integerPrimaryKeyEntityMember;
	}

	private EntityMember<? super Entity, Long> getLongPrimaryKeyEntityMember() {
		initIfneeded();
		return longPrimaryKeyEntityMember;
	}

	private EntityMember<? super Entity, String> getStringPrimaryKeyEntityMember() {
		initIfneeded();
		return stringPrimaryKeyEntityMember;
	}

	private void setIntegerPrimaryKeyEntityMember(EntityMember<? super Entity, Integer> integerPrimaryKeyEntityMember) {
		this.integerPrimaryKeyEntityMember = integerPrimaryKeyEntityMember;
	}

	private void setLongPrimaryKeyEntityMember(EntityMember<? super Entity, Long> longPrimaryKeyEntityMember) {
		this.longPrimaryKeyEntityMember = longPrimaryKeyEntityMember;
	}

	private void setStringPrimaryKeyEntityMember(EntityMember<? super Entity, String> stringPrimaryKeyEntityMember) {
		this.stringPrimaryKeyEntityMember = stringPrimaryKeyEntityMember;
	}

	private List<DaoImpl<? extends Entity>> getSubClassDaos() {
		if (subClassDaos == null) {
			subClassDaos = new ArrayList<DaoImpl<? extends Entity>>();
			this.initSubClassDaos();
		}
		return subClassDaos;
	}

	public void addSubClassDao(DaoImpl<? extends Entity> dao) {
		getSubClassDaos().add(dao);
	}

	public boolean hasSuperClassDao() {
		return getSuperClassDao() != null;
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

	private String getSuperTableJoinClause() {
		if (getSuperClassDao() == null) {
			return null;
		}
		String start = "\"";
		String end = "\".\"" + getPrimaryKeyEntityMember().getName() + "\"=\"" + getTableName() + "\".\"" + getPrimaryKeyEntityMember().getName() + "\"";
		String separator = end + " AND \"";

		return start + getSuperTableNamesInSingleString(separator) + end;
	}

	protected Collection<Entity> getInstantiatedEntyties() {
		return this.getInventory().values();
	}

	public interface StatementListener {
		public void listenStatement(String statement);
	}

	public void addStatementListener(StatementListener statementListener) {
		if (statementListeners == null) {
			statementListeners = new LinkedList<StatementListener>();
		}
		statementListeners.add(statementListener);
	}

	private void triggerStatementListener(String statement) {
		if (statementListeners != null) {
			for (StatementListener statementListener : statementListeners) {
				statementListener.listenStatement(statement);
			}
		}
	}

	// EntityMember adders

	public void addEntityMember(EntityMember<Entity, ?> entityMember) {
		if (hasEntityMember(entityMember.getName(), true)) {
			throw new DaoException("Dao implementation of class " + this.getClass() + " has duplicated EntityMember (name=\"" + entityMember.getName() + "\").");
		}
		this.getEntityMembers().add(entityMember);
	}

	/**
	 * postgreSQL equivalent type : integer
	 * 
	 * @param name
	 * @param valueGetter
	 * @param valueSetter
	 */
	public void addIntegerEntityMember(String name,
			EntityMemberGetter<Entity, Integer> valueGetter,
			EntityMemberSetter<Entity, Integer> valueSetter,
			boolean isPrimaryKey) {

		EntityMember<Entity, Integer> entityMember = new EntityMember<Entity, Integer>(
				name,
				valueGetter,
				valueSetter,
				ResultSet::getInt,
				PreparedStatement::setInt);

		this.addEntityMember(entityMember);

		if (isPrimaryKey) {
			if (hasPrimaryKey()) {
				throw new DaoException("A primary key called " + name + " can not be added because a other called " + getPrimaryKeyEntityMember().getName() + " is already defined.");
			}
			setIntegerPrimaryKeyEntityMember(entityMember);
		}
	}

	public void addIntegerEntityMember(String name,
			EntityMemberGetter<Entity, Integer> valueGetter,
			EntityMemberSetter<Entity, Integer> valueSetter) {

		addIntegerEntityMember(name, valueGetter, valueSetter, false);
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
			EntityMemberSetter<Entity, Long> valueSetter,
			boolean isPrimaryKey) {

		EntityMember<Entity, Long> entityMember = new EntityMember<Entity, Long>(
				name,
				valueGetter,
				valueSetter,
				ResultSet::getLong,
				PreparedStatement::setLong);

		this.addEntityMember(entityMember);

		if (isPrimaryKey) {
			if (hasPrimaryKey()) {
				throw new DaoException("A primary key called " + name + " can not be added because a other called " + getPrimaryKeyEntityMember().getName() + " is already defined.");
			}
			setLongPrimaryKeyEntityMember(entityMember);
		}
	}

	public void addLongEntityMember(String name,
			EntityMemberGetter<Entity, Long> valueGetter,
			EntityMemberSetter<Entity, Long> valueSetter) {

		addLongEntityMember(name, valueGetter, valueSetter, false);
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
			EntityMemberSetter<Entity, String> valueSetter,
			boolean isPrimaryKey) {

		EntityMember<Entity, String> entityMember = new EntityMember<Entity, String>(
				name,
				valueGetter,
				valueSetter,
				ResultSet::getString,
				PreparedStatement::setString);

		this.addEntityMember(entityMember);

		if (isPrimaryKey) {
			if (hasPrimaryKey()) {
				throw new DaoException("A primary key called " + name + " can not be added because a other called " + getPrimaryKeyEntityMember().getName() + " is already defined.");
			}
			setStringPrimaryKeyEntityMember(entityMember);
		}
	}

	public void addStringEntityMember(String name,
			EntityMemberGetter<Entity, String> valueGetter,
			EntityMemberSetter<Entity, String> valueSetter) {

		addStringEntityMember(name, valueGetter, valueSetter, false);
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
	 * postgreSQL equivalent type : integer
	 * 
	 * @param name
	 * @param valueGetter
	 * @param valueSetter
	 * @param referencedEntityDaoImpl
	 */
	public <ReferencedEntitySubclass> void addForeingKeyEntityMember(String name,
			EntityMemberGetter<Entity, ReferencedEntitySubclass> valueGetter,
			EntityMemberSetter<Entity, ReferencedEntitySubclass> valueSetter,
			DaoImpl<ReferencedEntitySubclass> referencedEntityDaoImpl) {

		this.addEntityMember(new EntityMember<Entity, ReferencedEntitySubclass>(
				name,
				valueGetter,
				valueSetter,
				(resultSet, thisName) -> referencedEntityDaoImpl.getByPk(resultSet, thisName),
				(preparedStatement, parameterIndex, value) -> referencedEntityDaoImpl.getPrimaryKeyEntityMember().setValueOfEntityInPreparedStatement(preparedStatement, parameterIndex, value)));

	}

}
