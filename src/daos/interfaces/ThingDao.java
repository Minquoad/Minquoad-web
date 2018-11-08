package daos.interfaces;

import java.util.List;

import Entities.Thing;
import Entities.User;

public interface ThingDao extends Dao<Thing> {
	
	public List<Thing> getUserThings(User user);
}
