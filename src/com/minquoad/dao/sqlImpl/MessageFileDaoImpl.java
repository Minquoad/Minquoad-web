package com.minquoad.dao.sqlImpl;

import com.minquoad.dao.interfaces.MessageFileDao;
import com.minquoad.entity.file.MessageFile;
import com.minquoad.entity.file.ProtectedFile;

public class MessageFileDaoImpl extends DaoImpl<MessageFile> implements MessageFileDao {

	public MessageFileDaoImpl(DaoFactoryImpl daoFactory) {
		super(daoFactory);
	}

	@Override
	protected MessageFile instantiateBlank() {
		return new MessageFile();
	}

	@Override
	protected void initSuperClass() {
		setSuperClass(ProtectedFile.class);
	}

	@Override
	protected void initSubClasses() {
	}

	@Override
	protected void initEntityMembers() {
		this.addBooleanEntityMember("image", MessageFile::isImage, MessageFile::setImage);
	}

}
