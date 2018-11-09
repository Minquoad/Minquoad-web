package servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import Entities.User;
import daos.interfaces.UserDao;

public class LogIn extends ImprovedHttpServlet {

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		if (this.getSessionUser(request) != null) {
			response.sendRedirect(request.getContextPath() + "/");
		} else {
			this.getServletContext().getRequestDispatcher("/WEB-INF/LogIn.jsp").forward(request, response);
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String nickname = request.getParameter("nickname");
		String password = request.getParameter("password");

		if (nickname != null && password != null) {
			List<String> formProblems = new ArrayList<String>();
			request.setAttribute("formProblems", formProblems);

			nickname = User.formatNickanameCase(nickname);

			UserDao userDao = getDaoFactory().getUserDao();

			List<User> users = userDao.getAllMatching(nickname, "nickname");

			if (users.size() == 1 && users.get(0).isPassword(password)) {
				this.setSessionUser(request, users.get(0));
				
				String lastRefusedLoggedUserServletPath = (String) request.getSession().getAttribute(ImprovedHttpServlet.lastRefusedLoggedUserRequestUriKey);
				if (lastRefusedLoggedUserServletPath != null) {
					request.getSession().removeAttribute(lastRefusedLoggedUserRequestUriKey);
					response.sendRedirect(lastRefusedLoggedUserServletPath);
				} else {
					doGet(request, response);
				}
			} else {
				formProblems.add("Login or password is incorrect.");
				request.setAttribute("prefilledNickname", nickname);
				
				doGet(request, response);
			}
		}
	}

}
