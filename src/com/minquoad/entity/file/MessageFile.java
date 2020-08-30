package com.minquoad.entity.file;

import com.minquoad.dao.interfaces.DaoFactory;
import com.minquoad.entity.User;
import com.minquoad.unit.UnitFactory;

public class MessageFile extends ProtectedFile {

	private Boolean image;

	@Override
	public boolean isDownloadableForUser(User user, DaoFactory daoFactory, UnitFactory unitFactory) {

		return super.isDownloadableForUser(user, daoFactory, unitFactory)
				&& unitFactory.getConversationUnit().hasUserConversationAccess(
						user,
						daoFactory.getMessageDao().getMessageFileMessage(this).getConversation());
	}

	public Boolean isImage() {
		return image;
	}

	public void setImage(Boolean image) {
		this.image = image;
	}

}
