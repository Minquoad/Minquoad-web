package dao.sqlImpl;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import dao.Database;
import dao.interfaces.MessageDao;
import entity.Conversation;
import entity.Message;
import entity.User;
import framework.dao.EntityDaoImpl;
import framework.dao.entityMember.ForeingKeyEntityMember;

public class MessageDaoImpl extends EntityDaoImpl<Message> implements MessageDao {

	private DaoFactoryImpl daoFactory;

	public MessageDaoImpl(DaoFactoryImpl daoFactory) {
		this.daoFactory = daoFactory;
	}

	@Override
	public void initEntityMembers() {
		this.addStringEntityMember("text", Message::getText, Message::setText);
		this.addStringEntityMember("editedText", Message::getEditedText, Message::setEditedText);
		this.addDateEntityMember("date", Message::getDate, Message::setDate);
		this.addEntityMember(new ForeingKeyEntityMember<Message, User>(
				"user",
				Message::getUser,
				Message::setUser,
				daoFactory.getUserDao()));
		this.addEntityMember(new ForeingKeyEntityMember<Message, Conversation>(
				"conversation",
				Message::getConversation,
				Message::setConversation,
				daoFactory.getConversationDao()));
	}

	@Override
	public PreparedStatement prepareStatement(String statement) throws SQLException {
		return Database.prepareStatement(statement);
	}

	@Override
	public Message instantiateBlank() {
		return new Message();
	}

	@Override
	public String getTableName() {
		return "Message";
	}

	public List<Message> getConversationMessages(Conversation conversation) {
		return this.getAllMatching(conversation, "conversation");
	}
	
}
