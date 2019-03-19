package com.minquoad.tool.http;

import com.minquoad.dao.interfaces.Dao;
import com.minquoad.dao.interfaces.DaoFactory;
import com.minquoad.framework.dao.Entity;

public interface DaoGetter<EntitySubclass extends Entity> {
	public Dao<EntitySubclass> getDao(DaoFactory daoFactory);
}
