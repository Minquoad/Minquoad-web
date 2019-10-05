package com.minquoad.frontComponent.form.field;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeParseException;

import javax.servlet.http.HttpServletRequest;

public class FormDatetimeField extends FormStringField {

	public static final String TIMEZONE_OFFSET_KEY = "timezoneOffset";

	private Instant instant;
	private ZoneOffset zoneOffset;

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

	@Override
	public void collectValue(HttpServletRequest request) {
		super.collectValue(request);

		try {
			String timezoneOffsetString = request.getParameter(getName() + "-" + TIMEZONE_OFFSET_KEY);
			int minutes = Integer.parseInt(timezoneOffsetString);
			zoneOffset = ZoneOffset.ofHours(-minutes / 60);
		} catch (Exception e) {
			zoneOffset = ZoneOffset.UTC;
		}
	}

	public Instant getInstant() {
		if (instant == null) {
			instant = LocalDateTime.parse(getValue()).toInstant(zoneOffset);
		}
		return instant;
	}

}
