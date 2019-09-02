package com.minquoad.servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import com.minquoad.dao.interfaces.ProtectedFileDao;
import com.minquoad.dao.interfaces.ThingDao;
import com.minquoad.entity.Thing;
import com.minquoad.entity.file.ProtectedFile;
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

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		// System.out.println("requestURL = " + request.getRequestURL().toString());
		// http://localhost:8080/Minquoad-web/Test

		// System.out.println("queryString = " + request.getQueryString());
		// null of name=value

		// System.out.println("requestURI = " + request.getRequestURI());
		// /Minquoad-web/Test

		// System.out.println("servletPath = " + request.getServletPath());
		// /Test

		// System.out.println("contextPath = " + request.getContextPath());
		// /Minquoad-web

		ThingDao thingDao = getDaoFactory(request).getThingDao();

		String descriptionMemberName = "description";

		List<Thing> things;

		things = thingDao.getAllMatching(descriptionMemberName, "description \"\"");

		for (Thing thing : things) {
			thing.setDescription("");
			thingDao.persist(thing);
		}

		things = thingDao.getAllMatching(descriptionMemberName, "description null");

		for (Thing thing : things) {
			thing.setDescription(null);
			thingDao.persist(thing);
		}

		things = thingDao.getAllMatching(descriptionMemberName, "à Adminquo");

		for (Thing thing : things) {
			thing.setOwner(getDaoFactory(request).getUserDao().getByPk(5611120057846513921l));
			thingDao.persist(thing);
		}

		things = thingDao.getAllMatching(descriptionMemberName, "à null");

		for (Thing thing : things) {
			thing.setOwner(null);
			thingDao.persist(thing);
		}

		things = thingDao.getAllMatching(descriptionMemberName, "à User-one");

		for (Thing thing : things) {
			thing.setOwner(getDaoFactory(request).getUserDao().getByPk(7345364893521792127l));
			thingDao.persist(thing);
		}

		request.setAttribute("things", thingDao.getAll());
		this.getServletContext().getRequestDispatcher("/WEB-INF/page/test.jsp").forward(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String idString = request.getParameter("id");
		String description = request.getParameter("description");

		if (idString != null && description != null) {

			if (idString.isEmpty()) {
				Thing thing = new Thing();
				if (!description.isEmpty()) {
					thing.setDescription(description);
				}
				getDaoFactory(request).getThingDao().persist(thing);

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
		if (part != null) {
			ProtectedFileDao protectedFileDao = getDaoFactory(request).getProtectedFileDao();
			ProtectedFile pf = protectedFileDao.getByPk(0l);
			if (pf != null) {
				pf.getFile(getStorageManager()).delete();
				protectedFileDao.delete(pf);
			}
			if (PartTool.hasFile(part)) {
				pf = new ProtectedFile();
				pf.setId(0l);
				pf.setOriginalName(PartTool.getFileName(part));
				PartTool.saveInProtectedFile(part, pf, getStorageManager());
				protectedFileDao.persist(pf);
			}
		}

		this.doGet(request, response);
	}

}
