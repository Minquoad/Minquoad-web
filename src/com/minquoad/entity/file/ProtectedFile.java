package com.minquoad.entity.file;

import java.io.File;

import com.minquoad.entity.User;
import com.minquoad.service.Deployment;
import com.minquoad.service.Logger;

public class ProtectedFile {

	private Integer id;
	private String relativePath;
	private String originalName;
	private String originalExtention;

	private File file;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getRelativePath() {
		return relativePath;
	}

	public void setRelativePath(String relativePath) {
		// relativePath is immutable
		if (this.relativePath == null) {
			this.relativePath = relativePath;
		} else {
			if (relativePath != this.relativePath) {
				Exception e = new Exception("immutability violation");
				e.printStackTrace();
				Logger.logError(e);
			}
		}
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
