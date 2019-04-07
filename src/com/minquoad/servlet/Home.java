package com.minquoad.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.minquoad.service.Database;
import com.minquoad.service.Deployment;
import com.minquoad.tool.http.ImprovedHttpServlet;

@WebServlet({ "/Home", "/" })
public class Home extends ImprovedHttpServlet {

	public static final String viewPath = "/WEB-INF/page/home.jsp";

	@Override
	public boolean isAccessible(HttpServletRequest request) {
		return true;
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {


		Database database = (Database) request.getServletContext().getAttribute(Deployment.databaseKey);
		
		database.close();
		
		forwardToView(request, response, viewPath);
	}

}
