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
		MessageAdditionForm form = new MessageAdditionForm(request);

		User user = getUser(request);
		Conversation conversation = form.getConversation();

		return user != null && conversation != null
				&& getUnitFactory(request).getConversationUnit().hasUserConversationAccess(user, conversation);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		MessageAdditionForm form = new MessageAdditionForm(request);

		if (form.isValide()) {
			String text = form.getFieldValueAsString(MessageAdditionForm.textKey);
			Conversation conversation = form.getConversation();

			Message message = new Message();
			message.setText(text);
			message.setUser(getUser(request));
			message.setConversation(conversation);
			message.setInstant(Instant.now());
			getDaoFactory(request).getMessageDao().persist(message);
		}
	}

}
