package com.minquoad.unit;

import com.minquoad.dao.interfaces.DaoFactory;

public abstract class Unit {

	private DaoFactory daoFactory;

	public Unit(DaoFactory daoFactory) {
		this.daoFactory = daoFactory;
	}

	public DaoFactory getDaoFactory() {
		return daoFactory;
	}

}
