package com.minquoad.unit;

import javax.servlet.ServletContext;

import com.minquoad.dao.interfaces.DaoFactory;
import com.minquoad.service.Deployment;
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
		return (Deployment) unitFactory.getServletContext().getAttribute(Deployment.class.getName());
	}

	public StorageManager getStorageManager() {
		return (StorageManager) getServletContext().getAttribute(StorageManager.class.getName());
	}

}
