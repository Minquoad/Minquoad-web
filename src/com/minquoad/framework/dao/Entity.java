package com.minquoad.framework.dao;

abstract public class Entity {

	private Integer id;

	public Entity() {
	}

	public final Integer getId() {
		return id;
	}

	public final void setId(Integer id) {
		this.id = id;
	}

}
