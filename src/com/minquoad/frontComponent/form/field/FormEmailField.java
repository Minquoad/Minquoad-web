package com.minquoad.frontComponent.form.field;

public class FormEmailField extends FormStringField {

	public FormEmailField(String name) {
		super(name);
	}

	@Override
	public void setValue(String value) {
		super.setValue(value);
		if (getValue() != null) {
			super.setValue(getValue().toLowerCase());
		}
	}

	@Override
	public String getFormatProblem(String value) {
		if (!value.matches(
				"(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?"
						+ ":[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\"
						+ "x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-"
						+ "9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|"
						+ "[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-"
						+ "9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7"
						+ "f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])")) {

			return getText("EmailWrongFormat");
		}
		return null;
	}

}
