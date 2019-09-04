package com.minquoad.service;

import java.io.File;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.servlet.ServletContext;

import com.fasterxml.jackson.databind.JsonNode;

public class Deployment {

	public static final String CONFIGURATION_JSON_PATH = System.getProperty("user.home") + "/minquoad-web-configuration.json";

	private final ServletContext servletContext;

	private boolean open;

	private String storagePath;
	private String databaseHost;
	private String databasePort;
	private String databaseName;
	private String databaseUser;
	private String databasePassword;
	private String userPasswordSalt;

	public Deployment(ServletContext servletContext) {
		this.servletContext = servletContext;

		Locale.setDefault(new Locale("en", "US"));
		ResourceBundle.clearCache();

		extractConfiguration();
	}

	private void extractConfiguration() {
		try {
			JsonNode configurationJson = StorageManager.fileToJsonNode(new File(CONFIGURATION_JSON_PATH));

			storagePath = configurationJson.findValue("storagePath").asText();

			JsonNode databaseJson = configurationJson.findValue("database");

			databaseHost = databaseJson.findValue("host").asText();
			databasePort = databaseJson.findValue("port").asText();
			databaseName = databaseJson.findValue("name").asText();
			databaseUser = databaseJson.findValue("user").asText();
			databasePassword = databaseJson.findValue("password").asText();
			userPasswordSalt = databaseJson.findValue("userPasswordSalt").asText();

			setOpen(true);

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public void clear() {
		extractConfiguration();
		ResourceBundle.clearCache();
		ServicesManager.getService(servletContext, Database.class).clear();
	}
	
	public void setOpen(boolean open) {
		this.open = open;
	}

	public boolean isOpen() {
		return open;
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

	public String getVersion() {
		return "0.2.0";
	}

}
