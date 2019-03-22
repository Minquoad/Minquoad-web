package com.minquoad.dao.sqlImpl;

import java.sql.SQLException;

import com.minquoad.dao.interfaces.FailedInLoginigAttemptDao;
import com.minquoad.entity.FailedInLoginigAttempt;

public class FailedInLoginigAttemptDaoImpl extends ImprovedEntityDaoImpl<FailedInLoginigAttempt> implements FailedInLoginigAttemptDao {

	public FailedInLoginigAttemptDaoImpl(DaoFactoryImpl daoFactory) {
		super(daoFactory);
	}

	@Override
	public void initEntityMembers() throws SQLException {
		this.addIntegerEntityMember("id", FailedInLoginigAttempt::getId, FailedInLoginigAttempt::setId, true);
		this.addStringEntityMember("mailAddress", FailedInLoginigAttempt::getMailAddress, FailedInLoginigAttempt::setMailAddress);
		this.addIntegerEntityMember("attemptsCount", FailedInLoginigAttempt::getAttemptsCount, FailedInLoginigAttempt::setAttemptsCount);
		this.addInstantEntityMember("lastArremptInstant", FailedInLoginigAttempt::getLastArremptInstant, FailedInLoginigAttempt::setLastArremptInstant);
	}

	@Override
	public FailedInLoginigAttempt instantiateBlank() {
		return new FailedInLoginigAttempt();
	}

}
