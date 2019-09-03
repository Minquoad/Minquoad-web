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
	protected UserProfileImage instantiateBlank() {
		return new UserProfileImage();
	}

	@Override
	protected DaoImpl<? super UserProfileImage> getSuperClassDao() {
		return getDaoFactory().getProtectedFileDao();
	}

	@Override
	protected void initEntityMembers() throws DaoException {
		this.addForeingKeyEntityMember("user", UserProfileImage::getUser, UserProfileImage::setUser, getDaoFactory().getUserDao());
	}

	@Override
	public UserProfileImage getUserUserProfileImage(User user) {
		return this.getOneMatching("user", user);
	}

}
