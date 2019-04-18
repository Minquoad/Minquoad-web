package com.minquoad.frontComponent.form.impl;

import javax.servlet.http.HttpServletRequest;

import com.minquoad.frontComponent.form.Form;
import com.minquoad.frontComponent.form.field.FormStringField;

public class ImprovementSuggestionAdditionForm extends Form {

	public static final String TEXT_KEY = "text";

	public ImprovementSuggestionAdditionForm(HttpServletRequest request) {
		super(request);
	}

	@Override
	protected void build() {
		FormStringField textField = new FormStringField(TEXT_KEY);
		textField.setEmptyPermitted(false);
		this.addField(textField);
	}

	public String getText() {
		FormStringField field = (FormStringField) getField(TEXT_KEY);
		return field.getValue();
	}

}
