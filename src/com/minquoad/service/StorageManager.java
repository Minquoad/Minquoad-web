package com.minquoad.service;

import java.io.File;

public abstract class StorageManager {

	public static final String internalPath = "internal/";

	public static final String tmpPath = internalPath + "tmp/";
	public static final String logPath = internalPath + "log/";
	public static final String uploadedPath = tmpPath + "uploaded/";

	public static final String communityPath = "community/";

	public static void initTree() {
		initFolderIfNotExists(Deployment.storagePath);
		initFolderIfNotExists(Deployment.storagePath + internalPath);
		initFolderIfNotExists(Deployment.storagePath + tmpPath);
		initFolderIfNotExists(Deployment.storagePath + logPath);
		initFolderIfNotExists(Deployment.storagePath + uploadedPath);
		initFolderIfNotExists(Deployment.storagePath + communityPath);
	}

	public static boolean initFolderIfNotExists(String filePath) {
		File file = new File(filePath);
		if (!file.exists()) {
			file.mkdir();
			return true;
		}
		return false;
	}

}
