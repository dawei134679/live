package com.mpig.api.service;

import java.util.List;
import java.util.Map;
import com.mpig.api.model.LiveMicTimeModel;
import com.mpig.api.model.ReturnModel;
import com.mpig.api.model.UserBaseInfoModel;

public interface ILiveService {

	/**
	 * 获取正在开播的主播信息
	 * 
	 * @param uid
	 * @return
	 */
	public Map<String, LiveMicTimeModel> getLiveIngByUid(String... uids);

	/**
	 * 主播开播
	 * 
	 * @param uid
	 * @param slogan
	 * @param province
	 * @param city
	 * @return
	 */
	public Boolean startLive(String os,Integer uid, String slogan, String province,String city);

	/**
	 * 下麦
	 * 
	 * @param uid
	 *            主播UID
	 * @return
	 */
	public Boolean exitLive(Integer uid, ReturnModel returnModel);

	/**
	 * 麦时心跳
	 * 
	 * @param uid
	 *            主播UID
	 * @return
	 */
	public Boolean heartBeatLive(Integer uid);

	/**
	 * 获取用户/主播进房间的配置信息 如 Domain 服务器IP 服务器端口
	 * 
	 * @param srcUid
	 *            用户UID
	 * @param dstUid
	 *            主播UID
	 * @param videoline
	 *            线路
	 * @return
	 */
	public Map<String, Object> getVideoConfig(Integer srcUid, Integer dstUid,
			Integer videoline);

	/**
	 * 新增麦时记录
	 * 
	 * @param objs
	 * @return
	 */
	public int insertLiveMicTime(Object... objs);
	
	/**
	 * 获取该用户未下播的数据
	 * @param uid
	 * @param bl
	 * @return
	 */
	public LiveMicTimeModel getLiveMicInfoByUid(Integer uid,Boolean bl);
	
	/**
	 * 获取刚结束的主播数据
	 * @param uid
	 * @param bl
	 * @return
	 */
	public LiveMicTimeModel getliveMicInfoLivedByUid(Integer uid,Boolean bl);

	/**
	 * 下播
	 * 
	 * @param endtime
	 *            结束时间
	 * @param type
	 *            下播状态
	 * @param audience
	 *            观看人数
	 * @param views
	 *            观看人次
	 * @param likes
	 *            喜欢数
	 * @param credit
	 *            声援值
	 * @param id
	 *            麦时ID
	 * @return
	 */
	public int updLiveMicTime(Integer uid,Integer endtime, Boolean type, int audience,
			int views, int likes, int credit, Integer id);

	/**
	 * 获取麦时信息
	 * 
	 * @param id
	 * @return
	 */
	public int getLiveMicTimeById(int id);
	
	/**
	 * 设置管理员
	 * @param anchoruid 主播uid
	 * @param dstUid 用户uid
	 * @param type ＝on 添加管理员 ＝off 解除管理员
	 * @return ＝1成功 其他失败
	 */
	public int setAnchorManage(int anchoruid,int dstuid,String type);
	
	/**
	 * 通过主播uid和用户uid 获取管理信息
	 * @param srcUid 主播uid
	 * @param dstUid 用户uid
	 * @return true 是管理员 false 不是管理员
	 */
	public boolean checkLiveManage(int srcUid,int dstUid);
	
	/**
	 * 获取主播房间中的管理员列表
	 * @param anchor 主播uid
	 * @param bl true从数据库中读取 false从缓存中读取
	 * @return List<LiveManageModel>
	 */
	public Map<String, String> getManagelistOfAnchor(int anchor,boolean bl); 
	
	/**
	 * 收回主播开播权限
	 * @param manageid
	 * @param anchoruid
	 */
	public void closeRoom(int manageid,int anchoruid,String cause,ReturnModel returnModel);
	
	/**
	 * 禁播  后台操作
	 * @param anchoruid
	 * @param returnModel
	 */
	public void banRoomByManage(int anchoruid,ReturnModel returnModel);
	
	/**
	 * 解禁 后台操作
	 * @param anchoruid
	 * @param returnModel
	 */
	public void unBanRoomByManage(int anchoruid,ReturnModel returnModel);

	/**
	 * 关闭本场直播
	 * @param manageid
	 * @param anchoruid
	 */
	public void closeLive(int uid,ReturnModel returnModel);
	
	/**
	 * 发红包
	 * @param type ＝1 点对点发（对主播发） ＝2 点对点（对指定用户发） ＝3 点对多发
	 * @param srcUid 发红包人
	 * @param dstUid type＝1时 有该uid type＝2 则为0
	 * @param money 总金额
	 * @param count 红包数量
	 * @param reddesc 红包说明
	 * @param roomId 房间uid 即主播uid
	 */
	public void sendRedEnvelope(int srcUid,int dstUid,int money,int count,String reddesc,int roomId,ReturnModel returnModel);
	
	/**
	 * 在指定房间内 给指定人发红包
	 * @param srcUid 发红包人UID
	 * @param dstUid 收红包人UID
	 * @param money 金额
	 * @param count 个数
	 * @param reddesc 祝福语
	 * @param roomId 房间uid
	 * @param returnModel
	 */
	public void sendRedEnvelopeToOne(int srcUid,int dstUid,int money,int count,String reddesc,int roomId,ReturnModel returnModel);
	
	/**
	 * 抢红包
	 * @param dstUid 抢红包人
	 * @param envelopeId 红包id
	 * @param returnModel
	 */
	public void getRedEnvelope(int dstUid,int envelopeId,ReturnModel returnModel);
	
	/**
	 * 检查是否抢过红包
	 * @param envelopeid
	 * @param uid
	 * @param returnModel
	 */
	public void checkTriedEnvelope(int envelopeid,int uid,ReturnModel returnModel);
	

	/**
	 * 邀请连麦
	 * @param uid	邀请人
	 * @param dstuid	被邀请人=主播
	 * @param returnModel
	 */
	public void liveInvite(int uid,int dstuid,int isCanncel,ReturnModel returnModel);

	/**
	 * 确认连麦邀请
	 * @param uid	被邀请人=主播
	 * @param dstuid	邀请人
	 * @param returnModel
	 */
	public void liveAckInvite(int uid,int dstuid,int isjoin,ReturnModel returnModel);

	/**
	 * 连麦后的第2路流数据处理
	 * 连麦后的第2路流广播处理
	 * @param uSrc
	 * @param uDst
	 * @param isjoin
	 */
	void updateLiveMic(UserBaseInfoModel uSrc, UserBaseInfoModel uDst, int isjoin);

	/**
	 * 取消申请的连麦，退房时候，下播时候自动调用
	 * @param uSrc
	 */
	public void liveInviteCanncel(UserBaseInfoModel uSrc);

	/**
	 * check 2nd stream
	 * @param roomid
	 * @param returnModel
	 */
	public void check2ndStream(Integer roomid, ReturnModel returnModel);
	
	
	public List<Map<String, Object>> getLivedTimeList(int uid,String type,int page,int pageSize);
	
	public long getLivedTimeTotalCount(int uid,String type);
	
	public Map<String,Object> getLivedTimeSumary(int uid,String type);
	
	public void closeLiveInvite(int uid, ReturnModel returnModel);
}