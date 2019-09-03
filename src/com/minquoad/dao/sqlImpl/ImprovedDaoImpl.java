package com.minquoad.dao.sqlImpl;

import java.sql.Connection;
import java.sql.SQLException;

import com.minquoad.framework.dao.DaoImpl;

public abstract class ImprovedDaoImpl<EntitySubclass> extends DaoImpl<EntitySubclass> {

	private String tableName;

	private DaoFactoryImpl daoFactory;

	public ImprovedDaoImpl(DaoFactoryImpl daoFactory) {
		//this.addStatementListener((statement) -> System.out.println(statement));
		this.daoFactory = daoFactory;
		this.tableName = this.instantiateBlank().getClass().getSimpleName();
	}	

	@Override
	protected Connection getConnection() throws SQLException {
		return getDaoFactory().getConnection();
	}

	@Override
	protected String getTableName() {
		return tableName;
	}

	protected DaoFactoryImpl getDaoFactory() {
		return daoFactory;
	}

}
