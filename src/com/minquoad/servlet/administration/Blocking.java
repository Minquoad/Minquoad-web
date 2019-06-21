package com.minquoad.servlet.administration;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.minquoad.entity.User;
import com.minquoad.frontComponent.form.impl.administration.BlockingForm;
import com.minquoad.tool.http.ImprovedHttpServlet;

@WebServlet("/Blocking")
public class Blocking extends ImprovedHttpServlet {

	@Override
	public boolean isAccessible(HttpServletRequest request) {
		return getUser(request) != null;
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		initForms(request);
		BlockingForm form = (BlockingForm) request.getAttribute("form");
		form.submit();

		if (form.isValide()) {
			User target = form.getTarget();
			target.setUnblockInstant(form.getUnblockDate());
			target.setAdminLevel(0);
			getDaoFactory(request).getUserDao().persist(target);
		}

		response.sendRedirect(request.getContextPath() + "/Administration?" + Administration.ADMINISTRATION_SUB_PAGE_KEY_NAME + "=UsersManagement");
	}

	private void initForms(HttpServletRequest request) {
		request.setAttribute("form", new BlockingForm(request));
	}

}
