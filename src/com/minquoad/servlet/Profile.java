package com.minquoad.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.minquoad.dao.interfaces.DaoFactory;
import com.minquoad.entity.User;
import com.minquoad.tool.http.ImprovedHttpServlet;

@WebServlet("/Profile")
public class Profile extends ImprovedHttpServlet {

	@Override
	public boolean isAccessible(HttpServletRequest request) {
		return getUser(request) != null;
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		User user = getEntityFromIdParameter(request, "userId", DaoFactory::getUserDao);

		request.setAttribute("showedUser", user);

		if (user != null) {
			this.getServletContext().getRequestDispatcher("/WEB-INF/page/profile.jsp").forward(request,
					response);
		} else {
			response.sendRedirect(request.getContextPath() + "/");
		}

	}

}
