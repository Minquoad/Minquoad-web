package dao.sqlImpl;

import dao.interfaces.DaoFactory;

public class DaoFactoryImpl implements DaoFactory {

	private ThingDaoImpl thingDaoImpl;
	private UserDaoImpl userDaoImpl;
	private MessageDaoImpl messageDaoImpl;
	private ConversationAccessDaoImpl conversationAccessDaoImpl;
	private ConversationDaoImpl conversationDaoImpl;

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

}
