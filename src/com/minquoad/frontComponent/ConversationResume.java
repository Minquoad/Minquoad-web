package com.minquoad.frontComponent;

import java.util.Collection;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
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
		ObjectNode conversationResumeJsonObject = JsonNodeFactory.instance.objectNode();

		ObjectNode conversationJsonObject = JsonNodeFactory.instance.objectNode();
		conversationJsonObject.put("id", conversation.getId().toString());
		conversationJsonObject.put("title", conversation.getTitle());
		conversationJsonObject.put("type", conversation.getType());
		conversationResumeJsonObject.set("conversation", conversationJsonObject);

		ArrayNode participantsJsonObject = JsonNodeFactory.instance.arrayNode();
		conversationResumeJsonObject.set("participants", participantsJsonObject);

		for (User user : this.getParticipants()) {
			ObjectNode userJsonObject = JsonNodeFactory.instance.objectNode();
			userJsonObject.put("id", user.getId().toString());
			userJsonObject.put("nickname", user.getNickname());
			participantsJsonObject.add(userJsonObject);
		}

		return conversationResumeJsonObject;
	}

}
