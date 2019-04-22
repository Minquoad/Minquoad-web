package com.minquoad.frontComponent.form.impl.administration;

import java.time.Instant;

import javax.servlet.http.HttpServletRequest;

import com.minquoad.dao.interfaces.DaoFactory;
import com.minquoad.entity.User;
import com.minquoad.frontComponent.form.Form;
import com.minquoad.frontComponent.form.field.FormDateField;
import com.minquoad.frontComponent.form.field.FormEntityField;

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

		FormDateField dateField = new FormDateField(DATE_KEY);
		dateField.setEmptyPermitted(false);
		this.addField(dateField);
	}

	public User getTarget() {
		@SuppressWarnings("unchecked")
		FormEntityField<User> field = (FormEntityField<User>) this.getField(TARGET_ID_KEY);
		return field.getValue();
	}

	public Instant getUnblockDate() {
		FormDateField field = (FormDateField) this.getField(DATE_KEY);
		return field.getInstant();
	}
	
}
