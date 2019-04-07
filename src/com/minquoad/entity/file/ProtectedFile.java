package com.minquoad.entity.file;

import java.io.File;

import com.minquoad.entity.User;
import com.minquoad.service.Deployment;
import com.minquoad.service.Logger;

public class ProtectedFile {

	private Long id;
	private String relativePath;
	private String originalName;

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
		int lastIndexOfDot = getOriginalName().lastIndexOf('.');
		if (lastIndexOfDot == -1) {
			return "";
		}
		return getOriginalName().substring(lastIndexOfDot+1);
	}

	public boolean isDownloadableForUser(User user) {
		return true;
	}

	public File getFile() {
		if (file == null) {
			file = new File(Deployment.getStoragePath() + getRelativePath());
		}
		return file;
	}

}
