package com.minquoad.websocketEndpoint;

import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.websocket.CloseReason;
import javax.websocket.Endpoint;
import javax.websocket.EndpointConfig;
import javax.websocket.MessageHandler;
import javax.websocket.RemoteEndpoint;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import com.minquoad.dao.interfaces.DaoFactory;
import com.minquoad.entity.User;
import com.minquoad.service.Logger;
import com.minquoad.service.SessionManager;
import com.minquoad.tool.http.ImprovedHttpServlet;

@ServerEndpoint(value = "/ImprovedEndpoint", configurator = HttpSessionLinkedEndpointConfig.class)
public class ImprovedEndpoint extends Endpoint {

	private String role;
	private Session websocketSession;
	private HttpSession httpSession;

	@Override
	public void onOpen(Session session, EndpointConfig config) {

		httpSession = (HttpSession) config.getUserProperties().get(HttpSession.class.getName());
		websocketSession = session;

		session.addMessageHandler(new MessageHandler.Whole<String>() {
			@Override
			public void onMessage(String message) {
				setRole(message);
			}
		});

		SessionManager sessionManager = (SessionManager) getContext().getAttribute(SessionManager.class.getName());
		sessionManager.add(this);

	}

	@Override
	public void onClose(Session session, CloseReason reason) {
		SessionManager sessionManager = (SessionManager) getContext().getAttribute(SessionManager.class.getName());
		sessionManager.remove(this);
	}

	@Override
	public void onError(Session session, Throwable throwable) {
		ServletContext context = getContext();
		SessionManager sessionManager = (SessionManager) context.getAttribute(SessionManager.class.getName());
		sessionManager.remove(this);

		Logger logger = (Logger) context.getAttribute(Logger.class.getName());
		logger.logError(throwable);
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

	public User getUser(DaoFactory daoFactory) {
		return daoFactory.getUserDao().getByPk((Long) httpSession.getAttribute(ImprovedHttpServlet.USER_ID_KEY));
	}
	
	public void sendText(String text) {
		try {
			getRemote().sendText(text);
		} catch (IOException e) {
			e.printStackTrace();
			this.onError(getWebsocketSession(), e);
		}
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public interface ImprovedEndpointFilter {
		public boolean accept(ImprovedEndpoint endpoint);
	}
	
}
