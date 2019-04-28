package com.minquoad.frontComponent.form;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.minquoad.dao.interfaces.DaoFactory;
import com.minquoad.entity.User;
import com.minquoad.frontComponent.form.field.FormField;
import com.minquoad.tool.InternationalizationTool;
import com.minquoad.tool.http.ImprovedHttpServlet;
import com.minquoad.unit.UnitFactory;

public class Form {

	private Map<String, FormField> fileds;
	private List<FormField> filedsInOrder;
	private HttpServletRequest request;
	private boolean submitted;

	public Form(HttpServletRequest request) {
		fileds = new HashMap<String, FormField>();
		filedsInOrder = new ArrayList<FormField>();
		setRequest(request);
		setSubmitted(false);
		this.build();
	}

	protected void build() {
	}

	public boolean isValide() {
		for (FormField field : getFiledsInOrder()) {
			if (!field.isValid()) {
				return false;
			}
		}
		return true;
	}

	public Map<String, FormField> getFields() {
		return fileds;
	}

	public List<String> getFieldValueProblems(String name) {
		FormField field = fileds.get(name);
		if (field == null) {
			new Exception("Field with name  " + name + " not existing in the form.").printStackTrace();
			return null;
		} else {
			return field.getValueProblems();
		}
	}

	public void addField(FormField field) {
		field.setForm(this);
		fileds.put(field.getName(), field);
		getFiledsInOrder().add(field);
	}

	public FormField getField(String name) {
		return fileds.get(name);
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	private void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	public void submit() {
		setSubmitted(true);
		for (FormField field : getFiledsInOrder()) {
			field.collectValue(request);
			field.computeValueProblems();
		}
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

	public String getText(String key) {
		return InternationalizationTool.getText(key, "resources.Form", ImprovedHttpServlet.getLocale(getRequest()));
	}

	public boolean isSubmitted() {
		return submitted;
	}

	private void setSubmitted(boolean submitted) {
		this.submitted = submitted;
	}

	private List<FormField> getFiledsInOrder() {
		return filedsInOrder;
	}

}
