package com.minquoad.service;

import java.io.File;

import org.json.JSONObject;

public class Deployment {

	public static final String VERSION = "0.1.2";

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
