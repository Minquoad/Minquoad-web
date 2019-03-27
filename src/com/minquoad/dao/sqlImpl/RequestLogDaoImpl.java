package com.minquoad.dao.sqlImpl;

import java.sql.SQLException;

import com.minquoad.dao.interfaces.RequestLogDao;
import com.minquoad.entity.RequestLog;

public class RequestLogDaoImpl extends ImprovedEntityDaoImpl<RequestLog> implements RequestLogDao {

	public RequestLogDaoImpl(DaoFactoryImpl daoFactory) {
		super(daoFactory);
	}

	@Override
	protected void initEntityMembers() throws SQLException {
		this.addLongEntityMember("id", RequestLog::getId, RequestLog::setId, true);
		this.addInstantEntityMember("instant", RequestLog::getInstant, RequestLog::setInstant);
		this.addStringEntityMember("url", RequestLog::getUrl, RequestLog::setUrl);
		this.addForeingKeyEntityMember("user", RequestLog::getUser, RequestLog::setUser, getDaoFactory().getUserDao());
		this.addStringEntityMember("error", RequestLog::getError, RequestLog::setError);
		this.addIntegerEntityMember("serviceDuration", RequestLog::getServiceDuration, RequestLog::setServiceDuration);
		this.addStringEntityMember("servletName", RequestLog::getServletName, RequestLog::setServletName);
	}

	@Override
	public RequestLog instantiateBlank() {
		return new RequestLog();
	}

}
