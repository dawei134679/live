package com.mpig.api.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mpig.api.model.ReturnModel;

public interface IRoomService {

	/**
	 * 用户进房间
	 * 
	 * @param srcUid
	 *            用户uid
	 * @param dstUid
	 *            主播uid
	 * @param returnModel
	 *            返回
	 */
	public void enterRoom(Integer srcUid, Integer dstUid,int os, ReturnModel returnModel);

	/**
	 * 游客进房
	 * @param srcUid
	 * @param dstUid
	 * @param os
	 * @param returnModel
	 */
	public void enterRoomVisitor(Integer dstUid,int os, ReturnModel returnModel);
	
	/**
	 * 机器人进房
	 * @param srcUid
	 * @param dstUid
	 * @param level
	 * @param nick
	 * @param avatar
	 * @param sex
	 * @return
	 */
	public Boolean robotEnterRoom(int srcUid,int dstUid,int level,String nick,String avatar,Boolean sex);

	/**
	 * 用户退出房间
	 * 
	 * @param srcUid
	 *            用户uid
	 * @param dstUid
	 *            主播uid
	 * @param returnModel
	 *            返回
	 */
	public void exitRoom(Integer srcUid, Integer dstUid, ReturnModel returnModel);

	/**
	 * 获取主播房间中的所有用户列表包含守护 vip 
	 * 
	 * @param dstUid
	 *            //主播uid
	 * @param returnModel
	 */
	public void allUserlistOfLive(int dstUid, int srcUid, Integer page, ReturnModel returnModel);
	/**
	 * 获取主播房间中的用户列表包含守护（PC）
	 * 
	 * @param dstUid
	 *            //主播uid
	 * @param returnModel
	 */
	public void userlistOfLiveForPc(int dstUid, int srcUid, Integer page, ReturnModel returnModel);
	
	/**
	 * 获取主播房间中的用户列表
	 * 
	 * @param dstUid
	 *            //主播uid
	 * @param srcUid
	 *            //主播uid
	 * @param page
	 *            //页数
	 * @param returnModel
	 */
	public void userlistOfLive(int dstUid, int srcUid, Integer page, ReturnModel returnModel);
	
	/**
	 * 获取主播房间中的守护列表
	 * 
	 * @param dstUid
	 *            //主播uid
	 * @param srcUid
	 *            //主播uid
	 * @param page
	 *            //页数
	 * @param returnModel
	 */
	public void guardUserlistOfLive(int dstUid, int srcUid, Integer page, ReturnModel returnModel);
	
	/**
	 * 获取主播房间中所有的守护列表 （包含不在房间的）
	 * 
	 * @param dstUid
	 *            //主播uid
	 * @param srcUid
	 *            //主播uid
	 * @param page
	 *            //页数
	 * @param returnModel
	 */
	public void guardUserAllList(int dstUid, int srcUid, Integer page, ReturnModel returnModel);
	/**
	 * 送礼功能
	 * 
	 * @param stamp
	 *            时间戳
	 * @param srcUid
	 *            送礼人uid
	 * @param dstUid
	 *            收礼人uid
	 * @param gid
	 *            礼物id
	 * @param count
	 *            礼物数量
	 */
	public void sendGift(String stamp, Integer srcUid, Integer dstUid, Integer anchoruid, int gid, int count, Byte os,
			int combo, int mengzhuSurplushCount,String iosVer, ReturnModel returnModel);

	/**
	 * 送礼功能
	 * 
	 * @param stamp
	 *            时间戳
	 * @param srcUid
	 *            送礼人uid
	 * @param dstUid
	 *            收礼人uid
	 * @param gid
	 *            礼物id
	 * @param count
	 *            礼物数量
	 */
	public void sendGiftBag(String stamp, Integer srcUid, Integer dstUid,Integer anchoruid, int gid, int count, Byte os, int combo,String iosVer,
			ReturnModel returnModel);

	/**
	 * 弹幕功能
	 * 
	 * @param srcUid
	 *            送礼人uid
	 * @param dstUid
	 *            收礼人uid
	 * @param gid
	 *            礼物id
	 * @param count
	 *            礼物数量
	 */
	public void sendDanmaku(Integer srcUid, Integer dstUid, int gid, Byte os, String msg, ReturnModel returnModel);

	/**
	 * 弹幕功能
	 * 
	 * @param srcUid
	 *            送礼人uid
	 * @param dstUid
	 *            收礼人uid
	 * @param gid
	 *            礼物id
	 * @param count
	 *            礼物数量
	 */
	public void sendDanmakuBag(Integer srcUid, Integer dstUid, int gid, Byte os, String msg, ReturnModel returnModel);
	
	/**
	 * 喇叭功能
	 * 
	 * @param srcUid
	 *            送礼人uid
	 * @param anchoruid
	 *            房间主播uid
	 * @param gid
	 *            礼物id
	 * @param count
	 *            礼物数量
	 */
	public void sendHorn(Integer srcUid, Integer anchoruid, int gid, Byte os, String msg, ReturnModel returnModel);
	
	/**
	 * 喇叭功能
	 * 
	 * @param srcUid
	 *            送礼人uid
	 * @param anchoruid
	 *            房间主播uid
	 * @param gid
	 *            礼物id
	 * @param count
	 *            礼物数量
	 */
	public void sendHornBag(Integer srcUid, Integer anchoruid, int gid, Byte os, String msg, ReturnModel returnModel);

	/**
	 * 获取开播列表
	 * 
	 * @param page
	 */
	public void getLiveingList(int oString,int page, ReturnModel returnModel, Integer srcuid);
	
	/**
	 * 获取手机开播列表
	 * @param page
	 * @param returnModel
	 * @param srcuid
	 */
	public void getMobileList( int page, ReturnModel returnModel, Integer srcuid);

	/**
	 * 获取最新入驻开播推荐
	 * @param os
	 * @param page
	 * @param returnModel
	 * @param srcuid
	 */
	public void getNewJoinLiveingList(int os, int page, ReturnModel returnModel, Integer srcuid);
	/**
	 * 获取PC端 首页上热门主播列表
	 * 
	 * @param page
	 */
	public void getHomeRecLiveingList(int oString,int page, ReturnModel returnModel, Integer srcuid);

	/**
	 * 获取PC端 首页上广场主播
	 * 
	 * @param page
	 */
	public void getSquareLiveList(int oString,int page, ReturnModel returnModel, Integer srcuid);

	/**
	 * 获取最新列表
	 * 
	 * @param page
	 * @param returnModel
	 */
	public void getHostList(int os,int page, ReturnModel returnModel, Integer srcuid);

	/**
	 * 首页关注列表
	 * 
	 * @param uid
	 */
	public void getFollowsHome(Integer uid, ReturnModel returnModel);

	/**
	 * 获取公告
	 * 
	 * @return
	 */
	public List<Map<String, Object>> getNotice();

	/**
	 * 推荐搜索
	 * 
	 * @param uid
	 * @param returnModel
	 */
	void getReCommend(int os,int uid, ReturnModel returnModel);
	
	/**
	 * 获取排行榜
	 * @param typeUser =anchor主播 =user用户
	 * @param typeTimes =day日榜 =week周榜 =month月榜 =all总榜
	 * @param returnModel
	 */
	void getUserRank(int srcuid,String typeUser,String typeTimes,ReturnModel returnModel);

	/**
	 * 获取排行榜
	 * @param typeUser =anchor主播 =user用户
	 * @param typeTimes =day日榜 =week周榜 =month月榜 =all总榜
	 * @param returnModel
	 */
	void getUserRankRq(int srcuid,String typeTimes,ReturnModel returnModel);

	/**
	 * 获取人气排行榜
	 * @param type =0当日 =1昨日
	 * @param num = 获取数量
	 * @param returnModel
	 */
	void getUserRankRqPC(Integer type,int num,ReturnModel returnModel);
	
	/**
	 * 获取排行榜
	 * @param returnModel
	 */
	void getRanks(ReturnModel returnModel);
	
	/**
	 * 获取周星
	 * @param typeUser =anchor主播 =user用户
	 * @param times =0本周 =1上周
	 * @param returnModel
	 */
	void getZhouxin(String typeUser,int times,ReturnModel returnModel);
	
	/**
	 * 获取每种周星礼物的头名用户信息
	 * @param returnModel
	 */
	void getZhouxinPC(ReturnModel returnModel);
	
	/**
	 * 获取周星
	 * @param typeUser =anchor主播 =user用户
	 * @param times =0本周 =1上周
	 * @param returnModel
	 */
	void getZhouxinOfPerson(int anchor,ReturnModel returnModel);
	
	/**
	 * 用户等级变更
	 * @param srcuid 送出礼物/获取经验值uid
	 * @param dstuid 收到礼物uid
	 * @param type =1用户 =2主播
	 * @param source =1送礼或收礼 =2活动获取经验值
	 * @return
	 */
	boolean updUserLevel(int srcuid,int dstuid,int anchoruid,int type,int source);
	
	
	/**
	 * 获取最新列表 的推荐结果
	 * 
	 * @param typeUser
	 *            =anchor主播 =user用户
	 * @param returnModel
	 */
	public void getRecommandByNow(int srcuid, ReturnModel returnModel);

	/**
	 * 获取最新列表 的推荐结果
	 * 
	 * @param typeUser
	 *            =anchor主播 =user用户
	 * @param returnModel
	 */
	public void getLivingForeign(HashMap<String, Object> returnModel);
	
	/**
	 * 注册时推荐10个用户
	 * @param page
	 * @return
	 */
	public void getRecommendForRegister(ReturnModel returnModel);
	
	/**
	 * 显示房间中的人数
	 * @param anchoruid 主播UID
	 * @param virtuals 后台设置的虚拟用户数
	 * @return
	 */
	public int getRoomShowUsers(int anchoruid,int virtuals);
	
	/**
	 * 获取推荐2个主播(PC端 主播下播时 使用)
	 * @param uid  待排重的uid
	 * @param num  推荐的数量
	 * @param returnModel
	 */
	public void getRecommendTwoFlash(String uid,int num,ReturnModel returnModel);
	

	
	/**
	 * 获取推荐2个主播(PC端 主播下播时 使用)
	 * @param uid  待排重的uid
	 * @param num  推荐的数量
	 * @param returnModel
	 */
	public void randomAnchor(String uid,ReturnModel returnModel);
	
	/**
	 * 后台添加声援值 业务
	 * @param uid
	 * @param credit
	 */
	public void addCreditByAdmin(int uid,int credit);
	
	/**
	 * 根据游戏ID获取两个最热的游戏直播间
	 * @param gameId
	 * @param useruid
	 * @param os
	 * @return
	 */
	public List<Map<String,Object>> getHotGameAnchorList(Long gameId, Integer useruid,int os);
	
	/**
	 * 根据游戏ID获取游戏直播间
	 * @param gameId
	 * @param useruid
	 * @param os
	 * @return
	 */
	public List<Map<String,Object>> gameAnchorListByGameId(Long gameId, Integer useruid,int os,int page);

	/**
	 * 获取直播间信息
	 * @param uid 用户id
	 * @param anchorId 主播ID
	 * @param returnModel 
	 */
	public void getRoomInfoByAnchorId(Integer uid,Integer anchorId,int os,ReturnModel returnModel);
	
	/**
	 * 获取主播声援值月榜
	 * @param returnModel 
	 */
	public void getAnchorMonthSupportRank(int srcuid,ReturnModel returnModel);
}