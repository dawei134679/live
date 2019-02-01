package com.tinypig.admin.redis.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tinypig.admin.redis.core.RedisBucket;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Tuple;

public class RedisOperat {

	private static final Logger logger = LoggerFactory.getLogger(RedisOperat.class.getSimpleName());


	private final static RedisOperat instance = new RedisOperat();

	public static RedisOperat getInstance() {
		return instance;
	}

	/**
	 * set设置
	 * 
	 * @param host
	 * @param port
	 * @param key
	 * @param value
	 * @return null 为不成功
	 */
	public String set(String host, int port, String key, String value) {
		Jedis redis = RedisBucket.getInstance().initialize(host, port);
		String set = null;
		try {
			if (redis != null) {
				set = redis.set(key, value);
			} else {
				logger.info("redis is null, Host:" + host + " 端口名:" + port + " key:" + key + " value:" + value + ">");
			}
		} catch (Exception e) {

			logger.error("redis Host:" + host + " 端口名:" + port + " key:" + key + " value:" + value + ">");
			logger.error("RedisOperat-set:", e);
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
		return set;
	}
	
	public String set(String host, int port, String key, String value,int seconds) {
		Jedis redis = RedisBucket.getInstance().initialize(host, port);
		String set = null;
		try {
			if (redis != null) {
				set = redis.setex(key, seconds, value);
			} else {
				logger.info("redis is null, Host:" + host + " 端口名:" + port + " key:" + key + " value:" + value + ">");
			}
		} catch (Exception e) {

			logger.error("redis Host:" + host + " 端口名:" + port + " key:" + key + " value:" + value + ">");
			logger.error("RedisOperat-set:", e);
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
		return set;
	}

	/**
	 * 获取set的值
	 * 
	 * @param host
	 * @param port
	 * @param key
	 * @return null 说明没有值
	 */
	public String get(String host, int port, String key) {

		Jedis redis = RedisBucket.getInstance().initialize(host, port);
		String set = null;
		try {
			if (redis != null) {
				set = redis.get(key);
			} else {
				logger.info("redis is null, Host:" + host + " 端口名:" + port + " key:" + key + ">");
			}
		} catch (Exception e) {

			logger.error("redis Host:" + host + " 端口名:" + port + " key:" + key + ">");
			logger.error("RedisOperat-get:", e);
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
		return set;
	}

	/**
	 * 删除del
	 * 
	 * @param host
	 * @param port
	 * @param key
	 * @return null 说明没有值
	 */
	public Long del(String host, int port, String key) {

		Jedis redis = RedisBucket.getInstance().initialize(host, port);
		Long del = null;
		try {
			if (redis != null) {
				 del = redis.del(key);
			} else {
				logger.info("redis is null, Host:" + host + " 端口名:" + port + " key:" + key + ">");
			}
		} catch (Exception e) {

			logger.error("redis Host:" + host + " 端口名:" + port + " key:" + key + ">");
			logger.error("RedisOperat-del:", e);
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
		return del;
	}

	/**
	 * hset 设置
	 * 
	 * @param host
	 * @param port
	 * @param key
	 * @param field
	 * @param value
	 * @return
	 */
	public Long hset(String host, int port, String key, String field, String value,int seconds) {

		Jedis redis = RedisBucket.getInstance().initialize(host, port);
		Long hset = null;
		try {
			if (redis != null) {
				hset = redis.hset(key, field, value);
				
				if (seconds > 0 && hset == 1) {
					redis.expire(key, seconds);
				}
			} else {
				logger.info("redis is null, Host:" + host + " 端口名:" + port + " key:" + key + " field:" + field
						+ " value:" + value + ">");
			}
		} catch (Exception e) {

			logger.error("redis Host:" + host + " 端口名:" + port + " key:" + key + " field:" + field + " value:" + value
					+ ">");
			logger.error("RedisOperat-hset:", e);
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
		return hset;
	}
	
	public Integer hlen(String host, int port, String key){

		Jedis redis = RedisBucket.getInstance().initialize(host, port);
		Long hlen = 0L;
		try {
			if (redis != null) {
				hlen = redis.hlen(key);
			} else {
				logger.info("redis is null, Host:" + host + " 端口名:" + port + " key:" + key + ">");
			}
		} catch (Exception e) {
			logger.error("redis Host:" + host + " 端口名:" + port + " key:" + key + ">");
			logger.error("RedisOperat-hget:", e);
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
		return hlen.intValue();
	}

	/**
	 * hset 获取
	 * 
	 * @param host
	 * @param port
	 * @param key
	 * @param field
	 * @param value
	 * @return
	 */
	public String hget(String host, int port, String key, String field) {

		Jedis redis = RedisBucket.getInstance().initialize(host, port);
		String hget = null;
		try {
			if (redis != null) {
				hget = redis.hget(key, field);
			} else {
				logger.info("redis is null, Host:" + host + " 端口名:" + port + " key:" + key + " field:" + field + ">");
			}
		} catch (Exception e) {

			logger.error("redis Host:" + host + " 端口名:" + port + " key:" + key + " field:" + field + ">");
			logger.error("RedisOperat-hget:", e);
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
		return hget;
	}

	/**
	 * hset 获取
	 * 
	 * @param host
	 * @param port
	 * @param key
	 * @param field
	 * @param value
	 * @return
	 */
	public Map<String, String> hgetAll(String host, int port, String key) {

		Jedis redis = RedisBucket.getInstance().initialize(host, port);
		Map<String, String> hgetAll = new HashMap<String, String>();
		try {
			if (redis != null) {
				hgetAll = redis.hgetAll(key);
			} else {
				logger.info("hgetAll redis is null, Host:" + host + " 端口名:" + port + " key:" + key + ">");
			}
		} catch (Exception e) {

			logger.error("redis Host:" + host + " 端口名:" + port + " key:" + key + ">");
			logger.error("RedisOperat-hgetAll:", e);
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
		return hgetAll;
	}
	/**
	 * 获取缓存用户信息 集合
	 * 
	 * @param uids
	 * @return
	 */
	public List<String> hmget(String host, int port,String key,String... fields) {
		
		List<String> hmget = null;
		Jedis redis = null;
		
		try {

			redis = RedisBucket.getInstance().initialize(host, port);
			hmget = redis.hmget(key, fields);

		} catch (Exception e) {
			logger.error("redis Host:" + host + " 端口名:" + port + " key:" + key + " field:" + fields + ">");
			logger.error("RedisOperat-hmget:", e);
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
		return hmget;
	}

	/**
	 * hset 获取
	 * 
	 * @param host
	 * @param port
	 * @param key
	 * @param field
	 * @param value
	 * @return
	 */
	public Long hdel(String host, int port, String key, String field) {

		Jedis redis = RedisBucket.getInstance().initialize(host, port);
		Long hdel = null;
		try {
			if (redis != null) {
				hdel = redis.hdel(key, field);
			} else {
				logger.info("redis is null, Host:" + host + " 端口名:" + port + " key:" + key + " field:" + field + ">");
			}
		} catch (Exception e) {

			logger.error("redis Host:" + host + " 端口名:" + port + " key:" + key + " field:" + field + ">");
			logger.error("RedisOperat-hdel:", e);
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
		return hdel;
	}
	
	public Long zadd(String host, int port,String key,double score,String member){

		Jedis redis = RedisBucket.getInstance().initialize(host, port);
		Long zadd = null;
		try {
			if (redis != null) {
				zadd = redis.zadd(key, score, member);
			} else {
				logger.info("redis is null, Host:" + host + " 端口名:" + port + " key:" + key+" score:"+score + " member:" + member + ">");
			}
		} catch (Exception e) {
			logger.error("redis Host:" + host + " 端口名:" + port + " key:" + key +" score:"+score + " member:" + member + ">");
			logger.error("RedisOperat-zadd:", e);
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
		return zadd;
	}
	
	public Long zcard(String host, int port,String key){

		Jedis redis = RedisBucket.getInstance().initialize(host, port);
		Long zcard = 0L;
		try {
			if (redis != null) {
				zcard = redis.zcard(key);
			} else {
				logger.info("redis is null, Host:" + host + " 端口名:" + port + " key:" + key + ">");
			}
		} catch (Exception e) {
			logger.error("redis Host:" + host + " 端口名:" + port + " key:" + key + ">");
			logger.error("RedisOperat-zcard:", e);
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
		return zcard;
	}
	
	public Long zrem(String host, int port,String key,String... members){

		Jedis redis = RedisBucket.getInstance().initialize(host, port);
		Long zadd = null;
		try {
			if (redis != null) {
				zadd = redis.zrem(key, members);
			} else {
				logger.info("redis is null, Host:" + host + " 端口名:" + port + " key:" + key+ " member:" + members.toString() + ">");
			}
		} catch (Exception e) {
			logger.error("redis Host:" + host + " 端口名:" + port + " key:" + key + " member:" + members.toString() + ">");
			logger.error("RedisOperat-zadd:", e);
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
		return zadd;
	}
	
	public Set<Tuple> zrevrangeWithScores(String host,int port,String key,int start,int end){
		Jedis redis = RedisBucket.getInstance().initialize(host, port);
		Set<Tuple> zrevrangeWithScores = null;
		try {
			if (redis != null) {
				zrevrangeWithScores = redis.zrevrangeWithScores(key, start, end);
			} else {
				logger.info("redis is null, Host:" + host + " 端口名:" + port + " key:" + key+" start:"+start + " end:" + end + ">");
			}
		} catch (Exception e) {
			logger.info("redis is null, Host:" + host + " 端口名:" + port + " key:" + key+" start:"+start + " end:" + end + ">");
			logger.error("RedisOperat-zrevrangeWithScores:", e);
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
		return zrevrangeWithScores;
	}
	
	public void zincrby(String host,int port,String key,double score,String member){
		Jedis redis = RedisBucket.getInstance().initialize(host, port);
		try {
			if (redis != null) {
				redis.zincrby(key, score, member);
			} else {
				logger.info("redis is null, Host:" + host + " 端口名:" + port + " key:" + key+" score:"+score + " member:" + member + ">");
			}
		} catch (Exception e) {
			logger.info("redis is null, Host:" + host + " 端口名:" + port + " key:" + key+" score:"+score + " member:" + member + ">");
			logger.error("zincrby-zincrby:", e);
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
	}
	
	public Long incrBy(String host,int port,String key,int integer,int seconds){
		
		Jedis redis = RedisBucket.getInstance().initialize(host, port);
		Long incrBy = 0L;
		try {
			if (redis != null) {
				incrBy = redis.incrBy(key, integer);
			}else {
				logger.info("redis is null, Host:" + host + " 端口名:" + port + " key:" + key+" integer:"+integer + " seconds:" + seconds + ">");
			}
			
			if (seconds > 0 && incrBy == integer) {
				redis.expire(key, seconds);
			}
			
		} catch (Exception e) {
			logger.info("redis is null, Host:" + host + " 端口名:" + port + " key:" + key+" integer:"+integer + " seconds:" + seconds + ">");
			logger.error("zincrby-incrBy:", e);
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
		return incrBy;
	}
	
	public void rpush(String host,int port,String key,String... value){
		
		Jedis redis = RedisBucket.getInstance().initialize(host, port);
		try {
			if (redis != null) {
				redis.rpush(key, value);
			} else {
				logger.info("redis is null, Host:" + host + " 端口名:" + port + " key:" + key+" value:"+value + ">");
			}
		} catch (Exception e) {
			logger.info("redis is null, Host:" + host + " 端口名:" + port + " key:" + key+" value:"+value + ">");
			logger.error("rpush-Exception:", e);
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
	}
	
	public String lpop(String host,int port,String key){
		Jedis redis = RedisBucket.getInstance().initialize(host, port);
		String lpop = null;
		try {
			if (redis != null) {
				lpop = redis.lpop(key);
			} else {
				logger.info("redis is null, Host:" + host + " 端口名:" + port + " key:" + key + ">");
			}
		} catch (Exception e) {
			logger.info("redis is null, Host:" + host + " 端口名:" + port + " key:" + key + ">");
			logger.error("lpop-Exception:", e);
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
		return lpop;
	}
	
	public Double zscore(String host, int port,String key,String member){

		Jedis redis = RedisBucket.getInstance().initialize(host, port);
		Double score = null;
		try {
			if (redis != null) {
				score = redis.zscore(key, member);
			} else {
				logger.info("redis is null, Host:" + host + " 端口名:" + port + " key:" + key + " member:" + member + ">");
			}
		} catch (Exception e) {
			logger.error("redis Host:" + host + " 端口名:" + port + " key:" + key + " member:" + member + ">");
			logger.error("RedisOperat-zscore:", e);
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
		return score;
	}
	/**
	 * 获取sadd的值
	 * 
	 * @param host
	 * @param port
	 * @param key
	 * @return null 说明没有值
	 */
	public Long sadd(String host, int port, String key, String members) {

		Jedis redis = RedisBucket.getInstance().initialize(host, port);
		Long set = null;
		try {
			if (redis != null) {
				set = redis.sadd(key, members);
			} else {
				logger.info("redis is null, Host:" + host + " 端口名:" + port + " key:" + key + ">");
			}
		} catch (Exception e) {

			logger.error("redis Host:" + host + " 端口名:" + port + " key:" + key + ">");
			logger.error("RedisOperat-sadd:", e);
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
		return set;
	}
	/**
	 * 删除sadd的值
	 * 
	 * @param host
	 * @param port
	 * @param key
	 * @return null 说明没有值
	 */
	public Long srem(String host, int port, String key, String members) {

		Jedis redis = RedisBucket.getInstance().initialize(host, port);
		Long set = null;
		try {
			if (redis != null) {
				set = redis.srem(key, members);
			} else {
				logger.info("redis is null, Host:" + host + " 端口名:" + port + " key:" + key + ">");
			}
		} catch (Exception e) {
			logger.error("redis Host:" + host + " 端口名:" + port + " key:" + key + ">");
			logger.error("RedisOperat-srem:", e);
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
		return set;
	}
	
	/**
	 * 随机获取
	 * @param host
	 * @param port
	 * @param key
	 * @param count
	 */
	public List<String> srandmember(String host, int port, String key, int count) {
		Jedis redis = RedisBucket.getInstance().initialize(host, port);
		List<String> list = null;
		try {
			if (redis != null) {
				list = redis.srandmember(key, count);
			} else {
				logger.info("redis is null, Host:" + host + " 端口名:" + port + " key:" + key + ">");
			}
		} catch (Exception e) {
			logger.error("redis Host:" + host + " 端口名:" + port + " key:" + key + ">");
			logger.error("RedisOperat-srandmember:", e);
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
		return list;
	}
}
