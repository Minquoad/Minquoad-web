package com.minquoad.servlet;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.minquoad.dao.interfaces.DaoFactory;
import com.minquoad.entity.file.ProtectedFile;
import com.minquoad.tool.http.ImprovedHttpServlet;

@WebServlet("/FileDownload")
public class FileDownload extends ImprovedHttpServlet {

	private static final String PROTECTED_FILE_ID = "id";

	private static final int DEFAULT_BUFFER_SIZE = 10240;

	@Override
	public boolean isAccessible(HttpServletRequest request) {
		ProtectedFile protectedFile = getEntityFromIdParameter(request, PROTECTED_FILE_ID, DaoFactory::getProtectedFileDao);
		return protectedFile != null && protectedFile.isDownloadableForUser(getUser(request));
	}

	@Override
	protected void doHead(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		ProtectedFile protectedFile = getEntityFromIdParameter(request, PROTECTED_FILE_ID, DaoFactory::getProtectedFileDao);

		response.setBufferSize(DEFAULT_BUFFER_SIZE);
		response.setContentType(getMimeType(protectedFile));
		response.setHeader("Content-Length", String.valueOf(protectedFile.getFile().length()));
		response.setHeader("Content-Disposition", getContentDisposition(protectedFile));
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		ProtectedFile protectedFile = getEntityFromIdParameter(request, PROTECTED_FILE_ID, DaoFactory::getProtectedFileDao);

		File file = protectedFile.getFile();

		response.reset();
		response.setBufferSize(DEFAULT_BUFFER_SIZE);
		response.setContentType(getMimeType(protectedFile));
		response.setHeader("Content-Length", String.valueOf(file.length()));
		response.setHeader("Content-Disposition", getContentDisposition(protectedFile));
		response.setDateHeader("Last-Modified", getLastModified(request));

		BufferedInputStream inputStream = null;
		BufferedOutputStream outputStream = null;
		try {
			inputStream = new BufferedInputStream(new FileInputStream(file), DEFAULT_BUFFER_SIZE);
			outputStream = new BufferedOutputStream(response.getOutputStream(), DEFAULT_BUFFER_SIZE);

			byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
			int length;
			while ((length = inputStream.read(buffer)) > 0) {
				outputStream.write(buffer, 0, length);
			}
			inputStream.close();
			outputStream.close();

		} catch (Exception e) {
			try {
				inputStream.close();
			} catch (Exception e0) {
			}
			try {
				outputStream.close();
			} catch (Exception e0) {
			}
			throw e;
		}
	}

	public String getContentDisposition(ProtectedFile protectedFile) {
		return "attachment; filename=\"" + protectedFile.getApparentName() + "\"";
	}

	protected String getMimeType(ProtectedFile protectedFile) {

		String mimeType = getServletContext().getMimeType(protectedFile.getFile().getName());

		if (mimeType == null) {
			mimeType = "application/octet-stream";
		}

		return mimeType;
	}

	@Override
	protected long getLastModified(HttpServletRequest request) {
		// for safety reason a fake constant old lastModified value is used
		// considering ProtectedFile.relativePath is immutable, no browser cache issue should append
		return 1000000000000l;
	}

}
