package com.mpig.api.dao;

import java.util.List;

import com.mpig.api.model.InviteUserInfoModel;
import com.mpig.api.model.InviteUserPeckLogModel;
import com.mpig.api.model.InviteUserRewardInfoModel;

public interface IInviteUserInfoDao {

	
	/**
	 * 根据用户id查找当前的邀请人信息
	 * @param uid
	 * @return
	 */
	InviteUserInfoModel selInviteUserInfoByUid(Integer uid);
	/**
	 * 添加邀请人的任务进度及奖励信息
	 * @param uid 
	 * @param inviteCount 发起邀请人的有效邀请次数
	 * @param gets 发起邀请人的奖励总额 单位/猪头
	 * @return
	 */
	int addInviteUserInfo(Integer uid, Integer inviteCount, Integer gets);
	
	/**
	 * 增加用户邀请到的人数
	 * @param uid 
	 * @param inviteCount 需要新增的邀请人次 默认是1
	 * @return
	 */
	int updInviteCount(Integer uid, Integer inviteCount);
	
	/**
	 * 修改用户获取的资产额度
	 * @param uid
	 * @param gets
	 * @return
	 */
	int updInviteGets(Integer uid, Integer gets);
	/**
	 * 添加被邀请的用户至邀请人的奖励列表中
	 * @param uid 被邀请的人
	 * @param inviteUid 发起邀请的人
	 * @param status 状态 1未领取 2已领取
	 * @return
	 */
	int addInviteUserRewardInfo(Integer uid, Integer inviteUid, int status);
	
	/**
	 * 修改奖励列表的领取状态
	 * @param uid
	 * @param status
	 * @return
	 */
	int updInviteUserRewardStatus(Integer uid, int status);
	
	/**
	 * 根据邀请者id一键修改状态
	 * @param inviteUid
	 * @param status
	 * @return
	 */
	int updInviteUserRewardStatusByInviteUid(Integer inviteUid, int status);
	
	/**
	 * 根据邀请人查找已经邀请的用户及奖励领取状态
	 * @param inviteUid
	 * @return
	 */
	List<InviteUserRewardInfoModel> selInviteUserRewardByUid(Integer inviteUid);
	
	/**
	 * 根据用户查找当前已经领取过的任务阶段奖励
	 * @param uid
	 * @return
	 */
	List<InviteUserPeckLogModel> getInviteUserPeckLog(Integer uid);
	
	/**
	 * 插入阶段任务领取数据
	 * @param uid
	 * @param invitationId
	 * @param status
	 * @return
	 */
	int insInviteUserPeckLog(Integer uid, Integer invitationId, int status);
	
	/**
	 * 查询当前某个任务的领取情况
	 * @param uid
	 * @param taskRewardId
	 * @return
	 */
	int selInviteUserPeckLogByInvitationId(Integer uid, Integer taskRewardId);
}
