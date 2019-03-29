package com.minquoad.service.cron;

import java.time.Instant;

public class CronManager {

	static long lastMinuteStart;
	static long lastHoureStart;
	static long lastDayStart;

	public static void start() {
		
		new Thread(new Runnable() {
			public void run() {
				long startTime = Instant.now().toEpochMilli();
				lastMinuteStart = startTime;
				lastHoureStart = startTime;
				lastDayStart = startTime;

				loop();
			}
		}).start();
		
	}

	public static void loop() {
		while (true) {

			long timeToWait = 1000 * 60 + lastMinuteStart - Instant.now().toEpochMilli();
			if (timeToWait > 0) {
				try {
					Thread.sleep(timeToWait);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

			lastMinuteStart += 1000 * 60;

			runMinutlyCrons();

			if (Instant.now().toEpochMilli() >= lastHoureStart + 1000 * 60 * 60) {
				lastHoureStart += 1000 * 60 * 60;
				runHourlyCrons();
			}

			if (Instant.now().toEpochMilli() >= lastDayStart + 1000 * 60 * 60 * 24) {
				lastDayStart += 1000 * 60 * 60 * 24;
				runDaylyCrons();
			}
		}
	}

	private static void runMinutlyCrons() {
	}

	private static void runHourlyCrons() {
	}

	private static void runDaylyCrons() {
	}

}
