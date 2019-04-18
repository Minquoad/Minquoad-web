package com.minquoad.frontComponent.form.impl.account;

import javax.servlet.http.HttpServletRequest;

import com.minquoad.frontComponent.form.Form;
import com.minquoad.frontComponent.form.field.FormFileField;
import com.minquoad.frontComponent.form.field.FormImageField;

public class UserPictureAlterationForm extends Form {

	public static final String USER_PICTURE_KEY = "userPicture";

	public UserPictureAlterationForm(HttpServletRequest request) {
		super(request);
	}

	@Override
	protected void build() {
		FormFileField field = new FormImageField(USER_PICTURE_KEY);
		this.addField(field);
	}

}
