package com.minquoad.frontComponent;

import java.util.Collection;

import com.minquoad.entity.Conversation;
import com.minquoad.entity.User;

public class ConversationResume {
	
	private Conversation conversation;

	private Collection<User> participants;

	public Conversation getConversation() {
		return conversation;
	}

	public void setConversation(Conversation conversation) {
		this.conversation = conversation;
	}

	public Collection<User> getParticipants() {
		return participants;
	}

	public void setParticipants(Collection<User> participants) {
		this.participants = participants;
	}

}
