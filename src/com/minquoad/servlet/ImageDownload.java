package com.minquoad.servlet;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;

import com.minquoad.entity.file.ProtectedFile;

@WebServlet("/ImageDownload")
public class ImageDownload extends FileDownload {

	@Override
	public String getContentDisposition(HttpServletRequest request, ProtectedFile protectedFile) {
		return "inline; filename=\""
				+ protectedFile.getContentDispositionFileName(
						getUser(request),
						getDaoFactory(request),
						getUnitFactory(request))
				+ "\"";
	}

}
