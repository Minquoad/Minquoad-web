package com.minquoad.servlet.profile;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.minquoad.frontComponent.form.Form;
import com.minquoad.frontComponent.form.impl.profile.ProfileEditionForm;
import com.minquoad.tool.http.ImprovedHttpServlet;

@WebServlet("/ProfileEdition")
@MultipartConfig(fileSizeThreshold = 1024 * 1024 * 1, // 1 MB
		maxFileSize = 1024 * 1024 * 10, // 10 MB
		maxRequestSize = 1024 * 1024 * 15 // 15 MB
)
public class ProfileEdition extends ImprovedHttpServlet {

	public static final String VIEW_PATH = "/WEB-INF/page/account/accountManagement.jsp";

	@Override
	public boolean isAccessible(HttpServletRequest request) {
		return getUser(request) != null;
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		initForms(request);

		forwardToView(request, response, VIEW_PATH);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		initForms(request);

		forwardToView(request, response, VIEW_PATH);
	}

	private void initForms(HttpServletRequest request) {
		request.setAttribute("form", new ProfileEditionForm(request));
	}

	public static <FormClass extends Form> FormClass getForm(HttpServletRequest request, FormConstructor<FormClass> formConstructor) {
		@SuppressWarnings("unchecked")
		FormClass form = (FormClass) request.getAttribute("form");
		if (form == null) {
			form = formConstructor.instantiate(request);
			request.setAttribute("form", form);
		}
		return form;
	}

	public interface FormConstructor<FormClass extends Form> {
		public FormClass instantiate(HttpServletRequest request);
	}
	
}
