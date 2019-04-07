package com.minquoad.dao.interfaces;

import java.util.List;

import com.minquoad.framework.dao.EntityCriterion;

public interface Dao<EntitySubclass> {

	public EntitySubclass getByPk(Integer id);

	public EntitySubclass getByPk(Long id);

	public EntitySubclass getByPk(String id);

	public void persist(EntitySubclass entity);

	public void insert(EntitySubclass entity);

	public void update(EntitySubclass entity);

	public void delete(EntitySubclass entity);

	public List<EntitySubclass> getAll();

	public List<EntitySubclass> getAllMatching(Object value, String memberName);

	public List<EntitySubclass> getAllMatching(EntityCriterion criterion);

	public List<EntitySubclass> getAllMatching(EntityCriterion[] criteria);

	public EntitySubclass getOneMatching(Object value, String memberName);

	public EntitySubclass getOneMatching(EntityCriterion criterion);

	public EntitySubclass getOneMatching(EntityCriterion[] criteria);

}
