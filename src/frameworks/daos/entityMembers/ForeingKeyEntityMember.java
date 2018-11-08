package frameworks.daos.entityMembers;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import frameworks.daos.EntityDao;
import frameworks.daos.Entity;

public abstract class ForeingKeyEntityMember<EntitySubclass extends Entity, ReferencedEntitySubclass extends Entity>
		extends EntityMember<EntitySubclass, ReferencedEntitySubclass> {

	public abstract EntityDao<ReferencedEntitySubclass> getReferencedEntityDao();

	@Override
	public ReferencedEntitySubclass getNonNullValue(ResultSet resultSet) throws SQLException {
		return this.getReferencedEntityDao().getById(resultSet.getInt(this.getName()));
	}

	@Override
	public void setNonNullValue(PreparedStatement preparedStatement, int parameterIndex, ReferencedEntitySubclass value)
			throws SQLException {
		preparedStatement.setInt(parameterIndex, value.getId());
	}

}
