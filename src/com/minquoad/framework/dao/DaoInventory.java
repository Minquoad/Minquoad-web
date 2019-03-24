package com.minquoad.framework.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;

import com.minquoad.framework.dao.entityMember.EntityMember;

public class DaoInventory<PrimaryKey, Entity> {

	HashMap<PrimaryKey, Entity> map;

	private EntityMember<? super Entity, PrimaryKey> pkEntityMember;

	private DaoInventory<PrimaryKey, ? super Entity> superEntityDaoInventory;

	public DaoInventory(EntityMember<? super Entity, PrimaryKey> pkEntityMember, DaoInventory<PrimaryKey, ? super Entity> superEntityDaoInventory) {

		setMap(new HashMap<PrimaryKey, Entity>());

		setPkEntityMember(pkEntityMember);
		setSuperEntityDaoInventory(superEntityDaoInventory);
	}

	public void delete(Entity entity) {
		getMap().remove(getPkEntityMember().getValue(entity));
		if (getSuperEntityDaoInventory() != null) {
			getSuperEntityDaoInventory().delete(entity);
		}
	}

	public void put(Entity entity) throws SQLException {
		PrimaryKey primaryKey = getPkEntityMember().getValue(entity);
		if (primaryKey == null) {
			throw new SQLException("Entity without initialised primary key cannot be put in a dao inventory.");
		}
		getMap().put(primaryKey, entity);
		if (getSuperEntityDaoInventory() != null) {
			getSuperEntityDaoInventory().put(entity);
		}
	}

	public void checkPrimaryKeyUnchanged(Entity entity) throws SQLException {
		Entity expectedEntity = this.getByPrimaryKey(pkEntityMember.getValue(entity));
		if (!entity.equals(expectedEntity)) {
			throw new SQLException("Entity primary key value changes are not allowed.");
		}
	}

	public Entity getByPrimaryKey(PrimaryKey primaryKey) {
		return getMap().get(primaryKey);
	}

	public boolean isInside(Entity entity) {
		return getMap().get(pkEntityMember.getValue(entity)) != null;
	}

	public Entity getByPrimaryKey(ResultSet resultSet) throws SQLException {
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

	public DaoInventory<PrimaryKey, ? super Entity> getSuperEntityDaoInventory() {
		return superEntityDaoInventory;
	}

	public void setSuperEntityDaoInventory(DaoInventory<PrimaryKey, ? super Entity> superEntityDaoInventory) {
		this.superEntityDaoInventory = superEntityDaoInventory;
	}

}
