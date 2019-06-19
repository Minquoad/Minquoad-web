package com.minquoad.frontComponent.form.field;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;

import com.minquoad.frontComponent.form.field.valueChecker.PartValueChecker;
import com.minquoad.tool.ImageTool;
import com.minquoad.tool.http.PartTool;

public class FormFileField extends FormField {

	private Part value;
	private List<String> allowedExtentions;
	private List<PartValueChecker> valueCheckers;

	public FormFileField(String name) {
		super(name);
		allowedExtentions = new ArrayList<String>();
		valueCheckers = new ArrayList<PartValueChecker>();
	}

	@Override
	public void computeValueProblems() {
		super.computeValueProblems();
		if (!isValueNull() && !isValueEmpty()) {

			if (!allowedExtentions.isEmpty()) {
				boolean isExtentionAuthorized = false;
				String originalExtention = getOriginalExtention();
				for (String allowedExtention : allowedExtentions) {
					if (allowedExtention.equals(originalExtention)) {
						isExtentionAuthorized = true;
						break;
					}
				}
				if (!isExtentionAuthorized) {
					String problem = getText("UnauthorizedExtension") + " ";
					boolean first = true;
					for (String allowedExtention : allowedExtentions) {
						if (!first) {
							problem += ", ";
						}
						problem += "." + allowedExtention;
						first = false;
					}
					getValueProblems().add(problem);
				}
			}

			if (getValueProblems().isEmpty()) {
				for (PartValueChecker valueChecker : valueCheckers) {
					String valueProblem = valueChecker.getValueProblem(getForm(), this, getValue());
					if (valueProblem != null) {
						getValueProblems().add(valueProblem);
					}
				}
			}
		}
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
			return "";
		}
		return getOriginalFileName().substring(lastIndexOfDot + 1);
	}

	@Override
	public void collectValue(HttpServletRequest request) {
		try {
			setValue(request.getPart(getName()));
		} catch (IOException | ServletException e) {
		}
	}

	public Part getValue() {
		return value;
	}

	protected void setValue(Part value) {
		this.value = value;
	}

	public boolean isImage() {
		if (!ImageTool.getPossibleImageExtentions().contains(getOriginalExtention())) {
			return false;
		}
		if (!ImageTool.isImage(getValue())) {
			return false;
		}
		return true;
	}

	public void addAllowedExtention(String extention) {
		allowedExtentions.add(extention);
	}

	public void addValueChecker(PartValueChecker valueChecker) {
		this.valueCheckers.add(valueChecker);
	}

	@Override
	public boolean isValueEmpty() {
		return !PartTool.hasFile(getValue());
	}

	@Override
	public boolean isValueNull() {
		return getValue() == null;
	}

}
