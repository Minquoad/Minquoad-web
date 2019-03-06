package com.minquoad.dao.interfaces;

public interface DaoFactory {

	public ThingDao getThingDao();

	public UserDao getUserDao();

	public MessageDao getMessageDao();

	public ConversationAccessDao getConversationAccessDao();

	public ConversationDao getConversationDao();

}
