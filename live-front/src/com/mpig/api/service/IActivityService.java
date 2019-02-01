package com.mpig.api.service;

import java.util.List;
import java.util.Map;

import com.mpig.api.model.ReturnModel;

public interface IActivityService {

	/**
	 * 查询当前的抽奖剩余次数
	 * @param uid
	 * @return
	 */
	public int getLotteryCount(Integer uid);
	
	public int getSurplusLotteryCount(Integer uid);
	
	/**
	 *  新年回家抽奖
	 * @param uid
	 * @param returnModel
	 * @return
	 */
	public ReturnModel newYearLottery(int uid, ReturnModel returnModel);
	/**
	 * 扣除当前抽奖次数 --新年回家活动
	 * @param uid
	 * @return
	 */
	public int getNewYearLotteryCount(Integer uid);
	/**
	 * 获取新年回家活动抽奖剩余次数
	 * @param uid
	 * @return
	 */
	public int getNewYearSurplusCount(Integer uid);
	/**
	 * 获取当前邀请者的相关信息
	 * @param uid
	 * @return
	 */
	public Map<String, Object> getInviteInfo(Integer uid);
	
	/**
	 * 领取奖励
	 * @param uid
	 * @param taskRewardId
	 * @return
	 */
	public void getInviteReward(Integer uid, Integer taskRewardId,Integer forcebuy, ReturnModel returnModel);
	
	/**
	 * 
	 * @param uid
	 * @param inviteUid
	 * @param getAll
	 * @param returnModel
	 */
	public void getInviteUserReward(Integer uid, Integer inviteUid, Integer getAll, ReturnModel returnModel);
	
	/**
	 * 插入抽奖消耗记录
	 * @param uid
	 * @param activityId
	 * @param consume
	 * @param des
	 * @return
	 */
	public int insConsumeLottory(Integer uid,Integer activityId,Integer consume,String des);
	
	/**
	 * 插入猪头中奖纪录
	 * @param uid
	 * @param activityId
	 * @param reward
	 * @param des
	 * @return
	 */
	public int insRewardLottory(Integer uid,Integer activityId,Integer reward,String des);
	/**
	 * 获取猪头中奖纪录
	 * @param activityId
	 * @return
	 */
	public List<Map<String, Object>> getRewardLottoryList(int activityId);
}
