package frameworks.daos.entityMembers;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;

import frameworks.daos.Entity;

public abstract class DateEntityMember<EntitySubclass extends Entity> extends EntityMember<EntitySubclass, Date> {

	public static final String postgreSQLEquivalentType = "timestamp with time zone";
	
	@Override
	public Date getNonNullValue(ResultSet resultSet) throws SQLException {
		return resultSet.getTimestamp(getName());
	}

	@Override
	public void setNonNullValue(PreparedStatement preparedStatement, int parameterIndex, Date value)
			throws SQLException {
		preparedStatement.setTimestamp(parameterIndex, new Timestamp(value.getTime()));
	}

}