package frameworks.daos.entityMembers;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import frameworks.daos.Entity;

public abstract class ShortEntityMember<EntitySubclass extends Entity> extends EntityMember<EntitySubclass, Short> {

	public static final String postgreSQLEquivalentType = "smallint";
	
	@Override
	public Short getNonNullValue(ResultSet resultSet) throws SQLException {
		return resultSet.getShort(getName());
	}

	@Override
	public void setNonNullValue(PreparedStatement preparedStatement, int parameterIndex, Short value)
			throws SQLException {
		preparedStatement.setShort(parameterIndex, value);
	}

}