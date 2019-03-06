package com.minquoad.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.minquoad.tool.http.ImprovedHttpServlet;

@WebServlet("/OutLoging")
public class OutLoging extends ImprovedHttpServlet {

	@Override
	public boolean isAccessible(HttpServletRequest request) {
		return true;
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		if (getControllingAdmin(request) == null) {

			request.getSession().removeAttribute(userIdKey);
			request.removeAttribute(userKey);

			response.sendRedirect(request.getContextPath() + "/");

		} else {
			response.sendRedirect(request.getContextPath() + "/Unpossession");
		}
	}

}
