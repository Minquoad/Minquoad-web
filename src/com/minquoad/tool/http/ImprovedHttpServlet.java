package com.minquoad.tool.http;

import java.io.IOException;
import java.time.Instant;
import java.util.List;
import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.minquoad.dao.interfaces.Dao;
import com.minquoad.dao.interfaces.DaoFactory;
import com.minquoad.dao.interfaces.DaoGetter;
import com.minquoad.entity.RequestLog;
import com.minquoad.entity.User;
import com.minquoad.service.Database;
import com.minquoad.service.Deployment;
import com.minquoad.service.Logger;
import com.minquoad.service.SessionManager;
import com.minquoad.service.StorageManager;
import com.minquoad.tool.InternationalizationTool;
import com.minquoad.unit.UnitFactory;
import com.minquoad.websocketEndpoint.ImprovedEndpoint;
import com.minquoad.websocketEndpoint.ImprovedEndpoint.ImprovedEndpointFilter;

public abstract class ImprovedHttpServlet extends HttpServlet {

	public static final String POST_METHOD = "POST";
	public static final String REFERER_HEADER_KEY = "referer";

	// request keys
	private final static String DAO_FACTORY_KEY = "daoFactory";
	private final static String UNIT_FACTORY_KEY = "unitFactory";

	protected final static String USER_KEY = "user";
	protected final static String CONTROLLING_ADMIN_KEY = "controllingAdmin";

	// session keys
	public final static String LOCALE_KEY = "locale";
	public final static String USER_ID_KEY = "userId";
	public final static String LAST_REFUSED_URL_KEY = "lastRefusedUrl";
	public final static String CONTROLLING_ADMIN_ID_KEY = "controllingAdminId";

	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		Instant serviceStartingInstant = Instant.now();

		RequestLog requestLog = new RequestLog();

		try {
			requestLog.setInstant(serviceStartingInstant);
			requestLog.setServletName(this.getServletName());
			requestLog.setIpAddress(request.getRemoteAddr());

			request.setCharacterEncoding("UTF-8");
			response.setCharacterEncoding("UTF-8");

			requestLog.setUrl(getCurrentUrlWithArguments(request));

			HttpSession session = request.getSession();

			User user = getDaoFactory(request).getUserDao().getByPk((Long) session.getAttribute(USER_ID_KEY));

			User controllingAdmin = null;

			if (user != null) {
				requestLog.setUser(user);
				setUser(request, user);

				controllingAdmin = getDaoFactory(request).getUserDao().getByPk((Long) session.getAttribute(CONTROLLING_ADMIN_ID_KEY));

				if (controllingAdmin == null) {
					user.setLastActivityInstant(Instant.now());
					getDaoFactory(request).getUserDao().persist(user);

				} else {
					requestLog.setControllingAdmin(controllingAdmin);
					setControllingAdmin(request, controllingAdmin);

					controllingAdmin.setLastActivityInstant(Instant.now());
					getDaoFactory(request).getUserDao().persist(controllingAdmin);
				}
			}

			Locale locale = getLocale(session);
			if (user == null) {
				if (locale == null) {
					setLocale(session, InternationalizationTool.getClosestValidLocale(request.getLocales()));
				}

			} else {
				User definingLocalUser = user;
				if (controllingAdmin != null) {
					definingLocalUser = controllingAdmin;
				}

				if (locale == null || definingLocalUser.getLanguage() != locale.getLanguage()) {
					locale = new Locale(definingLocalUser.getLanguage(), request.getLocale().getCountry());
					setLocale(session, locale);
				}
			}

			if (controllingAdmin == null) {

				if (getDeployment().isOpen()) {

					if (user != null
							&& user.isBlocked()
							&& !request.getServletPath().equals("/BlockedAccount")
							&& !request.getServletPath().equals("/OutLoging")) {

						if (isFullPage()) {
							response.sendRedirect(request.getContextPath() + "/BlockedAccount");
						} else {
							response.setStatus(HttpServletResponse.SC_FORBIDDEN);
						}
						return;
					}

				} else {

					if ((user == null || !user.isAdmin())
							&& !request.getServletPath().equals("/BlockedSite")
							&& !request.getServletPath().equals("/InLoging")
							&& !request.getServletPath().equals("/OutLoging")) {

						if (isFullPage()) {
							response.sendRedirect(request.getContextPath() + "/BlockedSite");
						} else {
							response.setStatus(HttpServletResponse.SC_FORBIDDEN);
						}
						return;
					}
				}

			} else {

				if (!controllingAdmin.isAdmin()
						&& !request.getServletPath().equals("/Unpossession")) {

					if (isFullPage()) {
						response.sendRedirect(request.getContextPath() + "/Unpossession");
					} else {
						response.setStatus(HttpServletResponse.SC_FORBIDDEN);
					}
					return;
				}
			}

			if (!this.isAccessible(request)) {

				if (user == null) {
					if (isFullPage()) {
						session.setAttribute(LAST_REFUSED_URL_KEY, getCurrentUrlWithArguments(request));
						response.sendRedirect(request.getContextPath() + "/InLoging");
						return;
					} else {
						response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
						return;
					}

				} else {
					if (isFullPage()) {
						response.sendRedirect(request.getContextPath() + "/");
					} else {
						response.setStatus(HttpServletResponse.SC_FORBIDDEN);
					}
					return;
				}
			}

			if (!request.getServletPath().equals("/InLoging")) {
				session.removeAttribute(LAST_REFUSED_URL_KEY);
			}

			super.service(request, response);

		} catch (Exception e) {
			requestLog.setError(Logger.getStackTraceAsString(e));
			throw e;

		} finally {
			if (isLoggingAllRequests() || requestLog.getError() != null) {
				requestLog.setServiceDuration((int) (Instant.now().toEpochMilli() - serviceStartingInstant.toEpochMilli()));
				getDaoFactory(request).getRequestLogDao().persist(requestLog);
			}
		}
	}

	public String getCurrentUrlWithArguments(HttpServletRequest request) {
		String currentUrlWithArguments = request.getRequestURI();
		String queryString = request.getQueryString();
		if (queryString != null) {
			currentUrlWithArguments += "?" + queryString;
		}
		return currentUrlWithArguments;
	}

	public boolean isLoggingAllRequests() {
		return true;
	}

	public boolean isFullPage() {
		return true;
	}

	public static DaoFactory getDaoFactory(HttpServletRequest request) {
		if (request.getAttribute(DAO_FACTORY_KEY) == null) {
			Database database = (Database) request.getServletContext().getAttribute(Database.class.getName());
			request.setAttribute(DAO_FACTORY_KEY, database.getNewDaoFactory());
		}
		return (DaoFactory) request.getAttribute(DAO_FACTORY_KEY);
	}

	public static UnitFactory getUnitFactory(HttpServletRequest request) {
		if (request.getAttribute(UNIT_FACTORY_KEY) == null) {
			request.setAttribute(UNIT_FACTORY_KEY, new UnitFactory(request.getServletContext(), getDaoFactory(request)));
		}
		return (UnitFactory) request.getAttribute(UNIT_FACTORY_KEY);
	}

	public StorageManager getStorageManager() {
		return (StorageManager) getServletContext().getAttribute(StorageManager.class.getName());
	}

	public static StorageManager getStorageManager(HttpServletRequest request) {
		return (StorageManager) request.getServletContext().getAttribute(StorageManager.class.getName());
	}

	public Deployment getDeployment() {
		return (Deployment) getServletContext().getAttribute(Deployment.class.getName());
	}

	public static Deployment getDeployment(HttpServletRequest request) {
		return (Deployment) request.getServletContext().getAttribute(Deployment.class.getName());
	}

	public SessionManager getSessionManager() {
		return (SessionManager) getServletContext().getAttribute(SessionManager.class.getName());
	}

	public static SessionManager getSessionManager(HttpServletRequest request) {
		return (SessionManager) request.getServletContext().getAttribute(SessionManager.class.getName());
	}

	public abstract boolean isAccessible(HttpServletRequest request);

	public static User getUser(HttpServletRequest request) {
		return (User) request.getAttribute(USER_KEY);
	}

	private static void setUser(HttpServletRequest request, User user) {
		request.setAttribute(USER_KEY, user);
	}

	public static void setSessionUser(HttpServletRequest request, User user) {
		request.getSession().setAttribute(USER_ID_KEY, user.getId());
		setUser(request, user);
	}

	public static User getControllingAdmin(HttpServletRequest request) {
		return (User) request.getAttribute(CONTROLLING_ADMIN_KEY);
	}

	private static void setControllingAdmin(HttpServletRequest request, User user) {
		request.setAttribute(CONTROLLING_ADMIN_KEY, user);
	}

	public static void setSessionControllingAdmin(HttpServletRequest request, User user) {
		request.getSession().setAttribute(CONTROLLING_ADMIN_ID_KEY, user.getId());
		setControllingAdmin(request, user);
	}

	public static Locale getLocale(HttpServletRequest request) {
		return (Locale) getLocale(request.getSession());
	}

	public static Locale getLocale(HttpSession session) {
		return (Locale) session.getAttribute(LOCALE_KEY);
	}

	public static void setLocale(HttpServletRequest request, Locale locale) {
		setLocale(request.getSession(), locale);
	}

	public static void setLocale(HttpSession session, Locale locale) {
		session.setAttribute(LOCALE_KEY, locale);
	}

	public static <EntitySubclass> EntitySubclass getEntityFromIdParameter(HttpServletRequest request, String idRequestParameterName, DaoGetter<EntitySubclass> daoGetter) {
		return getEntityFromIdParameter(request, idRequestParameterName, daoGetter.getDao(getDaoFactory(request)));
	}

	public static <EntitySubclass> EntitySubclass getEntityFromIdParameter(HttpServletRequest request, String idRequestParameterName, Dao<EntitySubclass> dao) {
		String idString = request.getParameter(idRequestParameterName);
		if (idString == null) {
			return null;
		}
		try {
			return dao.getByPk(Long.parseLong(idString));
		} catch (Exception e) {
		}
		try {
			return dao.getByPk(idString);
		} catch (Exception e) {
		}
		try {
			return dao.getByPk(Integer.parseInt(idString));
		} catch (Exception e) {
		}
		return null;
	}

	public void forwardToView(HttpServletRequest request, HttpServletResponse response, String viewPath) throws ServletException, IOException {
		this.getServletContext().getRequestDispatcher(viewPath).forward(request, response);
	}

	public void includeView(HttpServletRequest request, HttpServletResponse response, String viewPath) throws ServletException, IOException {
		this.getServletContext().getRequestDispatcher(viewPath).include(request, response);
	}

	public static void sendTextToClients(HttpServletRequest request, String text, ImprovedEndpointFilter filter) {
		sendTextToClients(getSessionManager(request), text, filter);
	}

	public void sendTextToClients(String text, ImprovedEndpointFilter filter) {
		sendTextToClients(getSessionManager(), text, filter);
	}

	public static void sendTextToClients(SessionManager sessionManager, String text, ImprovedEndpointFilter filter) {
		List<ImprovedEndpoint> endpoints = sessionManager.getImprovedEndpoints();
		for (ImprovedEndpoint endpoint : endpoints) {
			try {
				if (filter.accept(endpoint)) {
					endpoint.sendText(text);
				}
			} catch (Exception e) {
			}
		}
	}
	
}
