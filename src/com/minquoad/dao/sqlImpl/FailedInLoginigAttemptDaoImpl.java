package com.minquoad.dao.sqlImpl;

import com.minquoad.dao.interfaces.FailedInLoginigAttemptDao;
import com.minquoad.entity.FailedInLoginigAttempt;
import com.minquoad.framework.dao.DaoException;

public class FailedInLoginigAttemptDaoImpl extends DaoImpl<FailedInLoginigAttempt> implements FailedInLoginigAttemptDao {

	public FailedInLoginigAttemptDaoImpl(DaoFactoryImpl daoFactory) {
		super(daoFactory);
	}

	@Override
	protected FailedInLoginigAttempt instantiateBlank() {
		return new FailedInLoginigAttempt();
	}

	@Override
	protected void initSuperClass() {
	}

	@Override
	protected void initSubClasses() {
	}

	@Override
	protected void initEntityMembers() throws DaoException {
		this.addLongEntityMember("id", FailedInLoginigAttempt::getId, FailedInLoginigAttempt::setId);
		this.addStringEntityMember("mailAddress", FailedInLoginigAttempt::getMailAddress, FailedInLoginigAttempt::setMailAddress);
		this.addLongEntityMember("attemptsNumber", FailedInLoginigAttempt::getAttemptsNumber, FailedInLoginigAttempt::setAttemptsNumber);
		this.addInstantEntityMember("lastAttemptInstant", FailedInLoginigAttempt::getLastAttemptInstant, FailedInLoginigAttempt::setLastAttemptInstant);
	}

}
