package com.minquoad.entity;

import java.time.Instant;

public class FailedInLoginigAttempt {

	private Long id;
	private String mailAddress;
	private long attemptsNumber;
	private Instant lastAttemptInstant;

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

	public long getAttemptsNumber() {
		return attemptsNumber;
	}

	public void incrementAttemptsNumber() {
		if (attemptsNumber != Long.MAX_VALUE) {
			attemptsNumber++;
		}
	}

	public void setAttemptsNumber(long attemptsNumber) {
		this.attemptsNumber = attemptsNumber;
	}

	public Instant getLastAttemptInstant() {
		return lastAttemptInstant;
	}

	public void setLastAttemptInstant(Instant lastAttemptInstant) {
		this.lastAttemptInstant = lastAttemptInstant;
	}

}
