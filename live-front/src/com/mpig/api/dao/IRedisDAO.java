package com.mpig.api.dao;

import java.util.List;

public interface IRedisDAO<Value> {
	/**
	 * 新增 <br>
	 * ------------------------------<br>
	 * 
	 * @param DemoModel
	 * @return
	 */
	boolean add(Value demoModel,String strKey);

	/**
	 * 批量新增 使用pipeline方式 <br>
	 * ------------------------------<br>
	 * 
	 * @param list
	 * @return
	 */
	boolean add(List<Value> list);
	
	/**
	 * 新增
	 * @param key
	 * @param value
	 * @return
	 */
	boolean add(String key,String val);

	/**
	 * 删除 <br>
	 * ------------------------------<br>
	 * 
	 * @param key
	 */
	void delete(String key);

	/**
	 * 删除多个 <br>
	 * ------------------------------<br>
	 * 
	 * @param keys
	 */
	void delete(List<String> keys);

	/**
	 * 修改 <br>
	 * ------------------------------<br>
	 * 
	 * @param DemoModel
	 * @return
	 */
	boolean update(Value demoModel);

	/**
	 * 通过key获取 <br>
	 * ------------------------------<br>
	 * 
	 * @param keyId
	 * @return
	 */
	Object get(String keyId,String type);
}
