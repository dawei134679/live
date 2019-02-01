package com.mpig.api.async;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.log4j.Logger;

/**
 * @author jackzhang E-mail:519124189@qq.com
 * @version 创建时间：2012年2月28日 下午4:42:10
 */

public class AsyncManager {
	private static Logger LOG = Logger.getLogger(AsyncManager.class);

	private final static AsyncManager instance = new AsyncManager();

	public static AsyncManager getInstance() {
		return instance;
	}

	public void initWith(int threadCount) {
		this.executors = Executors.newFixedThreadPool(threadCount);
	}

	private ExecutorService executors;

	public void execute(final IAsyncTask runner) {
		if(runner == null){
			LOG.error("execute>>>>++++runner is null");
			return;
		}
		try{
			executors.execute(new Runnable() {
				@Override
				public void run() {
					long start = System.currentTimeMillis();
					Exception error = null;
					try {
						runner.runAsync();
					} catch (Exception e) {
						LOG.error("async running error", e);
						error = e;
					}
					long end = System.currentTimeMillis();
					if(end - start >= 20l){
						LOG.warn(String.format("execute async runner %s cost %s", runner.getClass().getName(), String.valueOf(end - start)));
					}

					if (error == null) {
						runner.afterOk();
					} else {
						runner.afterError(error);
					}
				}

			});
		}catch (Exception ex){
			LOG.error("execute>>>>++++",ex);
		}
	}
	
	public void execute(final Runnable runnable) {
		executors.execute(runnable);
	}
	
	public void onDestroyed(){
		if(this.executors != null){
			this.executors.shutdown();
		}
	}
}
