package com.minquoad.frontComponent.form;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.minquoad.dao.interfaces.DaoFactory;
import com.minquoad.entity.User;
import com.minquoad.frontComponent.form.field.FormField;
import com.minquoad.tool.InternationalizationTool;
import com.minquoad.tool.http.ImprovedHttpServlet;
import com.minquoad.unit.UnitFactory;

public class Form {

	public static final String formResourceBundleName = "resources.Form";

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
		for (FormField<?> field : getFields().values()) {
			field.computeValueProblems();
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

	public DaoFactory getDaoFactory() {
		return ImprovedHttpServlet.getDaoFactory(getRequest());
	}

	public UnitFactory getUnitFactory() {
		return ImprovedHttpServlet.getUnitFactory(getRequest());
	}

	public User getUser() {
		return ImprovedHttpServlet.getUser(getRequest());
	}

	public String getText(String key, Object... args) {
		return InternationalizationTool.getText(
				key,
				formResourceBundleName,
				getLocale(),
				args);
	}

	public String getText(String key) {
		return InternationalizationTool.getText(
				key,
				formResourceBundleName,
				getLocale());
	}

	public Locale getLocale() {
		return ImprovedHttpServlet.getLocale(getRequest());
	}

}
