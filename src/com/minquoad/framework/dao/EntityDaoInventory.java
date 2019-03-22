package com.minquoad.framework.dao;

import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;

import com.minquoad.framework.dao.entityMember.EntityMember;

public class EntityDaoInventory<PrimaryKeyType, EntitySubclass extends Entity> {

	HashMap<PrimaryKeyType, EntitySubclass> map;
	
	private EntityMember<EntitySubclass, PrimaryKeyType> idEntityMember;
	
	public EntityDaoInventory(EntityMember<EntitySubclass, PrimaryKeyType> idEntityMember) {
		map = new HashMap<PrimaryKeyType, EntitySubclass>();
		this.idEntityMember = idEntityMember;
	}

	public void delete(EntitySubclass entity) {
		map.remove(idEntityMember.getValue(entity));
	}

	public EntitySubclass put(EntitySubclass entity) throws SQLException {
		PrimaryKeyType primaryKey = idEntityMember.getValue(entity);
		if (primaryKey == null) {
			throw new SQLException("Entity without initialised primary key cannot be put in a dao inventory.");
		}
		return map.put(primaryKey, entity);
	}

	public EntitySubclass getByPrimaryKey(PrimaryKeyType primaryKey) {
		return map.get(primaryKey);
	}

	public Collection<EntitySubclass> values() {
		return map.values();
	}

}
