package frameworks.daos;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface ResultSetNonNullValueGetter<MemberType> {
	public MemberType getNonNullValue(ResultSet resultSet, String name) throws SQLException;
}
