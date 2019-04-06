package com.minquoad.entity;

public class Conversation {

	public static final int TYPE_MONOLOGUE = 0;
	public static final int TYPE_MAIN_BETWEEN_TWO_USERS = 1;

	private Long id;
	private String title;
	private int type;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

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

	public boolean isMainBetweenTwoUsers() {
		return getType() == TYPE_MAIN_BETWEEN_TWO_USERS;
	}

	public boolean isMonologue() {
		return getType() == TYPE_MONOLOGUE;
	}
	
}
