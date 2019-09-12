package com.minquoad.servlet.conversation;

import java.io.IOException;
import java.util.Collection;
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

	public static final String VIEW_PATH = "/WEB-INF/page/conversation/currentConversation.jsp";

	public static final String CONVERSATION_ID_KEY = "conversationId";

	@Override
	public boolean isFullPage() {
		return false;
	}

	@Override
	public boolean isAccessible(HttpServletRequest request) {
		User user = getUser(request);
		Conversation conversation = getEntityFromPkParameter(request, CONVERSATION_ID_KEY, DaoFactory::getConversationDao);

		return user != null && conversation != null
				&& getUnitFactory(request).getConversationUnit().hasUserConversationAccess(user, conversation);
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		Conversation conversation = getEntityFromPkParameter(request, CONVERSATION_ID_KEY, DaoFactory::getConversationDao);

		request.setAttribute("conversation", conversation);

		List<Message> messages = getUnitFactory(request).getConversationUnit().getConversationMessagesInOrder(conversation);

		ConversationAccessDao conversationAccessDao = getDaoFactory(request).getConversationAccessDao();
		ConversationAccess conversationAccess = conversationAccessDao.getConversationAccess(getUser(request), conversation);
		
		if (messages.size() != 0) {
			Message lastMessage = messages.get(messages.size() - 1);
			if (lastMessage != conversationAccess.getLastSeenMessage()) {
				conversationAccess.setLastSeenMessage(messages.get(messages.size() - 1));
				conversationAccessDao.persist(conversationAccess);
			}
		}

		Collection<User> conversationUsers = getDaoFactory(request).getUserDao().getConversationUsers(conversation);

		request.setAttribute("messages", messages);
		request.setAttribute("participants", conversationUsers);
		request.setAttribute("conversationAccess",  conversationAccess);

		forwardToView(request, response, VIEW_PATH);

	}

}
