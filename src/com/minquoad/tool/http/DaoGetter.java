package com.minquoad.tool.http;

import com.minquoad.dao.interfaces.Dao;
import com.minquoad.dao.interfaces.DaoFactory;

public interface DaoGetter<EntitySubclass> {
	public Dao<EntitySubclass> getDao(DaoFactory daoFactory);
}
