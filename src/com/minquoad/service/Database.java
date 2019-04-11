package com.minquoad.service;

import java.sql.Connection;
import java.sql.SQLException;

import javax.servlet.ServletContext;

import com.jolbox.bonecp.BoneCP;
import com.jolbox.bonecp.BoneCPConfig;
import com.minquoad.dao.interfaces.DaoFactory;
import com.minquoad.dao.sqlImpl.DaoFactoryImpl;

public class Database {

	public static final String dataBaseProtocol = "jdbc";
	public static final String dataBaseSubprotocol = "postgresql";

	public static final int minConnectionsPerPartition = 1;
	public static final int maxConnectionsPerPartition = 64;
	public static final int partitionCount = 2;

	private final ServletContext servletContext;

	private BoneCP connectionPool;

	public Database(ServletContext servletContext) {
		this.servletContext = servletContext;
		Deployment deployment = (Deployment) servletContext.getAttribute(Deployment.deploymentKey);

		try {
			Class.forName("org.postgresql.Driver");

			BoneCPConfig config = new BoneCPConfig();

			config.setJdbcUrl(getDatabaseUrl());
			config.setUsername(deployment.getDatabaseUser());
			config.setPassword(deployment.getDatabasePassword());

			config.setMinConnectionsPerPartition(minConnectionsPerPartition);
			config.setMaxConnectionsPerPartition(maxConnectionsPerPartition);
			config.setPartitionCount(partitionCount);

			connectionPool = new BoneCP(config);

		} catch (Exception e) {
			e.printStackTrace();
			Logger logger = (Logger) servletContext.getAttribute(Deployment.loggerKey);
			logger.logError(e);
		}
	}

	public Connection getConnection() throws SQLException {
        return connectionPool.getConnection();
    }

	public String getDatabaseUrl() {
		Deployment deployment = (Deployment) servletContext.getAttribute(Deployment.deploymentKey);
		return dataBaseProtocol + ":" + dataBaseSubprotocol + "://" + deployment.getDatabaseHost() + ":" + deployment.getDatabasePort() + "/" + deployment.getDatabaseName();
	}

	public void close() {
	}

	public DaoFactory getNewDaoFactory() {
		return DaoFactoryImpl.getNewDaoFactory(this);
	}

}
