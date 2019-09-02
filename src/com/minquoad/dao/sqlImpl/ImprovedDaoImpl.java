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
	public Connection getConnection() throws SQLException {
		return getDaoFactory().getConnection();
	}

	@Override
	public String getTableName() {
		return tableName;
	}

	public DaoFactoryImpl getDaoFactory() {
		return daoFactory;
	}

}
