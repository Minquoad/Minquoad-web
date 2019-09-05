package com.minquoad.dao.sqlImpl;

import com.minquoad.framework.dao.DaoFactoryImpl;

public abstract class DaoImpl<EntitySubclass> extends com.minquoad.framework.dao.DaoImpl<EntitySubclass> {

	private String tableName;

	public DaoImpl(DaoFactoryImpl daoFactoryImpl) {
		super(daoFactoryImpl);

		this.tableName = this.instantiateBlank().getClass().getSimpleName();
		//this.addStatementListener((statement) -> System.out.println(statement));
	}

	@Override
	protected String getTableName() {
		return tableName;
	}

	@Override
	public com.minquoad.dao.sqlImpl.DaoFactoryImpl getDaoFactory() {
		return (com.minquoad.dao.sqlImpl.DaoFactoryImpl) super.getDaoFactory();
	}

}
