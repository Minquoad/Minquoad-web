package frameworks.daos;

import java.util.List;

public interface EntityDao<EntitySubclass extends Entity> {

	public EntitySubclass getById(Integer id);

	public boolean insert(EntitySubclass entity);

	public boolean update(EntitySubclass entity);

	public boolean delete(EntitySubclass entity);

	public List<EntitySubclass> getAll();

	public List<EntitySubclass> getAllMatching(Object value, String memberName);

	public List<EntitySubclass> getAllMatching(EntityCriterion criterion);

	public List<EntitySubclass> getAllMatching(EntityCriterion[] criteria);

}
