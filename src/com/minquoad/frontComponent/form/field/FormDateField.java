package com.minquoad.frontComponent.form.field;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class FormDateField extends FormStringField {

	public FormDateField(String name) {
		super(name);
	}

	@Override
	public String getFormatProblem(String value) {
		try {
			LocalDate.parse(value);
			return null;
		} catch (DateTimeParseException e) {
			return "Wrong format (should be dd-mm-yyyy)";
		}
	}

}
