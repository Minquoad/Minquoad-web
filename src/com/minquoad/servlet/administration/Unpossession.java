package com.minquoad.servlet.administration;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.minquoad.tool.http.ImprovedHttpServlet;

@WebServlet("/Unpossession")
public class Unpossession extends ImprovedHttpServlet {

	@Override
	public boolean isAccessible(HttpServletRequest request) {
		return getControllingAdmin(request) != null;
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		setSessionUser(request, getControllingAdmin(request));

		request.getSession().removeAttribute(CONTROLLING_ADMIN_ID_KEY);
		request.removeAttribute(CONTROLLING_ADMIN_KEY);

		response.sendRedirect(request.getContextPath() + "/Administration");
	}

}
