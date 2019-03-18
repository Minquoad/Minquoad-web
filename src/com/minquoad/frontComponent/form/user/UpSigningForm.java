package com.minquoad.frontComponent.form.user;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.minquoad.entity.User;
import com.minquoad.frontComponent.form.Form;
import com.minquoad.frontComponent.form.FormField;
import com.minquoad.frontComponent.form.NonNullValueChecker;

public class UpSigningForm extends Form {

	public static final String mailAddressKey = "mailAddress";
	public static final String nicknameKey = "nickname";
	public static final String passwordKey = "password";
	public static final String passwordConfirmationKey = "passwordConfirmation";

	public UpSigningForm(HttpServletRequest request) {
		super(request);
	}

	@Override
	public void build() {

		FormField field = null;

		field = new FormField(mailAddressKey) {
			public List<String> getValueProblems() {
				List<String> problemes = super.getValueProblems();
				if (getValue() != null) {
					problemes.addAll(User.getMailAddressProblems(getValue()));
				}
				return problemes;
			}
		};
		field.addValueChecker(new NonNullValueChecker());
		field.addValueChecker((value) -> {
			User existingUser = getDaoFactory().getUserDao().getOneMatching(value, "mailAddress");
			if (existingUser == null) {
				return null;
			} else {
				return "The mail address \"" + value + "\" is alreadi taken.";
			}
		});
		this.addField(field);

		field = new FormField(nicknameKey) {
			public List<String> getValueProblems() {
				List<String> problemes = super.getValueProblems();
				if (getValue() != null) {
					problemes.addAll(User.getNicknameProblems(getValue()));
				}
				return problemes;
			}
		};
		field.addValueChecker(new NonNullValueChecker());
		field.addValueChecker((value) -> {
			User existingUser = getDaoFactory().getUserDao().getOneMatching(value, "nickname");
			if (existingUser == null) {
				return null;
			} else {
				return "The nickname \"" + value + "\" is alreadi taken.";
			}
		});
		this.addField(field);

		field = new FormField(passwordKey) {
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

		field = new FormField(passwordConfirmationKey);
		field.addValueChecker(new NonNullValueChecker());
		field.addValueChecker((value) -> {
			String password = this.getFieldValue(passwordKey);
			if (password != null && !password.equals(value)) {
				return "The password confirmation is different to the password.";
			} else
				return null;
		});
		this.addField(field);

	}

}
