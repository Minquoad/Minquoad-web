package com.minquoad.servlet;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import com.minquoad.entity.User;
import com.minquoad.service.StorageManager;
import com.minquoad.tool.http.ImprovedHttpServlet;
import com.minquoad.tool.http.PartTool;

@WebServlet("/AccountManagement")
@MultipartConfig(fileSizeThreshold = 1024 * 1024 * 1, // 1 MB
		maxFileSize = 1024 * 1024 * 10, // 10 MB
		maxRequestSize = 1024 * 1024 * 15, // 15 MB
		location = "C:/minquoad-web-storage/internal/tmp/uploaded/")
public class AccountManagement extends ImprovedHttpServlet {

	@Override
	public boolean isAccessible(HttpServletRequest request) {
		return getUser(request) != null;
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		this.getServletContext().getRequestDispatcher("/WEB-INF/page/accountManagement.jsp").forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String formId = request.getParameter("formId");

		if (formId != null) {

			if (formId.equals("userPictureAlteration")) {

				List<String> formProblems = new ArrayList<String>();
				request.setAttribute("userPictureAlterationFormProblems", formProblems);

				User user = getUser(request);

				if (user.getPictureName() != null) {
					new File(StorageManager.communityPath + user.getPictureName()).delete();
				}

				Part part = request.getPart("userPicture");
				if (PartTool.hasFile(part)) {
					String newFileName = PartTool.saveInNewFile(part, StorageManager.communityPath);
					user.setPictureName(newFileName);
				} else {
					user.setPictureName(null);
				}
				getDaoFactory(request).getUserDao().persist(user);

				doGet(request, response);
			}

			if (formId.equals("userPasswordAlteration")) {

				String oldPassowrd = request.getParameter("oldPassowrd");
				String newPassword = request.getParameter("newPassword");
				String newPasswordConfirmation = request.getParameter("newPasswordConfirmation");

				if (oldPassowrd != null && newPassword != null && newPasswordConfirmation != null) {

					User user = getUser(request);

					List<String> formProblems = new ArrayList<String>();
					request.setAttribute("userPasswordAlterationFormProblems", formProblems);

					if (!user.isPassword(oldPassowrd)) {
						formProblems.add("Old password not correct.");
					}

					if (!newPassword.equals(newPasswordConfirmation)) {
						formProblems.add("The password confirmation is different to the password.");
					} else {
						formProblems.addAll(User.getPasswordProblems(newPassword));
					}

					if (formProblems.size() == 0) {
						user.setPassword(newPassword);
						getDaoFactory(request).getUserDao().persist(user);
					}

					doGet(request, response);
				}
			}
		}
	}

}
