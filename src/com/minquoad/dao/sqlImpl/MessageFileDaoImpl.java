package com.minquoad.dao.sqlImpl;

import com.minquoad.dao.interfaces.MessageFileDao;
import com.minquoad.entity.file.MessageFile;
import com.minquoad.framework.dao.DaoImpl;

public class MessageFileDaoImpl extends ImprovedDaoImpl<MessageFile> implements MessageFileDao {

	public MessageFileDaoImpl(DaoFactoryImpl daoFactory) {
		super(daoFactory);
	}

	@Override
	protected MessageFile instantiateBlank() {
		return new MessageFile();
	}

	@Override
	protected DaoImpl<? super MessageFile> getSuperClassDao() {
		return getDaoFactory().getProtectedFileDao();
	}

	@Override
	protected void initEntityMembers() {
		this.addBooleanEntityMember("image", MessageFile::isImage, MessageFile::setImage);
	}

}
