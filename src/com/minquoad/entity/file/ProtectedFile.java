package com.minquoad.entity.file;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;

import javax.servlet.http.Part;

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
		return getOriginalName().substring(lastIndexOfDot + 1).toLowerCase();
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

	public void collectFromPart(Part part, StorageManager storageManager) throws IOException {

		String randomDirPath = StorageManager.COMMUNITY_PATH;
		randomDirPath += getRandomIntString(3) + "/";
		randomDirPath += getRandomIntString(3) + "/";
		StorageManager.initFolderIfNotExists(storageManager.getFile(randomDirPath));

		File file = null;
		String randomPath = null;
		while (file == null || file.exists()) {
			randomPath = randomDirPath + getRandomIntString(9);
			file = storageManager.getFile(randomPath);
		}
		this.setRelativePath(randomPath);

		BufferedInputStream input = null;
		BufferedOutputStream output = null;
		try {

			int bufferSize = 102400;
			input = new BufferedInputStream(part.getInputStream(), bufferSize);
			output = new BufferedOutputStream(new FileOutputStream(file), bufferSize);

			byte[] buffer = new byte[bufferSize];
			int length;
			while ((length = input.read(buffer)) > 0) {
				output.write(buffer, 0, length);
			}

		} finally {
			try {
				output.close();
			} catch (Exception ignored) {
			}
			try {
				input.close();
			} catch (Exception ignored) {
			}
		}
	}

	public static String getRandomIntString(int digits) {
		int max = (int) Math.pow(10, digits);
		String string = Integer.toString(new Random().nextInt(max));
		while (string.length() < digits) {
			string = "0" + string;
		}
		return string;
	}
	
}
