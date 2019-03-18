package com.minquoad.frontComponent.form;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.minquoad.dao.interfaces.Dao;
import com.minquoad.dao.interfaces.DaoFactory;
import com.minquoad.framework.dao.Entity;

public class FormField {

	private Form form;
	private String name;
	private String value;
	private List<ValueChecker> valueCheckers;

	public FormField(String name) {
		this.name = name;
		valueCheckers = new ArrayList<ValueChecker>();
	}

	public String getName() {
		return name;
	}

	public boolean isValid() {
		return getValueProblems().size() == 0;
	}

	public List<String> getValueProblems() {
		List<String> problems = new ArrayList<String>();
		for (ValueChecker valueChecker : valueCheckers) {
			String valueProblem = valueChecker.getValueProblem(getValue());
			if (valueProblem != null) {
				problems.add(valueProblem);
			}
		}
		return problems;
	}

	public void addValueChecker(ValueChecker valueChecker) {
		this.valueCheckers.add(valueChecker);
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public void collectValue(HttpServletRequest request) {
		this.value = request.getParameter(getName());
	}

	// custom value getters

	public Integer getValueAsInteger() {
		if (getValue() == null || getValue().equals("")) {
			return null;
		}
		try {
			Integer value = Integer.parseInt(getValue());
			return value;
		} catch (Exception e) {
			return null;
		}
	}

	public Float getValueAsFloat() {
		if (getValue() == null || getValue().equals("")) {
			return null;
		} else {
			try {
				Float value = Float.parseFloat(getValue());
				return value;
			} catch (Exception e) {
				return null;
			}
		}
	}

	public <EntitySubclass extends Entity> EntitySubclass getValueAsEntity(Dao<EntitySubclass> dao) {
		return dao.getById(getValueAsInteger());
	}

	public Form getForm() {
		return form;
	}

	public void setForm(Form form) {
		this.form = form;
	}

	protected DaoFactory getDaoFactory() {
		return getForm().getDaoFactory();
	}
	
}
