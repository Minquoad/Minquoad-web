package com.minquoad.websocketEndpoint;

import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.websocket.CloseReason;
import javax.websocket.Endpoint;
import javax.websocket.EndpointConfig;
import javax.websocket.RemoteEndpoint;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint(value = "/TestWebsocket", configurator = ServletAwareConfig.class)
public class TestWebsocket extends Endpoint {

	private Session websocketSession;
    private HttpSession httpSession;

	@Override
	public void onOpen(Session session, EndpointConfig config) {

		httpSession = (HttpSession) config.getUserProperties().get(HttpSession.class.getName());
		websocketSession = session;

		try {
			getRemote().sendText("test4");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onClose(Session session, CloseReason reason) {
		
	}

	public Session getWebsocketSession() {
		return websocketSession;
	}

	public HttpSession getHttpSession() {
		return httpSession;
	}
	
	public RemoteEndpoint.Basic getRemote() {
		return websocketSession.getBasicRemote();
	}

	public ServletContext getContext() {
		return getHttpSession().getServletContext();
	}
	
}
