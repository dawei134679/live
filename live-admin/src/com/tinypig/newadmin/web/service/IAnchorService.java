package com.tinypig.newadmin.web.service;

import java.util.List;
import java.util.Map;

public interface IAnchorService {

	/**
	 * 处罚查询-获取主播信息
	 * @param unionId 工会unionid
	 * @return
	 */
	public List<Map<String, Object>> getPunishAnchorlistByUnion(int unionId);
	
	/**
	 * 处罚查询-获取主播信息
	 * @param unionName 工会名字
	 * @return
	 */
	public List<Map<String, Object>> getPunishAnchorListByUnionName(String unionName,Integer stime,Integer etime);
	
	/**
	 * 处罚查询-获取主播信息
	 * @param anchoruid 主播uid
	 * @return
	 */
	public List<Map<String, Object>> getPunishAnchorListByUid(String sufix,Integer anchoruid,Integer stime,Integer etime);
	
	/**
	 * 通过主播uid获取 处罚查询 -主播信息
	 * @param sufix
	 * @param anchoruid
	 * @return
	 */
	public Map<String, Object> getPunishAnchorInfoByUid(String sufix,int anchoruid,Integer stime,Integer etime);
}