package daos.interfaces;

public interface DaoFactory {

	public ThingDao getThingDao();

	public UserDao getUserDao();

}
