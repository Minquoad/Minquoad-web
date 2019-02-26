package dao.sqlImpl;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import dao.Database;
import dao.interfaces.ConversationAccessDao;
import entity.Conversation;
import entity.ConversationAccess;
import entity.User;
import framework.dao.EntityDaoImpl;
import framework.dao.entityMember.ForeingKeyEntityMember;

public class ConversationAccessDaoImpl extends EntityDaoImpl<ConversationAccess> implements ConversationAccessDao {

	private DaoFactoryImpl daoFactory;

	public ConversationAccessDaoImpl(DaoFactoryImpl daoFactory) {
		this.daoFactory = daoFactory;
	}

	@Override
	public void initEntityMembers() {
		this.addEntityMember(new ForeingKeyEntityMember<ConversationAccess, User>(
				"user",
				ConversationAccess::getUser,
				ConversationAccess::setUser,
				daoFactory.getUserDao()));
		this.addEntityMember(new ForeingKeyEntityMember<ConversationAccess, Conversation>(
				"conversation",
				ConversationAccess::getConversation,
				ConversationAccess::setConversation,
				daoFactory.getConversationDao()));
	}

	@Override
	public PreparedStatement prepareStatement(String statement) throws SQLException {
		return Database.prepareStatement(statement);
	}

	@Override
	public ConversationAccess instantiateBlank() {
		return new ConversationAccess();
	}

	@Override
	public String getTableName() {
		return "ConversationAccess";
	}

}
