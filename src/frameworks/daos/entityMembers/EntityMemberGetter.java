package frameworks.daos.entityMembers;

import frameworks.daos.Entity;

public interface EntityMemberGetter<EntitySubclass extends Entity, MemberType> {
	public MemberType getValue(EntitySubclass entity);
}
