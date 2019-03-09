package com.minquoad.entity.file;

import com.minquoad.entity.User;
import com.minquoad.framework.dao.Entity;

public class ProtectedFile extends Entity {

	private String storagePath;

	public boolean isDownloadableForUser(User user) {
		return true;
	}

	public String getStoragePath() {
		return storagePath;
	}

	public void setStoragePath(String storagePath) {
		this.storagePath = storagePath;
	}
	
}
