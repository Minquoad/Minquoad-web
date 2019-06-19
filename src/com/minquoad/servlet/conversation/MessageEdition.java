package com.minquoad.servlet.conversation;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.minquoad.entity.Message;
import com.minquoad.entity.User;
import com.minquoad.frontComponent.form.impl.conversation.MessageEditionForm;
import com.minquoad.frontComponent.json.MessageJson;
import com.minquoad.tool.http.ImprovedHttpServlet;

@WebServlet("/MessageEdition")
public class MessageEdition extends ImprovedHttpServlet {

	@Override
	public boolean isFullPage() {
		return false;
	}

	@Override
	public boolean isAccessible(HttpServletRequest request) {
		return getUser(request) != null;
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		initForms(request);
		MessageEditionForm form = (MessageEditionForm) request.getAttribute("form");
		form.submit();

		if (form.isValide()) {
			Message message = form.getMessage();
			message.setEditedText(form.getNewText());
			getDaoFactory(request).getMessageDao().persist(message);

			String text = new MessageJson(message, "MessageEdition").toJson();
			List<User> conversationUsers = getDaoFactory(request).getUserDao().getConversationUsers(message.getConversation());

			sendTextToClients(
					text,
					(endpoint) -> {
						return "conversationUpdating".equals(endpoint.getRole())
								&& conversationUsers.contains(endpoint.getUser(getDaoFactory(request)));
					});

		} else {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		}
	}

	private void initForms(HttpServletRequest request) {
		request.setAttribute("form", new MessageEditionForm(request));
	}

}
