package daos.interfaces;

import java.util.List;

import frameworks.daos.EntityCriterion;

public interface Dao<EntitySubclass> {

	public EntitySubclass getById(Integer id);

	public boolean insert(EntitySubclass entity);

	public boolean update(EntitySubclass entity);

	public boolean delete(EntitySubclass entity);

	public List<EntitySubclass> getAll();

	public List<EntitySubclass> getAllMatching(Object value, String memberName);

	public List<EntitySubclass> getAllMatching(EntityCriterion criterion);

	public List<EntitySubclass> getAllMatching(EntityCriterion[] criteria);

}
