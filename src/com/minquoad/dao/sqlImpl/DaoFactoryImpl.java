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
	}

	private Database getDatabase() {
		return database;
	}

	public Connection getConnection() throws SQLException {
		return this.getDatabase().getConnection();
	}

	@Override
	public ThingDaoImpl getThingDao() {
		if (thingDaoImpl == null) {
			thingDaoImpl = new ThingDaoImpl(this);
		}
		return thingDaoImpl;
	}

	@Override
	public UserDaoImpl getUserDao() {
		if (userDaoImpl == null) {
			userDaoImpl = new UserDaoImpl(this);
		}
		return userDaoImpl;
	}

	@Override
	public MessageDaoImpl getMessageDao() {
		if (messageDaoImpl == null) {
			messageDaoImpl = new MessageDaoImpl(this);
		}
		return messageDaoImpl;
	}

	@Override
	public ConversationAccessDaoImpl getConversationAccessDao() {
		if (conversationAccessDaoImpl == null) {
			conversationAccessDaoImpl = new ConversationAccessDaoImpl(this);
		}
		return conversationAccessDaoImpl;
	}

	@Override
	public ConversationDaoImpl getConversationDao() {
		if (conversationDaoImpl == null) {
			conversationDaoImpl = new ConversationDaoImpl(this);
		}
		return conversationDaoImpl;
	}

	@Override
	public FailedInLoginigAttemptDaoImpl getFailedInLoginigAttemptDao() {
		if (failedInLoginigAttemptDaoImpl == null) {
			failedInLoginigAttemptDaoImpl = new FailedInLoginigAttemptDaoImpl(this);
		}
		return failedInLoginigAttemptDaoImpl;
	}

	@Override
	public ProtectedFileDaoImpl getProtectedFileDao() {
		if (protectedFileDaoImpl == null) {
			protectedFileDaoImpl = new ProtectedFileDaoImpl(this);
		}
		return protectedFileDaoImpl;
	}

	@Override
	public UserProfileImageDaoImpl getUserProfileImageDao() {
		if (userProfileImageDaoImpl == null) {
			userProfileImageDaoImpl = new UserProfileImageDaoImpl(this);
		}
		return userProfileImageDaoImpl;
	}

	@Override
	public RequestLogDaoImpl getRequestLogDao() {
		if (requestLogDaoImpl == null) {
			requestLogDaoImpl = new RequestLogDaoImpl(this);
		}
		return requestLogDaoImpl;
	}

	@Override
	public ImprovementSuggestionDaoImpl getImprovementSuggestionDao() {
		if (improvementSuggestionDaoImpl == null) {
			improvementSuggestionDaoImpl = new ImprovementSuggestionDaoImpl(this);
		}
		return improvementSuggestionDaoImpl;
	}

	@Override
	public MessageFileDaoImpl getMessageFileDao() {
		if (messageFileDaoImpl == null) {
			messageFileDaoImpl = new MessageFileDaoImpl(this);
		}
		return messageFileDaoImpl;
	}

	@Override
	public ConsiderationDaoImpl getConsiderationDao() {
		if (considerationDaoImpl == null) {
			considerationDaoImpl = new ConsiderationDaoImpl(this);
		}
		return considerationDaoImpl;
	}

}
