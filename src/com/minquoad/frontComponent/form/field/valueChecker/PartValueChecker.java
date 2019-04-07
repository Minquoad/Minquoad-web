package com.minquoad.frontComponent.form.field.valueChecker;

import javax.servlet.http.Part;

import com.minquoad.frontComponent.form.Form;
import com.minquoad.frontComponent.form.field.FormFileField;

public interface PartValueChecker {

	public String getValueProblem(Form form, FormFileField field, Part value);

}
