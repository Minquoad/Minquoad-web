package com.minquoad.dao.interfaces;

import java.util.Collection;

import com.minquoad.entity.Thing;
import com.minquoad.entity.User;

public interface ThingDao extends Dao<Thing> {
	
	public Collection<Thing> getUserThings(User user);

}
