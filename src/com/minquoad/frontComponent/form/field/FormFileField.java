package com.minquoad.frontComponent.form.field;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;

import com.minquoad.tool.http.PartTool;

public class FormFileField extends FormField {

	private Part value;
	private boolean required;

	public FormFileField(String name) {
		super(name);
		required = false;
	}

	@Override
	protected void computeValueProblems() {
		List<String> problems = new ArrayList<String>();
		if (isRequired()) {
			if (getValue() == null) {
				problems.add("A field is missing.");
			} else {
				if (!PartTool.hasFile(getValue())) {
					problems.add("The file is missing.");
				}
			}
		}
		this.setValueProblems(problems);
	}

	public String getOriginalFileName(boolean withExtention) {
		if (getValue() == null || !PartTool.hasFile(getValue())) {
			return null;
		} else {
			String fileName = PartTool.getFileName(getValue());

			if (withExtention) {
				return fileName;
			}

			int lastIndexOf = fileName.lastIndexOf('.');
			if (lastIndexOf == -1) {
				return fileName;
			} else {
				return fileName.substring(0, lastIndexOf);
			}

		}
	}

	public String getOriginalFileExtention() {
		if (getValue() == null || !PartTool.hasFile(getValue())) {
			return null;
		}
		String fileName = PartTool.getFileName(getValue());

		int lastIndexOf = fileName.lastIndexOf('.');
		if (lastIndexOf == -1) {
			return "";
		} else {
			return fileName.substring(lastIndexOf + 1);
		}
	}

	@Override
	public void collectValue(HttpServletRequest request) {
		try {
			setValue(request.getPart(getName()));
		} catch (Exception e) {
		}
	}

	public boolean isRequired() {
		return required;
	}

	public void setRequired(boolean required) {
		this.required = required;
	}

	public Part getValue() {
		return value;
	}

	protected void setValue(Part value) {
		this.value = value;
	}

	@Override
	public String getValueAsString() {
		return this.getOriginalFileName(true);
	}

	public boolean hasFile() {
		return PartTool.hasFile(getValue());
	}

}
