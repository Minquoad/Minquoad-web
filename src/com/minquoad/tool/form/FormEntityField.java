package com.minquoad.tool.form;

import javax.servlet.http.HttpServletRequest;

import com.minquoad.dao.interfaces.Dao;
import com.minquoad.dao.interfaces.DaoGetter;
import com.minquoad.framework.form.FormField;
import com.minquoad.tool.ImprovedHttpServlet;

public class FormEntityField<Entity> extends FormField<Entity> {

	private String stringValue;
	private Dao<Entity> dao;
	private DaoGetter<Entity> daoGetter;

	public FormEntityField(String name, Dao<Entity> dao) {
		this(name);
		this.dao = dao;
	}

	public FormEntityField(String name, DaoGetter<Entity> daoGetter) {
		this(name);
		this.daoGetter = daoGetter;
	}

	private FormEntityField(String name) {
		super(name);

		this.addBlockingChecker((form, thisField, value) -> {
			if (getValue() == null) {
				return form.getText("EntityNotFound");
			}
			return null;
		});
	}
	
	@Override
	public boolean isValueEmpty() {
		return getValueAsString().isEmpty();
	}

	@Override
	public boolean isValueNull() {
		return getValueAsString() == null;
	}

	@Override
	public void collectValue(HttpServletRequest request) {

		this.setStringValue(request.getParameter(getName()));

		if (!isValueNull() && !isValueEmpty()) {
			Dao<Entity> dao = this.dao;
			if (dao == null) {
				dao = this.daoGetter.getDao(ImprovedHttpServlet.getDaoFactory(request));
			}

			try {
				setValue(dao.getByPk(Long.parseLong(getValueAsString())));
				return;
			} catch (Exception e) {
			}
			try {
				setValue(dao.getByPk(Integer.parseInt(getValueAsString())));
				return;
			} catch (Exception e) {
			}
			try {
				setValue(dao.getByPk(getValueAsString()));
				return;
			} catch (Exception e) {
			}

			setValue(null);
		}
	}

	protected void setStringValue(String stringValue) {
		this.stringValue = stringValue;
	}

	protected String getValueAsString() {
		return stringValue;
	}

}
