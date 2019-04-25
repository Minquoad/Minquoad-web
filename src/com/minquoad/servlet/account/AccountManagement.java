package com.minquoad.servlet.account;

import java.io.IOException;
import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.minquoad.dao.interfaces.UserProfileImageDao;
import com.minquoad.entity.User;
import com.minquoad.entity.file.UserProfileImage;
import com.minquoad.frontComponent.form.field.FormFileField;
import com.minquoad.frontComponent.form.impl.account.UserParametersAlterationForm;
import com.minquoad.frontComponent.form.impl.account.UserPasswordAlterationForm;
import com.minquoad.frontComponent.form.impl.account.UserPictureAlterationForm;
import com.minquoad.tool.http.ImprovedHttpServlet;
import com.minquoad.tool.http.PartTool;

@WebServlet("/AccountManagement")
@MultipartConfig(fileSizeThreshold = 1024 * 1024 * 1, // 1 MB
		maxFileSize = 1024 * 1024 * 10, // 10 MB
		maxRequestSize = 1024 * 1024 * 15 // 15 MB
)
public class AccountManagement extends ImprovedHttpServlet {

	private static final String USER_PASSWORD_ALTERATION = "userPasswordAlteration";
	private static final String USER_PICTURE_ALTERATION = "userPictureAlteration";
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

				UserParametersAlterationForm form = (UserParametersAlterationForm) request.getAttribute(USER_PARAMETERS_ALTERATION);
				form.submit();

				if (form.isValide()) {
					User user = getUser(request);
					user.setNickname(form.getnickname());
					user.setMailAddress(form.getmailAddress());
					user.setDefaultColor(form.getDefaultColor());
					user.setLanguage(form.getLanguage());
					getDaoFactory(request).getUserDao().persist(user);

					setLocale(request, new Locale(user.getLanguage(), getLocale(request).getCountry()));
				}
			}

			if (formId.equals(USER_PICTURE_ALTERATION)) {

				UserPictureAlterationForm form = (UserPictureAlterationForm) request.getAttribute(USER_PICTURE_ALTERATION);
				form.submit();

				if (form.isValide()) {

					UserProfileImageDao userProfileImageDao = getDaoFactory(request).getUserProfileImageDao();

					UserProfileImage image = userProfileImageDao.getUserUserProfileImageDao(getUser(request));
					if (image != null) {
						image.getFile(getDeployment()).delete();
						userProfileImageDao.delete(image);
					}

					FormFileField field = (FormFileField) form.getField(UserPictureAlterationForm.USER_PICTURE_KEY);
					if (field.hasFile()) {

						image = new UserProfileImage();
						image.setOriginalName(field.getOriginalFileName());
						image.setUser(getUser(request));

						PartTool.saveInProtectedFile(field.getValue(), image, getStorageManager());

						userProfileImageDao.persist(image);
					}
				} else {
					request.setAttribute("userPictureAlterationForm", form);
				}
			}

			if (formId.equals(USER_PASSWORD_ALTERATION)) {

				UserPasswordAlterationForm form = (UserPasswordAlterationForm) request.getAttribute(USER_PASSWORD_ALTERATION);
				form.submit();

				if (form.isValide()) {
					User user = getUser(request);
					user.setPassword(form.getNewPassword(), getDeployment());
					getDaoFactory(request).getUserDao().persist(user);
				} else {
					request.setAttribute("userPasswordAlterationForm", form);
				}
			}

			forwardToView(request, response, VIEW_PATH);
		}
	}

	private void initForms(HttpServletRequest request) {
		request.setAttribute(USER_PARAMETERS_ALTERATION, new UserParametersAlterationForm(request));
		request.setAttribute(USER_PICTURE_ALTERATION, new UserPictureAlterationForm(request));
		request.setAttribute(USER_PASSWORD_ALTERATION, new UserPasswordAlterationForm(request));
	}

	@Override
	public void forwardToView(HttpServletRequest request, HttpServletResponse response, String viewPath) throws ServletException, IOException {
		request.setAttribute("nicknameMaxlength", User.NICKNAME_MAX_LENGTH);
		request.setAttribute("mailAddressMaxlength", User.MAIL_ADDRESS_MAX_LENGTH);

		UserProfileImageDao userProfileImageDao = getDaoFactory(request).getUserProfileImageDao();
		UserProfileImage image = userProfileImageDao.getUserUserProfileImageDao(getUser(request));
		request.setAttribute("userProfileImage", image);

		super.forwardToView(request, response, viewPath);
	}

}
