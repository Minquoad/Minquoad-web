package com.minquoad.entity.file;

import com.minquoad.entity.User;

public class UserPicture extends ProtectedFile {

	@Override
	public boolean isDownloadableForUser(User user) {
		System.out.println("I have been checked");
		return true;
	}

}
