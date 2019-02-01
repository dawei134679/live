package com.mpig.api.service;

import com.mpig.api.model.ReturnModel;
import com.mpig.api.modelcomet.BaseCMod;

public interface IPkService {
	
	/**
	 * 插入pk记录
	 * @param objs 参数严格按照sql中的占位符顺序
	 * @return
	 */
	public int insertPkRecord(Integer uid,Integer dstuid, Integer pkTime,Integer penaltyTime,Long firstUserVotes,Long secodUserVotes,Integer vinnerUid,Long createTime);
	
	/**
	 * 修改pk记录
	 * @param objs 参数严格按照sql中的占位符顺序
	 * @return
	 */
	public int updatePkRecord(Long firstUserVotes,Long secodUserVotes,Integer vinnerUid,Integer id);
	
	/**
	 * 查找可PK的人
	 * @param uid 主播ID
	 * @param dstuid 对方主播ID
	 * @param returnModel
	 */
	public void pkInquireInvitePK(int uid, int dstuid, ReturnModel returnModel);
	
	/**
	 * 发起连麦PK请求
	 * @param uid 主播ID
	 * @param dstuid 对方主播ID
	 * @param pktime PK时间
	 * @param penaltytime 惩罚时间
	 * @param istatus 状态标识（1:请求;0：取消）
	 * @param returnModel
	 */
	public void prepareInvitePk(int uid, int dstuid, int pktime, int penaltytime, int istatus,ReturnModel returnModel);
	
	/**
	 * 确认PK
	 * @param anchoruid 主播ID
	 * @param dstuid 副主播ID
	 * @param pktime PK时间
	 * @param penaltytime 惩罚时间
	 * @param cstatus 状态标识（1:确认;0：拒绝）
	 * @param returnModel
	 */
	public void confirmInvitePk(int anchoruid, int dstuid,int pktime,int penaltytime, int cstatus, ReturnModel returnModel);

	/**
	 * 开始PK
	 * @param anchoruid 主播ID(发起)
	 * @param dstuid 副主播ID
	 * @param pktime PK时长
	 * @param penaltytime 惩罚时长
	 * @param returnModel
	 */
	public void startPK(int anchoruid, int dstuid,int pktime,int penaltytime, ReturnModel returnModel);
	
	void sendMessage(Integer uid, BaseCMod baseCMod);

	void sendRoomMessage(Integer roomOwnerUid, BaseCMod baseCMod);

	public void getPKInfo(Integer anchoruid, ReturnModel returnModel);
}
