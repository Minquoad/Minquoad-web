package com.minquoad.frontComponent.form.field;

import javax.servlet.http.HttpServletRequest;

public class FormStringField extends FormField<String> {

	private boolean trimingValue;

	public FormStringField(String name) {
		super(name);
		setTrimingValue(true);
	}

	@Override
	public void collectValue(HttpServletRequest request) {
		this.setValue(request.getParameter(getName()));
	}

	@Override
	public void setValue(String value) {
		if (value != null) {
			value = value.replace("\r", "");
			if (isTrimingValue()) {
				value = value.trim();
			}
		}
		super.setValue(value);
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
	public boolean isValueEmpty() {
		return getValue().isEmpty();
	}

	@Override
	public boolean isValueNull() {
		return getValue() == null;
	}

}
