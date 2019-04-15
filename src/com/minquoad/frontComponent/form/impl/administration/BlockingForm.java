package com.minquoad.frontComponent.form.impl.administration;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.format.DateTimeParseException;

import javax.servlet.http.HttpServletRequest;

import com.minquoad.dao.interfaces.DaoFactory;
import com.minquoad.entity.User;
import com.minquoad.frontComponent.form.Form;
import com.minquoad.frontComponent.form.field.FormStringField;

public class BlockingForm extends Form {

	public static final String targetIdKey = "targetId";
	public static final String dateKey = "date";

	public BlockingForm(HttpServletRequest request) {
		super(request);
	}

	@Override
	protected void build() {
		FormStringField field = null;

		field = new FormStringField(targetIdKey);
		field.setEmptyPermitted(false);
		this.addField(field);

		field = new FormStringField(dateKey);
		field.setEmptyPermitted(false);
		this.addField(field);
	}

	public User getTarget() {
		FormStringField field = (FormStringField) this.getField(targetIdKey);
		return field.getValueAsEntity(DaoFactory::getUserDao);
	}

	public Instant getUnblockDate() {
		FormStringField field = (FormStringField) this.getField(dateKey);
		LocalDate date;
		try {
			date = LocalDate.parse(field.getValue());
		} catch (DateTimeParseException e) {
			return null;
		}
		return date.atStartOfDay().toInstant(ZoneOffset.UTC);
	}
	
}
