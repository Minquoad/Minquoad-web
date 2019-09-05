package com.minquoad.dao.sqlImpl;

import com.minquoad.dao.interfaces.ConsiderationDao;
import com.minquoad.entity.Consideration;
import com.minquoad.entity.User;

public class ConsiderationDaoImpl extends DaoImpl<Consideration> implements ConsiderationDao {

	public ConsiderationDaoImpl(DaoFactoryImpl daoFactory) {
		super(daoFactory);
	}

	@Override
	protected Consideration instantiateBlank() {
		return new Consideration();
	}

	@Override
	protected void initSuperClass() {
	}

	@Override
	protected void initSubClasses() {
	}

	@Override
	protected void initEntityMembers() {
		this.addLongEntityMember("id", Consideration::getId, Consideration::setId);
		this.addForeingKeyEntityMember("consideringUser", User.class, Consideration::getConsideringUser, Consideration::setConsideringUser);
		this.addForeingKeyEntityMember("consideredUser", User.class, Consideration::getConsideredUser, Consideration::setConsideredUser);
		this.addIntegerEntityMember("status", Consideration::getStatus, Consideration::setStatus);
		this.addIntegerEntityMember("perceptionColor", Consideration::getPerceptionColor, Consideration::setPerceptionColor);
	}

	@Override
	protected boolean isPrimaryKeyRandomlyGenerated() {
		return true;
	}

}
