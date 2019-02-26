package entity;

import framework.dao.Entity;

public class ConversationAccess extends Entity {

	private User user;
	
	private Conversation conversation;

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

}
