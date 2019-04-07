package com.minquoad.servlet;

import javax.servlet.annotation.WebServlet;

import com.minquoad.entity.file.ProtectedFile;

@WebServlet("/ImageDownload")
public class ImageDownload extends FileDownload {

	public String getContentDisposition(ProtectedFile protectedFile) {
		return "inline";
	}

}
