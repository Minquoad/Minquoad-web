package com.minquoad.frontComponent.form.field;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.minquoad.dao.interfaces.Dao;
import com.minquoad.frontComponent.form.field.valueChecker.StringValueChecker;
import com.minquoad.tool.http.DaoGetter;

public class FormStringField extends FormField {

	private String value;
	private List<StringValueChecker> valueCheckers;
	private boolean trimingValue;

	public FormStringField(String name) {
		super(name);
		valueCheckers = new ArrayList<StringValueChecker>();
		setTrimingValue(true);
	}

	@Override
	public void computeValueProblems() {
		super.computeValueProblems();
		if (getValueProblems().isEmpty()) {
			for (StringValueChecker valueChecker : valueCheckers) {
				String valueProblem = valueChecker.getValueProblem(getForm(), this, getValue());
				if (valueProblem != null) {
					getValueProblems().add(valueProblem);
				}
			}
		}
	}

	public void collectValue(HttpServletRequest request) {
		this.setValue(request.getParameter(getName()));
	}

	public void addValueChecker(StringValueChecker valueChecker) {
		this.valueCheckers.add(valueChecker);
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		if (isTrimingValue() && value != null) {
			value = value.trim();
		}
		this.value = value;
	}

	public Integer getValueAsInteger() {
		try {
			return Integer.parseInt(getValue());
		} catch (Exception e) {
			return null;
		}
	}

	public Long getValueAsLong() {
		try {
			return Long.parseLong(getValue());
		} catch (Exception e) {
			return null;
		}
	}

	public Float getValueAsFloat() {
		try {
			return Float.parseFloat(getValue());
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
			return dao.getByPk(getValue());
		} catch (Exception e) {
		}
		return null;
	}

	public boolean isTrimingValue() {
		return trimingValue;
	}

	public void setTrimingValue(boolean trimingValue) {
		this.trimingValue = trimingValue;
	}

	@Override
	protected boolean isValueEmpty() {
		return getValue().isEmpty();
	}

	@Override
	protected boolean isValueNull() {
		return getValue() == null;
	}

}
