package com.minquoad.unit.impl;

import java.util.List;

import com.minquoad.dao.interfaces.ConversationDao;
import com.minquoad.entity.Conversation;
import com.minquoad.entity.ConversationAccess;
import com.minquoad.entity.Message;
import com.minquoad.entity.User;
import com.minquoad.unit.Unit;
import com.minquoad.unit.UnitFactory;

public class ConversationUnit extends Unit {

	public ConversationUnit(UnitFactory unitFactory) {
		super(unitFactory);
	}

	public void initUserConversations(User user) {

		ConversationDao conversationDao = getDaoFactory().getConversationDao();

		Conversation selfConversation = new Conversation();
		selfConversation.setTitle("Monologue");
		selfConversation.setType(Conversation.TYPE_MONOLOGUE);
		conversationDao.insert(selfConversation);

		this.giveAccessToConversation(user, selfConversation);
	}

	public boolean hasUserConversationAccess(User user, Conversation conversation) {
		return getDaoFactory().getConversationAccessDao().getConversationAccess(user, conversation) != null;
	}

	public void giveAccessToConversation(User user, Conversation conversation) {
		this.giveAccessToConversation(user, conversation, false);
	}

	public void giveAccessToConversation(User user, Conversation conversation, boolean administrator) {
		ConversationAccess conversationAccess = new ConversationAccess();
		conversationAccess.setConversation(conversation);
		conversationAccess.setUser(user);
		conversationAccess.setAdministrator(administrator);
		getDaoFactory().getConversationAccessDao().insert(conversationAccess);
	}

	public List<Message> getConversationMessagesInOrder(Conversation conversation) {
		List<Message> messages = getDaoFactory().getMessageDao().getConversationMessages(conversation);

		int i = 0;
		while (i + 1 < messages.size()) {
			Message latest = messages.get(i);
			Message earlyest = messages.get(i + 1);
			if (latest.getInstant().isAfter(earlyest.getInstant())) {
				messages.set(i + 1, latest);
				messages.set(i, earlyest);
				if (i != 0) {
					i--;
				} else {
					i++;
				}
			} else {
				i++;
			}
		}

		return messages;
	}

}
