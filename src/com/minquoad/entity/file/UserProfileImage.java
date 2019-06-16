package com.minquoad.entity.file;

import com.minquoad.dao.interfaces.DaoFactory;
import com.minquoad.entity.User;

public class UserProfileImage extends ProtectedFile {

	private User user;

	@Override
	public boolean isDownloadableForUser(User user, DaoFactory daoFactory) {
		return user != null;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

}
