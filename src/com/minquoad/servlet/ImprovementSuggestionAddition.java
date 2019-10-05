package com.minquoad.servlet;

import java.io.IOException;
import java.time.Instant;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.minquoad.entity.ImprovementSuggestion;
import com.minquoad.frontComponent.form.ImprovementSuggestionAdditionForm;
import com.minquoad.tool.ImprovedHttpServlet;

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

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		ImprovementSuggestionAdditionForm form = new ImprovementSuggestionAdditionForm(request);
		form.submit();

		if (form.isValide()) {
			ImprovementSuggestion suggestion = new ImprovementSuggestion();
			suggestion.setInstant(Instant.now());
			suggestion.setText(form.getText());
			suggestion.setUser(getUser(request));

			getDaoFactory(request).getImprovementSuggestionDao().persist(suggestion);

		} else {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		}
	}

}
