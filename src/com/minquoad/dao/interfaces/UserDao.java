package com.minquoad.dao.interfaces;

import java.util.Collection;

import com.minquoad.entity.Conversation;
import com.minquoad.entity.User;

public interface UserDao extends Dao<User> {

	public Collection<User> getConversationUsers(Conversation conversation);

}
