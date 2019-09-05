package com.minquoad.dao.sqlImpl;

import com.minquoad.dao.interfaces.UserProfileImageDao;
import com.minquoad.entity.User;
import com.minquoad.entity.file.ProtectedFile;
import com.minquoad.entity.file.UserProfileImage;
import com.minquoad.framework.dao.DaoException;

public class UserProfileImageDaoImpl extends DaoImpl<UserProfileImage> implements UserProfileImageDao {

	public UserProfileImageDaoImpl(DaoFactoryImpl daoFactory) {
		super(daoFactory);
	}

	@Override
	protected UserProfileImage instantiateBlank() {
		return new UserProfileImage();
	}

	@Override
	protected void initSuperClass() {
		setSuperClass(ProtectedFile.class);
	}

	@Override
	protected void initSubClasses() {
	}

	@Override
	protected void initEntityMembers() throws DaoException {
		this.addForeingKeyEntityMember("user", User.class, UserProfileImage::getUser, UserProfileImage::setUser);
	}

	@Override
	public UserProfileImage getUserUserProfileImage(User user) {
		return this.getOneMatching("user", user);
	}

}
