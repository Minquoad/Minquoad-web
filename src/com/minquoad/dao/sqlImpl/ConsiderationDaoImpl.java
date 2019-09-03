package com.minquoad.dao.sqlImpl;

import com.minquoad.dao.interfaces.ConsiderationDao;
import com.minquoad.entity.Consideration;

public class ConsiderationDaoImpl extends ImprovedDaoImpl<Consideration> implements ConsiderationDao {

	public ConsiderationDaoImpl(DaoFactoryImpl daoFactory) {
		super(daoFactory);
	}

	@Override
	protected void initEntityMembers() {
		this.addLongEntityMember("id", Consideration::getId, Consideration::setId);
		this.addForeingKeyEntityMember("consideringUser", Consideration::getConsideringUser, Consideration::setConsideringUser, getDaoFactory().getUserDao());
		this.addForeingKeyEntityMember("consideredUser", Consideration::getConsideredUser, Consideration::setConsideredUser, getDaoFactory().getUserDao());
		this.addIntegerEntityMember("status", Consideration::getStatus, Consideration::setStatus);
		this.addIntegerEntityMember("perceptionColor", Consideration::getPerceptionColor, Consideration::setPerceptionColor);
	}

	@Override
	public Consideration instantiateBlank() {
		return new Consideration();
	}

	@Override
	public boolean isPrimaryKeyRandomlyGenerated() {
		return true;
	}

}
