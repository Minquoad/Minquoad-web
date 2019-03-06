package com.minquoad.service;

import com.minquoad.entity.User;

public interface Configuration {

	public String getDatabaseUrl();

	public String getDatabaseName();

	public String getDatabaseUser();

	public String getDatabasePassword();

	public String getPasswordSalt();

	public String getDynamicSalt(User user);

	public String getStoragePath();

}
