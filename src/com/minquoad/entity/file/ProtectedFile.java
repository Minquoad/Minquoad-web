package com.minquoad.entity.file;

import java.io.File;

import com.minquoad.dao.interfaces.DaoFactory;
import com.minquoad.entity.User;
import com.minquoad.service.StorageManager;
import com.minquoad.unit.UnitFactory;

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
			return null;
		}
		return getOriginalName().substring(lastIndexOfDot+1);
	}

	public File getFile(StorageManager storageManager) {
		if (file == null) {
			file = storageManager.getFile(getRelativePath());
		}
		return file;
	}

	public String getIdBaseName() {
		String idString = Long.toString(this.getId());
		String extention = this.getOriginalExtention();
		if (extention == null) {
			return idString;
		} else {
			return idString + "." + extention;
		}
	}
	
	public boolean isDownloadableForUser(User user, DaoFactory daoFactory, UnitFactory unitFactory) {
		return true;
	}

	public String getContentDispositionFileName(User user, DaoFactory daoFactory, UnitFactory unitFactory) {
		return this.getOriginalName();
	}

}
