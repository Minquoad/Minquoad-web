package com.minquoad.dao.interfaces;

import java.util.Collection;
import java.util.Map;

public interface Dao<Entity> {

	public Entity getByPk(Object id);

	public void persist(Entity entity);

	public void insert(Entity entity);

	public void update(Entity entity);

	public void delete(Entity entity);

	public Collection<Entity> getAll();

	public Collection<Entity> getAllMatching(String memberName, Object value);

	public Collection<Entity> getAllMatching(Map<String, Object> criteria);

	public Entity getOneMatching(String memberName, Object value);

	public Entity getOneMatching(Map<String, Object> criteria);

}
