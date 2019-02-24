package framework.dao.entityMember;

import framework.dao.Entity;

public interface EntityMemberGetter<EntitySubclass extends Entity, MemberType> {
	public MemberType getValue(EntitySubclass entity);
}
