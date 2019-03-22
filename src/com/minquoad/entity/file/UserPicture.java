package com.minquoad.entity.file;

import com.minquoad.entity.User;

public class UserPicture extends ProtectedFile {

	private User user;
	
	@Override
	public boolean isDownloadableForUser(User user) {
		System.out.println("I have been checked");
		return true;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

}
