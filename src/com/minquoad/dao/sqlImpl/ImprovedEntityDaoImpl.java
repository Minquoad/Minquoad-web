package com.minquoad.dao.sqlImpl;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.minquoad.dao.Database;
import com.minquoad.framework.dao.Entity;
import com.minquoad.framework.dao.DaoImpl;

public abstract class ImprovedEntityDaoImpl<EntitySubclass extends Entity> extends DaoImpl<EntitySubclass> {

	private String tableName;

	private DaoFactoryImpl daoFactory;

	public ImprovedEntityDaoImpl(DaoFactoryImpl daoFactory) {
		this.daoFactory = daoFactory;
		this.tableName = this.instantiateBlank().getClass().getSimpleName();
	}

	@Override
	public PreparedStatement prepareStatement(String statement) throws SQLException {
		return Database.prepareStatement(statement);
	}

	@Override
	public String getTableName() {
		return tableName;
	}

	public DaoFactoryImpl getDaoFactory() {
		return daoFactory;
	}

}
