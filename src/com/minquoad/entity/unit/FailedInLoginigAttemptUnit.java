package com.minquoad.entity.unit;

import java.time.Period;
import java.util.Date;

import com.minquoad.dao.interfaces.DaoFactory;
import com.minquoad.dao.interfaces.FailedInLoginigAttemptDao;
import com.minquoad.entity.FailedInLoginigAttempt;

public class FailedInLoginigAttemptUnit extends Unit {

	public FailedInLoginigAttemptUnit(DaoFactory daoFactory) {
		super(daoFactory);
	}

	public void registerFailedInLoginigAttempt(String mailAddress) {

		FailedInLoginigAttemptDao failedInLoginigAttemptDao = getDaoFactory().getFailedInLoginigAttemptDao();

		FailedInLoginigAttempt failedInLoginigAttempt = failedInLoginigAttemptDao.getOneMatching(mailAddress, "mailAddress");

		if (failedInLoginigAttempt != null) {
			failedInLoginigAttempt.incrementAttemptsCount();
			failedInLoginigAttempt.setLastArremptDate(new Date());
			failedInLoginigAttemptDao.update(failedInLoginigAttempt);

		} else {
			failedInLoginigAttempt = new FailedInLoginigAttempt();
			failedInLoginigAttempt.setMailAddress(mailAddress);
			failedInLoginigAttempt.incrementAttemptsCount();
			failedInLoginigAttempt.setLastArremptDate(new Date());
			failedInLoginigAttemptDao.insert(failedInLoginigAttempt);
		}
	}

	public Period getCoolDown(String mailAddress) {
		FailedInLoginigAttempt failedInLoginigAttempt = getDaoFactory().getFailedInLoginigAttemptDao().getOneMatching(mailAddress, "mailAddress");
		if (failedInLoginigAttempt == null) {
			return null;
		} else {
			Date lastAttemptDate = failedInLoginigAttempt.getLastArremptDate();
			int attemptsCount = failedInLoginigAttempt.getAttemptsCount();
			Period period = null;
			if (attemptsCount > 4) {
			}
			return period;
		}
	}

}
