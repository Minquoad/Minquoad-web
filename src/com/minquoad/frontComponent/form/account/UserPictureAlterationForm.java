package com.minquoad.frontComponent.form.account;

import javax.servlet.http.HttpServletRequest;

import com.minquoad.frontComponent.form.Form;
import com.minquoad.frontComponent.form.field.FormFileField;

public class UserPictureAlterationForm extends Form {

	public static final String userPictureKey = "userPicture";
	
	public UserPictureAlterationForm(HttpServletRequest request) {
		super(request);
	}

	@Override
	protected void build() {
		FormFileField field = new FormFileField(userPictureKey);
		this.addField(field);
	}

}
