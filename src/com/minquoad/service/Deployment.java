package com.minquoad.service;

import java.io.File;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.json.JSONObject;

import com.minquoad.service.cron.CronManager;

public class Deployment implements ServletContextListener {

	public static final String version = "0.1.0";

	public static final String deploymentKey = "deployment";
	public static final String storageManagerKey = "storageManager";
	public static final String cronManagerKey = "cronManager";
	public static final String databaseKey = "database";
	public static final String loggerKey = "logger";

	public static final String configurationJsonPath = System.getProperty("user.home") + "/minquoad-web-configuration.json";

	private String storagePath;
	private String databaseHost;
	private String databasePort;
	private String databaseName;
	private String databaseUser;
	private String databasePassword;
	private String userPasswordSalt;

	public Deployment() {
		File file = new File(configurationJsonPath);
		if (!file.exists()) {
			new RuntimeException("Configuration json not found at \"" + configurationJsonPath + "\".");
		} else {
			JSONObject configurationJson = StorageManager.fileToJsonObject(configurationJsonPath);

			storagePath = configurationJson.getString("storagePath");

			JSONObject databaseJson = configurationJson.getJSONObject("database");

			databaseHost = databaseJson.getString("host");
			databasePort = databaseJson.getString("port");
			databaseName = databaseJson.getString("name");
			databaseUser = databaseJson.getString("user");
			databasePassword = databaseJson.getString("password");
			userPasswordSalt = databaseJson.getString("userPasswordSalt");
		}
	}

	@Override
	public void contextInitialized(ServletContextEvent contextEvent) {
		ServletContext servletContext = contextEvent.getServletContext();

		Deployment deployment = new Deployment();
		servletContext.setAttribute(deploymentKey, deployment);

		StorageManager storageManager = new StorageManager(servletContext);
		servletContext.setAttribute(storageManagerKey, storageManager);

		Logger logger = new Logger(servletContext);
		servletContext.setAttribute(loggerKey, logger);

		Database database = new Database(servletContext);
		servletContext.setAttribute(databaseKey, database);

		CronManager cronManager = new CronManager(servletContext);
		servletContext.setAttribute(cronManagerKey, cronManager);

		// cronManager.start();
		logger.logInfo("Servlet context initialized");
	}

	@Override
	public void contextDestroyed(ServletContextEvent contextEvent) {
		ServletContext servletContext = contextEvent.getServletContext();

		CronManager cronManager = (CronManager) servletContext.getAttribute(cronManagerKey);
		Database database = (Database) servletContext.getAttribute(databaseKey);
		Logger logger = (Logger) servletContext.getAttribute(loggerKey);

		cronManager.stop();
		database.close();
		logger.logInfo("Servlet context destroyed");
	}

	public String getStoragePath() {
		return storagePath;
	}

	public String getDatabaseHost() {
		return databaseHost;
	}

	public String getDatabasePort() {
		return databasePort;
	}

	public String getDatabaseName() {
		return databaseName;
	}

	public String getDatabaseUser() {
		return databaseUser;
	}

	public String getDatabasePassword() {
		return databasePassword;
	}

	public String getUserPasswordSalt() {
		return userPasswordSalt;
	}

}
