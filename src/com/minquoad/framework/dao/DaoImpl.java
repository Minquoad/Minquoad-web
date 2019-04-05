package com.minquoad.framework.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

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

	protected abstract void initEntityMembers() throws DaoException;

	public abstract PreparedStatement prepareStatement(String statement) throws SQLException;

	public abstract Entity instantiateBlank();

	public abstract String getTableName();

	protected void initSubClassDaos() {
	};

	public DaoImpl<? super Entity> getSuperClassDao() {
		return null;
	}

	public Entity getByPk(Integer pk) {
		try {
			return getByPk(pk, DaoImpl::getIntegerPrimaryKeyEntityMember, DaoImpl::getIntegerInventory);
		} catch (SQLException | DaoException e) {
			e.printStackTrace();
		}
		return null;
	}

	public Entity getByPk(Long pk) {
		try {
			return getByPk(pk, DaoImpl::getLongPrimaryKeyEntityMember, DaoImpl::getLongInventory);
		} catch (SQLException | DaoException e) {
			e.printStackTrace();
		}
		return null;
	}

	public Entity getByPk(String pk) {
		try {
			return getByPk(pk, DaoImpl::getStringPrimaryKeyEntityMember, DaoImpl::getStringInventory);
		} catch (SQLException | DaoException e) {
			e.printStackTrace();
		}
		return null;
	}

	private interface PrimaryKeyMemberGetter<PrimaryKey> {
		public <DaoEntity> EntityMember<? super DaoEntity, PrimaryKey> getPrimaryKeyMember(DaoImpl<DaoEntity> dao) throws DaoException;
	}

	private interface InventoryGetter<PrimaryKey> {
		public <DaoEntity> DaoInventory<PrimaryKey, DaoEntity> getInventory(DaoImpl<DaoEntity> dao) throws DaoException;
	}

	private <PrimaryKey> Entity getByPk(
			PrimaryKey pk,
			PrimaryKeyMemberGetter<PrimaryKey> primaryKeyMemberGetter,
			InventoryGetter<PrimaryKey> inventorygetter) throws SQLException, DaoException {

		return getByPk(pk, primaryKeyMemberGetter, inventorygetter, true);
	}

	private <PrimaryKey> Entity getByPk(
			PrimaryKey pk,
			PrimaryKeyMemberGetter<PrimaryKey> primaryKeyMemberGetter,
			InventoryGetter<PrimaryKey> inventorygetter,
			boolean lookInInventory) throws SQLException, DaoException {

		if (pk == null) {
			return null;
		}

		EntityMember<? super Entity, PrimaryKey> primaryKeyMember = primaryKeyMemberGetter.getPrimaryKeyMember(this);
		DaoInventory<PrimaryKey, Entity> inventory = inventorygetter.getInventory(this);

		if (primaryKeyMember == null || pk == null) {
			return null;
		}

		Entity entity = null;

		if (lookInInventory) {
			entity = inventory.getByPrimaryKey(pk);
		}

		if (entity != null) {
			return entity;
		} else {

			for (DaoImpl<? extends Entity> subClassDao : getSubClassDaos()) {
				entity = subClassDao.getByPk(pk, primaryKeyMemberGetter, inventorygetter, false);
				if (entity != null) {
					return entity;
				}
			}

			PreparedStatement preparedStatement = prepareStatement(getIdBasedSelectAllQuery());
			primaryKeyMember.setValueInPreparedStatement(preparedStatement, 1, pk);
			ResultSet resultSet = preparedStatement.executeQuery();

			if (resultSet.next()) {
				entity = instantiateBlank();
				this.hydrate(entity, resultSet, true);
				getInventory().put(entity);
				return entity;
			} else {
				return null;
			}
		}
	}

	private String getIdBasedSelectAllQuery() throws DaoException {
		String query = "SELECT * FROM \"" + getTableName() + "\"";
		String superTables = getSuperTableNamesInSingleString("\", \"");
		if (superTables != null) {
			query += ", \"" + superTables + "\" ";
		} else {
			query += " ";
		}
		query += "WHERE \"" + getTableName() + "\".\"" + getPrimaryKeyEntityMember().getName() + "\"=? ";
		String superTableJoinClause = getSuperTableJoinClause();
		if (superTableJoinClause != null) {
			query += "AND " + superTableJoinClause;
		}
		query += "LIMIT 1 ;";
		return query;
	}

	public boolean persist(Entity entity) {
		if (entity != null) {
			try {

				if (getPrimaryKeyEntityMember().getValue(entity) == null || !getInventory().isInside(entity)) {
					return this.insert(entity);
				} else {
					return this.update(entity);
				}

			} catch (DaoException e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	public boolean insert(Entity entity) {
		if (entity != null) {
			try {
				insertRecursivelyFromSuper(entity);

				getInventory().put(entity);

				return true;
			} catch (SQLException | DaoException e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	private boolean insertRecursivelyFromSuper(Entity entity) throws SQLException, DaoException {

		if (hasSuperClassDao()) {
			getSuperClassDao().insertRecursivelyFromSuper(entity);
		}

		String query = "INSERT INTO \""
				+ getTableName()
				+ "\" ";
		boolean first = true;
		// if the Entity has a superclass entity then the primary key member is not in
		// the list returned by getEntityMembers()
		if (hasSuperClassDao()) {
			query += "(" + this.getPrimaryKeyEntityMember().getName();
			first = false;
		}
		for (EntityMember<Entity, ?> entityMember : getEntityMembers()) {
			if (entityMember.getValue(entity) != null) {
				if (first) {
					query += "(";
				} else {
					query += ", ";
				}
				query += "\"" + entityMember.getName() + "\"";
				first = false;
			}
		}
		// if no none null values
		if (first) {
			query += "DEFAULT VALUES ";
		} else {
			query += ") VALUES ";
			first = true;
			if (hasSuperClassDao()) {
				query += "(?";
				first = false;
			}
			for (EntityMember<Entity, ?> entityMember : getEntityMembers()) {
				if (entityMember.getValue(entity) != null) {
					if (first) {
						query += "(";
					} else {
						query += ", ";
					}
					query += "?";
					first = false;
				}
			}
			query += ") ";
		}
		query += "RETURNING * ;";

		PreparedStatement preparedStatement = prepareStatement(query);

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
		this.hydrate(entity, resultSet, false);

		return true;
	}

	public boolean delete(Entity entity) {
		if (entity != null) {
			try {

				// the deepest subclass is in charged
				for (DaoImpl<? extends Entity> subClassDao : getSubClassDaos()) {
					if (subClassDao.deleteSuperEntity(entity, this)) {
						return true;
					}
				}

				// if there is no subclass that handled the entity

				getInventory().delete(entity);

				return deleteRecursivelyToSuper(entity);

			} catch (DaoException | SQLException e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	private <SuperEntity> boolean deleteSuperEntity(SuperEntity superEntity, DaoImpl<SuperEntity> superDao) throws DaoException {
		return delete(getFromSuperClassIfInstanceof(superEntity, superDao));
	}
	
	private boolean deleteRecursivelyToSuper(Entity entity) throws SQLException, DaoException {

		String query = "DELETE FROM \"" + getTableName() + "\" WHERE \"" + getPrimaryKeyEntityMember().getName() + "\"=? ;";
		PreparedStatement preparedStatement = prepareStatement(query);
		getPrimaryKeyEntityMember().setValueOfEntityInPreparedStatement(preparedStatement, 1, entity);

		boolean success = 1 == preparedStatement.executeUpdate();

		if (hasSuperClassDao()) {
			success &= getSuperClassDao().deleteRecursivelyToSuper(entity);
		}

		return success;
	}

	public boolean update(Entity entity) {
		if (entity != null) {
			try {
				getInventory().checkPrimaryKeyUnchanged(entity);

				return updateChecked(entity);

			} catch (DaoException | SQLException e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	private boolean updateChecked(Entity entity) throws DaoException, SQLException {

		// the deepest subclass is in charged
		for (DaoImpl<? extends Entity> subClassDao : getSubClassDaos()) {
			if (subClassDao.updateSuperEntity(entity, this)) {
				return true;
			}
		}

		return updateRecursivelyToSuper(entity);

	}

	private <SuperEntity> boolean updateSuperEntity(SuperEntity superEntity, DaoImpl<SuperEntity> superDao) throws DaoException, SQLException {
		return updateChecked(getFromSuperClassIfInstanceof(superEntity, superDao));
	}

	private boolean updateRecursivelyToSuper(Entity entity) throws SQLException, DaoException {

		String query = "UPDATE \""
				+ getTableName()
				+ "\" SET \""
				+ getColumnNamesInSingleString("\"=?, \"", false)
				+ "\"=? WHERE \""
				+ getPrimaryKeyEntityMember().getName()
				+ "\"=? ;";

		PreparedStatement preparedStatement = prepareStatement(query);

		int i = 1;
		for (EntityMember<Entity, ?> entityMember : getEntityMembers()) {
			if (entityMember != getPrimaryKeyEntityMember()) {
				entityMember.setValueOfEntityInPreparedStatement(preparedStatement, i, entity);
				i++;
			}
		}

		getPrimaryKeyEntityMember().setValueOfEntityInPreparedStatement(preparedStatement, i, entity);

		boolean success = 1 == preparedStatement.executeUpdate();

		if (hasSuperClassDao()) {
			success &= getSuperClassDao().updateRecursivelyToSuper(entity);
		}

		return success;
	}

	public List<Entity> getAll() {
		return this.getAllMatching(new EntityCriterion[0]);
	}

	public List<Entity> getAllMatching(Object value, String memberName) {
		return getAllMatching(new EntityCriterion(value, memberName));
	}

	public List<Entity> getAllMatching(EntityCriterion criterion) {
		return getAllMatching(criterion.toArray());
	}

	public List<Entity> getAllMatching(EntityCriterion[] criteria) {
		try {

			String query = "SELECT ";
			if (getSubClassDaos().isEmpty()) {
				query += "* ";
			} else {
				query += "\"" + getTableName() + "\".\"" + getPrimaryKeyEntityMember().getName() + "\" ";
			}
			query += "FROM \"" + getTableName() + "\"";
			String superTables = getSuperTableNamesInSingleString("\", \"");
			if (superTables != null) {
				query += ", \"" + superTables + "\" ";
			} else {
				query += " ";
			}
			if (criteria.length != 0) {
				query += "WHERE ";
				for (EntityCriterion criterion : criteria) {
					if (criterion != criteria[0]) {
						query += "AND ";
					}
					query += "\"" + criterion.getName() + "\"";
					if (criterion.getValue() == null) {
						query += " IS NULL ";
					} else {
						query += "=? ";
					}
				}
			}
			String superTableJoinClause = getSuperTableJoinClause();
			if (superTableJoinClause != null) {
				if (criteria.length == 0) {
					query += "WHERE ";
				} else {
					query += "AND ";
				}
				query += superTableJoinClause;
			}
			query += " ;";

			PreparedStatement preparedStatement = prepareStatement(query);

			int i = 1;
			for (EntityCriterion criterion : criteria) {
				if (criterion.getValue() != null) {
					EntityMember<? super Entity, ?> entityMember = getEntityMember(criterion.getName(), true);
					entityMember.setValueOfCriterionInPreparedStatement(preparedStatement, i, criterion);
					i++;
				}
			}

			ResultSet resultSet = preparedStatement.executeQuery();

			while (resultSet.next()) {
				toUnifiedInstance(resultSet, !getSubClassDaos().isEmpty());
			}

			List<Entity> entities = new LinkedList<Entity>();
			for (Entity instantiatedEntity : getInstantiatedEntyties()) {
				if (isEntityMachingCriteria(instantiatedEntity, criteria)) {
					entities.add(instantiatedEntity);
				}
			}
			return entities;

		} catch (SQLException | DaoException e) {
			e.printStackTrace();
		}

		return null;
	}

	public Entity getOneMatching(Object value, String memberName) {
		return getOneMatching(new EntityCriterion(value, memberName));
	}

	public Entity getOneMatching(EntityCriterion criterion) {
		return getOneMatching(criterion.toArray());
	}

	public Entity getOneMatching(EntityCriterion[] criteria) {
		try {
			for (Entity instantiatedEntity : getInstantiatedEntyties()) {
				if (isEntityMachingCriteria(instantiatedEntity, criteria)) {
					return instantiatedEntity;
				}
			}

			String query = "SELECT ";
			if (getSubClassDaos().isEmpty()) {
				query += "* ";
			} else {
				query += "\"" + getTableName() + "\".\"" + getPrimaryKeyEntityMember().getName() + "\" ";
			}
			query += "FROM \"" + getTableName() + "\"";
			String superTables = getSuperTableNamesInSingleString("\", \"");
			if (superTables != null) {
				query += ", \"" + superTables + "\" ";
			} else {
				query += " ";
			}
			if (criteria.length != 0) {
				query += "WHERE ";
				for (EntityCriterion criterion : criteria) {
					if (criterion != criteria[0]) {
						query += "AND ";
					}
					query += "\"" + criterion.getName() + "\"";
					if (criterion.getValue() == null) {
						query += " IS NULL ";
					} else {
						query += "=? ";
					}
				}
			}
			String superTableJoinClause = getSuperTableJoinClause();
			if (superTableJoinClause != null) {
				if (criteria.length == 0) {
					query += "WHERE ";
				} else {
					query += "AND ";
				}
				query += superTableJoinClause;
			}
			query += " LIMIT 1 ;";

			PreparedStatement preparedStatement = prepareStatement(query);

			int i = 1;
			for (EntityCriterion criterion : criteria) {
				if (criterion.getValue() != null) {
					EntityMember<? super Entity, ?> entityMember = getEntityMember(criterion.getName(), true);
					entityMember.setValueOfCriterionInPreparedStatement(preparedStatement, i, criterion);
					i++;
				}
			}

			ResultSet resultSet = preparedStatement.executeQuery();

			if (resultSet.next()) {
				Entity entity = toUnifiedInstance(resultSet, !getSubClassDaos().isEmpty());
				if (isEntityMachingCriteria(entity, criteria)) {
					return entity;
				} else {
					return null;
				}
			} else {
				return null;
			}

		} catch (SQLException | DaoException e) {
			e.printStackTrace();
		}

		return null;
	}

	public boolean isEntityMachingCriteria(Entity entity, EntityCriterion[] criteria) throws DaoException {
		if (criteria != null) {
			for (EntityCriterion criterion : criteria) {
				EntityMember<? super Entity, ?> member = getEntityMember(criterion.getName(), true);
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
		}
		return true;
	}

	private void hydrate(Entity entity, ResultSet resultSet, boolean resultSetContainingSuperMemberResults) throws SQLException, DaoException {
		for (EntityMember<Entity, ?> entityMember : getEntityMembers()) {
			entityMember.setValueOfResultSetInEntity(entity, resultSet);
		}
		if (resultSetContainingSuperMemberResults && hasSuperClassDao()) {
			getSuperClassDao().hydrate(entity, resultSet, true);
		}
	}

	private Entity toUnifiedInstance(ResultSet resultSet, boolean pkOnlyInResultSet) throws DaoException, SQLException {
		if (pkOnlyInResultSet) {
			return getByPk(resultSet, getPrimaryKeyEntityMember().getName());
		} else {
			Entity entity = getInventory().getByPrimaryKey(resultSet);
			if (entity == null) {
				entity = instantiateBlank();
				this.hydrate(entity, resultSet, true);
				getInventory().put(entity);
			}
			return entity;
		}
	}

	private Entity getByPk(ResultSet pkResultSet, String keyColumnName) throws SQLException, DaoException {
		return getByPk(pkResultSet, keyColumnName, true);
	}

	private Entity getByPk(ResultSet pkResultSet, String keyColumnName, boolean lookInInventory) throws SQLException, DaoException {

		if (getPrimaryKeyEntityMember().getValueOfResultSet(pkResultSet, keyColumnName) == null) {
			return null;
		}

		Entity entity = null;

		if (lookInInventory) {
			entity = getInventory().getByPrimaryKey(pkResultSet, keyColumnName);
		}

		if (entity != null) {
			return entity;
		} else {

			for (DaoImpl<? extends Entity> subClassDao : getSubClassDaos()) {
				entity = subClassDao.getByPk(pkResultSet, keyColumnName, false);
				if (entity != null) {
					return entity;
				}
			}

			PreparedStatement preparedStatement = prepareStatement(getIdBasedSelectAllQuery());
			getPrimaryKeyEntityMember().setValueOfResultSetInPreparedStatement(preparedStatement, 1, pkResultSet, keyColumnName);
			ResultSet resultSet = preparedStatement.executeQuery();

			if (resultSet.next()) {
				entity = instantiateBlank();
				this.hydrate(entity, resultSet, true);
				getInventory().put(entity);
				return entity;
			} else {
				return null;
			}
		}
	}

	private <SuperEntity> Entity getFromSuperClassIfInstanceof(SuperEntity superEntity, DaoImpl<SuperEntity> superDao) throws DaoException {
		if (isPrimaryKeyInteger()) {
			return getIntegerInventory().getByPrimaryKey(superDao.getIntegerPrimaryKeyEntityMember().getValue(superEntity));
		}
		if (isPrimaryKeyLong()) {
			return getLongInventory().getByPrimaryKey(superDao.getLongPrimaryKeyEntityMember().getValue(superEntity));
		}
		if (isPrimaryKeyString()) {
			return getStringInventory().getByPrimaryKey(superDao.getStringPrimaryKeyEntityMember().getValue(superEntity));
		}
		throw new DaoException("Not all primary key types are handeled in getSubclassEntityIfIsOfSubclass()");
	}

	private void initIfneeded() throws DaoException {
		if (entityMembers == null) {
			entityMembers = new ArrayList<EntityMember<Entity, ?>>();

			if (hasSuperClassDao()) {
				setIntegerPrimaryKeyEntityMember(getSuperClassDao().getIntegerPrimaryKeyEntityMember());
				setLongPrimaryKeyEntityMember(getSuperClassDao().getLongPrimaryKeyEntityMember());
				setStringPrimaryKeyEntityMember(getSuperClassDao().getStringPrimaryKeyEntityMember());
				if (!hasPrimaryKey()) {
					throw new DaoException("Not all primary key types are handeled in initIfneeded()");
				}
			}

			this.initEntityMembers();

			if (!hasPrimaryKey()) {
				throw new DaoException("No primary key defined in " + this.getClass().getSimpleName());
			}

			if (isPrimaryKeyInteger()) {
				this.setIntegerInventory(new DaoInventory<Integer, Entity>(
						getIntegerPrimaryKeyEntityMember(),
						hasSuperClassDao() ? getSuperClassDao().getIntegerInventory() : null));
			} else if (isPrimaryKeyLong()) {
				setLongInventory(new DaoInventory<Long, Entity>(
						getLongPrimaryKeyEntityMember(),
						hasSuperClassDao() ? getSuperClassDao().getLongInventory() : null));
			} else if (isPrimaryKeyString()) {
				setStringInventory(new DaoInventory<String, Entity>(
						getStringPrimaryKeyEntityMember(),
						hasSuperClassDao() ? getSuperClassDao().getStringInventory() : null));
			} else {
				throw new DaoException("Not all primary key types are handeled in initIfneeded()");
			}

		}
	}

	private List<EntityMember<Entity, ?>> getEntityMembers() throws DaoException {
		initIfneeded();
		return entityMembers;
	}

	private EntityMember<? super Entity, ?> getEntityMember(String name, boolean lookInSuper) throws DaoException {
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

	private DaoInventory<Integer, Entity> getIntegerInventory() throws DaoException {
		initIfneeded();
		return integerInventory;
	}

	private DaoInventory<Long, Entity> getLongInventory() throws DaoException {
		initIfneeded();
		return longInventory;
	}

	private DaoInventory<String, Entity> getStringInventory() throws DaoException {
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

	private DaoInventory<?, Entity> getInventory() throws DaoException {
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

	private EntityMember<? super Entity, Integer> getIntegerPrimaryKeyEntityMember() throws DaoException {
		initIfneeded();
		return integerPrimaryKeyEntityMember;
	}

	private EntityMember<? super Entity, Long> getLongPrimaryKeyEntityMember() throws DaoException {
		initIfneeded();
		return longPrimaryKeyEntityMember;
	}

	private EntityMember<? super Entity, String> getStringPrimaryKeyEntityMember() throws DaoException {
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

	private boolean hasPrimaryKey() {
		try {
			getPrimaryKeyEntityMember();
			return true;
		} catch (DaoException e) {
			return false;
		}
	}

	private List<DaoImpl<? extends Entity>> getSubClassDaos() {
		if (subClassDaos == null) {
			subClassDaos = new ArrayList<DaoImpl<? extends Entity>>();
			this.initSubClassDaos();
		}
		return subClassDaos;
	}

	private EntityMember<? super Entity, ?> getPrimaryKeyEntityMember() throws DaoException {
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

	private boolean isPrimaryKeyInteger() throws DaoException {
		return getIntegerPrimaryKeyEntityMember() != null;
	}

	private boolean isPrimaryKeyLong() throws DaoException {
		return getLongPrimaryKeyEntityMember() != null;
	}

	private boolean isPrimaryKeyString() throws DaoException {
		return getStringPrimaryKeyEntityMember() != null;
	}

	public void addSubClassDao(DaoImpl<? extends Entity> dao) {
		getSubClassDaos().add(dao);
	}

	public boolean hasSuperClassDao() {
		return getSuperClassDao() != null;
	}
	
	private String getColumnNamesInSingleString(String separator, boolean withPrimaryKey) throws DaoException {
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

	private String getSuperTableJoinClause() throws DaoException {
		if (getSuperClassDao() == null) {
			return null;
		}
		String start = "\"";
		String end = "\".\"" + getPrimaryKeyEntityMember().getName() + "\"=\"" + getTableName() + "\".\"" + getPrimaryKeyEntityMember().getName() + "\" ";
		String separator = end + "AND \"";

		return start + getSuperTableNamesInSingleString(separator) + end;
	}

	protected Collection<Entity> getInstantiatedEntyties() throws DaoException {
		return this.getInventory().values();
	}

	// EntityMember adders

	public void addEntityMember(EntityMember<Entity, ?> entityMember) throws DaoException {
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
	 * @throws DaoException
	 */
	public void addIntegerEntityMember(String name,
			EntityMemberGetter<Entity, Integer> valueGetter,
			EntityMemberSetter<Entity, Integer> valueSetter,
			boolean isPrimaryKey) throws DaoException {

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
			EntityMemberSetter<Entity, Integer> valueSetter) throws DaoException {

		addIntegerEntityMember(name, valueGetter, valueSetter, false);
	}

	/**
	 * postgreSQL equivalent type : boolean
	 * 
	 * @param name
	 * @param valueGetter
	 * @param valueSetter
	 * @throws DaoException
	 */
	public void addBooleanEntityMember(String name,
			EntityMemberGetter<Entity, Boolean> valueGetter,
			EntityMemberSetter<Entity, Boolean> valueSetter) throws DaoException {

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
	 * @throws DaoException
	 */
	public void addByteaEntityMember(String name,
			EntityMemberGetter<Entity, byte[]> valueGetter,
			EntityMemberSetter<Entity, byte[]> valueSetter) throws DaoException {

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
	 * @throws DaoException
	 */
	public void addByteEntityMember(String name,
			EntityMemberGetter<Entity, Byte> valueGetter,
			EntityMemberSetter<Entity, Byte> valueSetter) throws DaoException {

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
	 * @throws DaoException
	 */
	public void addDoubleEntityMember(String name,
			EntityMemberGetter<Entity, Double> valueGetter,
			EntityMemberSetter<Entity, Double> valueSetter) throws DaoException {

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
	 * @throws DaoException
	 */
	public void addFloatEntityMember(String name,
			EntityMemberGetter<Entity, Float> valueGetter,
			EntityMemberSetter<Entity, Float> valueSetter) throws DaoException {

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
	 * @throws DaoException
	 */
	public void addLongEntityMember(String name,
			EntityMemberGetter<Entity, Long> valueGetter,
			EntityMemberSetter<Entity, Long> valueSetter,
			boolean isPrimaryKey) throws DaoException {

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
			EntityMemberSetter<Entity, Long> valueSetter) throws DaoException {

		addLongEntityMember(name, valueGetter, valueSetter, false);
	}

	/**
	 * postgreSQL equivalent type : smallint
	 * 
	 * @param name
	 * @param valueGetter
	 * @param valueSetter
	 * @throws DaoException
	 */
	public void addShortEntityMember(String name,
			EntityMemberGetter<Entity, Short> valueGetter,
			EntityMemberSetter<Entity, Short> valueSetter) throws DaoException {

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
	 * @throws DaoException
	 */
	public void addStringEntityMember(String name,
			EntityMemberGetter<Entity, String> valueGetter,
			EntityMemberSetter<Entity, String> valueSetter,
			boolean isPrimaryKey) throws DaoException {

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
			EntityMemberSetter<Entity, String> valueSetter) throws DaoException {

		addStringEntityMember(name, valueGetter, valueSetter, false);
	}

	/**
	 * postgreSQL equivalent type : timestamp with time zone
	 * 
	 * @param name
	 * @param valueGetter
	 * @param valueSetter
	 * @throws DaoException
	 */
	public void addDateEntityMember(String name,
			EntityMemberGetter<Entity, Date> valueGetter,
			EntityMemberSetter<Entity, Date> valueSetter) throws DaoException {

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
	 * @throws DaoException
	 */
	public void addInstantEntityMember(String name,
			EntityMemberGetter<Entity, Instant> valueGetter,
			EntityMemberSetter<Entity, Instant> valueSetter) throws DaoException {

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
	 * @throws DaoException
	 */
	public <ReferencedEntitySubclass> void addForeingKeyEntityMember(String name,
			EntityMemberGetter<Entity, ReferencedEntitySubclass> valueGetter,
			EntityMemberSetter<Entity, ReferencedEntitySubclass> valueSetter,
			DaoImpl<ReferencedEntitySubclass> referencedEntityDaoImpl) throws DaoException {

		this.addEntityMember(new EntityMember<Entity, ReferencedEntitySubclass>(
				name,
				valueGetter,
				valueSetter,
				(resultSet, thisName) -> referencedEntityDaoImpl.getByPk(resultSet, thisName),
				(preparedStatement, parameterIndex, value) -> referencedEntityDaoImpl.getPrimaryKeyEntityMember().setValueOfEntityInPreparedStatement(preparedStatement, parameterIndex, value)));

	}

}
