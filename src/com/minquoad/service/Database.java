package com.minquoad.service;

import java.sql.Connection;
import java.sql.SQLException;

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

	private BoneCP connectionPool;

	public Database() {
		try {
			Class.forName("org.postgresql.Driver");

			BoneCPConfig config = new BoneCPConfig();

			config.setJdbcUrl(getDatabaseUrl());
			config.setUsername(Deployment.getDatabaseUser());
			config.setPassword(Deployment.getDatabasePassword());

			config.setMinConnectionsPerPartition(minConnectionsPerPartition);
			config.setMaxConnectionsPerPartition(maxConnectionsPerPartition);
			config.setPartitionCount(partitionCount);

			connectionPool = new BoneCP(config);

		} catch (Exception e) {
			e.printStackTrace();
			Logger.logError(e);
		}
	}

	public Connection getConnection() throws SQLException {
        return connectionPool.getConnection();
    }

	public static String getDatabaseUrl() {
		return dataBaseProtocol + ":" + dataBaseSubprotocol + "://" + Deployment.getDatabaseHost() + ":" + Deployment.getDatabasePort() + "/" + Deployment.getDatabaseName();
	}

	public void close() {
	}

	public DaoFactory instantiateDaoFactory() {
		return new DaoFactoryImpl(this);
	}

}
