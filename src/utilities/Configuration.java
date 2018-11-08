package utilities;

import Entities.User;

public interface Configuration {

	public String getDatabaseUrl();

	public String getDatabaseUser();

	public String getDatabasePassword();

	public String getPasswordSalt();

	public String getPrivateStorage();

	public String getPublicStorage();

	public String getDynamicSalt(User user);
	
}
