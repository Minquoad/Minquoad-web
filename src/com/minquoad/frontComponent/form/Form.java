package com.minquoad.frontComponent.form;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.minquoad.dao.interfaces.DaoFactory;
import com.minquoad.entity.User;
import com.minquoad.entity.unit.UnitFactory;
import com.minquoad.frontComponent.form.field.FormField;
import com.minquoad.tool.http.ImprovedHttpServlet;

public class Form {

	private Map<String, FormField> fileds;

	private HttpServletRequest request;

	public Form(HttpServletRequest request) {

		fileds = new HashMap<String, FormField>();

		setRequest(request);

		this.build();
	}

	protected void build() {
	}

	public boolean isValide() {
		for (FormField field : getFileds()) {
			if (!field.isValid()) {
				return false;
			}
		}
		return true;
	}

	public Collection<FormField> getFileds() {
		return fileds.values();
	}

	public List<String> getFieldValueProblems(String name) {
		FormField field = fileds.get(name);
		if (field == null) {
			new Exception("Field with name  " + name + " not existing in the form.").printStackTrace();
			return null;
		} else {
			return field.getValueProblems();
		}
	}

	public void addField(FormField field) {
		field.setForm(this);
		fileds.put(field.getName(), field);
		field.collectValue(request);
	}

	public FormField getField(String name) {
		return fileds.get(name);
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

	public HttpServletRequest getRequest() {
		return request;
	}

	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	public String getFieldValueAsString(String name) {
		return this.getField(name).getValueAsString();
	}

}
