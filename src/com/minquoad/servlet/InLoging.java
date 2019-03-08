package com.minquoad.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.minquoad.dao.interfaces.FailedInLoginigAttemptDao;
import com.minquoad.dao.interfaces.UserDao;
import com.minquoad.entity.FailedInLoginigAttempt;
import com.minquoad.entity.User;
import com.minquoad.tool.http.ImprovedHttpServlet;

@WebServlet("/InLoging")
public class InLoging extends ImprovedHttpServlet {

	@Override
	public boolean isAccessible(HttpServletRequest request) {
		return true;
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		if (getUser(request) != null) {

			String lastRefusedUrl = (String) request.getSession().getAttribute(ImprovedHttpServlet.lastRefusedUrlKey);
			if (lastRefusedUrl != null) {
				response.sendRedirect(lastRefusedUrl);
			} else {
				response.sendRedirect(request.getContextPath() + "/");
			}
		} else {
			this.getServletContext().getRequestDispatcher("/WEB-INF/page/inLoging.jsp").forward(request, response);
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String mailAddress = request.getParameter("mailAddress");
		String password = request.getParameter("password");

		if (mailAddress != null && password != null) {
			List<String> formProblems = new ArrayList<String>();
			request.setAttribute("formProblems", formProblems);

			mailAddress = User.formatMailAddressCase(mailAddress);

			UserDao userDao = getDaoFactory(request).getUserDao();

			List<User> users = userDao.getAllMatching(mailAddress, "mailAddress");

			if (users.size() != 0 && users.get(0).isPassword(password)) {
				setSessionUser(request, users.get(0));
				
				FailedInLoginigAttemptDao failedInLoginigAttemptDao = getDaoFactory(request).getFailedInLoginigAttemptDao();
				FailedInLoginigAttempt failedInLoginigAttempt = failedInLoginigAttemptDao.getFailedInLoginigAttemptByMailAddress(mailAddress);
				failedInLoginigAttemptDao.delete(failedInLoginigAttempt);

			} else {
				formProblems.add("Mail address or password is incorrect.");
				request.setAttribute("prefilledMailAddress", mailAddress);

				getUnitFactory(request).getFailedInLoginigAttemptUnit().registerFailedInLoginigAttempt(mailAddress);
			}
			
			doGet(request, response);
			return;
		}

		response.setStatus(422);
	}

}
