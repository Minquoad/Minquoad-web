package dao.sqlImpl;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import dao.Database;
import dao.interfaces.UserDao;
import entity.User;
import framework.dao.EntityDaoImpl;

public class UserDaoImpl extends EntityDaoImpl<User> implements UserDao {

	public User instantiateBlank() {
		return new User();
	}

	public String getTableName() {
		return "User";
	}

	@Override
	public void initEntityMembers() {
		this.addStringEntityMember("nickname", User::getNickname, User::setNickname);
		this.addStringEntityMember("hashedSaltedPassword", User::getHashedSaltedPassword,
				User::setHashedSaltedPassword);
		this.addStringEntityMember("pictureName", User::getPictureName, User::setPictureName);
		this.addDateEntityMember("registrationDate", User::getRegistrationDate, User::setRegistrationDate);
		this.addDateEntityMember("lastActivityDate", User::getLastActivityDate, User::setLastActivityDate);
		this.addIntegerEntityMember("adminLevel", User::getAdminLevel, User::setAdminLevel);
		this.addDateEntityMember("unblockDate", User::getUnblockDate, User::setUnblockDate);
	}

	@Override
	public PreparedStatement prepareStatement(String statement) throws SQLException {
		return Database.prepareStatement(statement);
	}

}
