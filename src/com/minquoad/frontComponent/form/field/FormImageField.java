package com.minquoad.frontComponent.form.field;

public class FormImageField extends FormFileField {

	public FormImageField(String name) {
		super(name);

		this.addAllowedExtention(this.getPossibleImageExtentions());

		this.addBlockingChecker((form, field, value) -> {
			if (isImage()) {
				return null;
			}
			return form.getText("UnreadableImage");
		});
	}

}
