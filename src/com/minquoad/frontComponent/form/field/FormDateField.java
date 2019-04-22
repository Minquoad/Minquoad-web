package com.minquoad.frontComponent.form.field;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.format.DateTimeParseException;

import com.minquoad.tool.TimeTool;

public class FormDateField extends FormStringField {

	private Instant instant;
	private LocalDate localDate;

	public FormDateField(String name) {
		super(name);
	}

	@Override
	public String getFormatProblem(String value) {
		try {
			if (value.matches("([12]\\d{3}-(0[1-9]|1[0-2])-(0[1-9]|[12]\\d|3[01]))")) {
				LocalDate.parse(value);
				return null;
			}
		} catch (DateTimeParseException e) {
		}
		return "Wrong format (should be dd-mm-yyyy)";
	}

	public Instant getInstant() {
		if (instant == null) {
			instant = TimeTool.toInstant(getValue(), ZoneOffset.UTC);
		}
		return instant;
	}

	public LocalDate getLocalDate() {
		if (localDate == null) {
			localDate = TimeTool.toLocalDate(getValue());
		}
		return localDate;
	}

}
