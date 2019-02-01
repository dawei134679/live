package com.mpig.api.redis.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.mpig.api.redis.core.RedisBucket;
import com.mpig.api.utils.DateUtils;
import com.mpig.api.utils.RedisContant;

import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;
import redis.clients.jedis.Tuple;

/**
 * 缓存用户信息
 * 
 * @author fang
 *
 */
public class UserRedisService {
	private static Logger logger = Logger.getLogger("redislog");
	
	 private static final Long pageSiz = 20L;
	private static final long rankSize = 9L;
	private final static UserRedisService instance = new UserRedisService();
	private final static String redisName = "user";

	public static UserRedisService getInstance() {
		return instance;
	}

	/**
	 * 缓存用户基本信息
	 * 
	 * @param uid
	 * @param strJson
	 */
	public void setUserBaseInfo(Integer uid, String strJson) {

		ShardedJedisPool shardPool = null;
		ShardedJedis redis = null;
		try {
			shardPool = RedisBucket.getInstance().getShardPool(redisName);
			redis = shardPool.getResource();
			redis.hset(RedisContant.keyBaseInfoList, uid.toString(), strJson);
		} catch (Exception e) {
			logger.error("<setUserBaseInfo->Exception>" + e.toString());
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
	}

	/**
	 * 缓存用户基本信息
	 * 
	 * @param uid
	 * @param strJson
	 */
	public void setRobotBaseInfo(Integer uid, String strJson) {

		ShardedJedisPool shardPool = null;
		ShardedJedis redis = null;
		try {
			shardPool = RedisBucket.getInstance().getShardPool(redisName);
			redis = shardPool.getResource();
			redis.hset(RedisContant.keyRobotBaseInfoList, uid.toString(), strJson);
		} catch (Exception e) {
			logger.error("<setRobotBaseInfo->Exception>" + e.toString());
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
	}

	/**
	 * 获取缓存用户信息 集合
	 * 
	 * @param uids
	 * @return
	 */
	public List<String> getUserBaseInfo(String... uids) {
		if(null == uids || 0 >= uids.length){
			return null;
		}
		
		List<String> hmget = null;

		ShardedJedisPool shardPool = null;
		ShardedJedis redis = null;
		try {
			shardPool = RedisBucket.getInstance().getShardPool(redisName);
			redis = shardPool.getResource();
			hmget = redis.hmget(RedisContant.keyBaseInfoList, uids);

		} catch (Exception e) {
			logger.error("<getUserBaseInfo->Exception>" + e.toString());
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
		return hmget;
	}
	

	/**
	 * 获取缓存用户信息
	 * 
	 * @param uid
	 * @return
	 */
	public String getUserBaseInfo(Integer uid) {

		ShardedJedisPool shardPool = null;
		ShardedJedis redis = null;
		try {
			shardPool = RedisBucket.getInstance().getShardPool(redisName);
			redis = shardPool.getResource();
			return redis.hget(RedisContant.keyBaseInfoList, uid.toString());
		} catch (Exception e) {
			logger.error("<getUserBaseInfo->Exception>" + e.toString());
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
		return null;
	}

	/**
	 * 获取缓存用户信息
	 * 
	 * @param uid
	 * @return
	 */
	public List<String> getRobotBaseInfo(String... uids) {
		if(null == uids || 0 >= uids.length){
			return null;
		}
		
		ShardedJedisPool shardPool = null;
		ShardedJedis redis = null;
		try {
			shardPool = RedisBucket.getInstance().getShardPool(redisName);
			redis = shardPool.getResource();
			return redis.hmget(RedisContant.keyRobotBaseInfoList, uids);
		} catch (Exception e) {
			logger.error("<getRobotBaseInfo->Exception>" + e.toString());
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
		return null;
	}

	/**
	 * 获取缓存用户信息
	 * 
	 * @param uid
	 * @return
	 */
	public String getRobotBaseInfo(Integer uid) {

		ShardedJedisPool shardPool = null;
		ShardedJedis redis = null;
		try {
			shardPool = RedisBucket.getInstance().getShardPool(redisName);
			redis = shardPool.getResource();
			return redis.hget(RedisContant.keyRobotBaseInfoList, uid.toString());
		} catch (Exception e) {
			logger.error("<getRobotBaseInfo->Exception>" + e.toString());
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
		return null;
	}

	/**
	 * 获取缓存用户Account信息 集合
	 * 
	 * @param uids
	 * @return
	 */
	public List<String> getAccountInfo(String... uids) {
		if(null == uids || 0 >= uids.length){
			return null;
		}
		
		List<String> hmget = null;

		ShardedJedisPool shardPool = null;
		ShardedJedis redis = null;
		try {
			shardPool = RedisBucket.getInstance().getShardPool(redisName);
			redis = shardPool.getResource();
			hmget = redis.hmget(RedisContant.keyAccountList, uids);

		} catch (Exception e) {
			logger.error("<getAccountInfo->Exception>" + e.toString());
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
		return hmget;
	}
	
	/**
	 * 缓存用户账户
	 * 
	 * @param uid
	 * @param strJson
	 */
	public void setUserAccount(Integer uid, String strJson) {

		ShardedJedisPool shardPool = null;
		ShardedJedis redis = null;
		try {
			shardPool = RedisBucket.getInstance().getShardPool(redisName);
			redis = shardPool.getResource();
			redis.hset(RedisContant.keyAccountList, uid.toString(), strJson);
		} catch (Exception e) {
			logger.error("<setUserAccount->Exception>" + e.toString());
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
	}

	/**
	 * 获取缓存账号信息
	 * 
	 * @param uid
	 * @return
	 */
	public String getUserAccount(Integer uid) {

		ShardedJedisPool shardPool = null;
		ShardedJedis redis = null;
		try {
			shardPool = RedisBucket.getInstance().getShardPool(redisName);
			redis = shardPool.getResource();
			return redis.hget(RedisContant.keyAccountList, uid.toString());
		} catch (Exception e) {
			logger.error("<getUserAccount->Exception>" + e.toString());
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
		return null;
	}

	/**
	 * 缓存账号与uid的关系
	 * 
	 * @param accountName
	 * @param uid
	 */
	public void setAccountNameAndUid(String accountName, Integer uid) {

		ShardedJedisPool shardPool = null;
		ShardedJedis redis = null;
		try {
			shardPool = RedisBucket.getInstance().getShardPool(redisName);
			redis = shardPool.getResource();
			redis.hset(RedisContant.keyAccountNameAndUid, accountName, uid.toString());
		} catch (Exception e) {
			logger.error("<setAccountNameAndUid->Exception>" + e.toString());
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
	}

	/**
	 * 通过账号获取uid
	 * 
	 * @param accountName
	 * @return
	 */
	public int getAccountNameAndUid(String accountName) {
		String uid = "";

		ShardedJedisPool shardPool = null;
		ShardedJedis redis = null;
		try {
			shardPool = RedisBucket.getInstance().getShardPool(redisName);
			redis = shardPool.getResource();
			uid = redis.hget(RedisContant.keyAccountNameAndUid, accountName);
		} catch (Exception e) {
			logger.error("<setAccountNameAndUid->Exception>" + e.toString());
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
		if (uid == null || uid == "") {
			return 0;
		} else {
			return Integer.valueOf(uid);
		}
	}

	/**
	 * 缓存第三方标示与uid的关系
	 * 
	 * @param authKey
	 * @param uid
	 */
	public void setAuthKeyAndUid(String authKey, Integer uid) {

		ShardedJedisPool shardPool = null;
		ShardedJedis redis = null;
		try {
			shardPool = RedisBucket.getInstance().getShardPool(redisName);
			redis = shardPool.getResource();
			redis.hset(RedisContant.keyAuthKeyAndUid, authKey, uid.toString());
		} catch (Exception e) {
			logger.error("<setAuthKeyAndUid->Exception>" + e.toString());
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
	}

	/**
	 * 通过第三方标示获取uid
	 * 
	 * @param authKey
	 * @return
	 */
	public int getAuthKeyAndUid(String authKey) {
		String uid = "";

		ShardedJedisPool shardPool = null;
		ShardedJedis redis = null;
		try {
			shardPool = RedisBucket.getInstance().getShardPool(redisName);
			redis = shardPool.getResource();
			uid = redis.hget(RedisContant.keyAuthKeyAndUid, authKey);
		} catch (Exception e) {
			logger.error("<getAuthKeyAndUid->Exception>" + e.toString());
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
		if (uid == null || uid == "") {
			return 0;
		} else {
			return Integer.valueOf(uid);
		}
	}


	/**
	 * 通过第三方标示获取uid
	 * 
	 * @param authKey
	 * @return
	 */
	public int getUnionIdAndUid(String unionid) {
		String uid = "";

		ShardedJedisPool shardPool = null;
		ShardedJedis redis = null;
		try {
			shardPool = RedisBucket.getInstance().getShardPool(redisName);
			redis = shardPool.getResource();
			uid = redis.hget(RedisContant.keyUnionIdAndUid, unionid);
		} catch (Exception e) {
			logger.error("<getAuthKeyAndUid->Exception>" + e.toString());
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
		if (uid == null || uid == "") {
			return 0;
		} else {
			return Integer.valueOf(uid);
		}
	}

	/**
	 * 设置手机与uid的关系
	 * 
	 * @param mobile
	 * @param uid
	 */
	public void setMobileAndUid(String mobile, int uid) {

		ShardedJedisPool shardPool = null;
		ShardedJedis redis = null;
		try {
			shardPool = RedisBucket.getInstance().getShardPool(redisName);
			redis = shardPool.getResource();
			redis.hset(RedisContant.keyMbileAndUid, mobile, String.valueOf(uid));
		} catch (Exception e) {
			logger.error("<setMobileAndUid->Exception>" + e.toString());
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
	}

	/**
	 * 通过手机号获取uid
	 * 
	 * @param mobile
	 * @return
	 */
	public int getMobileAndUid(String mobile) {
		String uid = "";

		ShardedJedisPool shardPool = null;
		ShardedJedis redis = null;
		try {
			shardPool = RedisBucket.getInstance().getShardPool(redisName);
			redis = shardPool.getResource();
			uid = redis.hget(RedisContant.keyMbileAndUid, mobile);
		} catch (Exception e) {
			logger.error("<getMobileAndUid->Exception>" + e.toString());
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
		if (uid == null || uid == "") {
			return 0;
		} else {
			return Integer.valueOf(uid);
		}
	}

	/**
	 * 删除手机与uid的关系
	 * 
	 * @param mobile
	 */
	public void delMobileAndUid(String mobile) {

		ShardedJedisPool shardPool = null;
		ShardedJedis redis = null;
		try {
			shardPool = RedisBucket.getInstance().getShardPool(redisName);
			redis = shardPool.getResource();
			redis.hdel(RedisContant.keyMbileAndUid, mobile);
		} catch (Exception e) {
			logger.error("<delMobileAndUid->Exception>" + e.toString());
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
	}

	/**
	 * 缓存用户与提现账户的关联
	 * 
	 * @param uid
	 * @param strJson
	 */
	public void setPayAccount(Integer uid, String strJson) {

		ShardedJedisPool shardPool = null;
		ShardedJedis redis = null;
		try {
			shardPool = RedisBucket.getInstance().getShardPool(redisName);
			redis = shardPool.getResource();
			redis.hset(RedisContant.keyPayAccountList, uid.toString(), strJson);
		} catch (Exception e) {
			logger.error("<setUserAccount->Exception>" + e.toString());
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
	}

	/**
	 * 获取用户与提现账号的关联
	 * 
	 * @param uid
	 * @return
	 */
	public String getPayAccount(Integer uid) {

		ShardedJedisPool shardPool = null;
		ShardedJedis redis = null;
		try {
			shardPool = RedisBucket.getInstance().getShardPool(redisName);
			redis = shardPool.getResource();
			return redis.hget(RedisContant.keyPayAccountList, uid.toString());
		} catch (Exception e) {
			logger.error("<setUserAccount->Exception>" + e.toString());
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
		return null;
	}

	/**
	 * 缓存用户资产
	 * 
	 * @param uid
	 * @param strJson
	 */
	public void setUserAsset(Integer uid, String strJson) {

		ShardedJedisPool shardPool = null;
		ShardedJedis redis = null;
		try {
			shardPool = RedisBucket.getInstance().getShardPool(redisName);
			redis = shardPool.getResource();
			redis.hset(RedisContant.keyAssetList, uid.toString(), strJson);
		} catch (Exception e) {
			logger.error("<setUserAsset->Exception>" + e.toString());
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
	}

	/**
	 * 获取用户资产
	 * 
	 * @param uid
	 * @return
	 */
	public String getUserAsset(Integer uid) {

		ShardedJedisPool shardPool = null;
		ShardedJedis redis = null;
		try {
			shardPool = RedisBucket.getInstance().getShardPool(redisName);
			redis = shardPool.getResource();
			return redis.hget(RedisContant.keyAssetList, uid.toString());
		} catch (Exception e) {
			logger.error("<getUserAsset->Exception>" + e.toString());
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
		return null;
	}

	/**
	 * 缓存开播列表(只表示 开播过)
	 * 
	 * @param uid
	 * @param strJson
	 */
	public void setLiveInfo(Integer uid, String strJson) {

		ShardedJedisPool shardPool = null;
		ShardedJedis redis = null;
		try {
			shardPool = RedisBucket.getInstance().getShardPool(redisName);
			redis = shardPool.getResource();
			redis.hset(RedisContant.keyLiveInfoList, uid.toString(), strJson);
		} catch (Exception e) {
			logger.error("<setLiveInfo->Exception>" + e.toString());
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
	}

	/**
	 * 获取开播列表(只表示 开播过)
	 * 
	 * @param uid
	 * @return
	 */
	public String getLiveInfo(Integer uid) {

		ShardedJedisPool shardPool = null;
		ShardedJedis redis = null;
		try {
			shardPool = RedisBucket.getInstance().getShardPool(redisName);
			redis = shardPool.getResource();
			return redis.hget(RedisContant.keyLiveInfoList, uid.toString());
		} catch (Exception e) {
			logger.error("<getLiveInfo->Exception>" + e.toString());
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
		return null;
	}

	public List<String> getLiveInfoList(String... uids) {
		if(null == uids || 0 >= uids.length){
			return null;
		}
		
		List<String> list = new ArrayList<String>();

		ShardedJedisPool shardPool = null;
		ShardedJedis redis = null;
		try {
			shardPool = RedisBucket.getInstance().getShardPool(redisName);
			redis = shardPool.getResource();
			list = redis.hmget(RedisContant.keyLiveInfoList, uids);
		} catch (Exception e) {
			logger.error("<getLiveInfo->Exception>" + e.toString());
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
		return list;
	}

	/**
	 * 停播删除该记录
	 * 
	 * @param uid
	 */
	public void delLiveInfo(int uid) {

		ShardedJedisPool shardPool = null;
		ShardedJedis redis = null;
		try {
			shardPool = RedisBucket.getInstance().getShardPool(redisName);
			redis = shardPool.getResource();
			redis.hdel(RedisContant.keyLiveInfoList, String.valueOf(uid));
		} catch (Exception e) {
			logger.error("<delLiveInfo->Exception>" + e.toString());
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
	}

	/**
	 * 缓存礼物列表
	 * 
	 * @param gid
	 * @param strJson
	 */
	public void setGiftConfigList(Integer gid, String strJson) {

		ShardedJedisPool shardPool = null;
		ShardedJedis redis = null;
		try {
			shardPool = RedisBucket.getInstance().getShardPool(redisName);
			redis = shardPool.getResource();
			redis.hset(RedisContant.KeyGiftConfigList, gid.toString(), strJson);
		} catch (Exception e) {
			logger.error("<SetGiftConfigList->Exception>" + e.toString());
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
	}

	/**
	 * 获取礼物列表
	 * 
	 * @param gid
	 * @return
	 */
	public String getGiftConfigList(Integer gid) {

		ShardedJedisPool shardPool = null;
		ShardedJedis redis = null;
		try {
			shardPool = RedisBucket.getInstance().getShardPool(redisName);
			redis = shardPool.getResource();
			return redis.hget(RedisContant.KeyGiftConfigList, gid.toString());
		} catch (Exception e) {
			logger.error("<getGiftConfigList->Exception>" + e.toString());
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
		return null;
	}

	/**
	 * 设置主播在520中收到心动或情书的值
	 * 
	 * @param uid
	 * @param score
	 */
	public void setRankAnchor520(int uid, Double score) {

		ShardedJedisPool shardPool = null;
		ShardedJedis redis = null;
		try {
			shardPool = RedisBucket.getInstance().getShardPool(redisName);
			redis = shardPool.getResource();
			redis.zincrby(RedisContant.Rank520Anchor, score, String.valueOf(uid));
		} catch (Exception e) {
			logger.error("<setRankAnchor520->Exception>" + e.toString());
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
	}

	/**
	 * 设置用户在520中收到心动或情书的值
	 * 
	 * @param uid
	 * @param score
	 */
	public void setRankUser520(int uid, Double score) {

		ShardedJedisPool shardPool = null;
		ShardedJedis redis = null;
		try {
			shardPool = RedisBucket.getInstance().getShardPool(redisName);
			redis = shardPool.getResource();
			redis.zincrby(RedisContant.Rank520User, score, String.valueOf(uid));
		} catch (Exception e) {
			logger.error("<setRankUser520->Exception>" + e.toString());
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
	}

	/**
	 * 获取主播520 收礼排行榜
	 * 
	 * @param page
	 * @return
	 */
	public Set<Tuple> getRankAnchor520(int page) {


		ShardedJedisPool shardPool = null;
		ShardedJedis redis = null;
		try {
			shardPool = RedisBucket.getInstance().getShardPool(redisName);
			redis = shardPool.getResource();
			return redis.zrevrangeWithScores(RedisContant.Rank520Anchor, 0, 5);

		} catch (Exception e) {
			logger.error("<getRankAnchor520->Exception>" + e.toString());
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
		return null;
	}

	/**
	 * 获取送货520 送礼排行榜
	 * 
	 * @param page
	 * @return
	 */
	public Set<Tuple> getRankUser520(int page) {

		ShardedJedisPool shardPool = null;
		ShardedJedis redis = null;
		try {
			shardPool = RedisBucket.getInstance().getShardPool(redisName);
			redis = shardPool.getResource();
			return redis.zrevrangeWithScores(RedisContant.Rank520User, 0, 5);

		} catch (Exception e) {
			logger.error("<getRankUser520->Exception>" + e.toString());
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
		return null;
	}

	/**
	 * 设置开播主播切换到后台的标示 有效时间90秒
	 * 
	 * @param uid
	 */
	public void setSwitchPlate(int uid) {

		ShardedJedisPool shardPool = null;
		ShardedJedis redis = null;
		try {
			shardPool = RedisBucket.getInstance().getShardPool(redisName);
			redis = shardPool.getResource();
			redis.setex(RedisContant.SwithPlate + uid, 90, "1");
		} catch (Exception e) {
			logger.error("<setSwitchPlate->Exception>" + e.toString());
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
	}

	/**
	 * 获取开播主播 是否在后台
	 * 
	 * @param uid
	 * @return ＝true 在后台，＝false 不在后台
	 */
	public boolean getSwitchPlate(int uid) {
		String str = "";

		ShardedJedisPool shardPool = null;
		ShardedJedis redis = null;
		try {
			shardPool = RedisBucket.getInstance().getShardPool(redisName);
			redis = shardPool.getResource();
			str = redis.get(RedisContant.SwithPlate + uid);
		} catch (Exception e) {
			logger.error("<getSwitchPlate->Exception>" + e.toString());
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
		if (StringUtils.isNotEmpty(str)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 删除切换后台的标示
	 * 
	 * @param uid
	 */
	public void delSwitchPlate(int uid) {

		ShardedJedisPool shardPool = null;
		ShardedJedis redis = null;
		try {
			shardPool = RedisBucket.getInstance().getShardPool(redisName);
			redis = shardPool.getResource();
			redis.del(RedisContant.SwithPlate + uid);
		} catch (Exception e) {
			logger.error("<getSwitchPlate->Exception>" + e.toString());
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
	}

	/**
	 * 添加身份设置
	 * 
	 * @param field
	 * @param value
	 */
	public Boolean setIdentity(String field, String value) {
		Long lg = 0L;

		ShardedJedisPool shardPool = null;
		ShardedJedis redis = null;
		try {
			shardPool = RedisBucket.getInstance().getShardPool(redisName);
			redis = shardPool.getResource();
			lg = redis.hset(RedisContant.AuthenIdentity, field, value);
		} catch (Exception e) {
			logger.error("<getSwitchPlate->Exception>" + e.toString());
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
		if (lg > 0) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 获取身份对于的认证图片
	 * 
	 * @param field
	 * @return
	 */
	public String getIdentity(String field) {

		String val = "";

		ShardedJedisPool shardPool = null;
		ShardedJedis redis = null;
		try {
			shardPool = RedisBucket.getInstance().getShardPool(redisName);
			redis = shardPool.getResource();
			val = redis.hget(RedisContant.AuthenIdentity, field);
		} catch (Exception e) {
			logger.error("<getSwitchPlate->Exception>" + e.toString());
		} finally {
			if (redis != null) {
				redis.close();
			}
		}

		if (val == null) {
			return "";
		} else {
			return val;
		}
	}

	/**
	 * 设置os，主播开播时 需要广播的信息
	 * 
	 * @param anchor
	 * @param devicetoken
	 * @return
	 */
	public Boolean setBroadcastAnchor(String os, String anchor, String uid, String devicetoken) {

		Long lg = 0L;

		ShardedJedisPool shardPool = null;
		ShardedJedis redis = null;
		try {
			shardPool = RedisBucket.getInstance().getShardPool(redisName);
			redis = shardPool.getResource();
			lg = redis.hset(RedisContant.BroadcastAnchor + os + ":" + anchor, uid, devicetoken);
		} catch (Exception e) {
			logger.error("<setBroadcastAnchor->Exception>" + e.toString());
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
		if (lg > 0) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 获取os，anchor主播对应关注过的所有用户devicetoken
	 * 
	 * @param os
	 * @param anchor
	 * @return
	 */
	public Map<String, String> getBroadcastAnchor(String os, String anchor) {

		Map<String, String> map = new HashMap<String, String>();

		ShardedJedisPool shardPool = null;
		ShardedJedis redis = null;
		try {
			shardPool = RedisBucket.getInstance().getShardPool(redisName);
			redis = shardPool.getResource();
			map = redis.hgetAll(RedisContant.BroadcastAnchor + os + ":" + anchor);
		} catch (Exception e) {
			logger.error("<getBroadcastAnchor->Exception>" + e.toString());
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
		return map;
	}

	/**
	 * 获取 os anchor uid 对应的的devicetoken
	 * 
	 * @param os
	 * @param anchor
	 * @param uid
	 * @return
	 */
	public String getBroadcastAnchor(String os, String anchor, String uid) {

		String str = "";

		ShardedJedisPool shardPool = null;
		ShardedJedis redis = null;
		try {
			shardPool = RedisBucket.getInstance().getShardPool(redisName);
			redis = shardPool.getResource();
			str = redis.hget(RedisContant.BroadcastAnchor + os + ":" + anchor, uid);
		} catch (Exception e) {
			logger.error("<getBroadcastAnchor->Exception>" + e.toString());
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
		return str == null ? "" : str;
	}

	/**
	 * 删除对应的os，anchor，uid的devicetoken
	 * 
	 * @param os
	 * @param anchor
	 * @param uid
	 * @return
	 */
	public int delBroadcastAnchor(String os, String anchor, String uid) {
		Long lg = 0L;

		ShardedJedisPool shardPool = null;
		ShardedJedis redis = null;
		try {
			shardPool = RedisBucket.getInstance().getShardPool(redisName);
			redis = shardPool.getResource();
			lg = redis.hdel(RedisContant.BroadcastAnchor + os + ":" + anchor, uid);
		} catch (Exception e) {
			logger.error("<delBroadcastAnchor->Exception>" + e.toString());
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
		return lg == null ? 0 : lg.intValue();
	}

	/**
	 * 设置srcUid的msg Id
	 * 
	 * @param srcUid
	 * @return =0失败 其他则成功
	 */
	public Long setPrivateMsgId(int srcUid) {
		Long lg = 0L;

		ShardedJedisPool shardPool = null;
		ShardedJedis redis = null;
		try {
			shardPool = RedisBucket.getInstance().getShardPool(redisName);
			redis = shardPool.getResource();
			lg = redis.incr(RedisContant.PrivateMsgId + srcUid);
		} catch (Exception e) {
			logger.error("<setPrivateMsgId->Exception>" + e.toString());
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
		return lg;
	}

	/**
	 * srcUid给dstUid消息
	 * 
	 * @param dstUid
	 *            收消息人
	 * @param message
	 *            消息 json字符串
	 * @return false失败 true成功
	 */
	public Boolean setPrivateMsg(int dstUid, String msgId, String message) {

		Long lg = 0L;

		ShardedJedisPool shardPool = null;
		ShardedJedis redis = null;
		try {
			shardPool = RedisBucket.getInstance().getShardPool(redisName);
			redis = shardPool.getResource();
			lg = redis.hset(RedisContant.PrivateMsg + dstUid, msgId, message);

		} catch (Exception e) {
			logger.error("<setPrivateMsg->Exception>" + e.toString());
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
		return lg == null ? false : true;
	}

	/**
	 * 获取dstuid的信息
	 * 
	 * @param dstUid
	 * @return Map<Stirng ,String>
	 */
	public Map<String, String> getPrivateMsg(int dstUid) {

		Map<String, String> msgAll = null;

		ShardedJedisPool shardPool = null;
		ShardedJedis redis = null;
		try {
			shardPool = RedisBucket.getInstance().getShardPool(redisName);
			redis = shardPool.getResource();
			msgAll = redis.hgetAll(RedisContant.PrivateMsg + dstUid);
		} catch (Exception e) {
			logger.error("<setPrivateMsg->Exception>" + e.toString());
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
		return msgAll;
	}

	/**
	 * 删除dstUid发的消息
	 * 
	 * @param dstUid
	 * @param msgIds
	 * @return =false失败 =true成功
	 */
	public Boolean delPrivateMsg(int dstUid, String... msgIds) {
		if(null == msgIds || 0 >= msgIds.length){
			return false;
		}
		
		Long lg = 0L;

		ShardedJedisPool shardPool = null;
		ShardedJedis redis = null;
		try {
			shardPool = RedisBucket.getInstance().getShardPool(redisName);
			redis = shardPool.getResource();
			lg = redis.hdel(RedisContant.PrivateMsg + dstUid, msgIds);
		} catch (Exception e) {
			logger.error("<delPrivateMsg->Exception>" + e.toString());
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
		return lg == null ? false : true;
	}

	/**
	 * 添加日活跃
	 * 
	 * @param uid
	 * @param today
	 * @return
	 */
	public Boolean addLoginDetail(int uid, String today, String times) {
		Long hset = 0L;

		ShardedJedisPool shardPool = null;
		ShardedJedis redis = null;
		try {
			shardPool = RedisBucket.getInstance().getShardPool(redisName);
			redis = shardPool.getResource();
			hset = redis.hset(RedisContant.User_login + today, String.valueOf(uid), times);
		} catch (Exception e) {
			logger.error("<addLoginDetail->Exception>" + e.toString());
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
		return hset == null ? false : true;
	}

	/**
	 * 添加日活跃
	 * 
	 * @param uid
	 * @param today
	 * @return
	 */
	public Boolean hasLoginInThisDay(int uid, String today) {

		ShardedJedisPool shardPool = null;
		ShardedJedis redis = null;
		try {
			shardPool = RedisBucket.getInstance().getShardPool(redisName);
			redis = shardPool.getResource();
			return redis.hexists(RedisContant.User_login + today, String.valueOf(uid));
		} catch (Exception e) {
			logger.error("<hasLoginInThisDay->Exception>" + e.toString());
			return false;
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
	}

	/**
	 * 判断是否已添加
	 * 
	 * @param uid
	 * @param today
	 * @return
	 */
	public Boolean getLoginDetail(int uid, String today) {
		String str = "";

		ShardedJedisPool shardPool = null;
		ShardedJedis redis = null;
		try {
			shardPool = RedisBucket.getInstance().getShardPool(redisName);
			redis = shardPool.getResource();
			str = redis.hget(RedisContant.User_login + today, String.valueOf(uid));
		} catch (Exception e) {
			logger.error("<addLoginDetail->Exception>" + e.toString());
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
		return str == null ? false : true;
	}

	/**
	 * 记录用户尝试用code 验证错误次数
	 * 
	 * @param mobile
	 * @param times
	 */
	public void setCodeUsesTimes(String mobile) {

		ShardedJedisPool shardPool = null;
		ShardedJedis redis = null;
		try {
			shardPool = RedisBucket.getInstance().getShardPool(redisName);
			redis = shardPool.getResource();
			Long incr = redis.incr(RedisContant.loginCode + mobile);
			if (incr <= 1) {
				redis.expire(RedisContant.loginCode + mobile, 5 * 60);
			}
		} catch (Exception e) {
			logger.error("<setCodeUsesTimes->Exception>" + e.toString());
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
	}

	/**
	 * 获取尝试验证码验证错误的次数
	 * 
	 * @param mobile
	 * @return
	 */
	public int getCodeUsesTimes(String mobile) {
		String times = null;

		ShardedJedisPool shardPool = null;
		ShardedJedis redis = null;
		try {
			shardPool = RedisBucket.getInstance().getShardPool(redisName);
			redis = shardPool.getResource();
			times = redis.get(RedisContant.loginCode + mobile);
		} catch (Exception e) {
			logger.error("<setCodeUsesTimes->Exception>" + e.toString());
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
		return times == null ? 0 : Integer.valueOf(times);
	}

	/**
	 * 删除错误次数记录
	 * 
	 * @param mobile
	 */
	public void delCodeUsesTimes(String mobile) {

		ShardedJedisPool shardPool = null;
		ShardedJedis redis = null;
		try {
			shardPool = RedisBucket.getInstance().getShardPool(redisName);
			redis = shardPool.getResource();
			redis.del(RedisContant.loginCode + mobile);
		} catch (Exception e) {
			logger.error("<setCodeUsesTimes->Exception>" + e.toString());
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
	}

	/**
	 * 设置排行榜
	 * 
	 * @param srcuid
	 *            送礼人
	 * @param dstuid
	 *            收礼人
	 * @param amount
	 *            金额
	 */
	public void setRank(String srcuid, String dstuid, int sends,int gets) {

		ShardedJedisPool shardPool = null;
		ShardedJedis redis = null;
		try {
			shardPool = RedisBucket.getInstance().getShardPool(redisName);
			redis = shardPool.getResource();
			String day = DateUtils.dateToString(null, "yyyyMMdd");
			String month = DateUtils.getTimesMonthmorning("yyyyMM");
			String week = DateUtils.getWeekStart(0);

			if (StringUtils.isNotEmpty(srcuid)) {
				// 日榜
				redis.zincrby(RedisContant.userDay + day, sends, srcuid);
				// 周榜
				redis.zincrby(RedisContant.userWeek + week, sends, srcuid);
				// 月榜
				redis.zincrby(RedisContant.userMonth + month, sends, srcuid);
				// 总榜
				redis.zincrby(RedisContant.userAll, sends, srcuid);
			}
			if (StringUtils.isNotEmpty(dstuid)) {
				// 日榜
				redis.zincrby(RedisContant.anchorDay + day, gets, dstuid);
				// 周榜
				redis.zincrby(RedisContant.anchorWeek + week, gets, dstuid);
				// 月榜
				redis.zincrby(RedisContant.anchorMonth + month, gets, dstuid);
				// 总榜
				redis.zincrby(RedisContant.anchorAll, gets, dstuid);
			}
		} catch (Exception e) {
			logger.error("<setRank->Exception>" + e.toString());
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
	}
	
	/**
	 * 设置排行榜
	 * 
	 * @param srcuid
	 *            送礼人
	 * @param dstuid
	 *            收礼人
	 * @param count 数量
	 */
	public void setRewardGiftRank(String srcuid, String dstuid, int count) {

		ShardedJedisPool shardPool = null;
		ShardedJedis redis = null;
		try {
			shardPool = RedisBucket.getInstance().getShardPool(redisName);
			redis = shardPool.getResource();
			String day = DateUtils.dateToString(null, "yyyyMMdd");
			String month = DateUtils.getTimesMonthmorning("yyyyMM");
			String week = DateUtils.getWeekStart(0);

			if (StringUtils.isNotEmpty(srcuid)) {
				// 日榜
				redis.zincrby(RedisContant.userRewardGiftDay + day, count, srcuid);
				// 周榜
				redis.zincrby(RedisContant.userRewardGiftWeek + week, count, srcuid);
				// 月榜
				redis.zincrby(RedisContant.userRewardGiftMonth + month, count, srcuid);
				// 总榜
				redis.zincrby(RedisContant.userRewardGiftAll, count, srcuid);
			}
			if (StringUtils.isNotEmpty(dstuid)) {
				// 日榜
				redis.zincrby(RedisContant.anchorRewardGiftDay + day, count, dstuid);
				// 周榜
				redis.zincrby(RedisContant.anchorRewardGiftWeek + week, count, dstuid);
				// 月榜
				redis.zincrby(RedisContant.anchorRewardGiftMonth + month, count, dstuid);
				// 总榜
				redis.zincrby(RedisContant.anchorRewardGiftAll, count, dstuid);
			}
		} catch (Exception e) {
			logger.error("<setRewarGiftRank->Exception>" + e.toString());
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
	}

	/**
	 * 设置人气排行榜
	 * 
	 * @param dstuid
	 *            收礼人
	 * @param amount
	 *            金额
	 */
	public void setRankRQ(String dstuid, int count) {

		ShardedJedisPool shardPool = null;
		ShardedJedis redis = null;
		try {
			shardPool = RedisBucket.getInstance().getShardPool(redisName);
			redis = shardPool.getResource();
			String day = DateUtils.dateToString(null, "yyyyMMdd");
			String month = DateUtils.getTimesMonthmorning("yyyyMM");
			String week = DateUtils.getWeekStart(0);
			// 日榜
			redis.zincrby(RedisContant.anchorRqDay + day, count, dstuid);
			// 周榜
			redis.zincrby(RedisContant.anchorRqWeek + week, count, dstuid);
			// 月榜
			redis.zincrby(RedisContant.anchorRqMonth + month, count, dstuid);
			// 总榜
			redis.zincrby(RedisContant.anchorRqAll, count, dstuid);

		} catch (Exception e) {
			logger.error("<setRank->Exception>" + e.toString());
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
	}

	/**
	 * 
	 * @param srcuid
	 *            该用户抢占第一名
	 * @param dstuid
	 *            被srcuid 踢下第一名的宝座
	 * @param type
	 *            =user富豪榜 =anchor播主榜
	 */
	public void setDayAlternate(String srcuid, String dstuid, String type) {

		String day = DateUtils.dateToString(null, "yyyyMMdd");

		ShardedJedisPool shardPool = null;
		ShardedJedis redis = null;
		try {
			shardPool = RedisBucket.getInstance().getShardPool(redisName);
			redis = shardPool.getResource();
			redis.del(RedisContant.rankDayAlternate + day + ":" + type);
			redis.hset(RedisContant.rankDayAlternate + day + ":" + type, srcuid, dstuid);
			redis.expire(RedisContant.rankDayAlternate + day + ":" + type, 300);
		} catch (Exception e) {
			logger.error("<setDayAlternate->Exception>" + e.toString());
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
	}

	public String getDayAlternate(String srcuid, String type) {

		String day = DateUtils.dateToString(null, "yyyyMMdd");
		String hget = "";

		ShardedJedisPool shardPool = null;
		ShardedJedis redis = null;
		try {
			shardPool = RedisBucket.getInstance().getShardPool(redisName);
			redis = shardPool.getResource();
			hget = redis.hget(RedisContant.rankDayAlternate + day + ":" + type, srcuid);
		} catch (Exception e) {
			logger.error("<getDayAlternate->Exception>" + e.toString());
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
		return hget;
	}

	/**
	 * 设置周星礼物
	 * 
	 * @param gid
	 * @param week
	 */
	public void setZhouxinGift(int gid, String week, int price) {

		ShardedJedisPool shardPool = null;
		ShardedJedis redis = null;
		try {
			shardPool = RedisBucket.getInstance().getShardPool(redisName);
			redis = shardPool.getResource();
			redis.zadd(RedisContant.zxTimes + week, price, String.valueOf(gid));
		} catch (Exception e) {
			logger.error("<setZhouxinGift->Exception>" + e.toString());
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
	}

	/**
	 * 获取周星礼物
	 * 
	 * @param week
	 * @return
	 */
	public Double getZhouxinGift(String week, int gid) {
		Double zscore = null;

		ShardedJedisPool shardPool = null;
		ShardedJedis redis = null;
		try {
			shardPool = RedisBucket.getInstance().getShardPool(redisName);
			redis = shardPool.getResource();
			zscore = redis.zscore(RedisContant.zxTimes + week, String.valueOf(gid));

		} catch (Exception e) {
			logger.error("<getZhouxinGift->Exception>" + e.toString());
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
		return zscore;
	}

	/**
	 * 获取周星礼物列表
	 * 
	 * @param week
	 * @param num
	 *            =0 表示最贵的周星礼物，其他则全部周星礼物
	 * @return
	 */
	public Set<String> getZhouxinGiftlist(String week, int num) {
		Set<String> gidlist = null;

		ShardedJedisPool shardPool = null;
		ShardedJedis redis = null;
		try {
			shardPool = RedisBucket.getInstance().getShardPool(redisName);
			redis = shardPool.getResource();
			if (num == 0) {
				gidlist = redis.zrevrange(RedisContant.zxTimes + week, 0, 0);
			} else {
				gidlist = redis.zrevrange(RedisContant.zxTimes + week, 0, -1);
			}
		} catch (Exception e) {
			logger.error("<getZhouxinGiftlist->Exception>" + e.toString());
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
		return gidlist;
	}

	/**
	 * 
	 * @param srcuid
	 *            该用户抢占第一名
	 * @param dstuid
	 *            被srcuid 踢下第一名的宝座
	 * @param type
	 *            =user富豪榜 =anchor播主榜
	 */
	public void setZhouxinAlternate(String srcuid, String dstuid, String type, String week, String gid) {


		ShardedJedisPool shardPool = null;
		ShardedJedis redis = null;
		try {
			shardPool = RedisBucket.getInstance().getShardPool(redisName);
			redis = shardPool.getResource();
			redis.del(RedisContant.rankWeekAlternate + week + ":" + type + ":" + gid);
			redis.hset(RedisContant.rankWeekAlternate + week + ":" + type + ":" + gid, srcuid, dstuid);
			redis.expire(RedisContant.rankWeekAlternate + week + ":" + type + ":" + gid, 300);
		} catch (Exception e) {
			logger.error("<setZhouxinAlternate->Exception>" + e.toString());
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
	}

	public String getZhouxinAlternate(String srcuid, String type, String week, String gid) {

		String hget = "";

		ShardedJedisPool shardPool = null;
		ShardedJedis redis = null;
		try {
			shardPool = RedisBucket.getInstance().getShardPool(redisName);
			redis = shardPool.getResource();
			hget = redis.hget(RedisContant.rankWeekAlternate + week + ":" + type + ":" + gid, srcuid);
		} catch (Exception e) {
			logger.error("<getZhouxinAlternate->Exception>" + e.toString());
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
		return hget;
	}

	/**
	 * 设置周星
	 * 
	 * @param gid
	 * @param srcuid
	 * @param dstuid
	 * @param count
	 */
	public void setZhouxin(int gid, String srcuid, String dstuid, int count) {

		String week = DateUtils.getWeekStart(0);

		
		ShardedJedisPool shardPool = null;
		ShardedJedis redis = null;
		try {
			shardPool = RedisBucket.getInstance().getShardPool(redisName);
			redis = shardPool.getResource();
			// 周榜
			redis.zincrby(RedisContant.anchorZhouxin + week + ":" + gid, count, dstuid);
			redis.zincrby(RedisContant.userZhouxin + week + ":" + gid, count, srcuid);

		} catch (Exception e) {
			logger.error("<setZhouxin->Exception>" + e.toString());
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
	}

	private class RankAux implements Comparable<RankAux> {
		public Tuple orginal;
		public Double score;
		public int zoomIn;

		public RankAux(Tuple tuple, int zoomIn) {
			orginal = tuple;
			score = tuple.getScore();
			this.zoomIn = zoomIn;
		}

		@Override
		public int compareTo(RankAux o) {
			// return (int) (o.score * zoomIn - this.score * zoomIn);
//			return this.score > o.score ? 1 : (this.score == o.score ? 0 : -1);
			return o.score > this.score ? 1 : (this.score == o.score ? 0 : -1);
		}
	}

	/**
	 * 获取范围内的用户
	 * 
	 * @param typeUser
	 * @param start
	 * @param end
	 * @return
	 */
	public Set<String> getZhouxinRankUidPosition(String typeUser, int start, int end, String week, String gid) {

		Set<String> userSet = null;

		ShardedJedisPool shardPool = null;
		ShardedJedis redis = null;
		try {
			shardPool = RedisBucket.getInstance().getShardPool(redisName);
			redis = shardPool.getResource();

			if ("user".equalsIgnoreCase(typeUser)) {
				userSet = redis.zrevrange(RedisContant.userZhouxin + week + ":" + gid, start, end);
			} else if ("anchor".equalsIgnoreCase(typeUser)) {
				userSet = redis.zrevrange(RedisContant.anchorZhouxin + week + ":" + gid, start, end);
			}
		} catch (Exception e) {
			logger.error("<setRank->Exception>" + e.toString());
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
		return userSet;
	}

	/**
	 * 获取用户周星的排名
	 * 
	 * @param uid
	 * @param type
	 *            =user用户 =anchor主播
	 * @param week
	 * @param gid
	 * @return
	 */
	public Long getZhouxinRankUid(Integer uid, String type, String week, String gid) {
		Long lg = 0L;

		ShardedJedisPool shardPool = null;
		ShardedJedis redis = null;
		try {
			shardPool = RedisBucket.getInstance().getShardPool(redisName);
			redis = shardPool.getResource();
			if ("user".equalsIgnoreCase(type)) {
				lg = redis.zrevrank(RedisContant.userZhouxin + week + ":" + gid, uid.toString());
			} else if ("anchor".equalsIgnoreCase(type)) {
				lg = redis.zrevrank(RedisContant.anchorZhouxin + week + ":" + gid, uid.toString());
			}
		} catch (Exception e) {
			logger.error("<getRankOfUserDay->Exception>" + e.toString());
		}finally {
			if (redis != null) {
				redis.close();
			}
		}
		return lg;
	}
	

	/**
	 * 获取用户周星的排名及收到礼物数
	 * 
	 * @param uid
	 * @param type
	 *            =user用户 =anchor主播
	 * @param week
	 * @param gid
	 * @return
	 */
	public Map<String, Object> getZhouxinRankAndScoreUid(Integer uid, String week, String gid) {
		Long lg = 0L;
		Double zscore = 0.0;
		Map<String, Object> map = new HashMap<String,Object>();

		map.put("gid", gid);
		map.put("rank", "99+");
		map.put("num", 0);
		
		ShardedJedisPool shardPool = null;
		ShardedJedis redis = null;
		try {
			shardPool = RedisBucket.getInstance().getShardPool(redisName);
			redis = shardPool.getResource();
			lg = redis.zrevrank(RedisContant.anchorZhouxin + week + ":" + gid, uid.toString());
			if (lg != null) {
				if (lg.intValue() >= 99) {
					map.put("rank", "99+");
				}else {
					map.put("rank", lg.intValue()+1);
				}
				zscore = redis.zscore(RedisContant.anchorZhouxin + week + ":" + gid, uid.toString());
				map.put("num", zscore.intValue());
			}
		} catch (Exception e) {
			logger.error("<getRankOfUserDay->Exception>" + e.toString());
		}finally {
			if (redis != null) {
				redis.close();
			}
		}
		
		return map;
	}

	/**
	 * 获取周星排行
	 * 
	 * @param gid
	 * @param typeUser
	 *            =anchor主播，=user用户
	 * @return
	 */
	public Set<Tuple> getZhouxin(String gid, String typeUser, String week, int num) {
		Set<Tuple> ZhouxinRank = null;
//		List<Tuple> list = new ArrayList<Tuple>();

		ShardedJedisPool shardPool = null;
		ShardedJedis redis = null;
		try {
			shardPool = RedisBucket.getInstance().getShardPool(redisName);
			redis = shardPool.getResource();
			// 周榜
			if ("anchor".equalsIgnoreCase(typeUser)) {
				ZhouxinRank = redis.zrevrangeWithScores(RedisContant.anchorZhouxin + week + ":" + gid, 0, num);
			} else if ("user".equalsIgnoreCase(typeUser)) {
				ZhouxinRank = redis.zrevrangeWithScores(RedisContant.userZhouxin + week + ":" + gid, 0, num);
			}
		} catch (Exception e) {
			logger.error("<getZhouxin->Exception>" + e.toString());
		} finally {
			if (redis != null) {
				redis.close();
			}
		}

//		if (ZhouxinRank != null && ZhouxinRank.size() > 0) {
//			final int rankCount = 3;
//			final double factor = Math.pow(10, rankCount + 1);
//			double delta = (double) 1 / factor;
//			ArrayList<RankAux> result = new ArrayList<RankAux>();
//			RankAux last = null;
//			for (Tuple tuple : ZhouxinRank) {
//				double score = tuple.getScore();
//				RankAux rankAux = new RankAux(tuple, (int) factor);
//				if (last == null) {
//					result.add(rankAux);
//				} else {
//					if (score == last.orginal.getScore()) {
//						rankAux.score = last.score + delta;
//					}
//					result.add(rankAux);
//				}
//				last = rankAux;
//			}
//
//			Collections.sort(result);
//
//			ZhouxinRank.clear();
//			for (Iterator<RankAux> iterator = result.iterator(); iterator.hasNext();) {
//				RankAux tuple = (RankAux) iterator.next();
//				list.add(tuple.orginal);
////				ZhouxinRank.add(tuple.orginal);
//			}
//		}

		return ZhouxinRank;
	}

	/**
	 * 获取用户排名
	 * 
	 * @param uid
	 * @param type
	 *            =user用户 =anchor主播
	 * @return
	 */
	public Long getRankOfUserDay(Integer uid, String type) {
		Long lg = 0L;

		ShardedJedisPool shardPool = null;
		ShardedJedis redis = null;
		try {
			shardPool = RedisBucket.getInstance().getShardPool(redisName);
			redis = shardPool.getResource();
			if ("user".equalsIgnoreCase(type)) {
				lg = redis.zrevrank(RedisContant.userDay, uid.toString());
			} else if ("anchor".equalsIgnoreCase(type)) {
				lg = redis.zrevrank(RedisContant.anchorDay, uid.toString());
			}
		} catch (Exception e) {
			logger.error("<getRankOfUserDay->Exception>" + e.toString());
		}finally {

			if (redis != null) {
				redis.close();
			}
		}
		return lg;
	}

	/**
	 * 获取范围内的用户
	 * 
	 * @param typeUser
	 * @param start
	 * @param end
	 * @return
	 */
	public Set<String> getUserPosition(String typeUser, int start, int end) {

		Set<String> userSet = null;

		ShardedJedisPool shardPool = null;
		ShardedJedis redis = null;
		try {
			shardPool = RedisBucket.getInstance().getShardPool(redisName);
			redis = shardPool.getResource();
			String day = DateUtils.dateToString(null, "yyyyMMdd");

			if ("user".equalsIgnoreCase(typeUser)) {
				userSet = redis.zrevrange(RedisContant.userDay + day, start, end);
			} else if ("anchor".equalsIgnoreCase(typeUser)) {
				userSet = redis.zrevrange(RedisContant.anchorDay + day, start, end);
			}
		} catch (Exception e) {
			logger.error("<setRank->Exception>" + e.toString());
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
		return userSet;
	}

	/**
	 * 获取日榜 数据
	 * 
	 * @param typeUser
	 *            =anchor 主播 =user 用户
	 * @return
	 */
	public Set<Tuple> getUserDayRank(String typeUser, long num) {
		Set<Tuple> dayRank = null;
		if (num != 0) {
			num = rankSize;
		}

		ShardedJedisPool shardPool = null;
		ShardedJedis redis = null;
		try {
			shardPool = RedisBucket.getInstance().getShardPool(redisName);
			redis = shardPool.getResource();
			String day = DateUtils.dateToString(null, "yyyyMMdd");

			if ("user".equalsIgnoreCase(typeUser)) {
				dayRank = redis.zrevrangeWithScores(RedisContant.userDay + day, 0, num);
			} else if ("anchor".equalsIgnoreCase(typeUser)) {
				dayRank = redis.zrevrangeWithScores(RedisContant.anchorDay + day, 0, num);
			}
		} catch (Exception e) {
			logger.error("<getUserDayRank->Exception>" + e.toString());
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
		return dayRank;
	}

	/**
	 * 获取日榜 数据
	 * 
	 * @param typeUser
	 *            =anchor 主播 =user 用户
	 * @return
	 */
	public Set<Tuple> getUserDayRankRq(long num) {
		Set<Tuple> dayRank = null;
		if (num != 0) {
			num = rankSize;
		}

		ShardedJedisPool shardPool = null;
		ShardedJedis redis = null;
		try {
			shardPool = RedisBucket.getInstance().getShardPool(redisName);
			redis = shardPool.getResource();
			String day = DateUtils.dateToString(null, "yyyyMMdd");
			dayRank = redis.zrevrangeWithScores(RedisContant.anchorRqDay + day, 0, num);

		} catch (Exception e) {
			logger.error("<getUserDayRankRq->Exception>" + e.toString());
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
		return dayRank;
	}


	/**
	 * 获取日榜 数据
	 * 
	 * @param num
	 * @param day
	 * @return
	 */
	public Set<Tuple> getUserDayRankRq(String day, long num) {
		Set<Tuple> dayRank = null;

		ShardedJedisPool shardPool = null;
		ShardedJedis redis = null;
		try {
			shardPool = RedisBucket.getInstance().getShardPool(redisName);
			redis = shardPool.getResource();
			dayRank = redis.zrevrangeWithScores(RedisContant.anchorRqDay + day, 0, num);

		} catch (Exception e) {
			logger.error("<getUserDayRankRq->Exception>" + e.toString());
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
		return dayRank;
	}

	/**
	 * 获取周榜 数据
	 * 
	 * @param typeUser
	 *            =anchor 主播 =user 用户
	 * @return
	 */
	public Set<Tuple> getUserWeekRank(String typeUser,Integer size) {
		ShardedJedisPool shardPool = null;
		ShardedJedis redis = null;
		Set<Tuple> weekRank = null;
		try {
			shardPool = RedisBucket.getInstance().getShardPool(redisName);
			redis = shardPool.getResource();
			String week = DateUtils.getWeekStart(0);

			if ("user".equalsIgnoreCase(typeUser)) {
				weekRank = redis.zrevrangeWithScores(RedisContant.userWeek + week, 0, size);
			} else if ("anchor".equalsIgnoreCase(typeUser)) {
				weekRank = redis.zrevrangeWithScores(RedisContant.anchorWeek + week, 0, size);
			}
		} catch (Exception e) {
			logger.error("<getUserWeekRank->Exception>" + e.toString());
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
		return weekRank;
	}

	/**
	 * 获取周榜 数据
	 * 
	 * @param typeUser
	 *            =anchor 主播 =user 用户
	 * @return
	 */
	public Set<Tuple> getUserWeekRankRq() {
		ShardedJedisPool shardPool = null;
		ShardedJedis redis = null;
		Set<Tuple> weekRank = null;
		try {
			shardPool = RedisBucket.getInstance().getShardPool(redisName);
			redis = shardPool.getResource();
			String week = DateUtils.getWeekStart(0);
			weekRank = redis.zrevrangeWithScores(RedisContant.anchorRqWeek + week, 0, rankSize);
		} catch (Exception e) {
			logger.error("<getUserWeekRankRq->Exception>" + e.toString());
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
		return weekRank;
	}

	/**
	 * 获取月榜 数据
	 * 
	 * @param typeUser
	 *            =anchor 主播 =user 用户
	 * @return
	 */
	public Set<Tuple> getUserMonthRank(String typeUser) {
		ShardedJedisPool shardPool = null;
		ShardedJedis redis = null;
		Set<Tuple> weekRank = null;
		try {
			shardPool = RedisBucket.getInstance().getShardPool(redisName);
			redis = shardPool.getResource();
			String month = DateUtils.getTimesMonthmorning("yyyyMM");

			if ("user".equalsIgnoreCase(typeUser)) {
				weekRank = redis.zrevrangeWithScores(RedisContant.userMonth + month, 0, rankSize);
			} else if ("anchor".equalsIgnoreCase(typeUser)) {
				weekRank = redis.zrevrangeWithScores(RedisContant.anchorMonth + month, 0, rankSize);
			}
		} catch (Exception e) {
			logger.error("<getUserMonthRank->Exception>" + e.toString());
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
		return weekRank;
	}

	/**
	 * 获取月榜 数据
	 * 
	 * @param typeUser
	 *            =anchor 主播 =user 用户
	 * @return
	 */
	public Set<Tuple> getUserMonthRankRq() {
		ShardedJedisPool shardPool = null;
		ShardedJedis redis = null;
		Set<Tuple> weekRank = null;
		try {
			shardPool = RedisBucket.getInstance().getShardPool(redisName);
			redis = shardPool.getResource();
			String month = DateUtils.getTimesMonthmorning("yyyyMM");
			weekRank = redis.zrevrangeWithScores(RedisContant.anchorRqMonth + month, 0, rankSize);

		} catch (Exception e) {
			logger.error("<getUserMonthRankRq->Exception>" + e.toString());
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
		return weekRank;
	}

	/**
	 * 获取总榜 数据
	 * 
	 * @param typeUser
	 *            =anchor 主播 =user 用户
	 * @return
	 */
	public Set<Tuple> getUserAllRank(String typeUser) {
		ShardedJedisPool shardPool = null;
		ShardedJedis redis = null;
		Set<Tuple> allRank = null;
		try {
			shardPool = RedisBucket.getInstance().getShardPool(redisName);
			redis = shardPool.getResource();
			if ("user".equalsIgnoreCase(typeUser)) {
				allRank = redis.zrevrangeWithScores(RedisContant.userAll, 0, rankSize);
			} else if ("anchor".equalsIgnoreCase(typeUser)) {
				allRank = redis.zrevrangeWithScores(RedisContant.anchorAll, 0, rankSize);
			}
		} catch (Exception e) {
			logger.error("<getUserAllRank->Exception>" + e.toString());
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
		return allRank;
	}
	
	/**
	 * 根据KEY设置各种排行榜
	 * 
	 * @param key redis_key
	 * @param uid 用户id
	 * @param score 
	 */
	public void setRank(String key, String member, Double score) {
		ShardedJedisPool shardPool = null;
		ShardedJedis redis = null;
		try {
			shardPool = RedisBucket.getInstance().getShardPool(redisName);
			redis = shardPool.getResource();
			redis.zincrby(key, score, member);
		} catch (Exception e) {
			logger.error("<set"+key+"->Exception>" + e.toString());
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
	}
	
	/**
	 * 获取KEY获取所有的成员数据
	 * 
	 * @param page
	 * @return
	 */
	public Set<Tuple> getRank(String key, int index, int rows) {
		ShardedJedisPool shardPool = null;
		ShardedJedis redis = null;
		try {
			shardPool = RedisBucket.getInstance().getShardPool(redisName);
			redis = shardPool.getResource();
			return redis.zrevrangeWithScores(key, index, rows);

		} catch (Exception e) {
			logger.error("<get"+key+"->Exception>" + e.toString());
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
		return null;
	}
	
	/**
	 *获取某个key的member分数
	 * @param key
	 * @param member
	 * @return
	 */
	public Long getMembersRankScore(String key, String member){
		ShardedJedisPool shardPool = null;
		ShardedJedis redis = null;
		try {
			shardPool = RedisBucket.getInstance().getShardPool(redisName);
			redis = shardPool.getResource();
			Double score = redis.zscore(key, member);
			if(score==null){
				return null;
			}else{
				return	score.longValue();
			}
			 

		} catch (Exception e) {
			logger.error("<getRank "+key+"->Exception>" + e.toString());
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
		return null;
	}
	
	/**
	 * 设置KEY的有效期
	 * 
	 * @param page
	 * @return
	 */
	public Long expire(String key, int seconds) {
		ShardedJedisPool shardPool = null;
		ShardedJedis redis = null;
		try {
			shardPool = RedisBucket.getInstance().getShardPool(redisName);
			redis = shardPool.getResource();
			return redis.expire(key, seconds);

		} catch (Exception e) {
			logger.error("<setEx "+key+"->Exception>" + e.toString());
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
		return null;
	}
	/**
	 * 判断key是否存在
	 * 
	 * @param page
	 * @return
	 */
	public boolean exists(String key) {
		ShardedJedisPool shardPool = null;
		ShardedJedis redis = null;
		try {
			shardPool = RedisBucket.getInstance().getShardPool(redisName);
			redis = shardPool.getResource();
			return redis.exists(key);

		} catch (Exception e) {
			logger.error("<exists "+key+"->Exception>" + e.toString());
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
		return false;
	}
	
	/**
	 * 删除某个key
	 * 
	 * @param page
	 * @return
	 */
	public Long del(String key) {
		ShardedJedisPool shardPool = null;
		ShardedJedis redis = null;
		try {
			shardPool = RedisBucket.getInstance().getShardPool(redisName);
			redis = shardPool.getResource();
			return redis.del(key);

		} catch (Exception e) {
			logger.error("<del "+key+"->Exception>" + e.toString());
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
		return 0l;
	}
	
	public void set(String key, String value) {

		ShardedJedisPool shardPool = null;
		ShardedJedis redis = null;
		try {
			shardPool = RedisBucket.getInstance().getShardPool(redisName);
			redis = shardPool.getResource();
			redis.set(key, value);
		} catch (Exception e) {
			logger.error(AdStatisticsRedisService.class.getSimpleName()+" <hset->Exception>" + e.toString());
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
	}
	
	public String get(String key) {

		ShardedJedisPool shardPool = null;
		ShardedJedis redis = null;
		try {
			shardPool = RedisBucket.getInstance().getShardPool(redisName);
			redis = shardPool.getResource();
			return redis.get(key);
		} catch (Exception e) {
			logger.error(AdStatisticsRedisService.class.getSimpleName()+" <hset->Exception>" + e.toString());
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
		return null;
	}

	/**
	 * 获取总榜 数据
	 * 
	 * @param typeUser
	 *            =anchor 主播 =user 用户
	 * @return
	 */
	public Set<Tuple> getUserAllRankRq() {
		ShardedJedisPool shardPool = null;
		ShardedJedis redis = null;
		Set<Tuple> allRank = null;
		try {
			shardPool = RedisBucket.getInstance().getShardPool(redisName);
			redis = shardPool.getResource();
			allRank = redis.zrevrangeWithScores(RedisContant.anchorRqAll, 0, rankSize);
		} catch (Exception e) {
			logger.error("<getUserAllRankRq->Exception>" + e.toString());
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
		return allRank;
	}

	public int getUserAllRankRqScore(String uid) {
		ShardedJedisPool shardPool = null;
		ShardedJedis redis = null;
		Double zscore = null;
		try {
			shardPool = RedisBucket.getInstance().getShardPool(redisName);
			redis = shardPool.getResource();
			zscore = redis.zscore(RedisContant.anchorRqAll, uid);
		} catch (Exception e) {
			logger.error("<getUserAllRankRq->Exception>" + e.toString());
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
		return zscore == null ? 0 : zscore.intValue();
	}
	
	/**
	 * 设置禁止兑换的主播
	 * @param uid
	 */
	public void setForbidExchange(String uid){
		ShardedJedisPool shardPool = null;
		ShardedJedis redis = null;
		try {
			shardPool = RedisBucket.getInstance().getShardPool(redisName);
			redis = shardPool.getResource();
			redis.hset(RedisContant.forbidExchange, uid, "1");
		} catch (Exception e) {
			if (shardPool != null) {
				logger.info("redis 端口名" + RedisContant.RedisNameOther + " 数据库连接活跃数--<" + shardPool.getNumActive()
						+ ">--空闲连接数--<" + shardPool.getNumIdle() + ">--等待连接数--<" + shardPool.getNumWaiters() + ">");
			}else {
				logger.error("<setForbidExchange->Exception>" + e.toString());
			}
		}finally {
			if (redis != null) {
				redis.close();
			}
		}
	}
	
	/**
	 * 判断该主播是否被禁止兑换
	 * @param uid
	 * @return false没有禁止 true已禁止
	 */
	public boolean getForbidExchange(String uid){
		ShardedJedisPool shardPool = null;
		ShardedJedis redis = null;
		Boolean hexists = false;
		try {
			shardPool = RedisBucket.getInstance().getShardPool(redisName);
			redis = shardPool.getResource();
			hexists = redis.hexists(RedisContant.forbidExchange, uid);
		} catch (Exception e) {
			if (shardPool != null) {
				logger.info("redis 端口名" + RedisContant.RedisNameOther + " 数据库连接活跃数--<" + shardPool.getNumActive()
						+ ">--空闲连接数--<" + shardPool.getNumIdle() + ">--等待连接数--<" + shardPool.getNumWaiters() + ">");
			}else {
				logger.error("<getForbidExchange->Exception>" + e.toString());
			}
		}finally {
			if (redis != null) {
				redis.close();
			}
		}
		return hexists;
	}
	
	/**
	 * 获取指定日期的日榜 数据
	 * 
	 * @param typeUser
	 *            =anchor 主播 =user 用户
	 * @return
	 */
	public Set<Tuple> getUserDayRankForDate(Date date,long num) {
		Set<Tuple> dayRank = null;
		ShardedJedisPool shardPool = null;
		ShardedJedis redis = null;
		try {
			shardPool = RedisBucket.getInstance().getShardPool(redisName);
			redis = shardPool.getResource();
			String day = DateUtils.dateToString(date, "yyyyMMdd");
			dayRank = redis.zrevrangeWithScores(RedisContant.anchorRqDay + day, 0, num);

		} catch (Exception e) {
			logger.error("<getUserDayRankForDate->Exception>" + e.toString());
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
		return dayRank;
	}
	
	/**
	 * 设置用户 普通礼物 进背包
	 * @param uid
	 * @param gid
	 * @param obj
	 */
	public void setUserItemInfo(int uid,int gid,String obj){

		ShardedJedisPool shardPool = null;
		ShardedJedis redis = null;
		try {
			shardPool = RedisBucket.getInstance().getShardPool(redisName);
			redis = shardPool.getResource();
			redis.hset(RedisContant.userItem+uid, String.valueOf(gid), obj);

		} catch (Exception e) {
			if (shardPool != null) {
				logger.info("redis 端口名" + redisName+ " 数据库连接活跃数--<" + shardPool.getNumActive()
						+ ">--空闲连接数--<" + shardPool.getNumIdle() + ">--等待连接数--<" + shardPool.getNumWaiters() + ">");
			}else {
				logger.error("<setUserItemList->Exception>" + e.toString());
			}
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
	}

	/**
	 * 设置用户 徽章缓存
	 * @param uid
	 * @param gid
	 * @param obj
	 */
	public void setUserBadge(int uid,int gid,String obj){

		ShardedJedisPool shardPool = null;
		ShardedJedis redis = null;
		try {
			shardPool = RedisBucket.getInstance().getShardPool(redisName);
			redis = shardPool.getResource();
			redis.hset(RedisContant.userBadge+uid, String.valueOf(gid), obj);
			// 保留7天
			redis.expire(RedisContant.userBadge+uid, 604800);

		} catch (Exception e) {
			if (shardPool != null) {
				logger.info("redis 端口名" + redisName+ " 数据库连接活跃数--<" + shardPool.getNumActive()
						+ ">--空闲连接数--<" + shardPool.getNumIdle() + ">--等待连接数--<" + shardPool.getNumWaiters() + ">");
			}else {
				logger.error("<setUserBadge->Exception>" + e.toString());
			}
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
	}

	
	/**
	 * 获取用户的徽章列表
	 * @param uid
	 * @return
	 */
	public Map<String, String> getUserBadge(int uid){

		ShardedJedisPool shardPool = null;
		ShardedJedis redis = null;
		Map<String, String> map = new HashMap<String,String>();
		try {
			shardPool = RedisBucket.getInstance().getShardPool(redisName);
			redis = shardPool.getResource();
			map = redis.hgetAll(RedisContant.userBadge+uid);

		} catch (Exception e) {
			if (shardPool != null) {
				logger.info("redis 端口名" + redisName + " 数据库连接活跃数--<" + shardPool.getNumActive()
						+ ">--空闲连接数--<" + shardPool.getNumIdle() + ">--等待连接数--<" + shardPool.getNumWaiters() + ">");
			}else {
				logger.error("<getUserBadge->Exception>" + e.toString());
			}
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
		return map;
	}
	
	/**
	 * 获取用户背包礼物的详细
	 * @param uid
	 * @param gid
	 * @return
	 */
	public String getUserItemInfo(int uid,int gid){

		ShardedJedisPool shardPool = null;
		ShardedJedis redis = null;
		String hget = "";
		try {
			shardPool = RedisBucket.getInstance().getShardPool(redisName);
			redis = shardPool.getResource();
			hget = redis.hget(RedisContant.userItem+uid, String.valueOf(gid));

		} catch (Exception e) {
			if (shardPool != null) {
				logger.info("redis 端口名" + redisName + " 数据库连接活跃数--<" + shardPool.getNumActive()
						+ ">--空闲连接数--<" + shardPool.getNumIdle() + ">--等待连接数--<" + shardPool.getNumWaiters() + ">");
			}else {
				logger.error("<setUserItemList->Exception>" + e.toString());
			}
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
		return hget;
	}
	
	/**
	 * 获取用户的普通宝贝列表
	 * @param uid
	 * @return
	 */
	public Map<String, String> getUserItemList(int uid){

		ShardedJedisPool shardPool = null;
		ShardedJedis redis = null;
		Map<String, String> map = new HashMap<String,String>();
		try {
			shardPool = RedisBucket.getInstance().getShardPool(redisName);
			redis = shardPool.getResource();
			map = redis.hgetAll(RedisContant.userItem+uid);

		} catch (Exception e) {
			if (shardPool != null) {
				logger.info("redis 端口名" + redisName + " 数据库连接活跃数--<" + shardPool.getNumActive()
						+ ">--空闲连接数--<" + shardPool.getNumIdle() + ">--等待连接数--<" + shardPool.getNumWaiters() + ">");
			}else {
				logger.error("<setUserItemList->Exception>" + e.toString());
			}
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
		return map;
	}
	
	/**
	 * 设置用户 普通礼物 进背包
	 * @param uid
	 * @param gid
	 * @param obj
	 */
	public void setUserItemSpecialInfo(int uid,int gid,String obj){

		ShardedJedisPool shardPool = null;
		ShardedJedis redis = null;
		try {
			shardPool = RedisBucket.getInstance().getShardPool(redisName);
			redis = shardPool.getResource();
			redis.hset(RedisContant.userItemSpecail+uid, String.valueOf(gid), obj);

		} catch (Exception e) {
			if (shardPool != null) {
				logger.info("redis 端口名" + redisName+ " 数据库连接活跃数--<" + shardPool.getNumActive()
						+ ">--空闲连接数--<" + shardPool.getNumIdle() + ">--等待连接数--<" + shardPool.getNumWaiters() + ">");
			}else {
				logger.error("<setUserItemSpecialInfo->Exception>" + e.toString());
			}
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
	}
	
	/**
	 * 获取用户背包时效礼物的详细
	 * @param uid
	 * @param gid
	 * @return
	 */
	public String getUserItemSpecailInfo(int uid,int gid){

		ShardedJedisPool shardPool = null;
		ShardedJedis redis = null;
		String hget = "";
		try {
			shardPool = RedisBucket.getInstance().getShardPool(redisName);
			redis = shardPool.getResource();
			hget = redis.hget(RedisContant.userItemSpecail+uid, String.valueOf(gid));

		} catch (Exception e) {
			if (shardPool != null) {
				logger.info("redis 端口名" + redisName + " 数据库连接活跃数--<" + shardPool.getNumActive()
						+ ">--空闲连接数--<" + shardPool.getNumIdle() + ">--等待连接数--<" + shardPool.getNumWaiters() + ">");
			}else {
				logger.error("<getUserItemSpecailInfo->Exception>" + e.toString());
			}
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
		return hget;
	}
	
	/**
	 * 获取用户的时效宝贝列表
	 * @param uid
	 * @return
	 */
	public Map<String, String> getUserItemSpecialList(int uid){

		ShardedJedisPool shardPool = null;
		ShardedJedis redis = null;
		Map<String, String> map = new HashMap<String,String>();
		try {
			shardPool = RedisBucket.getInstance().getShardPool(redisName);
			redis = shardPool.getResource();
			map = redis.hgetAll(RedisContant.userItemSpecail+uid);

		} catch (Exception e) {
			if (shardPool != null) {
				logger.info("redis 端口名" + redisName + " 数据库连接活跃数--<" + shardPool.getNumActive()
						+ ">--空闲连接数--<" + shardPool.getNumIdle() + ">--等待连接数--<" + shardPool.getNumWaiters() + ">");
			}else {
				logger.error("<setUserItemList->Exception>" + e.toString());
			}
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
		return map;
	}
	
	/**
	 * 设置首充记录
	 * @param uid
	 * @param paytime
	 */
	public void setFirstPayStatus(int uid,long paytime){
		ShardedJedisPool shardPool = null;
		ShardedJedis redis = null;
		try {
			shardPool = RedisBucket.getInstance().getShardPool(redisName);
			redis = shardPool.getResource();
			redis.hset(RedisContant.firstPay, String.valueOf(uid), String.valueOf(paytime));

		} catch (Exception e) {
			if (shardPool != null) {
				logger.info("redis 端口名" + redisName + " 数据库连接活跃数--<" + shardPool.getNumActive()
						+ ">--空闲连接数--<" + shardPool.getNumIdle() + ">--等待连接数--<" + shardPool.getNumWaiters() + ">");
			}else {
				logger.error("<setFirstPayStatus->Exception>" + e.toString());
			}
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
	}
	
	public String getFirstPayStatus(int uid){
		ShardedJedisPool shardPool = null;
		ShardedJedis redis = null;
		String hget = "";
		
		try {
			shardPool = RedisBucket.getInstance().getShardPool(redisName);
			redis = shardPool.getResource();
			hget = redis.hget(RedisContant.firstPay, String.valueOf(uid));

		} catch (Exception e) {
			if (shardPool != null) {
				logger.info("redis 端口名" + redisName + " 数据库连接活跃数--<" + shardPool.getNumActive()
						+ ">--空闲连接数--<" + shardPool.getNumIdle() + ">--等待连接数--<" + shardPool.getNumWaiters() + ">");
			}else {
				logger.error("<getFirstPayStatus->Exception>" + e.toString());
			}
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
		return hget;
	}

	private final int weixinStateCodeExpired = 5*60;
	/**
	 * 
	 * @param state
	 * @param code
	 */
	public void saveWeixinWebCallCode(String state, String code) {
		ShardedJedisPool shardPool = null;
		ShardedJedis redis = null;
		try {
			shardPool = RedisBucket.getInstance().getShardPool(redisName);
			redis = shardPool.getResource();
			redis.setex(RedisContant.weixinState2Code+state,weixinStateCodeExpired,code);
		} catch (Exception e) {
			if (shardPool != null) {
				logger.info("redis 端口名" + redisName + " 数据库连接活跃数--<" + shardPool.getNumActive()
						+ ">--空闲连接数--<" + shardPool.getNumIdle() + ">--等待连接数--<" + shardPool.getNumWaiters() + ">");
			}else {
				logger.error(String.format("<saveWeixinWebCallCode->Exception>state:%s,code:%s,cause:%s",state,code,e.toString()));
			}
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
	}
	
	
	/**
	 * 添加身份设置
	 * 
	 * @param field
	 * @param value
	 */
	public Boolean setXzAuthPic(String field, String value) {
		Long lg = 0L;

		ShardedJedisPool shardPool = null;
		ShardedJedis redis = null;
		try {
			shardPool = RedisBucket.getInstance().getShardPool(redisName);
			redis = shardPool.getResource();
			lg = redis.hset(RedisContant.AuthXiaozhuPic, field, value);
		} catch (Exception e) {
			logger.error("<setXzAuthPic->Exception>" + e.toString());
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
		if (lg > 0) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 获取身份对于的认证图片
	 * 
	 * @param field
	 * @return
	 */
	public String getXzAuthpic(String field) {

		String val = "";

		ShardedJedisPool shardPool = null;
		ShardedJedis redis = null;
		try {
			shardPool = RedisBucket.getInstance().getShardPool(redisName);
			redis = shardPool.getResource();
			val = redis.hget(RedisContant.AuthXiaozhuPic, field);
		} catch (Exception e) {
			logger.error("<getXzAuthpic->Exception>" + e.toString());
		} finally {
			if (redis != null) {
				redis.close();
			}
		}

		if (val == null) {
			return "";
		} else {
			return val;
		}
	}
	
	public void zadd(String key, Double score, String member) {

		ShardedJedisPool shardPool = null;
		ShardedJedis redis = null;
		try {
			shardPool = RedisBucket.getInstance().getShardPool(redisName);
			redis = shardPool.getResource();
			redis.zadd(key, score, member);
		} catch (Exception e) {
			logger.error("<zadd->Exception>" + e);
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
	}
	
	
	public int zcard(String key) {
		ShardedJedisPool shardPool = null;
		ShardedJedis redis = null;
		int res = 0;
		try {
			shardPool = RedisBucket.getInstance().getShardPool(redisName);
			redis = shardPool.getResource();
			Long zcard = redis.zcard(key);
			if(zcard!=null){
				res = zcard.intValue();
			}
		} catch (Exception e) {
			logger.error("<zadd->Exception>" + e);
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
		return res;
	}
	
	public Set<Tuple> zrevrangeWithScores(String key,Long start, Long stop) {
		Set<Tuple> dayRank = null;
		ShardedJedisPool shardPool = null;
		ShardedJedis redis = null;
		try {
			shardPool = RedisBucket.getInstance().getShardPool(redisName);
			redis = shardPool.getResource();
			dayRank = redis.zrevrangeWithScores(key, start, stop);
		} catch (Exception e) {
			logger.error("<zrevrangeWithScores->Exception>" + e);
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
		return dayRank;
	}
	
	/**
	 * 设置新声援榜
	 * 
	 * @param srcuid  送礼人
	 * @param dstuid 收礼人
	 * @param count 数量
	 */
	public void setSupportRank(int srcuid, int dstuid, int count) {
		ShardedJedisPool shardPool = null;
		ShardedJedis redis = null;
		try {
			shardPool = RedisBucket.getInstance().getShardPool(redisName);
			redis = shardPool.getResource();
			String day = DateUtils.dateToString(null, "yyyyMMdd");
			String month = DateUtils.getTimesMonthmorning("yyyyMM");
			String week = DateUtils.getWeekStart(0);

			if (srcuid!=0) {
				// 日榜
				redis.zincrby(RedisContant.userSupportDay + day, count, String.valueOf(srcuid));
				// 周榜
				redis.zincrby(RedisContant.userSupportWeek + week, count, String.valueOf(srcuid));
				// 月榜
				redis.zincrby(RedisContant.userSupportMonth + month, count, String.valueOf(srcuid));
				// 总榜
				redis.zincrby(RedisContant.userSupportAll, count, String.valueOf(srcuid));
			}
			if (dstuid!=0) {
				// 日榜
				redis.zincrby(RedisContant.anchorSupportDay + day, count, String.valueOf(dstuid));
				// 周榜
				redis.zincrby(RedisContant.anchorSupportWeek + week, count, String.valueOf(dstuid));
				// 月榜
				redis.zincrby(RedisContant.anchorSupportMonth + month, count, String.valueOf(dstuid));
				// 总榜
				redis.zincrby(RedisContant.anchorSupportAll, count, String.valueOf(dstuid));
			}
		} catch (Exception e) {
			logger.error("设置魅力值异常",e);
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
	}
	
	/**
	 * 获取当前用户声援值
	 * 
	 * @param uid 用户ID
	 * @param type 1:当天;2:当周;3:当月;4:总值;
	 */
	public long getSupportByUid(int uid,int type) {
		ShardedJedisPool shardPool = null;
		ShardedJedis redis = null;
		try {
			shardPool = RedisBucket.getInstance().getShardPool(redisName);
			redis = shardPool.getResource();
			String day = DateUtils.dateToString(null, "yyyyMMdd");
			String month = DateUtils.getTimesMonthmorning("yyyyMM");
			String week = DateUtils.getWeekStart(0);
			if(1==type) {
				Double socre = redis.zscore(RedisContant.anchorSupportDay + day, String.valueOf(uid));
				if(socre==null) {
					return 0;
				}
				return socre.longValue();
			}
			if(2==type) {
				Double socre = redis.zscore(RedisContant.anchorSupportWeek + week, String.valueOf(uid));
				if(socre==null) {
					return 0;
				}
				return socre.longValue();
			}
			if(3==type) {
				Double socre = redis.zscore(RedisContant.anchorSupportMonth + month, String.valueOf(uid));
				if(socre==null) {
					return 0;
				}
				return socre.longValue();
			}
			if(4==type){
				Double socre = redis.zscore(RedisContant.anchorSupportAll, String.valueOf(uid));
				if(socre==null) {
					return 0;
				}
				return socre.longValue();
			}
			return 0;
		} catch (Exception e) {
			logger.error("获取声援值异常",e);
			return 0;
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
	}
	/**
	 * 当前用户对主播新声援值得贡献榜
	 * 
	 * @param srcuid  送礼人
	 * @param dstuid 收礼人
	 * @param count 数量
	 */
	public void setSupportRankByDstuid(int srcuid, int dstuid, int count) {
		ShardedJedisPool shardPool = null;
		ShardedJedis redis = null;
		try {
			shardPool = RedisBucket.getInstance().getShardPool(redisName);
			redis = shardPool.getResource();
			String day = DateUtils.dateToString(null, "yyyyMMdd");
			String month = DateUtils.getTimesMonthmorning("yyyyMM");
			String week = DateUtils.getWeekStart(0);
			if (srcuid!=0 && dstuid!=0) {
				// 日榜
				redis.zincrby(RedisContant.userSupportForAnchorDay + day + ":" + dstuid, count, String.valueOf(srcuid));
				// 周榜
				redis.zincrby(RedisContant.userSupportForAnchorWeek + week + ":" + dstuid, count, String.valueOf(srcuid));
				// 月榜
				redis.zincrby(RedisContant.userSupportForAnchorMonth + month + ":" + dstuid, count, String.valueOf(srcuid));
				// 总榜
				redis.zincrby(RedisContant.userSupportForAnchorAll + dstuid, count, String.valueOf(srcuid));
			}
		} catch (Exception e) {
			logger.error("设置新声援值异常",e);
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
	}
	
	/**
	 * 获取该主播的新声援值贡献榜
	 * 
	 * @param dstuid 主播ID
	 * @param type 1:当天;2:当周;3:当月;4:总值; 其他值返回null
	 * @return
	 */
	public Set<Tuple> getSupportContributionRankByDstuid(Integer dstuid,int type,int page) {
		ShardedJedisPool shardPool = null;
		ShardedJedis redis = null;
		try {
			shardPool = RedisBucket.getInstance().getShardPool(redisName);
			redis = shardPool.getResource();
			if(type==1) {
				String day = DateUtils.dateToString(null, "yyyyMMdd");
				return redis.zrevrangeWithScores(RedisContant.userSupportForAnchorDay + day + ":" + dstuid , (page - 1) * pageSiz,page * pageSiz - 1);
			}else if(type==2) {
				String week = DateUtils.getWeekStart(0);
				return redis.zrevrangeWithScores(RedisContant.userSupportForAnchorWeek + week + ":" + dstuid , (page - 1) * pageSiz,page * pageSiz - 1);
			}else if(type==3) {
				String month = DateUtils.getTimesMonthmorning("yyyyMM");
				return redis.zrevrangeWithScores(RedisContant.userSupportForAnchorMonth + month + ":" + dstuid , (page - 1) * pageSiz,page * pageSiz - 1);
			}else if(type==4) {
				return redis.zrevrangeWithScores(RedisContant.userSupportForAnchorAll + dstuid , (page - 1) * pageSiz,page * pageSiz - 1);
			}
			return null;
		} catch (Exception e) {
			logger.error("获取新声援值贡献榜异常",e);
			return null;
		} finally {
			if (redis != null) {
				redis.close();
			}
			
		}
	}
	
	/**
	 * 获取主播有多少人贡献过声援值
	 * 
	 * @param dstuid 主播ID
	 * @param type 1:当天;2:当周;3:当月;4:总值; 其他值返回0
	 * @return
	 */
	public Long getSupportContributionTotalByDstuid(Integer dstuid, int type) {
		ShardedJedisPool shardPool = null;
		ShardedJedis redis = null;
		try {
			shardPool = RedisBucket.getInstance().getShardPool(redisName);
			redis = shardPool.getResource();
			if(type==1) {
				String day = DateUtils.dateToString(null, "yyyyMMdd");
				return redis.zcard(RedisContant.userSupportForAnchorDay + day + ":" + dstuid);
			}else if(type==2) {
				String week = DateUtils.getWeekStart(0);
				return redis.zcard(RedisContant.userSupportForAnchorWeek + week + ":" + dstuid);
			}else if(type==3) {
				String month = DateUtils.getTimesMonthmorning("yyyyMM");
				return redis.zcard(RedisContant.userSupportForAnchorMonth + month + ":" + dstuid );
			}else if(type==4) {
				return redis.zcard(RedisContant.userSupportForAnchorAll + dstuid);
			}
			return 0L;
		} catch (Exception e) {
			logger.error("获取主播有多少人贡献过声援值",e);
			return 0L;
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
	}
	
	
	/**
	 * 判断用户是否为扶持号
	 * 
	 * @param uid 用户ID
	 */
	public boolean existsSp(int uid) {
		ShardedJedisPool shardPool = null;
		ShardedJedis redis = null;
		try {
			shardPool = RedisBucket.getInstance().getShardPool(redisName);
			redis = shardPool.getResource();
			return redis.sismember(RedisContant.supportUsersID, String.valueOf(uid));
		} catch (Exception e) {
			logger.error("判断是否为扶持号",e);
			return false;
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
	}
	
	/**
	 * 获取月榜 数据
	 * 
	 * @return
	 */
	public Set<Tuple> getAnchorMonthSupportRank(long num) {
		ShardedJedisPool shardPool = null;
		ShardedJedis redis = null;
		Set<Tuple> monthRank = null;
		try {
			shardPool = RedisBucket.getInstance().getShardPool(redisName);
			redis = shardPool.getResource();
			String month = DateUtils.getTimesMonthmorning("yyyyMM");
			monthRank = redis.zrevrangeWithScores(RedisContant.anchorSupportMonth + month, 0, num);

		} catch (Exception e) {
			logger.error("<getAnchorMonthSupportRank->Exception>" + e.toString());
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
		return monthRank;
	}

	/**
	 * 设置房间守护排序（按金币排序）
	 * @param uid
	 * @param dstuid
	 * @param realpricetotal
	 */
	public void setRoomAllGuardSort(Integer uid, Integer dstuid, Integer realpricetotal) {
		ShardedJedisPool shardPool = null;
		ShardedJedis redis = null;
		try {
			shardPool = RedisBucket.getInstance().getShardPool(redisName);
			redis = shardPool.getResource();
			redis.zincrby(RedisContant.roomAllGuardSort+dstuid, Double.valueOf(realpricetotal), String.valueOf(uid));
		} catch (Exception e) {
			logger.error("<setRoomAllGuardSort->Exception>" + e.toString());
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
	}
	
}
