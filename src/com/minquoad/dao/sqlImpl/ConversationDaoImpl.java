package com.minquoad.dao.sqlImpl;

import java.util.ArrayList;
import java.util.List;

import com.minquoad.dao.interfaces.ConversationDao;
import com.minquoad.entity.Conversation;
import com.minquoad.entity.ConversationAccess;
import com.minquoad.entity.User;
import com.minquoad.framework.dao.DaoException;

public class ConversationDaoImpl extends ImprovedDaoImpl<Conversation> implements ConversationDao {

	public ConversationDaoImpl(DaoFactoryImpl daoFactory) {
		super(daoFactory);
	}

	@Override
	public void initEntityMembers() throws DaoException {
		this.addLongEntityMember("id", Conversation::getId, Conversation::setId, true);
		this.addStringEntityMember("title", Conversation::getTitle, Conversation::setTitle);
		this.addIntegerEntityMember("type", Conversation::getType, Conversation::setType);
		this.addForeingKeyEntityMember("lastMessage", Conversation::getLastMessage, Conversation::setLastMessage, getDaoFactory().getMessageDao());
	}

	@Override
	public Conversation instantiateBlank() {
		return new Conversation();
	}

	@Override
	public List<Conversation> getUserConversations(User user) {
		List<ConversationAccess> conversationAccesses = getDaoFactory().getConversationAccessDao().getAllMatching("user", user);
		List<Conversation> conversations = new ArrayList<Conversation>();
		for (ConversationAccess conversationAccess : conversationAccesses) {
			conversations.add(conversationAccess.getConversation());
		}
		return conversations;
	}

	@Override
	public boolean isPrimaryKeyRandomlyGenerated() {
		return true;
	}
}
