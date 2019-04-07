package com.minquoad.frontComponent.form.field;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;

import com.minquoad.frontComponent.form.field.valueChecker.PartValueChecker;
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
	protected void computeValueProblems() {
		List<String> problems = new ArrayList<String>();

		if (getValue() == null || !PartTool.hasFile(getValue())) {
			if (isRequired()) {
				problems.add("The file is missing.");
			}
		} else {
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
					String problem = "Unauthorized extention. Authorized extentions are : ";
					boolean first = true;
					for (String allowedExtention : allowedExtentions) {
						if (!first) {
							problem += ", ";
						}
						problem += "." + allowedExtention;
						first = false;
					}
					problems.add(problem);
				}
			}

			if (problems.isEmpty()) {
				for (PartValueChecker valueChecker : valueCheckers) {
					String valueProblem = valueChecker.getValueProblem(getForm(), this, getValue());
					if (valueProblem != null) {
						problems.add(valueProblem);
					}
				}
			}
		}
		this.setValueProblems(problems);
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
		return getOriginalFileName().substring(lastIndexOfDot+1);
	}

	@Override
	public void collectValue(HttpServletRequest request) {
		try {
			setValue(request.getPart(getName()));
		} catch (Exception e) {
		}
	}

	public Part getValue() {
		return value;
	}

	protected void setValue(Part value) {
		this.value = value;
	}

	@Override
	public String getValueAsString() {
		return this.getOriginalFileName();
	}

	public boolean hasFile() {
		return PartTool.hasFile(getValue());
	}

	public void addAllowedExtention(String extention) {
		allowedExtentions.add(extention);
	}

	public void addValueChecker(PartValueChecker valueChecker) {
		this.valueCheckers.add(valueChecker);
	}

}
