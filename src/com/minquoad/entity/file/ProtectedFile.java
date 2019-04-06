package com.minquoad.entity.file;

import java.io.File;

import com.minquoad.entity.User;
import com.minquoad.service.Deployment;
import com.minquoad.service.Logger;

public class ProtectedFile {

	private Long id;
	private String relativePath;
	private String originalName;
	private String originalExtention;

	private File file;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getRelativePath() {
		return relativePath;
	}

	public void setRelativePath(String relativePath) {
		// relativePath is immutable
		if (this.relativePath != null && !relativePath.equals(this.relativePath)) {
			Exception e = new Exception("immutability violation");
			e.printStackTrace();
			Logger.logError(e);
		}
		this.relativePath = relativePath;
	}

	public String getOriginalName() {
		return originalName;
	}

	public void setOriginalName(String originalName) {
		this.originalName = originalName;
	}

	public String getOriginalExtention() {
		return originalExtention;
	}

	public void setOriginalExtention(String originalExtention) {
		this.originalExtention = originalExtention;
	}

	public boolean isDownloadableForUser(User user) {
		return true;
	}

	public String getApparentName() {
		return getOriginalName() + '.' + getOriginalExtention();
	}

	public File getFile() {
		if (file == null) {
			file = new File(Deployment.getStoragePath() + getRelativePath());
		}
		return file;
	}

}
