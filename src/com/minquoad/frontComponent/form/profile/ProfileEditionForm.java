package com.minquoad.frontComponent.form.profile;

import javax.servlet.http.HttpServletRequest;

import com.minquoad.entity.User;
import com.minquoad.framework.form.FormBooleanField;
import com.minquoad.framework.form.FormColorField;
import com.minquoad.framework.form.FormFileField;
import com.minquoad.framework.form.FormImageField;
import com.minquoad.framework.form.FormStringField;
import com.minquoad.tool.form.ImprovedForm;
import com.minquoad.unit.UserUnit;

public class ProfileEditionForm extends ImprovedForm {

	public static final String NICKNAME_KEY = "nickname";
	public static final String DEFAULT_COLOR_KEY = "defaultColor";
	public static final String PICTURE_KEY = "picture";
	public static final String PICTURE_RESET_KEY = "pictureReset";

	public ProfileEditionForm(HttpServletRequest request) {
		super(request);
	}

	@Override
	protected void build() {

		FormStringField field = null;

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

		field = new FormColorField(DEFAULT_COLOR_KEY);
		field.setEmptyPermitted(false);
		field.setValue(getUser().getDefaultColorAsHtmlValue());
		this.addField(field);

		FormFileField fileField = new FormImageField(PICTURE_KEY);
		this.addField(fileField);

		FormBooleanField checkboxField = new FormBooleanField(PICTURE_RESET_KEY);
		this.addField(checkboxField);

	}

	public String getnickname() {
		FormStringField field = (FormStringField) this.getField(NICKNAME_KEY);
		return field.getValue();
	}

	public Integer getDefaultColor() {
		FormColorField field = (FormColorField) this.getField(DEFAULT_COLOR_KEY);
		return field.getValueAsInteger();
	}

	public FormFileField getPictureField() {
		return (FormFileField) this.getField(PICTURE_KEY);
	}

	public boolean isPictureResetRequested() {
		FormBooleanField checkboxField = (FormBooleanField) this.getField(PICTURE_RESET_KEY);
		return checkboxField.getValue();
	}

}
