package com.minquoad.frontComponent.form.impl.account;

import javax.servlet.http.HttpServletRequest;

import com.minquoad.entity.User;
import com.minquoad.frontComponent.form.Form;
import com.minquoad.frontComponent.form.field.FormStringField;
import com.minquoad.tool.http.ImprovedHttpServlet;

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
			if (getUser().isPassword(value, ImprovedHttpServlet.getDeployment(form.getRequest()))) {
				return null;
			} else {
				return "Old password not correct.";
			}
		});
		this.addField(field);

		field = new FormStringField(NEW_PASSWORD_KEY);
		field.setEmptyPermitted(false);
		field.addValueChecker((form, thisField, value) -> {
			thisField.getValueProblems().addAll(User.getPasswordProblems(value));;
			return null;
		});
		this.addField(field);

		field = new FormStringField(NEW_PASSWORD_CONFIRMATION_KEY);
		field.setEmptyPermitted(false);
		field.addValueChecker((form, thisField, value) -> {
			FormStringField newPasswordField = (FormStringField) form.getField(NEW_PASSWORD_KEY);
			String password = newPasswordField.getValue();
			if (password != null && !password.equals(value)) {
				return "The password confirmation is different to the password.";
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