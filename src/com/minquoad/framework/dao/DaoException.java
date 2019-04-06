package com.minquoad.framework.dao;

public class DaoException extends RuntimeException {

	public DaoException(String string) {
		super(string);
	}

	public DaoException(Throwable cause) {
		super(cause);
	}

}
