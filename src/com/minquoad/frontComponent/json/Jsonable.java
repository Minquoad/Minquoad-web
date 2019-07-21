package com.minquoad.frontComponent.json;

import com.fasterxml.jackson.databind.JsonNode;

public interface Jsonable {
	public JsonNode toJson();
}
