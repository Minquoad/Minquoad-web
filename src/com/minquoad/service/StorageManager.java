package com.minquoad.service;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;

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

	public static void move(File source, File destination) throws IOException {
		StorageManager.initFolderIfNotExists(destination.getParentFile());
		Files.move(source.toPath(), destination.toPath());
	}

	public static boolean initFolderIfNotExists(File file) {
		if (!file.exists()) {
			file.mkdirs();
			return true;
		}
		return false;
	}

	public static JsonNode fileToJsonNode(File file) throws IOException {
		return new ObjectMapper().readTree(fileToString(file));
	}

	public static String fileToString(File file) throws IOException {

		BufferedInputStream inputStream = null;
		try {
			inputStream = new BufferedInputStream(new FileInputStream(file));
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

			byte[] buffer = new byte[8192];//Deployment.defaultBufferSize may not be initialised at this point
			int length;
			while ((length = inputStream.read(buffer)) != -1)
				outputStream.write(buffer, 0, length);

			return new String(outputStream.toByteArray());

		} finally {
			inputStream.close();
		}

	}

}
