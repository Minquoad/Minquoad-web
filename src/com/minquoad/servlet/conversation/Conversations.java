package com.minquoad.servlet.conversation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.minquoad.dao.interfaces.ConversationDao;
import com.minquoad.dao.interfaces.DaoFactory;
import com.minquoad.dao.interfaces.UserDao;
import com.minquoad.entity.Conversation;
import com.minquoad.entity.Message;
import com.minquoad.entity.User;
import com.minquoad.frontComponent.ConversationResume;
import com.minquoad.tool.ImprovedHttpServlet;
import com.minquoad.unit.ConversationUnit;

@WebServlet("/Conversations")
public class Conversations extends ImprovedHttpServlet {

	public static final String VIEW_PATH = "/WEB-INF/page/conversation/conversations.jsp";

	public static final String CONVERSATION_SUB_PAGE_KEY = "conversationSubPageKey";
	public static final String TARGET_USER_ID_KEY = "targetUserId";

	@Override
	public boolean isAccessible(HttpServletRequest request) {
		return getUser(request) != null;
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		UserDao userDao = getDaoFactory(request).getUserDao();
		ConversationDao conversationDao = getDaoFactory(request).getConversationDao();

		Collection<Conversation> conversations = conversationDao.getUserConversations(getUser(request));

		User targetUser = getEntityFromPkParameter(request, TARGET_USER_ID_KEY, DaoFactory::getUserDao);
		if (targetUser != null && targetUser != getUser(request)) {
			for (Conversation conversation : conversations) {
				if (conversation.getType() == Conversation.TYPE_MAIN_BETWEEN_TWO_USERS) {
					Collection<User> conversationUsers = userDao.getConversationUsers(conversation);
					for (User conversationUser : conversationUsers) {
						if (conversationUser == targetUser) {
							response.sendRedirect(request.getRequestURI() + "?" + CONVERSATION_SUB_PAGE_KEY + "=" + conversation.getId());
							return;
						}
					}
				}
			}

			Conversation conversation = new Conversation();
			conversation.setTitle("");
			conversation.setType(Conversation.TYPE_MAIN_BETWEEN_TWO_USERS);
			conversationDao.persist(conversation);
			ConversationUnit conversationUnit = getUnitFactory(request).getConversationUnit();
			conversationUnit.giveAccessToConversation(targetUser, conversation);
			conversationUnit.giveAccessToConversation(getUser(request), conversation);
			response.sendRedirect(request.getRequestURI() + "?" + CONVERSATION_SUB_PAGE_KEY + "=" + conversation.getId());
			return;
		}

		List<ConversationResume> conversationResumes = new ArrayList<ConversationResume>();

		for (Conversation conversation : conversations) {
			ConversationResume conversationResume = new ConversationResume();
			conversationResume.setConversation(conversation);
			conversationResume.setParticipants(userDao.getConversationUsers(conversation));
			conversationResumes.add(conversationResume);
		}

		conversationResumes.sort((compared, reference) -> {
			Message comparedMessage = compared.getConversation().getLastMessage();
			Message referenceMessage = reference.getConversation().getLastMessage();
			if (comparedMessage == null || referenceMessage == null) {
				if (comparedMessage != null) {
					return 1;
				}
				if (referenceMessage != null) {
					return -1;
				}
				return 0;
			}
			return referenceMessage.getInstant().compareTo(comparedMessage.getInstant());
		});

		request.setAttribute("conversationResumes", conversationResumes);

		forwardToView(request, response, VIEW_PATH);
	}
}
