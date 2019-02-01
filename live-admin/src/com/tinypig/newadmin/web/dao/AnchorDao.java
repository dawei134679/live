package com.tinypig.newadmin.web.dao;

import java.util.List;
import java.util.Map;

public interface AnchorDao {

	List<Map<String, Object>> getPunishAnchorlistByUnion(int unionId);
	List<Map<String, Object>> getPunishAnchorListByUnionName(String unionName);
	
	/**
	 * 通过用户uid获取用户信息
	 * @param sufix
	 * @param anchoruid
	 * @return
	 */
	Map<String, Object> getPunishAnchorInfoByUid(String sufix,int anchoruid,Integer stime,Integer etime);
	
	/**
	 * 通过工会ID 获取工会下的主播列表
	 * @param unionId
	 * @return
	 */
	List<Map<String, Object>> getanchorlistByUnionId(int unionId);
	
	/**
	 * 通过工会名称 获取工会下的主播列表
	 * @param unionId
	 * @return
	 */
	List<Map<String, Object>> getanchorlistByUnionName(String unionName);
	
	/*
	 * 根据用户uid获取用户公会信息
	 */
	List<Map<String, Object>> getanchorlistByUid(int uid);
	
}
