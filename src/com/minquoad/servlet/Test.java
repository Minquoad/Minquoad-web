package com.minquoad.servlet;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import com.minquoad.dao.interfaces.ThingDao;
import com.minquoad.entity.Thing;
import com.minquoad.entity.User;
import com.minquoad.service.StorageManager;
import com.minquoad.tool.http.ImprovedHttpServlet;
import com.minquoad.tool.http.PartTool;

@WebServlet("/Test")
@MultipartConfig(fileSizeThreshold = 1024 * 1024 * 1, // 1 MB
		maxFileSize = 1024 * 1024 * 10, // 10 MB
		maxRequestSize = 1024 * 1024 * 15 // 15 MB
)
public class Test extends ImprovedHttpServlet {

	@Override
	public boolean isAccessible(HttpServletRequest request) {
		return true;
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		// System.out.println("requestURL = " + request.getRequestURL().toString());
		// System.out.println("queryString = " + request.getQueryString());
		// System.out.println("requestURI = " + request.getRequestURI());
		// System.out.println("servletPath = " + request.getServletPath());
		// System.out.println("contextPath = " + request.getContextPath());

		ThingDao thingDao = getDaoFactory(request).getThingDao();

		Thing thing2 = new Thing();
		thing2.setId(15);
		User owner = new User();
		//owner.setId(5555);
		//thing2.setOwner(owner);
		thingDao.persist(thing2);

		String descriptionMemberName = "description";

		List<Thing> things;

		things = thingDao.getAllMatching("description \"\"", descriptionMemberName);

		for (Thing thing : things) {
			thing.setDescription("");
			thingDao.persist(thing);
		}

		things = thingDao.getAllMatching("description null", descriptionMemberName);

		for (Thing thing : things) {
			thing.setDescription(null);
			thingDao.persist(thing);
		}

		things = thingDao.getAllMatching("à admin", descriptionMemberName);

		for (Thing thing : things) {
			thing.setOwner(getDaoFactory(request).getUserDao().getByPk(1));
			thingDao.persist(thing);
		}

		things = thingDao.getAllMatching("à null", descriptionMemberName);

		for (Thing thing : things) {
			thing.setOwner(null);
			thingDao.persist(thing);
		}

		things = thingDao.getAllMatching("à User-one", descriptionMemberName);

		for (Thing thing : things) {
			thing.setOwner(getDaoFactory(request).getUserDao().getByPk(4));
			thingDao.persist(thing);
		}

		request.setAttribute("things", thingDao.getAll());
		this.getServletContext().getRequestDispatcher("/WEB-INF/page/test.jsp").forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String idString = request.getParameter("id");
		String description = request.getParameter("description");

		if (idString != null && description != null) {

			if (idString.isEmpty()) {
				if (!description.isEmpty()) {
					Thing thing = new Thing();
					thing.setDescription(description);
					getDaoFactory(request).getThingDao().persist(thing);
				}

			} else {
				int id = Integer.parseInt(idString);

				if (id < 0) {
					Thing thing = getDaoFactory(request).getThingDao().getByPk(-id);
					getDaoFactory(request).getThingDao().delete(thing);

				} else {
					Thing thing = getDaoFactory(request).getThingDao().getByPk(id);
					if (thing == null) {
						thing = new Thing();
						thing.setId(id);
						thing.setDescription(description);
						getDaoFactory(request).getThingDao().persist(thing);

					} else {
						thing.setDescription(description);
						getDaoFactory(request).getThingDao().persist(thing);
					}
				}
			}
			
		}

		Part part = request.getPart("file");
		if (PartTool.hasFile(part)) {
			PartTool.saveInNewFile(part, StorageManager.communityPath);
		}

		this.doGet(request, response);
	}

}
