package framework.dao.entityMember;

import framework.dao.Entity;
import framework.dao.EntityDaoImpl;

public class ForeingKeyEntityMember<EntitySubclass extends Entity, ReferencedEntitySubclass extends Entity>
		extends EntityMember<EntitySubclass, ReferencedEntitySubclass> {

	public ForeingKeyEntityMember(String name,
			EntityMemberGetter<EntitySubclass, ReferencedEntitySubclass> valueGetter,
			EntityMemberSetter<EntitySubclass, ReferencedEntitySubclass> valueSetter,
			EntityDaoImpl<ReferencedEntitySubclass> referencedEntityDao) {
		super(
				name,
				valueGetter,
				valueSetter,
				(resultSet, thisName) -> referencedEntityDao.getById(resultSet.getInt(thisName)),
				(preparedStatement, parameterIndex, value) -> preparedStatement.setInt(parameterIndex, value.getId()));
	}

}
