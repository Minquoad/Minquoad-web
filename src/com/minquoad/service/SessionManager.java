package com.minquoad.service;

import java.util.LinkedList;
import java.util.List;

import com.minquoad.websocketEndpoint.ImprovedEndpoint;

public class SessionManager {

	private List<ImprovedEndpoint> improvedEndpoints;

	public SessionManager() {
		improvedEndpoints = new LinkedList<ImprovedEndpoint>();
	}

	public void add(ImprovedEndpoint improvedEndpoint) {
		improvedEndpoints.add(improvedEndpoint);
	}

	public void remove(ImprovedEndpoint improvedEndpoint) {
		improvedEndpoints.remove(improvedEndpoint);
	}

	public List<ImprovedEndpoint> getImprovedEndpoints() {
		return improvedEndpoints;
	}

}
