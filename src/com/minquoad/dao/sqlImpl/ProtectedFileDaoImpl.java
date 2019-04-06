package com.minquoad.dao.sqlImpl;

import com.minquoad.dao.interfaces.ProtectedFileDao;
import com.minquoad.entity.file.ProtectedFile;
import com.minquoad.framework.dao.DaoException;

public class ProtectedFileDaoImpl extends ImprovedEntityDaoImpl<ProtectedFile> implements ProtectedFileDao {

	public ProtectedFileDaoImpl(DaoFactoryImpl daoFactory) {
		super(daoFactory);
	}

	@Override
	public void initEntityMembers() throws DaoException {
		this.addIntegerEntityMember("id", ProtectedFile::getId, ProtectedFile::setId, true);
		this.addStringEntityMember("relativePath", ProtectedFile::getRelativePath, ProtectedFile::setRelativePath);
		this.addStringEntityMember("originalName", ProtectedFile::getOriginalName, ProtectedFile::setOriginalName);
		this.addStringEntityMember("originalExtention", ProtectedFile::getOriginalExtention, ProtectedFile::setOriginalExtention);
	}

	@Override
	protected void initSubClassDaos() {
		this.addSubClassDao(getDaoFactory().getUserProfileImageDao());
	}

	@Override
	public ProtectedFile instantiateBlank() {
		return new ProtectedFile();
	}

	public boolean isPrimaryKeyRandomlyGenerated() {
		return true;
	}

}
