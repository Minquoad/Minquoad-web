package com.minquoad.service;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;

import javax.servlet.ServletContext;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.minquoad.entity.User;
import com.minquoad.websocketEndpoint.ImprovedEndpoint;

public class SessionManager {

	private final ServletContext servletContext;

	private Collection<ImprovedEndpoint> improvedEndpoints;

	public SessionManager(ServletContext servletContext) {
		this.servletContext = servletContext;
		improvedEndpoints = new LinkedList<ImprovedEndpoint>();
	}

	public void add(ImprovedEndpoint improvedEndpoint) {
		getImprovedEndpoints().add(improvedEndpoint);
	}

	public void remove(ImprovedEndpoint improvedEndpoint) {
		getImprovedEndpoints().remove(improvedEndpoint);
	}

	public int getImprovedEndpointsNumber() {
		return getImprovedEndpoints().size();
	}

	public Collection<ImprovedEndpoint> getImprovedEndpoints() {
		return improvedEndpoints;
	}

	public static void sendJsonToClientsWithRole(ServletContext context, JsonNode json, Collection<User> users, String role) {
		ServicesManager.getService(context, SessionManager.class).sendJsonToClientsWithRole(json, users, role);
	}

	public void sendJsonToClientsWithRole(JsonNode json, Collection<User> users, String role) {

		Collection<Long> usersIds = null;
		if (users != null) {
			usersIds = new HashSet<Long>();
			for (User user : users) {
				if (user == null) {
					usersIds.add(null);
				} else {
					usersIds.add(user.getId());
				}
			}
		}

		ObjectNode eventJsonObject = JsonNodeFactory.instance.objectNode();
		eventJsonObject.put("role", role);
		eventJsonObject.set("data", json);
		String text = eventJsonObject.toString();

		for (ImprovedEndpoint endpoint : this.getImprovedEndpoints()) {
			if (endpoint.isAvailable() && endpoint.hasRole(role)) {
				if (usersIds == null || usersIds.contains(endpoint.getUserId())) {
					try {
						endpoint.sendText(text);
					} catch (Exception e) {
						e.printStackTrace();
						ServicesManager.getService(servletContext, Logger.class).logError(e);
					}
				}
			}
		}
	}

}
