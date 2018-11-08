package frameworks.daos;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import frameworks.daos.entityMembers.EntityMember;
import frameworks.daos.entityMembers.IntEntityMember;

public abstract class EntityDaoImpl<EntitySubclass extends Entity> implements EntityDao<EntitySubclass> {

	private List<EntityMember<EntitySubclass, ?>> entityMembers = new ArrayList<EntityMember<EntitySubclass, ?>>();

	private EntityDaoInventory<EntitySubclass> inventory = new EntityDaoInventory<EntitySubclass>();

	private static final String idKey = "id";

	public EntityDaoImpl() {
		addEntityMember(new IntEntityMember<EntitySubclass>() {
			public String getName() {
				return idKey;
			}

			public void setValue(EntitySubclass entity, Integer string) {
				entity.setId(string);
			}

			public Integer getValue(EntitySubclass entity) {
				return entity.getId();
			}
		});
		initEntityMembers();
	}

	public abstract void initEntityMembers();

	public abstract PreparedStatement prepareStatement(String statement) throws SQLException;

	public abstract EntitySubclass instantiateBlank();

	public abstract String getTableName();

	protected List<EntityMember<EntitySubclass, ?>> getEntityMembers() {
		return this.entityMembers;
	}

	public void addEntityMember(EntityMember<EntitySubclass, ?> entityMember) {
		for (EntityMember<EntitySubclass, ?> entityMemberInList : entityMembers) {
			if (entityMemberInList.getName().equals(entityMember.getName())) {
				new Exception("Dao implementation of class " + this.getClass() + "has duplicated EntityMember name.")
						.printStackTrace();
			}
		}
		this.entityMembers.add(entityMember);
	}

	protected EntitySubclass toUnifiedInstance(ResultSet resultSet) throws SQLException {
		EntitySubclass entity = inventory.get(resultSet.getInt(idKey));
		if (entity == null) {
			entity = instantiateBlank();
			this.hydrate(entity, resultSet);
			inventory.put(entity);
		}
		return entity;
	}

	private void hydrate(EntitySubclass entity, ResultSet resultSet) throws SQLException {
		for (EntityMember<EntitySubclass, ?> entityMember : entityMembers) {
			entityMember.setValue(entity, resultSet);
		}
	}

	protected String[] getColumnNames(boolean withId) {
		int columnsCounts = entityMembers.size();
		if (!withId) {
			columnsCounts--;
		}
		String[] columnNames = new String[columnsCounts];
		int i = 0;
		for (EntityMember<EntitySubclass, ?> entityMember : entityMembers) {
			String name = entityMember.getName();
			if (withId || name != idKey) {
				columnNames[i] = name;
				i++;
			}
		}
		return columnNames;
	}

	@Override
	public EntitySubclass getById(Integer id) {
		if (id != null) {
			EntitySubclass entity = inventory.get(id);
			if (entity != null) {
				return entity;
			} else {
				try {
					String query = SqlQueryGenerator.generateSelectQuery(getTableName(), SqlQueryGenerator.all(),
							SqlQueryGenerator.toArray("id"));
					PreparedStatement preparedStatement = prepareStatement(query);
					preparedStatement.setInt(1, id);
					ResultSet resultSet = preparedStatement.executeQuery();

					if (resultSet.next()) {
						entity = instantiateBlank();
						this.hydrate(entity, resultSet);
						inventory.put(entity);
						return entity;
					}

				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}

	@Override
	public List<EntitySubclass> getAll() {
		return this.getAllMatching(new EntityCriterion[0]);
	}

	@Override
	public boolean insert(EntitySubclass entity) {
		try {
			String query = SqlQueryGenerator.generateInsertQuery(getTableName(), getColumnNames(false),
					SqlQueryGenerator.all());
			PreparedStatement preparedStatement = prepareStatement(query);

			int i = 1;
			for (EntityMember<EntitySubclass, ?> entityMember : entityMembers) {
				if (entityMember.getName() != idKey) {
					entityMember.setValue(preparedStatement, i, entity);
					i++;
				}
			}

			ResultSet resultSet = preparedStatement.executeQuery();
			resultSet.next();
			this.hydrate(entity, resultSet);

			inventory.put(entity);

			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public boolean delete(EntitySubclass entity) {
		inventory.delete(entity);
		try {
			String query = SqlQueryGenerator.generateDeleteQuery(getTableName(), SqlQueryGenerator.toArray("id"));
			PreparedStatement preparedStatement = prepareStatement(query);
			preparedStatement.setInt(1, entity.getId());

			return 1 == preparedStatement.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public boolean update(EntitySubclass entity) {
		try {
			String query = SqlQueryGenerator.generateUpdateQuery(getTableName(), getColumnNames(false),
					SqlQueryGenerator.toArray("id"));
			PreparedStatement preparedStatement = prepareStatement(query);

			int i = 1;
			for (EntityMember<EntitySubclass, ?> entityMember : entityMembers) {
				if (entityMember.getName() != idKey) {
					entityMember.setValue(preparedStatement, i, entity);
					i++;
				}
			}

			preparedStatement.setInt(i, entity.getId());

			return 1 == preparedStatement.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
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
				for (EntityMember<EntitySubclass, ?> entityMember : entityMembers) {

					if (entityMember.getName() == criterion.getName()) {
						entityMember.setValue(preparedStatement, i, criterion);
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

}
