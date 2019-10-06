package com.minquoad.frontComponent.form.account;

import java.util.Arrays;

import javax.servlet.http.HttpServletRequest;

import com.minquoad.entity.User;
import com.minquoad.framework.form.FormBooleanField;
import com.minquoad.framework.form.FormColorField;
import com.minquoad.framework.form.FormEmailField;
import com.minquoad.framework.form.FormSingleValuePickerField;
import com.minquoad.framework.form.FormStringField;
import com.minquoad.tool.InternationalizationTool;
import com.minquoad.tool.form.ImprovedForm;
import com.minquoad.unit.UserUnit;

public class UserParametersAlterationForm extends ImprovedForm {

	public static final String MAIL_ADDRESS_KEY = "mailAddress";
	public static final String NICKNAME_KEY = "nickname";
	public static final String LANGUAGE_KEY = "language";
	public static final String DEFAULT_COLOR_KEY = "defaultColor";
	public static final String READABILITY_IMPROVEMENT_ACTIVATED = "readabilityImprovementActivated";
	public static final String TYPING_ASSISTANCE_ACTIVATED = "typingAssistanceActivated";

	public UserParametersAlterationForm(HttpServletRequest request) {
		super(request);
	}

	@Override
	protected void build() {

		FormStringField field = null;

		field = new FormEmailField(MAIL_ADDRESS_KEY);
		field.setEmptyPermitted(false);
		field.addNonBlockingChecker((form, thisField, value) -> {
			User existingUser = getDaoFactory().getUserDao().getOneMatching("mailAddress", value);
			if (existingUser == null || existingUser == getUser()) {
				return null;
			} else {
				return getText("MailAdressAlreadytaken", value);
			}
		});
		field.setValue(getUser().getMailAddress());
		this.addField(field);

		field = new FormStringField(NICKNAME_KEY) {
			@Override
			public void setValue(String value) {
				super.setValue(value);
				if (getValue() != null) {
					super.setValue(UserUnit.formatNickanameCase(getValue()));
				}
			}
		};
		field.setEmptyPermitted(false);
		field.addNonBlockingChecker((form, thisField, value) -> {
			User existingUser = getDaoFactory().getUserDao().getOneMatching("nickname", value);
			if (existingUser == null || existingUser == getUser()) {
				return null;
			} else {
				return getText("NicknameAlreadytaken", value);
			}
		});
		field.addNonBlockingChecker((form, thisField, value) -> {
			thisField.getValueProblems().addAll(UserUnit.getNicknameProblems(value, getLocale()));
			return null;
		});
		field.setValue(getUser().getNickname());
		this.addField(field);

		FormSingleValuePickerField languageField = new FormSingleValuePickerField(LANGUAGE_KEY);
		languageField.setEmptyPermitted(false);
		languageField.addAcceptedValues(Arrays.asList(InternationalizationTool.supportedLanguageCodes));
		languageField.setValue(getUser().getLanguage());
		this.addField(languageField);

		field = new FormColorField(DEFAULT_COLOR_KEY);
		field.setEmptyPermitted(false);
		field.setValue(getUser().getDefaultColorAsHtmlValue());
		this.addField(field);

		FormBooleanField checkboxField = null;
		
		checkboxField = new FormBooleanField(READABILITY_IMPROVEMENT_ACTIVATED);
		checkboxField.setValue(getUser().isReadabilityImprovementActivated());
		this.addField(checkboxField);

		checkboxField = new FormBooleanField(TYPING_ASSISTANCE_ACTIVATED);
		checkboxField.setValue(getUser().isTypingAssistanceActivated());
		this.addField(checkboxField);
	}

	public String getnickname() {
		FormStringField field = (FormStringField) this.getField(NICKNAME_KEY);
		return field.getValue();
	}

	public String getmailAddress() {
		FormStringField field = (FormStringField) this.getField(MAIL_ADDRESS_KEY);
		return field.getValue();
	}

	public Integer getDefaultColor() {
		FormColorField field = (FormColorField) this.getField(DEFAULT_COLOR_KEY);
		return field.getValueAsInteger();
	}

	public String getLanguage() {
		FormStringField field = (FormStringField) this.getField(LANGUAGE_KEY);
		return field.getValue();
	}

	public boolean isReadabilityImprovementActivated() {
		FormBooleanField field = (FormBooleanField) this.getField(READABILITY_IMPROVEMENT_ACTIVATED);
		return field.getValue();
	}

	public boolean isTypingAssistanceActivated() {
		FormBooleanField field = (FormBooleanField) this.getField(TYPING_ASSISTANCE_ACTIVATED);
		return field.getValue();
	}

}
