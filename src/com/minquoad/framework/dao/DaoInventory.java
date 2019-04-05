package com.minquoad.framework.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;

import com.minquoad.framework.dao.entityMember.EntityMember;

public class DaoInventory<PrimaryKey, Entity> {

	HashMap<PrimaryKey, Entity> map;

	private EntityMember<? super Entity, PrimaryKey> pkEntityMember;

	public DaoInventory(EntityMember<? super Entity, PrimaryKey> pkEntityMember) {

		setMap(new HashMap<PrimaryKey, Entity>());

		setPkEntityMember(pkEntityMember);
	}

	public void delete(Entity entity) {
		getMap().remove(getPkEntityMember().getValue(entity));
	}

	public void put(Entity entity) throws DaoException {
		PrimaryKey primaryKey = getPkEntityMember().getValue(entity);
		if (primaryKey == null) {
			throw new DaoException("Entity without initialised primary key cannot be put in a dao inventory.");
		}
		getMap().put(primaryKey, entity);
	}

	public void checkPrimaryKeyUnchanged(Entity entity) throws DaoException {
		Entity expectedEntity = this.getByPrimaryKey(pkEntityMember.getValue(entity));
		if (!entity.equals(expectedEntity)) {
			throw new DaoException("Entity primary key value changes are not allowed.");
		}
	}

	public Entity getByPrimaryKey(PrimaryKey primaryKey) {
		return getMap().get(primaryKey);
	}

	public boolean isInside(Entity entity) {
		return getMap().get(pkEntityMember.getValue(entity)) != null;
	}

	public Entity getByPrimaryKey(ResultSet resultSet, String pkColumnName) throws SQLException, DaoException {
		return getMap().get(pkEntityMember.getValueOfResultSet(resultSet, pkColumnName));
	}

	public Entity getByPrimaryKey(ResultSet resultSet) throws SQLException, DaoException {
		return getMap().get(pkEntityMember.getValueOfResultSet(resultSet));
	}

	public Collection<Entity> values() {
		return getMap().values();
	}

	public HashMap<PrimaryKey, Entity> getMap() {
		return map;
	}

	public void setMap(HashMap<PrimaryKey, Entity> map) {
		this.map = map;
	}

	public EntityMember<? super Entity, PrimaryKey> getPkEntityMember() {
		return pkEntityMember;
	}

	public void setPkEntityMember(EntityMember<? super Entity, PrimaryKey> pkEntityMember) {
		this.pkEntityMember = pkEntityMember;
	}

}
