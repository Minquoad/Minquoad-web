package com.minquoad.dao.sqlImpl;

import com.minquoad.dao.interfaces.ImprovementSuggestionDao;
import com.minquoad.entity.ImprovementSuggestion;
import com.minquoad.entity.User;

public class ImprovementSuggestionDaoImpl extends DaoImpl<ImprovementSuggestion> implements ImprovementSuggestionDao {

	public ImprovementSuggestionDaoImpl(DaoFactoryImpl daoFactory) {
		super(daoFactory);
	}

	@Override
	protected ImprovementSuggestion instantiateBlank() {
		return new ImprovementSuggestion();
	}

	@Override
	protected void initSuperClass() {
	}

	@Override
	protected void initSubClasses() {
	}

	@Override
	protected void initEntityMembers() {
		this.addLongEntityMember("id", ImprovementSuggestion::getId, ImprovementSuggestion::setId);
		this.addStringEntityMember("text", ImprovementSuggestion::getText, ImprovementSuggestion::setText);
		this.addForeingKeyEntityMember("user", User.class, ImprovementSuggestion::getUser, ImprovementSuggestion::setUser);
		this.addInstantEntityMember("instant", ImprovementSuggestion::getInstant, ImprovementSuggestion::setInstant);
	}

}
