package com.minquoad.entity;

import java.time.Instant;

import com.minquoad.framework.dao.Entity;

public class FailedInLoginigAttempt extends Entity {

	private Integer id;
	private String mailAddress;
	private int attemptsCount;
	private Instant lastArremptInstant;

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

	public Instant getLastArremptInstant() {
		return lastArremptInstant;
	}

	public void setLastArremptInstant(Instant lastArremptInstant) {
		this.lastArremptInstant = lastArremptInstant;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

}
