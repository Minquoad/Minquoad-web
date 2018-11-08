package utilities;

import java.io.File;

public abstract class StorageManager {

	public static final String tmpFilesPath = Deployment.privateStorage + "TmpFiles/";
	public static final String logsPath = Deployment.privateStorage + "Logs/";
	public static final String uploadedPath = tmpFilesPath + "Uploaded/";

	public static final String communityPublicImgPath = Deployment.publicStorage + "img/Community/";

	static {
		initFolderIfNotExists(tmpFilesPath);
		initFolderIfNotExists(logsPath);
		initFolderIfNotExists(uploadedPath);
		initFolderIfNotExists(communityPublicImgPath);
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
