package com.minquoad.tool.http;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;

import javax.servlet.http.Part;

import com.minquoad.entity.file.ProtectedFile;
import com.minquoad.service.Deployment;
import com.minquoad.service.StorageManager;

public abstract class PartTool {

	public static boolean hasFile(Part part) {
		String fileName = getFileName(part);
		return fileName != null && !fileName.isEmpty();
	}

	public static String getFileName(Part part) {
		for (String contentDisposition : part.getHeader("content-disposition").split(";")) {
			if (contentDisposition.trim().startsWith("filename")) {
				return contentDisposition.substring(contentDisposition.indexOf('=') + 1).trim().replace("\"", "");
			}
		}
		return null;
	}

	public static void saveInProtectedFile(Part part, ProtectedFile protectedFile) throws IOException {

		File newFile = null;
		String randomPath = null;

		Random random = new Random();

		String randomDirPath = StorageManager.communityPath;
		for (int i = 0; i < 2; i++) {
			randomDirPath += random.nextInt(10) + "/";
		}
		StorageManager.initStorageFolderIfNotExists(randomDirPath);
		while (newFile == null || newFile.exists()) {
			String randomFileName = Integer.toString(Math.abs(random.nextInt(1000000000)));
			while (randomFileName.length() < 9) {
				randomFileName = "0" + randomFileName;
			}
			randomPath = randomDirPath + randomFileName;

			newFile = new File(Deployment.getStoragePath() + randomPath);
		}

		saveInFile(part, newFile);
		protectedFile.setRelativePath(randomPath);
	}

	public static void saveInFile(Part part, File file) throws IOException {
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

		} catch (IOException e) {
			e.printStackTrace();
			throw e;
		} finally {
			try {
				output.close();
			} catch (IOException ignore) {
			}
			try {
				input.close();
			} catch (IOException ignore) {
			}
		}
	}
}
