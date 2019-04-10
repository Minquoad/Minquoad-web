package com.minquoad.frontComponent.form.impl.conversation;

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

		FormStringField textField = new FormStringField(textKey);
		textField.setEmptyPermitted(false);
		this.addField(textField);
	}

	public Conversation getConversation() {
		FormStringField field = (FormStringField) this.getField(conversationIdKey);
		return field.getValueAsEntity(DaoFactory::getConversationDao);
	}

	public String getText() {
		FormStringField field = (FormStringField) this.getField(textKey);
		return field.getValue();
	}
}
