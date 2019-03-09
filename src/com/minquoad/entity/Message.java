package com.minquoad.entity;

import java.time.Instant;

import com.minquoad.framework.dao.Entity;

public class Message extends Entity {

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
	
}
