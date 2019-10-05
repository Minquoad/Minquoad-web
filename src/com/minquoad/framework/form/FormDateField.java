package com.minquoad.framework.form;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeParseException;

import javax.servlet.http.HttpServletRequest;

public class FormDateField extends FormStringField {

	public static final String TIMEZONE_OFFSET_KEY = "timezoneOffset";

	private LocalDateToLocalDateTimeConverter converter;
	private Instant instant;
	private ZoneOffset zoneOffset;

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
