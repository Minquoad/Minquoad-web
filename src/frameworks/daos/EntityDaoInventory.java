package frameworks.daos;

import java.sql.SQLException;
import java.util.HashMap;

public class EntityDaoInventory<EntitySubclass extends Entity> extends HashMap<Integer, EntitySubclass> {

	public void delete(EntitySubclass entity) {
		this.remove(entity.getId());
	}

	public EntitySubclass put(EntitySubclass entity) throws SQLException {
		if (entity.getId() == null) {
			throw new SQLException("Entity without initialised id cannot be put in a dao inventory.");
		}
		return this.put(entity.getId(), entity);
	}
	
}
