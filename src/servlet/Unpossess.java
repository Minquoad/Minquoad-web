package servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/Unpossess")
public class Unpossess extends ImprovedHttpServlet {

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		this.setSessionUser(request, this.getSessionAdminister(request));
		request.getSession().removeAttribute(sessionAdministerIdKey);
		request.removeAttribute("sessionAdminister");

		response.sendRedirect(request.getContextPath() + "/Administration");
	}

}
