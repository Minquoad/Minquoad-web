package com.minquoad.service.cron;

import java.time.Instant;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.servlet.ServletContext;

import com.minquoad.service.Logger;
import com.minquoad.service.ServicesManager;

public class CronManager {

	private ScheduledExecutorService scheduler;
	private ExecutorService executor;

	private final ServletContext servletContext;

	public CronManager(ServletContext servletContext) {
		this.servletContext = servletContext;

		scheduler = Executors.newSingleThreadScheduledExecutor();
		executor = Executors.newSingleThreadExecutor();
	}

	public void start() {
		//add(new LoggerCron(servletContext));
	}

	public void stop() {
		scheduler.shutdown();
	}

	public void add(Cron cron) {

		long rate = cron.getRate();

		scheduler.scheduleAtFixedRate(
				() -> runCron(cron),
				rate - (Instant.now().toEpochMilli() % rate),
				rate,
				TimeUnit.MILLISECONDS);
	}

	private void runCron(Cron cron) {

		Instant instant = round(Instant.now(), cron.getRate());

		executor.submit(() -> {
			try {
				cron.listenTime(instant);

			} catch (Exception e) {
				e.printStackTrace();
				ServicesManager.getService(servletContext, Logger.class).logError(e);
			}
		});
	}

	public static Instant round(Instant instant, long modulo) {
		long epochMilli = instant.toEpochMilli() + (modulo / 2l);
		return Instant.ofEpochMilli(epochMilli - (epochMilli % modulo));
	}

}
