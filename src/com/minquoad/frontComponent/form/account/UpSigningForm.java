package com.minquoad.frontComponent.form.account;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.minquoad.entity.User;
import com.minquoad.frontComponent.form.Form;
import com.minquoad.frontComponent.form.field.FormStringField;
import com.minquoad.frontComponent.form.field.valueChecker.NonNullValueChecker;

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

		FormStringField field = null;

		field = new FormStringField(mailAddressKey) {
			@Override
			public void setValue(String value) {
				if (value != null) {
					super.setValue(User.formatMailAddressCase(value));
				}
			}
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

		field = new FormStringField(nicknameKey) {
			@Override
			public void setValue(String value) {
				if (value != null) {
					super.setValue(User.formatNickanameCase(value));
				}
			}
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

		field = new FormStringField(passwordKey) {
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

		field = new FormStringField(passwordConfirmationKey);
		field.addValueChecker(new NonNullValueChecker());
		field.addValueChecker((value) -> {
			String password = this.getFieldValueAsString(passwordKey);
			if (password != null && !password.equals(value)) {
				return "The password confirmation is different to the password.";
			} else
				return null;
		});
		this.addField(field);

	}

}
