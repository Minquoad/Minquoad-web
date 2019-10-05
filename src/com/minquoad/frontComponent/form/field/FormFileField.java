package com.minquoad.frontComponent.form.field;

import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;

import com.minquoad.tool.ImageTool;
import com.minquoad.tool.http.PartTool;

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
		if (getValue() == null || !PartTool.hasFile(getValue())) {
			return null;
		} else {
			return PartTool.getFileName(getValue());
		}
	}

	public String getOriginalExtention() {
		int lastIndexOfDot = getOriginalFileName().lastIndexOf('.');
		if (lastIndexOfDot == -1) {
			return null;
		}
		return getOriginalFileName().substring(lastIndexOfDot + 1).toLowerCase();
	}

	@Override
	public void collectValue(HttpServletRequest request) {
		try {
			setValue(request.getPart(getName()));
		} catch (IOException | ServletException e) {
		}
	}

	public boolean isImage() {
		if (!this.getPossibleImageExtentions().contains(getOriginalExtention())) {
			return false;
		}
		if (!this.isImage(getValue())) {
			return false;
		}
		return true;
	}

	public void addAllowedExtention(String extention) {
		allowedExtentions.add(extention);
	}

	public void addAllowedExtention(Collection<String> extentions) {
		allowedExtentions.addAll(extentions);
	}

	@Override
	public boolean isValueEmpty() {
		return !PartTool.hasFile(getValue());
	}

	@Override
	public boolean isValueNull() {
		return getValue() == null;
	}

	public Collection<String> getPossibleImageExtentions() {
		return ImageTool.getPossibleImageExtentions();
	}

	public boolean isImage(Part part) {
		return ImageTool.isImage(part);
	}
	
}
