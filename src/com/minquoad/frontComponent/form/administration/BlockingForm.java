package com.minquoad.frontComponent.form.administration;

import java.time.Instant;

import javax.servlet.http.HttpServletRequest;

import com.minquoad.dao.interfaces.DaoFactory;
import com.minquoad.entity.User;
import com.minquoad.framework.form.FormDatetimeField;
import com.minquoad.tool.form.FormEntityField;
import com.minquoad.tool.form.ImprovedForm;

public class BlockingForm extends ImprovedForm {

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
		targetField.addNonBlockingChecker((form, thisField, value) -> {
			if (getUser().canAdminister(value)) {
				return null;
			} else {
				return "administration hierarchy not respected";
			}
		});
		this.addField(targetField);

		FormDatetimeField datetimeField = new FormDatetimeField(DATE_KEY);
		datetimeField.setEmptyPermitted(false);
		this.addField(datetimeField);
	}

	public User getTarget() {
		@SuppressWarnings("unchecked")
		FormEntityField<User> field = (FormEntityField<User>) this.getField(TARGET_ID_KEY);
		return field.getValue();
	}

	public Instant getUnblockDate() {
		FormDatetimeField field = (FormDatetimeField) this.getField(DATE_KEY);
		return field.getInstant();
	}
	
}
