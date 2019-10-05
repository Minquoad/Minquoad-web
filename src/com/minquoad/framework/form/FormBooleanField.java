package com.minquoad.framework.form;

import javax.servlet.http.HttpServletRequest;

public class FormBooleanField extends FormField<Boolean> {

	public FormBooleanField(String name) {
		super(name);
	}

	@Override
	public void collectValue(HttpServletRequest request) {
		this.setValue(request.getParameter(getName()) != null);
	}

	@Override
	public boolean isValueEmpty() {
		return !getValue();
	}

	@Override
	public boolean isValueNull() {
		return false;
	}

}
