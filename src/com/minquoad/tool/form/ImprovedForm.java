package com.minquoad.tool.form;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import com.minquoad.dao.interfaces.DaoFactory;
import com.minquoad.entity.User;
import com.minquoad.framework.form.Form;
import com.minquoad.tool.ImprovedHttpServlet;
import com.minquoad.tool.InternationalizationTool;
import com.minquoad.unit.UnitFactory;

public class ImprovedForm extends Form {

	public static final String formResourceBundleName = "resources.Form";

	public ImprovedForm(HttpServletRequest request) {
		super(request);
	}

	@Override
	public String getText(String key, Object... args) {
		return InternationalizationTool.getText(
				key,
				formResourceBundleName,
				getLocale(),
				args);
	}

	public DaoFactory getDaoFactory() {
		return ImprovedHttpServlet.getDaoFactory(getRequest());
	}

	public UnitFactory getUnitFactory() {
		return ImprovedHttpServlet.getUnitFactory(getRequest());
	}

	public User getUser() {
		return ImprovedHttpServlet.getUser(getRequest());
	}

	public Locale getLocale() {
		return ImprovedHttpServlet.getLocale(getRequest());
	}

}
