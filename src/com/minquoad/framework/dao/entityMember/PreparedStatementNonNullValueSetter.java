package com.minquoad.framework.dao.entityMember;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.minquoad.framework.dao.DaoException;

public interface PreparedStatementNonNullValueSetter<MemberType> {
	public void setNonNullValue(PreparedStatement preparedStatement, int parameterIndex, MemberType value) throws SQLException, DaoException;
}
