package com.minquoad.framework.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public abstract class DaoFactoryImpl {

	private Map<Class<?>, DaoImpl<?>> daos;

	private ConnectionProvier connectionProvier;

	public DaoFactoryImpl(ConnectionProvier connectionProvier) {
		this.connectionProvier = connectionProvier;

		daos = new HashMap<Class<?>, DaoImpl<?>>();

		initDaos();

		for (Entry<Class<?>, DaoImpl<?>> entry : daos.entrySet()) {
			entry.getValue().initIfneeded();
		}
	}

	protected abstract void initDaos();

	public Connection getConnection() throws SQLException {
		return connectionProvier.getConnection();
	}

	protected <Entity> void addDao(Class<Entity> entityClass, DaoImpl<Entity> daoImpl) {
		daos.put(entityClass, daoImpl);
	}

	@SuppressWarnings("unchecked")
	public <Entity> DaoImpl<Entity> getDao(Class<Entity> entityClass) {
		return (DaoImpl<Entity>) daos.get(entityClass);
	}

	public void clear() {
		for (Entry<Class<?>, DaoImpl<?>> entry : daos.entrySet()) {
			entry.getValue().clear();
		}
	}

	public interface ConnectionProvier {
		public Connection getConnection() throws SQLException;
	}
}
