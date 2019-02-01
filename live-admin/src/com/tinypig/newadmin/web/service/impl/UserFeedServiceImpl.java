package com.tinypig.newadmin.web.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.tinypig.admin.redis.service.RedisOperat;
import com.tinypig.admin.util.RedisContant;
import com.tinypig.newadmin.web.dao.ReportFeedDao;
import com.tinypig.newadmin.web.dao.ReportFeedUserDao;
import com.tinypig.newadmin.web.dao.UserFeedDao;
import com.tinypig.newadmin.web.entity.ReportFeed;
import com.tinypig.newadmin.web.entity.ReportFeedUser;
import com.tinypig.newadmin.web.entity.UserFeed;
import com.tinypig.newadmin.web.entity.UserFeedModel;
import com.tinypig.newadmin.web.service.UserFeedService;

@Service
@Transactional
public class UserFeedServiceImpl implements UserFeedService {

	@Autowired
	private UserFeedDao userFeedDao;
	@Autowired
	private ReportFeedDao reportFeedDao;
	@Autowired
	private ReportFeedUserDao reportFeedUserDao;

	@Override
	public Map<String, Object> chuliFeed(Integer id, Integer status, Integer userFeedId) {
		Map<String, Object> map = new HashMap<String, Object>();
		if (status == 1) {// 忽略

		} else if (status == 2) {// 删除
			UserFeed record = new UserFeed();
			record.setId(userFeedId);
			record.setStatus(0);// 删除用户动态
			userFeedDao.updateByPrimaryKeySelective(record);
			delRedisFeed(userFeedId);
		} else {
			map.put("success", false);
			map.put("msg", "处理失败，参数不正确");
		}
		// 更新举报记录和举报人举报记录详细信息
		ReportFeed record = new ReportFeed();
		record.setId(id);
		record.setStatus(status);// 忽略
		reportFeedDao.updateByPrimaryKeySelective(record);

		// 详细信息
		ReportFeedUser rfu = new ReportFeedUser();
		rfu.setId(userFeedId);
		reportFeedUserDao.updateStatusByRid(id, status);

		map.put("success", true);
		map.put("msg", "处理成功");
		return map;
	}
	
	
	
	public void delRedisFeed(Integer feedId) {
		int bucketIndex = getBucketIndex(feedId);
		RedisOperat.getInstance().hdel(RedisContant.host, RedisContant.port6379,RedisContant.FeedInfo + bucketIndex, feedId.toString());

		//删除赞赏
		RedisOperat.getInstance().del(RedisContant.host, RedisContant.port6379,RedisContant.FeedReward + feedId);
		RedisOperat.getInstance().del(RedisContant.host, RedisContant.port6379,RedisContant.FeedRewardCount + feedId);
		//删除回复
		RedisOperat.getInstance().del(RedisContant.host, RedisContant.port6379,RedisContant.FeedReply + feedId);
		RedisOperat.getInstance().del(RedisContant.host, RedisContant.port6379,RedisContant.FeedReplyCount + feedId);
		//删除点赞
		RedisOperat.getInstance().del(RedisContant.host, RedisContant.port6379,RedisContant.FeedLaud + feedId);
		RedisOperat.getInstance().del(RedisContant.host, RedisContant.port6379,RedisContant.FeedLaudCount + feedId);
		
		RedisOperat.getInstance().zrem(RedisContant.host, RedisContant.port6379,RedisContant.FeedSquare, feedId.toString());
		UserFeedModel feed = getFeed(feedId, false);
		if(feed != null){
			RedisOperat.getInstance().zrem(RedisContant.host, RedisContant.port6379,RedisContant.FeedUser+feed.getUid(), feedId.toString());	
		}
	}
	private int getBucketIndex(Integer id){
		int bucketIndex = id/1000;
		return bucketIndex;
	}
	private UserFeedModel getFeed(Integer id, Boolean directReadMysql) {
		int bucketIndex = getBucketIndex(id);
		UserFeedModel userFeedModel = null;
		if (!directReadMysql) {
			String feedInfo = RedisOperat.getInstance().hget(RedisContant.host, RedisContant.port6379, RedisContant.FeedInfo+bucketIndex, id.toString());
			if (StringUtils.isNotEmpty(feedInfo)) {
				userFeedModel = (UserFeedModel) JSONObject.parseObject(feedInfo, UserFeedModel.class);
			}
		}
		if(userFeedModel ==null){
			UserFeed uf = userFeedDao.selectByPrimaryKey(id);
			if(uf != null && uf.getStatus() == 1){
				userFeedModel = new UserFeedModel();
				userFeedModel.setId(uf.getId());
				userFeedModel.setUid(uf.getUid());
				userFeedModel.setContent(uf.getContent());
				userFeedModel.setImgs(uf.getImgs());
				userFeedModel.setCreateAt(uf.getCreateat());
				userFeedModel.setStatus(uf.getStatus());
				RedisOperat.getInstance().hset(RedisContant.host, RedisContant.port6379, RedisContant.FeedInfo+bucketIndex, id.toString(), JSONObject.toJSONString(userFeedModel), 0);	
			}
		}
		return userFeedModel;
	}
}