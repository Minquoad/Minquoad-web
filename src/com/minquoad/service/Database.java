package com.minquoad.service;

import java.sql.Connection;
import java.sql.SQLException;

import javax.servlet.ServletContext;
import javax.sql.DataSource;

import com.minquoad.dao.interfaces.DaoFactory;
import com.minquoad.dao.sqlImpl.DaoFactoryImpl;
import com.minquoad.tool.Pool;

public class Database {

	public static final String DATABASE_PROTOCOL_NAME = "jdbc";
	public static final String DATABASE_SUBPROTOCOL_NAME = "postgresql";

	public static final Class<org.postgresql.Driver> DATASOURCE_DRIVER_CLASS = org.postgresql.Driver.class;

	private final ServletContext servletContext;

	private DataSource dataSource;

	private Pool<DaoFactory> daoFactoryPool;

	public Database(ServletContext servletContext) {
		this.servletContext = servletContext;

		initConnectionPool();

		daoFactoryPool = new Pool<DaoFactory>(
				() -> new DaoFactoryImpl(this),
				(daoFactory) -> ((DaoFactoryImpl) daoFactory).clear());
	}

	private void initConnectionPool() {
		try {

			Deployment deployment = ServicesManager.getService(servletContext, Deployment.class);

			org.apache.tomcat.jdbc.pool.DataSource dataSource = new org.apache.tomcat.jdbc.pool.DataSource();

			dataSource.setDriverClassName(DATASOURCE_DRIVER_CLASS.getName());
			dataSource.setUrl(getDatabaseUrl());
			dataSource.setUsername(deployment.getDatabaseUser());
			dataSource.setPassword(deployment.getDatabasePassword());
			dataSource.setInitialSize(1);
			dataSource.setMinIdle(1);
			dataSource.setMaxIdle(10);
			dataSource.setMaxActive(64);

			this.dataSource = dataSource;

		} catch (Exception e) {
			e.printStackTrace();
			ServicesManager.getService(servletContext, Logger.class).logError(e);
		}
	}

	public Connection getConnection() throws SQLException {
		return dataSource.getConnection();
	}

	public String getDatabaseUrl() {
		Deployment deployment = ServicesManager.getService(servletContext, Deployment.class);
		return DATABASE_PROTOCOL_NAME
				+ ":" + DATABASE_SUBPROTOCOL_NAME
				+ "://" + deployment.getDatabaseHost()
				+ ":" + deployment.getDatabasePort()
				+ "/" + deployment.getDatabaseName();
	}

	public void close() {
	}

	public DaoFactory pickOneDaoFactory() {
		return daoFactoryPool.pickOne();
	}

	public void giveBack(DaoFactory reusableInstance) {
		daoFactoryPool.giveBack(reusableInstance);
	}

	public void clear() {
		initConnectionPool();
		daoFactoryPool.clear();
	}

}
