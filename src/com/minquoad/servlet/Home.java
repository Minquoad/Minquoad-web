package com.minquoad.servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.minquoad.dao.interfaces.DaoFactory;
import com.minquoad.entity.file.ProtectedFile;
import com.minquoad.tool.http.ImprovedHttpServlet;

@WebServlet({"/Home", "/"})
public class Home extends ImprovedHttpServlet {

	public static final String viewPath = "/WEB-INF/page/home.jsp";

	@Override
	public boolean isAccessible(HttpServletRequest request) {
		return true;
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		DaoFactory daoFactory = getDaoFactory(request);
		List<ProtectedFile> all = daoFactory.getProtectedFileDao().getAll();

		forwardToView(request, response, viewPath);
	}

}
