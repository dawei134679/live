package com.tinypig.admin.async;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.log4j.Logger;

public class AsyncManager {

	private static Logger log = Logger.getLogger(AsyncManager.class);

	private final static AsyncManager instance = new AsyncManager();

	public static AsyncManager getInstance() {
		return instance;
	}

	private ExecutorService executors = Executors.newCachedThreadPool();

	public void execute(final IAsyncTask runner) {
		if (runner == null) {
			log.error("execute>>>>++++runner is null");
			return;
		}
		try {
			executors.execute(new Runnable() {
				@Override
				public void run() {
					try {
						runner.runAsync();
					} catch (Exception e) {
						log.error("async running error", e);
					}
				}

			});
		} catch (Exception ex) {
			log.error("execute>>>>++++", ex);
		}
	}

	public void execute(final Runnable runnable) {
		executors.execute(runnable);
	}

	public void onDestroyed() {
		if (this.executors != null) {
			this.executors.shutdown();
		}
	}
}
