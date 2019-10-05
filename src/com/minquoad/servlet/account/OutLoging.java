package com.minquoad.servlet.account;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.minquoad.tool.ImprovedHttpServlet;

@WebServlet("/OutLoging")
public class OutLoging extends ImprovedHttpServlet {

	@Override
	public boolean isAccessible(HttpServletRequest request) {
		return getUser(request) != null;
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		if (getControllingAdmin(request) == null) {

			request.getSession().invalidate();
			request.removeAttribute(USER_KEY);
			response.sendRedirect(request.getContextPath() + "/");

		} else {
			response.sendRedirect(request.getContextPath() + "/Unpossession");
		}
	}

}
