package com.minquoad.framework.form;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

public abstract class Form {

	private LinkedHashMap<String, FormField<?>> fileds;
	private HttpServletRequest request;
	private boolean submitted;

	public Form(HttpServletRequest request) {
		fileds = new LinkedHashMap<String, FormField<?>>();
		this.request = request;
		setSubmitted(false);
		this.build();
	}

	protected void build() {
	}

	public void submit() {
		setSubmitted(true);
		for (FormField<?> field : getFields().values()) {
			field.collectValue(getRequest());
		}
	}

	public boolean isValide() {
		for (FormField<?> field : getFields().values()) {
			if (!field.isValid()) {
				return false;
			}
		}
		return true;
	}

	public Collection<String> getFieldValueProblems(String name) {
		return getFields().get(name).getValueProblems();
	}

	public void addField(FormField<?> field) {
		field.setForm(this);
		getFields().put(field.getName(), field);
	}

	public boolean hasField(String name) {
		return getFields().get(name) != null;
	}
	
	public FormField<?> getField(String name) {
		return getFields().get(name);
	}

	public Map<String, FormField<?>> getFields() {
		return fileds;
	}

	public boolean isSubmitted() {
		return submitted;
	}

	private void setSubmitted(boolean submitted) {
		this.submitted = submitted;
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public abstract String getText(String key, Object... args);

}
