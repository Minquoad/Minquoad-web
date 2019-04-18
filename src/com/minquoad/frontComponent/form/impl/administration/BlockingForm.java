package com.minquoad.frontComponent.form.impl.administration;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.format.DateTimeParseException;

import javax.servlet.http.HttpServletRequest;

import com.minquoad.dao.interfaces.DaoFactory;
import com.minquoad.entity.User;
import com.minquoad.frontComponent.form.Form;
import com.minquoad.frontComponent.form.field.FormEntityField;
import com.minquoad.frontComponent.form.field.FormStringField;

public class BlockingForm extends Form {

	public static final String TARGET_ID_KEY = "targetId";
	public static final String DATE_KEY = "date";

	public BlockingForm(HttpServletRequest request) {
		super(request);
	}

	@Override
	protected void build() {

		FormEntityField<User> targetField = 
				new FormEntityField<User>(TARGET_ID_KEY, DaoFactory::getUserDao);

		targetField.setEmptyPermitted(false);
		targetField.addValueChecker((form, thisField, value) -> {
			if (getUser().canAdminister(value)) {
				return null;
			} else {
				return "administration hierarchy not respected";
			}
		});
		this.addField(targetField);

		FormStringField dateField = new FormStringField(DATE_KEY);
		dateField.setEmptyPermitted(false);
		this.addField(dateField);
	}

	public User getTarget() {
		@SuppressWarnings("unchecked")
		FormEntityField<User> field = (FormEntityField<User>) this.getField(TARGET_ID_KEY);
		return field.getValue();
	}

	public Instant getUnblockDate() {
		FormStringField field = (FormStringField) this.getField(DATE_KEY);
		LocalDate date;
		try {
			date = LocalDate.parse(field.getValue());
		} catch (DateTimeParseException e) {
			return null;
		}
		return date.atStartOfDay().toInstant(ZoneOffset.UTC);
	}
	
}
