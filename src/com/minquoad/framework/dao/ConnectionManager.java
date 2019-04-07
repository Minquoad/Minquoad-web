package com.minquoad.framework.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public abstract class ConnectionManager {

	public static void close(ResultSet resultSet, Statement statement, Connection connexion) throws SQLException {
		resultSet.close();
		close(statement, connexion);
	}

	public static void close(Statement statement, Connection connexion) throws SQLException {
		statement.close();
		connexion.close();
	}

	public static void safeClose(ResultSet resultSet, Statement statement, Connection connexion) {
		safeClose(resultSet);
		safeClose(statement, connexion);
	}

	public static void safeClose(Statement statement, Connection connexion) {
		safeClose(statement);
		safeClose(connexion);
	}

	public static void safeClose(Connection connexion) {
		if (connexion != null) {
			try {
				connexion.close();
			} catch (SQLException e) {
			}
		}
	}

	public static void safeClose(Statement statement) {
		if (statement != null) {
			try {
				statement.close();
			} catch (SQLException e) {
			}
		}
	}

	public static void safeClose(ResultSet resultSet) {
		if (resultSet != null) {
			try {
				resultSet.close();
			} catch (SQLException e) {
			}
		}
	}

}
