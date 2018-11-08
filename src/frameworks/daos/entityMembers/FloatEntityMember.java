package frameworks.daos.entityMembers;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import frameworks.daos.Entity;

public abstract class FloatEntityMember<EntitySubclass extends Entity> extends EntityMember<EntitySubclass, Float> {

	public static final String postgreSQLEquivalentType = "real";
	
	@Override
	public Float getNonNullValue(ResultSet resultSet) throws SQLException {
		return resultSet.getFloat(getName());
	}

	@Override
	public void setNonNullValue(PreparedStatement preparedStatement, int parameterIndex, Float value)
			throws SQLException {
		preparedStatement.setFloat(parameterIndex, value);
	}

}