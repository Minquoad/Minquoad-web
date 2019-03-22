package com.minquoad.dao.sqlImpl;

import java.sql.SQLException;

import com.minquoad.dao.interfaces.UserPictureDao;
import com.minquoad.entity.file.UserPicture;
import com.minquoad.framework.dao.DaoImpl;

public class UserPictureDaoImpl extends ImprovedEntityDaoImpl<UserPicture> implements UserPictureDao {

	public UserPictureDaoImpl(DaoFactoryImpl daoFactory) {
		super(daoFactory);
	}

	@Override
	public void initEntityMembers() throws SQLException {
		this.addIntegerEntityMember("id", UserPicture::getId, UserPicture::setId, true);
		this.addForeingKeyEntityMember("user", UserPicture::getUser, UserPicture::setUser, getDaoFactory().getUserDao());
	}

	@Override
	public UserPicture instantiateBlank() {
		return new UserPicture();
	}

	@Override
	public DaoImpl<? super UserPicture> getSuperClassDao() {
		return getDaoFactory().getUserPictureDao();
	}
	
}
