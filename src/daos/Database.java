package daos;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import utilities.Deployment;
import utilities.Logger;

public abstract class Database {

	private static int connectionsSize = 1;

	private static Connection[] connections;

	private static int iterator = 0;

	static {
		try {
			Class.forName("org.postgresql.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		try {
			connections = new Connection[connectionsSize];
			for (int i = 0; i < connections.length; i++) {
				connections[i] = DriverManager.getConnection(Deployment.databaseUrl, Deployment.databaseUser,
						Deployment.databasePassword);
			}
			String message = connectionsSize + " connections to " + Deployment.databaseUrl + " has been created.";
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

			connections[iterator] = DriverManager.getConnection(Deployment.databaseUrl, Deployment.databaseUser,
					Deployment.databasePassword);
			String message = "A connection to " + Deployment.databaseUrl
					+ " has been created to replace an invalid one.";
			Logger.echoInfo(message);
			Logger.logInfo(message);
			return connections[iterator].prepareStatement(statement);
		}
	}

}
