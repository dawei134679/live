package com.mpig.api.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.mpig.api.async.AsyncManager;
import com.mpig.api.async.IAsyncTask;
import com.mpig.api.dao.IFeedDao;
import com.mpig.api.model.UserAlbumModel;
import com.mpig.api.model.UserBaseInfoModel;
import com.mpig.api.model.UserFeedModel;
import com.mpig.api.model.UserFeedReplyModel;
import com.mpig.api.modelcomet.FeedNoticeCmod;
import com.mpig.api.redis.service.RedisCommService;
import com.mpig.api.redis.service.RelationRedisService;
import com.mpig.api.redis.service.UserRedisService;
import com.mpig.api.service.IConfigService;
import com.mpig.api.service.IFeedService;
import com.mpig.api.service.ILiveService;
import com.mpig.api.service.IUserAlbumService;
import com.mpig.api.service.IUserService;
import com.mpig.api.utils.ChatMessageUtil;
import com.mpig.api.utils.RedisContant;

import redis.clients.jedis.Tuple;

@Service
public class FeedServiceImpl implements IFeedService{
	private static final List<Map<String,Object>> feedRecommendAnchor = new ArrayList<Map<String, Object>>();
	private static Long lastTime = 0l;

	private static final Logger logger = Logger.getLogger(FeedServiceImpl.class);

	private static final int replycount = 20; //评论回复每页20条
	private static final int feedcount = 20; //评论回复每页20条

	@Autowired
	private IFeedDao feedDao;
	@Resource
	private IUserService userService;
	@Resource
	private ILiveService liveService;
	@Resource
	private IConfigService configService;
	@Resource
	private IUserAlbumService userAlbumService;
	
	public int getBucketIndex(Integer id){
		int bucketIndex = id/1000;
		return bucketIndex;
	}
	
	@Override
	public List<UserFeedModel> getFeedByUser(Integer uid, Integer dstUid, Integer page) {
		Set<Tuple> set = RedisCommService.getInstance().zrevrangeWithScores(RedisContant.RedisNameFeed, RedisContant.FeedUser+dstUid, (page - 1) * feedcount, page * feedcount - 1);
		Map<Integer, List<String>> bucketUidMap = new HashMap<Integer, List<String>>();
		if (set != null) {
			for (Tuple tuple : set) {
				int bucketIndex = getBucketIndex(Integer.parseInt(tuple.getElement()));
				List<String> list = bucketUidMap.get(bucketIndex);
				if (list == null) {
					list = new ArrayList<String>();
				}
				list.add(tuple.getElement());
				bucketUidMap.put(bucketIndex, list);
			}
		}
		List<UserFeedModel> feedList = new ArrayList<UserFeedModel>();
		for(Integer bucketIndex : bucketUidMap.keySet()){
			List<String> list = bucketUidMap.get(bucketIndex);
			List<UserFeedModel> feeds = getFeed(bucketIndex,uid, list.toArray(new String[0]));
			feedList.addAll(feeds);
		}
		return feedList;
	}

	@Override
	public List<UserFeedModel> getFeedByUserFlow(Integer uid, Integer page) {
		Set<Tuple> set = RedisCommService.getInstance().zrevrangeWithScores(RedisContant.RedisNameFeed, RedisContant.FeedUserFollow+uid, (page - 1) * feedcount, page * feedcount - 1);
		Map<Integer, List<String>> bucketUidMap = new HashMap<Integer, List<String>>();
		if (set != null) {
			for(Tuple tuple: set){
				int bucketIndex = getBucketIndex(Integer.parseInt(tuple.getElement()));
				List<String> list = bucketUidMap.get(bucketIndex);
				if(list==null){
					list = new ArrayList<String>();
				}
				list.add(tuple.getElement());
				bucketUidMap.put(bucketIndex, list);
			}
		}
		List<UserFeedModel> feedList = new ArrayList<UserFeedModel>();
		for(Integer bucketIndex : bucketUidMap.keySet()){
			List<String> list = bucketUidMap.get(bucketIndex);
			List<UserFeedModel> feeds = getFeed(bucketIndex, uid, list.toArray(new String[0]));
			feedList.addAll(feeds);
		}
		return feedList;
	}

	@Override
	public List<UserFeedModel> getFeedBySquare(Integer uid, Integer page) {
		Set<Tuple> set = RedisCommService.getInstance().zrevrangeWithScores(RedisContant.RedisNameFeed, RedisContant.FeedSquare, (page - 1) * feedcount, page * feedcount - 1);
		Map<Integer, List<String>> bucketUidMap = new HashMap<Integer, List<String>>();
		if (set != null) {
			for(Tuple tuple: set){
				int bucketIndex = getBucketIndex(Integer.parseInt(tuple.getElement()));
				List<String> list = bucketUidMap.get(bucketIndex);
				if(list==null){
					list = new ArrayList<String>();
				}
				list.add(tuple.getElement());
				bucketUidMap.put(bucketIndex, list);
			}
		}
		List<UserFeedModel> feedList = new ArrayList<UserFeedModel>();
		for(Integer bucketIndex : bucketUidMap.keySet()){
			List<String> list = bucketUidMap.get(bucketIndex);
			List<UserFeedModel> feeds = getFeed(bucketIndex, uid, list.toArray(new String[0]));
			feedList.addAll(feeds);
		}
		return feedList;
	}

	@Override
	public UserFeedModel getFeed(Integer id, Boolean directReadMysql) {
		int bucketIndex = getBucketIndex(id);
		UserFeedModel userFeedModel = null;
		if (!directReadMysql) {
			String feedInfo = RedisCommService.getInstance().hget(RedisContant.RedisNameFeed, RedisContant.FeedInfo+bucketIndex, id.toString());
			if (StringUtils.isNotEmpty(feedInfo)) {
				userFeedModel = (UserFeedModel) JSONObject.parseObject(feedInfo, UserFeedModel.class);
			}
		}
		if(userFeedModel ==null){
			userFeedModel = feedDao.getFeed(id);
			if(userFeedModel != null){
				RedisCommService.getInstance().hset(RedisContant.RedisNameFeed, RedisContant.FeedInfo+bucketIndex, id.toString(), JSONObject.toJSONString(userFeedModel), 0);	
			}
		}
		return userFeedModel;
	}
	
	@Override
	public UserFeedModel getFeedById(Integer id, Integer uid) {
		UserFeedModel feed = getFeed(id, false);
		if(feed != null){
			UserBaseInfoModel userbaseInfoByUid = userService.getUserbaseInfoByUid(feed.getUid(), false);
			if (userbaseInfoByUid != null) {
				feed.setHeadimage(userbaseInfoByUid.getHeadimage());
				feed.setNickname(userbaseInfoByUid.getNickname());
			}
			feed.setLaudCount(getFeedLaudCount(id));
			feed.setReplyCount(getFeedReplyCount(id));
			feed.setRewardCount(getFeedRewardCount(id)); 
			String isLaud = RedisCommService.getInstance().hget(RedisContant.RedisNameFeed, RedisContant.FeedLaud+feed.getId().toString(), uid.toString());
			feed.setIsLaud(StringUtils.isEmpty(isLaud)?false:true);
		}
		return feed;
	}

	@Override
	public List<UserFeedModel> getFeed(Integer bucketIndex,Integer uid, String... ids) {
		List<UserFeedModel> list = new ArrayList<UserFeedModel>();
		List<String> idsList = new ArrayList<String>();
		idsList = Arrays.asList(ids);
		
		List<String> queryList = new ArrayList<String>();
		List<String> UserFeedInfo = RedisCommService.getInstance().hget(RedisContant.RedisNameFeed, RedisContant.FeedInfo+bucketIndex, ids);
		if (UserFeedInfo != null) {
			for (String string : UserFeedInfo) {
				UserFeedModel userFeedModel = null;
				userFeedModel = (UserFeedModel) JSONObject.parseObject(string, UserFeedModel.class);
				if (userFeedModel != null) {
					UserBaseInfoModel userbaseInfoByUid = userService.getUserbaseInfoByUid(userFeedModel.getUid(), false);
					if (userbaseInfoByUid != null) {
						userFeedModel.setHeadimage(userbaseInfoByUid.getHeadimage());
						userFeedModel.setNickname(userbaseInfoByUid.getNickname());
					}
					userFeedModel.setLaudCount(getFeedLaudCount(userFeedModel.getId()));
					userFeedModel.setReplyCount(getFeedReplyCount(userFeedModel.getId()));
					userFeedModel.setRewardCount(getFeedRewardCount(userFeedModel.getId()));
					String isLaud = RedisCommService.getInstance().hget(RedisContant.RedisNameFeed, RedisContant.FeedLaud+userFeedModel.getId().toString(), uid.toString());
					userFeedModel.setIsLaud(StringUtils.isEmpty(isLaud)?false:true);
					list.add(userFeedModel);
					
					queryList.add(userFeedModel.getId().toString());
				}
			}
		}
		if(idsList.size()>0){
			List<String> tmpList = new ArrayList<String>();
			for (String id : idsList) {
				tmpList.add(id);
			}
			tmpList.removeAll(queryList);
			if(tmpList.size()>0){
				RedisCommService.getInstance().zrem(RedisContant.RedisNameFeed, RedisContant.FeedUserFollow+uid, tmpList.toArray(new String[0]));
				RedisCommService.getInstance().zrem(RedisContant.RedisNameFeed, RedisContant.FeedSquare, tmpList.toArray(new String[0]));
			}
		}
		return list;
	}

	@Override
	public int addFeed(Integer uid, String content) {
		Integer feedId = feedDao.addFeed(uid, content);
		if(feedId > 0){
			UserFeedModel feed = getFeed(feedId, false);
			if(feed != null){
				int bucketIndex = getBucketIndex(feedId);
				UserBaseInfoModel userBaseInfoModel = userService.getUserbaseInfoByUid(uid, false);
				
				if(userBaseInfoModel == null){
					return -1;
				}
				feed.setCity(userBaseInfoModel.getCity());
				feed.setNickname(userBaseInfoModel.getNickname());
				feed.setHeadimage(userBaseInfoModel.getHeadimage());
				feed.setSex(userBaseInfoModel.getSex());
				feed.setVerified(userBaseInfoModel.isVerified());
				RedisCommService.getInstance().hset(RedisContant.RedisNameFeed, RedisContant.FeedInfo+bucketIndex, feedId.toString(), JSONObject.toJSONString(feed), 0);

				try{
					AsyncManager.getInstance().execute(new PassFeedTask(uid, feedId));
				}catch (Exception ex){
					logger.error("PassFeedTask>>>",ex);
				}


				return feedId;
			}else{
				return -1;
			}
		}
		return -1;
	}

	/**
	 * 动态发布的同步操作
	 * @author zyl
	 * @date 2016-12-9 下午3:24:10
	 */
	private class PassFeedTask implements IAsyncTask{
		private Integer uid;
		private Integer feedId;
		
		public PassFeedTask(Integer uid, Integer feedId){
			this.uid = uid;
			this.feedId = feedId;
		}
		@Override
		public void runAsync() {
			Long now = System.currentTimeMillis()/1000;
			//获取当前用户所有的粉丝 并给所有的粉丝关注动态列表增加动态
			Set<String> set = RelationRedisService.getInstance().getFans(uid, 0);
			if(set != null){
				for(String uidstr : set){
					int uid = Integer.parseInt(uidstr);
					if(uid < 900000000){
						RedisCommService.getInstance().zadd(RedisContant.RedisNameFeed, RedisContant.FeedUserFollow+uid, now.intValue(), feedId.toString());
					}
				}	
			}
			
			// 认证列表
			String auths = RedisCommService.getInstance().get(RedisContant.RedisNameUser, RedisContant.AllAuth+uid);
			
			JSONObject jsonObject = JSONObject.parseObject(auths);
			
			//实名认证用户同步至至广场
			if (jsonObject.containsKey("realnameAuth") && jsonObject.getIntValue("realnameAuth") == 3) {
				RedisCommService.getInstance().zadd(RedisContant.RedisNameFeed, RedisContant.FeedSquare, now.intValue(), feedId.toString());
			}
			//同步至我的动态缓存
			RedisCommService.getInstance().zadd(RedisContant.RedisNameFeed, RedisContant.FeedUser+uid, now.intValue(), feedId.toString());
		}
		@Override
		public void afterOk() {
			
		}
		@Override
		public void afterError(Exception e) {
			
		}
		@Override
		public String getName() {
			return "PassFeedTask";
		}
	}
	@Override
	public int addFeedImgs(Integer feedId, String imgs) {
		Integer rec = feedDao.addFeedImgs(feedId, imgs);
		if(rec > 0){
			UserFeedModel feed = getFeed(feedId, true);
			if(feed != null){
				int bucketIndex = getBucketIndex(feedId);
				UserBaseInfoModel userBaseInfoModel = userService.getUserbaseInfoByUid(feed.getUid(), false);
				if(userBaseInfoModel == null){
					return -1;
				}
				feed.setCity(userBaseInfoModel.getCity());
				feed.setNickname(userBaseInfoModel.getNickname());
				feed.setHeadimage(userBaseInfoModel.getHeadimage());
				feed.setSex(userBaseInfoModel.getSex());
				feed.setVerified(userBaseInfoModel.isVerified());
				RedisCommService.getInstance().hset(RedisContant.RedisNameFeed, RedisContant.FeedInfo+bucketIndex, feedId.toString(), JSONObject.toJSONString(feed), 0);
				return 1;
			}else{
				return -1;
			}
		}
		return -1;
	}

	@Override
	public int delFeed(Integer feedId) {
		Integer rec = feedDao.delFeed(feedId);
		if(rec > 0){
			int bucketIndex = getBucketIndex(feedId);
			RedisCommService.getInstance().hdel(RedisContant.RedisNameFeed, RedisContant.FeedInfo + bucketIndex, feedId.toString());

			//删除赞赏
			RedisCommService.getInstance().del(RedisContant.RedisNameFeed, RedisContant.FeedReward + feedId);
			RedisCommService.getInstance().del(RedisContant.RedisNameFeed, RedisContant.FeedRewardCount + feedId);
			//删除回复
			RedisCommService.getInstance().del(RedisContant.RedisNameFeed, RedisContant.FeedReply + feedId);
			RedisCommService.getInstance().del(RedisContant.RedisNameFeed, RedisContant.FeedReplyCount + feedId);
			//删除点赞
			RedisCommService.getInstance().del(RedisContant.RedisNameFeed, RedisContant.FeedLaud + feedId);
			RedisCommService.getInstance().del(RedisContant.RedisNameFeed, RedisContant.FeedLaudCount + feedId);
			
			RedisCommService.getInstance().zrem(RedisContant.RedisNameFeed, RedisContant.FeedSquare, feedId.toString());
			UserFeedModel feed = getFeed(feedId, false);
			if(feed != null){
				RedisCommService.getInstance().zrem(RedisContant.RedisNameFeed, RedisContant.FeedUser+feed.getUid(), feedId.toString());	
			}
			
			return rec;
		}
		return -1;
	}

	@Override
	public int addFeedLaud(Integer feedId, Integer uid, Integer type) {
		Long now = System.currentTimeMillis()/1000;
		if(type == 1){
			int rec = feedDao.addFeedLaud(feedId, uid);
			if(rec > 0){
				RedisCommService.getInstance().incrBy(RedisContant.RedisNameFeed, RedisContant.FeedLaudCount+feedId, 1, 0);
				RedisCommService.getInstance().hset(RedisContant.RedisNameFeed, RedisContant.FeedLaud+feedId, uid.toString(), now.toString(), 0);
				
				
//				UserFeedModel feed = getFeed(feedId, false);
//				if(feed != null){
//					if(feed.getUid().intValue() != uid.intValue()){
//						UserBaseInfoModel userBaseInfoModel = userService.getUserbaseInfoByUid(uid, false);
//						HashMap<String, Object> map = new HashMap<String, Object>();
//			    		map.put("nickname", userBaseInfoModel.getNickname());
//			    		map.put("level", userBaseInfoModel.getUserLevel());
//			    		map.put("islink", true);
//			    		map.put("sex", userBaseInfoModel.getSex());
//			    		map.put("uid", userBaseInfoModel.getUid());
//			    		map.put("type", FeedNoticeCmod.TYPE_LAUD);
//						ChatMessageUtil.sendFeedNotice(feed.getUid(), feedId, map);
//					}
//				}
			}
			return rec;
		}else if(type == 0){
			int rec = feedDao.delFeedLaud(feedId, uid);
			if(rec > 0){
				RedisCommService.getInstance().incrBy(RedisContant.RedisNameFeed, RedisContant.FeedLaudCount+feedId, -1, 0);
				RedisCommService.getInstance().hdel(RedisContant.RedisNameFeed, RedisContant.FeedLaud+feedId, uid.toString());
			}
			return rec;
		}
		return -1;
	}

	@Override
	public UserFeedReplyModel getFeedReplyById(Integer id) {
		return feedDao.getFeedReplyById(id);
	}

	@Override
	public List<UserFeedReplyModel> getFeedReplyByFeedId(Integer feedId, Integer page) {
		List<UserFeedReplyModel> replyModels = new ArrayList<UserFeedReplyModel>();
		Set<Tuple> set = RedisCommService.getInstance().zrevrangeWithScores(RedisContant.RedisNameFeed, RedisContant.FeedReply + feedId, (page - 1) * replycount, page * replycount - 1);
		for (Tuple replyStr : set) {
			UserFeedReplyModel userFeedReplyModel = (UserFeedReplyModel) JSONObject.parseObject(replyStr.getElement(), UserFeedReplyModel.class);
			replyModels.add(userFeedReplyModel);
		}
		return replyModels;
	}

	@Override
	public int addFeedReply(Integer feedId, Integer parentFrId, Integer fromUid, Integer toUid,
			String content) {
		Long now = System.currentTimeMillis()/1000;
		int frid = feedDao.addFeedReply(feedId,parentFrId, fromUid, toUid, content);
		if(frid>0){
			UserBaseInfoModel userBaseInfoModel = userService.getUserbaseInfoByUid(fromUid, false);
			if(userBaseInfoModel == null){
				return -1;
			}
			UserFeedReplyModel userFeedReplyModel = new UserFeedReplyModel();
			userFeedReplyModel.setId(frid);
			userFeedReplyModel.setFeedid(feedId);
			userFeedReplyModel.setCreateAt(now.intValue());
			userFeedReplyModel.setContent(content);
			userFeedReplyModel.setFromUid(fromUid);
			userFeedReplyModel.setFromNickName(userBaseInfoModel.getNickname());
			userFeedReplyModel.setFromHeadImage(userBaseInfoModel.getHeadimage());
			userFeedReplyModel.setFromSex(userBaseInfoModel.getSex());
			userFeedReplyModel.setFromUserLevel(userBaseInfoModel.getUserLevel());
			if(toUid != null && toUid>0){
				userBaseInfoModel = userService.getUserbaseInfoByUid(toUid, false);
				if(userBaseInfoModel != null){
					userFeedReplyModel.setToUid(toUid);
					userFeedReplyModel.setToNickName(userBaseInfoModel.getNickname());
				}
			}
			RedisCommService.getInstance().incrBy(RedisContant.RedisNameFeed, RedisContant.FeedReplyCount+feedId, 1, 0);
			RedisCommService.getInstance().zadd(RedisContant.RedisNameFeed, RedisContant.FeedReply+feedId, frid, JSONObject.toJSONString(userFeedReplyModel));
			
//			UserFeedModel feed = getFeed(feedId, false);
//			if(feed != null){
//				if(fromUid.intValue() != feed.getUid().intValue()){
//					HashMap<String, Object> map = new HashMap<String, Object>();
//		    		map.put("nickname", userBaseInfoModel.getNickname());
//		    		map.put("level", userBaseInfoModel.getUserLevel());
//		    		map.put("islink", true);
//		    		map.put("sex", userBaseInfoModel.getSex());
//		    		map.put("uid", userBaseInfoModel.getUid());
//		    		map.put("type", FeedNoticeCmod.TYPE_REPLY);
//					ChatMessageUtil.sendFeedNotice(feed.getUid(), feedId, map);
//				}
//			}
			return frid;
		}
		return -1;
	}

	@Override
	public int delFeedReply(Integer frId, Integer feedId) {
		int rec = feedDao.delFeedReply(frId);
		if(rec > 0){
			RedisCommService.getInstance().zremrangeByScore(RedisContant.RedisNameFeed, RedisContant.FeedReply+feedId, frId.toString(), frId.toString());
			RedisCommService.getInstance().incrBy(RedisContant.RedisNameFeed, RedisContant.FeedReplyCount+feedId, -1, 0);
			return rec;
		}
		return -1;
	}

	@Override
	public int getFeedLaudCount(Integer feedId) {
		String count = RedisCommService.getInstance().get(RedisContant.RedisNameFeed, RedisContant.FeedLaudCount+feedId);
		return count == null ? 0 : Integer.valueOf(count);
	}

	@Override
	public int getFeedReplyCount(Integer feedId) {
		String count = RedisCommService.getInstance().get(RedisContant.RedisNameFeed, RedisContant.FeedReplyCount+feedId);
		return count == null ? 0 : Integer.valueOf(count);
	}

	@Override
	public int getFeedRewardCount(Integer feedId) {
		String count = RedisCommService.getInstance().get(RedisContant.RedisNameFeed, RedisContant.FeedRewardCount+feedId);
		return count == null ? 0 : Integer.valueOf(count);
	}

	@Override
	public int addFeedReward(Integer feedId, Integer feedCUid, Integer rewardUid, Integer zhutou) {
		int addFeedReward = feedDao.addFeedReward(feedId, feedCUid, rewardUid, zhutou);
		if(addFeedReward > 0){
			int zscore = RedisCommService.getInstance().zscore(RedisContant.RedisNameFeed, RedisContant.FeedReward+feedId, rewardUid.toString());
			if(zscore == 0){
				RedisCommService.getInstance().incrBy(RedisContant.RedisNameFeed, RedisContant.FeedRewardCount+feedId, 1, 0);	
			}
			RedisCommService.getInstance().zincrby(RedisContant.RedisNameFeed, RedisContant.FeedReward+feedId, zhutou, rewardUid.toString(),0);
			
			if(feedCUid.intValue() != rewardUid.intValue() && zhutou >= 10){
				UserBaseInfoModel userBaseInfoModel = userService.getUserbaseInfoByUid(rewardUid, false);
				HashMap<String, Object> map = new HashMap<String, Object>();
	    		map.put("nickname", userBaseInfoModel.getNickname());
	    		map.put("level", userBaseInfoModel.getUserLevel());
	    		map.put("islink", true);
	    		map.put("sex", userBaseInfoModel.getSex());
	    		map.put("uid", userBaseInfoModel.getUid());
	    		map.put("type", FeedNoticeCmod.TYPE_REWARD);
	    		map.put("zhutou", zhutou);
				
				UserFeedModel feed = getFeed(feedId, false);
				ChatMessageUtil.sendFeedNotice(feed.getUid(), feedId, map);
			}
			
			return addFeedReward;
		}
		return 0;
	}

	@Override
	public List<Map<String, Object>> getFeedReward(Integer feedId) {
		Set<Tuple> set = RedisCommService.getInstance().zrevrangeWithScores(RedisContant.RedisNameFeed, RedisContant.FeedReward+feedId, 0, -1);
		List<String> uids = new ArrayList<String>();
		Map<String, Object> rewardMap = new HashMap<String, Object>();
		for(Tuple tuple: set){
			uids.add(tuple.getElement());
			rewardMap.put(tuple.getElement(), tuple.getScore());
		}
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		if(uids.size() > 0){
			list = userService.getUserProfile(uids.toArray(new String[0]));
		}
		for(Map<String, Object> map : list){
			map.put("zhutou", rewardMap.get(map.get("uid")));
		}
		return list;
	}
	
	@Override
	public int addFeedRewardGift(Integer feedId, Integer feedCUid, Integer rewardUid, Integer gift, Integer count) {
		int addFeedReward = feedDao.addFeedRewardGift(feedId, feedCUid, rewardUid, gift, count);
		if(addFeedReward > 0){
			int zscore = RedisCommService.getInstance().zscore(RedisContant.RedisNameFeed, RedisContant.FeedReward+feedId, rewardUid.toString());
			if(zscore == 0){
				RedisCommService.getInstance().incrBy(RedisContant.RedisNameFeed, RedisContant.FeedRewardGiftCount+feedId, 1, 0);	
			}
			RedisCommService.getInstance().zincrby(RedisContant.RedisNameFeed, RedisContant.FeedReward+feedId, count, rewardUid.toString(),0);
			
			if(feedCUid.intValue() != rewardUid.intValue() && count >= 1){
				UserBaseInfoModel userBaseInfoModel = userService.getUserbaseInfoByUid(rewardUid, false);
				HashMap<String, Object> map = new HashMap<String, Object>();
	    		map.put("nickname", userBaseInfoModel.getNickname());
	    		map.put("level", userBaseInfoModel.getUserLevel());
	    		map.put("islink", true);
	    		map.put("sex", userBaseInfoModel.getSex());
	    		map.put("uid", userBaseInfoModel.getUid());
//	    		map.put("type", FeedNoticeCmod.TYPE_REWARD);	//tosy debug
	    		map.put("type", FeedNoticeCmod.TYPE_REWARDGIFT);
	    		map.put("count", count);
				
				UserFeedModel feed = getFeed(feedId, false);
//				ChatMessageUtil.sendFeedNotice(feed.getUid(), feedId, map);		//tosy debug
			}
			
			return addFeedReward;
		}
		return 0;
	}

	@Override
	public List<Map<String, Object>> getFeedRewardGift (Integer feedId) {
		Set<Tuple> set = RedisCommService.getInstance().zrevrangeWithScores(RedisContant.RedisNameFeed, RedisContant.FeedRewardGift+feedId, 0, -1);
		List<String> uids = new ArrayList<String>();
		Map<String, Object> rewardMap = new HashMap<String, Object>();
		for(Tuple tuple: set){
			uids.add(tuple.getElement());
			rewardMap.put(tuple.getElement(), tuple.getScore());
		}
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		if(uids.size() > 0){
			list = userService.getUserProfile(uids.toArray(new String[0]));
		}
		for(Map<String, Object> map : list){
			map.put("count", rewardMap.get(map.get("uid")));
		}
		return list;
	}
	
	
	@Override
	public int getReportFeedByFid(Integer fid) {
		return feedDao.getReportFeedByFid(fid);
	}

	@Override
	public int getReportFeedUserByRid(Integer rid, Integer dstuid) {
		return feedDao.getReportFeedUserByRid(rid, dstuid);
	}

	@Override
	public int addReportFeedUser(Integer rid, Integer fid, String reportReason,
			Integer dstuid) {
		return feedDao.addReportFeedUser(rid, fid, reportReason, dstuid);
	}

	@Override
	public int addReportFeed(Integer reportUid, Integer reportFid) {
		return feedDao.addReportFeed(reportUid, reportFid);
	}

	@Override
	public int updReportFeed(Integer reportFid) {
		return feedDao.updReportFeed(reportFid);
	}
	
	@Override
	public List<Map<String,Object>> getRecommendAnchor(){
		Long now = System.currentTimeMillis()/1000;
		if(now-lastTime >300){
			reloadRecommendAnchor();
		}
		return feedRecommendAnchor;	
	}
	
	public void reloadRecommendAnchor(){
		feedRecommendAnchor.clear();
		List<String> recommendAnchorId = feedDao.getRecommendAnchorId();
		List<String> userBaseInfo = UserRedisService.getInstance().getUserBaseInfo(recommendAnchorId.toArray(new String[0]));
		if (userBaseInfo != null) {
			List<Map<String, Object>>  onLiveList = new ArrayList<Map<String, Object>>();
			List<Map<String, Object>>  noLiveList = new ArrayList<Map<String, Object>>();
			for (String string : userBaseInfo) {
				UserBaseInfoModel userBaseInfoModel = null;
				userBaseInfoModel = (UserBaseInfoModel) JSONObject.parseObject(string, UserBaseInfoModel.class);
				if (userBaseInfoModel != null) {
					Map<String, Object> userInfoMap = new HashMap<String, Object>();
					userInfoMap.put("uid", userBaseInfoModel.getUid());
					userInfoMap.put("nickname", userBaseInfoModel.getNickname().trim());
					userInfoMap.put("headimage", userBaseInfoModel.getHeadimage());
					userInfoMap.put("anchorLevel", userBaseInfoModel.getAnchorLevel());
					userInfoMap.put("city", userBaseInfoModel.getCity());
					userInfoMap.put("mobileliveimg", userBaseInfoModel.getLivimage());
					userInfoMap.put("sex", userBaseInfoModel.getSex());
					userInfoMap.put("status", userBaseInfoModel.getLiveStatus());
					Long opentime = 0l;
					if(userBaseInfoModel.getOpentime() > 0){
						opentime = System.currentTimeMillis()/1000-userBaseInfoModel.getOpentime();
					}
					userInfoMap.put("opentime", opentime);
					String stream = configService.getThirdStream(userBaseInfoModel.getUid());
					if (null == stream) {
						userInfoMap.put("domain", liveService.getVideoConfig(0, userBaseInfoModel.getUid(), userBaseInfoModel.getVideoline()).get("domain"));
					} else {
						userInfoMap.put("domain", stream);
					}
					userInfoMap.put("verified", userBaseInfoModel.isVerified());
					List<UserAlbumModel> userAlbumDate = userAlbumService.getUserAlbumDate(userBaseInfoModel.getUid(), false);
					if(userAlbumDate.size()>3){
						userAlbumDate = userAlbumDate.subList(0, 3);
					}
					userInfoMap.put("albums", userAlbumDate);
					if(userBaseInfoModel.getLiveStatus()){
						onLiveList.add(userInfoMap);
					}else{
						noLiveList.add(userInfoMap);
					}
				} 
			}
			feedRecommendAnchor.addAll(onLiveList);
			feedRecommendAnchor.addAll(noLiveList);
		}
		lastTime = System.currentTimeMillis()/1000;
	}
	
}
