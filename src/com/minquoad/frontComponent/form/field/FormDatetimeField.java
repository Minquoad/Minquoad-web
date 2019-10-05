package com.minquoad.frontComponent.form.field;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeParseException;

public class FormDatetimeField extends FormStringField {

	public static final String TIMEZONE_OFFSET_KEY = "timezoneOffset";

	private Instant instant;

	public FormDatetimeField(String name) {
		super(name);
		this.addBlockingChecker((form, thisField, value) -> {
			try {
				LocalDateTime.parse(value);
				return null;
			} catch (DateTimeParseException e) {
				return form.getText("DatetimeWrongFormat");
			}
		});
	}

	public Instant getInstant() {
		if (instant == null) {
			ZoneOffset zoneOffset = null;

			try {
				String requestTimezoneOffset = this.getForm().getRequest().getParameter(TIMEZONE_OFFSET_KEY);
				int minutes = Integer.parseInt(requestTimezoneOffset);
				zoneOffset = ZoneOffset.ofHours(-minutes / 60);
			} catch (Exception e) {
				zoneOffset = ZoneOffset.UTC;
			}

			instant = LocalDateTime.parse(getValue()).toInstant(zoneOffset);
		}
		return instant;
	}

}
