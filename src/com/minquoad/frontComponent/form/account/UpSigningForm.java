package com.minquoad.frontComponent.form.account;

import javax.servlet.http.HttpServletRequest;

import com.minquoad.entity.User;
import com.minquoad.framework.form.FormEmailField;
import com.minquoad.framework.form.FormStringField;
import com.minquoad.tool.form.ImprovedForm;
import com.minquoad.unit.UserUnit;

public class UpSigningForm extends ImprovedForm {

	public static final String MAIL_ADDRESS_KEY = "mailAddress";
	public static final String NICKNAME_KEY = "nickname";
	public static final String PASSWORD_KEY = "password";
	public static final String PASSWORD_CONFIRMATION_KEY = "passwordConfirmation";
	public static final String UP_SIGNING_CODE_KEY = "upSigningCode";

	public static final String UP_SIGNING_CODE = "gyroroue";

	public UpSigningForm(HttpServletRequest request) {
		super(request);
	}

	@Override
	public void build() {

		FormStringField field = null;

		field = new FormEmailField(MAIL_ADDRESS_KEY);
		field.setEmptyPermitted(false);
		field.addNonBlockingChecker((form, thisField, value) -> {
			User existingUser = getDaoFactory().getUserDao().getOneMatching("mailAddress", value);
			if (existingUser == null) {
				return null;
			} else {
				return getText("MailAdressAlreadytaken", value);
			}
		});
		this.addField(field);

		field = new FormStringField(NICKNAME_KEY) {
			@Override
			public void setValue(String value) {
				super.setValue(value);
				if (getValue() != null) {
					super.setValue(UserUnit.formatNickanameCase(getValue()));
				}
			}
		};
		field.setEmptyPermitted(false);
		field.addNonBlockingChecker((form, thisField, value) -> {
			User existingUser = getDaoFactory().getUserDao().getOneMatching("nickname", value);
			if (existingUser == null) {
				return null;
			} else {
				return getText("NicknameAlreadytaken", value);
			}
		});
		field.addNonBlockingChecker((form, thisField, value) -> {
			thisField.getValueProblems().addAll(UserUnit.getNicknameProblems(value, getLocale()));
			return null;
		});
		this.addField(field);

		field = new FormStringField(PASSWORD_KEY);
		field.setEmptyPermitted(false);
		field.addNonBlockingChecker((form, thisField, value) -> {
			thisField.getValueProblems().addAll(UserUnit.getPasswordProblems(value, getLocale()));;
			return null;
		});
		this.addField(field);

		field = new FormStringField(PASSWORD_CONFIRMATION_KEY);
		field.setEmptyPermitted(false);
		field.addNonBlockingChecker((form, thisField, value) -> {
			FormStringField passwordField = (FormStringField) form.getField(PASSWORD_KEY);
			String password = passwordField.getValue();
			if (!value.equals(password)) {
				return getText("PasswordConfirmationFail");
			} else
				return null;
		});
		this.addField(field);

		field = new FormStringField(UP_SIGNING_CODE_KEY);
		field.setEmptyPermitted(false);
		field.addNonBlockingChecker((form, thisField, value) -> {
			if (!UP_SIGNING_CODE.equals(value)) {
				return getText("WrongUpSigningCode");
			} else
				return null;
		});
		this.addField(field);

	}

	public String getMailAddress() {
		FormStringField field = (FormStringField) this.getField(MAIL_ADDRESS_KEY);
		return field.getValue();
	}

	public String getNickname() {
		FormStringField field = (FormStringField) this.getField(NICKNAME_KEY);
		return field.getValue();
	}

	public String getPassword() {
		FormStringField field = (FormStringField) this.getField(PASSWORD_KEY);
		return field.getValue();
	}

}
