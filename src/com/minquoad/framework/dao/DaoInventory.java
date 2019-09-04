package com.minquoad.framework.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.minquoad.framework.dao.entityMember.EntityMember;

public class DaoInventory<Entity> {

	private Map<Object, Entity> map;

	private EntityMember<? super Entity, ?> pkEntityMember;

	public DaoInventory(EntityMember<? super Entity, ?> pkEntityMember) {

		setMap(new HashMap<Object, Entity>());

		setPkEntityMember(pkEntityMember);
	}

	public void delete(Entity entity) {
		getMap().remove(getPkEntityMember().getValue(entity));
	}

	public void put(Entity entity) {
		Object primaryKey = getPkEntityMember().getValue(entity);
		if (primaryKey == null) {
			throw new DaoException("Entity without initialised primary key cannot be put in a dao inventory.");
		}
		getMap().put(primaryKey, entity);
	}

	public void checkPrimaryKeyUnchanged(Entity entity) {
		Entity expectedEntity = this.getByPrimaryKey(getPkEntityMember().getValue(entity));
		if (!entity.equals(expectedEntity)) {
			throw new DaoException("Entity primary key value changes are not allowed.");
		}
	}

	public boolean isInside(Entity entity) {
		return getMap().get(getPkEntityMember().getValue(entity)) != null;
	}

	public Entity getByPrimaryKey(Object primaryKey) {
		return getMap().get(primaryKey);
	}

	public Entity getByPrimaryKey(ResultSet resultSet) throws SQLException {
		return getMap().get(getPkEntityMember().getValueOfResultSet(resultSet));
	}

	public Entity getByPrimaryKey(ResultSet resultSet, String pkColumnName) throws SQLException {
		return getMap().get(getPkEntityMember().getValueOfResultSet(resultSet, pkColumnName));
	}

	public Collection<Entity> values() {
		return getMap().values();
	}

	public Map<Object, Entity> getMap() {
		return map;
	}

	public void setMap(Map<Object, Entity> map) {
		this.map = map;
	}

	public EntityMember<? super Entity, ?> getPkEntityMember() {
		return pkEntityMember;
	}

	public void setPkEntityMember(EntityMember<? super Entity, ?> pkEntityMember) {
		this.pkEntityMember = pkEntityMember;
	}

	public void clear() {
		map.clear();
	}

}
