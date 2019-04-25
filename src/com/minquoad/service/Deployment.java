package com.minquoad.service;

import java.io.File;
import java.util.Locale;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.json.JSONObject;

import com.minquoad.service.cron.CronManager;

public class Deployment implements ServletContextListener {

	public static final String VERSION = "0.1.0";

	public static final String DEPLOYMENT_KEY = "deployment";
	public static final String STORAGE_MANAGER_KEY = "storageManager";
	public static final String CRON_MANAGER_KEY = "cronManager";
	public static final String DATABASE_KEY = "database";
	public static final String LOGGER_KEY = "logger";

	public static final String CONFIGURATION_JSON_PATH = System.getProperty("user.home") + "/minquoad-web-configuration.json";

	private String storagePath;
	private String databaseHost;
	private String databasePort;
	private String databaseName;
	private String databaseUser;
	private String databasePassword;
	private String userPasswordSalt;

	private boolean open;

	public Deployment() {
		File file = new File(CONFIGURATION_JSON_PATH);
		if (!file.exists()) {
			new RuntimeException("Configuration json not found at \"" + CONFIGURATION_JSON_PATH + "\".");
		} else {
			JSONObject configurationJson = StorageManager.fileToJsonObject(CONFIGURATION_JSON_PATH);

			storagePath = configurationJson.getString("storagePath");

			JSONObject databaseJson = configurationJson.getJSONObject("database");

			databaseHost = databaseJson.getString("host");
			databasePort = databaseJson.getString("port");
			databaseName = databaseJson.getString("name");
			databaseUser = databaseJson.getString("user");
			databasePassword = databaseJson.getString("password");
			userPasswordSalt = databaseJson.getString("userPasswordSalt");

			setOpen(true);
		}
	}

	@Override
	public void contextInitialized(ServletContextEvent contextEvent) {

		Locale.setDefault(new Locale("en", "US"));

		ServletContext servletContext = contextEvent.getServletContext();

		Deployment deployment = new Deployment();
		servletContext.setAttribute(DEPLOYMENT_KEY, deployment);

		StorageManager storageManager = new StorageManager(servletContext);
		servletContext.setAttribute(STORAGE_MANAGER_KEY, storageManager);

		Logger logger = new Logger(servletContext);
		servletContext.setAttribute(LOGGER_KEY, logger);

		Database database = new Database(servletContext);
		servletContext.setAttribute(DATABASE_KEY, database);

		CronManager cronManager = new CronManager(servletContext);
		servletContext.setAttribute(CRON_MANAGER_KEY, cronManager);

		// cronManager.start();
		logger.logInfo("Servlet context initialized. Running version : " + VERSION);
	}

	@Override
	public void contextDestroyed(ServletContextEvent contextEvent) {
		ServletContext servletContext = contextEvent.getServletContext();

		CronManager cronManager = (CronManager) servletContext.getAttribute(CRON_MANAGER_KEY);
		Database database = (Database) servletContext.getAttribute(DATABASE_KEY);
		Logger logger = (Logger) servletContext.getAttribute(LOGGER_KEY);

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

	public boolean isOpen() {
		return open;
	}

	public void setOpen(boolean open) {
		this.open = open;
	}

}
