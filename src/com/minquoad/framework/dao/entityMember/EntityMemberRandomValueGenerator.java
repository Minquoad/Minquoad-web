package com.minquoad.framework.dao.entityMember;

@FunctionalInterface
public interface EntityMemberRandomValueGenerator<MemberType> {
	public MemberType generateValue();
}
