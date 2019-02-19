package frameworks.daos.entityMembers;

import frameworks.daos.Entity;

public interface EntityMemberSetter<EntitySubclass extends Entity, MemberType> {
	public void setValue(EntitySubclass entity, MemberType value);
}