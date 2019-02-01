package com.hkzb.game.common.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;
import redis.clients.jedis.Tuple;

/**
 * 
 * 功能描述: redis链接管理对象，单例使用，放在初始化对象里
 * 
 */
public class RedisShardClient {

	private String addresses; // 中间用逗号分隔例如：127.0.0.1:6379,127.0.0.2:6379
	private String password; // 权限
	private List<JedisShardInfo> shards = new ArrayList<JedisShardInfo>();
	private GenericObjectPoolConfig poolConfig;
	ShardedJedisPool pool;

	/**
	 * 初始化方法，系统启动的时候调用
	 */
	public void init() {
		String[] host = addresses.split(",");
		for (String item : host) {
			String[] tmp = item.split(":");
			JedisShardInfo jedisShardInfo = new JedisShardInfo(tmp[0], Integer.parseInt(tmp[1]));
			jedisShardInfo.setPassword(password);
			shards.add(jedisShardInfo);
		}
		pool = new ShardedJedisPool(poolConfig, shards);
	}

	public void destory() {
		pool.destroy();
	}

	public Set<String> haveKey(String keyName) {
		ShardedJedis resource = null;
		try {
			resource = pool.getResource();
			return resource.hkeys(keyName);
		} catch (Exception e) {
			LogUtils.error(getClass(), e);
			throw new RuntimeException("");
		} finally {
			if (resource != null) {
				resource.close();
			}
		}
	}

	public Long srem(String key, String... members) {
		ShardedJedis resource = null;
		try {
			resource = pool.getResource();
			return resource.srem(key, members);
		} catch (Exception e) {
			LogUtils.error(getClass(), e);
			return null;
		} finally {
			if (resource != null) {
				resource.close();
			}
		}
	}

	public Set<String> smembers(String key) {
		ShardedJedis resource = null;
		try {
			resource = pool.getResource();
			return resource.smembers(key);
		} catch (Exception e) {
			LogUtils.error(getClass(), e);
			return null;
		} finally {
			if (resource != null) {
				resource.close();
			}
		}
	}

	public Long scard(String key) {
		ShardedJedis resource = null;
		try {
			resource = pool.getResource();
			return resource.scard(key);
		} catch (Exception e) {
			LogUtils.error(getClass(), e);
			return null;
		} finally {
			if (resource != null) {
				resource.close();
			}
		}
	}

	public Long sadd(String key, String... members) {
		ShardedJedis resource = null;
		try {
			resource = pool.getResource();
			return resource.sadd(key, members);
		} catch (Exception e) {
			LogUtils.error(getClass(), e);
			return null;
		} finally {
			if (resource != null) {
				resource.close();
			}
		}
	}
	
	public String spop(String key) {
		ShardedJedis resource = null;
		try {
			resource = pool.getResource();
			return resource.spop(key);
		} catch (Exception e) {
			LogUtils.error(getClass(), e);
			return null;
		} finally {
			if (resource != null) {
				resource.close();
			}
		}
	}

	// 查看value是否存在集合中
	public Boolean sismember(String key, String members) {
		ShardedJedis resource = null;
		try {
			resource = pool.getResource();
			return resource.sismember(key, members);
		} catch (Exception e) {
			LogUtils.error(getClass(), e);
			return false;
		} finally {
			if (resource != null) {
				resource.close();
			}
		}
	}

	public String get(String key) {
		ShardedJedis resource = null;
		try {
			resource = pool.getResource();
			return resource.get(key);
		} catch (Exception e) {
			LogUtils.error(getClass(), e);
			return null;
		} finally {
			if (resource != null) {
				resource.close();
			}
		}
	}

	public String set(String key, String value) {
		ShardedJedis resource = null;
		try {
			resource = pool.getResource();
			return resource.set(key, value);
		} catch (Exception e) {
			LogUtils.error(getClass(), e);
			return null;
		} finally {
			if (resource != null) {
				resource.close();
			}
		}
	}

	public Boolean exists(String key) {
		ShardedJedis resource = null;
		try {
			resource = pool.getResource();
			return resource.exists(key);
		} catch (Exception e) {
			LogUtils.error(getClass(), e);
			return false;
		} finally {
			if (resource != null) {
				resource.close();
			}
		}
	}

	public String setex(String key, int seconds, String value) {
		ShardedJedis resource = null;
		try {
			resource = pool.getResource();
			return resource.setex(key, seconds, value);
		} catch (Exception e) {
			LogUtils.error(getClass(), e);
			return null;
		} finally {
			if (resource != null) {
				resource.close();
			}
		}
	}

	public Long setnx(String key, String value) {
		ShardedJedis resource = null;
		try {
			resource = pool.getResource();
			return resource.setnx(key, value);
		} catch (Exception e) {
			LogUtils.error(getClass(), e);
			return null;
		} finally {
			if (resource != null) {
				resource.close();
			}
		}
	}

	public String getSet(String key, String value) {
		ShardedJedis resource = null;
		try {
			resource = pool.getResource();
			return resource.getSet(key, value);
		} catch (Exception e) {
			LogUtils.error(getClass(), e);
			return null;
		} finally {
			if (resource != null) {
				resource.close();
			}
		}
	}

	public Long del(String key) {
		ShardedJedis resource = null;
		try {
			resource = pool.getResource();
			return resource.del(key);
		} catch (Exception e) {
			LogUtils.error(getClass(), e);
			return null;
		} finally {
			if (resource != null) {
				resource.close();
			}
		}
	}

	public Long expire(String key, int seconds) {
		ShardedJedis resource = null;
		try {
			resource = pool.getResource();
			return resource.expire(key, seconds);
		} catch (Exception e) {
			LogUtils.error(getClass(), e);
			return null;
		} finally {
			if (resource != null) {
				resource.close();
			}
		}
	}

	public Long zrem(String key, String... members) {
		ShardedJedis resource = null;
		try {
			resource = pool.getResource();
			return resource.zrem(key, members);
		} catch (Exception e) {
			LogUtils.error(getClass(), e);
			return null;
		} finally {
			if (resource != null) {
				resource.close();
			}
		}
	}

	public Long incr(String key) {
		ShardedJedis resource = null;
		try {
			resource = pool.getResource();
			return resource.incr(key);
		} catch (Exception e) {
			LogUtils.error(getClass(), e);
			return null;
		} finally {
			if (resource != null) {
				resource.close();
			}
		}
	}

	public Long incr(String key, Long num) {
		ShardedJedis resource = null;
		try {
			resource = pool.getResource();
			return resource.incrBy(key, num);
		} catch (Exception e) {
			LogUtils.error(getClass(), e);
			return null;
		} finally {
			if (resource != null) {
				resource.close();
			}
		}
	}

	public Long decr(String key) {
		ShardedJedis resource = null;
		try {
			resource = pool.getResource();
			return resource.decr(key);
		} catch (Exception e) {
			LogUtils.error(getClass(), e);
			return null;
		} finally {
			if (resource != null) {
				resource.close();
			}
		}
	}

	public Long zadd(String key, String member) {
		ShardedJedis resource = null;
		try {
			resource = pool.getResource();
			return resource.zadd(key, 1, member);
		} catch (Exception e) {
			LogUtils.error(getClass(), e);
			return null;
		} finally {
			if (resource != null) {
				resource.close();
			}
		}
	}

	public Long zadd(String key, double score, String member) {
		ShardedJedis resource = null;
		try {
			resource = pool.getResource();
			return resource.zadd(key, score, member);
		} catch (Exception e) {
			LogUtils.error(getClass(), e);
			return null;
		} finally {
			if (resource != null) {
				resource.close();
			}
		}
	}

	public Double zincrby(String key, double score, String member) {
		ShardedJedis resource = null;
		try {
			resource = pool.getResource();
			return resource.zincrby(key, score, member);
		} catch (Exception e) {
			LogUtils.error(getClass(), e);
			return null;
		} finally {
			if (resource != null) {
				resource.close();
			}
		}
	}

	public Long zcard(String key) {
		ShardedJedis resource = null;
		try {
			resource = pool.getResource();
			return resource.zcard(key);
		} catch (Exception e) {
			LogUtils.error(getClass(), e);
			return null;
		} finally {
			if (resource != null) {
				resource.close();
			}
		}
	}

	public Set<String> zrange(String key, long start, long end) {
		ShardedJedis resource = null;
		try {
			resource = pool.getResource();
			return resource.zrange(key, start, end);
		} catch (Exception e) {
			LogUtils.error(getClass(), e);
			return null;
		} finally {
			if (resource != null) {
				resource.close();
			}
		}
	}
	
	public Set<Tuple> zrangeWithScores(String key, long start, long end) {
		ShardedJedis resource = null;
		try {
			resource = pool.getResource();
			return resource.zrangeWithScores(key, start, end);
		} catch (Exception e) {
			LogUtils.error(getClass(), e);
			return null;
		} finally {
			if (resource != null) {
				resource.close();
			}
		}
	}

	public Set<String> zrevrange(String key, long start, long end) {
		ShardedJedis resource = null;
		try {
			resource = pool.getResource();
			return resource.zrevrange(key, start, end);
		} catch (Exception e) {
			LogUtils.error(getClass(), e);
			return null;
		} finally {
			if (resource != null) {
				resource.close();
			}
		}
	}

	public Set<String> zrevrangebyscore(String key) {
		ShardedJedis resource = null;
		try {
			resource = pool.getResource();
			return resource.zrevrangeByScore(key, "+inf", "-inf");
		} catch (Exception e) {
			LogUtils.error(getClass(), e);
			return null;
		} finally {
			if (resource != null) {
				resource.close();
			}
		}
	}

	public Set<String> zrevrangebyscore(String key, int offset, int count) {
		ShardedJedis resource = null;
		try {
			resource = pool.getResource();
			return resource.zrevrangeByScore(key, "+inf", "-inf", offset, count);
		} catch (Exception e) {
			LogUtils.error(getClass(), e);
			return null;
		} finally {
			if (resource != null) {
				resource.close();
			}
		}
	}

	public Long llen(String key) {
		ShardedJedis resource = null;
		try {
			resource = pool.getResource();
			return resource.llen(key);
		} catch (Exception e) {
			LogUtils.error(getClass(), e);
			return null;
		} finally {
			if (resource != null) {
				resource.close();
			}
		}
	}

	public Long lpush(String key, String msg) {
		ShardedJedis resource = null;
		try {
			resource = pool.getResource();
			return resource.lpush(key, msg);
		} catch (Exception e) {
			LogUtils.error(getClass(), e);
			return null;
		} finally {
			if (resource != null) {
				resource.close();
			}
		}
	}
	
	public Long rpush(String key, String msg) {
		ShardedJedis resource = null;
		try {
			resource = pool.getResource();
			return resource.rpush(key, msg);
		} catch (Exception e) {
			LogUtils.error(getClass(), e);
			return null;
		} finally {
			if (resource != null) {
				resource.close();
			}
		}
	}

	public Long lpush(String channel, String key, String msg) {
		ShardedJedis resource = null;
		try {
			resource = pool.getResource();
			return resource.lpush(channel, key, msg);
		} catch (Exception e) {
			LogUtils.error(getClass(), e);
			return null;
		} finally {
			if (resource != null) {
				resource.close();
			}
		}
	}
	
	public List<String> brpop(String key) {
		ShardedJedis resource = null;
		try {
			resource = pool.getResource();
			return resource.brpop(key);
		} catch (Exception e) {
			LogUtils.error(getClass(), e);
			return null;
		} finally {
			if (resource != null) {
				resource.close();
			}
		}
	}

	public List<String> brpop(int timeout, String key) {
		ShardedJedis resource = null;
		try {
			resource = pool.getResource();
			return resource.brpop(timeout, key);
		} catch (Exception e) {
			LogUtils.error(getClass(), e);
			return null;
		} finally {
			if (resource != null) {
				resource.close();
			}
		}
	}

	public List<String> lrange(String key, long start, long end) {
		ShardedJedis resource = null;
		try {
			resource = pool.getResource();
			return resource.lrange(key, start, end);
		} catch (Exception e) {
			LogUtils.error(getClass(), e);
			return null;
		} finally {
			if (resource != null) {
				resource.close();
			}
		}
	}
	
	public String lindex(String key, long index) {
		ShardedJedis resource = null;
		try {
			resource = pool.getResource();
			return resource.lindex(key, index);
		} catch (Exception e) {
			LogUtils.error(getClass(), e);
			return null;
		} finally {
			if (resource != null) {
				resource.close();
			}
		}
	}

	public Long lrem(String key, long count, String value) {
		ShardedJedis resource = null;
		try {
			resource = pool.getResource();
			return resource.lrem(key, count, value);
		} catch (Exception e) {
			LogUtils.error(getClass(), e);
			return null;
		} finally {
			if (resource != null) {
				resource.close();
			}
		}
	}

	public List<String> blpop(int timeout, String key) {
		ShardedJedis resource = null;
		try {
			resource = pool.getResource();
			return resource.blpop(timeout, key);
		} catch (Exception e) {
			LogUtils.error(getClass(), e);
			return null;
		} finally {
			if (resource != null) {
				resource.close();
			}
		}
	}
	
	public String lset(String key,long index,String value) {
		ShardedJedis resource = null;
		try {
			resource = pool.getResource();
			return resource.lset(key, index, value);
		} catch (Exception e) {
			LogUtils.error(getClass(), e);
			return null;
		} finally {
			if (resource != null) {
				resource.close();
			}
		}
	}
	
	public Long getLength(String key) {
		ShardedJedis resource = null;
		try {
			resource = pool.getResource();
			return resource.llen(key);
		} catch (Exception e) {
			LogUtils.error(getClass(), e);
			return null;
		} finally {
			if (resource != null) {
				resource.close();
			}
		}
	}

	public Long hset(String key, String field, String value) {
		ShardedJedis resource = null;
		try {
			resource = pool.getResource();
			return resource.hset(key, field, value);
		} catch (Exception e) {
			LogUtils.error(getClass(), e);
			return null;
		} finally {
			if (resource != null) {
				resource.close();
			}
		}
	}

	public String hget(String key, String field) {
		ShardedJedis resource = null;
		try {
			resource = pool.getResource();
			return resource.hget(key, field);
		} catch (Exception e) {
			LogUtils.error(getClass(), e);
			return null;
		} finally {
			if (resource != null) {
				resource.close();
			}
		}
	}
	public Map<String, String> hgetAll(String key) {
		ShardedJedis resource = null;
		try {
			resource = pool.getResource();
			return resource.hgetAll(key);
		} catch (Exception e) {
			LogUtils.error(getClass(), e);
			return null;
		} finally {
			if (resource != null) {
				resource.close();
			}
		}
	}
	
	public Long hdel(String key, String field) {
		ShardedJedis resource = null;
		try {
			resource = pool.getResource();
			return resource.hdel(key, field);
		} catch (Exception e) {
			LogUtils.error(getClass(), e);
			return null;
		} finally {
			if (resource != null) {
				resource.close();
			}
		}
	}
	
	public List<String> blpop(String key) {
		ShardedJedis resource = null;
		try {
			resource = pool.getResource();
			return resource.blpop(key);
		} catch (Exception e) {
			LogUtils.error(getClass(), e);
			return null;
		} finally {
			if (resource != null) {
				resource.close();
			}
		}
	}
	
	public Boolean hexists(String key,String field) {
		ShardedJedis resource = null;
		try {
			resource = pool.getResource();
			return resource.hexists(key, field);
		} catch (Exception e) {
			LogUtils.error(getClass(), e);
			return null;
		} finally {
			if (resource != null) {
				resource.close();
			}
		}
	}
	
	/**
	 * 获取一个Redis链接资源 (注意：需要关闭)
	 * 
	 * @return
	 */
	public ShardedJedis getResource() {
		try {
			return pool.getResource();
		} catch (Exception e) {
			LogUtils.error(getClass(), e);
			return null;
		}
	}

	/**
	 * 关闭链接资源
	 * 
	 * @param resource
	 */
	public void closeResource(ShardedJedis resource) {
		try {
			if (resource != null) {
				resource.close();
			}
		} catch (Exception e) {
			LogUtils.error(getClass(), e);
		}
	}

	public String getAddresses() {
		return addresses;
	}

	public void setAddresses(String addresses) {
		this.addresses = addresses;
	}

	public GenericObjectPoolConfig getPoolConfig() {
		return poolConfig;
	}

	public void setPoolConfig(GenericObjectPoolConfig poolConfig) {
		this.poolConfig = poolConfig;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
