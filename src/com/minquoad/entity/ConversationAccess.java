package com.minquoad.entity;

import com.minquoad.framework.dao.Entity;

public class ConversationAccess extends Entity {

	private User user;

	private Conversation conversation;

	private boolean administrator;
	
	private Message lastSeenMessage;

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

	public boolean isAdministrator() {
		return administrator;
	}

	public void setAdministrator(boolean administrator) {
		this.administrator = administrator;
	}

	public Message getLastSeenMessage() {
		return lastSeenMessage;
	}

	public void setLastSeenMessage(Message lastSeenMessage) {
		this.lastSeenMessage = lastSeenMessage;
	}

}
