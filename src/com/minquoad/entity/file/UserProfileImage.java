package com.minquoad.entity.file;

import com.minquoad.dao.interfaces.DaoFactory;
import com.minquoad.entity.User;
import com.minquoad.unit.UnitFactory;

public class UserProfileImage extends ProtectedFile {

	private User user;

	@Override
	public boolean isDownloadableForUser(User user, DaoFactory daoFactory, UnitFactory unitFactory) {
		return user != null;
	}

	@Override
	public String getContentDispositionFileName(User user, DaoFactory daoFactory, UnitFactory unitFactory) {
		return getIdBaseName();
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

}
