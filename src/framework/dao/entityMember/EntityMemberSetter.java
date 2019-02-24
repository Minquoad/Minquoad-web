package framework.dao.entityMember;

import framework.dao.Entity;

public interface EntityMemberSetter<EntitySubclass extends Entity, MemberType> {
	public void setValue(EntitySubclass entity, MemberType value);
}