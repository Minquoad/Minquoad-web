package com.minquoad.dao.sqlImpl;

import java.sql.SQLException;

import com.minquoad.dao.interfaces.ProtectedFileDao;
import com.minquoad.entity.file.ProtectedFile;

public class ProtectedFileDaoImpl extends ImprovedEntityDaoImpl<ProtectedFile> implements ProtectedFileDao {

	public ProtectedFileDaoImpl(DaoFactoryImpl daoFactory) {
		super(daoFactory);
	}

	@Override
	public void initEntityMembers() throws SQLException {
		this.addIntegerEntityMember("id", ProtectedFile::getId, ProtectedFile::setId, true);
		this.addStringEntityMember("relativePath", ProtectedFile::getRelativePath, ProtectedFile::setRelativePath);
		this.addStringEntityMember("originalName", ProtectedFile::getOriginalName, ProtectedFile::setOriginalName);
		this.addStringEntityMember("originalExtention", ProtectedFile::getOriginalExtention, ProtectedFile::setOriginalExtention);
		this.addInstantEntityMember("lastModificationDate", ProtectedFile::getLastModificationDate, ProtectedFile::setLastModificationDate);
	}

	@Override
	protected void initSubClassDaos() {
		this.addSubClassDao(getDaoFactory().getUserProfileImageDao());
	}

	@Override
	public ProtectedFile instantiateBlank() {
		return new ProtectedFile();
	}

}
