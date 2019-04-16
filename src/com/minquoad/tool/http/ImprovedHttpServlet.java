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
import com.minquoad.service.StorageManager;
import com.minquoad.unit.UnitFactory;

public abstract class ImprovedHttpServlet extends HttpServlet {

	public static final String POST_METHOD = "POST";

	// request keys
	private final static String DAO_FACTORY_KEY = "daoFactory";
	private final static String UNIT_FACTORY_KEY = "unitFactory";

	protected final static String USER_KEY = "user";
	protected final static String CONTROLLING_ADMIN_KEY = "controllingAdmin";

	// session keys
	protected final static String USER_ID_KEY = "userId";
	protected final static String LAST_REFUSED_URL_KEY = "lastRefusedUrl";
	protected final static String CONTROLLING_ADMIN_ID_KEY = "controllingAdminId";

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

			User user = getDaoFactory(request).getUserDao().getByPk((Long) request.getSession().getAttribute(USER_ID_KEY));

			User controllingAdmin = null;

			if (user != null) {
				requestLog.setUser(user);
				setUser(request, user);

				controllingAdmin = getDaoFactory(request).getUserDao().getByPk((Long) request.getSession().getAttribute(CONTROLLING_ADMIN_ID_KEY));

				if (controllingAdmin == null) {
					user.setLastActivityInstant(Instant.now());
					getDaoFactory(request).getUserDao().persist(user);

				} else {
					requestLog.setControllingAdmin(controllingAdmin);
					setControllingAdmin(request, controllingAdmin);

					controllingAdmin.setLastActivityInstant(Instant.now());
					getDaoFactory(request).getUserDao().persist(user);
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

				if (controllingAdmin != null
						&& !controllingAdmin.isAdmin()
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
						request.getSession().setAttribute(LAST_REFUSED_URL_KEY, getCurrentUrlWithArguments(request));
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
				request.getSession().removeAttribute(LAST_REFUSED_URL_KEY);
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
			Database database = (Database) request.getServletContext().getAttribute(Deployment.DATABASE_KEY);
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
		return (StorageManager) getServletContext().getAttribute(Deployment.STORAGE_MANAGER_KEY);
	}

	public static StorageManager getStorageManager(HttpServletRequest request) {
		return (StorageManager) request.getServletContext().getAttribute(Deployment.STORAGE_MANAGER_KEY);
	}

	public Deployment getDeployment() {
		return (Deployment) getServletContext().getAttribute(Deployment.DEPLOYMENT_KEY);
	}

	public static Deployment getDeployment(HttpServletRequest request) {
		return (Deployment) request.getServletContext().getAttribute(Deployment.DEPLOYMENT_KEY);
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
