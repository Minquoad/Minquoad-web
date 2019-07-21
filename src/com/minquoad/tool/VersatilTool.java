package com.minquoad.tool;

public abstract class VersatilTool {
	
	public static int toBoundedInt(long l) {
		if (l > (long) Integer.MAX_VALUE) {
			return Integer.MAX_VALUE;
		}
		if (l < (long) Integer.MIN_VALUE) {
			return Integer.MIN_VALUE;
		}
		return (int) l;
	}
	
}
