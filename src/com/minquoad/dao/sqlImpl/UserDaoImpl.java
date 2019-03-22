package com.minquoad.dao.sqlImpl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.minquoad.dao.interfaces.UserDao;
import com.minquoad.entity.Conversation;
import com.minquoad.entity.ConversationAccess;
import com.minquoad.entity.User;

public class UserDaoImpl extends ImprovedEntityDaoImpl<User> implements UserDao {

	public UserDaoImpl(DaoFactoryImpl daoFactory) {
		super(daoFactory);
	}

	@Override
	public User instantiateBlank() {
		return new User();
	}

	@Override
	public void initEntityMembers() throws SQLException {
		this.addIntegerEntityMember("id", User::getId, User::setId, true);
		this.addStringEntityMember("mailAddress", User::getMailAddress, User::setMailAddress);
		this.addStringEntityMember("nickname", User::getNickname, User::setNickname);
		this.addStringEntityMember("hashedSaltedPassword", User::getHashedSaltedPassword, User::setHashedSaltedPassword);
		this.addInstantEntityMember("registrationInstant", User::getRegistrationInstant, User::setRegistrationInstant);
		this.addInstantEntityMember("lastActivityInstant", User::getLastActivityInstant, User::setLastActivityInstant);
		this.addIntegerEntityMember("adminLevel", User::getAdminLevel, User::setAdminLevel);
		this.addInstantEntityMember("unblockInstant", User::getUnblockInstant, User::setUnblockInstant);
	}

	@Override
	public List<User> getConversationUsers(Conversation conversation) {
		List<User> users = new ArrayList<User>();
		List<ConversationAccess> conversationAccesses = getDaoFactory().getConversationAccessDao().getAllMatching(conversation, "conversation");
		for (ConversationAccess conversationAccess : conversationAccesses) {
			users.add(conversationAccess.getUser());
		}
		return users;
	}
	
}
