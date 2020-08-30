package com.minquoad.service;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
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
		writeInFile(
				Instant.now() + " : " + string + "\n",
				ServicesManager.getService(servletContext, StorageManager.class).getFile(StorageManager.LOG_PATH + filePath));
	}

	public static void writeInFile(String string, File file) {
		try {
			if (!file.exists()) {
				file.getParentFile().mkdirs();
				file.createNewFile();
			}

			OutputStream bos = new BufferedOutputStream(new FileOutputStream(file, true));
			bos.write(string.getBytes());
			bos.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static String getStackTraceAsString(Throwable throwable) {
		StringWriter sw = new StringWriter();
		throwable.printStackTrace(new PrintWriter(sw));
		return sw.toString();
	}

}
