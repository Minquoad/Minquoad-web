package com.minquoad.servlet.account;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.minquoad.entity.User;
import com.minquoad.frontComponent.form.account.UpSigningForm;
import com.minquoad.tool.ImprovedHttpServlet;

@WebServlet("/UpSigning")
public class UpSigning extends ImprovedHttpServlet {

	public static final String VIEW_PATH = "/WEB-INF/page/account/upSigning.jsp";

	@Override
	public boolean isAccessible(HttpServletRequest request) {
		return getUser(request) == null;
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		initForms(request);

		forwardToView(request, response, VIEW_PATH);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		initForms(request);
		UpSigningForm form = (UpSigningForm) request.getAttribute(FORM_KEY);
		form.submit();

		if (form.isValide()) {
			User user = getUnitFactory(request).getUserUnit().createNewUser(
					form.getMailAddress(),
					form.getNickname(),
					form.getPassword(),
					getLocale(request).getLanguage());

			setSessionUser(request, user);
			response.sendRedirect(request.getContextPath() + "/");

		} else {
			forwardToView(request, response, VIEW_PATH);
		}
	}

	private void initForms(HttpServletRequest request) {
		request.setAttribute(FORM_KEY, new UpSigningForm(request));
	}

}
