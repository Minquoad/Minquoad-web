package com.minquoad.frontComponent.form.field;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.minquoad.frontComponent.form.field.valueChecker.ValueChecker;

public class FormStringField extends FormField {

	private String value;
	private List<ValueChecker> valueCheckers;

	public FormStringField(String name) {
		super(name);
		valueCheckers = new ArrayList<ValueChecker>();
	}

	protected void computeValueProblems() {
		List<String> problems = new ArrayList<String>();
		for (ValueChecker valueChecker : valueCheckers) {
			String valueProblem = valueChecker.getValueProblem(getValue());
			if (valueProblem != null) {
				problems.add(valueProblem);
			}
		}
		this.setValueProblems(problems);
	}

	public void collectValue(HttpServletRequest request) {
		this.value = request.getParameter(getName());
	}

	public void addValueChecker(ValueChecker valueChecker) {
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
