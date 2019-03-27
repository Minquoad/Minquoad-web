package com.minquoad.frontComponent.form.account;

import javax.servlet.http.HttpServletRequest;

import com.minquoad.frontComponent.form.Form;
import com.minquoad.frontComponent.form.field.FormStringField;
import com.minquoad.frontComponent.form.field.valueChecker.NonNullValueChecker;

public class UserParametersAlterationForm extends Form {

	public static final String defaultColorKey = "defaultColor";

	public UserParametersAlterationForm(HttpServletRequest request) {
		super(request);
	}


	@Override
	protected void build() {

		FormStringField field = null;

		field = new FormStringField(defaultColorKey);
		field.addValueChecker(new NonNullValueChecker());
		this.addField(field);

	}

}
