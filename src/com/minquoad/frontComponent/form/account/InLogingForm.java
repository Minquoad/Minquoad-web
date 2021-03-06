package com.minquoad.frontComponent.form.account;

import java.time.Duration;

import javax.servlet.http.HttpServletRequest;

import com.minquoad.dao.interfaces.UserDao;
import com.minquoad.entity.User;
import com.minquoad.framework.form.FormEmailField;
import com.minquoad.framework.form.FormStringField;
import com.minquoad.service.Deployment;
import com.minquoad.service.ServicesManager;
import com.minquoad.tool.form.ImprovedForm;

public class InLogingForm extends ImprovedForm {

	public static final String MAIL_ADDRESS_KEY = "mailAddress";
	public static final String PASSWORD_KEY = "password";

	public InLogingForm(HttpServletRequest request) {
		super(request);
	}

	@Override
	public void build() {

		FormStringField field = null;

		field = new FormEmailField(MAIL_ADDRESS_KEY);
		field.setEmptyPermitted(false);
		field.addNonBlockingChecker((form, thisField, value) -> {
			Duration coolDown = getUnitFactory().getFailedInLoginigAttemptUnit().getCoolDown(value);
			if (coolDown == null) {
				return null;
			} else {
				return getText(
						"TooMannyFailedInLoginigAttempt",
						value,
						coolDown.getSeconds() / 60,
						coolDown.getSeconds() % 60);
			}
		});
		this.addField(field);

		field = new FormStringField(PASSWORD_KEY);
		field.setEmptyPermitted(false);
		field.addNonBlockingChecker((form, thisField, value) -> {
			FormStringField mailAddressField = (FormStringField) form.getField(MAIL_ADDRESS_KEY);
			if (mailAddressField.isValid()) {

				UserDao userDao = getDaoFactory().getUserDao();

				User user = userDao.getOneMatching("mailAddress", mailAddressField.getValue());

				if (user == null || !user.isPassword(value, ServicesManager.getService(form.getRequest(), Deployment.class))) {
					return getText("MailAddressOrPasswordIsIncorrect");
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
