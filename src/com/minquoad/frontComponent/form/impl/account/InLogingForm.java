package com.minquoad.frontComponent.form.impl.account;

import java.time.Duration;

import javax.servlet.http.HttpServletRequest;

import com.minquoad.dao.interfaces.UserDao;
import com.minquoad.entity.User;
import com.minquoad.frontComponent.form.Form;
import com.minquoad.frontComponent.form.field.FormStringField;
import com.minquoad.frontComponent.form.field.valueChecker.EmailAddressValueChecker;
import com.minquoad.tool.http.ImprovedHttpServlet;
import com.minquoad.unit.impl.FailedInLoginigAttemptUnit;

public class InLogingForm extends Form {

	public static final String MAIL_ADDRESS_KEY = "mailAddress";
	public static final String PASSWORD_KEY = "password";

	public InLogingForm(HttpServletRequest request) {
		super(request);
	}

	@Override
	public void build() {

		FailedInLoginigAttemptUnit failedInLoginigAttemptUnit = getUnitFactory().getFailedInLoginigAttemptUnit();

		FormStringField field = null;

		field = new FormStringField(MAIL_ADDRESS_KEY) {
			@Override
			public void setValue(String value) {
				super.setValue(value);
				if (getValue() != null) {
					super.setValue(User.formatMailAddressCase(getValue()));
				}
			}
		};
		field.setEmptyPermitted(false);
		field.addValueChecker(new EmailAddressValueChecker());
		field.addValueChecker((form, thisField, value) -> {
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

		field = new FormStringField(PASSWORD_KEY);
		field.setEmptyPermitted(false);
		field.addValueChecker((form, thisField, value) -> {
			FormStringField mailAddressField = (FormStringField) form.getField(MAIL_ADDRESS_KEY);
			if (mailAddressField.isValid()) {

				UserDao userDao = getDaoFactory().getUserDao();

				User user = userDao.getOneMatching("mailAddress", mailAddressField.getValue());

				if (user == null || !user.isPassword(value, ImprovedHttpServlet.getDeployment(form.getRequest()))) {
					return "Mail address or password is incorrect.";
				}
			}
			return null;
		});
		this.addField(field);

	}

	public User getInLoggingUser() {
		return getDaoFactory().getUserDao().getOneMatching("mailAddress", getMailAddress());
	}

	public String getMailAddress() {
		FormStringField field = (FormStringField) this.getField(MAIL_ADDRESS_KEY);
		return field.getValue();
	}

}
