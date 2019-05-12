package com.minquoad.service.cron;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;

import com.minquoad.service.Logger;
import com.minquoad.service.StorageManager;

public class CronManager {

	private boolean stopRequested;

	private long lastMinuteStart;

	private List<Runnable> minutelyCrons = new ArrayList<Runnable>();

	private final ServletContext servletContext;

	public CronManager(ServletContext servletContext) {
		this.servletContext = servletContext;
	}

	public void start() {

		stopRequested = false;

		new Thread(new Runnable() {
			@Override
			public void run() {
				long startTime = Instant.now().toEpochMilli();
				lastMinuteStart = startTime;

				CronManager.this.loop();
			}
		}).start();

	}

	public void stop() {
		stopRequested = true;
	}

	public void loop() {
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

	private void runMinutlyCrons() {
		Logger logger = (Logger) servletContext.getAttribute(Logger.class.getName());

		logger.logInFile(Logger.getDateTime() + " : " + "CronManager.runMinutlyCrons() called", StorageManager.LOG_PATH + "cron.log");

		for (Runnable minutelyCron : minutelyCrons) {
			try {
				minutelyCron.run();

			} catch (Exception e) {
				e.printStackTrace();
				logger.logError(e);
			}
		}
	}

}
