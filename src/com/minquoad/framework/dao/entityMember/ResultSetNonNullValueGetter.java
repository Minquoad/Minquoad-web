package com.minquoad.framework.dao.entityMember;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface ResultSetNonNullValueGetter<MemberType> {
	public MemberType getNonNullValue(ResultSet resultSet, String name) throws SQLException;
}
