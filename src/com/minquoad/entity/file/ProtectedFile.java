package com.minquoad.entity.file;

import java.io.File;

import com.minquoad.entity.User;
import com.minquoad.service.Deployment;

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
			throw new RuntimeException("immutability violation");
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

	public File getFile(Deployment deployment) {
		if (file == null) {
			file = new File(deployment.getStoragePath() + getRelativePath());
		}
		return file;
	}

}
