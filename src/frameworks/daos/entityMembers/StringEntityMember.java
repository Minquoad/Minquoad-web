package frameworks.daos.entityMembers;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import frameworks.daos.Entity;

public abstract class StringEntityMember<EntitySubclass extends Entity> extends EntityMember<EntitySubclass, String> {

	public static final String postgreSQLEquivalentType = "text";

	@Override
	public String getNonNullValue(ResultSet resultSet) throws SQLException {
		return resultSet.getString(getName());
	}

	@Override
	public void setNonNullValue(PreparedStatement preparedStatement, int parameterIndex, String value)
			throws SQLException {
		preparedStatement.setString(parameterIndex, value);
	}

}
