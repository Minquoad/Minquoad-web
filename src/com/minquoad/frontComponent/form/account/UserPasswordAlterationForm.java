package com.minquoad.frontComponent.form.account;

import javax.servlet.http.HttpServletRequest;

import com.minquoad.entity.User;
import com.minquoad.frontComponent.form.Form;
import com.minquoad.frontComponent.form.field.FormStringField;

public class UserPasswordAlterationForm extends Form {

	public static final String oldPassowrdKey = "oldPassowrd";
	public static final String newPasswordKey = "newPassword";
	public static final String newPasswordConfirmationKey = "newPasswordConfirmation";

	public UserPasswordAlterationForm(HttpServletRequest request) {
		super(request);
	}

	@Override
	protected void build() {

		FormStringField field = null;

		field = new FormStringField(oldPassowrdKey);
		field.setEmptyPermitted(false);
		field.addValueChecker((form, thisField, value) -> {
			if (getUser().isPassword(value)) {
				return null;
			} else {
				return "Old password not correct.";
			}
		});
		this.addField(field);

		field = new FormStringField(newPasswordKey);
		field.setEmptyPermitted(false);
		field.addValueChecker((form, thisField, value) -> {
			thisField.getValueProblems().addAll(User.getPasswordProblems(value));;
			return null;
		});
		this.addField(field);

		field = new FormStringField(newPasswordConfirmationKey);
		field.setEmptyPermitted(false);
		field.addValueChecker((form, thisField, value) -> {
			FormStringField newPasswordField = (FormStringField) form.getField(newPasswordKey);
			String password = newPasswordField.getValue();
			if (password != null && !password.equals(value)) {
				return "The password confirmation is different to the password.";
			} else
				return null;
		});
		this.addField(field);

	}

	public String getNewPassword() {
		FormStringField field = (FormStringField) this.getField(newPasswordKey);
		return field.getValue();
	}

}
