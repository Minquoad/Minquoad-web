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

	protected abstract void initEntityMembers() throws SQLException;

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
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public Entity getByPk(Long pk) {
		try {
			return getByPk(pk, DaoImpl::getLongPrimaryKeyEntityMember, DaoImpl::getLongInventory);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public Entity getByPk(String pk) {
		try {
			return getByPk(pk, DaoImpl::getStringPrimaryKeyEntityMember, DaoImpl::getStringInventory);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	private interface PrimaryKeyMemberGetter<PrimaryKey> {
		public <DaoEntity> EntityMember<? super DaoEntity, PrimaryKey> getPrimaryKeyMember(DaoImpl<DaoEntity> dao) throws SQLException;
	}

	private interface InventoryGetter<PrimaryKey> {
		public <DaoEntity> DaoInventory<PrimaryKey, DaoEntity> getInventory(DaoImpl<DaoEntity> dao) throws SQLException;
	}

	private <PrimaryKey> Entity getByPk(
			PrimaryKey pk,
			PrimaryKeyMemberGetter<PrimaryKey> primaryKeyMemberGetter,
			InventoryGetter<PrimaryKey> inventorygetter) throws SQLException {

		return getByPk(pk, primaryKeyMemberGetter, inventorygetter, true);
	}

	private <PrimaryKey> Entity getByPk(
			PrimaryKey pk,
			PrimaryKeyMemberGetter<PrimaryKey> primaryKeyMemberGetter,
			InventoryGetter<PrimaryKey> inventorygetter,
			boolean lookInInventory) throws SQLException {

		EntityMember<? super Entity, PrimaryKey> primaryKeyMember = primaryKeyMemberGetter.getPrimaryKeyMember(this);
		DaoInventory<PrimaryKey, Entity> inventory = inventorygetter.getInventory(this);

		if (primaryKeyMember != null && pk != null) {

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

				PreparedStatement preparedStatement = prepareStatement(query);
				primaryKeyMember.setValueInPreparedStatement(preparedStatement, 1, pk);
				ResultSet resultSet = preparedStatement.executeQuery();

				if (resultSet.next()) {
					entity = instantiateBlank();
					this.hydrate(entity, resultSet, true);
					getInventory().put(entity);
					return entity;
				}

			}
		}

		return null;
	}

	public boolean persist(Entity entity) {
		if (entity != null) {
			try {

				if (getPrimaryKeyEntityMember().getValue(entity) == null || !getInventory().isInside(entity)) {
					return this.insert(entity);
				} else {
					return this.update(entity);
				}

			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	public boolean insert(Entity entity) {
		if (entity != null) {
			try {

				if (getSuperClassDao() != null) {
					getSuperClassDao().insert(entity);
				}

				boolean primaryKeyNull = getPrimaryKeyEntityMember().getValue(entity) == null;

				int valuesCount = getEntityMembers().size();
				if (primaryKeyNull) {
					valuesCount--;
				}

				String query = "INSERT INTO \""
						+ getTableName()
						+ "\" (\""
						+ getColumnNamesInSingleString("\", \"", !primaryKeyNull)
						+ "\") VALUES (";
				for (int i = 0; i < valuesCount; i++) {
					if (i == 0) {
						query += "?";
					} else {
						query += ", ?";
					}
				}
				query += ") RETURNING * ;";

				PreparedStatement preparedStatement = prepareStatement(query);

				int i = 1;
				for (EntityMember<Entity, ?> entityMember : getEntityMembers()) {
					if (!primaryKeyNull || entityMember != getPrimaryKeyEntityMember()) {
						entityMember.setValueOfEntityInPreparedStatement(preparedStatement, i, entity);
						i++;
					}
				}

				ResultSet resultSet = preparedStatement.executeQuery();
				resultSet.next();
				this.hydrate(entity, resultSet, false);

				getInventory().put(entity);

				return true;
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	public boolean delete(Entity entity) {
		if (entity != null) {
			try {

				// the deepest subclass is in charged
				for (DaoImpl<? extends Entity> subClassDao : getSubClassDaos()) {
					if (isPrimaryKeyInteger()) {
						if (subClassDao.deleteByPk(getIntegerPrimaryKeyEntityMember().getValue(entity))) {
							return true;
						}
					} else if (isPrimaryKeyLong()) {
						if (subClassDao.deleteByPk(getLongPrimaryKeyEntityMember().getValue(entity))) {
							return true;
						}
					} else if (isPrimaryKeyString()) {
						if (subClassDao.deleteByPk(getStringPrimaryKeyEntityMember().getValue(entity))) {
							return true;
						}
					} else {
						throw new SQLException("Not all primary key types are handeled in delete()");
					}
				}

				// if there is no subclass that handled the entity

				getInventory().delete(entity);

				return deleteRecursivelyToSuper(entity);

			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	private boolean deleteRecursivelyToSuper(Entity entity) throws SQLException {
		
		String query = "DELETE FROM \"" + getTableName() + "\" WHERE \"" + getPrimaryKeyEntityMember().getName() + "\"=? ;";
		PreparedStatement preparedStatement = prepareStatement(query);
		getPrimaryKeyEntityMember().setValueOfEntityInPreparedStatement(preparedStatement, 1, entity);

		boolean success = 1 == preparedStatement.executeUpdate();

		if (getSuperClassDao() != null) {
			success &= getSuperClassDao().deleteRecursivelyToSuper(entity);
		}
		
		return success;
	}
	
	private boolean deleteByPk(Integer pk) {
		Entity entity = getByPk(pk);
		return delete(entity);
	}

	private boolean deleteByPk(Long pk) {
		Entity entity = getByPk(pk);
		return delete(entity);
	}

	private boolean deleteByPk(String pk) {
		Entity entity = getByPk(pk);
		return delete(entity);
	}

	public boolean update(Entity entity) {
		if (entity != null) {
			try {

				// the deepest subclass is in charged

				for (DaoImpl<? extends Entity> subClassDao : getSubClassDaos()) {
					if (isPrimaryKeyInteger()) {
						if (subClassDao.updateByPk(getIntegerPrimaryKeyEntityMember().getValue(entity))) {
							return true;
						}
					} else if (isPrimaryKeyLong()) {
						if (subClassDao.updateByPk(getLongPrimaryKeyEntityMember().getValue(entity))) {
							return true;
						}
					} else if (isPrimaryKeyString()) {
						if (subClassDao.updateByPk(getStringPrimaryKeyEntityMember().getValue(entity))) {
							return true;
						}
					} else {
						throw new SQLException("Not all primary key types are handeled in delete()");
					}
				}

				getInventory().checkPrimaryKeyUnchanged(entity);

				return updateRecursivelyToSuper(entity);

			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	private boolean updateRecursivelyToSuper(Entity entity) throws SQLException {

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

		if (getSuperClassDao() != null) {
			success &= getSuperClassDao().updateRecursivelyToSuper(entity);
		}
		
		return success;
	}
	
	private boolean updateByPk(Integer pk) {
		Entity entity = getByPk(pk);
		return update(entity);
	}

	private boolean updateByPk(Long pk) {
		Entity entity = getByPk(pk);
		return update(entity);
	}

	private boolean updateByPk(String pk) {
		Entity entity = getByPk(pk);
		return update(entity);
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

			String query = "SELECT * FROM \"" + getTableName() + "\"";
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
				toUnifiedInstance(resultSet);
			}

			List<Entity> entities = new LinkedList<Entity>();
			for (Entity instantiatedEntity : getInstantiatedEntyties()) {
				if (isEntityMachingCriteria(instantiatedEntity, criteria)) {
					entities.add(instantiatedEntity);
				}
			}
			return entities;

		} catch (SQLException e) {
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

			String query = "SELECT * FROM \"" + getTableName() + "\"";
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
				Entity entity = toUnifiedInstance(resultSet);
				if (isEntityMachingCriteria(entity, criteria)) {
					return entity;
				} else {
					return null;
				}
			} else {
				return null;
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return null;
	}

	public boolean isEntityMachingCriteria(Entity entity, EntityCriterion[] criteria) throws SQLException {
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

	private void hydrate(Entity entity, ResultSet resultSet, boolean resultSetContainingSuperMemberResults) throws SQLException {
		for (EntityMember<Entity, ?> entityMember : getEntityMembers()) {
			entityMember.setValueOfResultSetInEntity(entity, resultSet);
		}
		if (resultSetContainingSuperMemberResults && getSuperClassDao() != null) {
			getSuperClassDao().hydrate(entity, resultSet, true);
		}
	}

	private Entity toUnifiedInstance(ResultSet resultSet) throws SQLException {
		Entity entity = getInventory().getByPrimaryKey(resultSet);
		if (entity == null) {
			entity = instantiateBlank();
			this.hydrate(entity, resultSet, true);
			getInventory().put(entity);
		}
		return entity;
	}

	private void initIfneeded() throws SQLException {
		if (entityMembers == null) {
			entityMembers = new ArrayList<EntityMember<Entity, ?>>();

			DaoImpl<? super Entity> superClassDao = getSuperClassDao();

			if (superClassDao != null) {
				setIntegerPrimaryKeyEntityMember(superClassDao.getIntegerPrimaryKeyEntityMember());
				setLongPrimaryKeyEntityMember(superClassDao.getLongPrimaryKeyEntityMember());
				setStringPrimaryKeyEntityMember(superClassDao.getStringPrimaryKeyEntityMember());
			}

			this.initEntityMembers();

			if (isPrimaryKeyInteger()) {
				this.setIntegerInventory(new DaoInventory<Integer, Entity>(
						getIntegerPrimaryKeyEntityMember(),
						getSuperClassDao() == null ? null : getSuperClassDao().getIntegerInventory()));
			} else if (isPrimaryKeyLong()) {
				setLongInventory(new DaoInventory<Long, Entity>(
						getLongPrimaryKeyEntityMember(),
						getSuperClassDao() == null ? null : getSuperClassDao().getLongInventory()));
			} else if (isPrimaryKeyString()) {
				setStringInventory(new DaoInventory<String, Entity>(
						getStringPrimaryKeyEntityMember(),
						getSuperClassDao() == null ? null : getSuperClassDao().getStringInventory()));
			} else {
				throw new SQLException("Not all primary key types are handeled in initIfneeded()");
			}

			if (!hasPrimaryKey()) {
				throw new SQLException("No primary key defined in " + this.getClass().getSimpleName());
			}
		}
	}

	private boolean hasPrimaryKey() throws SQLException {
		try {
			getPrimaryKeyEntityMember();
			return true;
		} catch (SQLException e) {
		}
		return false;
	}

	private boolean isPrimaryKeyInteger() throws SQLException {
		return getIntegerPrimaryKeyEntityMember() != null;
	}

	private boolean isPrimaryKeyLong() throws SQLException {
		return getLongPrimaryKeyEntityMember() != null;
	}

	private boolean isPrimaryKeyString() throws SQLException {
		return getStringPrimaryKeyEntityMember() != null;
	}

	private DaoInventory<Integer, Entity> getIntegerInventory() throws SQLException {
		initIfneeded();
		return integerInventory;
	}

	private DaoInventory<Long, Entity> getLongInventory() throws SQLException {
		initIfneeded();
		return longInventory;
	}

	private DaoInventory<String, Entity> getStringInventory() throws SQLException {
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

	private DaoInventory<?, Entity> getInventory() throws SQLException {
		if (isPrimaryKeyInteger()) {
			return getIntegerInventory();
		}
		if (isPrimaryKeyLong()) {
			return getLongInventory();
		}
		if (isPrimaryKeyString()) {
			return getStringInventory();
		}
		throw new SQLException("Not all primary key types are handeled in getInventory()");
	}

	private List<EntityMember<Entity, ?>> getEntityMembers() throws SQLException {
		initIfneeded();
		return entityMembers;
	}

	private EntityMember<? super Entity, ?> getEntityMember(String name, boolean lookInSuper) throws SQLException {
		for (EntityMember<Entity, ?> entityMember : getEntityMembers()) {
			if (entityMember.getName().equals(name)) {
				return entityMember;
			}
		}
		try {
			if (lookInSuper) {
				return getSuperClassDao().getEntityMember(name, true);
			}
		} catch (Exception e) {
		}
		String error = "EntityMember with name " + name + " do not exist in " + this.getClass().getSimpleName();
		if (lookInSuper) {
			error += " or in its super class daos";
		}
		error += ".";
		throw new SQLException(error);
	}

	private boolean hasEntityMember(String name, boolean includeSuper) {
		try {
			getEntityMember(name, includeSuper);
			return true;
		} catch (SQLException e) {
			return false;
		}
	}

	private EntityMember<? super Entity, ?> getPrimaryKeyEntityMember() throws SQLException {
		if (isPrimaryKeyInteger()) {
			return getIntegerPrimaryKeyEntityMember();
		}
		if (isPrimaryKeyLong()) {
			return getLongPrimaryKeyEntityMember();
		}
		if (isPrimaryKeyString()) {
			return getStringPrimaryKeyEntityMember();
		}
		throw new SQLException("Not all primary key types are handeled in getPrimaryKeyEntityMember()");
	}

	private EntityMember<? super Entity, Integer> getIntegerPrimaryKeyEntityMember() throws SQLException {
		initIfneeded();
		return integerPrimaryKeyEntityMember;
	}

	private EntityMember<? super Entity, Long> getLongPrimaryKeyEntityMember() throws SQLException {
		initIfneeded();
		return longPrimaryKeyEntityMember;
	}

	private EntityMember<? super Entity, String> getStringPrimaryKeyEntityMember() throws SQLException {
		initIfneeded();
		return stringPrimaryKeyEntityMember;
	}

	private void setIntegerPrimaryKeyEntityMember(EntityMember<? super Entity, Integer> integerPrimaryKeyEntityMember) throws SQLException {
		this.integerPrimaryKeyEntityMember = integerPrimaryKeyEntityMember;
	}

	private void setLongPrimaryKeyEntityMember(EntityMember<? super Entity, Long> longPrimaryKeyEntityMember) throws SQLException {
		this.longPrimaryKeyEntityMember = longPrimaryKeyEntityMember;
	}

	private void setStringPrimaryKeyEntityMember(EntityMember<? super Entity, String> stringPrimaryKeyEntityMember) throws SQLException {
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

	private String getColumnNamesInSingleString(String separator, boolean withPrimaryKey) throws SQLException {
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

	private String getSuperTableJoinClause() throws SQLException {
		if (getSuperClassDao() == null) {
			return null;
		}
		String start = "\"";
		String end = "\".\"" + getPrimaryKeyEntityMember().getName() + "\"=\"" + getTableName() + "\".\"" + getPrimaryKeyEntityMember().getName() + "\" ";
		String separator = end + "AND \"";

		return start + getSuperTableNamesInSingleString(separator) + end;
	}

	protected Collection<Entity> getInstantiatedEntyties() throws SQLException {
		return this.getInventory().values();
	}

	// EntityMember adders

	public void addEntityMember(EntityMember<Entity, ?> entityMember) throws SQLException {
		if (hasEntityMember(entityMember.getName(), true)) {
			new Exception("Dao implementation of class " + this.getClass() + " has duplicated EntityMember (name=\"" + entityMember.getName() + "\").")
					.printStackTrace();
			return;
		}
		this.getEntityMembers().add(entityMember);
	}

	/**
	 * postgreSQL equivalent type : integer
	 * 
	 * @param name
	 * @param valueGetter
	 * @param valueSetter
	 * @throws SQLException
	 */
	public void addIntegerEntityMember(String name,
			EntityMemberGetter<Entity, Integer> valueGetter,
			EntityMemberSetter<Entity, Integer> valueSetter,
			boolean isPrimaryKey) throws SQLException {

		EntityMember<Entity, Integer> entityMember = new EntityMember<Entity, Integer>(
				name,
				valueGetter,
				valueSetter,
				ResultSet::getInt,
				PreparedStatement::setInt);

		this.addEntityMember(entityMember);

		if (isPrimaryKey) {
			if (hasPrimaryKey()) {
				throw new SQLException("A primary key called " + name + " can not be added because a other called " + getPrimaryKeyEntityMember().getName() + " is already defined.");
			}
			setIntegerPrimaryKeyEntityMember(entityMember);
		}
	}

	public void addIntegerEntityMember(String name,
			EntityMemberGetter<Entity, Integer> valueGetter,
			EntityMemberSetter<Entity, Integer> valueSetter) throws SQLException {
		addIntegerEntityMember(name, valueGetter, valueSetter, false);
	}

	/**
	 * postgreSQL equivalent type : boolean
	 * 
	 * @param name
	 * @param valueGetter
	 * @param valueSetter
	 * @throws SQLException
	 */
	public void addBooleanEntityMember(String name,
			EntityMemberGetter<Entity, Boolean> valueGetter,
			EntityMemberSetter<Entity, Boolean> valueSetter) throws SQLException {

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
	 * @throws SQLException
	 */
	public void addByteaEntityMember(String name,
			EntityMemberGetter<Entity, byte[]> valueGetter,
			EntityMemberSetter<Entity, byte[]> valueSetter) throws SQLException {

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
	 * @throws SQLException
	 */
	public void addByteEntityMember(String name,
			EntityMemberGetter<Entity, Byte> valueGetter,
			EntityMemberSetter<Entity, Byte> valueSetter) throws SQLException {

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
	 * @throws SQLException
	 */
	public void addDoubleEntityMember(String name,
			EntityMemberGetter<Entity, Double> valueGetter,
			EntityMemberSetter<Entity, Double> valueSetter) throws SQLException {

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
	 * @throws SQLException
	 */
	public void addFloatEntityMember(String name,
			EntityMemberGetter<Entity, Float> valueGetter,
			EntityMemberSetter<Entity, Float> valueSetter) throws SQLException {

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
	 * @throws SQLException
	 */
	public void addLongEntityMember(String name,
			EntityMemberGetter<Entity, Long> valueGetter,
			EntityMemberSetter<Entity, Long> valueSetter,
			boolean isPrimaryKey) throws SQLException {

		EntityMember<Entity, Long> entityMember = new EntityMember<Entity, Long>(
				name,
				valueGetter,
				valueSetter,
				ResultSet::getLong,
				PreparedStatement::setLong);

		this.addEntityMember(entityMember);

		if (isPrimaryKey) {
			if (hasPrimaryKey()) {
				throw new SQLException("A primary key called " + name + " can not be added because a other called " + getPrimaryKeyEntityMember().getName() + " is already defined.");
			}
			setLongPrimaryKeyEntityMember(entityMember);
		}
	}

	public void addLongEntityMember(String name,
			EntityMemberGetter<Entity, Long> valueGetter,
			EntityMemberSetter<Entity, Long> valueSetter) throws SQLException {

		addLongEntityMember(name, valueGetter, valueSetter, false);
	}

	/**
	 * postgreSQL equivalent type : smallint
	 * 
	 * @param name
	 * @param valueGetter
	 * @param valueSetter
	 * @throws SQLException
	 */
	public void addShortEntityMember(String name,
			EntityMemberGetter<Entity, Short> valueGetter,
			EntityMemberSetter<Entity, Short> valueSetter) throws SQLException {

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
	 * @throws SQLException
	 */
	public void addStringEntityMember(String name,
			EntityMemberGetter<Entity, String> valueGetter,
			EntityMemberSetter<Entity, String> valueSetter,
			boolean isPrimaryKey) throws SQLException {

		EntityMember<Entity, String> entityMember = new EntityMember<Entity, String>(
				name,
				valueGetter,
				valueSetter,
				ResultSet::getString,
				PreparedStatement::setString);

		this.addEntityMember(entityMember);

		if (isPrimaryKey) {
			if (hasPrimaryKey()) {
				throw new SQLException("A primary key called " + name + " can not be added because a other called " + getPrimaryKeyEntityMember().getName() + " is already defined.");
			}
			setStringPrimaryKeyEntityMember(entityMember);
		}
	}

	public void addStringEntityMember(String name,
			EntityMemberGetter<Entity, String> valueGetter,
			EntityMemberSetter<Entity, String> valueSetter) throws SQLException {

		addStringEntityMember(name, valueGetter, valueSetter, false);
	}

	/**
	 * postgreSQL equivalent type : timestamp with time zone
	 * 
	 * @param name
	 * @param valueGetter
	 * @param valueSetter
	 * @throws SQLException
	 */
	public void addDateEntityMember(String name,
			EntityMemberGetter<Entity, Date> valueGetter,
			EntityMemberSetter<Entity, Date> valueSetter) throws SQLException {

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
	 * @throws SQLException
	 */
	public void addInstantEntityMember(String name,
			EntityMemberGetter<Entity, Instant> valueGetter,
			EntityMemberSetter<Entity, Instant> valueSetter) throws SQLException {

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
	 * @throws SQLException
	 */
	public <ReferencedEntitySubclass> void addForeingKeyEntityMember(String name,
			EntityMemberGetter<Entity, ReferencedEntitySubclass> valueGetter,
			EntityMemberSetter<Entity, ReferencedEntitySubclass> valueSetter,
			DaoImpl<ReferencedEntitySubclass> referencedEntityDaoImpl) throws SQLException {

		this.addEntityMember(new EntityMember<Entity, ReferencedEntitySubclass>(
				name,
				valueGetter,
				valueSetter,
				(resultSet, thisName) -> {
					if (referencedEntityDaoImpl.isPrimaryKeyInteger()) {
						Integer nonNullPrimaryKey = referencedEntityDaoImpl.getIntegerPrimaryKeyEntityMember().getResultSetNonNullValueGetter().getNonNullValue(resultSet, thisName);
						// the wasNull() check is done here to avoid and unnecessary getByPk(0) call
						if (resultSet.wasNull()) {
							return null;
						} else {
							return referencedEntityDaoImpl.getByPk(nonNullPrimaryKey);
						}
					}
					if (referencedEntityDaoImpl.isPrimaryKeyLong()) {
						Long nonNullPrimaryKey = referencedEntityDaoImpl.getLongPrimaryKeyEntityMember().getResultSetNonNullValueGetter().getNonNullValue(resultSet, thisName);
						// the wasNull() check is done here to avoid and unnecessary getByPk(0) call
						if (resultSet.wasNull()) {
							return null;
						} else {
							return referencedEntityDaoImpl.getByPk(nonNullPrimaryKey);
						}
					}
					if (referencedEntityDaoImpl.isPrimaryKeyString()) {
						String nonNullPrimaryKey = referencedEntityDaoImpl.getStringPrimaryKeyEntityMember().getResultSetNonNullValueGetter().getNonNullValue(resultSet, thisName);
						return referencedEntityDaoImpl.getByPk(nonNullPrimaryKey);
					}
					throw new SQLException("Not all primary key types are handeled in addForeingKeyEntityMember()");
				},
				(preparedStatement, parameterIndex, value) -> referencedEntityDaoImpl.getPrimaryKeyEntityMember().setValueOfEntityInPreparedStatement(preparedStatement, parameterIndex, value)) {

			public void setValueOfResultSetInEntity(Entity entity, ResultSet resultSet) throws SQLException {

				ReferencedEntitySubclass value = this.getResultSetNonNullValueGetter().getNonNullValue(
						resultSet,
						this.getName());
				// no need to re-check the wasNull() because it is done by the
				// ResultSetNonNullValueGetter
				this.setValue(entity, value);
			}

		});

	}

}
