package com.minquoad.entity;

import com.minquoad.framework.dao.Entity;

public class Conversation extends Entity {

	public static final int TYPE_MONOLOGUE = 0;
	public static final int TYPE_MAIN_BETWEEN_TWO_USERS = 1;

	private Integer id;
	private String title;

	private int type;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

}
