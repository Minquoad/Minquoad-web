package com.minquoad.servlet.conversation;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.minquoad.dao.interfaces.ConversationDao;
import com.minquoad.dao.interfaces.DaoFactory;
import com.minquoad.dao.interfaces.UserDao;
import com.minquoad.entity.Conversation;
import com.minquoad.entity.User;
import com.minquoad.frontComponent.ConversationResume;
import com.minquoad.tool.http.ImprovedHttpServlet;
import com.minquoad.unit.impl.ConversationUnit;

@WebServlet("/Conversations")
public class Conversations extends ImprovedHttpServlet {

	public static final String VIEW_PATH = "/WEB-INF/page/conversation/conversations.jsp";

	public static final String CONVERSATION_ID_KEY = "conversationId";
	public static final String USER_ID_KEY = "userId";

	@Override
	public boolean isAccessible(HttpServletRequest request) {
		return getUser(request) != null;
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		UserDao userDao = getDaoFactory(request).getUserDao();
		ConversationDao conversationDao = getDaoFactory(request).getConversationDao();

		List<Conversation> conversations = conversationDao.getUserConversations(getUser(request));

		User targetUser = getEntityFromIdParameter(request, USER_ID_KEY, DaoFactory::getUserDao);
		if (targetUser != null && targetUser != getUser(request)) {
			for (Conversation conversation : conversations) {
				if (conversation.getType() == Conversation.TYPE_MAIN_BETWEEN_TWO_USERS) {
					List<User> conversationUsers = userDao.getConversationUsers(conversation);
					for (User conversationUser : conversationUsers) {
						if (conversationUser == targetUser) {
							response.sendRedirect(request.getRequestURI() + "?" + CONVERSATION_ID_KEY + "=" + conversation.getId());
							return;
						}
					}
				}
			}

			Conversation conversation = new Conversation();
			conversation.setTitle("");
			conversation.setType(Conversation.TYPE_MAIN_BETWEEN_TWO_USERS);
			conversationDao.insert(conversation);
			ConversationUnit conversationUnit = getUnitFactory(request).getConversationUnit();
			conversationUnit.giveAccessToConversation(targetUser, conversation);
			conversationUnit.giveAccessToConversation(getUser(request), conversation);
			response.sendRedirect(request.getRequestURI() + "?" + CONVERSATION_ID_KEY + "=" + conversation.getId());
			return;
		}

		List<ConversationResume> conversationResumes = new LinkedList<ConversationResume>();

		for (Conversation conversation : conversations) {
			ConversationResume conversationResume = new ConversationResume();
			conversationResume.setConversation(conversation);
			conversationResume.setParticipants(userDao.getConversationUsers(conversation));
			conversationResumes.add(conversationResume);
		}

		request.setAttribute("conversationResumes", conversationResumes);

		forwardToView(request, response, VIEW_PATH);
	}
}
