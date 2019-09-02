package com.minquoad.frontComponent.form.impl.account;

import javax.servlet.http.HttpServletRequest;

import com.minquoad.frontComponent.form.Form;
import com.minquoad.frontComponent.form.field.FormStringField;
import com.minquoad.service.Deployment;
import com.minquoad.service.ServicesManager;
import com.minquoad.unit.UserUnit;

public class UserPasswordAlterationForm extends Form {

	public static final String OLD_PASSOWRD_KEY = "oldPassowrd";
	public static final String NEW_PASSWORD_KEY = "newPassword";
	public static final String NEW_PASSWORD_CONFIRMATION_KEY = "newPasswordConfirmation";

	public UserPasswordAlterationForm(HttpServletRequest request) {
		super(request);
	}

	@Override
	protected void build() {

		FormStringField field = null;

		field = new FormStringField(OLD_PASSOWRD_KEY);
		field.setEmptyPermitted(false);
		field.addValueChecker((form, thisField, value) -> {
			if (getUser().isPassword(value, ServicesManager.getService(form.getRequest(), Deployment.class))) {
				return null;
			} else {
				return getText("OldPasswordNotCorrect");
			}
		});
		this.addField(field);

		field = new FormStringField(NEW_PASSWORD_KEY);
		field.setEmptyPermitted(false);
		field.addValueChecker((form, thisField, value) -> {
			thisField.getValueProblems().addAll(UserUnit.getPasswordProblems(value, getLocale()));;
			return null;
		});
		this.addField(field);

		field = new FormStringField(NEW_PASSWORD_CONFIRMATION_KEY);
		field.setEmptyPermitted(false);
		field.addValueChecker((form, thisField, value) -> {
			FormStringField newPasswordField = (FormStringField) form.getField(NEW_PASSWORD_KEY);
			String password = newPasswordField.getValue();
			if (password != null && !password.equals(value)) {
				return getText("PasswordConfirmationFail");
			} else
				return null;
		});
		this.addField(field);

	}

	public String getNewPassword() {
		FormStringField field = (FormStringField) this.getField(NEW_PASSWORD_KEY);
		return field.getValue();
	}

}
