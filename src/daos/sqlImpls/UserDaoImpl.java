package daos.sqlImpls;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;

import Entities.User;
import daos.Database;
import daos.interfaces.UserDao;
import frameworks.daos.EntityDaoImpl;
import frameworks.daos.entityMembers.DateEntityMember;
import frameworks.daos.entityMembers.IntEntityMember;
import frameworks.daos.entityMembers.StringEntityMember;

public class UserDaoImpl extends EntityDaoImpl<User> implements UserDao {

	public User instantiateBlank() {
		return new User();
	}

	public String getTableName() {
		return "User";
	}

	@Override
	public void initEntityMembers() {
		this.addEntityMember(new StringEntityMember<User>() {
			public String getName() {
				return "nickname";
			}

			public String getValue(User entity) {
				return entity.getNickname();
			}

			public void setValue(User entity, String string) {
				entity.setNickname(string);
			}
		});
		this.addEntityMember(new StringEntityMember<User>() {
			public String getName() {
				return "hashedSaltedPassword";
			}

			public String getValue(User entity) {
				return entity.getHashedSaltedPassword();
			}

			public void setValue(User entity, String string) {
				entity.setHashedSaltedPassword(string);
			}
		});
		this.addEntityMember(new StringEntityMember<User>() {
			public String getName() {
				return "pictureName";
			}

			public String getValue(User entity) {
				return entity.getPictureName();
			}

			public void setValue(User entity, String string) {
				entity.setPictureName(string);
			}
		});
		this.addEntityMember(new DateEntityMember<User>() {
			public String getName() {
				return "registrationDate";
			}

			public Date getValue(User entity) {
				return entity.getRegistrationDate();
			}

			public void setValue(User entity, Date value) {
				entity.setRegistrationDate(value);
			}
		});
		this.addEntityMember(new DateEntityMember<User>() {
			public String getName() {
				return "lastActivityDate";
			}

			public Date getValue(User entity) {
				return entity.getLastActivityDate();
			}

			public void setValue(User entity, Date value) {
				entity.setLastActivityDate(value);
			}
		});
		this.addEntityMember(new IntEntityMember<User>() {
			public String getName() {
				return "adminLevel";
			}

			public Integer getValue(User entity) {
				return entity.getAdminLevel();
			}

			public void setValue(User entity, Integer value) {
				entity.setAdminLevel(value);
			}
		});
	}

	@Override
	public PreparedStatement prepareStatement(String statement) throws SQLException {
		return Database.prepareStatement(statement);
	}

}
