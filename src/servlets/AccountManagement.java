package servlets;

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

import Entities.User;
import utilities.StorageManager;
import utilities.http.PartTool;

@WebServlet("/AccountManagement")
@MultipartConfig(fileSizeThreshold = 1024 * 1024 * 1, // 1 MB
		maxFileSize = 1024 * 1024 * 10, // 10 MB
		maxRequestSize = 1024 * 1024 * 15, // 15 MB
		location = "C:/Users/Minquoad/Dev/Java/Projects/Repositories/GitHub/Minquoad-web/Storage/TmpFiles/Uploaded/")
public class AccountManagement extends ImprovedHttpServlet {

	@Override
	public boolean servesOnlyLoggedUsers() {
		return true;
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		this.getServletContext().getRequestDispatcher("/WEB-INF/AccountManagement.jsp").forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String formId = request.getParameter("formId");

		if (formId != null) {

			if (formId.equals("userPictureAlteration")) {

				List<String> formProblems = new ArrayList<String>();
				request.setAttribute("userPictureAlterationFormProblems", formProblems);

				User user = getSessionUser(request);

				if (user.getPictureName() != null) {
					new File(StorageManager.communityPublicImgPath + user.getPictureName()).delete();
				}

				Part part = request.getPart("userPicture");
				if (PartTool.hasFile(part)) {
					String newFileName = PartTool.saveInNewFile(part, StorageManager.communityPublicImgPath);
					user.setPictureName(newFileName);
				} else {
					user.setPictureName(null);
				}
				getDaoFactory().getUserDao().update(user);

			}
		}

		doGet(request, response);
	}

}