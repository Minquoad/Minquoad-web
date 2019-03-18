package com.minquoad.frontComponent.form;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;import javax.servlet.http.HttpServletRequest;

import com.minquoad.dao.interfaces.Dao;
import com.minquoad.dao.interfaces.DaoFactory;
import com.minquoad.framework.dao.Entity;
import com.minquoad.tool.http.ImprovedHttpServlet;

public class Form {

	private Map<String, FormField> fileds;

	private HttpServletRequest request;

	public Form(HttpServletRequest request) {
		
		fileds = new HashMap<String, FormField>();
		
		setRequest(request);
		
		this.build();
		for (FormField field : getFileds()) {
			field.collectValue(request);
		}
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

	public String getFieldValue(String name) {
		FormField field = fileds.get(name);
		if (field == null) {
			return null;
		} else {
			return field.getValue();
		}
	}

	public List<String> getFieldValueProblems(String name) {
		FormField field = fileds.get(name);
		if (field == null) {
			return null;
		} else {
			return field.getValueProblems();
		}
	}

	public void addField(String name) {
		addField(new FormField(name));
	}

	public void addField(FormField formField) {
		formField.setForm(this);
		fileds.put(formField.getName(), formField);
	}

	public FormField getField(String name) {
		return fileds.get(name);
	}

	protected DaoFactory getDaoFactory() {
		return ImprovedHttpServlet.getDaoFactory(getRequest());
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	// custom value getters

	public Integer getFieldValueAsInteger(String name) {
		FormField field = fileds.get(name);
		if (field == null) {
			return null;
		} else {
			return field.getValueAsInteger();
		}
	}

	public Float getFieldValueAsFloat(String name) {
		FormField field = fileds.get(name);
		if (field == null) {
			return null;
		} else {
			return field.getValueAsFloat();
		}
	}

	public <EntitySubclass extends Entity> EntitySubclass getValueAsEntity(String name, Dao<EntitySubclass> dao) {
		FormField field = fileds.get(name);
		if (field == null) {
			return null;
		} else {
			return field.getValueAsEntity(dao);
		}
	}

}
