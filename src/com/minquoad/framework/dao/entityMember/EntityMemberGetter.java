package com.minquoad.framework.dao.entityMember;

public interface EntityMemberGetter<Entity, MemberType> {
	public MemberType getValue(Entity entity);
}
