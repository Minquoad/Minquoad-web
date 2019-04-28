package com.minquoad.frontComponent.form.field;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.minquoad.dao.interfaces.Dao;
import com.minquoad.dao.interfaces.DaoGetter;
import com.minquoad.frontComponent.form.field.valueChecker.EntityValueChecker;

public class FormEntityField<Entity> extends FormField {

	private String stringValue;
	private Entity value;
	private List<EntityValueChecker<Entity>> valueCheckers;
	private Dao<Entity> dao;
	private DaoGetter<Entity> daoGetter;

	public FormEntityField(String name, Dao<Entity> dao) {
		super(name);
		valueCheckers = new ArrayList<EntityValueChecker<Entity>>();
		this.dao = dao;
	}

	public FormEntityField(String name, DaoGetter<Entity> daoGetter) {
		super(name);
		valueCheckers = new ArrayList<EntityValueChecker<Entity>>();
		this.daoGetter = daoGetter;
	}

	@Override
	public void computeValueProblems() {
		super.computeValueProblems();
		if (!isValueNull() && !isValueEmpty()) {
			if (getValue() == null) {
				getValueProblems().add(getText("EntityNotFound"));
			} else {
				for (EntityValueChecker<Entity> valueChecker : valueCheckers) {
					String valueProblem = valueChecker.getValueProblem(getForm(), this, getValue());
					if (valueProblem != null) {
						getValueProblems().add(valueProblem);
					}
				}
			}
		}
	}

	public Entity getValue() {
		return value;
	}

	public void setValue(Entity value) {
		this.value = value;
	}

	@Override
	protected boolean isValueEmpty() {
		return getStringValue().isEmpty();
	}

	@Override
	protected boolean isValueNull() {
		return getStringValue() == null;
	}

	@Override
	public void collectValue(HttpServletRequest request) {
		this.setStringValue(request.getParameter(getName()));
		if (!isValueNull() && !isValueEmpty()) {
			Dao<Entity> dao = this.dao;
			if (dao == null) {
				dao = this.daoGetter.getDao(getForm().getDaoFactory());
			}
			try {
				setValue(dao.getByPk(getValueAsInteger()));
			} catch (Exception e) {
			}
			try {
				setValue(dao.getByPk(getValueAsLong()));
			} catch (Exception e) {
			}
			try {
				setValue(dao.getByPk(getStringValue()));
			} catch (Exception e) {
			}
		}
	}

	protected String getStringValue() {
		return stringValue;
	}

	protected void setStringValue(String stringValue) {
		this.stringValue = stringValue.trim();
	}

	protected Integer getValueAsInteger() {
		try {
			return Integer.parseInt(getStringValue());
		} catch (Exception e) {
			return null;
		}
	}

	protected Long getValueAsLong() {
		try {
			return Long.parseLong(getStringValue());
		} catch (Exception e) {
			return null;
		}
	}

	public void addValueChecker(EntityValueChecker<Entity> valueChecker) {
		this.valueCheckers.add(valueChecker);
	}

}
