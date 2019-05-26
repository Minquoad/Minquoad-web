package com.minquoad.service;

import java.util.Locale;
import java.util.ResourceBundle;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpServletRequest;

import com.minquoad.service.cron.CronManager;

@WebListener
public class ServicesManager implements ServletContextListener {

	@Override
	public void contextInitialized(ServletContextEvent contextEvent) {

		Locale.setDefault(new Locale("en", "US"));
		ResourceBundle.clearCache();

		ServletContext servletContext = contextEvent.getServletContext();

		addService(servletContext, new Deployment());
		addService(servletContext, new StorageManager(servletContext));
		addService(servletContext, new Logger(servletContext));
		addService(servletContext, new Database(servletContext));
		addService(servletContext, new SessionManager());
		addService(servletContext, new CronManager(servletContext));

		//getService(servletContext, CronManager.class).start();
		getService(servletContext, Logger.class).logInfo("Servlet context initialized. Running version : " + Deployment.VERSION);
	}

	@Override
	public void contextDestroyed(ServletContextEvent contextEvent) {
		ServletContext servletContext = contextEvent.getServletContext();

		getService(servletContext, CronManager.class).stop();
		getService(servletContext, Database.class).close();
		getService(servletContext, Logger.class).logInfo("Servlet context destroyed");
	}

	public static void addService(ServletContext servletContext, Object service) {
		servletContext.setAttribute(service.getClass().getName(), service);
	}

	public static <ServiceClass> ServiceClass getService(HttpServletRequest request, Class<ServiceClass> serviceClass) {
		return ServicesManager.getService(request.getServletContext(), serviceClass);
	}

	@SuppressWarnings("unchecked")
	public static <ServiceClass> ServiceClass getService(ServletContext context, Class<ServiceClass> serviceClass) {
		return (ServiceClass) context.getAttribute(serviceClass.getName());
	}

}
