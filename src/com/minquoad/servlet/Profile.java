package com.minquoad.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.minquoad.dao.interfaces.DaoFactory;
import com.minquoad.dao.interfaces.UserProfileImageDao;
import com.minquoad.entity.User;
import com.minquoad.entity.file.UserProfileImage;
import com.minquoad.tool.http.ImprovedHttpServlet;

@WebServlet("/Profile")
public class Profile extends ImprovedHttpServlet {

	public static final String VIEW_PATH = "/WEB-INF/page/profile.jsp";

	@Override
	public boolean isAccessible(HttpServletRequest request) {
		return getUser(request) != null;
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		User user = getEntityFromIdParameter(request, "userId", DaoFactory::getUserDao);

		if (user != null) {

			UserProfileImageDao userProfileImageDao = getDaoFactory(request).getUserProfileImageDao();
			UserProfileImage image = userProfileImageDao.getUserUserProfileImageDao(user);
			request.setAttribute("userProfileImage", image);

			request.setAttribute("showedUser", user);
			forwardToView(request, response, VIEW_PATH);
		} else {
			response.sendRedirect(request.getContextPath() + "/");
		}

	}

}
