package com.minquoad.unit.impl;

import java.time.Instant;

import com.minquoad.dao.interfaces.UserDao;
import com.minquoad.entity.User;
import com.minquoad.unit.Unit;
import com.minquoad.unit.UnitFactory;

public class UserUnit extends Unit {

	public UserUnit(UnitFactory unitFactory) {
		super(unitFactory);
	}

	public User createNewUser(String mailAddress, String nickname, String password) {

		UserDao userDao = getDaoFactory().getUserDao();

		User user = new User();
		user.setMailAddress(mailAddress);
		user.setNickname(nickname);
		user.setRegistrationInstant(Instant.now());
		user.setLastActivityInstant(Instant.now());
		user.setDefaultColor(User.DEFAULT_DEFAULT_COLOR);
		userDao.insert(user);
		user.setPassword(password, getDeployment());
		userDao.persist(user);
		getUnitFactory().getConversationUnit().initUserConversations(user);

		return user;
	}
	
}
