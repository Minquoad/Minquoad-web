package com.minquoad.dao.interfaces;

import java.util.List;

import com.minquoad.entity.Thing;
import com.minquoad.entity.User;

public interface ThingDao extends Dao<Thing> {
	
	public List<Thing> getUserThings(User user);
}
