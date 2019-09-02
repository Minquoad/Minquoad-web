package com.minquoad.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import javax.servlet.ServletContext;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class StorageManager {

	public static final String INTERNAL_PATH = "internal/";
	public static final String TMP_PATH = INTERNAL_PATH + "tmp/";
	public static final String UPLOADED_PATH = TMP_PATH + "uploaded/";
	public static final String LOG_PATH = INTERNAL_PATH + "log/";
	public static final String COMMUNITY_PATH = "community/";

	private final ServletContext servletContext;

	public StorageManager(ServletContext servletContext) {
		this.servletContext = servletContext;

		initFolderIfNotExists(getFile(""));
		initFolderIfNotExists(getFile(INTERNAL_PATH));
		initFolderIfNotExists(getFile(TMP_PATH));
		initFolderIfNotExists(getFile(LOG_PATH));
		initFolderIfNotExists(getFile(UPLOADED_PATH));
		initFolderIfNotExists(getFile(COMMUNITY_PATH));
	}

	public File getFile(String relativePath) {
		return new File(ServicesManager.getService(servletContext, Deployment.class).getStoragePath() + relativePath);
	}

	public static boolean initFolderIfNotExists(File file) {
		if (!file.exists()) {
			file.mkdirs();
			return true;
		}
		return false;
	}

	public static JsonNode fileToJsonNode(File file) throws IOException {
		return new ObjectMapper().readTree(StorageManager.fileToString(file));
	}

	public static String fileToString(File file) throws IOException {
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

		} finally {
			try {
				reader.close();
			} catch (Exception e) {
			}
		}
	}

}
