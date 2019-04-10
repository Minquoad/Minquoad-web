package com.minquoad.framework.dao.entityMember;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

public class EntityMember<Entity, MemberType> {

	private String name;

	private EntityMemberGetter<Entity, MemberType> valueGetter;
	private EntityMemberSetter<Entity, MemberType> valueSetter;

	private ResultSetNonNullValueGetter<MemberType> resultSetNonNullValueGetter;
	private PreparedStatementNonNullValueSetter<MemberType> preparedStatementNonNullValueSetter;

	public EntityMember(String name,
			EntityMemberGetter<Entity, MemberType> valueGetter,
			EntityMemberSetter<Entity, MemberType> valueSetter,
			ResultSetNonNullValueGetter<MemberType> resultSetNonNullValueGetter,
			PreparedStatementNonNullValueSetter<MemberType> preparedStatementNonNullValueSetter) {
		this.name = name;
		this.valueGetter = valueGetter;
		this.valueSetter = valueSetter;
		this.resultSetNonNullValueGetter = resultSetNonNullValueGetter;
		this.preparedStatementNonNullValueSetter = preparedStatementNonNullValueSetter;
	}

	public void setValueOfResultSetInEntity(Entity entity, ResultSet resultSet) throws SQLException {
		this.setValue(entity, getValueOfResultSet(resultSet));
	}

	public MemberType getValueOfResultSet(ResultSet resultSet) throws SQLException {
		return getValueOfResultSet(resultSet, this.getName());
	}

	public MemberType getValueOfResultSet(ResultSet resultSet, String columnName) throws SQLException {
		MemberType value = this.getResultSetNonNullValueGetter().getNonNullValue(
				resultSet,
				columnName);
		if (resultSet.wasNull()) {
			value = null;
		}
		return value;
	}

	public void setValueOfEntityInPreparedStatement(PreparedStatement preparedStatement, int parameterIndex, Entity entity) throws SQLException {

		setValueInPreparedStatement(preparedStatement, parameterIndex, getValue(entity));
	}

	public void setValueInPreparedStatement(PreparedStatement preparedStatement, int parameterIndex, MemberType value) throws SQLException {

		if (value == null) {
			preparedStatement.setNull(parameterIndex, Types.NULL);
		} else {
			this.getPreparedStatementNonNullValueSetter().setNonNullValue(
					preparedStatement,
					parameterIndex,
					value);
		}
	}

	public void setValueOfResultSetInPreparedStatement(PreparedStatement preparedStatement, int parameterIndex, ResultSet resultSet, String resultSetColumnName) throws SQLException {
		setValueInPreparedStatement(preparedStatement, parameterIndex, getValueOfResultSet(resultSet, resultSetColumnName));
	}

	public void setValueOfCriterionInPreparedStatement(PreparedStatement preparedStatement, int parameterIndex, Object value) throws SQLException {

		@SuppressWarnings("unchecked")
		MemberType castedValue = (MemberType) value;

		setValueInPreparedStatement(preparedStatement, parameterIndex, castedValue);
	}

	public MemberType getValue(Entity entity) {
		return this.getValueGetter().getValue(entity);
	}

	public void setValue(Entity entity, MemberType value) {
		this.getValueSetter().setValue(entity, value);
	}

	public String getName() {
		return name;
	}

	protected EntityMemberGetter<Entity, MemberType> getValueGetter() {
		return this.valueGetter;
	}

	protected EntityMemberSetter<Entity, MemberType> getValueSetter() {
		return this.valueSetter;
	}

	public ResultSetNonNullValueGetter<MemberType> getResultSetNonNullValueGetter() {
		return this.resultSetNonNullValueGetter;
	}

	public PreparedStatementNonNullValueSetter<MemberType> getPreparedStatementNonNullValueSetter() {
		return this.preparedStatementNonNullValueSetter;
	}

}
