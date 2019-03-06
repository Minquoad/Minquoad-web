package com.minquoad.framework.dao.entityMember;

import com.minquoad.framework.dao.Entity;

public interface EntityMemberGetter<EntitySubclass extends Entity, MemberType> {
	public MemberType getValue(EntitySubclass entity);
}
