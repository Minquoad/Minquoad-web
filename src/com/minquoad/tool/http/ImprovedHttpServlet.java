package com.minquoad.tool.http;

import java.io.IOException;
import java.time.Instant;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.minquoad.dao.interfaces.Dao;
import com.minquoad.dao.interfaces.DaoFactory;
import com.minquoad.entity.RequestLog;
import com.minquoad.entity.User;
import com.minquoad.service.Database;
import com.minquoad.service.Deployment;
import com.minquoad.service.Logger;
import com.minquoad.unit.UnitFactory;

public abstract class ImprovedHttpServlet extends HttpServlet {

	public static final String METHOD_POST = "POST";

	// request keys
	private final static String daoFactoryKey = "daoFactory";
	private final static String unitFactoryKey = "unitFactory";

	protected final static String userKey = "user";
	protected final static String controllingAdminKey = "controllingAdmin";

	// session keys
	protected final static String userIdKey = "userId";
	protected final static String lastRefusedUrlKey = "lastRefusedUrl";
	protected final static String controllingAdminIdKey = "controllingAdminId";

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

			User user = getDaoFactory(request).getUserDao().getByPk((Long) request.getSession().getAttribute(userIdKey));

			if (user != null) {
				requestLog.setUser(user);
				setUser(request, user);

				User controllingAdmin = getDaoFactory(request).getUserDao().getByPk((Long) request.getSession().getAttribute(controllingAdminIdKey));

				if (controllingAdmin == null) {
					user.setLastActivityInstant(Instant.now());
					getDaoFactory(request).getUserDao().persist(user);

					if (user.isBlocked() && !request.getServletPath().equals("/BlockedAccount") && !request.getServletPath().equals("/OutLoging")) {
						if (isFullPage()) {
							response.sendRedirect(request.getContextPath() + "/BlockedAccount");
						} else {
							response.setStatus(HttpServletResponse.SC_FORBIDDEN);
						}
						return;
					}

				} else {
					if (controllingAdmin.isBlocked() && !request.getServletPath().equals("/Unpossession")) {
						if (isFullPage()) {
							response.sendRedirect(request.getContextPath() + "/Unpossession");
						} else {
							response.setStatus(HttpServletResponse.SC_FORBIDDEN);
						}
						return;
					}

					requestLog.setControllingAdmin(controllingAdmin);
					setControllingAdmin(request, controllingAdmin);

					controllingAdmin.setLastActivityInstant(Instant.now());
					getDaoFactory(request).getUserDao().persist(user);

				}
			}

			if (!this.isAccessible(request)) {

				if (user == null) {
					if (isFullPage()) {
						request.getSession().setAttribute(lastRefusedUrlKey, getCurrentUrlWithArguments(request));
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
				request.getSession().removeAttribute(lastRefusedUrlKey);
			}

			super.service(request, response);

			if (isLoggingAllRequests()) {
				requestLog.setServiceDuration((int) (Instant.now().toEpochMilli() - serviceStartingInstant.toEpochMilli()));
				getDaoFactory(request).getRequestLogDao().persist(requestLog);
			}

		} catch (Exception e) {
			requestLog.setError(Logger.getStackTraceAsString(e));
			requestLog.setServiceDuration((int) (Instant.now().toEpochMilli() - serviceStartingInstant.toEpochMilli()));
			getDaoFactory(request).getRequestLogDao().persist(requestLog);
			throw e;
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
		if (request.getAttribute(daoFactoryKey) == null) {
			Database database = (Database) request.getServletContext().getAttribute(Deployment.databaseKey);
			request.setAttribute(daoFactoryKey, database.getNewDaoFactory());
		}
		return (DaoFactory) request.getAttribute(daoFactoryKey);
	}

	public static UnitFactory getUnitFactory(HttpServletRequest request) {
		if (request.getAttribute(unitFactoryKey) == null) {
			request.setAttribute(unitFactoryKey, new UnitFactory(getDaoFactory(request)));
		}
		return (UnitFactory) request.getAttribute(unitFactoryKey);
	}

	public abstract boolean isAccessible(HttpServletRequest request);

	public static User getUser(HttpServletRequest request) {
		return (User) request.getAttribute(userKey);
	}

	private static void setUser(HttpServletRequest request, User user) {
		request.setAttribute(userKey, user);
	}

	public static void setSessionUser(HttpServletRequest request, User user) {
		request.getSession().setAttribute(userIdKey, user.getId());
		setUser(request, user);
	}

	public static User getControllingAdmin(HttpServletRequest request) {
		return (User) request.getAttribute(controllingAdminKey);
	}

	private static void setControllingAdmin(HttpServletRequest request, User user) {
		request.setAttribute(controllingAdminKey, user);
	}

	public static void setSessionControllingAdmin(HttpServletRequest request, User user) {
		request.getSession().setAttribute(controllingAdminIdKey, user.getId());
		setControllingAdmin(request, user);
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

}
