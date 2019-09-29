package com.minquoad.service.cron;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;

import com.minquoad.service.Logger;
import com.minquoad.service.ServicesManager;

public class CronManager {

	private boolean stopRequested;

	private long lastMinuteStart;

	private List<Cron> minutelyCrons;

	private final ServletContext servletContext;

	public CronManager(ServletContext servletContext) {
		this.servletContext = servletContext;
		minutelyCrons = new ArrayList<Cron>();
		
		minutelyCrons.add(new TestCron());
	}

	public void start() {

		stopRequested = false;
		long epochMilli = Instant.now().toEpochMilli();
		lastMinuteStart = epochMilli - (epochMilli % 60_000l);

		new Thread(() -> CronManager.this.loop()).start();
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

			Instant instant = Instant.ofEpochMilli(lastMinuteStart);

			for (Cron minutelyCron : minutelyCrons) {
				try {
					minutelyCron.listenTime(instant);
				} catch (Exception e) {
					e.printStackTrace();
					ServicesManager.getService(servletContext, Logger.class).logError(e);
				}
			}
		}
	}

}
