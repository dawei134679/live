package com.mpig.api.service;

import java.util.List;
import java.util.Map;

import com.mpig.api.model.ReturnModel;

public interface IGameService {
	
	/**
	 * 下注  记录下注日志，扣除下注金额
	 * @param orderId 订单ID
	 * @param roomid 下注房间ID
	 * @param srcUid 下注人uid
	 * @param money 下注金额
	 * @param nid 动物ID
	 * @param type =1内环 =2外环
	 */
	public void addBets(String orderId,int roomid,int srcUid,int money,int nid,int type,ReturnModel returnModel);
	
	/**
	 * 中奖
	 * @param orderId 订单ID
	 * @param roomid 下注房间ID
	 * @param innerid 内圈 动物ID
	 * @param outerid 外圈 动物ID
	 * @param betTime 结果时间
	 */
	public List<Map<String, Object>> BetResult(String orderId,int roomid,int innerid,int outerid);
	
	/**
	 * 中奖上跑道
	 * @param orderId
	 */
	public void sendRunway(String orderId);
	
	/**
	 * 加入砸蛋的日志记录信息
	 * @param hammerPrice 锤子价格
	 * @param hammerCount 锤子数量
	 * @param roomId 房间id
	 * @param rewardGiftId 奖励的礼物id
	 * @param rewardGiftName 奖励的礼物名称
	 * @param rewardGiftPrice 奖励的礼物价格
	 * @param rewardGiftCount 奖励的礼物数量
	 * @param rewardGiftTotalPrice 奖励的礼物总价
	 * @return
	 */
	public int addSmashedEggLog(Integer uid, Integer hammerPrice, Integer hammerCount, Integer roomId, Integer rewardGiftId, String rewardGiftName, Integer rewardGiftPrice, Integer rewardGiftCount,Integer rewardGiftTotalPrice, Integer rewardGiftType);
}
