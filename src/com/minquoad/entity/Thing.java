package com.minquoad.entity;

import com.minquoad.framework.dao.Entity;

public class Thing extends Entity {

	private User owner;
	
	private String description;

	public User getOwner() {
		return owner;
	}

	public void setOwner(User owner) {
		this.owner = owner;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
