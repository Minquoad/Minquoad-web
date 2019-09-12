package com.minquoad.service.cron;

import java.time.Instant;

public class TestCron implements Cron {

	@Override
	public void listenTime(Instant instant) {
		System.out.println("TestCron.listenTime(Instant) called");
		throw new RuntimeException("This is a throw test.");
	}

}
