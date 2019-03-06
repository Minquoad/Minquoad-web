package com.minquoad.dao.interfaces;

import java.util.List;

import com.minquoad.entity.Conversation;
import com.minquoad.entity.User;

public interface UserDao extends Dao<User> {

	public List<User> getConversationUsers(Conversation conversation);

}
