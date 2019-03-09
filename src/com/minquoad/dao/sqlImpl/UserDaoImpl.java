package com.minquoad.dao.sqlImpl;

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
	public void initEntityMembers() {
		this.addStringEntityMember("mailAddress", User::getMailAddress, User::setMailAddress);
		this.addStringEntityMember("nickname", User::getNickname, User::setNickname);
		this.addStringEntityMember("hashedSaltedPassword", User::getHashedSaltedPassword, User::setHashedSaltedPassword);
		this.addStringEntityMember("pictureName", User::getPictureName, User::setPictureName);
		this.addDateEntityMember("registrationDate", User::getRegistrationDate, User::setRegistrationDate);
		this.addDateEntityMember("lastActivityDate", User::getLastActivityDate, User::setLastActivityDate);
		this.addIntegerEntityMember("adminLevel", User::getAdminLevel, User::setAdminLevel);
		this.addDateEntityMember("unblockDate", User::getUnblockDate, User::setUnblockDate);
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