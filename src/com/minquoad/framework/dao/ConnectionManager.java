package com.minquoad.framework.dao;

import java.sql.Connection;
import java.sql.SQLException;

public class ConnectionManager {

	private DaoImpl<?> dao;

	public ConnectionManager(DaoImpl<?> dao) {
		this.dao = dao;
	}

	public DaoImpl<?> getDao() {
		return dao;
	}

	private Connection getConnection() throws SQLException {
		return getDao().getConnection();
	}

	public <R> R runInSingleTransaction(ConnectionUse<R> connectionUse) {

		Connection connection = null;
		try {
			connection = getConnection();
			connection.setAutoCommit(false);

			R returnedObject = connectionUse.use(connection);

			connection.commit();
			connection.close();

			return returnedObject;

		} catch (SQLException exception) {
			try {
				connection.rollback();
			} catch (Exception e) {
			}
			try {
				connection.close();
			} catch (Exception e) {
			}
			throw new DaoException(exception);
		}
	}

	public <R> R runWithAutoCommit(ConnectionUse<R> connectionUse) {

		Connection connection = null;
		try {
			connection = getConnection();
			connection.setAutoCommit(true);

			R returnedObject = connectionUse.use(connection);

			connection.close();

			return returnedObject;

		} catch (SQLException exception) {
			try {
				connection.close();
			} catch (Exception e) {
			}
			throw new DaoException(exception);
		}
	}

	@FunctionalInterface
	public interface ConnectionUse<R> {
		public R use(Connection connection) throws SQLException;
	}

}
