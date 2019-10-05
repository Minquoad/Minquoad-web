package com.minquoad.frontComponent.form.field;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.minquoad.frontComponent.form.Form;

public abstract class FormField<Value> {

	public interface Checker<Value> {
		public String getValueProblem(Form form, FormField<Value> field, Value value);
	}

	private Form form;
	private String name;
	private Value value;
	private boolean nullPermitted;
	private boolean emptyPermitted;
	private List<Checker<Value>> blockingCheckers;
	private Collection<Checker<Value>> nonBlockingCheckers;

	private Collection<String> valueProblems;

	public FormField(String name) {
		this.name = name;
		setNullPermitted(false);
		setEmptyPermitted(true);
		blockingCheckers = new ArrayList<Checker<Value>>();
		nonBlockingCheckers = new ArrayList<Checker<Value>>();
		valueProblems = new LinkedList<String>();
	}

	public boolean isValid() {
		return getValueProblems().size() == 0;
	}

	public Collection<String> getValueProblems() {
		return valueProblems;
	}

	public void computeValueProblems() {
		if (isValueNull()) {
			if (!isNullPermitted()) {
				getValueProblems().add(getForm().getText("FieldIsMissing"));
			}
		} else {
			if (isValueEmpty()) {
				if (!isEmptyPermitted()) {
					getValueProblems().add(getForm().getText("FieldIsEmpty"));
				}
			} else {
				for (Checker<Value> checker : blockingCheckers) {
					String valueProblem = checker.getValueProblem(getForm(), this, getValue());
					if (valueProblem != null) {
						getValueProblems().add(valueProblem);
						return;
					}
				}
				for (Checker<Value> checker : nonBlockingCheckers) {
					String valueProblem = checker.getValueProblem(getForm(), this, getValue());
					if (valueProblem != null) {
						getValueProblems().add(valueProblem);
					}
				}
			}
		}
	}

	public abstract boolean isValueEmpty();

	public abstract boolean isValueNull();

	public abstract void collectValue(HttpServletRequest request);

	public void addNonBlockingChecker(Checker<Value> checker) {
		this.nonBlockingCheckers.add(checker);
	}

	public void addBlockingChecker(Checker<Value> checker) {
		this.blockingCheckers.add(checker);
	}

	public Value getValue() {
		return value;
	}

	public void setValue(Value value) {
		this.value = value;
	}

	public Form getForm() {
		return form;
	}

	public void setForm(Form form) {
		this.form = form;
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

	public String getName() {
		return name;
	}

}
