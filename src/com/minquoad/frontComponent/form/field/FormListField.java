package com.minquoad.frontComponent.form.field;

import java.util.ArrayList;
import java.util.Collection;

public class FormListField extends FormStringField {

	private Collection<String> acceptedValues;

	public FormListField(String name) {
		super(name);
		acceptedValues = new ArrayList<String>();
	}

	@Override
	public String getFormatProblem(String value) {
		for (String acceptedValue : acceptedValues) {
			if (acceptedValue.equals(value)) {
				return null;
			}
		}
		return getText("ValueNotInAcceptedValues");
	}

	public void addPossibleValue(String value) {
		acceptedValues.add(value);
	}

	public void addPossibleValues(Collection<String> values) {
		acceptedValues.addAll(values);
	}

}
