package com.minquoad.servlet.account;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.minquoad.entity.User;
import com.minquoad.frontComponent.form.user.UpSigningForm;
import com.minquoad.tool.http.ImprovedHttpServlet;

@WebServlet("/UpSigning")
public class UpSigning extends ImprovedHttpServlet {

	public static final String viewPath = "/WEB-INF/page/account/upSigning.jsp";

	@Override
	public boolean isAccessible(HttpServletRequest request) {
		return getUser(request) == null;
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		forwardToView(request, response, viewPath);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		UpSigningForm form = new UpSigningForm(request);

		if (form.isValide()) {
			User user = getUnitFactory(request).getUserUnit().createNewUser(
					form.getFieldValueAsString(UpSigningForm.mailAddressKey),
					form.getFieldValueAsString(UpSigningForm.nicknameKey),
					form.getFieldValueAsString(UpSigningForm.passwordKey));

			setSessionUser(request, user);
			response.sendRedirect(request.getContextPath() + "/");

		} else {
			request.setAttribute("form", form);
			forwardToView(request, response, viewPath);
		}
	}

	@Override
	public void forwardToView(HttpServletRequest request, HttpServletResponse response, String viewPath) throws ServletException, IOException {
		request.setAttribute("nicknameMaxlength", User.nicknameMaxlength);
		request.setAttribute("mailAddressMaxlength", User.mailAddressMaxlength);
		super.forwardToView(request, response, viewPath);
	}

}
