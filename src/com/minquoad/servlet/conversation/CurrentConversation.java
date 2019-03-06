package com.minquoad.servlet.conversation;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.minquoad.dao.interfaces.ConversationAccessDao;
import com.minquoad.dao.interfaces.DaoFactory;
import com.minquoad.entity.Conversation;
import com.minquoad.entity.ConversationAccess;
import com.minquoad.entity.Message;
import com.minquoad.entity.User;
import com.minquoad.tool.http.ImprovedHttpServlet;

@WebServlet("/CurrentConversation")
public class CurrentConversation extends ImprovedHttpServlet {

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

			request.setAttribute("conversation", conversation);

			List<Message> messages = getUnitFactory(request).getConversationUnit().getConversationMessagesInOrder(conversation);

			if (messages.size() != 0) {
				ConversationAccessDao conversationAccessDao = getDaoFactory(request).getConversationAccessDao();
				ConversationAccess conversationAccess = conversationAccessDao.getConversationAccess(getUser(request), conversation);
				conversationAccess.setLastSeenMessage(messages.get(messages.size() - 1));
				conversationAccessDao.update(conversationAccess);
			}

			request.setAttribute("messages", messages);

			this.getServletContext().getRequestDispatcher("/WEB-INF/subPage/currentConversation.jsp")
					.include(request, response);

		} catch (Exception e) {
		}
	}

}
