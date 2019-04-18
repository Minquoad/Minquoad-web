package com.minquoad.frontComponent.form.field.valueChecker;

import com.minquoad.frontComponent.form.Form;
import com.minquoad.frontComponent.form.field.FormEntityField;

public interface EntityValueChecker<Entity> {

	public String getValueProblem(Form form, FormEntityField<Entity> field, Entity value);

}
