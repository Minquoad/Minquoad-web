package com.minquoad.frontComponent.json;

import org.json.JSONObject;

import com.minquoad.entity.Message;
import com.minquoad.entity.User;

public class MessageEditionJson implements Jsonable {

	public static final String enventKey = "MessageEdition";
	
	private Message message;

	public MessageEditionJson(Message message) {
		this.message = message;
	}

	@Override
	public String toJson() {
		JSONObject eventJsonObject = new JSONObject();
		eventJsonObject.put("enventKey", enventKey);

		JSONObject messageJsonObject = new JSONObject();
		messageJsonObject.put("id", Long.toString(message.getId()));
		messageJsonObject.put("text", message.getText());
		messageJsonObject.put("editedText", 
				message.getEditedText() == null ? JSONObject.NULL : message.getEditedText());
		messageJsonObject.put("instant", message.getInstant());
		messageJsonObject.put("conversation", Long.toString(message.getConversation().getId()));

		User user = message.getUser();
		JSONObject userJsonObject = new JSONObject();
		userJsonObject.put("id", Long.toString(user.getId()));
		userJsonObject.put("nickname", user.getNickname());
		userJsonObject.put("defaultColor", user.getDefaultColorAsHtmlValue());

		messageJsonObject.put("user", userJsonObject);

		eventJsonObject.put("data", messageJsonObject);

		return eventJsonObject.toString();
	}

}
