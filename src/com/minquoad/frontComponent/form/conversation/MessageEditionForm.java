package com.minquoad.frontComponent.form.conversation;

import javax.servlet.http.HttpServletRequest;

import com.minquoad.dao.interfaces.DaoFactory;
import com.minquoad.entity.Message;
import com.minquoad.framework.form.FormStringField;
import com.minquoad.tool.form.FormEntityField;
import com.minquoad.tool.form.ImprovedForm;

public class MessageEditionForm extends ImprovedForm {

	public static final String MESSAGE_ID_KEY = "messageId";
	public static final String NEW_TEXT_KEY = "newText";

	public MessageEditionForm(HttpServletRequest request) {
		super(request);
	}

	@Override
	protected void build() {

		FormEntityField<Message> messageField = new FormEntityField<Message>(MESSAGE_ID_KEY, DaoFactory::getMessageDao);
		messageField.setEmptyPermitted(false);
		messageField.addNonBlockingChecker((form, field, value) -> {
			if (getUnitFactory().getConversationUnit().hasUserConversationAccess(getUser(), value.getConversation())) {
				return null;
			} else {
				return "conversation access problem";
			}
		});
		messageField.addNonBlockingChecker((form, field, value) -> {
			if (value.getUser() == getUser()) {
				return null;
			} else {
				return "only author can edit a message";
			}
		});
		this.addField(messageField);

		FormStringField newTextField = new FormStringField(NEW_TEXT_KEY);
		this.addField(newTextField);
	}

	public Message getMessage() {
		@SuppressWarnings("unchecked")
		FormEntityField<Message> field = (FormEntityField<Message>) this.getField(MESSAGE_ID_KEY);
		return field.getValue();
	}

	public String getNewText() {
		FormStringField field = (FormStringField) this.getField(NEW_TEXT_KEY);
		return field.getValue();
	}
	
}
