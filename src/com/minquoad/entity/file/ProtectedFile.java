package com.minquoad.entity.file;

import com.minquoad.entity.User;

public class ProtectedFile {

	private Integer id;
	private String relativePath;

	public boolean isDownloadableForUser(User user) {
		return true;
	}

	public String getApparentName() {
		return Integer.toString(getId());
	}
	
	public String getRelativePath() {
		return relativePath;
	}

	public void setRelativePath(String relativePath) {
		this.relativePath = relativePath;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
}
