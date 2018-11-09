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

	private DaoFactory daoFactory;

	public DaoFactory getDaoFactory() {
		return daoFactory;
	}

	public boolean servesOnlyLoggedUsers() {
		return false;
	}

	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		daoFactory = new DaoFactoryImpl();

		req.setCharacterEncoding("UTF-8");
		resp.setCharacterEncoding("UTF-8");

		User sessionUser = this.getSessionUser(req);
		if (sessionUser != null) {
			sessionUser.setLastActivityDate(new Date());
			getDaoFactory().getUserDao().update(sessionUser);
			req.setAttribute("sessionUser", getSessionUser(req));
		} else {
			if (this.servesOnlyLoggedUsers()) {
				req.getSession().setAttribute(lastRefusedLoggedUserRequestUriKey, req.getRequestURI());
				resp.sendRedirect(req.getContextPath() + "/LogIn");
				return;
			} else {
				if (!req.getServletPath().equals("/LogIn")) {
					req.getSession().removeAttribute(lastRefusedLoggedUserRequestUriKey);
				}
			}
		}

		super.service(req, resp);
	}

	public User getSessionUser(HttpServletRequest req) {
		Integer sessionUserId = (Integer) req.getSession().getAttribute(sessionUserIdKey);
		if (sessionUserId == null) {
			return null;
		}
		return getDaoFactory().getUserDao().getById((Integer) req.getSession().getAttribute(sessionUserIdKey));
	}

	public void setSessionUser(HttpServletRequest req, User user) {
		req.getSession().setAttribute(sessionUserIdKey, user.getId());
	}

}
