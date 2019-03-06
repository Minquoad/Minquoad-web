package com.minquoad.frontComponent;

import java.util.List;

import com.minquoad.entity.Conversation;
import com.minquoad.entity.User;

public class ConversationResume {
	
	private Conversation conversation;

	private List<User> participants;

	public Conversation getConversation() {
		return conversation;
	}

	public void setConversation(Conversation conversation) {
		this.conversation = conversation;
	}

	public List<User> getParticipants() {
		return participants;
	}

	public void setParticipants(List<User> participants) {
		this.participants = participants;
	}

}
