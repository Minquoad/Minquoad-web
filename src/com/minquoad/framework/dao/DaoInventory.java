package com.minquoad.framework.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;

import com.minquoad.framework.dao.entityMember.EntityMember;

public class DaoInventory<PrimaryKey, Entity> {

	HashMap<PrimaryKey, Entity> map;
	
	private EntityMember<Entity, PrimaryKey> pkEntityMember;
	
	public DaoInventory(EntityMember<Entity, PrimaryKey> pkEntityMember) {
		map = new HashMap<PrimaryKey, Entity>();
		this.pkEntityMember = pkEntityMember;
	}

	public void delete(Entity entity) {
		map.remove(pkEntityMember.getValue(entity));
	}

	public Entity put(Entity entity) throws SQLException {
		PrimaryKey primaryKey = pkEntityMember.getValue(entity);
		if (primaryKey == null) {
			throw new SQLException("Entity without initialised primary key cannot be put in a dao inventory.");
		}
		return map.put(primaryKey, entity);
	}

	public void checkPrimaryKeyUnchanged(Entity entity) throws SQLException {
		Entity expectedEntity = this.getByPrimaryKey(pkEntityMember.getValue(entity));
		if (!entity.equals(expectedEntity)) {
			throw new SQLException("Entity primary key value changes are not allowed.");
		}
	}
	
	public Entity getByPrimaryKey(PrimaryKey primaryKey) {
		return map.get(primaryKey);
	}

	public boolean isInside(Entity entity) {
		return map.get(pkEntityMember.getValue(entity)) == null;
	}

	public Entity getByPrimaryKey(ResultSet resultSet) throws SQLException {
		return map.get(pkEntityMember.getValueOfResultSet(resultSet));
	}

	public Collection<Entity> values() {
		return map.values();
	}

}
