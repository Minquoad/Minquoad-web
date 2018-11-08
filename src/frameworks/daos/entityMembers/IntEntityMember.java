package frameworks.daos.entityMembers;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import frameworks.daos.Entity;

public abstract class IntEntityMember<EntitySubclass extends Entity> extends EntityMember<EntitySubclass, Integer> {

	public static final String postgreSQLEquivalentType = "integer";

	@Override
	public Integer getNonNullValue(ResultSet resultSet) throws SQLException {
		return resultSet.getInt(getName());
	}

	@Override
	public void setNonNullValue(PreparedStatement preparedStatement, int parameterIndex, Integer value)
			throws SQLException {
		preparedStatement.setInt(parameterIndex, value);
	}

}
