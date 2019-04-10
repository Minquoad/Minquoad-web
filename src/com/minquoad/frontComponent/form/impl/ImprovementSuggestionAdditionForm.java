package com.minquoad.frontComponent.form.impl;

import javax.servlet.http.HttpServletRequest;

import com.minquoad.frontComponent.form.Form;
import com.minquoad.frontComponent.form.field.FormStringField;

public class ImprovementSuggestionAdditionForm extends Form {

	public static final String textKey = "text";

	public ImprovementSuggestionAdditionForm(HttpServletRequest request) {
		super(request);
	}

	@Override
	protected void build() {

		FormStringField textField = new FormStringField(textKey) {
			@Override
			public void setValue(String value) {
				if (value != null) {
					super.setValue(value.trim());
				}
			}
		};
		textField.addValueChecker((form, field, value)-> {
			if (value== null || value.equals("")) {
				return "The text field is empty.";
			} else {
				return null;
			}
		});
		this.addField(textField);

	}

	public String getText() {
		FormStringField field = (FormStringField) getField(textKey);
		return field.getValue();
	}

}
