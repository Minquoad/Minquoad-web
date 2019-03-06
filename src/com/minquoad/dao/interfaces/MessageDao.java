package com.minquoad.dao.interfaces;

import java.util.List;

import com.minquoad.entity.Conversation;
import com.minquoad.entity.Message;

public interface MessageDao extends Dao<Message> {

	public List<Message> getConversationMessages(Conversation conversation);

}
