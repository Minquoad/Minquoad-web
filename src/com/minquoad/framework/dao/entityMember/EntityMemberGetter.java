package com.minquoad.framework.dao.entityMember;

@FunctionalInterface
public interface EntityMemberGetter<Entity, MemberType> {
	public MemberType getValue(Entity entity);
}
