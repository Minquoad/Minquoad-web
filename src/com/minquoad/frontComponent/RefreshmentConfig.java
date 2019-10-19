package com.minquoad.frontComponent;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class RefreshmentConfig {

	private boolean hard; 
	private int maximumWait;
	
	public RefreshmentConfig(boolean hard, int maximumWait) {
		this.hard = hard;
		this.maximumWait = maximumWait;
	}

	public JsonNode toJson() {
		ObjectNode json = JsonNodeFactory.instance.objectNode();
		json.put("hard", hard);
		json.put("maximumWait", maximumWait);
		return json;
	}
	
}
