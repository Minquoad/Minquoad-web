package com.minquoad.service;

import java.sql.Connection;
import java.sql.SQLException;

import javax.servlet.ServletContext;

import com.jolbox.bonecp.BoneCP;
import com.jolbox.bonecp.BoneCPConfig;
import com.minquoad.dao.interfaces.DaoFactory;
import com.minquoad.dao.sqlImpl.DaoFactoryImpl;

public class Database {

	public static final String DATABASE_PROTOCOL_NAME = "jdbc";
	public static final String DATABASE_SUBPROTOCOL_NAME = "postgresql";

	public static final int CONNECTIONS_PER_PARTITION_MIN = 1;
	public static final int CONNECTIONS_PER_PARTITION_MAX = 64;
	public static final int PARTITION_COUNT = 2;

	private final ServletContext servletContext;

	private BoneCP connectionPool;

	public Database(ServletContext servletContext) {
		this.servletContext = servletContext;
		Deployment deployment = (Deployment) servletContext.getAttribute(Deployment.DEPLOYMENT_KEY);

		try {
			Class.forName("org.postgresql.Driver");

			BoneCPConfig config = new BoneCPConfig();

			config.setJdbcUrl(getDatabaseUrl());
			config.setUsername(deployment.getDatabaseUser());
			config.setPassword(deployment.getDatabasePassword());

			config.setMinConnectionsPerPartition(CONNECTIONS_PER_PARTITION_MIN);
			config.setMaxConnectionsPerPartition(CONNECTIONS_PER_PARTITION_MAX);
			config.setPartitionCount(PARTITION_COUNT);

			connectionPool = new BoneCP(config);

		} catch (Exception e) {
			e.printStackTrace();
			Logger logger = (Logger) servletContext.getAttribute(Deployment.LOGGER_KEY);
			logger.logError(e);
		}
	}

	public Connection getConnection() throws SQLException {
        return connectionPool.getConnection();
    }

	public String getDatabaseUrl() {
		Deployment deployment = (Deployment) servletContext.getAttribute(Deployment.DEPLOYMENT_KEY);
		return DATABASE_PROTOCOL_NAME + ":" + DATABASE_SUBPROTOCOL_NAME + "://" + deployment.getDatabaseHost() + ":" + deployment.getDatabasePort() + "/" + deployment.getDatabaseName();
	}

	public void close() {
	}

	public DaoFactory getNewDaoFactory() {
		return DaoFactoryImpl.getNewDaoFactory(this);
	}

}
