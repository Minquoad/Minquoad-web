package com.minquoad.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.minquoad.tool.ImprovedHttpServlet;

@WebServlet({ "/Home", "/" })
public class Home extends ImprovedHttpServlet {

	public static final String VIEW_PATH = "/WEB-INF/page/home.jsp";

	@Override
	public boolean isAccessible(HttpServletRequest request) {
		return true;
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		forwardToView(request, response, VIEW_PATH);
	}

}
