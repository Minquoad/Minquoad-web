package com.minquoad.frontComponent.form.field;

import java.util.Collection;
import java.util.LinkedList;

import javax.servlet.http.HttpServletRequest;

import com.minquoad.dao.interfaces.DaoFactory;
import com.minquoad.entity.User;
import com.minquoad.frontComponent.form.Form;
import com.minquoad.unit.UnitFactory;

public abstract class FormField {

	private Form form;
	private String name;
	private boolean nullPermitted;
	private boolean emptyPermitted;

	private Collection<String> valueProblems;

	public FormField(String name) {
		this.name = name;
		setNullPermitted(false);
		setEmptyPermitted(true);
		valueProblems = new LinkedList<String>();
	}

	public String getName() {
		return name;
	}

	public boolean isValid() {
		return getValueProblems().size() == 0;
	}

	protected void setValueProblems(Collection<String> valueProblems) {
		this.valueProblems = valueProblems;
	}

	public Collection<String> getValueProblems() {
		return valueProblems;
	}

	public void computeValueProblems() {
		if (isValueNull()) {
			if (!isNullPermitted()) {
				valueProblems.add(getText("FieldIsMissing"));
			}
		} else {
			if (isValueEmpty()) {
				if (!isEmptyPermitted()) {
					valueProblems.add(getText("FieldIsEmpty"));
				}
			}
		}
	}

	public abstract boolean isValueEmpty();

	public abstract boolean isValueNull();

	public abstract void collectValue(HttpServletRequest request);

	public Form getForm() {
		return form;
	}

	public void setForm(Form form) {
		this.form = form;
	}

	protected User getUser() {
		return getForm().getUser();
	}

	protected String getText(String key) {
		return getForm().getText(key);
	}

	protected DaoFactory getDaoFactory() {
		return getForm().getDaoFactory();
	}

	protected UnitFactory getUnitFactory() {
		return getForm().getUnitFactory();
	}

	public boolean isNullPermitted() {
		return nullPermitted;
	}

	public void setNullPermitted(boolean nullPermitted) {
		this.nullPermitted = nullPermitted;
	}

	public boolean isEmptyPermitted() {
		return emptyPermitted;
	}

	public void setEmptyPermitted(boolean emptyPermitted) {
		this.emptyPermitted = emptyPermitted;
	}

}
