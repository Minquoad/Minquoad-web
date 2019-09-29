package com.minquoad.frontComponent;

import java.util.Collection;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
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

	public JsonNode toJson() {

		ObjectNode conversationJsonObject = JsonNodeFactory.instance.objectNode();
		conversationJsonObject.put("id", Long.toString(conversation.getId()));
		conversationJsonObject.put("title", conversation.getTitle());
		conversationJsonObject.put("type", conversation.getType());

		ObjectNode conversationResumeJsonObject = JsonNodeFactory.instance.objectNode();
		conversationResumeJsonObject.set("conversation", conversationJsonObject);

		return conversationResumeJsonObject;
	}

}
