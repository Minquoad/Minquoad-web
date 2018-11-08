package utilities;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

public abstract class Logger {

	public static void echoInfo(String string) {
		System.out.println("INFO : " + string);
	}

	public static void logInfo(String string) {
		logInFile(getDateTime() + " : " + string, StorageManager.logsPath + "Infos.log");
	}

	public static void logInFile(String string, String filePath) {
		try {
			File file = new File(filePath);
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

}
