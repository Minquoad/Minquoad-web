package com.minquoad.dao.interfaces;

import com.minquoad.entity.FailedInLoginigAttempt;

public interface FailedInLoginigAttemptDao extends Dao<FailedInLoginigAttempt> {

	public FailedInLoginigAttempt getFailedInLoginigAttemptByMailAddress(String mailAddress);

}
