package dao.interfaces;

import java.util.List;

import entity.Thing;
import entity.User;

public interface ThingDao extends Dao<Thing> {
	
	public List<Thing> getUserThings(User user);
}
