package com.minquoad.dao.interfaces;

import com.minquoad.entity.User;
import com.minquoad.entity.file.UserProfileImage;

public interface UserProfileImageDao extends Dao<UserProfileImage> {

	public UserProfileImage getUserUserProfileImageDao(User user);

}
