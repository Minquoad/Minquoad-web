package com.minquoad.entity.unit;

import java.time.Duration;
import java.time.Instant;

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
			failedInLoginigAttempt.setLastArremptInstant(Instant.now());
			failedInLoginigAttemptDao.persist(failedInLoginigAttempt);

		} else {
			failedInLoginigAttempt = new FailedInLoginigAttempt();
			failedInLoginigAttempt.setMailAddress(mailAddress);
			failedInLoginigAttempt.incrementAttemptsCount();
			failedInLoginigAttempt.setLastArremptInstant(Instant.now());
			failedInLoginigAttemptDao.persist(failedInLoginigAttempt);
		}
	}

	public Duration getCoolDown(String mailAddress) {
		FailedInLoginigAttempt failedInLoginigAttempt = getDaoFactory().getFailedInLoginigAttemptDao().getOneMatching(mailAddress, "mailAddress");
		if (failedInLoginigAttempt == null) {
			return null;
		} else {
			Instant lastAttemptInstant = failedInLoginigAttempt.getLastArremptInstant();
			int attemptsCount = failedInLoginigAttempt.getAttemptsCount();
			Duration duration = null;
			if (attemptsCount >= 5) {
				if (attemptsCount < 8) {
					Instant unblockInstant = Instant.ofEpochMilli(lastAttemptInstant.toEpochMilli() + 1000 * 60);
					duration = Duration.ofMillis(unblockInstant.toEpochMilli() - Instant.now().toEpochMilli());
				} else {
					if (attemptsCount < 10) {
						Instant unblockInstant = Instant.ofEpochMilli(lastAttemptInstant.toEpochMilli() + 1000 * 60 * 10);
						duration = Duration.ofMillis(unblockInstant.toEpochMilli() - Instant.now().toEpochMilli());
					} else {
						Instant unblockInstant = Instant.ofEpochMilli(lastAttemptInstant.toEpochMilli() + 1000 * 60 * 20);
						duration = Duration.ofMillis(unblockInstant.toEpochMilli() - Instant.now().toEpochMilli());
					}
				}
			}
			
			if (duration.isNegative()) {
				duration = null;
			}
			
			return duration;
		}
	}

}
