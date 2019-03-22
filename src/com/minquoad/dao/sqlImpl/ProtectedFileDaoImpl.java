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
		this.addStringEntityMember("relativePath", ProtectedFile::getRelativePath, ProtectedFile::setRelativePath);
	}

	@Override
	public ProtectedFile instantiateBlank() {
		return new ProtectedFile();
	}

}
