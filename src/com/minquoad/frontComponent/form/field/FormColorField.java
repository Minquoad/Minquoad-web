package com.minquoad.frontComponent.form.field;

public class FormColorField extends FormStringField {

	public FormColorField(String name) {
		super(name);
	}

	@Override
	public Integer getValueAsInteger() {
		try {
			return Integer.parseInt(getValue().substring(1), 16);
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public String getFormatProblem(String value) {
		if (value.length() == "#0f0f0f".length() && value.charAt(0) == '#') {
			try {
				Integer.parseInt(value.substring(1), 16);
				return null;
			} catch (Exception e) {
			}
		}
		return "Wrong format (should be like #0f0f0f)";
	}

}
