package frameworks.daos.entityMembers;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import frameworks.daos.Entity;

public abstract class ByteaEntityMember<EntitySubclass extends Entity> extends EntityMember<EntitySubclass, byte[]> {

	public static final String postgreSQLEquivalentType = "bytea";
	
	@Override
	public byte[] getNonNullValue(ResultSet resultSet) throws SQLException {
		return resultSet.getBytes(getName());
	}

	@Override
	public void setNonNullValue(PreparedStatement preparedStatement, int parameterIndex, byte[] value)
			throws SQLException {
		preparedStatement.setBytes(parameterIndex, value);
	}

}