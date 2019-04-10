package com.minquoad.frontComponent.form.account;

import javax.servlet.http.HttpServletRequest;

import com.minquoad.entity.User;
import com.minquoad.frontComponent.form.Form;
import com.minquoad.frontComponent.form.field.FormStringField;
import com.minquoad.frontComponent.form.field.valueChecker.ColorValueChecker;
import com.minquoad.frontComponent.form.field.valueChecker.EmailAddressValueChecker;

public class UserParametersAlterationForm extends Form {

	public static final String mailAddressKey = "mailAddress";
	public static final String nicknameKey = "nickname";
	public static final String defaultColorKey = "defaultColor";

	public UserParametersAlterationForm(HttpServletRequest request) {
		super(request);
	}

	@Override
	protected void build() {

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
			if (existingUser == null || existingUser == getUser()) {
				return null;
			} else {
				return "The mail address \"" + value + "\" is already taken.";
			}
		});
		field.addValueChecker((form, thisField, value) -> {
			thisField.getValueProblems().addAll(User.getMailAddressProblems(value));
			return null;
		});
		field.setValue(getUser().getMailAddress());
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
			if (existingUser == null || existingUser == getUser()) {
				return null;
			} else {
				return "The nickname \"" + value + "\" is alreadi taken.";
			}
		});
		field.addValueChecker((form, thisField, value) -> {
			thisField.getValueProblems().addAll(User.getNicknameProblems(value));
			return null;
		});
		field.setValue(getUser().getNickname());
		this.addField(field);

		field = new FormStringField(defaultColorKey);
		field.setEmptyPermitted(false);
		field.addValueChecker(new ColorValueChecker());
		field.setValue(getUser().getDefaultColorAsHtmlValue());
		this.addField(field);

	}

	public String getnickname() {
		FormStringField field = (FormStringField) this.getField(nicknameKey);
		return field.getValue();
	}

	public String getmailAddress() {
		FormStringField field = (FormStringField) this.getField(mailAddressKey);
		return field.getValue();
	}

	public String getdefaultColor() {
		FormStringField field = (FormStringField) this.getField(defaultColorKey);
		return field.getValue();
	}

}
