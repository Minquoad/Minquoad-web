package com.minquoad.servlet.conversation;

import java.io.IOException;
import java.time.Instant;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.minquoad.entity.Conversation;
import com.minquoad.entity.Message;
import com.minquoad.entity.User;
import com.minquoad.frontComponent.form.conversation.MessageAdditionForm;
import com.minquoad.tool.http.ImprovedHttpServlet;

@WebServlet("/MessageAddition")
public class MessageAddition extends ImprovedHttpServlet {

	@Override
	public boolean isFullPage() {
		return false;
	}

	@Override
	public boolean isAccessible(HttpServletRequest request) {
		initForms(request);

		MessageAdditionForm form = (MessageAdditionForm) request.getAttribute("form");
		form.submit();

		User user = getUser(request);
		Conversation conversation = form.getConversation();

		return user != null && conversation != null
				&& getUnitFactory(request).getConversationUnit().hasUserConversationAccess(user, conversation);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		MessageAdditionForm form = (MessageAdditionForm) request.getAttribute("form");
		form.submit();

		if (form.isValide()) {
			Message message = new Message();
			message.setText(form.getText());
			message.setUser(getUser(request));
			message.setConversation(form.getConversation());
			message.setInstant(Instant.now());
			getDaoFactory(request).getMessageDao().persist(message);
		}
	}

	private void initForms(HttpServletRequest request) {
		request.setAttribute("form", new MessageAdditionForm(request));
	}

}
