package com.minquoad.servlet.administration;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.minquoad.dao.interfaces.DaoFactory;
import com.minquoad.entity.User;
import com.minquoad.tool.http.ImprovedHttpServlet;

@WebServlet("/Possession")
public class Possession extends ImprovedHttpServlet {

	@Override
	public boolean isAccessible(HttpServletRequest request) {
		User user = getUser(request);
		User userToControl = getEntityFromIdParameter(request, "userId", DaoFactory::getUserDao);

		return user != null && userToControl != null && user.canAdminister(userToControl);
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		if (getControllingAdmin(request) == null) {
			setSessionControllingAdmin(request, getUser(request));
		}
		User userToControl = getEntityFromIdParameter(request, "userId", DaoFactory::getUserDao);
		setSessionUser(request, userToControl);

		response.sendRedirect(request.getContextPath() + "/");
	}

}
