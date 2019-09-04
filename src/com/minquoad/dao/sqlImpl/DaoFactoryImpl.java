package com.minquoad.dao.sqlImpl;

import java.sql.Connection;
import java.sql.SQLException;

import com.minquoad.dao.interfaces.DaoFactory;
import com.minquoad.service.Database;

public class DaoFactoryImpl implements DaoFactory {

	private Database database;

	private ThingDaoImpl thingDaoImpl;
	private UserDaoImpl userDaoImpl;
	private MessageDaoImpl messageDaoImpl;
	private ConversationAccessDaoImpl conversationAccessDaoImpl;
	private ConversationDaoImpl conversationDaoImpl;
	private FailedInLoginigAttemptDaoImpl failedInLoginigAttemptDaoImpl;
	private ProtectedFileDaoImpl protectedFileDaoImpl;
	private UserProfileImageDaoImpl userProfileImageDaoImpl;
	private RequestLogDaoImpl requestLogDaoImpl;
	private ImprovementSuggestionDaoImpl improvementSuggestionDaoImpl;
	private MessageFileDaoImpl messageFileDaoImpl;
	private ConsiderationDaoImpl considerationDaoImpl;

	public DaoFactoryImpl(Database database) {
		this.database = database;

		thingDaoImpl = new ThingDaoImpl(this);
		userDaoImpl = new UserDaoImpl(this);
		messageDaoImpl = new MessageDaoImpl(this);
		conversationAccessDaoImpl = new ConversationAccessDaoImpl(this);
		conversationDaoImpl = new ConversationDaoImpl(this);
		failedInLoginigAttemptDaoImpl = new FailedInLoginigAttemptDaoImpl(this);
		protectedFileDaoImpl = new ProtectedFileDaoImpl(this);
		userProfileImageDaoImpl = new UserProfileImageDaoImpl(this);
		requestLogDaoImpl = new RequestLogDaoImpl(this);
		improvementSuggestionDaoImpl = new ImprovementSuggestionDaoImpl(this);
		messageFileDaoImpl = new MessageFileDaoImpl(this);
		considerationDaoImpl = new ConsiderationDaoImpl(this);
	}

	public void clear() {
		thingDaoImpl.clear();
		userDaoImpl.clear();
		messageDaoImpl.clear();
		conversationAccessDaoImpl.clear();
		conversationDaoImpl.clear();
		failedInLoginigAttemptDaoImpl.clear();
		protectedFileDaoImpl.clear();
		userProfileImageDaoImpl.clear();
		requestLogDaoImpl.clear();
		improvementSuggestionDaoImpl.clear();
		messageFileDaoImpl.clear();
		considerationDaoImpl.clear();
	}

	private Database getDatabase() {
		return database;
	}

	public Connection getConnection() throws SQLException {
		return this.getDatabase().getConnection();
	}

	@Override
	public ThingDaoImpl getThingDao() {
		return thingDaoImpl;
	}

	@Override
	public UserDaoImpl getUserDao() {
		return userDaoImpl;
	}

	@Override
	public MessageDaoImpl getMessageDao() {
		return messageDaoImpl;
	}

	@Override
	public ConversationAccessDaoImpl getConversationAccessDao() {
		return conversationAccessDaoImpl;
	}

	@Override
	public ConversationDaoImpl getConversationDao() {
		return conversationDaoImpl;
	}

	@Override
	public FailedInLoginigAttemptDaoImpl getFailedInLoginigAttemptDao() {
		return failedInLoginigAttemptDaoImpl;
	}

	@Override
	public ProtectedFileDaoImpl getProtectedFileDao() {
		return protectedFileDaoImpl;
	}

	@Override
	public UserProfileImageDaoImpl getUserProfileImageDao() {
		return userProfileImageDaoImpl;
	}

	@Override
	public RequestLogDaoImpl getRequestLogDao() {
		return requestLogDaoImpl;
	}

	@Override
	public ImprovementSuggestionDaoImpl getImprovementSuggestionDao() {
		return improvementSuggestionDaoImpl;
	}

	@Override
	public MessageFileDaoImpl getMessageFileDao() {
		return messageFileDaoImpl;
	}

	@Override
	public ConsiderationDaoImpl getConsiderationDao() {
		return considerationDaoImpl;
	}

}
