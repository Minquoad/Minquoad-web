package com.minquoad.service.cron;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import com.minquoad.service.Logger;
import com.minquoad.service.StorageManager;

public class CronManager {

	static boolean stopRequested;
	
	static long lastMinuteStart;
	static long lastHoureStart;
	static long lastDayStart;

	static List<Runnable> minutelyCrons = new ArrayList<Runnable>();

	static {
		
	}
	
	public static void start() {
		
		stopRequested = false;
		
		new Thread(new Runnable() {
			public void run() {
				long startTime = Instant.now().toEpochMilli();
				lastMinuteStart = startTime;
				lastHoureStart = startTime;
				lastDayStart = startTime;

				CronManager.loop();
			}
		}).start();
		
	}

	public static void stop() {
		stopRequested = true;
	}

	public static void loop() {
		while (!stopRequested) {

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
		}
	}

	private static void runMinutlyCrons() {
		
		Logger.logInFile(Logger.getDateTime() + " : " + "CronManager.runMinutlyCrons() called", StorageManager.logPath + "cron.log");
		
		for (Runnable minutelyCron : minutelyCrons) {
			try {
				minutelyCron.run();

			} catch (Exception e) {
				e.printStackTrace();
				Logger.logError(e);
			}
		}
	}

}
