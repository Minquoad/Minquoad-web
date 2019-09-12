package com.minquoad.dao.interfaces;

import java.util.Collection;

import com.minquoad.entity.Conversation;
import com.minquoad.entity.Message;
import com.minquoad.entity.file.MessageFile;

public interface MessageDao extends Dao<Message> {

	public Collection<Message> getConversationMessages(Conversation conversation);

	public Message getMessageFileMessage(MessageFile file);
	
}
