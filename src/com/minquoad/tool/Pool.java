package com.minquoad.tool;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class Pool<ReusableInstance> {

	private Map<ReusableInstance, Boolean> store;

	private Instanciator<ReusableInstance> instanciator;
	private Cleaner<ReusableInstance> cleaner;

	public Pool(Instanciator<ReusableInstance> instanciator) {

		store = new HashMap<ReusableInstance, Boolean>();
		this.instanciator = instanciator;
	}

	public Pool(Instanciator<ReusableInstance> instanciator, Cleaner<ReusableInstance> cleaner) {
		this(instanciator);
		this.cleaner = cleaner;
	}

	public ReusableInstance pickOne() {
		for (Entry<ReusableInstance, Boolean> entry : store.entrySet()) {
			if (entry.getValue()) {
				entry.setValue(false);
				return entry.getKey();
			}
		}

		ReusableInstance newInstance = instanciator.getNew();
		store.put(newInstance, false);
		return newInstance;
	}

	public void giveBack(ReusableInstance reusableInstance) {
		if (store.containsKey(reusableInstance)) {
			if (cleaner != null) {
				cleaner.clean(reusableInstance);
			}
			store.put(reusableInstance, true);
		}
	}

	public void discard(ReusableInstance reusableInstance) {
		store.remove(reusableInstance);
	}
	
	public void clear() {
		store.clear();
	}
	
	public interface Instanciator<ReusableInstance> {
		public ReusableInstance getNew();
	}

	public interface Cleaner<ReusableInstance> {
		public void clean(ReusableInstance reusableInstance);
	}

}
