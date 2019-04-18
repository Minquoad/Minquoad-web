package com.minquoad.frontComponent.form.field;

import com.minquoad.tool.ImageTool;

public class FormImageField extends FormFileField {

	public FormImageField(String name) {
		super(name);

		for (String extention : ImageTool.getPossibleImageExtentions()) {
			this.addAllowedExtention(extention);
		}
		this.addValueChecker((form, field, value) -> {
			if (ImageTool.isImage(value)) {
				return null;
			}
			return "There is a problem in the image file.";
		});
	}

}
