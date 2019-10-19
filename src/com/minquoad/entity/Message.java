package com.minquoad.entity;

import java.time.Instant;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.minquoad.entity.file.MessageFile;

public class Message {

	private Long id;
	private String text;
	private String editedText;
	private Instant instant;
	private User user;
	private Conversation conversation;
	private MessageFile messageFile;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
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

	public boolean isEdited() {
		return getEditedText() != null;
	}

	public boolean hasMessageFile() {
		return getMessageFile() != null;
	}

	public MessageFile getMessageFile() {
		return messageFile;
	}

	public void setMessageFile(MessageFile messageFile) {
		this.messageFile = messageFile;
	}

	public JsonNode toJson() {

		User user = this.getUser();
		MessageFile file = this.getMessageFile();

		ObjectNode messageJsonObject = JsonNodeFactory.instance.objectNode();
		messageJsonObject.put("id", this.getId().toString());
		messageJsonObject.put("text", this.getText());
		messageJsonObject.put("editedText", this.getEditedText());
		messageJsonObject.put("instant", this.getInstant().toString());
		messageJsonObject.put("conversation", this.getConversation().getId().toString());

		ObjectNode userJsonObject = JsonNodeFactory.instance.objectNode();
		userJsonObject.put("id", user.getId().toString());
		userJsonObject.put("nickname", user.getNickname());
		userJsonObject.put("defaultColor", user.getDefaultColorAsHtmlValue());

		ObjectNode fileJsonObject = null;
		if (file != null) {
			fileJsonObject = JsonNodeFactory.instance.objectNode();
			fileJsonObject.put("id", file.getId().toString());
			fileJsonObject.put("image", file.isImage());
			fileJsonObject.put("originalName", file.getOriginalName());
		}

		messageJsonObject.set("user", userJsonObject);
		messageJsonObject.set("messageFile", fileJsonObject);

		return messageJsonObject;
	}
	
}
