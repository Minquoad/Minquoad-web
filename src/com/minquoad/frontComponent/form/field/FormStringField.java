package com.minquoad.frontComponent.form.field;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.minquoad.frontComponent.form.field.valueChecker.StringValueChecker;

public class FormStringField extends FormField {

	private String value;
	private List<StringValueChecker> valueCheckers;

	public FormStringField(String name) {
		super(name);
		valueCheckers = new ArrayList<StringValueChecker>();
	}

	protected void computeValueProblems() {
		List<String> problems = new ArrayList<String>();
		
		if (getValue() == null) {
			if (isRequired()) {
				problems.add("The field is missing.");
			}
		} else {
			for (StringValueChecker valueChecker : valueCheckers) {
				String valueProblem = valueChecker.getValueProblem(getForm(), this, getValue());
				if (valueProblem != null) {
					problems.add(valueProblem);
				}
			}
		}
		
		this.setValueProblems(problems);
	}

	public void collectValue(HttpServletRequest request) {
		this.setValue(request.getParameter(getName()));
	}

	public void addValueChecker(StringValueChecker valueChecker) {
		this.valueCheckers.add(valueChecker);
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getValueAsString() {
		return getValue();
	}

}
