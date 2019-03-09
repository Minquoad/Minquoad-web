package com.minquoad.servlet.conversation;

import java.io.IOException;
import java.time.Instant;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.minquoad.dao.interfaces.DaoFactory;
import com.minquoad.entity.Conversation;
import com.minquoad.entity.Message;
import com.minquoad.entity.User;
import com.minquoad.tool.http.ImprovedHttpServlet;

@WebServlet("/MessageAddition")
public class MessageAddition extends ImprovedHttpServlet {

	@Override
	public boolean isFullPage() {
		return false;
	}

	@Override
	public boolean isAccessible(HttpServletRequest request) {
		User user = getUser(request);
		Conversation conversation = getEntityFromIdParameter(request, "conversationId", DaoFactory::getConversationDao);

		return user != null && conversation != null
				&& getUnitFactory(request).getConversationUnit().hasUserConversationAccess(user, conversation);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		String text = request.getParameter("text");
		Conversation conversation = getEntityFromIdParameter(request, "conversationId", DaoFactory::getConversationDao);

		if (conversation != null && text != null) {
			text = text.trim();
			if (!text.equals("")) {
				Message message = new Message();
				message.setText(text);
				message.setUser(getUser(request));
				message.setConversation(conversation);
				message.setInstant(Instant.now());
				getDaoFactory(request).getMessageDao().insert(message);
			}
		}
	}

}
