package com.minquoad.service.cron;

import java.time.Instant;

import javax.servlet.ServletContext;

import com.minquoad.service.Logger;
import com.minquoad.service.ServicesManager;

public class LoggerCron extends Cron {

	public LoggerCron(ServletContext servletContext) {
		super(servletContext);
	}

	@Override
	public void listenTime(Instant instant) {
		ServicesManager.getService(getServletContext(), Logger.class).logInfo("LoggerCron run with param " + instant);
	}

	@Override
	public long getRate() {
		return 60_000l;
	}

}
