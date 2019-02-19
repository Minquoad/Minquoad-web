package daos.sqlImpls;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import Entities.Thing;
import Entities.User;
import daos.Database;
import daos.interfaces.ThingDao;
import frameworks.daos.EntityDaoImpl;
import frameworks.daos.entityMembers.ForeingKeyEntityMember;

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

	@Override
	public void initEntityMembers() {

		this.addStringEntityMember("description", Thing::getDescription, Thing::setDescription);

		this.addEntityMember(new ForeingKeyEntityMember<Thing, User>(
				"owner",
				Thing::getOwner,
				Thing::setOwner,
				daoFactory.getUserDao()));
	}

	public List<Thing> getUserThings(User user) {
		return this.getAllMatching(user, "owner");
	}

	@Override
	public PreparedStatement prepareStatement(String statement) throws SQLException {
		return Database.prepareStatement(statement);
	}

}
