package com.minquoad.servlet.account;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.minquoad.dao.interfaces.FailedInLoginigAttemptDao;
import com.minquoad.entity.FailedInLoginigAttempt;
import com.minquoad.entity.User;
import com.minquoad.frontComponent.form.impl.account.InLogingForm;
import com.minquoad.tool.http.ImprovedHttpServlet;
import com.minquoad.unit.FailedInLoginigAttemptUnit;

@WebServlet("/InLoging")
public class InLoging extends ImprovedHttpServlet {

	public static final String viewPath = "/WEB-INF/page/account/inLoging.jsp";

	@Override
	public boolean isAccessible(HttpServletRequest request) {
		return getUser(request) == null;
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		initForms(request);

		forwardToView(request, response, viewPath);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		initForms(request);
		InLogingForm form = (InLogingForm) request.getAttribute("form");
		form.submit();

		if (form.isValide()) {

			User user = form.getInLoggingUser();

			setSessionUser(request, user);

			FailedInLoginigAttemptDao failedInLoginigAttemptDao = getDaoFactory(request).getFailedInLoginigAttemptDao();
			FailedInLoginigAttempt failedInLoginigAttempt = failedInLoginigAttemptDao.getOneMatching("mailAddress", user.getMailAddress());
			if (failedInLoginigAttempt != null) {
				failedInLoginigAttemptDao.delete(failedInLoginigAttempt);
			}
			
		} else {
			FailedInLoginigAttemptUnit failedInLoginigAttemptUnit = getUnitFactory(request).getFailedInLoginigAttemptUnit();

			failedInLoginigAttemptUnit.registerFailedInLoginigAttempt(form.getMailAddress());
		}

		if (getUser(request) == null) {
			forwardToView(request, response, viewPath);
		} else {
			String lastRefusedUrl = (String) request.getSession().getAttribute(ImprovedHttpServlet.lastRefusedUrlKey);
			if (lastRefusedUrl != null) {
				response.sendRedirect(lastRefusedUrl);
			} else {
				response.sendRedirect(request.getContextPath() + "/");
			}
		}
	}

	private void initForms(HttpServletRequest request) {
		request.setAttribute("form", new InLogingForm(request));
	}

}
