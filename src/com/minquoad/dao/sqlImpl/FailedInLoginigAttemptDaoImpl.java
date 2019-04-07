package com.minquoad.dao.sqlImpl;

import com.minquoad.dao.interfaces.FailedInLoginigAttemptDao;
import com.minquoad.entity.FailedInLoginigAttempt;
import com.minquoad.framework.dao.DaoException;

public class FailedInLoginigAttemptDaoImpl extends ImprovedDaoImpl<FailedInLoginigAttempt> implements FailedInLoginigAttemptDao {

	public FailedInLoginigAttemptDaoImpl(DaoFactoryImpl daoFactory) {
		super(daoFactory);
	}

	@Override
	public void initEntityMembers() throws DaoException {
		this.addLongEntityMember("id", FailedInLoginigAttempt::getId, FailedInLoginigAttempt::setId, true);
		this.addStringEntityMember("mailAddress", FailedInLoginigAttempt::getMailAddress, FailedInLoginigAttempt::setMailAddress);
		this.addLongEntityMember("attemptsCount", FailedInLoginigAttempt::getAttemptsCount, FailedInLoginigAttempt::setAttemptsCount);
		this.addInstantEntityMember("lastArremptInstant", FailedInLoginigAttempt::getLastArremptInstant, FailedInLoginigAttempt::setLastArremptInstant);
	}

	@Override
	public FailedInLoginigAttempt instantiateBlank() {
		return new FailedInLoginigAttempt();
	}

}
