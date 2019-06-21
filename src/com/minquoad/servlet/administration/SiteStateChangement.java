package com.minquoad.servlet.administration;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.minquoad.tool.http.ImprovedHttpServlet;

@WebServlet("/SiteStateChangement")
public class SiteStateChangement extends ImprovedHttpServlet {

	public static final String OPEN_KEY = "open";

	@Override
	public boolean isAccessible(HttpServletRequest request) {
		return getUser(request) != null && getUser(request).isAdmin();
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		getDeployment().setOpen("true".equals(request.getParameter(OPEN_KEY)));

		response.sendRedirect(request.getContextPath() + "/Administration?" + Administration.ADMINISTRATION_SUB_PAGE_KEY_NAME + "=SiteManagement");
	}
	
}
