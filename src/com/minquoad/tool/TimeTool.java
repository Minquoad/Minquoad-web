package com.minquoad.tool;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;

public abstract class TimeTool {

	public static Instant toInstant(String str, ZoneOffset zoneOffset) {
		return toLocalDate(str).atStartOfDay().toInstant(zoneOffset);
	}

	public static LocalDate toLocalDate(String str) {
		return LocalDate.parse(str);
	}

}
