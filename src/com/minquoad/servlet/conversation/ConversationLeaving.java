package com.minquoad.servlet.conversation;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.minquoad.dao.interfaces.ConversationAccessDao;
import com.minquoad.entity.Conversation;
import com.minquoad.entity.ConversationAccess;
import com.minquoad.frontComponent.form.conversation.ConversationLeavingForm;
import com.minquoad.tool.ImprovedHttpServlet;

@WebServlet("/ConversationLeaving")
public class ConversationLeaving extends ImprovedHttpServlet {

	@Override
	public boolean isFullPage() {
		return false;
	}

	@Override
	public boolean isAccessible(HttpServletRequest request) {
		return getUser(request) != null;
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		ConversationLeavingForm form = new ConversationLeavingForm(request);
		form.submit();

		if (form.isValide()) {
			Conversation conversation = form.getConversation();

			ConversationAccessDao conversationAccessDao = getDaoFactory(request).getConversationAccessDao();
			
			ConversationAccess conversationAccess = conversationAccessDao.getConversationAccess(getUser(request), conversation);
			
			conversationAccessDao.delete(conversationAccess);

			response.setStatus(HttpServletResponse.SC_NO_CONTENT);

		} else {
			response.setStatus(HttpServletResponse.SC_FORBIDDEN);
		}
	}

}
