package com.minquoad.unit;

import java.time.Duration;
import java.time.Instant;

import com.minquoad.dao.interfaces.FailedInLoginigAttemptDao;
import com.minquoad.entity.FailedInLoginigAttempt;

public class FailedInLoginigAttemptUnit extends Unit {

	public FailedInLoginigAttemptUnit(UnitFactory unitFactory) {
		super(unitFactory);
	}

	public void registerFailedInLoginigAttempt(String mailAddress) {

		if (getCoolDown(mailAddress) == null) {

			FailedInLoginigAttemptDao failedInLoginigAttemptDao = getDaoFactory().getFailedInLoginigAttemptDao();

			FailedInLoginigAttempt failedInLoginigAttempt = failedInLoginigAttemptDao.getOneMatching("mailAddress", mailAddress);

			if (failedInLoginigAttempt != null) {
				failedInLoginigAttempt.incrementAttemptsNumber();
				failedInLoginigAttempt.setLastAttemptInstant(Instant.now());
				failedInLoginigAttemptDao.persist(failedInLoginigAttempt);

			} else {
				failedInLoginigAttempt = new FailedInLoginigAttempt();
				failedInLoginigAttempt.setMailAddress(mailAddress);
				failedInLoginigAttempt.incrementAttemptsNumber();
				failedInLoginigAttempt.setLastAttemptInstant(Instant.now());
				failedInLoginigAttemptDao.persist(failedInLoginigAttempt);
			}
		}
	}

	public Duration getCoolDown(String mailAddress) {
		FailedInLoginigAttempt failedInLoginigAttempt = getDaoFactory().getFailedInLoginigAttemptDao().getOneMatching("mailAddress", mailAddress);
		if (failedInLoginigAttempt == null) {
			return null;
		} else {
			Instant lastAttemptInstant = failedInLoginigAttempt.getLastAttemptInstant();
			long attemptsNumber = failedInLoginigAttempt.getAttemptsNumber();
			
			Duration duration = null;
			
			if (attemptsNumber >= 5l) {
				if (attemptsNumber < 8l) {
					Instant unblockInstant = Instant.ofEpochMilli(lastAttemptInstant.toEpochMilli() + 1000 * 60);
					duration = Duration.ofMillis(unblockInstant.toEpochMilli() - Instant.now().toEpochMilli());
				} else {
					if (attemptsNumber < 10l) {
						Instant unblockInstant = Instant.ofEpochMilli(lastAttemptInstant.toEpochMilli() + 1000 * 60 * 10);
						duration = Duration.ofMillis(unblockInstant.toEpochMilli() - Instant.now().toEpochMilli());
					} else {
						Instant unblockInstant = Instant.ofEpochMilli(lastAttemptInstant.toEpochMilli() + 1000 * 60 * 20);
						duration = Duration.ofMillis(unblockInstant.toEpochMilli() - Instant.now().toEpochMilli());
					}
				}
			}
			
			if (duration == null || duration.isNegative()) {
				return null;
			} else {
				return duration;
			}
		}
	}

}
