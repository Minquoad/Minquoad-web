package com.minquoad.frontComponent.form.account;

import java.util.List;

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
			public List<String> getValueProblems() {
				List<String> problemes = super.getValueProblems();
				if (getValue() != null) {
					problemes.addAll(User.getMailAddressProblems(getValue()));
				}
				return problemes;
			}
		};
		field.setRequired(true);
		field.addValueChecker(new EmailAddressValueChecker());
		field.addValueChecker((form, thisField, value) -> {
			User existingUser = getDaoFactory().getUserDao().getOneMatching(value, "mailAddress");
			if (existingUser == null || existingUser == getUser()) {
				return null;
			} else {
				return "The mail address \"" + value + "\" is already taken.";
			}
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

			public List<String> getValueProblems() {
				List<String> problemes = super.getValueProblems();
				if (getValue() != null) {
					problemes.addAll(User.getNicknameProblems(getValue()));
				}
				return problemes;
			}
		};
		field.setRequired(true);
		field.addValueChecker((form, thisField, value) -> {
			User existingUser = getDaoFactory().getUserDao().getOneMatching(value, "nickname");
			if (existingUser == null || existingUser == getUser()) {
				return null;
			} else {
				return "The nickname \"" + value + "\" is alreadi taken.";
			}
		});
		this.addField(field);

		field = new FormStringField(defaultColorKey);
		field.setRequired(true);
		field.addValueChecker(new ColorValueChecker());
		this.addField(field);

	}

}
