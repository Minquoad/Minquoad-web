package com.minquoad.frontComponent.form.field;

import java.util.Collection;
import java.util.HashSet;

public class FormListField extends FormStringField {

	private Collection<String> acceptedValues;

	public FormListField(String name) {
		super(name);
		acceptedValues = new HashSet<String>();
		this.addBlockingChecker((form, thisField, value) -> {
			if (acceptedValues.contains(value)) {
				return null;
			}
			return form.getText("ValueNotInAcceptedValues");
		});
	}

	public void addPossibleValue(String value) {
		acceptedValues.add(value);
	}

	public void addPossibleValues(Collection<String> values) {
		acceptedValues.addAll(values);
	}

}
