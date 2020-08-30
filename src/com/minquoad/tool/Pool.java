package com.minquoad.tool;

import java.util.LinkedList;
import java.util.List;

public class Pool<ReusableObject> {

	private ReusableObjectFactory<ReusableObject> factory;

	private int maxSize;

	private List<ReusableObject> availables;
	private List<ReusableObject> unavailables;

	public Pool(ReusableObjectFactory<ReusableObject> factory, int maxSize) {
		this.factory = factory;
		this.maxSize = maxSize;
		availables = new LinkedList<ReusableObject>();
		unavailables = new LinkedList<ReusableObject>();
	}

	public Pool(ReusableObjectFactory<ReusableObject> factory) {
		this(factory, 256);
	}

	public synchronized ReusableObject pickOne() {
		if (availables.isEmpty()) {
			ReusableObject newObject = factory.construct();
			unavailables.add(newObject);

			if (unavailables.size() > maxSize)
				unavailables.remove(0);

			return newObject;

		} else {
			ReusableObject pickedObject = availables.remove(0);
			unavailables.add(pickedObject);
			return pickedObject;
		}
	}

	public synchronized void giveBack(ReusableObject reusableObject) {
		if (unavailables.remove(reusableObject)) {
			factory.clean(reusableObject);
			availables.add(reusableObject);

		} else {
			if (!availables.contains(reusableObject)) {
				factory.destruct(reusableObject);
			}
		}
	}

	public synchronized void discard(ReusableObject reusableObject) {
		availables.remove(reusableObject);
		unavailables.remove(reusableObject);
		factory.destruct(reusableObject);
	}

	public synchronized void clear() {
		while (!availables.isEmpty())
			factory.destruct(availables.remove(0));
		unavailables.clear();
	}

	public synchronized int getSize() {
		return availables.size() + unavailables.size();
	}

	public synchronized int getNumberOfAvalables() {
		return availables.size();
	}

	public synchronized void prepare(int number) {
		if (number < maxSize)
			throw new RuntimeException("Number to prepare acceed max size.");

		while (availables.size() < number)
			availables.add(factory.construct());

		while (this.getSize() > maxSize)
			unavailables.remove(0);
	}

	public synchronized void setMaxSize(int maxSize) {
		if (maxSize < 0)
			throw new RuntimeException("Max size can not be negative.");

		while (getSize() > maxSize && !availables.isEmpty())
			discard(availables.get(0));

		while (getSize() > maxSize && !unavailables.isEmpty())
			unavailables.remove(0);

		this.maxSize = maxSize;
	}

	public interface ReusableObjectFactory<ReusableObject> {
		public ReusableObject construct();

		public void clean(ReusableObject reusableInstance);

		public void destruct(ReusableObject reusableInstance);
	}

}
