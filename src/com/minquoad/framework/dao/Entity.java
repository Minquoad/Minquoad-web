package com.minquoad.framework.dao;

abstract public class Entity {

	private Integer id;

	public Entity() {
	}

	public final Integer getId() {
		return id;
	}

	public final void setId(Integer id) {
		if (this.id != null && id != this.id) {
			new Exception("Id changes are not allowed ones initialised.").printStackTrace();
		}
		this.id = id;
	}

}
