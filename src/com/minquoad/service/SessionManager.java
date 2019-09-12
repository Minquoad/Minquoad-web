package com.minquoad.service;

import java.util.Collection;
import java.util.LinkedList;

import com.minquoad.websocketEndpoint.ImprovedEndpoint;

public class SessionManager {

	private Collection<ImprovedEndpoint> improvedEndpoints;

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

	public Collection<ImprovedEndpoint> getImprovedEndpoints() {
		return improvedEndpoints;
	}

}
