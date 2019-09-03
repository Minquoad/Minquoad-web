package com.minquoad.dao.interfaces;

import java.util.List;
import java.util.Map;

public interface Dao<EntitySubclass> {

	public <PrimaryKey> EntitySubclass getByPk(PrimaryKey id);

	public void persist(EntitySubclass entity);

	public void insert(EntitySubclass entity);

	public void update(EntitySubclass entity);

	public void delete(EntitySubclass entity);

	public List<EntitySubclass> getAll();

	public List<EntitySubclass> getAllMatching(String memberName, Object value);

	public List<EntitySubclass> getAllMatching(Map<String, Object> criteria);

	public EntitySubclass getOneMatching(String memberName, Object value);

	public EntitySubclass getOneMatching(Map<String, Object> criteria);

}
