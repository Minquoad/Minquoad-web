package com.minquoad.framework.form;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;

public class FormFileField extends FormField<Part> {

	private Collection<String> allowedExtentions;

	public FormFileField(String name) {
		super(name);

		allowedExtentions = new HashSet<String>();

		this.addBlockingChecker((form, thisField, value) -> {
			if (allowedExtentions.isEmpty()) {
				return null;
			}
			if (allowedExtentions.contains(getOriginalExtention())) {
				return null;
			}
			String problem = form.getText("UnauthorizedExtension") + " ";
			boolean first = true;
			for (String allowedExtention : allowedExtentions) {
				if (!first) {
					problem += ", ";
				}
				problem += "." + allowedExtention;
				first = false;
			}
			return problem;
		});
	}

	public String getOriginalFileName() {
		return getFileName(getValue());
	}

	public String getOriginalExtention() {
		int lastIndexOfDot = getOriginalFileName().lastIndexOf('.');
		if (lastIndexOfDot == -1) {
			return null;
		}
		return getOriginalFileName().substring(lastIndexOfDot + 1).toLowerCase();
	}

	public void addAllowedExtention(String extention) {
		allowedExtentions.add(extention);
	}

	public void addAllowedExtention(Collection<String> extentions) {
		allowedExtentions.addAll(extentions);
	}

	@Override
	public boolean isValueEmpty() {
		return !hasFile(getValue());
	}

	@Override
	public boolean isValueNull() {
		return getValue() == null;
	}

	@Override
	public void collectValue(HttpServletRequest request) {
		try {
			setValue(request.getPart(getName()));
		} catch (IOException | ServletException e) {
			e.printStackTrace();
		}
	}

	public boolean isImage() {
		Collection<String> possibleImageExtentions = this.getPossibleImageExtentions();
		if (possibleImageExtentions != null
				&& !possibleImageExtentions.isEmpty()
				&& !possibleImageExtentions.contains(getOriginalExtention())) {
			return false;
		}
		if (!this.isFileImage(getValue())) {
			return false;
		}
		return true;
	}

	public Collection<String> getPossibleImageExtentions() {
		String[] table = { "gif", "jpeg", "jpg", "png", "bmp" };
		return Arrays.asList(table);
	}

	public boolean isFileImage(Part part) {
		try {
			return ImageIO.read(part.getInputStream()) != null;
		} catch (Exception e) {
			return false;
		}
	}

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

}
