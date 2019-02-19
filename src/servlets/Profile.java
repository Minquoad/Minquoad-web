package servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import Entities.User;

@WebServlet("/Profile")
public class Profile extends ImprovedHttpServlet {

	@Override
	public boolean isAccessibleForUser(User user) {
		return user != null;
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		try {
			String userIdString = request.getParameter("userId");

			int userId = Integer.parseInt(userIdString);
			User user = this.getDaoFactory().getUserDao().getById(userId);

			request.setAttribute("user", user);

			this.getServletContext().getRequestDispatcher("/WEB-INF/page/profile.jsp").forward(request,
					response);

		} catch (Exception e) {
			response.sendRedirect(request.getContextPath() + "/");
		}
	}

}
