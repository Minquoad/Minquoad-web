package com.minquoad.unit;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.minquoad.dao.interfaces.ConversationDao;
import com.minquoad.entity.Conversation;
import com.minquoad.entity.ConversationAccess;
import com.minquoad.entity.Message;
import com.minquoad.entity.User;
import com.minquoad.tool.VersatilTool;

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
		Collection<Message> messages = getDaoFactory().getMessageDao().getConversationMessages(conversation);

		List<Message> list = new ArrayList<Message>(messages);
		list.sort((latest, earlyest) -> {
			return VersatilTool.toBoundedInt(latest.getInstant().toEpochMilli() - earlyest.getInstant().toEpochMilli());
		});

		return list;
	}

}
