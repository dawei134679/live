package com.mpig.api.service;

import java.util.List;
import java.util.Map;

import com.mpig.api.model.UserFeedModel;
import com.mpig.api.model.UserFeedReplyModel;

public interface IFeedService {
	/**
	 * 根据用户获取当前用户所有的状态
	 * @param uid
	 * @return
	 */
	List<UserFeedModel> getFeedByUser(Integer uid,Integer dstUid, Integer page);
	
	/**
	 * 查找当前用户关注的动态
	 * @param uid
	 * @return
	 */
	List<UserFeedModel> getFeedByUserFlow(Integer uid, Integer page);
	
	/**
	 * 查找广场的动态
	 * @return
	 */
	List<UserFeedModel> getFeedBySquare(Integer uid, Integer page);
	/**
	 * 根据id获取动态详情
	 * @param id
	 * @return
	 */
	UserFeedModel getFeed(Integer id, Boolean directReadMysql);
	
	/**
	 * 根据多个id获取动态信息
	 * @param ids
	 * @return
	 */
	List<UserFeedModel> getFeed(Integer bucketIndex,Integer uid, String... ids);
	
	/**
	 * 根据id获取详情 并增加 评论 点赞 打赏次数
	 * @param id
	 * @return
	 */
	UserFeedModel getFeedById(Integer id, Integer uid);
	/**
	 * 添加动态
	 * @param uid
	 * @param content
	 * @return
	 */
	int addFeed(Integer uid, String content);
	
	/**
	 * 添加动态的图片
	 * @param feedId
	 * @param imgs
	 * @return
	 */
	int addFeedImgs(Integer feedId, String imgs);
	
	/**
	 * 删除动态
	 * @param feedId
	 * @return
	 */
	int delFeed(Integer feedId);
	
	/**
	 * 点赞
	 * @param feedId
	 * @param uid
	 * @return
	 */
	int addFeedLaud(Integer feedId, Integer uid, Integer type);
	
	/**
	 * 根据ID查询详情
	 * @param id
	 * @return
	 */
	UserFeedReplyModel getFeedReplyById(Integer id);
	/**
	 * 获取评论
	 * @param feedId
	 * @return
	 */
	List<UserFeedReplyModel> getFeedReplyByFeedId(Integer feedId, Integer page);
	/**
	 * 添加评论
	 * @param feedId
	 * @param fromUid
	 * @param toUid
	 * @param content
	 * @return
	 */
	int addFeedReply(Integer feedId, Integer parentFrId, Integer fromUid, Integer toUid,
			String content);
	
	/**
	 * 删除评论
	 * @param frId
	 * @return
	 */
	int delFeedReply(Integer frId, Integer feedId);
	
	/**
	 * 获取动态点赞的次数
	 * @param feedId
	 * @return
	 */
	int getFeedLaudCount(Integer feedId);
	
	/**
	 * 获取动态回复的次数
	 * @param feedId
	 * @return
	 */
	int getFeedReplyCount(Integer feedId);
	
	/**
	 * 获取动态打赏的次数
	 * @param feedId
	 * @return
	 */
	int getFeedRewardCount(Integer feedId);
	
	/**
	 * 打赏
	 * @param feedId
	 * @param feedCUid
	 * @param rewardUid
	 * @param zhutou
	 * @return
	 */
	int addFeedReward(Integer feedId, Integer feedCUid, Integer rewardUid, Integer zhutou);
	
	/**
	 * 获取当前动态的打赏列表
	 * @param feedId
	 * @return
	 */
	List<Map<String,Object>> getFeedReward(Integer feedId);
	
	/**
	 * 打赏礼物
	 * @param feedId
	 * @param feedCUid
	 * @param rewardUid
	 * @param zhutou
	 * @return
	 */
	int addFeedRewardGift(Integer feedId, Integer feedCUid, Integer rewardUid, Integer gift, Integer count);
	
	/**
	 * 获取当前动态的打赏礼物列表
	 * @param feedId
	 * @return
	 */
	List<Map<String,Object>> getFeedRewardGift(Integer feedId);
	
	
	
	/**
	 * 根据相片id获取举报记录
	 * @param fid
	 * @return
	 */
	public int getReportFeedByFid(Integer fid);
	
	/**
	 * 根据举报信息查看当前举报人是否举报过
	 * @param rid
	 * @param dstuid
	 * @return
	 */
	public int getReportFeedUserByRid(Integer rid, Integer dstuid);
	
	/**
	 * 添加相片举报的用户详情信息
	 * @param rid
	 * @param pid
	 * @param reportReason
	 * @param dstuid
	 * @return
	 */
	public int addReportFeedUser(Integer rid, Integer fid, String reportReason, Integer dstuid);
	
	/**
	 * 添加举报相册信息
	 * @param reportUid
	 * @param reportFid
	 * @return
	 */
	public int addReportFeed(Integer reportUid, Integer reportFid);
	
	/**
	 * 修改举报次数
	 * @param reportFid
	 * @return
	 */
	public int updReportFeed(Integer reportFid);
	
	/**
	 * 获取动态推荐主播
	 * @return
	 */
	public List<Map<String,Object>> getRecommendAnchor();
	
}
