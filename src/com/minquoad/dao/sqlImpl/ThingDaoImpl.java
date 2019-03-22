package com.minquoad.dao.sqlImpl;

import java.sql.SQLException;
import java.util.List;

import com.minquoad.dao.interfaces.ThingDao;
import com.minquoad.entity.Thing;
import com.minquoad.entity.User;

public class ThingDaoImpl extends ImprovedEntityDaoImpl<Thing> implements ThingDao {

	public ThingDaoImpl(DaoFactoryImpl daoFactory) {
		super(daoFactory);
	}

	@Override
	public Thing instantiateBlank() {
		return new Thing();
	}

	@Override
	public void initEntityMembers() throws SQLException {
		this.addStringEntityMember("description", Thing::getDescription, Thing::setDescription);
		this.addForeingKeyEntityMember("owner", Thing::getOwner, Thing::setOwner, getDaoFactory().getUserDao());
	}

	@Override
	public List<Thing> getUserThings(User user) {
		return this.getAllMatching(user, "owner");
	}

}
