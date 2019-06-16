package com.minquoad.frontComponent.form.field;

import javax.servlet.http.HttpServletRequest;

public class FormBooleanField extends FormField {

	private boolean checked;

	public FormBooleanField(String name) {
		super(name);
	}

	public boolean isChecked() {
		return checked;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
	}

	@Override
	public void collectValue(HttpServletRequest request) {
		this.setChecked(request.getParameter(getName()) != null);
	}

	@Override
	protected boolean isValueEmpty() {
		return !isChecked();
	}

	@Override
	protected boolean isValueNull() {
		return false;
	}

}