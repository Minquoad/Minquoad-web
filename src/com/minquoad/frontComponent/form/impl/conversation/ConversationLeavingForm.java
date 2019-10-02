package com.minquoad.frontComponent.form.impl.conversation;

import javax.servlet.http.HttpServletRequest;

import com.minquoad.dao.interfaces.DaoFactory;
import com.minquoad.entity.Conversation;
import com.minquoad.frontComponent.form.Form;
import com.minquoad.frontComponent.form.field.FormEntityField;

public class ConversationLeavingForm extends Form {

	public static final String CONVERSATION_ID_KEY = "conversationId";

	public ConversationLeavingForm(HttpServletRequest request) {
		super(request);
	}

	@Override
	protected void build() {

		FormEntityField<Conversation> conversationField =
				new FormEntityField<Conversation>(CONVERSATION_ID_KEY, DaoFactory::getConversationDao);

		conversationField.setEmptyPermitted(false);
		conversationField.addValueChecker((form, field, value) -> {
			if (getUnitFactory().getConversationUnit().hasUserConversationAccess(getUser(), value)) {
				return null;
			} else {
				return "conversation access problem";
			}
		});
		conversationField.addValueChecker((form, field, value) -> {
			if (Conversation.TYPE_CREATED_BY_USER == value.getType()) {
				return null;
			} else {
				return "conversation type do not allow this";
			}
		});
		this.addField(conversationField);

	}

	public Conversation getConversation() {
		@SuppressWarnings("unchecked")
		FormEntityField<Conversation> field = (FormEntityField<Conversation>) this.getField(CONVERSATION_ID_KEY);
		return field.getValue();
	}

}