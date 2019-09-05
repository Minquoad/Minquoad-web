package com.minquoad.dao.sqlImpl;

import java.util.ArrayList;
import java.util.List;

import com.minquoad.dao.interfaces.UserDao;
import com.minquoad.entity.Conversation;
import com.minquoad.entity.ConversationAccess;
import com.minquoad.entity.User;
import com.minquoad.framework.dao.DaoException;

public class UserDaoImpl extends DaoImpl<User> implements UserDao {

	public UserDaoImpl(DaoFactoryImpl daoFactory) {
		super(daoFactory);
	}

	@Override
	protected User instantiateBlank() {
		return new User();
	}

	@Override
	protected void initSuperClass() {
	}

	@Override
	protected void initSubClasses() {
	}

	@Override
	protected void initEntityMembers() throws DaoException {
		this.addLongEntityMember("id", User::getId, User::setId);
		this.addStringEntityMember("mailAddress", User::getMailAddress, User::setMailAddress);
		this.addStringEntityMember("nickname", User::getNickname, User::setNickname);
		this.addStringEntityMember("hashedSaltedPassword", User::getHashedSaltedPassword, User::setHashedSaltedPassword);
		this.addInstantEntityMember("registrationInstant", User::getRegistrationInstant, User::setRegistrationInstant);
		this.addInstantEntityMember("lastActivityInstant", User::getLastActivityInstant, User::setLastActivityInstant);
		this.addIntegerEntityMember("adminLevel", User::getAdminLevel, User::setAdminLevel);
		this.addInstantEntityMember("unblockInstant", User::getUnblockInstant, User::setUnblockInstant);
		this.addIntegerEntityMember("defaultColor", User::getDefaultColor, User::setDefaultColor);
		this.addStringEntityMember("language", User::getLanguage, User::setLanguage);
		this.addBooleanEntityMember("readabilityImprovementActivated", User::isReadabilityImprovementActivated, User::setReadabilityImprovementActivated);
		this.addBooleanEntityMember("typingAssistanceActivated", User::isTypingAssistanceActivated, User::setTypingAssistanceActivated);
	}

	@Override
	protected boolean isPrimaryKeyRandomlyGenerated() {
		return true;
	}
	
	@Override
	public List<User> getConversationUsers(Conversation conversation) {
		List<User> users = new ArrayList<User>();
		List<ConversationAccess> conversationAccesses = getDaoFactory().getConversationAccessDao().getAllMatching("conversation", conversation);
		for (ConversationAccess conversationAccess : conversationAccesses) {
			users.add(conversationAccess.getUser());
		}
		return users;
	}

}
