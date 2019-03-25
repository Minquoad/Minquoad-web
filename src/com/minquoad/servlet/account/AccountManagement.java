package com.minquoad.servlet.account;

import java.io.IOException;
import java.time.Instant;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.minquoad.dao.interfaces.UserProfileImageDao;
import com.minquoad.entity.User;
import com.minquoad.entity.file.UserProfileImage;
import com.minquoad.frontComponent.form.account.UserPasswordAlterationForm;
import com.minquoad.frontComponent.form.account.UserPictureAlterationForm;
import com.minquoad.frontComponent.form.field.FormFileField;
import com.minquoad.service.StorageManager;
import com.minquoad.tool.http.ImprovedHttpServlet;
import com.minquoad.tool.http.PartTool;

@WebServlet("/AccountManagement")
@MultipartConfig(fileSizeThreshold = 1024 * 1024 * 1, // 1 MB
		maxFileSize = 1024 * 1024 * 10, // 10 MB
		maxRequestSize = 1024 * 1024 * 15, // 15 MB
		location = "C:/minquoad-web-storage/internal/tmp/uploaded/")
public class AccountManagement extends ImprovedHttpServlet {

	public static final String viewPath = "/WEB-INF/page/account/accountManagement.jsp";

	@Override
	public boolean isAccessible(HttpServletRequest request) {
		return getUser(request) != null;
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		forwardToView(request, response, viewPath);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String formId = request.getParameter("formId");

		if (formId != null) {

			if (formId.equals("userPictureAlteration")) {

				UserPictureAlterationForm form = new UserPictureAlterationForm(request);
				
				if (form.isValide()) {

					UserProfileImageDao userProfileImageDao = getDaoFactory(request).getUserProfileImageDao();
					
					UserProfileImage image = userProfileImageDao.getUserUserProfileImageDao(getUser(request));
					if (image != null) {
						image.getFile().delete();
						userProfileImageDao.delete(image);
					}

					FormFileField field = (FormFileField) form.getField(UserPictureAlterationForm.userPictureKey);
					if (field.hasFile()) {
						String fileName = PartTool.saveInNewFile(field.getValue(), StorageManager.communityPath);

						image = new UserProfileImage();
						image.setRelativePath(StorageManager.communityPath + fileName);
						image.setOriginalName(field.getOriginalFileName(false));
						image.setOriginalExtention(field.getOriginalFileExtention());
						image.setLastModificationDate(Instant.now());
						image.setUser(getUser(request));

						userProfileImageDao.persist(image);
					}
				} else {
					request.setAttribute("userPictureAlterationForm", form);
				}
			}

			if (formId.equals("userPasswordAlteration")) {

				UserPasswordAlterationForm form = new UserPasswordAlterationForm(request);

				if (form.isValide()) {
					User user = getUser(request);
					user.setPassword(form.getFieldValueAsString(UserPasswordAlterationForm.newPasswordKey));
					getDaoFactory(request).getUserDao().persist(user);
				} else {
					request.setAttribute("userPasswordAlterationForm", form);
				}
			}

			forwardToView(request, response, viewPath);
		}
	}

	@Override
	public void forwardToView(HttpServletRequest request, HttpServletResponse response, String viewPath) throws ServletException, IOException {

		UserProfileImageDao userProfileImageDao = getDaoFactory(request).getUserProfileImageDao();
		UserProfileImage image = userProfileImageDao.getUserUserProfileImageDao(getUser(request));
		request.setAttribute("userProfileImage", image);

		super.forwardToView(request, response, viewPath);
	}
	
}
