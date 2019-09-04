package com.minquoad.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.minquoad.tool.http.ImprovedHttpServlet;

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
		/*
		Database db = getService(Database.class);

		DaoFactory daoFactory = getDaoFactory(request);

		daoFactory.getThingDao().getByPk(null);
		daoFactory.getUserDao().getByPk(null);
		daoFactory.getMessageDao().getByPk(null);
		daoFactory.getConversationAccessDao().getByPk(null);
		daoFactory.getConversationDao().getByPk(null);
		daoFactory.getFailedInLoginigAttemptDao().getByPk(null);
		daoFactory.getProtectedFileDao().getByPk(null);
		daoFactory.getUserProfileImageDao().getByPk(null);
		daoFactory.getRequestLogDao().getByPk(20526l);
		daoFactory.getImprovementSuggestionDao().getByPk(null);
		daoFactory.getMessageFileDao().getByPk(null);
		daoFactory.getConsiderationDao().getByPk(null);
		*/
	}

}
