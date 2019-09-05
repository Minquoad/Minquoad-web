package com.minquoad.tool;

import java.util.LinkedList;
import java.util.List;

public class Pool<ReusableObject> {

	private int maxSize;

	private List<ReusableObject> availables;
	private List<ReusableObject> unavailables;
	
	private Constructor<ReusableObject> constructor;
	private Cleaner<ReusableObject> cleaner;
	private Destructor<ReusableObject> destructor;

	public Pool() {
		maxSize = 256;
		availables = new LinkedList<ReusableObject>();
		unavailables = new LinkedList<ReusableObject>();
	}

	public ReusableObject pickOne() {
		if (!availables.isEmpty()) {
			ReusableObject pickedObject = availables.remove(0);
			unavailables.add(pickedObject);
			return pickedObject;
		}

		if (getSize() == maxSize) {
			unavailables.remove(0);
		}

		ReusableObject newObject = constructor.construct();
		unavailables.add(newObject);
		return newObject;
	}

	public void giveBack(ReusableObject reusableObject) {
		if (unavailables.remove(reusableObject)) {
			if (cleaner != null) {
				cleaner.clean(reusableObject);
			}
			availables.add(reusableObject);

		} else {
			if (destructor != null) {
				destructor.destruct(reusableObject);
			}
		}
	}

	public void discard(ReusableObject reusableObject) {
		unavailables.remove(reusableObject);
		if (destructor != null) {
			destructor.destruct(reusableObject);
		}
	}

	public void clear() {
		availables.clear();
		unavailables.clear();
	}

	public int getSize() {
		return availables.size() + unavailables.size();
	}

	public void setMaxSize(int maxSize) {
		if (maxSize < 1) {
			throw new RuntimeException("Max size must be at least 1.");
		}
		this.maxSize = maxSize;
	}

	public void setConstructor(Constructor<ReusableObject> constructor) {
		this.constructor = constructor;
	}

	public void setCleaner(Cleaner<ReusableObject> cleaner) {
		this.cleaner = cleaner;
	}

	public void setDestructor(Destructor<ReusableObject> destructor) {
		this.destructor = destructor;
	}

	public interface Constructor<ReusableObject> {
		public ReusableObject construct();
	}

	public interface Cleaner<ReusableObject> {
		public void clean(ReusableObject reusableInstance);
	}

	public interface Destructor<ReusableObject> {
		public void destruct(ReusableObject reusableInstance);
	}

}
