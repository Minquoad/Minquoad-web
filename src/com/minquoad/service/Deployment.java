package com.minquoad.service;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.minquoad.entity.User;

public class Deployment implements ServletContextListener {

	private static final Configuration configuration = new ConfigurationImpl();

	public static final String databaseUrl = configuration.getDatabaseUrl();
	public static final String databaseName = configuration.getDatabaseName();
	public static final String databaseUser = configuration.getDatabaseUser();
	public static final String databasePassword = configuration.getDatabasePassword();
	public static final String passwordSalt = configuration.getPasswordSalt();
	public static final String storagePath = configuration.getStoragePath();

	public static String getDynamicSalt(User user) {
		return configuration.getDynamicSalt(user);
	}

	@Override
	public void contextInitialized(ServletContextEvent contextEvent) {
		StorageManager.initTree();
	}

	@Override
	public void contextDestroyed(ServletContextEvent contextEvent) {
	}

}
