package com.minquoad.servlet.account;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.minquoad.entity.User;
import com.minquoad.tool.http.ImprovedHttpServlet;

@WebServlet("/BlockedAccount")
public class BlockedAccount extends ImprovedHttpServlet {

	public static final String VIEW_PATH = "/WEB-INF/page/account/blockedAccount.jsp";

	@Override
	public boolean isAccessible(HttpServletRequest request) {
		User user = getUser(request);
		return user != null && user.isBlocked();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		forwardToView(request, response, VIEW_PATH);
	}

}
