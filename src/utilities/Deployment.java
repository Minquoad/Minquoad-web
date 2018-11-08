package utilities;

import Entities.User;

public abstract class Deployment {

	private static final Configuration configuration = new ConfigurationImpl();

	public static final String databaseUrl = configuration.getDatabaseUrl();
	public static final String databaseUser = configuration.getDatabaseUser();
	public static final String databasePassword = configuration.getDatabasePassword();
	public static final String passwordSalt = configuration.getPasswordSalt();
	public static final String privateStorage = configuration.getPrivateStorage();
	public static final String publicStorage = configuration.getPublicStorage();

	public static String getDynamicSalt(User user) {
		return configuration.getDynamicSalt(user);
	}

}
