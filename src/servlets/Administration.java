package servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import Entities.User;

@WebServlet("/Administration")
public class Administration extends ImprovedHttpServlet {

	@Override
	public boolean isAccessibleForUser(User user) {
		return user != null && super.isAccessibleForUser(user) && user.isAdmin();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setAttribute("users", this.getDaoFactory().getUserDao().getAll());
		this.getServletContext().getRequestDispatcher("/WEB-INF/page/administration.jsp").forward(request,
				response);
	}

}
