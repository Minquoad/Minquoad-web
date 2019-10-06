package com.minquoad.framework.form;

import java.util.Collection;
import java.util.HashSet;

public class FormSingleValuePickerField extends FormStringField {

	private Collection<String> acceptedValues;

	public FormSingleValuePickerField(String name) {
		super(name);

		acceptedValues = new HashSet<String>();

		this.addBlockingChecker((form, thisField, value) -> {
			if (acceptedValues == null || acceptedValues.contains(value)) {
				return null;
			}
			return form.getText("ValueNotInAcceptedValues");
		});
	}

	public Collection<String> getAcceptedValues() {
		return acceptedValues;
	}

	public void setAcceptedValues(Collection<String> acceptedValues) {
		this.acceptedValues = acceptedValues;
	}

	public void addAcceptedValue(String value) {
		if (acceptedValues == null) {
			acceptedValues = new HashSet<String>();
		}
		acceptedValues.add(value);
	}

	public void addAcceptedValues(Collection<String> values) {
		if (acceptedValues == null) {
			acceptedValues = new HashSet<String>();
		}
		acceptedValues.addAll(values);
	}

}
