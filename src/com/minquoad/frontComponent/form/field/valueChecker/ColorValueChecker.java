package com.minquoad.frontComponent.form.field.valueChecker;

import com.minquoad.frontComponent.form.Form;
import com.minquoad.frontComponent.form.field.FormStringField;

public class ColorValueChecker implements StringValueChecker {

	@Override
	public String getValueProblem(Form form, FormStringField field, String value) {
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
