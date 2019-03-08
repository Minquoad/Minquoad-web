package com.minquoad.entity.unit;

import java.util.Date;

import com.minquoad.dao.interfaces.DaoFactory;
import com.minquoad.dao.interfaces.UserDao;
import com.minquoad.entity.User;

public class UserUnit extends Unit {

	public UserUnit(DaoFactory daoFactory) {
		super(daoFactory);
	}

	public User createNewUser(String mailAddress, String nickname, String password) {

		UserDao userDao = getDaoFactory().getUserDao();

		User user = new User();
		user.setMailAddress(mailAddress);
		user.setNickname(nickname);
		user.setRegistrationDate(new Date());
		user.setLastActivityDate(new Date());
		userDao.insert(user);
		user.setPassword(password);
		userDao.update(user);

		new ConversationUnit(getDaoFactory()).initUserConversations(user);

		return user;
	}
	
}
