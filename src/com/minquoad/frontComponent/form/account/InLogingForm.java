package com.minquoad.frontComponent.form.account;

import java.time.Duration;

import javax.servlet.http.HttpServletRequest;

import com.minquoad.dao.interfaces.UserDao;
import com.minquoad.entity.User;
import com.minquoad.frontComponent.form.Form;
import com.minquoad.frontComponent.form.field.FormStringField;
import com.minquoad.frontComponent.form.field.valueChecker.NonNullValueChecker;
import com.minquoad.unit.FailedInLoginigAttemptUnit;

public class InLogingForm extends Form {

	public static final String mailAddressKey = "mailAddress";
	public static final String passwordKey = "password";

	public InLogingForm(HttpServletRequest request) {
		super(request);
	}

	@Override
	public void build() {

		FailedInLoginigAttemptUnit failedInLoginigAttemptUnit = getUnitFactory().getFailedInLoginigAttemptUnit();

		FormStringField field = null;

		field = new FormStringField(mailAddressKey) {
			@Override
			public void setValue(String value) {
				if (value != null) {
					super.setValue(User.formatMailAddressCase(value));
				}
			}
		};
		field.addValueChecker(new NonNullValueChecker());
		field.addValueChecker((value) -> {

			Duration coolDown = failedInLoginigAttemptUnit.getCoolDown(value);

			if (coolDown == null) {
				return null;
			} else {
				return "Too manny failed connection trials have been done with the mail address "
						+ value
						+ ". This mail address will not be usable for "
						+ coolDown.getSeconds() / 60
						+ " minutes and "
						+ coolDown.getSeconds() % 60
						+ " seconds.";
			}
		});
		this.addField(field);

		field = new FormStringField(passwordKey);
		field.addValueChecker(new NonNullValueChecker());
		field.addValueChecker((value) -> {
			if (getField(mailAddressKey).isValid()) {

				UserDao userDao = getDaoFactory().getUserDao();

				User user = userDao.getOneMatching(getFieldValueAsString(mailAddressKey), "mailAddress");

				if (user == null || !user.isPassword(value)) {
					return "Mail address or password is incorrect.";
				}
			}
			return null;
		});
		this.addField(field);

	}

}
