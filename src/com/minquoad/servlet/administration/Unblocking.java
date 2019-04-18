package com.minquoad.servlet.administration;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.minquoad.dao.interfaces.DaoFactory;
import com.minquoad.entity.User;
import com.minquoad.tool.http.ImprovedHttpServlet;

@WebServlet("/Unblocking")
public class Unblocking extends ImprovedHttpServlet {

	public static final String TARGET_ID_KEY = "targetId";

	@Override
	public boolean isAccessible(HttpServletRequest request) {
		User target = getEntityFromIdParameter(request, TARGET_ID_KEY, DaoFactory::getUserDao);
		return getUser(request) != null && target != null && getUser(request).canAdminister(target);
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		User target = getEntityFromIdParameter(request, TARGET_ID_KEY, DaoFactory::getUserDao);

		target.setUnblockInstant(null);

		getDaoFactory(request).getUserDao().persist(target);

		response.sendRedirect(request.getContextPath() + "/Administration");
	}

}
