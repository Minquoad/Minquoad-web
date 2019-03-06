package com.minquoad.dao.interfaces;

import java.util.List;

import com.minquoad.entity.Conversation;
import com.minquoad.entity.User;

public interface ConversationDao extends Dao<Conversation> {

	public List<Conversation> getUserConversations(User user);

}
