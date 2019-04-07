package com.minquoad.servlet;

import java.io.IOException;
import java.time.Instant;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.minquoad.entity.ImprovementSuggestion;
import com.minquoad.frontComponent.form.ImprovementSuggestionAdditionForm;
import com.minquoad.tool.http.ImprovedHttpServlet;

@WebServlet("/ImprovementSuggestionAddition")
public class ImprovementSuggestionAddition extends ImprovedHttpServlet {

	@Override
	public boolean isFullPage() {
		return false;
	}

	@Override
	public boolean isAccessible(HttpServletRequest request) {
		return getUser(request) != null;
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		ImprovementSuggestionAdditionForm form = new ImprovementSuggestionAdditionForm(request);

		if (form.isValide()) {
			ImprovementSuggestion suggestion = new ImprovementSuggestion();
			suggestion.setInstant(Instant.now());
			suggestion.setText(form.getFieldValueAsString(ImprovementSuggestionAdditionForm.textKey));
			suggestion.setUser(getUser(request));

			getDaoFactory(request).getImprovementSuggestionDao().persist(suggestion);
		}
	}

}
