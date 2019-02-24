package framework.dao;

public class EntityModification {

	private Object value;
	private String memberName;

	public EntityModification(Object value, String memberName) {
		this.value = value;
		this.memberName = memberName;
	}
	
	public Object getValue() {
		return value;
	}

	public String getName() {
		return memberName;
	}

	public EntityModification[] toArray() {
		EntityModification[] modifications = new EntityModification[1];
		modifications[0] = this;
		return modifications;
	}

}
