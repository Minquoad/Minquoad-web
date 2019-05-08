package com.minquoad.servlet.administration;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.minquoad.entity.User;
import com.minquoad.tool.http.ImprovedHttpServlet;

@WebServlet("/ImprovementSuggestionsConsulting")
public class ImprovementSuggestionsConsulting extends ImprovedHttpServlet {

	public static final String VIEW_PATH = "/WEB-INF/page/administration/improvementSuggestionsConsulting.jsp";

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

		request.setAttribute("improvementSuggestions", getDaoFactory(request).getImprovementSuggestionDao().getAll());

		forwardToView(request, response, VIEW_PATH);
	}

}
