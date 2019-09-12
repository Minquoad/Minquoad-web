package com.minquoad.dao.sqlImpl;

import java.util.Collection;

import com.minquoad.dao.interfaces.MessageDao;
import com.minquoad.entity.Conversation;
import com.minquoad.entity.Message;
import com.minquoad.entity.User;
import com.minquoad.entity.file.MessageFile;
import com.minquoad.framework.dao.DaoException;

public class MessageDaoImpl extends DaoImpl<Message> implements MessageDao {

	public MessageDaoImpl(DaoFactoryImpl daoFactory) {
		super(daoFactory);
	}

	@Override
	protected Message instantiateBlank() {
		return new Message();
	}

	@Override
	protected void initSuperClass() {
	}

	@Override
	protected void initSubClasses() {
	}

	@Override
	protected void initEntityMembers() throws DaoException {
		this.addLongEntityMember("id", Message::getId, Message::setId);
		this.addStringEntityMember("text", Message::getText, Message::setText);
		this.addStringEntityMember("editedText", Message::getEditedText, Message::setEditedText);
		this.addInstantEntityMember("instant", Message::getInstant, Message::setInstant);
		this.addForeingKeyEntityMember("user", User.class, Message::getUser, Message::setUser);
		this.addForeingKeyEntityMember("conversation", Conversation.class, Message::getConversation, Message::setConversation);
		this.addForeingKeyEntityMember("messageFile", MessageFile.class, Message::getMessageFile, Message::setMessageFile);
	}

	@Override
	protected boolean isPrimaryKeyRandomlyGenerated() {
		return true;
	}

	@Override
	public Collection<Message> getConversationMessages(Conversation conversation) {
		return this.getAllMatching("conversation", conversation);
	}

	@Override
	public Message getMessageFileMessage(MessageFile messageFile) {
		return this.getOneMatching("messageFile", messageFile);
	}

}
