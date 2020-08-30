package com.minquoad.entity.file;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;

import javax.servlet.ServletContext;
import javax.servlet.http.Part;

import com.minquoad.dao.interfaces.DaoFactory;
import com.minquoad.entity.User;
import com.minquoad.service.Deployment;
import com.minquoad.service.ServicesManager;
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

	public String getDefaultRelativePath() {

		String path = "";
		
		Class<?> clazz = this.getClass();
		while (!clazz.equals(ProtectedFile.class)) {
			path = clazz.getSimpleName() + "/" + path;
			clazz = clazz.getSuperclass();
		}

		path = StorageManager.COMMUNITY_PATH + path;

		if (this.getId() == null)
			this.setId(Math.abs(new Random().nextLong()));

		return path + Long.toString(this.getId());
	}

	public void setRelativePath(String relativePath) {
		if (this.relativePath != null && !relativePath.equals(this.relativePath))
			throw new RuntimeException("to change relativePath, use change changeRelativePath()");
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
		if (file == null)
			file = storageManager.getFile(getRelativePath());
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

	public void changeRelativePath(String relativePath, ServletContext servletContext) throws IOException {
		StorageManager storageManager = ServicesManager.getService(servletContext, StorageManager.class);

		File newFile = storageManager.getFile(relativePath);

		StorageManager.move(storageManager.getFile(this.relativePath), newFile);

		this.relativePath = relativePath;
		this.file = newFile;
	}
	
	public void collectFromPart(Part part, ServletContext servletContext) throws IOException {
		
		StorageManager storageManager = ServicesManager.getService(servletContext, StorageManager.class);

		String defaultRelativePath = this.getDefaultRelativePath();
		
		File file = storageManager.getFile(defaultRelativePath);
		StorageManager.initFolderIfNotExists(file.getParentFile());
		this.setRelativePath(defaultRelativePath);

		BufferedInputStream input = null;
		BufferedOutputStream output = null;
		try {

			input = new BufferedInputStream(part.getInputStream());
			output = new BufferedOutputStream(new FileOutputStream(file));

			byte[] buffer = new byte[ServicesManager.getService(servletContext, Deployment.class).getDefaultBufferSize()];
			int length;
			while ((length = input.read(buffer)) != -1)
				output.write(buffer, 0, length);

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
