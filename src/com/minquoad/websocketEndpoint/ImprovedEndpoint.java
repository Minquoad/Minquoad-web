package com.minquoad.websocketEndpoint;

import java.util.ArrayList;
import java.util.List;

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
import com.minquoad.service.Database;
import com.minquoad.service.Logger;
import com.minquoad.service.ServicesManager;
import com.minquoad.service.SessionManager;
import com.minquoad.tool.http.ImprovedHttpServlet;

@ServerEndpoint(value = "/ImprovedEndpoint", configurator = HttpSessionLinkedEndpointConfig.class)
public class ImprovedEndpoint extends Endpoint {

	private List<String> roles;
	private Session websocketSession;
	private HttpSession httpSession;

	@Override
	public void onOpen(Session session, EndpointConfig config) {
		roles = new ArrayList<String>();

		httpSession = (HttpSession) config.getUserProperties().get(HttpSession.class.getName());
		websocketSession = session;

		DaoFactory daoFactory = getService(Database.class).getNewDaoFactory();
		if (!getUser(daoFactory).isBlocked()) {

			session.addMessageHandler(new MessageHandler.Whole<String>() {
				@Override
				public void onMessage(String message) {
					addRole(message);
				}
			});

			getService(SessionManager.class).add(this);
		}
	}

	@Override
	public void onClose(Session session, CloseReason reason) {
		getService(SessionManager.class).remove(this);
	}

	@Override
	public void onError(Session session, Throwable throwable) {
		getService(Logger.class).logError(throwable);
		getService(SessionManager.class).remove(this);
	}

	public Session getWebsocketSession() {
		return websocketSession;
	}

	public HttpSession getHttpSession() {
		return httpSession;
	}

	public RemoteEndpoint.Async getRemote() {
		return websocketSession.getAsyncRemote();
	}

	public ServletContext getContext() {
		return getHttpSession().getServletContext();
	}

	public User getUser(DaoFactory daoFactory) {
		return daoFactory.getUserDao().getByPk(getUserId());
	}

	public Long getUserId() {
		return (Long) httpSession.getAttribute(ImprovedHttpServlet.USER_ID_KEY);
	}

	public void sendText(String text) {
		getRemote().sendText(text);
	}

	public boolean hasRole(String role) {
		return this.roles.contains(role);
	}

	public void addRole(String role) {
		this.roles.add(role);
	}

	public interface ImprovedEndpointFilter {
		public boolean accepts(ImprovedEndpoint endpoint);
	}

	public <ServiceClass> ServiceClass getService(Class<ServiceClass> serviceClass) {
		return ServicesManager.getService(getContext(), serviceClass);
	}

}
