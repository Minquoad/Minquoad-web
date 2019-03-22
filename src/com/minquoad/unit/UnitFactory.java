package com.minquoad.unit;

import com.minquoad.dao.interfaces.DaoFactory;

public class UnitFactory {

	private DaoFactory daoFactory;

	public UnitFactory(DaoFactory daoFactory) {
		this.daoFactory = daoFactory;
	}

	public DaoFactory getDaoFactory() {
		return daoFactory;
	}

	public UserUnit getUserUnit() {
		return new UserUnit(getDaoFactory());
	}

	public ConversationUnit getConversationUnit() {
		return new ConversationUnit(getDaoFactory());
	}
	
	public FailedInLoginigAttemptUnit getFailedInLoginigAttemptUnit() {
		return new FailedInLoginigAttemptUnit(getDaoFactory());
	}
	
}
