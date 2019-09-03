package com.minquoad.dao.sqlImpl;

import com.minquoad.dao.interfaces.RequestLogDao;
import com.minquoad.entity.RequestLog;
import com.minquoad.framework.dao.DaoException;

public class RequestLogDaoImpl extends ImprovedDaoImpl<RequestLog> implements RequestLogDao {

	public RequestLogDaoImpl(DaoFactoryImpl daoFactory) {
		super(daoFactory);
	}

	@Override
	protected RequestLog instantiateBlank() {
		return new RequestLog();
	}

	@Override
	protected void initEntityMembers() throws DaoException {
		this.addLongEntityMember("id", RequestLog::getId, RequestLog::setId);
		this.addInstantEntityMember("instant", RequestLog::getInstant, RequestLog::setInstant);
		this.addStringEntityMember("url", RequestLog::getUrl, RequestLog::setUrl);
		this.addForeingKeyEntityMember("user", RequestLog::getUser, RequestLog::setUser, getDaoFactory().getUserDao());
		this.addForeingKeyEntityMember("controllingAdmin", RequestLog::getControllingAdmin, RequestLog::setControllingAdmin, getDaoFactory().getUserDao());
		this.addStringEntityMember("ipAddress", RequestLog::getIpAddress, RequestLog::setIpAddress);
		this.addStringEntityMember("error", RequestLog::getError, RequestLog::setError);
		this.addIntegerEntityMember("serviceDuration", RequestLog::getServiceDuration, RequestLog::setServiceDuration);
		this.addStringEntityMember("servletName", RequestLog::getServletName, RequestLog::setServletName);
	}

}
