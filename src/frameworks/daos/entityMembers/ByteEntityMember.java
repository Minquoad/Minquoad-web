package frameworks.daos.entityMembers;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import frameworks.daos.Entity;

public abstract class ByteEntityMember<EntitySubclass extends Entity> extends EntityMember<EntitySubclass, Byte> {

	public static final String postgreSQLEquivalentType = "char";

	@Override
	public Byte getNonNullValue(ResultSet resultSet) throws SQLException {
		return resultSet.getByte(getName());
	}

	@Override
	public void setNonNullValue(PreparedStatement preparedStatement, int parameterIndex, Byte value)
			throws SQLException {
		preparedStatement.setByte(parameterIndex, value);
	}

}