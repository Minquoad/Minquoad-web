package com.minquoad.dao.sqlImpl;

import com.minquoad.dao.interfaces.ImprovementSuggestionDao;
import com.minquoad.entity.ImprovementSuggestion;

public class ImprovementSuggestionDaoImpl extends ImprovedDaoImpl<ImprovementSuggestion> implements ImprovementSuggestionDao {

	public ImprovementSuggestionDaoImpl(DaoFactoryImpl daoFactory) {
		super(daoFactory);
	}

	@Override
	protected void initEntityMembers() {
		this.addLongEntityMember("id", ImprovementSuggestion::getId, ImprovementSuggestion::setId);
		this.addStringEntityMember("text", ImprovementSuggestion::getText, ImprovementSuggestion::setText);
		this.addForeingKeyEntityMember("user", ImprovementSuggestion::getUser, ImprovementSuggestion::setUser, getDaoFactory().getUserDao());
		this.addInstantEntityMember("instant", ImprovementSuggestion::getInstant, ImprovementSuggestion::setInstant);
	}

	@Override
	public ImprovementSuggestion instantiateBlank() {
		return new ImprovementSuggestion();
	}

}
