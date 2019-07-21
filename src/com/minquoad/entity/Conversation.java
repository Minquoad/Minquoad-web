package com.minquoad.entity;

public class Conversation {

	public static final int TYPE_MONOLOGUE = 0;
	public static final int TYPE_MAIN_BETWEEN_TWO_USERS = 1;
	public static final int TYPE_CREATED_BY_USER = 2;

	private Long id;
	private String title;
	private Integer type;
	private Message lastMessage;

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

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Message getLastMessage() {
		return lastMessage;
	}

	public void setLastMessage(Message lastMessage) {
		this.lastMessage = lastMessage;
	}

	public boolean isMainBetweenTwoUsers() {
		return getType() == TYPE_MAIN_BETWEEN_TWO_USERS;
	}

	public boolean isMonologue() {
		return getType() == TYPE_MONOLOGUE;
	}

	public boolean isCreatedByUser() {
		return getType() == TYPE_CREATED_BY_USER;
	}
	
}
