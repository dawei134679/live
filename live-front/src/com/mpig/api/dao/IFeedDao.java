package com.mpig.api.dao;

import java.util.List;

import com.mpig.api.model.UserFeedModel;
import com.mpig.api.model.UserFeedReplyModel;

public interface IFeedDao {

	/**
	 * 根据用户获取当前用户所有的状态
	 * @param uid
	 * @return
	 */
	List<UserFeedModel> getFeedByUser(Integer uid);
	
	/**
	 * 根据id获取动态详情
	 * @param id
	 * @return
	 */
	UserFeedModel getFeed(Integer id);
	
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
	int addFeedLaud(Integer feedId, Integer uid);
	
	/**
	 * 取消点赞
	 * @param feedId
	 * @param uid
	 * @return
	 */
	int delFeedLaud(Integer feedId, Integer uid);
	
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
	List<UserFeedReplyModel> getFeedReplyByFeedId(Integer feedId);
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
	int delFeedReply(Integer frId);
	
	/**
	 * 打赏动态
	 * @param feedId
	 * @param feedCUid
	 * @param rewardUid
	 * @param zhutou
	 * @return
	 */
	int addFeedReward(Integer feedId, Integer feedCUid, Integer rewardUid, Integer zhutou);
	
	/**
	 * 打赏礼物
	 * @param feedId 动态id
	 * @param feedCUid 动态发布人id
	 * @param rewardUid 打赏用户
	 * @param gift 礼物id
	 * @param count 数量
	 * @return
	 */
	int addFeedRewardGift(Integer feedId, Integer feedCUid, Integer rewardUid, Integer gift, Integer count);
	
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
	 * 获取推荐的主播
	 * @return
	 */
	public List<String> getRecommendAnchorId();
}
