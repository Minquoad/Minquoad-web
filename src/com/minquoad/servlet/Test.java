package com.minquoad.servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import com.minquoad.dao.interfaces.ThingDao;
import com.minquoad.entity.Thing;
import com.minquoad.service.StorageManager;
import com.minquoad.tool.http.ImprovedHttpServlet;
import com.minquoad.tool.http.PartTool;

@WebServlet("/Test")
@MultipartConfig(fileSizeThreshold = 1024 * 1024 * 1, // 1 MB
		maxFileSize = 1024 * 1024 * 10, // 10 MB
		maxRequestSize = 1024 * 1024 * 15, // 15 MB
		location = "C:/minquoad-web-storage/internal/tmp/uploaded/")
public class Test extends ImprovedHttpServlet {

	@Override
	public boolean isAccessible(HttpServletRequest request) {
		return true;
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		/*
		System.out.println("requestURL = " + request.getRequestURL().toString());
		System.out.println("queryString = " + request.getQueryString());
		System.out.println("requestURI = " + request.getRequestURI());
		System.out.println("servletPath = " + request.getServletPath());
		System.out.println("contextPath = " + request.getContextPath());
		*/
		ThingDao thingDao = getDaoFactory(request).getThingDao();

		String descriptionMemberName = "description";

		List<Thing> things;

		things = thingDao.getAllMatching("description \"\"", descriptionMemberName);

		for (Thing thing : things) {
			thing.setDescription("");
			thingDao.update(thing);
		}

		things = thingDao.getAllMatching("description null", descriptionMemberName);

		for (Thing thing : things) {
			thing.setDescription(null);
			thingDao.update(thing);
		}

		things = thingDao.getAllMatching("à admin", descriptionMemberName);

		for (Thing thing : things) {
			thing.setOwner(getDaoFactory(request).getUserDao().getById(1));
			thingDao.update(thing);
		}

		things = thingDao.getAllMatching("à null", descriptionMemberName);

		for (Thing thing : things) {
			thing.setOwner(null);
			thingDao.update(thing);
		}

		things = thingDao.getAllMatching("à Test-zero", descriptionMemberName);

		for (Thing thing : things) {
			thing.setOwner(getDaoFactory(request).getUserDao().getById(2));
			thingDao.update(thing);
		}

		request.setAttribute("things", thingDao.getAll());
		this.getServletContext().getRequestDispatcher("/WEB-INF/page/test.jsp").forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String idString = request.getParameter("id");
		String description = request.getParameter("description");

		if (idString != null && description != null) {

			int id = 0;
			if (!idString.isEmpty()) {
				id = Integer.parseInt(idString);
			}

			if (id < 0) {

				Thing thing = getDaoFactory(request).getThingDao().getById(-id);
				if (thing != null) {
					getDaoFactory(request).getThingDao().delete(thing);
				}

			} else {

				if (!description.isEmpty()) {

					Thing thing = getDaoFactory(request).getThingDao().getById(id);

					if (thing == null) {

						thing = new Thing();
						thing.setId(id);
						thing.setDescription(description);
						getDaoFactory(request).getThingDao().insert(thing);
					} else {

						thing.setDescription(description);
						getDaoFactory(request).getThingDao().update(thing);
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