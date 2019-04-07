package com.minquoad.dao.interfaces;

public interface DaoFactory {

	public ThingDao getThingDao();
	public UserDao getUserDao();
	public MessageDao getMessageDao();
	public ConversationAccessDao getConversationAccessDao();
	public ConversationDao getConversationDao();
	public FailedInLoginigAttemptDao getFailedInLoginigAttemptDao();
	public ProtectedFileDao getProtectedFileDao();
	public UserProfileImageDao getUserProfileImageDao();
	public RequestLogDao getRequestLogDao();
	public ImprovementSuggestionDao getImprovementSuggestionDao();

}
