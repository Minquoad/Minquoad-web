package com.minquoad.servlet.administration;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.minquoad.entity.User;
import com.minquoad.tool.http.ImprovedHttpServlet;

@WebServlet("/Administration")
public class Administration extends ImprovedHttpServlet {

	public static final String VIEW_PATH = "/WEB-INF/page/administration/administration.jsp";

	@Override
	public boolean isAccessible(HttpServletRequest request) {
		User user = getUser(request);
		return user != null && user.isAdmin();
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		request.setAttribute("users", getDaoFactory(request).getUserDao().getAll());
		request.setAttribute("improvementSuggestions", getDaoFactory(request).getImprovementSuggestionDao().getAll());

		forwardToView(request, response, VIEW_PATH);
	}

}
