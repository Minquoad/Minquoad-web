package frameworks.daos.entityMembers;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import frameworks.daos.Entity;
import frameworks.daos.EntityCriterion;
import frameworks.daos.EntityModification;
import frameworks.daos.PreparedStatementNonNullValueSetter;
import frameworks.daos.ResultSetNonNullValueGetter;

public class EntityMember<EntitySubclass extends Entity, MemberType> {

	private String name;

	private EntityMemberGetter<EntitySubclass, MemberType> valueGetter;
	private EntityMemberSetter<EntitySubclass, MemberType> valueSetter;

	private ResultSetNonNullValueGetter<MemberType> resultSetNonNullValueGetter;
	private PreparedStatementNonNullValueSetter<MemberType> preparedStatementNonNullValueSetter;

	public EntityMember(String name,
			EntityMemberGetter<EntitySubclass, MemberType> valueGetter,
			EntityMemberSetter<EntitySubclass, MemberType> valueSetter,
			ResultSetNonNullValueGetter<MemberType> resultSetNonNullValueGetter,
			PreparedStatementNonNullValueSetter<MemberType> preparedStatementNonNullValueSetter) {
		this.name = name;
		this.valueGetter = valueGetter;
		this.valueSetter = valueSetter;
		this.resultSetNonNullValueGetter = resultSetNonNullValueGetter;
		this.preparedStatementNonNullValueSetter = preparedStatementNonNullValueSetter;
	}

	public String getName() {
		return name;
	}

	public void setValueOfResultSetInEntity(EntitySubclass entity, ResultSet resultSet) throws SQLException {

		MemberType value = this.getResultSetNonNullValueGetter().getNonNullValue(
				resultSet,
				this.getName());
		if (resultSet.wasNull()) {
			value = null;
		}
		this.setValue(entity, value);
	}

	public void setValueOfEntityInPreparedStatement(PreparedStatement preparedStatement, int parameterIndex, EntitySubclass entity)
			throws SQLException {

		setValueInPreparedStatement(preparedStatement, parameterIndex, getValue(entity));
	}

	public void setValueInPreparedStatement(PreparedStatement preparedStatement, int parameterIndex, MemberType value)
			throws SQLException {

		if (value == null) {
			preparedStatement.setNull(parameterIndex, Types.NULL);
		} else {
			this.getPreparedStatementNonNullValueSetter().setNonNullValue(
					preparedStatement,
					parameterIndex,
					value);
		}
	}

	public void setValueOfCriterionInPreparedStatement(PreparedStatement preparedStatement, int parameterIndex, EntityCriterion criterion)
			throws SQLException {

		@SuppressWarnings("unchecked")
		MemberType value = (MemberType) criterion.getValue();

		setValueInPreparedStatement(preparedStatement, parameterIndex, value);
	}

	public MemberType getValue(EntitySubclass entity) {
		return this.getValueGetter().getValue(entity);
	}

	public void setValue(EntitySubclass entity, MemberType value) {
		this.getValueSetter().setValue(entity, value);
	}

	protected EntityMemberGetter<EntitySubclass, MemberType> getValueGetter() {
		return this.valueGetter;
	}

	protected EntityMemberSetter<EntitySubclass, MemberType> getValueSetter() {
		return this.valueSetter;
	}

	protected ResultSetNonNullValueGetter<MemberType> getResultSetNonNullValueGetter() {
		return this.resultSetNonNullValueGetter;
	}

	protected PreparedStatementNonNullValueSetter<MemberType> getPreparedStatementNonNullValueSetter() {
		return this.preparedStatementNonNullValueSetter;
	}

	// not used yet
	
	public void setValueOfModificationInPreparedStatement(PreparedStatement preparedStatement, int parameterIndex,
			EntityModification modification) throws SQLException {

		@SuppressWarnings("unchecked")
		MemberType value = (MemberType) modification.getValue();

		setValueInPreparedStatement(preparedStatement, parameterIndex, value);
	}

	public void setValueOfModificationInEntity(EntitySubclass entity, EntityModification modification) {

		@SuppressWarnings("unchecked")
		MemberType value = (MemberType) modification.getValue();

		setValue(entity, value);
	}

}
