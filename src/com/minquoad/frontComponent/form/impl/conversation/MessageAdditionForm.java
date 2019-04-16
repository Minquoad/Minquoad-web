package com.minquoad.frontComponent.form.impl.conversation;

import javax.servlet.http.HttpServletRequest;

import com.minquoad.dao.interfaces.DaoFactory;
import com.minquoad.entity.Conversation;
import com.minquoad.frontComponent.form.Form;
import com.minquoad.frontComponent.form.field.FormStringField;

public class MessageAdditionForm extends Form {

	public static final String CONVERSATION_ID_KEY = "conversationId";
	public static final String TEXT_KEY = "text";

	public MessageAdditionForm(HttpServletRequest request) {
		super(request);
	}

	@Override
	protected void build() {

		FormStringField conversationIdField = new FormStringField(CONVERSATION_ID_KEY);
		this.addField(conversationIdField);

		FormStringField textField = new FormStringField(TEXT_KEY);
		textField.setEmptyPermitted(false);
		this.addField(textField);
	}

	public Conversation getConversation() {
		FormStringField field = (FormStringField) this.getField(CONVERSATION_ID_KEY);
		return field.getValueAsEntity(DaoFactory::getConversationDao);
	}

	public String getText() {
		FormStringField field = (FormStringField) this.getField(TEXT_KEY);
		return field.getValue();
	}
}
