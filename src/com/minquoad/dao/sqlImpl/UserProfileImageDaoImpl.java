package com.minquoad.dao.sqlImpl;

import com.minquoad.dao.interfaces.UserProfileImageDao;
import com.minquoad.entity.User;
import com.minquoad.entity.file.UserProfileImage;
import com.minquoad.framework.dao.DaoException;
import com.minquoad.framework.dao.DaoImpl;

public class UserProfileImageDaoImpl extends ImprovedDaoImpl<UserProfileImage> implements UserProfileImageDao {

	public UserProfileImageDaoImpl(DaoFactoryImpl daoFactory) {
		super(daoFactory);
	}

	@Override
	public void initEntityMembers() throws DaoException {
		this.addForeingKeyEntityMember("user", UserProfileImage::getUser, UserProfileImage::setUser, getDaoFactory().getUserDao());
	}

	@Override
	public UserProfileImage instantiateBlank() {
		return new UserProfileImage();
	}

	@Override
	public DaoImpl<? super UserProfileImage> getSuperClassDao() {
		return getDaoFactory().getProtectedFileDao();
	}

	@Override
	public UserProfileImage getUserUserProfileImageDao(User user) {
		return this.getOneMatching(user, "user");
	}

}
