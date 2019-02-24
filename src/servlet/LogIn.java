package servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.interfaces.UserDao;
import entity.User;

@WebServlet("/LogIn")
public class LogIn extends ImprovedHttpServlet {

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		if (this.getSessionUser(request) != null) {

			String lastRefusedLoggedUserServletPath = (String) request.getSession().getAttribute(ImprovedHttpServlet.lastRefusedLoggedUserRequestUriKey);
			if (lastRefusedLoggedUserServletPath != null) {
				response.sendRedirect(lastRefusedLoggedUserServletPath);
			} else {
				response.sendRedirect(request.getContextPath() + "/");
			}
		} else {
			this.getServletContext().getRequestDispatcher("/WEB-INF/page/logIn.jsp").forward(request, response);
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
			} else {
				formProblems.add("Login or password is incorrect.");
				request.setAttribute("prefilledNickname", nickname);
			}
			doGet(request, response);
		}
	}

}
