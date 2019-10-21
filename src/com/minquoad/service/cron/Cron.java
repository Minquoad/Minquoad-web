package com.minquoad.service.cron;

import java.time.Instant;

import javax.servlet.ServletContext;

public abstract class Cron {

	private final ServletContext servletContext;

	public Cron(ServletContext servletContext) {
		this.servletContext = servletContext;
	}

	public ServletContext getServletContext() {
		return servletContext;
	}

	public abstract void listenTime(Instant instant);

	public abstract long getRate();

}
