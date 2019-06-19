package com.minquoad.frontComponent.form.impl.conversation;

import javax.servlet.http.HttpServletRequest;

import com.minquoad.dao.interfaces.DaoFactory;
import com.minquoad.entity.Conversation;
import com.minquoad.frontComponent.form.Form;
import com.minquoad.frontComponent.form.field.FormEntityField;
import com.minquoad.frontComponent.form.field.FormFileField;
import com.minquoad.frontComponent.form.field.FormStringField;

public class MessageAdditionForm extends Form {

	public static final String CONVERSATION_ID_KEY = "conversationId";
	public static final String TEXT_KEY = "text";
	public static final String FILE_KEY = "file";

	public MessageAdditionForm(HttpServletRequest request) {
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
		this.addField(conversationField);

		FormStringField textField = new FormStringField(TEXT_KEY);
		this.addField(textField);

		FormFileField fileField = new FormFileField(FILE_KEY) {
			@Override
			public void computeValueProblems() {
				if (!textField.isValueNull() && textField.isValueEmpty()) {
					this.setEmptyPermitted(false);
				}
				super.computeValueProblems();
			}
		};
		this.addField(fileField);


	}

	public Conversation getConversation() {
		@SuppressWarnings("unchecked")
		FormEntityField<Conversation> field = (FormEntityField<Conversation>) this.getField(CONVERSATION_ID_KEY);
		return field.getValue();
	}

	public String getText() {
		FormStringField field = (FormStringField) this.getField(TEXT_KEY);
		return field.getValue();
	}

	public FormFileField getFileField() {
		return (FormFileField) this.getField(FILE_KEY);
	}
	
}
