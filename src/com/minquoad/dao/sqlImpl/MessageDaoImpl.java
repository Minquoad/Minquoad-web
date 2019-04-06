package com.minquoad.dao.sqlImpl;

import java.util.List;

import com.minquoad.dao.interfaces.MessageDao;
import com.minquoad.entity.Conversation;
import com.minquoad.entity.Message;
import com.minquoad.framework.dao.DaoException;

public class MessageDaoImpl extends ImprovedEntityDaoImpl<Message> implements MessageDao {

	public MessageDaoImpl(DaoFactoryImpl daoFactory) {
		super(daoFactory);
	}

	@Override
	public void initEntityMembers() throws DaoException {
		this.addLongEntityMember("id", Message::getId, Message::setId, true);
		this.addStringEntityMember("text", Message::getText, Message::setText);
		this.addStringEntityMember("editedText", Message::getEditedText, Message::setEditedText);
		this.addInstantEntityMember("instant", Message::getInstant, Message::setInstant);
		this.addForeingKeyEntityMember("user", Message::getUser, Message::setUser, getDaoFactory().getUserDao());
		this.addForeingKeyEntityMember("conversation", Message::getConversation, Message::setConversation, getDaoFactory().getConversationDao());
	}

	@Override
	public Message instantiateBlank() {
		return new Message();
	}

	@Override
	public List<Message> getConversationMessages(Conversation conversation) {
		return this.getAllMatching(conversation, "conversation");
	}

	@Override
	public boolean isPrimaryKeyRandomlyGenerated() {
		return true;
	}
}
