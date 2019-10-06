package com.minquoad.framework.form;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;

import javax.servlet.http.HttpServletRequest;

public class FormMultipleValuePickerField extends FormField<Collection<String>> {

	private Collection<String> acceptedValues;

	public FormMultipleValuePickerField(String name) {
		super(name);

		acceptedValues = null;

		this.addBlockingChecker((form, thisField, value) -> {
			if (acceptedValues == null || acceptedValues.containsAll(value)) {
				return null;
			}
			return form.getText("ValueNotInAcceptedValues");
		});
	}

	@Override
	public void collectValue(HttpServletRequest request) {
		String[] parameterValues = request.getParameterValues(getName());
		if (parameterValues == null) {
			this.setValue(new ArrayList<String>());
		} else {
			this.setValue(Arrays.asList(parameterValues));
		}
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

	@Override
	public boolean isValueEmpty() {
		return getValue().isEmpty();
	}

	@Override
	public boolean isValueNull() {
		return false;
	}

}
