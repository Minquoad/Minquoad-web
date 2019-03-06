package com.minquoad.framework.dao.entityMember;

import com.minquoad.framework.dao.Entity;

public interface EntityMemberSetter<EntitySubclass extends Entity, MemberType> {
	public void setValue(EntitySubclass entity, MemberType value);
}