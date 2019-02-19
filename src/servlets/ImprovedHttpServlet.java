package servlets;

import java.io.IOException;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import Entities.User;
import daos.interfaces.DaoFactory;
import daos.sqlImpls.DaoFactoryImpl;

public abstract class ImprovedHttpServlet extends HttpServlet {

	protected final static String sessionUserIdKey = "sessionUserId";
	public final static String lastRefusedLoggedUserRequestUriKey = "lastRefusedLoggedUserRequestUri";
	public final static String sessionAdministerIdKey = "sessionAdministerId";
	
	private DaoFactory daoFactory = new DaoFactoryImpl();

	public DaoFactory getDaoFactory() {
		return daoFactory;
	}

	private void resetDaoFactory() {
		daoFactory = new DaoFactoryImpl();
	}

	public boolean isAccessibleForUser(User user) {
		return true;
	}

	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		resetDaoFactory();

		req.setCharacterEncoding("UTF-8");

		User sessionUser = this.getSessionUser(req);
		
		if (sessionUser != null) {

			req.setAttribute("sessionUser", sessionUser);

			User sessionAdminister = this.getSessionAdminister(req);

			if (sessionAdminister == null) {
				sessionUser.setLastActivityDate(new Date());
				getDaoFactory().getUserDao().update(sessionUser);
				
				if (sessionUser.isBlocked() && !req.getServletPath().equals("/AccountBlocked")) {
					resp.sendRedirect(req.getContextPath() + "/AccountBlocked");
					return;
				}

			} else {
				sessionAdminister.setLastActivityDate(new Date());
				getDaoFactory().getUserDao().update(sessionUser);

				req.setAttribute("sessionAdminister", sessionAdminister);
			}
		}

		if (!this.isAccessibleForUser(sessionUser)) {
			if (sessionUser == null) {
				req.getSession().setAttribute(lastRefusedLoggedUserRequestUriKey, req.getRequestURI());
				resp.sendRedirect(req.getContextPath() + "/LogIn");
				return;
			} else {
				resp.sendRedirect(req.getContextPath() + "/");
				return;
			}
		}

		if (!req.getServletPath().equals("/LogIn") ) {
			req.getSession().removeAttribute(lastRefusedLoggedUserRequestUriKey);
		}

		super.service(req, resp);
	}

	public User getSessionUser(HttpServletRequest req) {
		Integer sessionUserId = (Integer) req.getSession().getAttribute(sessionUserIdKey);
		if (sessionUserId == null) {
			return null;
		}
		return getDaoFactory().getUserDao().getById(sessionUserId);
	}

	public void setSessionUser(HttpServletRequest req, User user) {
		req.getSession().setAttribute(sessionUserIdKey, user.getId());
	}

	public User getSessionAdminister(HttpServletRequest req) {
		Integer sessionAdministerId = (Integer) req.getSession().getAttribute(sessionAdministerIdKey);
		if (sessionAdministerId == null) {
			return null;
		}
		return getDaoFactory().getUserDao().getById(sessionAdministerId);
	}

}
