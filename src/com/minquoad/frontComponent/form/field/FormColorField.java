package com.minquoad.frontComponent.form.field;

public class FormColorField extends FormStringField {

	public FormColorField(String name) {
		super(name);
	}

	@Override
	public void computeValueProblems() {
		super.computeValueProblems();
		if (!isValueNull() && !isValueEmpty()) {
			
			if (getValue().length() != "#0f0f0f".length() || getValue().charAt(0) != '#') {
				
				getValueProblems().add("Wrong format (should be like #0f0f0f)");

			} else {
				try {
					Integer.parseInt(getValue().substring(1), 16);
				} catch (Exception e) {
					getValueProblems().add("Wrong format (should be like #0f0f0f)");
				}
			}
		}
	}

	@Override
	public Integer getValueAsInteger() {
		try {
			return Integer.parseInt(getValue().substring(1), 16);
		} catch (Exception e) {
			return null;
		}
	}

}
