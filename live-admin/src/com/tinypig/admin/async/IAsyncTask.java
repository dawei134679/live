package com.tinypig.admin.async;

public interface IAsyncTask {

	void runAsync();

	void afterOk();
	
	void afterError(Exception e);
	
	String getName();

}
