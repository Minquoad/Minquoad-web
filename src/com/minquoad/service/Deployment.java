package com.minquoad.service;

import java.io.File;

import com.fasterxml.jackson.databind.JsonNode;

public class Deployment {

	public static final String VERSION = "0.2.0";

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

			JsonNode configurationJson = StorageManager.fileToJsonNode(CONFIGURATION_JSON_PATH);

			storagePath = configurationJson.findValue("storagePath").asText();

			JsonNode databaseJson = configurationJson.findValue("database");

			databaseHost = databaseJson.findValue("host").asText();
			databasePort = databaseJson.findValue("port").asText();
			databaseName = databaseJson.findValue("name").asText();
			databaseUser = databaseJson.findValue("user").asText();
			databasePassword = databaseJson.findValue("password").asText();
			userPasswordSalt = databaseJson.findValue("userPasswordSalt").asText();

			setOpen(true);

		}
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

	public String getVersion() {
		return VERSION;
	}

}
