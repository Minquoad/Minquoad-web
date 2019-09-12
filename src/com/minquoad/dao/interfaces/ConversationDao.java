package com.minquoad.dao.interfaces;

import java.util.Collection;

import com.minquoad.entity.Conversation;
import com.minquoad.entity.User;

public interface ConversationDao extends Dao<Conversation> {

	public Collection<Conversation> getUserConversations(User user);

}
