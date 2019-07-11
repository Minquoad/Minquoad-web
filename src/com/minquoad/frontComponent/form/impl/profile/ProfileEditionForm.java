package com.minquoad.frontComponent.form.impl.profile;

import javax.servlet.http.HttpServletRequest;

import com.minquoad.entity.User;
import com.minquoad.entity.file.UserProfileImage;
import com.minquoad.frontComponent.form.Form;
import com.minquoad.frontComponent.form.field.FormBooleanField;
import com.minquoad.frontComponent.form.field.FormColorField;
import com.minquoad.frontComponent.form.field.FormFileField;
import com.minquoad.frontComponent.form.field.FormImageField;
import com.minquoad.frontComponent.form.field.FormStringField;
import com.minquoad.unit.impl.UserUnit;

public class ProfileEditionForm extends Form {

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

		field = new FormColorField(DEFAULT_COLOR_KEY);
		field.setEmptyPermitted(false);
		field.setValue(getUser().getDefaultColorAsHtmlValue());
		this.addField(field);

		FormFileField fileField = new FormImageField(PICTURE_KEY);
		this.addField(fileField);

		UserProfileImage image = getDaoFactory().getUserProfileImageDao().getUserUserProfileImage(getUser());
		if (image != null) {
			FormBooleanField checkboxField = new FormBooleanField(PICTURE_RESET_KEY);
			this.addField(checkboxField);
		}
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
		return checkboxField.isChecked();
	}

}
