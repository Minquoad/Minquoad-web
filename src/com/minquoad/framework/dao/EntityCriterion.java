package com.minquoad.framework.dao;

public class EntityCriterion {

	private Object value;
	private String memberName;

	public EntityCriterion(Object value, String memberName) {
		this.value = value;
		this.memberName = memberName;
	}

	public Object getValue() {
		return value;
	}

	public String getName() {
		return memberName;
	}

	public EntityCriterion[] toArray() {
		EntityCriterion[] criteria = new EntityCriterion[1];
		criteria[0] = this;
		return criteria;
	}

}
