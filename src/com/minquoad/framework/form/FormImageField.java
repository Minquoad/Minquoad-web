package com.minquoad.framework.form;

public class FormImageField extends FormFileField {

	public FormImageField(String name) {
		super(name);

		this.addAllowedExtention(this.getPossibleImageExtentions());

		this.addBlockingChecker((form, field, value) -> {
			if (this.isFileImage(value)) {
				return null;
			}
			return form.getText("UnreadableImage");
		});
	}

}
