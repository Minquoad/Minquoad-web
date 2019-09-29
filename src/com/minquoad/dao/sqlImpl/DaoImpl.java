package com.minquoad.dao.sqlImpl;

public abstract class DaoImpl<EntitySubclass> extends com.minquoad.framework.dao.DaoImpl<EntitySubclass> {

	private String tableName;

	public DaoImpl(DaoFactoryImpl daoFactoryImpl) {
		super(daoFactoryImpl);

		this.tableName = this.instantiateBlank().getClass().getSimpleName();
/*
		this.addStatementListener(statement -> {
			ServletContext servletContext = getDaoFactory().getServletContext();
			Logger logger = ServicesManager.getService(servletContext, Logger.class);
			StorageManager storageManager = ServicesManager.getService(servletContext, StorageManager.class);
			logger.logInFile(statement, storageManager.getFile(StorageManager.LOG_PATH + "statements.sql"));
		});
*/
	}

	@Override
	protected String getTableName() {
		return tableName;
	}

	@Override
	public DaoFactoryImpl getDaoFactory() {
		return (DaoFactoryImpl) super.getDaoFactory();
	}

}
