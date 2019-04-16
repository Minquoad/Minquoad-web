package com.minquoad.frontComponent.form.impl.account;

import javax.servlet.http.HttpServletRequest;

import com.minquoad.entity.User;
import com.minquoad.frontComponent.form.Form;
import com.minquoad.frontComponent.form.field.FormColorField;
import com.minquoad.frontComponent.form.field.FormStringField;
import com.minquoad.frontComponent.form.field.valueChecker.EmailAddressValueChecker;

public class UserParametersAlterationForm extends Form {

	public static final String MAIL_ADDRESS_KEY = "mailAddress";
	public static final String NICKNAME_KEY = "nickname";
	public static final String DEFAULT_COLOR_KEY = "defaultColor";

	public UserParametersAlterationForm(HttpServletRequest request) {
		super(request);
	}

	@Override
	protected void build() {

		FormStringField field = null;

		field = new FormStringField(MAIL_ADDRESS_KEY) {
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
			User existingUser = getDaoFactory().getUserDao().getOneMatching("mailAddress", value);
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

		field = new FormStringField(NICKNAME_KEY) {
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
			User existingUser = getDaoFactory().getUserDao().getOneMatching("nickname", value);
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

		field = new FormColorField(DEFAULT_COLOR_KEY);
		field.setEmptyPermitted(false);
		field.setValue(getUser().getDefaultColorAsHtmlValue());
		this.addField(field);

	}

	public String getnickname() {
		FormStringField field = (FormStringField) this.getField(NICKNAME_KEY);
		return field.getValue();
	}

	public String getmailAddress() {
		FormStringField field = (FormStringField) this.getField(MAIL_ADDRESS_KEY);
		return field.getValue();
	}

	public String getdefaultColor() {
		FormStringField field = (FormStringField) this.getField(DEFAULT_COLOR_KEY);
		return field.getValue();
	}

	public Integer getDefaultColor() {
		FormStringField field = (FormStringField) this.getField(DEFAULT_COLOR_KEY);
		return field.getValueAsInteger();
	}

}