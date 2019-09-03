package com.minquoad.dao.sqlImpl;

import java.util.HashMap;
import java.util.Map;

import com.minquoad.dao.interfaces.ConversationAccessDao;
import com.minquoad.entity.Conversation;
import com.minquoad.entity.ConversationAccess;
import com.minquoad.entity.User;
import com.minquoad.framework.dao.DaoException;

public class ConversationAccessDaoImpl extends ImprovedDaoImpl<ConversationAccess> implements ConversationAccessDao {

	public ConversationAccessDaoImpl(DaoFactoryImpl daoFactory) {
		super(daoFactory);
	}

	@Override
	protected ConversationAccess instantiateBlank() {
		return new ConversationAccess();
	}

	@Override
	protected void initEntityMembers() throws DaoException {
		this.addLongEntityMember("id", ConversationAccess::getId, ConversationAccess::setId);
		this.addBooleanEntityMember("administrator", ConversationAccess::isAdministrator, ConversationAccess::setAdministrator);
		this.addForeingKeyEntityMember("user", ConversationAccess::getUser, ConversationAccess::setUser, getDaoFactory().getUserDao());
		this.addForeingKeyEntityMember("conversation", ConversationAccess::getConversation, ConversationAccess::setConversation, getDaoFactory().getConversationDao());
		this.addForeingKeyEntityMember("lastSeenMessage", ConversationAccess::getLastSeenMessage, ConversationAccess::setLastSeenMessage, getDaoFactory().getMessageDao());
	}

	@Override
	protected boolean isPrimaryKeyRandomlyGenerated() {
		return true;
	}

	@Override
	public ConversationAccess getConversationAccess(User user, Conversation conversation) {
		Map<String, Object> criteria = new HashMap<String, Object>();
		criteria.put("user", user);
		criteria.put("conversation", conversation);
		return this.getOneMatching(criteria);
	}

}
