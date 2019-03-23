package com.minquoad.entity.file;

import java.io.File;
import java.time.Instant;

import com.minquoad.entity.User;
import com.minquoad.service.Deployment;

public class ProtectedFile {

	private Integer id;
	private String relativePath;
	private String originalName;
	private String originalExtention;
	private Instant lastModificationDate;

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

	public Instant getLastModificationDate() {
		return lastModificationDate;
	}

	public void setLastModificationDate(Instant lastModificationDate) {
		this.lastModificationDate = lastModificationDate;
	}

	public boolean isDownloadableForUser(User user) {
		return true;
	}

	public String getApparentName() {
		return Integer.toString(getId()) + '.' + getOriginalExtention();
	}

	public File getFile() {
		if (file == null) {
			file = new File(Deployment.storagePath + getRelativePath());
		}
		return file;
	}

	public String getMimeType() {
		return null;
	}

}
