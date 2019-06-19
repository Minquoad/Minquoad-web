package com.minquoad.frontComponent.json;

import org.json.JSONObject;

import com.minquoad.entity.Message;
import com.minquoad.entity.User;
import com.minquoad.entity.file.MessageFile;

public class MessageJson implements Jsonable {

	private String enventKey;
	private Message message;

	public MessageJson(Message message, String enventKey) {
		this.message = message;
		this.enventKey = enventKey;
	}

	@Override
	public String toJson() {
		User user = message.getUser();
		MessageFile file = message.getMessageFile();

		JSONObject eventJsonObject = new JSONObject();
		eventJsonObject.put("enventKey", enventKey);

		JSONObject messageJsonObject = new JSONObject();
		messageJsonObject.put("id", Long.toString(message.getId()));
		messageJsonObject.put("text", message.getText());
		messageJsonObject.put("editedText",
				message.getEditedText() == null ? JSONObject.NULL : message.getEditedText());
		messageJsonObject.put("instant", message.getInstant());
		messageJsonObject.put("conversation", Long.toString(message.getConversation().getId()));

		JSONObject userJsonObject = new JSONObject();
		userJsonObject.put("id", Long.toString(user.getId()));
		userJsonObject.put("nickname", user.getNickname());
		userJsonObject.put("defaultColor", user.getDefaultColorAsHtmlValue());

		JSONObject fileJsonObject = null;
		if (file != null) {
			fileJsonObject = new JSONObject();
			fileJsonObject.put("id", Long.toString(file.getId()));
			fileJsonObject.put("image", file.isImage());
			fileJsonObject.put("originalName", file.getOriginalName());
		}

		messageJsonObject.put("user", userJsonObject);
		messageJsonObject.put("messageFile",
				fileJsonObject == null ? JSONObject.NULL : fileJsonObject);

		eventJsonObject.put("data", messageJsonObject);

		return eventJsonObject.toString();
	}

}
