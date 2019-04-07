package com.minquoad.frontComponent.form.field.valueChecker;

import com.minquoad.frontComponent.form.Form;
import com.minquoad.frontComponent.form.field.FormStringField;

public interface StringValueChecker {

	public String getValueProblem(Form form, FormStringField field, String value);

}
