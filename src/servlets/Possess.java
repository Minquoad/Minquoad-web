package servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import Entities.User;

@WebServlet("/Possess")
public class Possess extends ImprovedHttpServlet {

	@Override
	public boolean isAccessibleForUser(User user) {
		return user != null && super.isAccessibleForUser(user) && user.isAdmin();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		try {
			String userIdString = request.getParameter("userId");

			int userId = Integer.parseInt(userIdString);
			User user = this.getDaoFactory().getUserDao().getById(userId);

			if (user != null && this.getSessionUser(request).canAdminister(user)) {
				if (this.getSessionAdminister(request) == null) {
					request.getSession().setAttribute(
							sessionAdministerIdKey,
							this.getSessionUser(request).getId());
				}
				this.setSessionUser(request, user);
			}

		} catch (Exception e) {
		}

		response.sendRedirect(request.getContextPath() + "/");
	}

}
