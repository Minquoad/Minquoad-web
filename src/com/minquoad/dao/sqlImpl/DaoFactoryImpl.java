package com.minquoad.dao.sqlImpl;

import com.minquoad.dao.interfaces.DaoFactory;
import com.minquoad.service.Database;

public class DaoFactoryImpl implements DaoFactory {

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

	private Database database;
	
	public DaoFactoryImpl(Database database) {
		this.database = database;
	}

	public Database getDatabase() {
		return database;
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

}
