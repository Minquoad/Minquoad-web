package com.minquoad.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import org.json.JSONObject;

public abstract class StorageManager {

	public static final String internalPath = "internal/";

	public static final String tmpPath = internalPath + "tmp/";
	public static final String logPath = internalPath + "log/";
	public static final String uploadedPath = tmpPath + "uploaded/";

	public static final String communityPath = "community/";

	public static void initTree() {
		initFolderIfNotExists(Deployment.getStoragePath());
		initStorageFolderIfNotExists(internalPath);
		initStorageFolderIfNotExists(tmpPath);
		initStorageFolderIfNotExists(logPath);
		initStorageFolderIfNotExists(uploadedPath);
		initStorageFolderIfNotExists(communityPath);
	}

	public static boolean initStorageFolderIfNotExists(String filePath) {
		return initFolderIfNotExists(Deployment.getStoragePath() + filePath);
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
