package frameworks.daos.entityMembers;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import frameworks.daos.Entity;

public abstract class LongEntityMember<EntitySubclass extends Entity> extends EntityMember<EntitySubclass, Long> {

	public static final String postgreSQLEquivalentType = "bigint";
	
	@Override
	public Long getNonNullValue(ResultSet resultSet) throws SQLException {
		return resultSet.getLong(getName());
	}

	@Override
	public void setNonNullValue(PreparedStatement preparedStatement, int parameterIndex, Long value)
			throws SQLException {
		preparedStatement.setLong(parameterIndex, value);
	}

}