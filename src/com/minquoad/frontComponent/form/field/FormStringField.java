package com.minquoad.frontComponent.form.field;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.minquoad.frontComponent.form.field.valueChecker.StringValueChecker;

public class FormStringField extends FormField {

	private String value;
	private List<StringValueChecker> valueCheckers;
	private boolean trimingValue;

	public FormStringField(String name) {
		super(name);
		valueCheckers = new ArrayList<StringValueChecker>();
		setTrimingValue(true);
	}

	@Override
	public void computeValueProblems() {
		super.computeValueProblems();
		if (!isValueNull() && !isValueEmpty()) {
			String formatProblem = getFormatProblem(getValue());
			if (formatProblem != null) {
				getValueProblems().add(formatProblem);
			} else {
				for (StringValueChecker valueChecker : valueCheckers) {
					String valueProblem = valueChecker.getValueProblem(getForm(), this, getValue());
					if (valueProblem != null) {
						getValueProblems().add(valueProblem);
					}
				}
			}
		}
	}

	public String getFormatProblem(String value) {
		return null;
	}

	@Override
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
		if (value != null) {
			value = value.replace("\r", "");
			if (isTrimingValue()) {
				value = value.trim();
			}
		}
		this.value = value;
	}

	public Integer getValueAsInteger() {
		try {
			return Integer.parseInt(getValue());
		} catch (Exception e) {
			return null;
		}
	}

	public Float getValueAsFloat() {
		try {
			return Float.parseFloat(getValue());
		} catch (Exception e) {
			return null;
		}
	}

	public boolean isTrimingValue() {
		return trimingValue;
	}

	public void setTrimingValue(boolean trimingValue) {
		this.trimingValue = trimingValue;
	}

	@Override
	protected boolean isValueEmpty() {
		return getValue().isEmpty();
	}

	@Override
	protected boolean isValueNull() {
		return getValue() == null;
	}

}
