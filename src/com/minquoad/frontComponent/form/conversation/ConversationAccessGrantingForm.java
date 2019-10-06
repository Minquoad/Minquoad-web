package com.minquoad.frontComponent.form.conversation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

import javax.servlet.http.HttpServletRequest;

import com.minquoad.dao.interfaces.DaoFactory;
import com.minquoad.entity.Conversation;
import com.minquoad.entity.ConversationAccess;
import com.minquoad.entity.User;
import com.minquoad.framework.form.FormMultipleValuePickerField;
import com.minquoad.tool.form.FormEntityField;
import com.minquoad.tool.form.ImprovedForm;

public class ConversationAccessGrantingForm extends ImprovedForm {

	public static final String CONVERSATION_ID_KEY = "conversationId";
	public static final String TARGERS_KEY = "targets";

	public ConversationAccessGrantingForm(HttpServletRequest request) {
		super(request);
	}

	@Override
	protected void build() {

		FormEntityField<Conversation> conversationField =
				new FormEntityField<Conversation>(CONVERSATION_ID_KEY, DaoFactory::getConversationDao);

		conversationField.setEmptyPermitted(false);
		conversationField.addNonBlockingChecker((form, field, value) -> {

			ConversationAccess conversationAccess = getDaoFactory().getConversationAccessDao().getConversationAccess(getUser(), value);
			if (conversationAccess == null) {
				return "conversation access problem";
			}
			if (!conversationAccess.isAdministrator()) {
				return "conversation access not administrator";
			}
			return null;
		});
		this.addField(conversationField);

		FormMultipleValuePickerField targetsField = new FormMultipleValuePickerField(TARGERS_KEY);
		targetsField.addBlockingChecker((form, field, value) -> {
			if (!conversationField.isValid()) {
				return null;
			}
			Collection<User> evrybody = getDaoFactory().getUserDao().getAll();
			Collection<User> conversationUsers = getDaoFactory().getUserDao().getConversationUsers(conversationField.getValue());
			evrybody.removeAll(conversationUsers);
			Collection<String> possibleTargets = new HashSet<String>();
			for (User user : evrybody) {
				possibleTargets.add(user.getId().toString());
			}
			if (possibleTargets.containsAll(value)) {
				return null;
			}
			return "illegal target(s)";
		});
		this.addField(targetsField);
	}

	public Conversation getConversation() {
		@SuppressWarnings("unchecked")
		FormEntityField<Conversation> field = (FormEntityField<Conversation>) this.getField(CONVERSATION_ID_KEY);
		return field.getValue();
	}

	public Collection<User> getTargets() {
		FormMultipleValuePickerField field = (FormMultipleValuePickerField) this.getField(TARGERS_KEY);
		Collection<User> targets = new ArrayList<User>();
		for (String userIdString : field.getValue()) {
			targets.add(getDaoFactory().getUserDao().getByPk(Long.parseLong(userIdString)));
		}
		return targets;
	}

}
