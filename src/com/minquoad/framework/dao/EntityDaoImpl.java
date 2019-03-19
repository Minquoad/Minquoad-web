package com.minquoad.framework.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import com.minquoad.framework.dao.entityMember.EntityMember;
import com.minquoad.framework.dao.entityMember.EntityMemberGetter;
import com.minquoad.framework.dao.entityMember.EntityMemberSetter;

public abstract class EntityDaoImpl<EntitySubclass extends Entity> {

	public static final String idKey = "id";

	private List<EntityMember<EntitySubclass, ?>> entityMembers;
	private EntityMember<EntitySubclass, ?> idEntityMember;

	private EntityDaoInventory<EntitySubclass> inventory;

	public abstract void initEntityMembers();

	public abstract PreparedStatement prepareStatement(String statement) throws SQLException;

	public abstract EntitySubclass instantiateBlank();

	public abstract String getTableName();

	private String[] getColumnNames(boolean withId) {
		int columnsCounts = getEntityMembers().size();
		if (!withId) {
			columnsCounts--;
		}
		String[] columnNames = new String[columnsCounts];
		int i = 0;
		for (EntityMember<EntitySubclass, ?> entityMember : getEntityMembers()) {
			if (withId || entityMember != getIdEntityMember()) {
				columnNames[i] = entityMember.getName();
				i++;
			}
		}
		return columnNames;
	}

	public EntitySubclass getById(Integer id) {
		if (id != null) {
			EntitySubclass entity = getInventory().get(id);
			if (entity != null) {
				return entity;
			} else {
				try {
					String query = SqlQueryGenerator.generateSelectQuery(getTableName(), SqlQueryGenerator.all(),
							SqlQueryGenerator.toArray("id"), true);
					PreparedStatement preparedStatement = prepareStatement(query);
					preparedStatement.setInt(1, id);
					ResultSet resultSet = preparedStatement.executeQuery();

					if (resultSet.next()) {
						entity = instantiateBlank();
						this.hydrate(entity, resultSet);
						getInventory().put(entity);
						return entity;
					}

				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}

	public boolean persist(EntitySubclass entity) {
		if (entity != null) {
			if (entity.getId() == null || getInventory().get(entity.getId()) == null) {
				return this.insert(entity);
			} else {
				return this.update(entity);
			}
		}
		return false;
	}

	public boolean insert(EntitySubclass entity) {
		if (entity != null) {
			try {
				boolean idNull = entity.getId() == null;

				String query = SqlQueryGenerator.generateInsertQuery(getTableName(), getColumnNames(!idNull),
						SqlQueryGenerator.all());
				PreparedStatement preparedStatement = prepareStatement(query);

				int i = 1;
				for (EntityMember<EntitySubclass, ?> entityMember : getEntityMembers()) {
					if (!idNull || entityMember != getIdEntityMember()) {
						entityMember.setValueOfEntityInPreparedStatement(preparedStatement, i, entity);
						i++;
					}
				}

				ResultSet resultSet = preparedStatement.executeQuery();
				resultSet.next();
				this.hydrate(entity, resultSet);

				getInventory().put(entity);

				return true;
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	public boolean delete(EntitySubclass entity) {
		if (entity != null) {
			getInventory().delete(entity);
			try {
				String query = SqlQueryGenerator.generateDeleteQuery(getTableName(), SqlQueryGenerator.toArray("id"));
				PreparedStatement preparedStatement = prepareStatement(query);
				preparedStatement.setInt(1, entity.getId());

				return 1 == preparedStatement.executeUpdate();

			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	public boolean update(EntitySubclass entity) {
		if (entity != null) {
			try {
				String query = SqlQueryGenerator.generateUpdateQuery(getTableName(), getColumnNames(false),
						SqlQueryGenerator.toArray("id"));
				PreparedStatement preparedStatement = prepareStatement(query);

				int i = 1;
				for (EntityMember<EntitySubclass, ?> entityMember : getEntityMembers()) {
					if (entityMember != getIdEntityMember()) {
						entityMember.setValueOfEntityInPreparedStatement(preparedStatement, i, entity);
						i++;
					}
				}

				preparedStatement.setInt(i, entity.getId());

				return 1 == preparedStatement.executeUpdate();

			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	public List<EntitySubclass> getAll() {
		return this.getAllMatching(new EntityCriterion[0]);
	}

	public List<EntitySubclass> getAllMatching(Object value, String memberName) {
		return getAllMatching(new EntityCriterion(value, memberName));
	}

	public List<EntitySubclass> getAllMatching(EntityCriterion criterion) {
		return getAllMatching(criterion.toArray());
	}

	public List<EntitySubclass> getAllMatching(EntityCriterion[] criteria) {
		try {
			String[] whereColumns = new String[criteria.length];
			for (int i = 0; i < whereColumns.length; i++) {
				whereColumns[i] = criteria[i].getName();
			}
			String query = SqlQueryGenerator.generateSelectQuery(getTableName(), SqlQueryGenerator.all(), whereColumns);

			PreparedStatement preparedStatement = prepareStatement(query);

			int i = 1;
			for (EntityCriterion criterion : criteria) {
				for (EntityMember<EntitySubclass, ?> entityMember : getEntityMembers()) {

					if (entityMember.getName() == criterion.getName()) {
						entityMember.setValueOfCriterionInPreparedStatement(preparedStatement, i, criterion);
						i++;
					}
				}
			}

			ResultSet resultSet = preparedStatement.executeQuery();

			List<EntitySubclass> entities = new ArrayList<EntitySubclass>();
			while (resultSet.next()) {
				entities.add(toUnifiedInstance(resultSet));
			}
			return entities;

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return null;
	}

	public EntitySubclass getOneMatching(Object value, String memberName) {
		return getOneMatching(new EntityCriterion(value, memberName));
	}

	public EntitySubclass getOneMatching(EntityCriterion criterion) {
		return getOneMatching(criterion.toArray());
	}

	public EntitySubclass getOneMatching(EntityCriterion[] criteria) {
		try {
			for (EntitySubclass instantiatedEntity : getInstantiatedEntyties()) {
				boolean isMatching = true;
				for (EntityCriterion criterion : criteria) {
					for (EntityMember<EntitySubclass, ?> member : getEntityMembers()) {
						if (criterion.getName().equals(member.getName())) {
							if (criterion.getValue() == null) {
								if (member.getValue(instantiatedEntity) != null) {
									isMatching = false;
								}
							} else {
								if (!criterion.getValue().equals(member.getValue(instantiatedEntity))) {
									isMatching = false;
								}
							}
						}
					}
				}
				if (isMatching) {
					return instantiatedEntity;
				}
			}

			String[] whereColumns = new String[criteria.length];
			for (int i = 0; i < whereColumns.length; i++) {
				whereColumns[i] = criteria[i].getName();
			}
			String query = SqlQueryGenerator.generateSelectQuery(getTableName(), SqlQueryGenerator.all(), whereColumns, true);

			PreparedStatement preparedStatement = prepareStatement(query);

			int i = 1;
			for (EntityCriterion criterion : criteria) {
				for (EntityMember<EntitySubclass, ?> entityMember : getEntityMembers()) {

					if (entityMember.getName() == criterion.getName()) {
						entityMember.setValueOfCriterionInPreparedStatement(preparedStatement, i, criterion);
						i++;
					}
				}
			}

			ResultSet resultSet = preparedStatement.executeQuery();

			List<EntitySubclass> entities = new ArrayList<EntitySubclass>();
			while (resultSet.next()) {
				entities.add(toUnifiedInstance(resultSet));
			}

			if (entities.size() == 0) {
				return null;
			} else {
				return entities.get(0);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return null;
	}

	public void hydrate(EntitySubclass entity, ResultSet resultSet) throws SQLException {
		for (EntityMember<EntitySubclass, ?> entityMember : getEntityMembers()) {
			entityMember.setValueOfResultSetInEntity(entity, resultSet);
		}
	}

	private EntitySubclass toUnifiedInstance(ResultSet resultSet) throws SQLException {
		EntitySubclass entity = getInventory().get(resultSet.getInt(getIdEntityMember().getName()));
		if (entity == null) {
			entity = instantiateBlank();
			this.hydrate(entity, resultSet);
			getInventory().put(entity);
		}
		return entity;
	}

	private EntityDaoInventory<EntitySubclass> getInventory() {
		if (inventory == null) {
			inventory = new EntityDaoInventory<EntitySubclass>();
		}
		return inventory;
	}

	private List<EntityMember<EntitySubclass, ?>> getEntityMembers() {
		if (entityMembers == null) {
			entityMembers = new ArrayList<EntityMember<EntitySubclass, ?>>();

			this.addIntegerEntityMember(idKey, Entity::getId, Entity::setId);

			this.initEntityMembers();
		}
		return entityMembers;
	}

	protected Collection<EntitySubclass> getInstantiatedEntyties() {
		return this.getInventory().values();
	}

	public EntityMember<EntitySubclass, ?> getIdEntityMember() {
		if (idEntityMember == null) {
			for (EntityMember<EntitySubclass, ?> entityMember : getEntityMembers()) {
				if (entityMember.getName().equals(idKey)) {
					idEntityMember = entityMember;
				}
			}
		}
		return idEntityMember;
	}

	// EntityMember adders

	public void addEntityMember(EntityMember<EntitySubclass, ?> entityMember) {
		for (EntityMember<EntitySubclass, ?> entityMemberInList : getEntityMembers()) {
			if (entityMemberInList.getName().equals(entityMember.getName())) {
				new Exception("Dao implementation of class " + this.getClass() + " has duplicated EntityMember name.")
						.printStackTrace();
			}
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
			EntityMemberGetter<EntitySubclass, Integer> valueGetter,
			EntityMemberSetter<EntitySubclass, Integer> valueSetter) {

		this.addEntityMember(new EntityMember<EntitySubclass, Integer>(
				name,
				valueGetter,
				valueSetter,
				ResultSet::getInt,
				PreparedStatement::setInt));
	}

	/**
	 * postgreSQL equivalent type : boolean
	 * 
	 * @param name
	 * @param valueGetter
	 * @param valueSetter
	 */
	public void addBooleanEntityMember(String name,
			EntityMemberGetter<EntitySubclass, Boolean> valueGetter,
			EntityMemberSetter<EntitySubclass, Boolean> valueSetter) {

		this.addEntityMember(new EntityMember<EntitySubclass, Boolean>(
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
			EntityMemberGetter<EntitySubclass, byte[]> valueGetter,
			EntityMemberSetter<EntitySubclass, byte[]> valueSetter) {

		this.addEntityMember(new EntityMember<EntitySubclass, byte[]>(
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
			EntityMemberGetter<EntitySubclass, Byte> valueGetter,
			EntityMemberSetter<EntitySubclass, Byte> valueSetter) {

		this.addEntityMember(new EntityMember<EntitySubclass, Byte>(
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
			EntityMemberGetter<EntitySubclass, Double> valueGetter,
			EntityMemberSetter<EntitySubclass, Double> valueSetter) {

		this.addEntityMember(new EntityMember<EntitySubclass, Double>(
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
			EntityMemberGetter<EntitySubclass, Float> valueGetter,
			EntityMemberSetter<EntitySubclass, Float> valueSetter) {

		this.addEntityMember(new EntityMember<EntitySubclass, Float>(
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
			EntityMemberGetter<EntitySubclass, Long> valueGetter,
			EntityMemberSetter<EntitySubclass, Long> valueSetter) {

		this.addEntityMember(new EntityMember<EntitySubclass, Long>(
				name,
				valueGetter,
				valueSetter,
				ResultSet::getLong,
				PreparedStatement::setLong));
	}

	/**
	 * postgreSQL equivalent type : smallint
	 * 
	 * @param name
	 * @param valueGetter
	 * @param valueSetter
	 */
	public void addShortEntityMember(String name,
			EntityMemberGetter<EntitySubclass, Short> valueGetter,
			EntityMemberSetter<EntitySubclass, Short> valueSetter) {

		this.addEntityMember(new EntityMember<EntitySubclass, Short>(
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
			EntityMemberGetter<EntitySubclass, String> valueGetter,
			EntityMemberSetter<EntitySubclass, String> valueSetter) {

		this.addEntityMember(new EntityMember<EntitySubclass, String>(
				name,
				valueGetter,
				valueSetter,
				ResultSet::getString,
				PreparedStatement::setString));
	}

	/**
	 * postgreSQL equivalent type : timestamp with time zone
	 * 
	 * @param name
	 * @param valueGetter
	 * @param valueSetter
	 */
	public void addDateEntityMember(String name,
			EntityMemberGetter<EntitySubclass, Date> valueGetter,
			EntityMemberSetter<EntitySubclass, Date> valueSetter) {

		this.addEntityMember(new EntityMember<EntitySubclass, Date>(
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
			EntityMemberGetter<EntitySubclass, Instant> valueGetter,
			EntityMemberSetter<EntitySubclass, Instant> valueSetter) {

		this.addEntityMember(new EntityMember<EntitySubclass, Instant>(
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
	public <ReferencedEntitySubclass extends Entity> void addForeingKeyEntityMember(String name,
			EntityMemberGetter<EntitySubclass, ReferencedEntitySubclass> valueGetter,
			EntityMemberSetter<EntitySubclass, ReferencedEntitySubclass> valueSetter,
			EntityDaoImpl<ReferencedEntitySubclass> referencedEntityDaoImpl) {

		this.addEntityMember(new EntityMember<EntitySubclass, ReferencedEntitySubclass>(
				name,
				valueGetter,
				valueSetter,
				(resultSet, thisName) -> referencedEntityDaoImpl.getById(resultSet.getInt(thisName)),
				(preparedStatement, parameterIndex, value) -> preparedStatement.setInt(parameterIndex, value.getId())));

	}

}
