package com.minquoad.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.minquoad.dao.interfaces.MessageDao;
import com.minquoad.dao.interfaces.UserDao;
import com.minquoad.entity.Message;
import com.minquoad.entity.User;
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
		
		
		UserDao userDao = getDaoFactory(request).getUserDao();
		MessageDao messageDao = getDaoFactory(request).getMessageDao();
		
		User u = userDao.getByPk(5611120057846513921l);
		Message m = messageDao.getByPk(1686885004079862984l);
		
		
	}

}
