package com.minquoad.dao.interfaces;

import java.util.List;

import com.minquoad.framework.dao.EntityCriterion;

public interface Dao<EntitySubclass> {

	public EntitySubclass getById(Integer id);

	public boolean persist(EntitySubclass entity);

	public boolean insert(EntitySubclass entity);

	public boolean update(EntitySubclass entity);

	public boolean delete(EntitySubclass entity);

	public List<EntitySubclass> getAll();

	public List<EntitySubclass> getAllMatching(Object value, String memberName);

	public List<EntitySubclass> getAllMatching(EntityCriterion criterion);

	public List<EntitySubclass> getAllMatching(EntityCriterion[] criteria);

	public EntitySubclass getOneMatching(Object value, String memberName);

	public EntitySubclass getOneMatching(EntityCriterion criterion);

	public EntitySubclass getOneMatching(EntityCriterion[] criteria);

}
