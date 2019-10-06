package com.minquoad.servlet.conversation;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.minquoad.entity.Conversation;
import com.minquoad.entity.User;
import com.minquoad.frontComponent.form.conversation.ConversationAccessGrantingForm;
import com.minquoad.tool.ImprovedHttpServlet;
import com.minquoad.unit.ConversationUnit;

@WebServlet("/ConversationAccessGranting")
public class ConversationAccessGranting extends ImprovedHttpServlet {

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
		ConversationAccessGrantingForm form = new ConversationAccessGrantingForm(request);
		form.submit();

		if (form.isValide()) {

			ConversationUnit conversationUnit = getUnitFactory(request).getConversationUnit();
			Conversation conversation = form.getConversation();

			for (User target : form.getTargets()) {
				conversationUnit.giveAccessToConversation(target, conversation);
			}
			
		} else {
			response.setStatus(HttpServletResponse.SC_FORBIDDEN);
		}
	}

}
