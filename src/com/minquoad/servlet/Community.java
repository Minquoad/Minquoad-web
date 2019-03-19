package com.minquoad.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.minquoad.tool.http.ImprovedHttpServlet;

@WebServlet("/Community")
public class Community extends ImprovedHttpServlet {

	public static final String viewPath = "/WEB-INF/page/community.jsp";

	@Override
	public boolean isAccessible(HttpServletRequest request) {
		return true;
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setAttribute("users", getDaoFactory(request).getUserDao().getAll());
		
		forwardToView(request, response, viewPath);
	}

}
