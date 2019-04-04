package com.minquoad.framework.dao.entityMember;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import com.minquoad.framework.dao.DaoException;
import com.minquoad.framework.dao.EntityCriterion;
import com.minquoad.framework.dao.EntityModification;

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

	public void setValueOfResultSetInEntity(Entity entity, ResultSet resultSet) throws SQLException, DaoException {

		MemberType value = getValueOfResultSet(resultSet);
		this.setValue(entity, value);
	}

	public MemberType getValueOfResultSet(ResultSet resultSet) throws SQLException, DaoException {
		MemberType value = this.getResultSetNonNullValueGetter().getNonNullValue(
				resultSet,
				this.getName());
		if (resultSet.wasNull()) {
			value = null;
		}
		return value;
	}

	public void setValueOfEntityInPreparedStatement(PreparedStatement preparedStatement, int parameterIndex, Entity entity) throws SQLException, DaoException {

		setValueInPreparedStatement(preparedStatement, parameterIndex, getValue(entity));
	}

	public void setValueInPreparedStatement(PreparedStatement preparedStatement, int parameterIndex, MemberType value) throws SQLException, DaoException {

		if (value == null) {
			preparedStatement.setNull(parameterIndex, Types.NULL);
		} else {
			this.getPreparedStatementNonNullValueSetter().setNonNullValue(
					preparedStatement,
					parameterIndex,
					value);
		}
	}

	public void setValueOfCriterionInPreparedStatement(PreparedStatement preparedStatement, int parameterIndex, EntityCriterion criterion) throws SQLException, DaoException {

		@SuppressWarnings("unchecked")
		MemberType value = (MemberType) criterion.getValue();

		setValueInPreparedStatement(preparedStatement, parameterIndex, value);
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

	// not used yet

	public void setValueOfModificationInPreparedStatement(PreparedStatement preparedStatement, int parameterIndex,
			EntityModification modification) throws SQLException, DaoException {

		@SuppressWarnings("unchecked")
		MemberType value = (MemberType) modification.getValue();

		setValueInPreparedStatement(preparedStatement, parameterIndex, value);
	}

	public void setValueOfModificationInEntity(Entity entity, EntityModification modification) {

		@SuppressWarnings("unchecked")
		MemberType value = (MemberType) modification.getValue();

		setValue(entity, value);
	}

}
