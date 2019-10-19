package com.minquoad.websocketEndpoint;

import java.util.Collection;
import java.util.HashSet;

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
import com.minquoad.tool.ImprovedHttpServlet;

@ServerEndpoint(value = "/ImprovedEndpoint", configurator = HttpSessionLinkedEndpointConfig.class)
public class ImprovedEndpoint extends Endpoint {

	private boolean available;
	private Collection<String> roles;
	
	private ServletContext servletContext;
	private HttpSession httpSession;
	private Session websocketSession;

	@Override
	public void onOpen(Session session, EndpointConfig config) {
		roles = new HashSet<String>();
		setAvailable(true);

		httpSession = (HttpSession) config.getUserProperties().get(HttpSession.class.getName());
		servletContext = httpSession.getServletContext();
		websocketSession = session;

		Database database = getService(Database.class);
		DaoFactory daoFactory = database.pickOneDaoFactory();
		User user = getUser(daoFactory);
		if (user != null && user.isBlocked()) {
			setAvailable(false);
		}
		database.giveBack(daoFactory);

		session.addMessageHandler(new MessageHandler.Whole<String>() {
			@Override
			public void onMessage(String message) {
				addRole(message);
			}
		});

		getService(SessionManager.class).add(this);
	}

	@Override
	public void onClose(Session session, CloseReason reason) {
		getService(SessionManager.class).remove(this);
	}

	@Override
	public void onError(Session session, Throwable throwable) {
		getService(SessionManager.class).remove(this);
		getService(Logger.class).logError(throwable);
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
		return servletContext;
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
		return roles.contains(role);
	}

	public void addRole(String role) {
		// in order to handle some attacks
		if (role.length() > 1024) {
			RuntimeException exception = new RuntimeException("role too long");
			exception.printStackTrace();
			getService(Logger.class).logError(exception);
			return;
		}
		// in order to handle some attacks
		if (this.roles.size() == 1024) {
			RuntimeException exception = new RuntimeException("too many roles for one endpoint");
			exception.printStackTrace();
			getService(Logger.class).logError(exception);
			return;
		}

		this.roles.add(role);
	}

	public <ServiceClass> ServiceClass getService(Class<ServiceClass> serviceClass) {
		return ServicesManager.getService(getContext(), serviceClass);
	}

	public boolean isAvailable() {
		return available;
	}

	public void setAvailable(boolean available) {
		this.available = available;
	}
}
