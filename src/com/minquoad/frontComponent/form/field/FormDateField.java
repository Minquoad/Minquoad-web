package com.minquoad.frontComponent.form.field;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeParseException;

public class FormDateField extends FormStringField {

	public static final String TIMEZONE_OFFSET_KEY = "timezoneOffset";

	private LocalDateToLocalDateTimeConverter converter;
	private Instant instant;

	public FormDateField(String name) {
		super(name);
		setConverter(LocalDate::atStartOfDay);
		this.addBlockingChecker((form, thisField, value) -> {
			try {
				LocalDate.parse(value);
				return null;
			} catch (DateTimeParseException e) {
				return form.getText("DateWrongFormat");
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

			instant = getConverter().toLocalDateTime(LocalDate.parse(getValue())).toInstant(zoneOffset);
		}
		return instant;
	}

	public LocalDateToLocalDateTimeConverter getConverter() {
		return converter;
	}

	public void setConverter(LocalDateToLocalDateTimeConverter converter) {
		this.converter = converter;
	}

	public interface LocalDateToLocalDateTimeConverter {
		public LocalDateTime toLocalDateTime(LocalDate date);
	}
	
}
