package com.minquoad.service;

import java.io.File;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.json.JSONObject;

import com.minquoad.service.cron.CronManager;

public class Deployment implements ServletContextListener {

	public static final String version = "0.1.0";

	public static final String databaseKey = "database";

	public static final String configurationJsonPath = System.getProperty("user.home") + "/minquoad-web-configuration.json";

	private static String storagePath;
	private static String databaseHost;
	private static String databasePort;
	private static String databaseName;
	private static String databaseUser;
	private static String databasePassword;
	private static String userPasswordSalt;

	@Override
	public void contextInitialized(ServletContextEvent contextEvent) {
		ServletContext servletContext = contextEvent.getServletContext();

		initConfiguration();
		StorageManager.initTree();
		servletContext.setAttribute(databaseKey, new Database());
		// CronManager.start();

		Logger.logInfo("Servlet context initialized");
	}

	@Override
	public void contextDestroyed(ServletContextEvent contextEvent) {
		ServletContext servletContext = contextEvent.getServletContext();

		CronManager.stop();
		Database database = (Database) servletContext.getAttribute(Deployment.databaseKey);
		database.close();

		Logger.logInfo("Servlet context destroyed");
	}

	public static void initConfiguration() {

		File file = new File(configurationJsonPath);
		if (!file.exists()) {
			new Exception("Configuration json not found at \"" + configurationJsonPath + "\".").printStackTrace();
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

	public static String getStoragePath() {
		return storagePath;
	}

	public static String getDatabaseHost() {
		return databaseHost;
	}

	public static String getDatabasePort() {
		return databasePort;
	}

	public static String getDatabaseName() {
		return databaseName;
	}

	public static String getDatabaseUser() {
		return databaseUser;
	}

	public static String getDatabasePassword() {
		return databasePassword;
	}

	public static String getUserPasswordSalt() {
		return userPasswordSalt;
	}

}
