package dao.sqlImpl;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import dao.Database;
import dao.interfaces.UserDao;
import entity.Conversation;
import entity.ConversationAccess;
import entity.User;
import framework.dao.EntityDaoImpl;

public class UserDaoImpl extends EntityDaoImpl<User> implements UserDao {

	private DaoFactoryImpl daoFactory;

	public UserDaoImpl(DaoFactoryImpl daoFactory) {
		this.daoFactory = daoFactory;
	}

	@Override
	public User instantiateBlank() {
		return new User();
	}

	@Override
	public String getTableName() {
		return "User";
	}

	@Override
	public void initEntityMembers() {
		this.addStringEntityMember("nickname", User::getNickname, User::setNickname);
		this.addStringEntityMember("hashedSaltedPassword", User::getHashedSaltedPassword,
				User::setHashedSaltedPassword);
		this.addStringEntityMember("pictureName", User::getPictureName, User::setPictureName);
		this.addDateEntityMember("registrationDate", User::getRegistrationDate, User::setRegistrationDate);
		this.addDateEntityMember("lastActivityDate", User::getLastActivityDate, User::setLastActivityDate);
		this.addIntegerEntityMember("adminLevel", User::getAdminLevel, User::setAdminLevel);
		this.addDateEntityMember("unblockDate", User::getUnblockDate, User::setUnblockDate);
	}

	@Override
	public PreparedStatement prepareStatement(String statement) throws SQLException {
		return Database.prepareStatement(statement);
	}

	public List<User> getConversationUsers(Conversation conversation) {
		List<User> users = new ArrayList<User>();
		List<ConversationAccess> conversationAccesses = daoFactory.getConversationAccessDao().getAllMatching(conversation, "conversation");
		for (ConversationAccess conversationAccess : conversationAccesses) {
			users.add(conversationAccess.getUser());
		}
		return users;
	}
	
}
