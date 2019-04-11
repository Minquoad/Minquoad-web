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
		initForms(request);
		BlockingForm form = (BlockingForm) request.getAttribute("form");
		form.submit();

		User target = form.getTarget();
		User user = getUser(request);
		return user != null && target != null && user.canAdminister(target);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		BlockingForm form = (BlockingForm) request.getAttribute("form");

		if (form.isValide()) {
			User target = form.getTarget();
			target.setUnblockInstant(form.getUnblockDate());
			getDaoFactory(request).getUserDao().persist(target);
		}

		response.sendRedirect(request.getContextPath() + "/Administration");
	}
	
	private void initForms(HttpServletRequest request) {
		request.setAttribute("form", new BlockingForm(request));
	}

}
