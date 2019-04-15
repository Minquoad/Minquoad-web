package com.minquoad.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.minquoad.tool.http.ImprovedHttpServlet;

@WebServlet("/BlockedSite")
public class BlockedSite extends ImprovedHttpServlet {

	public static final String viewPath = "/WEB-INF/page/blockedSite.jsp";

	@Override
	public boolean isAccessible(HttpServletRequest request) {
		return !getDeployment().isOpen();
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		forwardToView(request, response, viewPath);
	}

}
