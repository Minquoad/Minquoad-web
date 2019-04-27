package com.minquoad.servlet;

import java.io.IOException;
import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.minquoad.tool.InternationalizationTool;
import com.minquoad.tool.http.ImprovedHttpServlet;

@WebServlet("/LanguageChangement")
public class LanguageChangement extends ImprovedHttpServlet {

	public static final String LANGUAGE_KEY = "language";

	@Override
	public boolean isAccessible(HttpServletRequest request) {
		return getUser(request) == null;
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String language = request.getParameter(LANGUAGE_KEY);

		if (InternationalizationTool.isLanguageValid(language)) {
			setLocale(request, new Locale(language, getLocale(request).getCountry()));
		}

		String referer = request.getHeader(REFERER_HEADER_KEY);
		if (referer == null) {
			response.sendRedirect(request.getContextPath() + "/");
		} else {
			response.sendRedirect(referer);
		}
	}

}
