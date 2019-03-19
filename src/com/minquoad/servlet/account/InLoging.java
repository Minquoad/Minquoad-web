package com.minquoad.servlet.account;

import java.io.IOException;
import java.time.Duration;
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
import com.minquoad.entity.unit.FailedInLoginigAttemptUnit;
import com.minquoad.tool.http.ImprovedHttpServlet;

@WebServlet("/InLoging")
public class InLoging extends ImprovedHttpServlet {

	public static final String viewPath = "/WEB-INF/page/account/inLoging.jsp";

	@Override
	public boolean isAccessible(HttpServletRequest request) {
		return true;
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		forwardToView(request, response, viewPath);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String mailAddress = request.getParameter("mailAddress");
		String password = request.getParameter("password");

		if (mailAddress != null && password != null) {

			FailedInLoginigAttemptUnit failedInLoginigAttemptUnit = getUnitFactory(request).getFailedInLoginigAttemptUnit();

			List<String> formProblems = new ArrayList<String>();
			request.setAttribute("formProblems", formProblems);

			mailAddress = User.formatMailAddressCase(mailAddress);

			Duration coolDown = failedInLoginigAttemptUnit.getCoolDown(mailAddress);

			if (coolDown == null) {

				UserDao userDao = getDaoFactory(request).getUserDao();

				User user = userDao.getOneMatching(mailAddress, "mailAddress");

				if (user != null && user.isPassword(password)) {
					setSessionUser(request, user);

					FailedInLoginigAttemptDao failedInLoginigAttemptDao = getDaoFactory(request).getFailedInLoginigAttemptDao();
					FailedInLoginigAttempt failedInLoginigAttempt = failedInLoginigAttemptDao.getOneMatching(mailAddress, "mailAddress");
					failedInLoginigAttemptDao.delete(failedInLoginigAttempt);

				} else {
					formProblems.add("Mail address or password is incorrect.");
					failedInLoginigAttemptUnit.registerFailedInLoginigAttempt(mailAddress);
				}
			}

			Duration newCoolDown = failedInLoginigAttemptUnit.getCoolDown(mailAddress);

			if (newCoolDown != null) {
				formProblems.add("Too manny failed connection trials have been done with the mail address " + mailAddress
						+ ". This mail address will not be usable for " + newCoolDown.getSeconds()/60 + " minutes and " + newCoolDown.getSeconds()%60 + " seconds.");
			}

			if (formProblems.size() != 0) {
				request.setAttribute("prefilledMailAddress", mailAddress);
			}

			forwardToView(request, response, viewPath);
			return;
		}

		response.setStatus(422);
	}

	@Override
	public void forwardToView(HttpServletRequest request, HttpServletResponse response, String viewPath) throws ServletException, IOException {
		if (getUser(request) != null) {

			String lastRefusedUrl = (String) request.getSession().getAttribute(ImprovedHttpServlet.lastRefusedUrlKey);
			if (lastRefusedUrl != null) {
				response.sendRedirect(lastRefusedUrl);
			} else {
				response.sendRedirect(request.getContextPath() + "/");
			}
		} else {
			super.forwardToView(request, response, viewPath);
		}
	}
}
