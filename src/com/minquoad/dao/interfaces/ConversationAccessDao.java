package com.minquoad.dao.interfaces;

import com.minquoad.entity.Conversation;
import com.minquoad.entity.ConversationAccess;
import com.minquoad.entity.User;

public interface ConversationAccessDao extends Dao<ConversationAccess> {

	public ConversationAccess getConversationAccess(User user, Conversation conversation);
}
