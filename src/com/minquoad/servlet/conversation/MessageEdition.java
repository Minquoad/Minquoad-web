package com.minquoad.servlet.conversation;

import java.io.IOException;
import java.util.Collection;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.minquoad.entity.Message;
import com.minquoad.entity.User;
import com.minquoad.frontComponent.form.impl.conversation.MessageEditionForm;
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
		MessageEditionForm form = new MessageEditionForm(request);
		form.submit();

		if (form.isValide()) {
			Message message = form.getMessage();
			message.setEditedText(form.getNewText());
			getDaoFactory(request).getMessageDao().persist(message);

			Collection<User> conversationUsers = getDaoFactory(request).getUserDao().getConversationUsers(message.getConversation());

			sendJsonToClientsWithRole(
					message.toJson(),
					conversationUsers,
					"messageEdition");

		} else {
			response.setStatus(HttpServletResponse.SC_FORBIDDEN);
		}
	}

}
