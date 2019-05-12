package com.minquoad.service;

import java.util.Locale;
import java.util.ResourceBundle;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import com.minquoad.service.cron.CronManager;

@WebListener
public class ServletContextListenerImpl implements ServletContextListener {

	@Override
	public void contextInitialized(ServletContextEvent contextEvent) {

		Locale.setDefault(new Locale("en", "US"));
		ResourceBundle.clearCache();

		ServletContext servletContext = contextEvent.getServletContext();

		Deployment deployment = new Deployment();
		servletContext.setAttribute(Deployment.class.getName(), deployment);

		StorageManager storageManager = new StorageManager(servletContext);
		servletContext.setAttribute(StorageManager.class.getName(), storageManager);

		Logger logger = new Logger(servletContext);
		servletContext.setAttribute(Logger.class.getName(), logger);

		Database database = new Database(servletContext);
		servletContext.setAttribute(Database.class.getName(), database);

		CronManager cronManager = new CronManager(servletContext);
		servletContext.setAttribute(CronManager.class.getName(), cronManager);

		SessionManager sessionManager = new SessionManager();
		servletContext.setAttribute(SessionManager.class.getName(), sessionManager);

		// cronManager.start();
		logger.logInfo("Servlet context initialized. Running version : " + Deployment.VERSION);
	}

	@Override
	public void contextDestroyed(ServletContextEvent contextEvent) {
		ServletContext servletContext = contextEvent.getServletContext();

		CronManager cronManager = (CronManager) servletContext.getAttribute(CronManager.class.getName());
		Database database = (Database) servletContext.getAttribute(Database.class.getName());
		Logger logger = (Logger) servletContext.getAttribute(Logger.class.getName());

		cronManager.stop();
		database.close();
		logger.logInfo("Servlet context destroyed");
	}

}
