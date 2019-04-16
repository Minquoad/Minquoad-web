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

	@Override
	public boolean isAccessible(HttpServletRequest request) {
		return getUser(request) != null;
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		UserDao userDao = getDaoFactory(request).getUserDao();
		ConversationDao conversationDao = getDaoFactory(request).getConversationDao();

		List<Conversation> conversations = conversationDao.getUserConversations(getUser(request));

		Conversation selectedConversation = getEntityFromIdParameter(request, "conversationId", DaoFactory::getConversationDao);

		if (selectedConversation == null) {
			User user = getEntityFromIdParameter(request, "userId", DaoFactory::getUserDao);
			if (user != null && user != getUser(request)) {
				for (Conversation conversation : conversations) {
					if (conversation.getType() == Conversation.TYPE_MAIN_BETWEEN_TWO_USERS) {

						List<User> conversationUsers = userDao.getConversationUsers(conversation);
						for (User conversationUser : conversationUsers) {
							if (conversationUser == user) {
								selectedConversation = conversation;
							}
						}
					}
				}
				if (selectedConversation == null) {
					Conversation newConversation = new Conversation();
					newConversation.setTitle("");
					newConversation.setType(Conversation.TYPE_MAIN_BETWEEN_TWO_USERS);
					conversationDao.insert(newConversation);
					ConversationUnit conversationUnit = getUnitFactory(request).getConversationUnit();
					conversationUnit.giveAccessToConversation(user, newConversation);
					conversationUnit.giveAccessToConversation(getUser(request), newConversation);
					conversations.add(newConversation);
					selectedConversation = newConversation;
				}
			}
		}

		if (selectedConversation == null) {
			selectedConversation = conversations.get(0);
		}

		List<ConversationResume> conversationResumes = new LinkedList<ConversationResume>();

		for (Conversation conversation : conversations) {
			ConversationResume conversationResume = new ConversationResume();
			conversationResume.setConversation(conversation);
			conversationResume.setParticipants(userDao.getConversationUsers(conversation));
			conversationResumes.add(conversationResume);
		}

		request.setAttribute("conversationResumes", conversationResumes);
		request.setAttribute("selectedConversation", selectedConversation);

		forwardToView(request, response, VIEW_PATH);
	}
}
