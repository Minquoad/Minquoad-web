package com.minquoad.framework.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;

import com.minquoad.framework.dao.entityMember.EntityMember;

public class EntityDaoInventory<PrimaryKeyType, EntitySubclass extends Entity> {

	HashMap<PrimaryKeyType, EntitySubclass> map;
	
	private EntityMember<EntitySubclass, PrimaryKeyType> pkEntityMember;
	
	public EntityDaoInventory(EntityMember<EntitySubclass, PrimaryKeyType> pkEntityMember) {
		map = new HashMap<PrimaryKeyType, EntitySubclass>();
		this.pkEntityMember = pkEntityMember;
	}

	public void delete(EntitySubclass entity) {
		map.remove(pkEntityMember.getValue(entity));
	}

	public EntitySubclass put(EntitySubclass entity) throws SQLException {
		PrimaryKeyType primaryKey = pkEntityMember.getValue(entity);
		if (primaryKey == null) {
			throw new SQLException("Entity without initialised primary key cannot be put in a dao inventory.");
		}
		return map.put(primaryKey, entity);
	}

	public void checkPrimaryKeyUnchanged(EntitySubclass entity) throws SQLException {
		EntitySubclass expectedEntity = this.getByPrimaryKey(pkEntityMember.getValue(entity));
		if (!entity.equals(expectedEntity)) {
			throw new SQLException("Entity primary key value changes are not allowed.");
		}
	}
	
	public EntitySubclass getByPrimaryKey(PrimaryKeyType primaryKey) {
		return map.get(primaryKey);
	}

	public boolean isInside(EntitySubclass entity) {
		return map.get(pkEntityMember.getValue(entity)) == null;
	}

	public EntitySubclass getByPrimaryKey(ResultSet resultSet) throws SQLException {
		return map.get(pkEntityMember.getValueOfResultSet(resultSet));
	}

	public Collection<EntitySubclass> values() {
		return map.values();
	}

}
