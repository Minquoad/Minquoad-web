package com.minquoad.dao.sqlImpl;

import com.minquoad.dao.interfaces.ConsiderationDao;
import com.minquoad.dao.interfaces.ConversationAccessDao;
import com.minquoad.dao.interfaces.ConversationDao;
import com.minquoad.dao.interfaces.DaoFactory;
import com.minquoad.dao.interfaces.FailedInLoginigAttemptDao;
import com.minquoad.dao.interfaces.ImprovementSuggestionDao;
import com.minquoad.dao.interfaces.MessageDao;
import com.minquoad.dao.interfaces.MessageFileDao;
import com.minquoad.dao.interfaces.ProtectedFileDao;
import com.minquoad.dao.interfaces.RequestLogDao;
import com.minquoad.dao.interfaces.ThingDao;
import com.minquoad.dao.interfaces.UserDao;
import com.minquoad.dao.interfaces.UserProfileImageDao;
import com.minquoad.entity.Consideration;
import com.minquoad.entity.Conversation;
import com.minquoad.entity.ConversationAccess;
import com.minquoad.entity.FailedInLoginigAttempt;
import com.minquoad.entity.ImprovementSuggestion;
import com.minquoad.entity.Message;
import com.minquoad.entity.RequestLog;
import com.minquoad.entity.Thing;
import com.minquoad.entity.User;
import com.minquoad.entity.file.MessageFile;
import com.minquoad.entity.file.ProtectedFile;
import com.minquoad.entity.file.UserProfileImage;
import com.minquoad.service.Database;

public class DaoFactoryImpl extends com.minquoad.framework.dao.DaoFactoryImpl implements DaoFactory {

	public DaoFactoryImpl(Database database) {
		super(() -> database.getConnection());
	}

	@Override
	protected void initDaos() {
		addDao(Thing.class, new ThingDaoImpl(this));
		addDao(User.class, new UserDaoImpl(this));
		addDao(Message.class, new MessageDaoImpl(this));
		addDao(ConversationAccess.class, new ConversationAccessDaoImpl(this));
		addDao(Conversation.class, new ConversationDaoImpl(this));
		addDao(FailedInLoginigAttempt.class, new FailedInLoginigAttemptDaoImpl(this));
		addDao(ProtectedFile.class, new ProtectedFileDaoImpl(this));
		addDao(UserProfileImage.class, new UserProfileImageDaoImpl(this));
		addDao(RequestLog.class, new RequestLogDaoImpl(this));
		addDao(ImprovementSuggestion.class, new ImprovementSuggestionDaoImpl(this));
		addDao(MessageFile.class, new MessageFileDaoImpl(this));
		addDao(Consideration.class, new ConsiderationDaoImpl(this));
	}

	@Override
	public ThingDao getThingDao() {
		return (ThingDao) getDao(Thing.class);
	}

	@Override
	public UserDao getUserDao() {
		return (UserDao) getDao(User.class);
	}

	@Override
	public MessageDao getMessageDao() {
		return (MessageDao) getDao(Message.class);
	}

	@Override
	public ConversationAccessDao getConversationAccessDao() {
		return (ConversationAccessDao) getDao(ConversationAccess.class);
	}

	@Override
	public ConversationDao getConversationDao() {
		return (ConversationDao) getDao(Conversation.class);
	}

	@Override
	public FailedInLoginigAttemptDao getFailedInLoginigAttemptDao() {
		return (FailedInLoginigAttemptDao) getDao(FailedInLoginigAttempt.class);
	}

	@Override
	public ProtectedFileDao getProtectedFileDao() {
		return (ProtectedFileDao) getDao(ProtectedFile.class);
	}

	@Override
	public UserProfileImageDao getUserProfileImageDao() {
		return (UserProfileImageDao) getDao(UserProfileImage.class);
	}

	@Override
	public RequestLogDao getRequestLogDao() {
		return (RequestLogDao) getDao(RequestLog.class);
	}

	@Override
	public ImprovementSuggestionDao getImprovementSuggestionDao() {
		return (ImprovementSuggestionDao) getDao(ImprovementSuggestion.class);
	}

	@Override
	public MessageFileDao getMessageFileDao() {
		return (MessageFileDao) getDao(MessageFile.class);
	}

	@Override
	public ConsiderationDao getConsiderationDao() {
		return (ConsiderationDao) getDao(Consideration.class);
	}

}
