package com.minquoad.frontComponent.form.account;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.minquoad.entity.User;
import com.minquoad.frontComponent.form.Form;
import com.minquoad.frontComponent.form.field.FormStringField;
import com.minquoad.frontComponent.form.field.valueChecker.NonNullValueChecker;

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
		field.addValueChecker(new NonNullValueChecker());
		field.addValueChecker((value) -> {
			if (getUser().isPassword(value)) {
				return null;
			} else {
				return "Old password not correct.";
			}
		});
		this.addField(field);

		field = new FormStringField(newPasswordKey) {
			public List<String> getValueProblems() {
				List<String> problemes = super.getValueProblems();
				if (getValue() != null) {
					problemes.addAll(User.getPasswordProblems(getValue()));
				}
				return problemes;
			}
		};
		field.addValueChecker(new NonNullValueChecker());
		this.addField(field);

		field = new FormStringField(newPasswordConfirmationKey);
		field.addValueChecker(new NonNullValueChecker());
		field.addValueChecker((value) -> {
			String password = this.getFieldValueAsString(newPasswordKey);
			if (password != null && !password.equals(value)) {
				return "The password confirmation is different to the password.";
			} else
				return null;
		});
		field.addValueChecker(new NonNullValueChecker());
		this.addField(field);

	}

}
