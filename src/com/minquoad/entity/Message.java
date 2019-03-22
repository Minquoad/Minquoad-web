package com.minquoad.entity;

import java.time.Instant;

public class Message {

	private Long id;
	private String text;
	private String editedText;
	private Instant instant;
	private User user;
	private Conversation conversation;

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getEditedText() {
		return editedText;
	}

	public void setEditedText(String editedText) {
		this.editedText = editedText;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Conversation getConversation() {
		return conversation;
	}

	public void setConversation(Conversation conversation) {
		this.conversation = conversation;
	}

	public Instant getInstant() {
		return instant;
	}

	public void setInstant(Instant instant) {
		this.instant = instant;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
}
