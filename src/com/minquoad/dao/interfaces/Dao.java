package com.minquoad.dao.interfaces;

import java.util.List;
import java.util.Map;

public interface Dao<Entity> {

	public Entity getByPk(Object id);

	public void persist(Entity entity);

	public void insert(Entity entity);

	public void update(Entity entity);

	public void delete(Entity entity);

	public List<Entity> getAll();

	public List<Entity> getAllMatching(String memberName, Object value);

	public List<Entity> getAllMatching(Map<String, Object> criteria);

	public Entity getOneMatching(String memberName, Object value);

	public Entity getOneMatching(Map<String, Object> criteria);

}
