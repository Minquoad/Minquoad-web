package com.minquoad.frontComponent.form.conversation;

import javax.servlet.http.HttpServletRequest;

import com.minquoad.framework.form.FormStringField;
import com.minquoad.tool.form.ImprovedForm;

public class ConversationCreationForm extends ImprovedForm {
	
	public static final String TITLE_KEY = "title";

	public ConversationCreationForm(HttpServletRequest request) {
		super(request);
	}

	@Override
	protected void build() {
		FormStringField textField = new FormStringField(TITLE_KEY);
		textField.setEmptyPermitted(false);
		this.addField(textField);
	}

	public String getTitle() {
		FormStringField field = (FormStringField) this.getField(TITLE_KEY);
		return field.getValue();
	}

}
