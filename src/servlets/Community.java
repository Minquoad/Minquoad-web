package servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/Community")
public class Community extends ImprovedHttpServlet {

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setAttribute("users", this.getDaoFactory().getUserDao().getAll());
		this.getServletContext().getRequestDispatcher("/WEB-INF/page/community.jsp").forward(request, response);
	}

}
