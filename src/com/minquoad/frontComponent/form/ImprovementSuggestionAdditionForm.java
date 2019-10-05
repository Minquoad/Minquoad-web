package com.minquoad.frontComponent.form;

import javax.servlet.http.HttpServletRequest;

import com.minquoad.framework.form.FormStringField;
import com.minquoad.tool.form.ImprovedForm;

public class ImprovementSuggestionAdditionForm extends ImprovedForm {

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
