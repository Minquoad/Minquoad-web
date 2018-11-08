package daos.sqlImpls;

import daos.interfaces.DaoFactory;

public class DaoFactoryImpl implements DaoFactory {

	private ThingDaoImpl thingDao;
	public ThingDaoImpl getThingDao() {
		if (thingDao == null) {
			thingDao = new ThingDaoImpl(this);
		}
		return thingDao;
	}

	private UserDaoImpl userDao;
	public UserDaoImpl getUserDao() {
		if (userDao == null) {
			userDao = new UserDaoImpl();
		}
		return userDao;
	}

}
