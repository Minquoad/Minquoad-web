package com.minquoad.dao.sqlImpl;

import java.util.ArrayList;
import java.util.List;

import com.minquoad.dao.interfaces.ConversationDao;
import com.minquoad.entity.Conversation;
import com.minquoad.entity.ConversationAccess;
import com.minquoad.entity.Message;
import com.minquoad.entity.User;
import com.minquoad.framework.dao.DaoException;

public class ConversationDaoImpl extends DaoImpl<Conversation> implements ConversationDao {

	public ConversationDaoImpl(DaoFactoryImpl daoFactory) {
		super(daoFactory);
	}

	@Override
	protected Conversation instantiateBlank() {
		return new Conversation();
	}

	@Override
	protected void initSuperClass() {
	}

	@Override
	protected void initSubClasses() {
	}

	@Override
	protected void initEntityMembers() throws DaoException {
		this.addLongEntityMember("id", Conversation::getId, Conversation::setId);
		this.addStringEntityMember("title", Conversation::getTitle, Conversation::setTitle);
		this.addIntegerEntityMember("type", Conversation::getType, Conversation::setType);
		this.addForeingKeyEntityMember("lastMessage", Message.class, Conversation::getLastMessage, Conversation::setLastMessage);
	}

	@Override
	protected boolean isPrimaryKeyRandomlyGenerated() {
		return true;
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

}
