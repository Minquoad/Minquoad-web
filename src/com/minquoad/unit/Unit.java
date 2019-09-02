package com.minquoad.unit;

import javax.servlet.ServletContext;

import com.minquoad.dao.interfaces.DaoFactory;
import com.minquoad.service.Deployment;
import com.minquoad.service.ServicesManager;
import com.minquoad.service.StorageManager;

public abstract class Unit {

	private final UnitFactory unitFactory;

	public Unit(UnitFactory unitFactory) {
		this.unitFactory = unitFactory;
	}

	public UnitFactory getUnitFactory() {
		return unitFactory;
	}

	public ServletContext getServletContext() {
		return unitFactory.getServletContext();
	}

	public DaoFactory getDaoFactory() {
		return unitFactory.getDaoFactory();
	}

	public Deployment getDeployment() {
		return ServicesManager.getService(getServletContext(), Deployment.class);
	}

	public StorageManager getStorageManager() {
		return ServicesManager.getService(getServletContext(), StorageManager.class);
	}

}
