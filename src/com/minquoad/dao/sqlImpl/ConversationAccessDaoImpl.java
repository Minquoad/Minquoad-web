package com.minquoad.dao.sqlImpl;

import java.util.Collection;
import java.util.List;

import com.minquoad.dao.interfaces.ConversationAccessDao;
import com.minquoad.entity.Conversation;
import com.minquoad.entity.ConversationAccess;
import com.minquoad.entity.User;
import com.minquoad.framework.dao.EntityCriterion;

public class ConversationAccessDaoImpl extends ImprovedEntityDaoImpl<ConversationAccess> implements ConversationAccessDao {

	public ConversationAccessDaoImpl(DaoFactoryImpl daoFactory) {
		super(daoFactory);
	}

	@Override
	public void initEntityMembers() {
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

		Collection<ConversationAccess> conversationAccessesInInventory = this.getInstantiatedEntyties();
		// a user-conversation pair is unique in the table so if we find one already already instantiated, no need to ask the database
		for (ConversationAccess conversationAccess : conversationAccessesInInventory) {
			if (conversationAccess.getUser() == user && conversationAccess.getConversation() == conversation) {
				return conversationAccess;
			}
		}

		EntityCriterion[] criteria = new EntityCriterion[2];
		criteria[0] = new EntityCriterion(conversation, "conversation");
		criteria[1] = new EntityCriterion(user, "user");
		List<ConversationAccess> conversationAccesses = this.getAllMatching(criteria);
		if (conversationAccesses.isEmpty()) {
			return null;
		} else {
			return conversationAccesses.get(0);
		}
	}
	
}
