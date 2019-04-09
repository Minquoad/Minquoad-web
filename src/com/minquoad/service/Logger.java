package com.minquoad.service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

public abstract class Logger {

	public static void logError(Exception e) {
		logError(getStackTraceAsString(e));
	}

	public static void logError(String string) {
		logInFile(getDateTime() + " : " + string, StorageManager.logPath + "error.log");
	}

	public static void logWarning(String string) {
		logInFile(getDateTime() + " : " + string, StorageManager.logPath + "warning.log");
	}

	public static void logInfo(String string) {
		logInFile(getDateTime() + " : " + string, StorageManager.logPath + "info.log");
	}

	public static void logInFile(String string, String filePath) {
		try {
			File file = new File(Deployment.getStoragePath() + filePath);
			if (!file.exists()) {
				file.getParentFile().mkdirs();
				file.createNewFile();
			}

			FileWriter fw = new FileWriter(file, true);
			BufferedWriter bw = new BufferedWriter(fw);
			PrintWriter out = new PrintWriter(bw);
			out.println(string);
			out.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static String getDateTime() {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return formatter.format(new Date());
	}

	public static String getStackTraceAsString(Exception e) {

		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		e.printStackTrace(pw);
		String stackTrace = sw.toString();

		return stackTrace;
	}
	
}
