package com.minquoad.servlet.profile;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.minquoad.dao.interfaces.UserProfileImageDao;
import com.minquoad.entity.User;
import com.minquoad.entity.file.UserProfileImage;
import com.minquoad.frontComponent.form.field.FormFileField;
import com.minquoad.frontComponent.form.impl.profile.ProfileEditionForm;
import com.minquoad.tool.http.ImprovedHttpServlet;
import com.minquoad.tool.http.PartTool;

@WebServlet("/ProfileEdition")
@MultipartConfig(fileSizeThreshold = 1024 * 1024 * 1, // 1 MB
		maxFileSize = 1024 * 1024 * 10, // 10 MB
		maxRequestSize = 1024 * 1024 * 15 // 15 MB
)
public class ProfileEdition extends ImprovedHttpServlet {

	public static final String VIEW_PATH = "/WEB-INF/page/profile/profileEdition.jsp";

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

		ProfileEditionForm form = (ProfileEditionForm) request.getAttribute(FORM_KEY);
		form.submit();

		if (form.isValide()) {

			User user = getUser(request);
			user.setNickname(form.getnickname());
			user.setDefaultColor(form.getDefaultColor());
			getDaoFactory(request).getUserDao().persist(user);

			UserProfileImageDao userProfileImageDao = getDaoFactory(request).getUserProfileImageDao();

			UserProfileImage currentImage = userProfileImageDao.getUserUserProfileImage(user);
			FormFileField field = form.getPictureField();
			if (currentImage != null && (form.isPictureResetRequested() || !field.isValueEmpty())) {
				currentImage.getFile(getDeployment()).delete();
				userProfileImageDao.delete(currentImage);
			}

			if (!field.isValueEmpty()) {
				UserProfileImage image = new UserProfileImage();
				image.setOriginalName(field.getOriginalFileName());
				image.setUser(getUser(request));

				PartTool.saveInProtectedFile(field.getValue(), image, getStorageManager());

				userProfileImageDao.persist(image);
			}
		}

		forwardToView(request, response, VIEW_PATH);
	}

	private void initForms(HttpServletRequest request) {
		request.setAttribute(FORM_KEY, new ProfileEditionForm(request));
	}

}
