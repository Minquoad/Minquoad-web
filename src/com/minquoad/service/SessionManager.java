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
		getImprovedEndpoints().add(improvedEndpoint);
	}

	public void remove(ImprovedEndpoint improvedEndpoint) {
		getImprovedEndpoints().remove(improvedEndpoint);
	}

	public int getImprovedEndpointsNumber() {
		return getImprovedEndpoints().size();
	}

	public List<ImprovedEndpoint> getImprovedEndpoints() {
		return improvedEndpoints;
	}

}
