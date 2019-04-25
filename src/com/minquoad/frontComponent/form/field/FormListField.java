package com.minquoad.frontComponent.form.field;

import java.util.ArrayList;
import java.util.List;

public class FormListField extends FormStringField {

	private List<String> acceptedValues;
	
	public FormListField(String name) {
		super(name);
		acceptedValues = new ArrayList<String>();
	}

	@Override
	public String getFormatProblem(String value) {
		for (String acceptedValue :acceptedValues) {
			if (acceptedValue.equals(value)) {
				return null;
			}
		}
		return "This value is not acceptable.";
	}

	public void addPossibleValue(String value) {
		acceptedValues.add(value);
	}

}
