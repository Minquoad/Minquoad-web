package frameworks.daos.entityMembers;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import frameworks.daos.Entity;

public abstract class DoubleEntityMember<EntitySubclass extends Entity> extends EntityMember<EntitySubclass, Double> {

	public static final String postgreSQLEquivalentType = "double precision";
	
	@Override
	public Double getNonNullValue(ResultSet resultSet) throws SQLException {
		return resultSet.getDouble(getName());
	}

	@Override
	public void setNonNullValue(PreparedStatement preparedStatement, int parameterIndex, Double value)
			throws SQLException {
		preparedStatement.setDouble(parameterIndex, value);
	}

}