package com.mpig.api.redis.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import com.mpig.api.redis.core.RedisBucket;

import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;
import redis.clients.jedis.Tuple;

public class RedisCommService {
	
	private static Logger logger = Logger.getLogger("redislog");
	private final static RedisCommService instance = new RedisCommService();

	public static RedisCommService getInstance() {
		return instance;
	}
	
	/**
	 * incrBy
	 * @param redisName
	 * @param key
	 * @param count
	 * @param seconds
	 */
	public Long incrBy(String redisName,String key,int count,int seconds){

		ShardedJedisPool shardPool = null;;
		ShardedJedis redis = null;
		Long incrBy = null;
		try {
			shardPool = RedisBucket.getInstance().getShardPool(redisName);
			redis = shardPool.getResource();
			
			incrBy = redis.incrBy(key, count);
			if (seconds > 0 && incrBy == count) {
				redis.expire(key, seconds);
			}
			
		} catch (Exception e) {
			logger.info("redis 端口名" + redisName + " 数据库连接活跃数--<" + shardPool.getNumActive()
					+ ">--空闲连接数--<" + shardPool.getNumIdle() + ">--等待连接数--<" + shardPool.getNumWaiters() + ">");
			logger.error("incrBy-Exception:", e);
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
		return incrBy;
	}
	
	public Double zincrby(String redisName,String key,int score,String member,int seconds){

		ShardedJedisPool shardPool = null;;
		ShardedJedis redis = null;
		Double zincrby = 0.0;
		try {
			shardPool = RedisBucket.getInstance().getShardPool(redisName);
			redis = shardPool.getResource();
			
			zincrby = redis.zincrby(key, score, member);
			if (seconds > 0 && zincrby == score) {
				redis.expire(key, seconds);
			}
			
		} catch (Exception e) {
			logger.info("redis 端口名" + redisName + " 数据库连接活跃数--<" + shardPool.getNumActive()
					+ ">--空闲连接数--<" + shardPool.getNumIdle() + ">--等待连接数--<" + shardPool.getNumWaiters() + ">");
			logger.error("incrBy-Exception:", e);
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
		return zincrby;
	}
	
	public Long zadd(String redisName,String key,int score,String member){

		ShardedJedisPool shardPool = null;;
		ShardedJedis redis = null;
		Long zadd = 0l;
		try {
			shardPool = RedisBucket.getInstance().getShardPool(redisName);
			redis = shardPool.getResource();
			zadd = redis.zadd(key, score, member);
		} catch (Exception e) {
			logger.info("redis 端口名" + redisName + " 数据库连接活跃数--<" + shardPool.getNumActive()
					+ ">--空闲连接数--<" + shardPool.getNumIdle() + ">--等待连接数--<" + shardPool.getNumWaiters() + ">");
			logger.error("zadd-Exception:", e);
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
		return zadd;
	}
	public Long zcard(String redisName,String key){
		
		ShardedJedisPool shardPool = null;;
		ShardedJedis redis = null;
		Long zadd = 0l;
		try {
			shardPool = RedisBucket.getInstance().getShardPool(redisName);
			redis = shardPool.getResource();
			zadd = redis.zcard(key);
		} catch (Exception e) {
			logger.info("redis 端口名" + redisName + " 数据库连接活跃数--<" + shardPool.getNumActive()
					+ ">--空闲连接数--<" + shardPool.getNumIdle() + ">--等待连接数--<" + shardPool.getNumWaiters() + ">");
			logger.error("zcard-Exception:", e);
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
		return zadd;
	}
	
	public int zrevrank(String redisName,String key,String member){

		ShardedJedisPool shardPool = null;;
		ShardedJedis redis = null;
		Long zrevrank = 0L;
		try {
			shardPool = RedisBucket.getInstance().getShardPool(redisName);
			redis = shardPool.getResource();
			
			zrevrank = redis.zrevrank(key, member);
		} catch (Exception e) {
			logger.info("redis 端口名" + redisName + " 数据库连接活跃数--<" + shardPool.getNumActive()
					+ ">--空闲连接数--<" + shardPool.getNumIdle() + ">--等待连接数--<" + shardPool.getNumWaiters() + ">");
			logger.error("incrBy-Exception:", e);
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
		if (zrevrank == null) {
			zrevrank = 1000L;
		}else {
			zrevrank = zrevrank+1;
		}
		return zrevrank.intValue();
	}
	
	public int zscore(String redisName,String key,String member){
		ShardedJedisPool shardPool = null;;
		ShardedJedis redis = null;
		Double zscore = 0.0;
		try {
			shardPool = RedisBucket.getInstance().getShardPool(redisName);
			redis = shardPool.getResource();
			
			zscore = redis.zscore(key, member);
		} catch (Exception e) {
			logger.info("redis 端口名" + redisName + " 数据库连接活跃数--<" + shardPool.getNumActive()
					+ ">--空闲连接数--<" + shardPool.getNumIdle() + ">--等待连接数--<" + shardPool.getNumWaiters() + ">");
			logger.error("incrBy-Exception:", e);
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
		if (zscore == null) {
			zscore = 0d;
		}
		return zscore.intValue();
	}
	
	public int zremrangeByScore(String redisName,String key, String start, String end){

		ShardedJedisPool shardPool = null;;
		ShardedJedis redis = null;
		Long zscore = 0l;
		try {
			shardPool = RedisBucket.getInstance().getShardPool(redisName);
			redis = shardPool.getResource();
			
			zscore = redis.zremrangeByScore(key, start, end);
		} catch (Exception e) {
			logger.info("redis 端口名" + redisName + " 数据库连接活跃数--<" + shardPool.getNumActive()
					+ ">--空闲连接数--<" + shardPool.getNumIdle() + ">--等待连接数--<" + shardPool.getNumWaiters() + ">");
			logger.error("zremrangeByScore-Exception:", e);
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
		return zscore.intValue();
	}
	
	public int zrem(String redisName,String key, String... members){

		if(null == members || 0 >= members.length){
			return 0;
		}
		
		ShardedJedisPool shardPool = null;;
		ShardedJedis redis = null;
		Long zrem = 0l;
		try {
			shardPool = RedisBucket.getInstance().getShardPool(redisName);
			redis = shardPool.getResource();
			
			zrem = redis.zrem(key, members);
		} catch (Exception e) {
			logger.info("redis 端口名" + redisName + " 数据库连接活跃数--<" + shardPool.getNumActive()
					+ ">--空闲连接数--<" + shardPool.getNumIdle() + ">--等待连接数--<" + shardPool.getNumWaiters() + ">");
			logger.error("zrem-Exception:", e);
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
		return zrem.intValue();
	}
	
	public Set<Tuple> zrevrangeWithScores(String redisName,String key,int start,int end){

		ShardedJedisPool shardPool = null;;
		ShardedJedis redis = null;
		Set<Tuple> zrevrangeWithScores = null;
		try {
			shardPool = RedisBucket.getInstance().getShardPool(redisName);
			redis = shardPool.getResource();
			
			zrevrangeWithScores = redis.zrevrangeWithScores(key, start, end);
		} catch (Exception e) {
			logger.info("redis 端口名" + redisName + " 数据库连接活跃数--<" + shardPool.getNumActive()
					+ ">--空闲连接数--<" + shardPool.getNumIdle() + ">--等待连接数--<" + shardPool.getNumWaiters() + ">");
			logger.error("incrBy-Exception:", e);
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
		return zrevrangeWithScores;
	}
	
	public Set<String> zrevrange(String redisName,String key,int start,int end){

		ShardedJedisPool shardPool = null;;
		ShardedJedis redis = null;
		Set<String> zrevrange = null;
		try {
			shardPool = RedisBucket.getInstance().getShardPool(redisName);
			redis = shardPool.getResource();
			
			zrevrange = redis.zrevrange(key, start, end);
		} catch (Exception e) {
			logger.info("redis 端口名" + redisName + " 数据库连接活跃数--<" + shardPool.getNumActive()
					+ ">--空闲连接数--<" + shardPool.getNumIdle() + ">--等待连接数--<" + shardPool.getNumWaiters() + ">");
			logger.error("incrBy-Exception:", e);
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
		return zrevrange;
	}
	
	
	/**
	 * set 
	 * @param redisName
	 * @param key
	 * @param value
	 * @param seconds
	 */
	public void set(String redisName,String key,String value,int seconds) {

		ShardedJedisPool shardPool = null;;
		ShardedJedis redis = null;
		try {
			shardPool = RedisBucket.getInstance().getShardPool(redisName);
			redis = shardPool.getResource();
			
			redis.set(key, value);
			if (seconds > 0 ) {
				redis.expire(key, seconds);
			}
			
		} catch (Exception e) {
			logger.info("redis 端口名" + redisName + " 数据库连接活跃数--<" + shardPool.getNumActive()
					+ ">--空闲连接数--<" + shardPool.getNumIdle() + ">--等待连接数--<" + shardPool.getNumWaiters() + ">");
			logger.error("set-Exception:", e);
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
	}
	
	/**
	 * get
	 * @param redisName
	 * @param key
	 */
	public String get(String redisName,String key) {

		ShardedJedisPool shardPool = null;;
		ShardedJedis redis = null;
		String string = null;
		try {
			shardPool = RedisBucket.getInstance().getShardPool(redisName);
			redis = shardPool.getResource();
			
			string = redis.get(key);
			
		} catch (Exception e) {
			logger.info("redis 端口名" + redisName + " 数据库连接活跃数--<" + shardPool.getNumActive()
					+ ">--空闲连接数--<" + shardPool.getNumIdle() + ">--等待连接数--<" + shardPool.getNumWaiters() + ">");
			logger.error("get-Exception:", e);
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
		return string;
	}
	
	/**
	 * del
	 * @param redisName
	 * @param key
	 */
	public void del(String redisName,String key) {

		ShardedJedisPool shardPool = null;;
		ShardedJedis redis = null;
		try {
			shardPool = RedisBucket.getInstance().getShardPool(redisName);
			redis = shardPool.getResource();
			
			redis.del(key);
			
		} catch (Exception e) {
			logger.info("redis 端口名" + redisName + " 数据库连接活跃数--<" + shardPool.getNumActive()
					+ ">--空闲连接数--<" + shardPool.getNumIdle() + ">--等待连接数--<" + shardPool.getNumWaiters() + ">");
			logger.error("del-Exception:", e);
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
	}

	/**
	 * hset
	 * @param redisName
	 * @param key
	 * @param field
	 * @param value
	 * @param seconds
	 */
	public int hset(String redisName,String key,String field,String value,int seconds) {

		ShardedJedisPool shardPool = null;;
		ShardedJedis redis = null;
		try {
			shardPool = RedisBucket.getInstance().getShardPool(redisName);
			redis = shardPool.getResource();
			
			Long hset = redis.hset(key, field, value);
			if (seconds > 0 && hset == 1) {
				redis.expire(key, seconds);
			}
			return hset.intValue();
		} catch (Exception e) {
			logger.info("redis 端口名" + redisName + " 数据库连接活跃数--<" + shardPool.getNumActive()
					+ ">--空闲连接数--<" + shardPool.getNumIdle() + ">--等待连接数--<" + shardPool.getNumWaiters() + ">");
			logger.error("hset-Exception:", e);
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
		return -1;
	}

	/**
	 * hset
	 * @param redisName
	 * @param key
	 * @param field
	 */
	public String hget(String redisName,String key,String field) {

		ShardedJedisPool shardPool = null;;
		ShardedJedis redis = null;
		String hget = "";
		
		try {
			shardPool = RedisBucket.getInstance().getShardPool(redisName);
			redis = shardPool.getResource();
			
			hget = redis.hget(key, field);
			
		} catch (Exception e) {
			logger.info("redis 端口名" + redisName + " 数据库连接活跃数--<" + shardPool.getNumActive()
					+ ">--空闲连接数--<" + shardPool.getNumIdle() + ">--等待连接数--<" + shardPool.getNumWaiters() + ">");
			logger.error("hget-Exception:", e);
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
		return hget;
	}
	/**
	 * hset
	 * @param redisName
	 * @param key
	 * @param field
	 */
	public List<String> hget(String redisName,String key,String... fields) {

		if(null == fields || 0 >= fields.length){
			return null;
		}
		
		ShardedJedisPool shardPool = null;;
		ShardedJedis redis = null;
		List<String> hget = null;
		
		try {
			shardPool = RedisBucket.getInstance().getShardPool(redisName);
			redis = shardPool.getResource();
			
			hget = redis.hmget(key, fields);
			
		} catch (Exception e) {
			logger.info("redis 端口名" + redisName + " 数据库连接活跃数--<" + shardPool.getNumActive()
					+ ">--空闲连接数--<" + shardPool.getNumIdle() + ">--等待连接数--<" + shardPool.getNumWaiters() + ">");
			logger.error("hget-Exception:", e);
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
		return hget;
	}
	/**
	 * hgetall
	 * @param redisName
	 * @param key
	 * @return
	 */
	public Map<String, String> hgetAll(String redisName,String key) {

		ShardedJedisPool shardPool = null;;
		ShardedJedis redis = null;
		Map<String, String> hgetAll = new HashMap<String,String>();
		try {
			shardPool = RedisBucket.getInstance().getShardPool(redisName);
			redis = shardPool.getResource();
			
			hgetAll = redis.hgetAll(key);
			
		} catch (Exception e) {
			logger.info("redis 端口名" + redisName + " 数据库连接活跃数--<" + shardPool.getNumActive()
					+ ">--空闲连接数--<" + shardPool.getNumIdle() + ">--等待连接数--<" + shardPool.getNumWaiters() + ">");
			logger.error("hgetAll-Exception:", e);
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
		return hgetAll;
	}

	/**
	 * hdel
	 * @param redisName
	 * @param key
	 * @param fields
	 */
	public void hdel(String redisName,String key,String... fields) {
		if(null == fields || 0 >= fields.length){
			return;
		}
		
		ShardedJedisPool shardPool = null;;
		ShardedJedis redis = null;
		try {
			shardPool = RedisBucket.getInstance().getShardPool(redisName);
			redis = shardPool.getResource();
			
			redis.hdel(key, fields);
			
		} catch (Exception e) {
			logger.info("redis 端口名" + redisName + " 数据库连接活跃数--<" + shardPool.getNumActive()
					+ ">--空闲连接数--<" + shardPool.getNumIdle() + ">--等待连接数--<" + shardPool.getNumWaiters() + ">");
			logger.error("hdel-Exception:", e);
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
	}
	
	/**
	 * lpush
	 * @param redisName
	 * @param key
	 * @param fields
	 */
	public void lpush(String redisName,String key,String... fields) {
		
		if(null == fields || 0 >= fields.length){
			return;
		}
		
		ShardedJedisPool shardPool = null;;
		ShardedJedis redis = null;
		try {
			shardPool = RedisBucket.getInstance().getShardPool(redisName);
			redis = shardPool.getResource();
			
			redis.lpush(key, fields);
			
		} catch (Exception e) {
			logger.info("redis 端口名" + redisName + " 数据库连接活跃数--<" + shardPool.getNumActive()
					+ ">--空闲连接数--<" + shardPool.getNumIdle() + ">--等待连接数--<" + shardPool.getNumWaiters() + ">");
			logger.error("lpush-Exception:", e);
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
	}
	
	/**
	 * lrange
	 * @param redisName
	 * @param key
	 * @param start
	 * @param end
	 */
	public List<String> lrange(String redisName,String key, Integer start, Integer end) {
		List<String> list = new ArrayList<>();
		ShardedJedisPool shardPool = null;;
		ShardedJedis redis = null;
		try {
			shardPool = RedisBucket.getInstance().getShardPool(redisName);
			redis = shardPool.getResource();
			
			list = redis.lrange(key, start, end);
			
		} catch (Exception e) {
			logger.info("redis 端口名" + redisName + " 数据库连接活跃数--<" + shardPool.getNumActive()
					+ ">--空闲连接数--<" + shardPool.getNumIdle() + ">--等待连接数--<" + shardPool.getNumWaiters() + ">");
			logger.error("lrange-Exception:", e);
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
		return list;
	}
	
	
	/**
	 * zrank
	 * @param redisName
	 * @param key
	 * @param member
	 */
	public Long zrank(String redisName, String key, String member) {
		Long str = null;
		ShardedJedisPool shardPool = null;;
		ShardedJedis redis = null;
		try {
			shardPool = RedisBucket.getInstance().getShardPool(redisName);
			redis = shardPool.getResource();
			str = redis.zrank(key, member);
			
		} catch (Exception e) {
			logger.info("redis 端口名" + redisName + " 数据库连接活跃数--<" + shardPool.getNumActive()
					+ ">--空闲连接数--<" + shardPool.getNumIdle() + ">--等待连接数--<" + shardPool.getNumWaiters() + ">");
			logger.error("zrank-Exception:", e);
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
		return str;
	}
}
