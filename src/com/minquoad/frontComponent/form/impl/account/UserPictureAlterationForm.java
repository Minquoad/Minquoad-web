package com.minquoad.frontComponent.form.impl.account;

import javax.servlet.http.HttpServletRequest;

import com.minquoad.frontComponent.form.Form;
import com.minquoad.frontComponent.form.field.FormFileField;
import com.minquoad.frontComponent.form.field.valueChecker.ImageChecker;
import com.minquoad.tool.ImageTool;

public class UserPictureAlterationForm extends Form {

	public static final String USER_PICTURE_KEY = "userPicture";

	public UserPictureAlterationForm(HttpServletRequest request) {
		super(request);
	}

	@Override
	protected void build() {
		FormFileField field = new FormFileField(USER_PICTURE_KEY);
		for (String extention : ImageTool.getPossibleImageExtentions()) {
			field.addAllowedExtention(extention);
		}
		field.addValueChecker(new ImageChecker());
		this.addField(field);
	}

}
