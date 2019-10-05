package com.minquoad.servlet.profile;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.minquoad.dao.interfaces.DaoFactory;
import com.minquoad.entity.User;
import com.minquoad.tool.ImprovedHttpServlet;

@WebServlet("/Profile")
public class Profile extends ImprovedHttpServlet {

	public static final String TARGET_USER_ID_KEY = "targetUserId";

	public static final String VIEW_PATH = "/WEB-INF/page/profile/profile.jsp";

	@Override
	public boolean isAccessible(HttpServletRequest request) {
		return getUser(request) != null;
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		User user = getEntityFromPkParameter(request, TARGET_USER_ID_KEY, DaoFactory::getUserDao);

		if (user != null) {
			request.setAttribute("showedUser", user);
			forwardToView(request, response, VIEW_PATH);

		} else {
			response.sendRedirect(request.getContextPath() + "/");
		}

	}

}
