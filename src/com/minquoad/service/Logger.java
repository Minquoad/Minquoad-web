package com.minquoad.service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.Instant;

import javax.servlet.ServletContext;

public class Logger {

	private final ServletContext servletContext;

	public Logger(ServletContext servletContext) {
		this.servletContext = servletContext;
	}

	public void logError(Throwable throwable) {
		logError(getStackTraceAsString(throwable));
	}

	public void logError(String string) {
		standartLogInFile(string, "error.log");
	}

	public void logWarning(String string) {
		standartLogInFile(string, "warning.log");
	}

	public void logInfo(String string) {
		standartLogInFile(string, "info.log");
	}

	private void standartLogInFile(String string, String filePath) {
		logInFile(
				Instant.now() + " : " + string,
				ServicesManager.getService(servletContext, StorageManager.class).getFile(StorageManager.LOG_PATH + filePath));
	}

	public void logInFile(String string, File file) {
		try {
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

	public static String getStackTraceAsString(Throwable throwable) {

		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		throwable.printStackTrace(pw);
		String stackTrace = sw.toString();

		return stackTrace;
	}

}
