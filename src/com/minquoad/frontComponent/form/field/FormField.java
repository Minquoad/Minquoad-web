package com.minquoad.frontComponent.form.field;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.minquoad.dao.interfaces.Dao;
import com.minquoad.dao.interfaces.DaoFactory;
import com.minquoad.entity.unit.UnitFactory;
import com.minquoad.framework.dao.Entity;
import com.minquoad.frontComponent.form.Form;

public abstract class FormField {

	private Form form;
	private String name;

	private List<String> problems;

	public FormField(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public boolean isValid() {
		return getValueProblems().size() == 0;
	}

	protected void setValueProblems(List<String> problems) {
		this.problems = problems;
	}
	
	public List<String> getValueProblems() {
		if (problems == null) {
			computeValueProblems();
		}
		return problems;
	}
	
	protected abstract void computeValueProblems();
	
	public abstract void collectValue(HttpServletRequest request);

	public Form getForm() {
		return form;
	}

	public void setForm(Form form) {
		this.form = form;
	}

	protected DaoFactory getDaoFactory() {
		return getForm().getDaoFactory();
	}

	protected UnitFactory getUnitFactory() {
		return getForm().getUnitFactory();
	}

	public abstract String getValueAsString();

	public Integer getValueAsInteger() {
		if (getValueAsString() == null || getValueAsString().equals("")) {
			return null;
		}
		try {
			Integer value = Integer.parseInt(getValueAsString());
			return value;
		} catch (Exception e) {
			return null;
		}
	}

	public Float getValueAsFloat() {
		if (getValueAsString() == null || getValueAsString().equals("")) {
			return null;
		} else {
			try {
				Float value = Float.parseFloat(getValueAsString());
				return value;
			} catch (Exception e) {
				return null;
			}
		}
	}

	public <EntitySubclass extends Entity> EntitySubclass getValueAsEntity(Dao<EntitySubclass> dao) {
		return dao.getById(getValueAsInteger());
	}

}
