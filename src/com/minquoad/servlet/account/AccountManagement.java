package com.minquoad.servlet.account;

import java.io.IOException;
import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.minquoad.entity.User;
import com.minquoad.frontComponent.form.impl.account.UserParametersAlterationForm;
import com.minquoad.frontComponent.form.impl.account.UserPasswordAlterationForm;
import com.minquoad.tool.http.ImprovedHttpServlet;

@WebServlet("/AccountManagement")
@MultipartConfig(fileSizeThreshold = 1024 * 1024 * 1, // 1 MB
		maxFileSize = 1024 * 1024 * 10, // 10 MB
		maxRequestSize = 1024 * 1024 * 15 // 15 MB
)
public class AccountManagement extends ImprovedHttpServlet {

	private static final String USER_PASSWORD_ALTERATION = "userPasswordAlteration";
	private static final String USER_PARAMETERS_ALTERATION = "userParametersAlteration";

	public static final String VIEW_PATH = "/WEB-INF/page/account/accountManagement.jsp";

	@Override
	public boolean isAccessible(HttpServletRequest request) {
		return getUser(request) != null;
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

		String formId = request.getParameter("formId");

		if (formId != null) {

			if (formId.equals(USER_PARAMETERS_ALTERATION)) {

				//TODO rendre l'aide à la saisie optionnel

				UserParametersAlterationForm form = (UserParametersAlterationForm) request.getAttribute(USER_PARAMETERS_ALTERATION);
				form.submit();

				if (form.isValide()) {
					User user = getUser(request);
					user.setNickname(form.getnickname());
					user.setMailAddress(form.getmailAddress());
					user.setDefaultColor(form.getDefaultColor());
					user.setLanguage(form.getLanguage());
					user.setReadabilityImprovementActivated(form.isReadabilityImprovementActivated());
					user.setTypingAssistanceActivated(form.isTypingAssistanceActivated());
					getDaoFactory(request).getUserDao().persist(user);

					setLocale(request, new Locale(user.getLanguage(), getLocale(request).getCountry()));
				}
			}

			if (formId.equals(USER_PASSWORD_ALTERATION)) {

				UserPasswordAlterationForm form = (UserPasswordAlterationForm) request.getAttribute(USER_PASSWORD_ALTERATION);
				form.submit();

				if (form.isValide()) {
					User user = getUser(request);
					user.setPassword(form.getNewPassword(), getDeployment());
					getDaoFactory(request).getUserDao().persist(user);

				}
			}

			forwardToView(request, response, VIEW_PATH);
		}
	}

	private void initForms(HttpServletRequest request) {
		request.setAttribute(USER_PARAMETERS_ALTERATION, new UserParametersAlterationForm(request));
		request.setAttribute(USER_PASSWORD_ALTERATION, new UserPasswordAlterationForm(request));
	}

}
