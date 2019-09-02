package com.minquoad.unit;

import javax.servlet.ServletContext;

import com.minquoad.dao.interfaces.DaoFactory;

public class UnitFactory {

	private final ServletContext servletContext;
	private DaoFactory daoFactory;

	public UnitFactory(ServletContext servletContext, DaoFactory daoFactory) {
		this.servletContext = servletContext;
		this.daoFactory = daoFactory;
	}

	public DaoFactory getDaoFactory() {
		return daoFactory;
	}

	public ServletContext getServletContext() {
		return servletContext;
	}

	public UserUnit getUserUnit() {
		return new UserUnit(this);
	}

	public ConversationUnit getConversationUnit() {
		return new ConversationUnit(this);
	}

	public FailedInLoginigAttemptUnit getFailedInLoginigAttemptUnit() {
		return new FailedInLoginigAttemptUnit(this);
	}

}
