package com.minquoad.frontComponent.form.field;

import com.minquoad.tool.ImageTool;

public class FormImageField extends FormFileField {

	public FormImageField(String name) {
		super(name);

		for (String extention : ImageTool.getPossibleImageExtentions()) {
			this.addAllowedExtention(extention);
		}
		this.addValueChecker((form, field, value) -> {
			// if the process reaches this code, then the extension are already good
			if (field.isImage()) {
				return null;
			}
			return getText("UnreadableImage");
		});
	}

}
