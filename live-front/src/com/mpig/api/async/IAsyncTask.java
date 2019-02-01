package com.mpig.api.async;

/**
 * @author jackzhang E-mail:519124189@qq.com
 * @version 创建时间：2012年2月28日 下午4:42:10
 */

public interface IAsyncTask {

	void runAsync();

	void afterOk();
	
	void afterError(Exception e);
	
	String getName();

}
