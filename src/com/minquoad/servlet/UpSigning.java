package com.minquoad.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.minquoad.entity.User;
import com.minquoad.tool.http.ImprovedHttpServlet;

@WebServlet("/UpSigning")
public class UpSigning extends ImprovedHttpServlet {

	@Override
	public boolean isAccessible(HttpServletRequest request) {
		return getUser(request) == null;
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setAttribute("nicknameMaxlength", User.nicknameMaxlength);
		this.getServletContext().getRequestDispatcher("/WEB-INF/page/upSigning.jsp").forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String nickname = request.getParameter("nickname");
		String password = request.getParameter("password");
		String passwordConfirmation = request.getParameter("passwordConfirmation");

		if (nickname != null && password != null) {
			List<String> formProblems = new ArrayList<String>();
			request.setAttribute("formProblems", formProblems);

			nickname = User.formatNickanameCase(nickname);

			request.setAttribute("creatingAccount", true);

			formProblems.addAll(User.getNicknameProblems(nickname));
			formProblems.addAll(User.getPasswordProblems(password));

			if (!password.equals(passwordConfirmation)) {
				formProblems.add("The password confirmation is different to the password.");
			}

			List<User> users = getDaoFactory(request).getUserDao().getAllMatching(nickname, "nickname");
			if (users.size() != 0) {
				formProblems.add("The nickname \"" + nickname + "\" is alreadi taken.");
			}

			if (formProblems.size() == 0) {

				User user = getUnitFactory(request).getUserUnit().createNewUser(nickname, password);

				setSessionUser(request, user);
				response.sendRedirect(request.getContextPath() + "/");
			} else {
				request.setAttribute("prefilledNickname", nickname);
				doGet(request, response);
			}
		}
	}

}
