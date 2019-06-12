package com.minquoad.service.cron;

import java.time.Instant;

public interface Cron {
	public void listenTime(Instant instant);
}
