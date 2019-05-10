package com.minquoad.service;

import java.util.LinkedList;
import java.util.List;

import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import javax.websocket.Session;

@WebListener
public class SessionManager implements HttpSessionListener {

	private List<HttpSession> httpSessions;
	private List<Session> websocketSessions;

	public SessionManager() {
		httpSessions = new LinkedList<HttpSession>();
	}

	@Override
	public void sessionCreated(HttpSessionEvent event) {
		httpSessions.add(event.getSession());
	}

	@Override
	public void sessionDestroyed(HttpSessionEvent event) {
		httpSessions.remove(event.getSession());
	}

	public void add(Session websocketSession) {
		websocketSessions.add(websocketSession);
	}

	public void remove(Session websocketSession) {
		websocketSessions.remove(websocketSession);
	}
	
}
