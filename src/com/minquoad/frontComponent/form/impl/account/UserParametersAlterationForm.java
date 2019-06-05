package com.minquoad.frontComponent.form.impl.account;

import javax.servlet.http.HttpServletRequest;

import com.minquoad.entity.User;
import com.minquoad.frontComponent.form.Form;
import com.minquoad.frontComponent.form.field.FormBooleanField;
import com.minquoad.frontComponent.form.field.FormColorField;
import com.minquoad.frontComponent.form.field.FormEmailField;
import com.minquoad.frontComponent.form.field.FormListField;
import com.minquoad.frontComponent.form.field.FormStringField;
import com.minquoad.tool.InternationalizationTool;
import com.minquoad.unit.impl.UserUnit;

public class UserParametersAlterationForm extends Form {

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
		field.addValueChecker((form, thisField, value) -> {
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
		field.addValueChecker((form, thisField, value) -> {
			User existingUser = getDaoFactory().getUserDao().getOneMatching("nickname", value);
			if (existingUser == null || existingUser == getUser()) {
				return null;
			} else {
				return getText("NicknameAlreadytaken", value);
			}
		});
		field.addValueChecker((form, thisField, value) -> {
			thisField.getValueProblems().addAll(UserUnit.getNicknameProblems(value, getLocale()));
			return null;
		});
		field.setValue(getUser().getNickname());
		this.addField(field);

		FormListField languageField = new FormListField(LANGUAGE_KEY);
		languageField.setEmptyPermitted(false);
		for (String supportedLanguageCode : InternationalizationTool.supportedLanguageCodes) {
			languageField.addPossibleValue(supportedLanguageCode);
		}
		languageField.setValue(getUser().getLanguage());
		this.addField(languageField);

		field = new FormColorField(DEFAULT_COLOR_KEY);
		field.setEmptyPermitted(false);
		field.setValue(getUser().getDefaultColorAsHtmlValue());
		this.addField(field);

		FormBooleanField checkboxField = null;
		
		checkboxField = new FormBooleanField(READABILITY_IMPROVEMENT_ACTIVATED);
		checkboxField.setChecked(getUser().isReadabilityImprovementActivated());
		this.addField(checkboxField);

		checkboxField = new FormBooleanField(TYPING_ASSISTANCE_ACTIVATED);
		checkboxField.setChecked(getUser().isTypingAssistanceActivated());
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

	public String getdefaultColor() {
		FormStringField field = (FormStringField) this.getField(DEFAULT_COLOR_KEY);
		return field.getValue();
	}

	public Integer getDefaultColor() {
		FormStringField field = (FormStringField) this.getField(DEFAULT_COLOR_KEY);
		return field.getValueAsInteger();
	}

	public String getLanguage() {
		FormStringField field = (FormStringField) this.getField(LANGUAGE_KEY);
		return field.getValue();
	}

	public boolean isReadabilityImprovementActivated() {
		FormBooleanField field = (FormBooleanField) this.getField(READABILITY_IMPROVEMENT_ACTIVATED);
		return field.isChecked();
	}

	public boolean isTypingAssistanceActivated() {
		FormBooleanField field = (FormBooleanField) this.getField(TYPING_ASSISTANCE_ACTIVATED);
		return field.isChecked();
	}

}
