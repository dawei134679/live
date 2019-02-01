package com.mpig.api.redis.service;

import org.apache.log4j.Logger;

import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

import com.mpig.api.redis.core.RedisBucket;

public class AdStatisticsRedisService {
	private static Logger logger = Logger.getLogger("redislog");
	
	private final static AdStatisticsRedisService instance = new AdStatisticsRedisService();
	private final static String redisName = "statistics";
	public static AdStatisticsRedisService getInstance() {
		return instance;
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
	
	public void setex(String key, int seconds, String value) {

		ShardedJedisPool shardPool = null;
		ShardedJedis redis = null;
		try {
			shardPool = RedisBucket.getInstance().getShardPool(redisName);
			redis = shardPool.getResource();
			redis.setex(key, seconds, value);
		} catch (Exception e) {
			logger.error(AdStatisticsRedisService.class.getSimpleName()+" <hset->Exception>" + e.toString());
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
	}
	
	public void hset(String key, String hkey, String value) {

		ShardedJedisPool shardPool = null;
		ShardedJedis redis = null;
		try {
			shardPool = RedisBucket.getInstance().getShardPool(redisName);
			redis = shardPool.getResource();
			redis.hset(key, hkey, value);
		} catch (Exception e) {
			logger.error(AdStatisticsRedisService.class.getSimpleName()+" <hset->Exception>" + e.toString());
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
	}
	
	public String hget(String key, String hkey) {

		ShardedJedisPool shardPool = null;
		ShardedJedis redis = null;
		try {
			shardPool = RedisBucket.getInstance().getShardPool(redisName);
			redis = shardPool.getResource();
			return redis.hget(key, hkey);
		} catch (Exception e) {
			logger.error("<hget->Exception>" + e.toString());
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
		return null;
	}
	

	public Long expire(String key, int seconds) {

		ShardedJedisPool shardPool = null;
		ShardedJedis redis = null;
		try {
			shardPool = RedisBucket.getInstance().getShardPool(redisName);
			redis = shardPool.getResource();
			return redis.expire(key, seconds);

		} catch (Exception e) {
			logger.error("<expire "+key+"->Exception>" + e.toString());
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
		return null;
	}

	public boolean exists(String key) {

		ShardedJedis redis = RedisBucket.getInstance().getShardPool(redisName).getResource();
		try {
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
	
	public Long del(String key) {

		ShardedJedis redis = RedisBucket.getInstance().getShardPool(redisName).getResource();
		try {
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
}
