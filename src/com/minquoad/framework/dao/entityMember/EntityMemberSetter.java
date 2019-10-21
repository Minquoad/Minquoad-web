package com.minquoad.framework.dao.entityMember;

@FunctionalInterface
public interface EntityMemberSetter<Entity, MemberType> {
	public void setValue(Entity entity, MemberType value);
}