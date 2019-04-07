package com.minquoad.frontComponent.form.field.valueChecker;

import javax.servlet.http.Part;

import com.minquoad.frontComponent.form.Form;
import com.minquoad.frontComponent.form.field.FormFileField;
import com.minquoad.tool.ImageTool;

public class ImageChecker implements PartValueChecker {

	@Override
	public String getValueProblem(Form form, FormFileField field, Part value) {
		if (ImageTool.isImage(value)) {
			return null;
		}
		return "There is a problem in the image file.";
	}

}
