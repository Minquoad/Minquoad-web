package com.minquoad.servlet.conversation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.minquoad.entity.Conversation;
import com.minquoad.entity.User;
import com.minquoad.frontComponent.ConversationResume;
import com.minquoad.frontComponent.form.conversation.ConversationCreationForm;
import com.minquoad.tool.ImprovedHttpServlet;
import com.minquoad.unit.ConversationUnit;

@WebServlet("/ConversationCreation")
public class ConversationCreation extends ImprovedHttpServlet {

	public static final String VIEW_PATH = "/WEB-INF/page/conversation/conversationCreation.jsp";

	@Override
	public boolean isFullPage() {
		return false;
	}

	@Override
	public boolean isAccessible(HttpServletRequest request) {
		return getUser(request) != null;
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		initForms(request);
		forwardToView(request, response, VIEW_PATH);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		initForms(request);

		ConversationCreationForm form = (ConversationCreationForm) request.getAttribute(FORM_KEY);

		form.submit();
		if (form.isValide()) {
			Conversation conversation = new Conversation();
			conversation.setTitle(form.getTitle());
			conversation.setType(Conversation.TYPE_CREATED_BY_USER);
			getDaoFactory(request).getConversationDao().persist(conversation);
			ConversationUnit conversationUnit = getUnitFactory(request).getConversationUnit();
			conversationUnit.giveAccessToConversation(getUser(request), conversation, true);

			Collection<User> conversationUsers = new ArrayList<User>(1);
			conversationUsers.add(getUser(request));

			ConversationResume conversationResume = new ConversationResume();
			conversationResume.setConversation(conversation);
			conversationResume.setParticipants(conversationUsers);

			sendJsonToClientsWithRole(
					conversationResume.toJson(),
					conversationUsers,
					"conversationAddition");

			ObjectNode json = JsonNodeFactory.instance.objectNode();
			json.put("id", Long.toString(conversation.getId()));

			respondJson(response, json);
			response.setStatus(HttpServletResponse.SC_CREATED);

		} else {
			forwardToView(request, response, VIEW_PATH);
		}
	}

	private void initForms(HttpServletRequest request) {
		request.setAttribute(FORM_KEY, new ConversationCreationForm(request));
	}

}
