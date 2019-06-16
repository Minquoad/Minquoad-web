package com.minquoad.servlet.conversation;

import java.io.IOException;
import java.time.Instant;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.minquoad.entity.Message;
import com.minquoad.entity.User;
import com.minquoad.frontComponent.form.impl.conversation.MessageAdditionForm;
import com.minquoad.frontComponent.json.MessageAdditionJson;
import com.minquoad.tool.http.ImprovedHttpServlet;

@WebServlet("/MessageAddition")
@MultipartConfig(fileSizeThreshold = 1024 * 1024 * 1, // 1 MB
maxFileSize = 1024 * 1024 * 10, // 10 MB
maxRequestSize = 1024 * 1024 * 15 // 15 MB
)
public class MessageAddition extends ImprovedHttpServlet {

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
		MessageAdditionForm form = (MessageAdditionForm) request.getAttribute("form");
		form.submit();

		if (form.isValide()) {
			Message message = new Message();
			message.setText(form.getText());
			message.setUser(getUser(request));
			message.setConversation(form.getConversation());
			message.setInstant(Instant.now());
			getDaoFactory(request).getMessageDao().persist(message);

			String text = new MessageAdditionJson(message).toJson();
			List<User> conversationUsers = getDaoFactory(request).getUserDao().getConversationUsers(form.getConversation());

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
		request.setAttribute("form", new MessageAdditionForm(request));
	}

}
