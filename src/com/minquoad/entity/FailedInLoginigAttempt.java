package com.minquoad.entity;

import java.time.Instant;

public class FailedInLoginigAttempt {

	private Long id;
	private String mailAddress;
	private long attemptsCount;
	private Instant lastArremptInstant;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getMailAddress() {
		return mailAddress;
	}

	public void setMailAddress(String mailAddress) {
		this.mailAddress = mailAddress;
	}

	public long getAttemptsCount() {
		return attemptsCount;
	}

	public void incrementAttemptsCount() {
		if (attemptsCount != Long.MAX_VALUE) {
			attemptsCount++;
		}
	}

	public void setAttemptsCount(long attemptsCount) {
		this.attemptsCount = attemptsCount;
	}

	public Instant getLastArremptInstant() {
		return lastArremptInstant;
	}

	public void setLastArremptInstant(Instant lastArremptInstant) {
		this.lastArremptInstant = lastArremptInstant;
	}

}
