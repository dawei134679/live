package com.tinypig.admin.redis.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tinypig.admin.redis.core.RedisBucket;
import com.tinypig.admin.util.RedisContant;

import redis.clients.jedis.Jedis;

/**
 * 缓存用户信息
 * 
 * @author fang
 *
 */
public class UserRedisService {
	private static final Logger logger = LoggerFactory.getLogger(RelationRedisService.class.getSimpleName());
	private final static UserRedisService instance = new UserRedisService();

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
		Jedis redis = RedisBucket.getInstance().initialize(RedisContant.host, RedisContant.port6379);
		try {
			redis.hset(RedisContant.keyBaseInfoList, uid.toString(), strJson);
		} catch (Exception e) {
			logger.error("<setUserBaseInfo->Exception>" + e.toString());
		} finally {
			redis.close();
		}
	}

	/**
	 * 获取缓存用户信息
	 * 
	 * @param uid
	 * @return
	 */
	public String getUserBaseInfo(Integer uid) {
		Jedis redis = RedisBucket.getInstance().initialize(RedisContant.host, RedisContant.port6379);
		try {
			return redis.hget(RedisContant.keyBaseInfoList, uid.toString());
		} catch (Exception e) {
			logger.error("<getUserBaseInfo->Exception>" + e.toString());
		} finally {
			redis.close();
		}
		return null;
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
		Jedis redis = null;
		try {
			redis = RedisBucket.getInstance().initialize(RedisContant.host, RedisContant.port6379);
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
	 * 获取缓存用户信息 集合
	 * 
	 * @param list
	 * @param pageFlag
	 * @return
	 */
	public List<String> getUserBaseInfo(List<String> list,boolean pageFlag) {
		if(null == list || list.size()==0){
			return null;
		}
		Jedis redis = null;
		try {
			redis = RedisBucket.getInstance().initialize(RedisContant.host, RedisContant.port6379);
			if(!pageFlag||list.size()<=50) {
				return redis.hmget(RedisContant.keyBaseInfoList, list.toArray(new String[0]));
			}
			
			List<String> resList = new ArrayList<String>();
			
			List<String> tempList =  new ArrayList<String>();
			for (int i = 0; i < list.size(); i++) {
				tempList.add(list.get(i));
				if(i%100==0) {
					List<String> dbList = redis.hmget(RedisContant.keyBaseInfoList, tempList.toArray(new String[0]));
					resList.addAll(dbList);
					tempList.clear();
				}
			}
			if(tempList.size()>0) {
				List<String> dbList = redis.hmget(RedisContant.keyBaseInfoList, tempList.toArray(new String[0]));
				resList.addAll(dbList);
			}
			return resList;
		} catch (Exception e) {
			logger.error("<getUserBaseInfo->Exception>" + e.toString());
			return null;
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
	}
	
	/**
	 * 缓存用户账户
	 * 
	 * @param uid
	 * @param strJson
	 */
	public void setUserAccount(Integer uid, String strJson) {
		Jedis redis = RedisBucket.getInstance().initialize(RedisContant.host, RedisContant.port6379);
		try {
			redis.hset(RedisContant.keyAccountList, uid.toString(), strJson);
		} catch (Exception e) {
			logger.error("<setUserAccount->Exception>" + e.toString());
		} finally {
			redis.close();
		}
	}

	/**
	 * 获取缓存账号信息
	 * 
	 * @param uid
	 * @return
	 */
	public String getUserAccount(Integer uid) {
		Jedis redis = RedisBucket.getInstance().initialize(RedisContant.host, RedisContant.port6379);
		try {
			return redis.hget(RedisContant.keyAccountList, uid.toString());
		} catch (Exception e) {
			logger.error("<getUserAccount->Exception>" + e.toString());
		} finally {
			redis.close();
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
		Jedis redis = RedisBucket.getInstance().initialize(RedisContant.host, RedisContant.port6379);
		try {
			redis.hset(RedisContant.keyAccountNameAndUid, accountName, uid.toString());
		} catch (Exception e) {
			logger.error("<setAccountNameAndUid->Exception>" + e.toString());
		} finally {
			redis.close();
		}
	}

	/**
	 * 通过账号获取uid
	 * 
	 * @param accountName
	 * @return
	 */
	public int getAccountNameAndUid(String accountName) {
		Jedis redis = RedisBucket.getInstance().initialize(RedisContant.host, RedisContant.port6379);
		String uid = "";
		try {
			uid = redis.hget(RedisContant.keyAccountNameAndUid, accountName);
		} catch (Exception e) {
			logger.error("<setAccountNameAndUid->Exception>" + e.toString());
		} finally {
			redis.close();
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
		Jedis redis = RedisBucket.getInstance().initialize(RedisContant.host, RedisContant.port6379);
		try {
			redis.hset(RedisContant.keyAuthKeyAndUid, authKey, uid.toString());
		} catch (Exception e) {
			logger.error("<setAuthKeyAndUid->Exception>" + e.toString());
		} finally {
			redis.close();
		}
	}

	/**
	 * 通过第三方标示获取uid
	 * 
	 * @param authKey
	 * @return
	 */
	public int getAuthKeyAndUid(String authKey) {
		Jedis redis = RedisBucket.getInstance().initialize(RedisContant.host, RedisContant.port6379);
		String uid = "";
		try {
			uid = redis.hget(RedisContant.keyAuthKeyAndUid, authKey);
		} catch (Exception e) {
			logger.error("<getAuthKeyAndUid->Exception>" + e.toString());
		} finally {
			redis.close();
		}
		if (uid == null || uid == "") {
			return 0;
		} else {
			return Integer.valueOf(uid);
		}
	}

	/**
	 * 设置手机与uid的关系
	 * @param mobile
	 * @param uid
	 */
	public void setMobileAndUid(String mobile, int uid) {
		Jedis redis = RedisBucket.getInstance().initialize(RedisContant.host, RedisContant.port6379);
		try {
			redis.hset(RedisContant.keyMbileAndUid, mobile, String.valueOf(uid));
		} catch (Exception e) {
			logger.error("<setMobileAndUid->Exception>" + e.toString());
		} finally {
			redis.close();
		}
	}
	
	/**
	 * 通过手机号获取uid
	 * @param mobile
	 * @return
	 */
	public int getMobileAndUid(String mobile){
		Jedis redis = RedisBucket.getInstance().initialize(RedisContant.host, RedisContant.port6379);
		String uid = "";
		try {
			uid = redis.hget(RedisContant.keyMbileAndUid, mobile);
		} catch (Exception e) {
			logger.error("<getMobileAndUid->Exception>" + e.toString());
		} finally {
			redis.close();
		}
		if (uid == null || uid == "") {
			return 0;
		} else {
			return Integer.valueOf(uid);
		}
	}
	
	/**
	 * 删除手机与uid的关系
	 * @param mobile
	 */
	public void delMobileAndUid(String mobile){
		Jedis redis = RedisBucket.getInstance().initialize(RedisContant.host, RedisContant.port6379);
		try {
			redis.hdel(RedisContant.keyMbileAndUid,mobile);
		} catch (Exception e) {
			logger.error("<delMobileAndUid->Exception>" + e.toString());
		} finally {
			redis.close();
		}
	}

	/**
	 * 缓存用户与提现账户的关联
	 * 
	 * @param uid
	 * @param strJson
	 */
	public void setPayAccount(Integer uid, String strJson) {
		Jedis redis = RedisBucket.getInstance().initialize(RedisContant.host, RedisContant.port6379);
		try {
			redis.hset(RedisContant.keyPayAccountList, uid.toString(), strJson);
		} catch (Exception e) {
			logger.error("<setUserAccount->Exception>" + e.toString());
		} finally {
			redis.close();
		}
	}

	/**
	 * 获取用户与提现账号的关联
	 * 
	 * @param uid
	 * @return
	 */
	public String getPayAccount(Integer uid) {
		Jedis redis = RedisBucket.getInstance().initialize(RedisContant.host, RedisContant.port6379);
		try {
			return redis.hget(RedisContant.keyPayAccountList, uid.toString());
		} catch (Exception e) {
			logger.error("<setUserAccount->Exception>" + e.toString());
		} finally {
			redis.close();
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
		Jedis redis = RedisBucket.getInstance().initialize(RedisContant.host, RedisContant.port6379);
		try {
			redis.hset(RedisContant.keyAssetList, uid.toString(), strJson);
		} catch (Exception e) {
			logger.error("<setUserAsset->Exception>" + e.toString());
		} finally {
			redis.close();
		}
	}

	/**
	 * 获取用户资产
	 * 
	 * @param uid
	 * @return
	 */
	public String getUserAsset(Integer uid) {
		Jedis redis = RedisBucket.getInstance().initialize(RedisContant.host, RedisContant.port6379);
		try {
			return redis.hget(RedisContant.keyAssetList, uid.toString());
		} catch (Exception e) {
			logger.error("<getUserAsset->Exception>" + e.toString());
		} finally {
			redis.close();
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
		Jedis redis = RedisBucket.getInstance().initialize(RedisContant.host, RedisContant.port6379);
		try {
			redis.hset(RedisContant.keyLiveInfoList, uid.toString(), strJson);
		} catch (Exception e) {
			logger.error("<setLiveInfo->Exception>" + e.toString());
		} finally {
			redis.close();
		}
	}

	/**
	 * 获取开播列表(只表示 开播过)
	 * 
	 * @param uid
	 * @return
	 */
	public String getLiveInfo(Integer uid) {
		Jedis redis = RedisBucket.getInstance().initialize(RedisContant.host, RedisContant.port6379);
		try {
			return redis.hget(RedisContant.keyLiveInfoList, uid.toString());
		} catch (Exception e) {
			logger.error("<getLiveInfo->Exception>" + e.toString());
		} finally {
			redis.close();
		}
		return null;
	}

	/**
	 * 停播删除该记录
	 * 
	 * @param uid
	 */
	public void delLiveInfo(int uid) {
		Jedis redis = RedisBucket.getInstance().initialize(RedisContant.host, RedisContant.port6379);
		try {
			redis.hdel(RedisContant.keyLiveInfoList, String.valueOf(uid));
		} catch (Exception e) {
			logger.error("<delLiveInfo->Exception>" + e.toString());
		} finally {
			redis.close();
		}
	}

	/**
	 * 缓存礼物列表
	 * 
	 * @param gid
	 * @param strJson
	 */
	public void setGiftConfigList(Integer gid, String strJson) {
		Jedis redis = RedisBucket.getInstance().initialize(RedisContant.host, RedisContant.port6379);
		try {
			redis.hset(RedisContant.KeyGiftConfigList, gid.toString(), strJson);
		} catch (Exception e) {
			logger.error("<SetGiftConfigList->Exception>" + e.toString());
		} finally {
			redis.close();
		}
	}

	/**
	 * 获取礼物列表
	 * 
	 * @param gid
	 * @return
	 */
	public String getGiftConfigList(Integer gid) {
		Jedis redis = RedisBucket.getInstance().initialize(RedisContant.host, RedisContant.port6379);
		try {
			return redis.hget(RedisContant.KeyGiftConfigList, gid.toString());
		} catch (Exception e) {
			logger.error("<getGiftConfigList->Exception>" + e.toString());
		} finally {
			redis.close();
		}
		return null;
	}
	
	public void setManager(int uid){
		
	}
	
	public void getManagerList(){
		
	}
}
