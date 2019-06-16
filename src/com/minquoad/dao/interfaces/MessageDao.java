package com.minquoad.dao.interfaces;

import java.util.List;

import com.minquoad.entity.Conversation;
import com.minquoad.entity.Message;
import com.minquoad.entity.file.MessageFile;

public interface MessageDao extends Dao<Message> {

	public List<Message> getConversationMessages(Conversation conversation);

	public Message getMessageFileMessage(MessageFile file);
	
}
