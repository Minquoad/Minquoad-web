package com.minquoad.frontComponent.form.conversation;

import javax.servlet.http.HttpServletRequest;

import com.minquoad.dao.interfaces.DaoFactory;
import com.minquoad.entity.Conversation;
import com.minquoad.frontComponent.form.Form;
import com.minquoad.frontComponent.form.field.FormStringField;

public class MessageAdditionForm extends Form {

	public static final String conversationIdKey = "conversationId";
	public static final String textKey = "text";

	public MessageAdditionForm(HttpServletRequest request) {
		super(request);
	}

	@Override
	protected void build() {

		FormStringField conversationIdField = new FormStringField(conversationIdKey);
		this.addField(conversationIdField);

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
				return "Empty message are forbidden.";
			} else {
				return null;
			}
		});
		this.addField(textField);
	}

	public Conversation getConversation() {
		return this.getField(conversationIdKey).getValueAsEntity(DaoFactory::getConversationDao);
	}
}
