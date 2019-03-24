package com.minquoad.servlet;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.minquoad.dao.Database;
import com.minquoad.tool.http.ImprovedHttpServlet;

@WebServlet({"/Home", "/"})
public class Home extends ImprovedHttpServlet {

	public static final String viewPath = "/WEB-INF/page/home.jsp";

	@Override
	public boolean isAccessible(HttpServletRequest request) {
		return true;
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {


		try {

			String q = "SELECT * FROM \"UserProfileImage\", \"ProtectedFile\" WHERE \"ProtectedFile\".\"id\"=2 AND \"ProtectedFile\".\"id\"=\"UserProfileImage\".\"id\";";
			
			
			PreparedStatement preparedStatement = Database.prepareStatement(q);
			
			
			ResultSet resultSet = preparedStatement.executeQuery();

			resultSet.next();

			String string = resultSet.getString("originalName");

			int integer0 = resultSet.getInt("id");
			int integer1 = resultSet.getInt(1);
			int integer2 = resultSet.getInt(2);
			int integer3 = resultSet.getInt(3);

			int i = 0;
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		
		
		
		forwardToView(request, response, viewPath);
	}

}
