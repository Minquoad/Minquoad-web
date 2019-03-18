package com.minquoad.frontComponent.form;

public class NonNullValueChecker implements ValueChecker {

	@Override
	public String getValueProblem(String value) {
		if (value != null) {
			return null;
		} else {
			return "A field is missing.";
		}
	}

}
