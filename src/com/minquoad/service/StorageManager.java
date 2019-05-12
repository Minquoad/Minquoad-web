package com.minquoad.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import javax.servlet.ServletContext;

import org.json.JSONObject;

public class StorageManager {

	public static final String INTERNAL_PATH = "internal/";
	public static final String TMP_PATH = INTERNAL_PATH + "tmp/";
	public static final String UPLOADED_PATH = TMP_PATH + "uploaded/";
	public static final String LOG_PATH = INTERNAL_PATH + "log/";
	public static final String COMMUNITY_PATH = "community/";

	private final ServletContext servletContext;

	public StorageManager(ServletContext servletContext) {
		this.servletContext = servletContext;
		Deployment deployment = (Deployment) servletContext.getAttribute(Deployment.class.getName());

		initFolderIfNotExists(deployment.getStoragePath());
		initStorageFolderIfNotExists(INTERNAL_PATH);
		initStorageFolderIfNotExists(TMP_PATH);
		initStorageFolderIfNotExists(LOG_PATH);
		initStorageFolderIfNotExists(UPLOADED_PATH);
		initStorageFolderIfNotExists(COMMUNITY_PATH);
	}

	public String getStoragePath(String relativePath) {
		Deployment deployment = (Deployment) servletContext.getAttribute(Deployment.class.getName());
		return deployment.getStoragePath() + relativePath;
	}

	public boolean initStorageFolderIfNotExists(String filePath) {
		return initFolderIfNotExists(getStoragePath(filePath));
	}

	public static boolean initFolderIfNotExists(String filePath) {
		File file = new File(filePath);
		if (!file.exists()) {
			file.mkdirs();
			return true;
		}
		return false;
	}

	public static JSONObject fileToJsonObject(String path) {
		return fileToJsonObject(new File(path));
	}

	public static JSONObject fileToJsonObject(File file) {
		return new JSONObject(StorageManager.fileToString(file));
	}

	public static String fileToString(String path) {
		return fileToString(new File(path));
	}

	public static String fileToString(File file) {
		BufferedReader reader = null;
		try {

			reader = new BufferedReader(new FileReader(file));
			StringBuilder stringBuilder = new StringBuilder();
			String line = null;
			String ls = System.getProperty("line.separator");
			boolean firstLine = true;
			while ((line = reader.readLine()) != null) {
				if (!firstLine) {
					stringBuilder.append(ls);
				}
				stringBuilder.append(line);
				firstLine = false;
			}

			return stringBuilder.toString();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				reader.close();
			} catch (Exception e) {
			}
		}

		return null;
	}

}
