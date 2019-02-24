package utility;

import java.io.File;

public abstract class StorageManager {

	public static final String internalPath = Deployment.storagePath + "internal/";

	public static final String tmpPath = internalPath + "tmp/";
	public static final String logPath = internalPath + "log/";
	public static final String uploadedPath = tmpPath + "uploaded/";

	public static final String communityPath = Deployment.storagePath + "community/";

	public static void initTree() {
		initFolderIfNotExists(Deployment.storagePath);
		initFolderIfNotExists(internalPath);
		initFolderIfNotExists(tmpPath);
		initFolderIfNotExists(logPath);
		initFolderIfNotExists(uploadedPath);
		initFolderIfNotExists(communityPath);
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
