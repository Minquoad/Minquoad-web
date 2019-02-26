package dao.sqlImpl;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import dao.Database;
import dao.interfaces.ConversationDao;
import entity.Conversation;
import entity.ConversationAccess;
import entity.User;
import framework.dao.EntityDaoImpl;

public class ConversationDaoImpl extends EntityDaoImpl<Conversation> implements ConversationDao {

	private DaoFactoryImpl daoFactory;

	public ConversationDaoImpl(DaoFactoryImpl daoFactory) {
		this.daoFactory = daoFactory;
	}

	@Override
	public void initEntityMembers() {
		this.addStringEntityMember("title", Conversation::getTitle, Conversation::setTitle);
	}

	@Override
	public PreparedStatement prepareStatement(String statement) throws SQLException {
		return Database.prepareStatement(statement);
	}

	@Override
	public Conversation instantiateBlank() {
		return new Conversation();
	}

	@Override
	public String getTableName() {
		return "Conversation";
	}

	public List<Conversation> getUserConversations(User user) {
		List<ConversationAccess> conversationAccesses = daoFactory.getConversationAccessDao().getAllMatching(user, "user");
		List<Conversation> conversations = new ArrayList<Conversation>();
		for (ConversationAccess conversationAccess : conversationAccesses) {
			conversations.add(conversationAccess.getConversation());
		}
		return conversations;
	}

}
