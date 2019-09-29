package com.minquoad.tool;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.HashSet;

import javax.imageio.ImageIO;
import javax.servlet.http.Part;

public abstract class ImageTool {

	public static Collection<String> getPossibleImageExtentions() {
		Collection<String> possibleImageExtentions = new HashSet<String>();

		for (Extention extention : Extention.values()) {
			possibleImageExtentions.add(extention.toString());
		}

		return possibleImageExtentions;
	}

	public static boolean isImage(Part part) {
		try {
			return isImage(part.getInputStream());
		} catch (IOException e) {
			return false;
		}
	}

	public static boolean isImage(File file) {
		try {
			return ImageIO.read(file) != null;
		} catch (Exception e) {
			return false;
		}
	}

	public static boolean isImage(InputStream stream) {
		try {
			return ImageIO.read(stream) != null;
		} catch (Exception e) {
			return false;
		}
	}

	public enum Extention {
		gif(),
		jpeg(),
		jpg(),
		png(),
		bmp();
	}

}
