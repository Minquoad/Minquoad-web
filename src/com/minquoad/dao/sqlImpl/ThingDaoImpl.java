package com.minquoad.dao.sqlImpl;

import java.util.List;

import com.minquoad.dao.interfaces.ThingDao;
import com.minquoad.entity.Thing;
import com.minquoad.entity.User;
import com.minquoad.framework.dao.DaoException;

public class ThingDaoImpl extends DaoImpl<Thing> implements ThingDao {

	public ThingDaoImpl(DaoFactoryImpl daoFactory) {
		super(daoFactory);
	}

	@Override
	protected Thing instantiateBlank() {
		return new Thing();
	}

	@Override
	protected void initSuperClass() {
	}

	@Override
	protected void initSubClasses() {
	}

	@Override
	protected void initEntityMembers() throws DaoException {
		this.addIntegerEntityMember("id", Thing::getId, Thing::setId);
		this.addStringEntityMember("description", Thing::getDescription, Thing::setDescription);
		this.addForeingKeyEntityMember("owner", User.class, Thing::getOwner, Thing::setOwner);
	}

	@Override
	public List<Thing> getUserThings(User user) {
		return this.getAllMatching("owner", user);
	}

}
