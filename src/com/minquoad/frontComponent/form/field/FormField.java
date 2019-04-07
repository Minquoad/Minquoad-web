package com.minquoad.frontComponent.form.field;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.minquoad.dao.interfaces.Dao;
import com.minquoad.dao.interfaces.DaoFactory;
import com.minquoad.frontComponent.form.Form;
import com.minquoad.tool.http.DaoGetter;
import com.minquoad.unit.UnitFactory;

public abstract class FormField {

	private Form form;
	private String name;
	private boolean required;

	private List<String> problems;

	public FormField(String name) {
		this.name = name;
		required = false;
	}

	public boolean isRequired() {
		return required;
	}

	public void setRequired(boolean required) {
		this.required = required;
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
		try {
			return Integer.parseInt(getValueAsString());
		} catch (Exception e) {
			return null;
		}
	}

	public Long getValueAsLong() {
		try {
			return Long.parseLong(getValueAsString());
		} catch (Exception e) {
			return null;
		}
	}

	public Float getValueAsFloat() {
		try {
			return Float.parseFloat(getValueAsString());
		} catch (Exception e) {
			return null;
		}
	}

	public <EntitySubclass> EntitySubclass getValueAsEntity(DaoGetter<EntitySubclass> daoGetter) {
		return getValueAsEntity(daoGetter.getDao(getDaoFactory()));
	}

	public <EntitySubclass> EntitySubclass getValueAsEntity(Dao<EntitySubclass> dao) {
		try {
			return dao.getByPk(getValueAsInteger());
		} catch (Exception e) {
		}
		try {
			return dao.getByPk(getValueAsLong());
		} catch (Exception e) {
		}
		try {
			return dao.getByPk(getValueAsString());
		} catch (Exception e) {
		}
		return null;
	}

}
