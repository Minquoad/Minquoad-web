package servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import entity.User;

@WebServlet("/AccountBlocked")
public class AccountBlocked extends ImprovedHttpServlet {

	@Override
	public boolean isAccessibleForUser(User user) {
		return user != null && super.isAccessibleForUser(user) && user.isBlocked();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		this.getServletContext().getRequestDispatcher("/WEB-INF/page/accountBlocked.jsp").forward(request, response);
	}

}
