package com.minquoad.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.minquoad.dao.interfaces.DaoFactory;
import com.minquoad.dao.sqlImpl.DaoFactoryImpl;
import com.minquoad.service.Deployment;
import com.minquoad.service.Logger;

public abstract class Database {

	public static final String dataBaseProtocol = "jdbc";
	public static final String dataBaseSubprotocol = "postgresql";
	public static final int connectionsSize = 1;

	private static Connection[] connections;
	private static int iterator = 0;

	public static void init() {
		try {
			Class.forName("org.postgresql.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		try {
			connections = new Connection[connectionsSize];
			for (int i = 0; i < connections.length; i++) {
				connections[i] = getConnection();
			}
			String message = connectionsSize + " connections to " + getDatabaseUrl() + " has been created.";
			Logger.echoInfo(message);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private static void incrementConnectionIterator() {
		iterator = (iterator + 1) % connectionsSize;
	}

	public static PreparedStatement prepareStatement(String statement) throws SQLException {
		incrementConnectionIterator();
		try {

			return connections[iterator].prepareStatement(statement);

		} catch (SQLException e) {

			connections[iterator] = getConnection();
			String message = "A connection to " + getDatabaseUrl()
					+ " has been created to replace an invalid one.";
			Logger.logWarning(message);
			return connections[iterator].prepareStatement(statement);
		}
	}

	public static Connection getConnection() throws SQLException {
		return DriverManager.getConnection(
				getDatabaseUrl(),
				Deployment.getDatabaseUser(),
				Deployment.getDatabasePassword());
	}

	private static String getDatabaseUrl() {
		return dataBaseProtocol + ":" + dataBaseSubprotocol + "://" + Deployment.getDatabaseHost() + ":" + Deployment.getDatabasePort() + "/" + Deployment.getDatabaseName();
	}

	public static DaoFactory instantiateDaoFactory() {
		return new DaoFactoryImpl();
	}

}
