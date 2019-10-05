package com.minquoad.frontComponent.form.field;

public class FormColorField extends FormStringField {

	public FormColorField(String name) {
		super(name);
		this.addBlockingChecker((form, thisField, value) -> {
			if (value.length() == "#123456".length() && value.charAt(0) == '#') {
				try {
					getValueAsInteger();
					return null;
				} catch (Exception e) {
				}
			}
			return form.getText("ColorWrongFormat");
		});
	}

	@Override
	public Integer getValueAsInteger() {
		return Integer.parseInt(getValue().substring(1), 16);
	}

}
