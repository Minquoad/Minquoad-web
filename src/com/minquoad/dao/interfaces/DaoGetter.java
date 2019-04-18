package com.minquoad.dao.interfaces;

public interface DaoGetter<EntitySubclass> {
	public Dao<EntitySubclass> getDao(DaoFactory daoFactory);
}
