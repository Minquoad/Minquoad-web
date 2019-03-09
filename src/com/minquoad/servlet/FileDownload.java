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
import com.minquoad.service.Deployment;
import com.minquoad.tool.http.ImprovedHttpServlet;

@WebServlet("/FileDownload")
public class FileDownload extends ImprovedHttpServlet {

	private static final int DEFAULT_BUFFER_SIZE = 10240;

	@Override
	public boolean isAccessible(HttpServletRequest request) {
		ProtectedFile protectedFile = getEntityFromIdParameter(request, "protectedFileId", DaoFactory::getProtectedFileDao);
		return protectedFile != null && protectedFile.isDownloadableForUser(getUser(request));
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		ProtectedFile protectedFile = getEntityFromIdParameter(request, "protectedFileId", DaoFactory::getProtectedFileDao);

		File file = new File(Deployment.storagePath + protectedFile.getStoragePath());

		String type = getServletContext().getMimeType(file.getName());

		if (type == null) {
			type = "application/octet-stream";
		}

		response.reset();
		response.setBufferSize(DEFAULT_BUFFER_SIZE);
		response.setContentType(type);
		response.setHeader("Content-Length", String.valueOf(file.length()));
		response.setHeader("Content-Disposition", "attachment; filename=\"" + protectedFile.getId() + "\"");

		BufferedInputStream input = null;
		BufferedOutputStream output = null;
		try {
			input = new BufferedInputStream(new FileInputStream(file), DEFAULT_BUFFER_SIZE);
			output = new BufferedOutputStream(response.getOutputStream(), DEFAULT_BUFFER_SIZE);

			byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
			int longueur;
			while ((longueur = input.read(buffer)) > 0) {
				output.write(buffer, 0, longueur);
			}

		} finally {
			try {
				input.close();
			} catch (IOException ignore) {
			}
			try {
				output.close();
			} catch (IOException ignore) {
			}
		}
	}
}
