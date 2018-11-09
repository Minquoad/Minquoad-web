package servlets;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import Entities.Thing;
import daos.interfaces.ThingDao;
import utilities.StorageManager;
import utilities.http.PartTool;

@WebServlet("/Test")
@MultipartConfig(fileSizeThreshold = 1024 * 1024 * 1, // 1 MB
		maxFileSize = 1024 * 1024 * 10, // 10 MB
		maxRequestSize = 1024 * 1024 * 15, // 15 MB
		location = "C:/Users/Minquoad/Dev/Java/Projects/Repositories/GitHub/Minquoad-web/Storage/TmpFiles/Uploaded/")
public class Test extends ImprovedHttpServlet {

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		System.out.println("requestURL = " + request.getRequestURL().toString());
		System.out.println("requestURI = " + request.getRequestURI());
		System.out.println("servletPath = " + request.getServletPath());
		System.out.println("contextPath = " + request.getContextPath());
		
		ThingDao thingDao = getDaoFactory().getThingDao();

		String descriptionMemberName = "description";

		List<Thing> things;

		things = thingDao.getAllMatching("ttt", descriptionMemberName);

		for (Thing thing : things) {
			thing.setDescription("");
			thingDao.update(thing);
		}

		things = thingDao.getAllMatching("null", descriptionMemberName);

		for (Thing thing : things) {
			thing.setDescription(null);
			thingDao.update(thing);
		}

		things = thingDao.getAllMatching("à admin", descriptionMemberName);

		for (Thing thing : things) {
			thing.setOwner(getDaoFactory().getUserDao().getById(1));
			thingDao.update(thing);
		}

		things = thingDao.getAllMatching("à persone", descriptionMemberName);

		for (Thing thing : things) {
			thing.setOwner(null);
			thingDao.update(thing);
		}

		things = thingDao.getAllMatching("à zero", descriptionMemberName);

		for (Thing thing : things) {
			thing.setOwner(getDaoFactory().getUserDao().getById(0));
			thingDao.update(thing);
		}

		request.setAttribute("things", thingDao.getAll());
		this.getServletContext().getRequestDispatcher("/WEB-INF/Test.jsp").forward(request, response);
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

				Thing thing = getDaoFactory().getThingDao().getById(-id);
				if (thing != null) {
					getDaoFactory().getThingDao().delete(thing);
				}

			} else {

				if (!description.isEmpty()) {

					Thing thing = getDaoFactory().getThingDao().getById(id);

					if (thing == null) {

						thing = new Thing();
						thing.setId(id);
						thing.setDescription(description);
						getDaoFactory().getThingDao().insert(thing);
					} else {

						thing.setDescription(description);
						getDaoFactory().getThingDao().update(thing);
					}
				}
			}
		}

		Part part = request.getPart("file");
		if (PartTool.hasFile(part)) {
			PartTool.saveInNewFile(part, StorageManager.communityPublicImgPath);
		}

		this.doGet(request, response);
	}

}
