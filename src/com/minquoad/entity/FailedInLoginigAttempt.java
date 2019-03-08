package com.minquoad.entity;

import java.util.Date;

import com.minquoad.framework.dao.Entity;

public class FailedInLoginigAttempt extends Entity {

	private String mailAddress;
	private int attemptsCount;
	private Date lastArremptDate;

	public String getMailAddress() {
		return mailAddress;
	}

	public void setMailAddress(String mailAddress) {
		this.mailAddress = mailAddress;
	}

	public int getAttemptsCount() {
		return attemptsCount;
	}

	public void incrementAttemptsCount() {
		if (attemptsCount != Integer.MAX_VALUE) {
			attemptsCount++;
		}
	}

	public void setAttemptsCount(int attemptsCount) {
		this.attemptsCount = attemptsCount;
	}

	public Date getLastArremptDate() {
		return lastArremptDate;
	}

	public void setLastArremptDate(Date lastArremptDate) {
		this.lastArremptDate = lastArremptDate;
	}

}
