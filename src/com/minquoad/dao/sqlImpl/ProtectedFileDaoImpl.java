package com.minquoad.dao.sqlImpl;

import com.minquoad.dao.interfaces.ProtectedFileDao;
import com.minquoad.entity.file.MessageFile;
import com.minquoad.entity.file.ProtectedFile;
import com.minquoad.entity.file.UserProfileImage;
import com.minquoad.framework.dao.DaoException;

public class ProtectedFileDaoImpl extends DaoImpl<ProtectedFile> implements ProtectedFileDao {

	public ProtectedFileDaoImpl(DaoFactoryImpl daoFactory) {
		super(daoFactory);
	}

	@Override
	protected ProtectedFile instantiateBlank() {
		return new ProtectedFile();
	}

	@Override
	protected void initSuperClass() {
	}

	@Override
	protected void initSubClasses() {
		addSubClass(UserProfileImage.class);
		addSubClass(MessageFile.class);
	}

	@Override
	public void initEntityMembers() throws DaoException {
		this.addLongEntityMember("id", ProtectedFile::getId, ProtectedFile::setId);
		this.addStringEntityMember("relativePath", ProtectedFile::getRelativePath, ProtectedFile::setRelativePath);
		this.addStringEntityMember("originalName", ProtectedFile::getOriginalName, ProtectedFile::setOriginalName);
	}

	@Override
	protected boolean isPrimaryKeyRandomlyGenerated() {
		return true;
	}

}
