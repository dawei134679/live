package com.mpig.api.service;

import java.util.List;
import java.util.Map;

public interface IRankService {

	/**
	 * 获取奥运活动中 用户的热度排行榜
	 * @param type =user用户 =anchor主播
	 * @return
	 */
	List<Map<String, Object>> getOlypicRank(String type);
	
	/**
	 * 获取奥运活动任务进展
	 * @param uid
	 * @return
	 */
	Map<String, Object> getOlypicStar(int uid);
	
	/**
	 * 中秋 收到月饼数及中奖排序
	 * @return
	 */
	Map<String,Object> getMidAutumn(String uid);
	
	/**
	 * 国庆 获取收到魅力排名 魅力值，及荣耀王者 第一名
	 * @param uid
	 * @return
	 */
	Map<String,Object> getNationalDay(String uid);
	
	/**
	 * 获取 魅力值排名 和 荣耀王者 排名
	 * @param type  =1荣耀(用户) =2魅力(主播)
	 * @return
	 */
	List<Map<String, Object>> getNationalDayRank(String type);
	
	/**
	 * 中秋 收到月饼排名
	 * @return
	 */
	List<Map<String, Object>> getMidReceivedRank();
	
	/**
	 * 中秋 送月饼中奖500倍次数排名
	 * @return
	 */
	List<Map<String, Object>> getMidMutilRank();

	/**
	 * 国庆 获取收到小猪排名 ，及送出小猪 第一名
	 * @param uid
	 * @return
	 */
	Map<String,Object> getXiaozhuRun(String uid);
	
	/**
	 * 获取 送出 和 收入 排名
	 * @param type  =1(用户) =2(主播)
	 * @return
	 */
	List<Map<String, Object>> getXiaozhuRunRank(String type);
	

	Map<String,Object> getHalloween(String uid);

	List<Map<String, Object>> getHalloweenRank(String type);


	Map<String,Object> getCommen(String uid,String anchorKey,String userKey);

	List<Map<String, Object>> getCommenRank(String type,String anchorKey,String userKey);
}
