package com.minquoad.dao.sqlImpl;

import com.minquoad.dao.interfaces.ConversationAccessDao;
import com.minquoad.entity.Conversation;
import com.minquoad.entity.ConversationAccess;
import com.minquoad.entity.User;
import com.minquoad.framework.dao.DaoException;
import com.minquoad.framework.dao.EntityCriterion;

public class ConversationAccessDaoImpl extends ImprovedDaoImpl<ConversationAccess> implements ConversationAccessDao {

	public ConversationAccessDaoImpl(DaoFactoryImpl daoFactory) {
		super(daoFactory);
	}

	@Override
	public void initEntityMembers() throws DaoException {
		this.addLongEntityMember("id", ConversationAccess::getId, ConversationAccess::setId, true);
		this.addBooleanEntityMember("administrator", ConversationAccess::isAdministrator, ConversationAccess::setAdministrator);
		this.addForeingKeyEntityMember("user", ConversationAccess::getUser, ConversationAccess::setUser, getDaoFactory().getUserDao());
		this.addForeingKeyEntityMember("conversation", ConversationAccess::getConversation, ConversationAccess::setConversation, getDaoFactory().getConversationDao());
		this.addForeingKeyEntityMember("lastSeenMessage", ConversationAccess::getLastSeenMessage, ConversationAccess::setLastSeenMessage, getDaoFactory().getMessageDao());
	}

	@Override
	public ConversationAccess instantiateBlank() {
		return new ConversationAccess();
	}

	@Override
	public ConversationAccess getConversationAccess(User user, Conversation conversation) {
		EntityCriterion[] criteria = new EntityCriterion[2];
		criteria[0] = new EntityCriterion(conversation, "conversation");
		criteria[1] = new EntityCriterion(user, "user");
		return this.getOneMatching(criteria);
	}

	@Override
	public boolean isPrimaryKeyRandomlyGenerated() {
		return true;
	}
	
}
