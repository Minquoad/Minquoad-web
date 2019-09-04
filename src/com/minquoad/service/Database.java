package com.minquoad.service;

import java.sql.Connection;
import java.sql.SQLException;

import javax.servlet.ServletContext;

import com.jolbox.bonecp.BoneCP;
import com.jolbox.bonecp.BoneCPConfig;
import com.minquoad.dao.interfaces.DaoFactory;
import com.minquoad.dao.sqlImpl.DaoFactoryImpl;
import com.minquoad.tool.Pool;

public class Database {

	public static final String DATABASE_PROTOCOL_NAME = "jdbc";
	public static final String DATABASE_SUBPROTOCOL_NAME = "postgresql";

	public static final int CONNECTIONS_PER_PARTITION_MIN = 1;
	public static final int CONNECTIONS_PER_PARTITION_MAX = 64;
	public static final int PARTITION_NUMBER = 2;

	private final ServletContext servletContext;

	private BoneCP connectionPool;

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
			Class.forName("org.postgresql.Driver");

			Deployment deployment = ServicesManager.getService(servletContext, Deployment.class);

			BoneCPConfig config = new BoneCPConfig();

			config.setJdbcUrl(getDatabaseUrl());
			config.setUsername(deployment.getDatabaseUser());
			config.setPassword(deployment.getDatabasePassword());

			config.setMinConnectionsPerPartition(CONNECTIONS_PER_PARTITION_MIN);
			config.setMaxConnectionsPerPartition(CONNECTIONS_PER_PARTITION_MAX);
			config.setPartitionCount(PARTITION_NUMBER);

			connectionPool = new BoneCP(config);

		} catch (Exception e) {
			e.printStackTrace();
			ServicesManager.getService(servletContext, Logger.class).logError(e);
		}
	}

	public Connection getConnection() throws SQLException {
		return connectionPool.getConnection();
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
