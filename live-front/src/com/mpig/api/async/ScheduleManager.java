package com.mpig.api.async;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ScheduleManager {

	private final static ScheduleManager instance = new ScheduleManager();

	public static ScheduleManager getInstance() {
		return instance;
	}

	private ScheduledExecutorService executor;

	public void initWith(int threadCount) {
		this.executor = Executors.newScheduledThreadPool(threadCount);
	}

	public void delay(Runnable runner) {
		delay(0, runner);
	}

	public void delay(int second, Runnable runner) {
		executor.schedule(runner, second, TimeUnit.SECONDS);
	}

	public void periodic(int period, Runnable runner) {
		periodic(0, period, runner);
	}

	public void periodic(int delay, int period, Runnable runner) {
		executor.scheduleWithFixedDelay(runner, delay, period, TimeUnit.SECONDS);
	}

}
