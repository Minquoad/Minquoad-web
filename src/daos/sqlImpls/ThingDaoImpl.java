package daos.sqlImpls;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import Entities.Thing;
import Entities.User;
import daos.Database;
import daos.interfaces.ThingDao;
import frameworks.daos.EntityDao;
import frameworks.daos.EntityDaoImpl;
import frameworks.daos.entityMembers.ForeingKeyEntityMember;
import frameworks.daos.entityMembers.StringEntityMember;

public class ThingDaoImpl extends EntityDaoImpl<Thing> implements ThingDao {

	private DaoFactoryImpl daoFactory;

	public ThingDaoImpl(DaoFactoryImpl daoFactory) {
		this.daoFactory = daoFactory;
	}

	public Thing instantiateBlank() {
		return new Thing();
	}

	public String getTableName() {
		return "Thing";
	}

	public List<Thing> getUserThings(User user) {
		return this.getAllMatching(user, "owner");
	}

	@Override
	public void initEntityMembers() {
		this.addEntityMember(new StringEntityMember<Thing>() {
			public String getName() {
				return "description";
			}

			public void setValue(Thing entity, String string) {
				entity.setDescription(string);
			}

			public String getValue(Thing entity) {
				return entity.getDescription();
			}
		});
		this.addEntityMember(new ForeingKeyEntityMember<Thing, User>() {
			public EntityDao<User> getReferencedEntityDao() {
				return daoFactory.getUserDao();
			}

			public void setValue(Thing entity, User referencedEntity) {
				entity.setOwner(referencedEntity);
			}

			public User getValue(Thing entity) {
				return entity.getOwner();
			}

			public String getName() {
				return "owner";
			}
		});
	}

	@Override
	public PreparedStatement prepareStatement(String statement) throws SQLException {
		return Database.prepareStatement(statement);
	}

}
