package com.minquoad.tool.http;

import java.io.IOException;
import java.time.Instant;
import java.util.List;
import java.util.Locale;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.minquoad.dao.interfaces.Dao;
import com.minquoad.dao.interfaces.DaoFactory;
import com.minquoad.dao.interfaces.DaoGetter;
import com.minquoad.entity.RequestLog;
import com.minquoad.entity.User;
import com.minquoad.service.Database;
import com.minquoad.service.Deployment;
import com.minquoad.service.Logger;
import com.minquoad.service.ServicesManager;
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

	protected final static String FORM_KEY = "form";

	// session keys
	public final static String LOCALE_KEY = "locale";
	public final static String USER_ID_KEY = "userId";
	public final static String LAST_REFUSED_URL_KEY = "lastRefusedUrl";
	public final static String CONTROLLING_ADMIN_ID_KEY = "controllingAdminId";

	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		Instant serviceStartingInstant = Instant.now();

		Database database = getService(Database.class);
		DaoFactory daoFactory = database.pickOneDaoFactory();

		RequestLog requestLog = new RequestLog();

		try {
			requestLog.setInstant(serviceStartingInstant);
			requestLog.setServletName(this.getServletName());
			requestLog.setIpAddress(request.getRemoteAddr());

			request.setCharacterEncoding("UTF-8");
			response.setCharacterEncoding("UTF-8");

			requestLog.setUrl(getCurrentUrlWithArguments(request));

			request.setAttribute(DAO_FACTORY_KEY, daoFactory);
			request.setAttribute(UNIT_FACTORY_KEY, new UnitFactory(request.getServletContext(), daoFactory));

			HttpSession session = request.getSession();

			User user = null;
			User controllingAdmin = null;

			Long userId = (Long) session.getAttribute(USER_ID_KEY);

			if (userId != null) {
				user = daoFactory.getUserDao().getByPk(userId);
				requestLog.setUser(user);
				setUser(request, user);

				Long controllingAdminId = (Long) session.getAttribute(CONTROLLING_ADMIN_ID_KEY);

				if (controllingAdminId == null) {
					user.setLastActivityInstant(Instant.now());
					daoFactory.getUserDao().persist(user);

				} else {
					controllingAdmin = daoFactory.getUserDao().getByPk((Long) session.getAttribute(CONTROLLING_ADMIN_ID_KEY));
					requestLog.setControllingAdmin(controllingAdmin);
					setControllingAdmin(request, controllingAdmin);

					controllingAdmin.setLastActivityInstant(Instant.now());
					daoFactory.getUserDao().persist(controllingAdmin);
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

				if (locale == null || !definingLocalUser.getLanguage().equals(locale.getLanguage())) {
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
							&& !request.getServletPath().equals("/OutLoging")
							&& !request.getServletPath().equals("/LanguageChangement")) {

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
				daoFactory.getRequestLogDao().persist(requestLog);
				
				database.giveBack(daoFactory);
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
		return (DaoFactory) request.getAttribute(DAO_FACTORY_KEY);
	}

	public static UnitFactory getUnitFactory(HttpServletRequest request) {
		return (UnitFactory) request.getAttribute(UNIT_FACTORY_KEY);
	}

	public Deployment getDeployment() {
		return getService(Deployment.class);
	}

	public Logger getLogger() {
		return getService(Logger.class);
	}

	public StorageManager getStorageManager() {
		return getService(StorageManager.class);
	}

	public <ServiceClass> ServiceClass getService(Class<ServiceClass> serviceClass) {
		return ServicesManager.getService(getServletContext(), serviceClass);
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

	public static <EntitySubclass> EntitySubclass getEntityFromPkParameter(HttpServletRequest request, String pkRequestParameterName, DaoGetter<EntitySubclass> daoGetter) {
		return getEntityFromPkParameter(request, pkRequestParameterName, daoGetter.getDao(getDaoFactory(request)));
	}

	public static <EntitySubclass> EntitySubclass getEntityFromPkParameter(HttpServletRequest request, String pkRequestParameterName, Dao<EntitySubclass> dao) {
		String idString = request.getParameter(pkRequestParameterName);
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

	public static void sendJsonToClientsWithRole(HttpServletRequest request, JsonNode json, String role, ImprovedEndpointFilter filter) {
		sendJsonToClientsWithRole(request.getServletContext(), json, role, filter);
	}

	public void sendJsonToClientsWithRole(JsonNode json, String role, ImprovedEndpointFilter filter) {
		sendJsonToClientsWithRole(getServletContext(), json, role, filter);
	}

	public static void sendJsonToClientsWithRole(ServletContext context, JsonNode json, String role, ImprovedEndpointFilter filter) {

		ObjectNode eventJsonObject = JsonNodeFactory.instance.objectNode();
		eventJsonObject.put("role", role);
		eventJsonObject.set("data", json);
		String text = eventJsonObject.toString();

		List<ImprovedEndpoint> endpoints = ServicesManager.getService(context, SessionManager.class).getImprovedEndpoints();
		for (ImprovedEndpoint endpoint : endpoints) {
			if (endpoint.hasRole(role)) {
				try {
					if (filter == null || filter.accepts(endpoint)) {
						endpoint.sendText(text);
					}
				} catch (Exception e) {
					e.printStackTrace();
					ServicesManager.getService(context, Logger.class).logError(e);
				}
			}
		}
	}

	public void forwardToView(HttpServletRequest request, HttpServletResponse response, String viewPath) throws ServletException, IOException {
		this.getServletContext().getRequestDispatcher(viewPath).forward(request, response);
	}

	public void includeView(HttpServletRequest request, HttpServletResponse response, String viewPath) throws ServletException, IOException {
		this.getServletContext().getRequestDispatcher(viewPath).include(request, response);
	}

	public static void sendRedirectToReferer(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String referer = request.getHeader(REFERER_HEADER_KEY);
		if (referer == null) {
			response.sendRedirect(request.getContextPath() + "/");
		} else {
			response.sendRedirect(referer);
		}
	}

}
