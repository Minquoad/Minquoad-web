package com.minquoad.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.minquoad.dao.interfaces.UserDao;
import com.minquoad.entity.User;
import com.minquoad.tool.http.ImprovedHttpServlet;

@WebServlet("/InLoging")
public class InLoging extends ImprovedHttpServlet {

	@Override
	public boolean isAccessible(HttpServletRequest request) {
		return true;
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		if (getUser(request) != null) {

			String lastRefusedUrl = (String) request.getSession().getAttribute(ImprovedHttpServlet.lastRefusedUrlKey);
			if (lastRefusedUrl != null) {
				response.sendRedirect(lastRefusedUrl);
			} else {
				response.sendRedirect(request.getContextPath() + "/");
			}
		} else {
			this.getServletContext().getRequestDispatcher("/WEB-INF/page/inLoging.jsp").forward(request, response);
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

			UserDao userDao = getDaoFactory(request).getUserDao();

			List<User> users = userDao.getAllMatching(nickname, "nickname");

			if (users.size() == 1 && users.get(0).isPassword(password)) {
				setSessionUser(request, users.get(0));
			} else {
				formProblems.add("Login or password is incorrect.");
				request.setAttribute("prefilledNickname", nickname);
			}
			doGet(request, response);
		}
	}

}
