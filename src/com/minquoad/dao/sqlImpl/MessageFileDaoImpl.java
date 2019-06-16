package com.minquoad.dao.sqlImpl;

import com.minquoad.dao.interfaces.MessageFileDao;
import com.minquoad.entity.file.MessageFile;
import com.minquoad.framework.dao.DaoImpl;

public class MessageFileDaoImpl extends ImprovedDaoImpl<MessageFile> implements MessageFileDao {

	public MessageFileDaoImpl(DaoFactoryImpl daoFactory) {
		super(daoFactory);
	}

	@Override
	protected void initEntityMembers() {
	}

	@Override
	public MessageFile instantiateBlank() {
		return new MessageFile();
	}

	@Override
	public DaoImpl<? super MessageFile> getSuperClassDao() {
		return getDaoFactory().getProtectedFileDao();
	}

}
