package com.minquoad.tool.http;

import java.io.IOException;
import java.time.Instant;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.minquoad.dao.interfaces.Dao;
import com.minquoad.dao.interfaces.DaoFactory;
import com.minquoad.dao.sqlImpl.DaoFactoryImpl;
import com.minquoad.entity.User;
import com.minquoad.entity.unit.UnitFactory;
import com.minquoad.framework.dao.Entity;

public abstract class ImprovedHttpServlet extends HttpServlet {

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

		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");

		User user = getDaoFactory(request).getUserDao().getById((Integer) request.getSession().getAttribute(userIdKey));

		if (user != null) {

			setUser(request, user);

			User controllingAdmin = getDaoFactory(request).getUserDao().getById((Integer) request.getSession().getAttribute(controllingAdminIdKey));

			if (controllingAdmin == null) {
				user.setLastActivityInstant(Instant.now());
				getDaoFactory(request).getUserDao().persist(user);

				if (user.isBlocked() && !request.getServletPath().equals("/BlockedAccount")) {
					response.sendRedirect(request.getContextPath() + "/BlockedAccount");
					return;
				}

			} else {
				setControllingAdmin(request, controllingAdmin);

				controllingAdmin.setLastActivityInstant(Instant.now());
				getDaoFactory(request).getUserDao().persist(user);

			}
		}

		if (!this.isAccessible(request)) {

			if (user == null) {
				if (isFullPage()) {
					String lastRefusedUrl = request.getRequestURI();
					String queryString = request.getQueryString();
					if (queryString != null) {
						lastRefusedUrl += "?" + queryString;
					}
					request.getSession().setAttribute(lastRefusedUrlKey, lastRefusedUrl);
					response.sendRedirect(request.getContextPath() + "/InLoging");
					return;
				} else {
					response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
					return;
				}
			} else {
				response.setStatus(HttpServletResponse.SC_FORBIDDEN);
				return;
			}

		}

		if (!request.getServletPath().equals("/InLoging")) {
			request.getSession().removeAttribute(lastRefusedUrlKey);
		}

		super.service(request, response);
	}

	public boolean isFullPage() {
		return true;
	}

	public static DaoFactory getDaoFactory(HttpServletRequest request) {
		if (request.getAttribute(daoFactoryKey) == null) {
			request.setAttribute(daoFactoryKey, ImprovedHttpServlet.instantiateDaoFactory());
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

	public static <EntitySubclass extends Entity> EntitySubclass getEntityFromIdParameter(HttpServletRequest request, String idRequestParameterName, DaoGetter<EntitySubclass> daoGetter) {
		return getEntityFromIdParameter(request, idRequestParameterName, daoGetter.getDao(getDaoFactory(request)));
	}

	public static <EntitySubclass extends Entity> EntitySubclass getEntityFromIdParameter(HttpServletRequest request, String idRequestParameterName, Dao<EntitySubclass> dao) {
		String idString = request.getParameter(idRequestParameterName);
		if (idString != null) {
			try {
				int id = Integer.parseInt(idString);
				return dao.getById(id);

			} catch (NumberFormatException e) {
			}
		}
		return null;
	}

	public static DaoFactory instantiateDaoFactory() {
		return new DaoFactoryImpl();
	}

	protected interface DaoGetter<EntitySubclass extends Entity> {
		public Dao<EntitySubclass> getDao(DaoFactory daoFactory);
	}

}
