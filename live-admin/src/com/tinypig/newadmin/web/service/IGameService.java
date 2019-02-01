package com.tinypig.newadmin.web.service;

import java.util.List;
import java.util.Map;

public interface IGameService {

	/**
	 * 获取押注及中奖列表
	 * @param uid
	 * @param stime
	 * @param etime
	 */
	public Map<String, Object> getBetWinList(int uid,String stime,String etime,int page,int size);
	
	/**
	 * 获取押注及中奖列表条数
	 * @param uid
	 * @param stime
	 * @param etime
	 */
	public int getBetWins(int uid,String stime,String etime);
	
	
	/**
	 * 获取指定动物的在一定时间内押注及中奖列表
	 * @param stime
	 * @param etime
	 * @return
	 */
	public List<Map<String, Object>> getNidBetWinlist(Integer nid,String stime,String etime);
	
	public int getNidBetWins(Integer nid,String stime,String etime);
}
