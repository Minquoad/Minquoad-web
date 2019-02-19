package servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import Entities.User;
import daos.interfaces.UserDao;

@WebServlet("/SignUp")
public class SignUp extends ImprovedHttpServlet {

	@Override
	public boolean isAccessibleForUser(User user) {
		return user == null;
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setAttribute("nicknameMaxlength", User.nicknameMaxlength);
		this.getServletContext().getRequestDispatcher("/WEB-INF/page/signUp.jsp").forward(request, response);
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

			UserDao userDao = getDaoFactory().getUserDao();

			request.setAttribute("creatingAccount", true);

			formProblems.addAll(User.getNicknameProblems(nickname));
			formProblems.addAll(User.getPasswordProblems(password));

			if (!password.equals(passwordConfirmation)) {
				formProblems.add("The password confirmation is different to the password.");
			}

			List<User> users = getDaoFactory().getUserDao().getAllMatching(nickname, "nickname");
			if (users.size() != 0) {
				formProblems.add("The nickname \"" + nickname + "\" is alreadi taken.");
			}

			if (formProblems.size() == 0) {
				User user = new User();
				user.setNickname(nickname);
				user.setRegistrationDate(new Date());
				user.setLastActivityDate(new Date());
				userDao.insert(user);
				user.setPassword(password);
				userDao.update(user);

				this.setSessionUser(request, user);
			} else {
				request.setAttribute("prefilledNickname", nickname);
			}
			doGet(request, response);
		}
	}

}
