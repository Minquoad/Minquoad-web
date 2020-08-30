package com.minquoad.servlet.conversation;

import java.io.IOException;
import java.time.Instant;
import java.util.Collection;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.minquoad.entity.Conversation;
import com.minquoad.entity.Message;
import com.minquoad.entity.User;
import com.minquoad.entity.file.MessageFile;
import com.minquoad.framework.form.FormFileField;
import com.minquoad.frontComponent.form.conversation.MessageAdditionForm;
import com.minquoad.tool.ImprovedHttpServlet;

@WebServlet("/MessageAddition")
@MultipartConfig(fileSizeThreshold = 1024 * 1024 * 1, // 1 MB
		maxFileSize = 1024 * 1024 * 10, // 10 MB
		maxRequestSize = 1024 * 1024 * 15 // 15 MB
)
public class MessageAddition extends ImprovedHttpServlet {

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
		MessageAdditionForm form = new MessageAdditionForm(request);
		form.submit();

		if (form.isValide()) {
			Conversation conversation = form.getConversation();

			MessageFile messageFile = null;

			FormFileField fileField = form.getFileField();

			if (!fileField.isValueEmpty()) {
				messageFile = new MessageFile();
				messageFile.setOriginalName(fileField.getOriginalFileName());
				messageFile.setImage(fileField.isImage());
				messageFile.collectFromPart(fileField.getValue(), getServletContext());
				getDaoFactory(request).getMessageFileDao().persist(messageFile);
			}

			Message message = new Message();
			message.setText(form.getText());
			message.setUser(getUser(request));
			message.setConversation(conversation);
			message.setInstant(Instant.now());
			message.setMessageFile(messageFile);
			getDaoFactory(request).getMessageDao().persist(message);
	
			conversation.setLastMessage(message);
			getDaoFactory(request).getConversationDao().persist(conversation);

			Collection<User> conversationUsers = getDaoFactory(request).getUserDao().getConversationUsers(conversation);

			sendJsonToClientsWithRole(
					message.toJson(),
					conversationUsers,
					"messageAddition");

		} else {
			response.setStatus(HttpServletResponse.SC_FORBIDDEN);
		}
	}

}
