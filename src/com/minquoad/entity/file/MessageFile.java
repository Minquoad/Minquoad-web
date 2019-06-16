package com.minquoad.entity.file;

import com.minquoad.dao.interfaces.DaoFactory;
import com.minquoad.entity.User;

public class MessageFile extends ProtectedFile {

	@Override
	public boolean isDownloadableForUser(User user, DaoFactory daoFactory) {
		return true;
	}

}
