package com.minquoad.tool.http;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;

import javax.servlet.http.Part;

import com.minquoad.service.Deployment;

public abstract class PartTool {

	public static boolean hasFile(Part part) {
		if (part == null) {
			return false;
		}
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

	public static String saveInNewFile(Part part, String directoryPath) {
		BufferedInputStream input = null;
		BufferedOutputStream output = null;
		try {
			String randomName = null;
			File newFile = null;
			while (newFile == null || newFile.exists()) {
				randomName = Long.toString(Math.abs(new Random().nextLong()));
				newFile = new File(Deployment.getStoragePath() + directoryPath + randomName);
			}

			int bufferSize = 102400;
			input = new BufferedInputStream(part.getInputStream(), bufferSize);
			output = new BufferedOutputStream(new FileOutputStream(newFile), bufferSize);

			byte[] buffer = new byte[bufferSize];
			int length;
			while ((length = input.read(buffer)) > 0) {
				output.write(buffer, 0, length);
			}

			return randomName;

		} catch (IOException e) {
			e.printStackTrace();
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
		return null;
	}

}
