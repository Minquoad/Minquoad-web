package frameworks.daos.entityMembers;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import frameworks.daos.Entity;

public abstract class BooleanEntityMember<EntitySubclass extends Entity> extends EntityMember<EntitySubclass, Boolean> {

	public static final String postgreSQLEquivalentType = "boolean";

	@Override
	public Boolean getNonNullValue(ResultSet resultSet) throws SQLException {
		return resultSet.getBoolean(getName());
	}

	@Override
	public void setNonNullValue(PreparedStatement preparedStatement, int parameterIndex, Boolean value)
			throws SQLException {
		preparedStatement.setBoolean(parameterIndex, value);
	}

}
