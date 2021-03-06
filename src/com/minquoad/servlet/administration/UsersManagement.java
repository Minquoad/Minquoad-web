package com.minquoad.servlet.administration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.minquoad.entity.User;
import com.minquoad.frontComponent.RefreshmentConfig;
import com.minquoad.frontComponent.form.administration.BlockingForm;
import com.minquoad.service.SessionManager;
import com.minquoad.tool.ImprovedHttpServlet;
import com.minquoad.websocketEndpoint.ImprovedEndpoint;

@WebServlet("/UsersManagement")
public class UsersManagement extends ImprovedHttpServlet {

	public static final String VIEW_PATH = "/WEB-INF/page/administration/usersManagement.jsp";

	@Override
	public boolean isFullPage() {
		return false;
	}

	@Override
	public boolean isAccessible(HttpServletRequest request) {
		User user = getUser(request);
		return user != null && user.isAdmin();
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		forwardToView(request, response, VIEW_PATH);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		BlockingForm form = new BlockingForm(request);
		form.submit();

		if (form.isValide()) {
			User target = form.getTarget();
			target.setUnblockInstant(form.getUnblockDate());
			target.setAdminLevel(0);
			getDaoFactory(request).getUserDao().persist(target);

			if (target.isBlocked()) {

				Collection<User> targets = new ArrayList<User>(1);
				targets.add(target);

				sendJsonToClientsWithRole(
						new RefreshmentConfig(false, 0).toJson(),
						targets,
						"refreshment");

				for (ImprovedEndpoint improvedEndpoint : getService(SessionManager.class).getImprovedEndpoints()) {
					if (improvedEndpoint.getUserId() == target.getId()) {
						improvedEndpoint.setAvailable(false);
					}
				}
			}
		}

		forwardToView(request, response, VIEW_PATH);
	}

}
