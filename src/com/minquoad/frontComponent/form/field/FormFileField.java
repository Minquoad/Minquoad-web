package com.minquoad.frontComponent.form.field;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;

import com.minquoad.tool.http.PartTool;

public class FormFileField extends FormField {

	private Part value;
	private boolean required;

	public FormFileField(String name) {
		super(name);
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

	public String getOriginalFileName() {
		if (getValue() == null || !PartTool.hasFile(getValue())) {
			return null;
		} else {
			return PartTool.getFileName(getValue());
		}
	}

	@Override
	public void collectValue(HttpServletRequest request) {
		try {
			setValue(request.getPart(getName()));
		} catch (IOException | ServletException e) {
			e.printStackTrace();
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
		return this.getOriginalFileName();
	}

	public boolean hasFile() {
		return PartTool.hasFile(getValue());
	}

}
