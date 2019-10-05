package com.minquoad.servlet.administration;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.minquoad.entity.User;
import com.minquoad.tool.ImprovedHttpServlet;

@WebServlet("/SiteManagement")
public class SiteManagement extends ImprovedHttpServlet {

	public static final String VIEW_PATH = "/WEB-INF/page/administration/siteManagement.jsp";

	private static final String SITE_STATE_MANAGEMENT = "siteStateManagement";
	private static final String SITE_CLEAR_MANAGEMENT = "siteClearManagement";
	
	public static final String OPEN_KEY = "open";

	@Override
	public boolean isFullPage() {
		return false;
	}

	@Override
	public boolean isAccessible(HttpServletRequest request) {
		User user = getUser(request);
		return user != null && user.isAdmin();
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		forwardToView(request, response, VIEW_PATH);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		String formId = request.getParameter("formId");

		if (formId != null) {
			if (formId.equals(SITE_STATE_MANAGEMENT)) {
				getDeployment().setOpen("true".equals(request.getParameter(OPEN_KEY)));
			}
			if (formId.equals(SITE_CLEAR_MANAGEMENT)) {
				getDeployment().clear();
			}
		}

		forwardToView(request, response, VIEW_PATH);
	}

}
