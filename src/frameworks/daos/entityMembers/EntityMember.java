package frameworks.daos.entityMembers;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import frameworks.daos.Entity;
import frameworks.daos.EntityCriterion;
import frameworks.daos.EntityModification;

public abstract class EntityMember<EntitySubclass extends Entity, MemberType> {

	public abstract String getName();

	public void setValue(EntitySubclass entity, ResultSet resultSet) throws SQLException {

		MemberType value = getNonNullValue(resultSet);
		if (resultSet.wasNull()) {
			value = null;
		}
		this.setValue(entity, value);
	}

	public void setValue(PreparedStatement preparedStatement, int parameterIndex, EntitySubclass entity)
			throws SQLException {

		setValue(preparedStatement, parameterIndex, getValue(entity));
	}

	public void setValue(PreparedStatement preparedStatement, int parameterIndex, MemberType value)
			throws SQLException {

		if (value == null) {
			preparedStatement.setNull(parameterIndex, Types.NULL);
		} else {
			setNonNullValue(preparedStatement, parameterIndex, value);
		}
	}

	public void setValue(PreparedStatement preparedStatement, int parameterIndex, EntityCriterion criterion)
			throws SQLException {

		@SuppressWarnings("unchecked")
		MemberType value = (MemberType) criterion.getValue();

		setValue(preparedStatement, parameterIndex, value);
	}

	public void setValue(PreparedStatement preparedStatement, int parameterIndex,
			EntityModification modification) throws SQLException {

		@SuppressWarnings("unchecked")
		MemberType value = (MemberType) modification.getValue();

		setValue(preparedStatement, parameterIndex, value);
	}

	public abstract void setNonNullValue(PreparedStatement preparedStatement, int parameterIndex, MemberType value)
			throws SQLException;

	public abstract MemberType getValue(EntitySubclass entity);

	public abstract void setValue(EntitySubclass entity, MemberType value);

	public abstract MemberType getNonNullValue(ResultSet resultSet) throws SQLException;

	public void setValue(EntitySubclass entity, EntityModification modification) {

		@SuppressWarnings("unchecked")
		MemberType value = (MemberType) modification.getValue();

		setValue(entity, value);
	}

}
