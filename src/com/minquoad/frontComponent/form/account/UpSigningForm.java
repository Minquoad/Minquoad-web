package com.minquoad.frontComponent.form.account;

import javax.servlet.http.HttpServletRequest;

import com.minquoad.entity.User;
import com.minquoad.frontComponent.form.Form;
import com.minquoad.frontComponent.form.field.FormStringField;
import com.minquoad.frontComponent.form.field.valueChecker.EmailAddressValueChecker;

public class UpSigningForm extends Form {

	public static final String mailAddressKey = "mailAddress";
	public static final String nicknameKey = "nickname";
	public static final String passwordKey = "password";
	public static final String passwordConfirmationKey = "passwordConfirmation";
	public static final String upSigningCodeKey = "upSigningCode";

	public static final String upSigningCode = "gyroroue";

	public UpSigningForm(HttpServletRequest request) {
		super(request);
	}

	@Override
	public void build() {

		FormStringField field = null;

		field = new FormStringField(mailAddressKey) {
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
			User existingUser = getDaoFactory().getUserDao().getOneMatching(value, "mailAddress");
			if (existingUser == null) {
				return null;
			} else {
				return "The mail address \"" + value + "\" is already taken.";
			}
		});
		field.addValueChecker((form, thisField, value) -> {
			thisField.getValueProblems().addAll(User.getMailAddressProblems(value));
			return null;
		});
		this.addField(field);

		field = new FormStringField(nicknameKey) {
			@Override
			public void setValue(String value) {
				super.setValue(value);
				if (getValue() != null) {
					super.setValue(User.formatNickanameCase(getValue()));
				}
			}
		};
		field.setEmptyPermitted(false);
		field.addValueChecker((form, thisField, value) -> {
			User existingUser = getDaoFactory().getUserDao().getOneMatching(value, "nickname");
			if (existingUser == null) {
				return null;
			} else {
				return "The nickname \"" + value + "\" is alreadi taken.";
			}
		});
		field.addValueChecker((form, thisField, value) -> {
			thisField.getValueProblems().addAll(User.getNicknameProblems(value));
			return null;
		});
		this.addField(field);

		field = new FormStringField(passwordKey);
		field.setEmptyPermitted(false);
		field.addValueChecker((form, thisField, value) -> {
			thisField.getValueProblems().addAll(User.getPasswordProblems(value));;
			return null;
		});
		this.addField(field);

		field = new FormStringField(passwordConfirmationKey);
		field.setEmptyPermitted(false);
		field.addValueChecker((form, thisField, value) -> {
			FormStringField field2 = (FormStringField) form.getField(passwordKey);
			String password = field2.getValue();
			if (!value.equals(password)) {
				return "The password confirmation is different to the password.";
			} else
				return null;
		});
		this.addField(field);

		field = new FormStringField(upSigningCodeKey);
		field.setEmptyPermitted(false);
		field.addValueChecker((form, thisField, value) -> {
			if (!upSigningCode.equals(value)) {
				return "Wrong up signing code.";
			} else
				return null;
		});
		this.addField(field);

	}

	public String getMailAddress() {
		FormStringField field = (FormStringField) this.getField(mailAddressKey);
		return field.getValue();
	}

	public String getNickname() {
		FormStringField field = (FormStringField) this.getField(nicknameKey);
		return field.getValue();
	}

	public String getPassword() {
		FormStringField field = (FormStringField) this.getField(passwordKey);
		return field.getValue();
	}

}
