package com.minquoad.framework.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public abstract class ConnectionManager {

	public static void close(Statement statement, Connection connexion) throws SQLException {
		statement.close();
		connexion.close();
	}

	public static void safeClose(Statement statement, Connection connexion) {
		safelyClose(statement);
		safelyClose(connexion);
	}

	public static void safelyClose(AutoCloseable closable) {
		try {
			closable.close();
		} catch (Exception e) {
		}
	}
	
}
