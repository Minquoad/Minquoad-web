package com.minquoad.service;

import java.sql.Connection;
import java.sql.SQLException;

import javax.servlet.ServletContext;

import org.apache.tomcat.jdbc.pool.DataSource;
import org.postgresql.Driver;

import com.minquoad.dao.interfaces.DaoFactory;
import com.minquoad.dao.sqlImpl.DaoFactoryImpl;
import com.minquoad.tool.Pool;
import com.minquoad.tool.Pool.ReusableObjectFactory;

public class Database {

	public static final String DATABASE_PROTOCOL_NAME = "jdbc";
	public static final String DATABASE_SUBPROTOCOL_NAME = "postgresql";

	private final ServletContext servletContext;

	private DataSource dataSource;

	private Pool<DaoFactory> daoFactoryPool;

	public Database(ServletContext servletContext) {
		this.servletContext = servletContext;

		dataSource = createDataSource();
		daoFactoryPool = createDaoFactoryPool();
	}

	private DataSource createDataSource() {
		Deployment deployment = ServicesManager.getService(servletContext, Deployment.class);

		DataSource dataSource = new DataSource();

		dataSource.setDriverClassName(Driver.class.getName());
		dataSource.setUrl(getDatabaseUrl());
		dataSource.setUsername(deployment.getDatabaseUser());
		dataSource.setPassword(deployment.getDatabasePassword());
		dataSource.setInitialSize(4);
		dataSource.setMinIdle(4);
		dataSource.setMaxIdle(32);
		dataSource.setMaxActive(64);

		try {
			dataSource.getConnection();
		} catch (Exception e) {
			ServicesManager.getService(servletContext, Logger.class).logError(e);
			throw new RuntimeException(e);
		}

		return dataSource;
	}

	private Pool<DaoFactory> createDaoFactoryPool() {

		ReusableObjectFactory<DaoFactory> factory = new ReusableObjectFactory<DaoFactory>() {

			@Override
			public DaoFactory construct() {
				return new DaoFactoryImpl(servletContext);
			}

			@Override
			public void clean(DaoFactory daoFactory) {
				((DaoFactoryImpl) daoFactory).clear();
			}

			@Override
			public void destruct(DaoFactory daoFactory) {
			}
		};

		return new Pool<DaoFactory>(factory);
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
		dataSource.close();
	}

	public DaoFactory pickOneDaoFactory() {
		return daoFactoryPool.pickOne();
	}

	public void giveBack(DaoFactory daoFactory) {
		daoFactoryPool.giveBack(daoFactory);
	}

	public void clear() {
		dataSource.close();
		dataSource = createDataSource();

		daoFactoryPool.clear();
	}

	public int getDatabaseConnectionPoolSize() {
		return dataSource.getSize();
	}

	public int getDaoFactoryPoolSize() {
		return daoFactoryPool.getSize();
	}

}
