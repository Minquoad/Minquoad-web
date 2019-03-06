package com.minquoad.servlet.conversation;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.minquoad.dao.interfaces.DaoFactory;
import com.minquoad.entity.Conversation;
import com.minquoad.entity.ConversationAccess;
import com.minquoad.entity.Message;
import com.minquoad.entity.User;
import com.minquoad.tool.http.ImprovedHttpServlet;

@WebServlet("/UnseenMessages")
public class UnseenMessages extends ImprovedHttpServlet {

	@Override
	public boolean isFullPage() {
		return false;
	}

	@Override
	public boolean isAccessible(HttpServletRequest request) {
		User user = getUser(request);
		Conversation conversation = getEntityFromIdParameter(request, "conversationId", DaoFactory::getConversationDao);

		return getUser(request) != null
				&& getUnitFactory(request).getConversationUnit().hasUserConversationAccess(user, conversation);
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		try {
			Conversation conversation = getEntityFromIdParameter(request, "conversationId", DaoFactory::getConversationDao);

			List<Message> messages = getUnitFactory(request).getConversationUnit().getConversationMessagesInOrder(conversation);

			List<Message> unseenMessages = null;

			ConversationAccess conversationAccess = getDaoFactory(request).getConversationAccessDao()
					.getConversationAccess(getUser(request), conversation);

			Message lastSeenMessage = conversationAccess.getLastSeenMessage();
			
			if (lastSeenMessage == null) {
				unseenMessages = messages;

			} else {
				unseenMessages = new LinkedList<Message>();
				boolean lastSeenMessageFonded = false;
				for (Message message : messages) {
					if (lastSeenMessageFonded) {
						unseenMessages.add(message);
					} else {
						lastSeenMessageFonded = message == lastSeenMessage;
					}
				}
			}

			if (unseenMessages.size() != 0) {
				conversationAccess.setLastSeenMessage(unseenMessages.get(unseenMessages.size() - 1));
				getDaoFactory(request).getConversationAccessDao().update(conversationAccess);
			}

			request.setAttribute("messages", unseenMessages);

			this.getServletContext().getRequestDispatcher("/WEB-INF/subPage/unseenMessages.jsp")
					.forward(request, response);

		} catch (Exception e) {
		}
	}

}
