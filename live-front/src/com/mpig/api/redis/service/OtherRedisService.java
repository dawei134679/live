package com.mpig.api.redis.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mpig.api.dictionary.lib.TaskConfigLib;
import com.mpig.api.dictionary.lib.TaskConfigLib.TaskFor;
import com.mpig.api.perf.PerfFilter;
import com.mpig.api.perf.vo.CostRequestLog;
import com.mpig.api.redis.core.RedisBucket;
import com.mpig.api.utils.BaseContant;
import com.mpig.api.utils.DateUtils;
import com.mpig.api.utils.RedisContant;

import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;
import redis.clients.jedis.Tuple;

/**
 * 缓存其他的
 *
 * @author fang
 */
public class OtherRedisService {
	private static Logger logger = Logger.getLogger("redislog");

	private static final Long pageSiz = 20L;
	private final static OtherRedisService instance = new OtherRedisService();

	public static OtherRedisService getInstance() {
		return instance;
	}

	/**
	 * 记录主播开播后心跳时间
	 *
	 * @param uid
	 * @param micid
	 *            麦时表ID
	 * @param times
	 *            心跳时间
	 */
	public void addRoomEndTime(Integer uid, int micid, Long times) {
		ShardedJedisPool shardPool = RedisBucket.getInstance().getShardPool(RedisContant.RedisNameOther);
		ShardedJedis redis = null;
		try {
			redis = shardPool.getResource();
			redis.hset(RedisContant.keyStartLive, uid.toString(), micid + "," + times);
		} catch (Exception e) {
			logger.info("redis 端口名" + RedisContant.RedisNameOther + " 数据库连接活跃数--<" + shardPool.getNumActive()
					+ ">--空闲连接数--<" + shardPool.getNumIdle() + ">--等待连接数--<" + shardPool.getNumWaiters() + ">");
			logger.error("<delRoomLikes->Exception>" + e.toString());
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
	}

	/**
	 * 获取主播最后一次心跳时间
	 *
	 * @param uid
	 * @return
	 */
	public String getRoomEndTime(Integer uid) {
		ShardedJedisPool shardPool = RedisBucket.getInstance().getShardPool(RedisContant.RedisNameOther);
		ShardedJedis redis = null;
		try {
			redis = shardPool.getResource();
			return redis.hget(RedisContant.keyStartLive, uid.toString());
		} catch (Exception e) {
			logger.info(
					"getRoomEndTime redis 端口名" + RedisContant.RedisNameOther + " 数据库连接活跃数--<" + shardPool.getNumActive()
							+ ">--空闲连接数--<" + shardPool.getNumIdle() + ">--等待连接数--<" + shardPool.getNumWaiters() + ">");
			logger.error("<getRoomEndTime->Exception>" + e.toString());
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
		return null;
	}

	/**
	 * 删除本场心跳记录
	 *
	 * @param uid
	 * @return
	 */
	public Long delRoomEndTime(Integer uid) {
		ShardedJedisPool shardPool = RedisBucket.getInstance().getShardPool(RedisContant.RedisNameOther);
		ShardedJedis redis = null;
		try {
			redis = shardPool.getResource();
			return redis.hdel(RedisContant.keyStartLive, uid.toString());
		} catch (Exception e) {
			logger.info("redis 端口名" + RedisContant.RedisNameOther + " 数据库连接活跃数--<" + shardPool.getNumActive()
					+ ">--空闲连接数--<" + shardPool.getNumIdle() + ">--等待连接数--<" + shardPool.getNumWaiters() + ">");
			logger.error("<delRoomEndTime->Exception>" + e.toString());
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
		return null;

	}

	/**
	 * 开播推荐列表
	 *
	 * @param uid
	 *            主播uid
	 * @param recommend
	 *            =0 不推荐 =1推荐
	 * @param rq
	 *            人气值 7位数，不足左边补0
	 * @param type
	 *            = 0 新增 =1 是人气修正
	 */
	public void addRecommendRoom(int uid, int recommend, int rq, int type) {

		ShardedJedisPool shardPool = RedisBucket.getInstance().getShardPool(RedisContant.RedisNameOther);
		ShardedJedis redis = null;

		try {
			redis = shardPool.getResource();
			Long zadd = redis.zadd(RedisContant.KeyRoomRecommend, Double.valueOf(rq), String.valueOf(uid));
			if (type == 1) {
				if (zadd == 1) {
					redis.zrem(RedisContant.KeyRoomRecommend, String.valueOf(uid));
				}
			}
		} catch (Exception e) {
			logger.info("redis 端口名" + RedisContant.RedisNameOther + " 数据库连接活跃数--<" + shardPool.getNumActive()
					+ ">--空闲连接数--<" + shardPool.getNumIdle() + ">--等待连接数--<" + shardPool.getNumWaiters() + ">");
			logger.error("<addRecommendRoom->Exception>" + e.toString());
		} finally {
			if (redis != null) {
				redis.close();
			}
		}

	}

	/**
	 * 增加最新入驻开播列表
	 * @param uid
	 * @param rq
	 */
	public void addNewJoinRoom(int uid, int rq) {

		ShardedJedisPool shardPool = RedisBucket.getInstance().getShardPool(RedisContant.RedisNameOther);
		ShardedJedis redis = null;

		try {
			redis = shardPool.getResource();
			redis.zadd(RedisContant.keyRoomNewJoin, Double.valueOf(rq), String.valueOf(uid));
		} catch (Exception e) {
			logger.info("redis 端口名" + RedisContant.RedisNameOther + " 数据库连接活跃数--<" + shardPool.getNumActive()
					+ ">--空闲连接数--<" + shardPool.getNumIdle() + ">--等待连接数--<" + shardPool.getNumWaiters() + ">");
			logger.error("<addNewJoinRoom->Exception>" + e.toString());
		} finally {
			if (redis != null) {
				redis.close();
			}
		}

	}

	/**
	 * 获取开播推荐列表
	 *
	 * @param page
	 * @return
	 */
	public Set<String> getRecommendRoom(int page) {
		ShardedJedisPool shardPool = RedisBucket.getInstance().getShardPool(RedisContant.RedisNameOther);
		ShardedJedis redis = null;
		try {
			redis = shardPool.getResource();
			if (page > 0) {
				return redis.zrevrange(RedisContant.KeyRoomRecommend, (page - 1) * pageSiz, page * pageSiz - 1);
			} else {
				return redis.zrevrange(RedisContant.KeyRoomRecommend, 0, -1);
			}
		} catch (Exception e) {
			logger.info("redis 端口名" + RedisContant.RedisNameOther + " 数据库连接活跃数--<" + shardPool.getNumActive()
					+ ">--空闲连接数--<" + shardPool.getNumIdle() + ">--等待连接数--<" + shardPool.getNumWaiters() + ">");
			logger.error("<getRecommendRoom->Exception>" + e.toString());
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
		return null;
	}

	/**
	 * 获取推荐列表某个主播的人数
	 *
	 * @param page
	 * @return
	 */
	public int getRecommendRoomUserCounts(Integer anchorId) {
		ShardedJedisPool shardPool = RedisBucket.getInstance().getShardPool(RedisContant.RedisNameOther);
		ShardedJedis redis = null;
		try {
			redis = shardPool.getResource();
			Double zscore = redis.zscore(RedisContant.KeyRoomRecommend, anchorId.toString());
			int count = 1;
			if (zscore != null) {
				count = zscore.intValue();
			}
			return count;
		} catch (Exception e) {
			logger.info("redis 端口名" + RedisContant.RedisNameOther + " 数据库连接活跃数--<" + shardPool.getNumActive()
					+ ">--空闲连接数--<" + shardPool.getNumIdle() + ">--等待连接数--<" + shardPool.getNumWaiters() + ">");
			logger.error("<getRecommendRoomUserCounts->Exception>" + e.toString());
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
		return 0;
	}

	/**
	 * 获取某个主播的人数
	 *
	 * @param page
	 * @return
	 */
	public int getRoomUserCounts(Integer anchorId) {
		ShardedJedisPool shardPool = RedisBucket.getInstance().getShardPool(RedisContant.RedisNameOther);
		ShardedJedis redis = null;
		try {
			redis = shardPool.getResource();
			Double zscore = redis.zscore(RedisContant.KeyRoomShowUserCounts, anchorId.toString());
			int count = 0;
			if (zscore != null) {
				count = zscore.intValue();
			}
			return count;
		} catch (Exception e) {
			logger.info("redis 端口名" + RedisContant.RedisNameOther + " 数据库连接活跃数--<" + shardPool.getNumActive()
					+ ">--空闲连接数--<" + shardPool.getNumIdle() + ">--等待连接数--<" + shardPool.getNumWaiters() + ">");
			logger.error("<getRoomUserCounts->Exception>" + e.toString());
			return 0;
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
	}

	/**
	 * 设置某个主播的人数
	 *
	 * @param page
	 * @return
	 */
	public int setRoomUserCounts(Integer anchorId, Double score) {
		ShardedJedisPool shardPool = RedisBucket.getInstance().getShardPool(RedisContant.RedisNameOther);
		ShardedJedis redis = null;
		try {
			redis = shardPool.getResource();
			Long zscore = redis.zadd(RedisContant.KeyRoomShowUserCounts, score, anchorId.toString());
			return zscore.intValue();
		} catch (Exception e) {
			logger.info("redis 端口名" + RedisContant.RedisNameOther + " 数据库连接活跃数--<" + shardPool.getNumActive()
					+ ">--空闲连接数--<" + shardPool.getNumIdle() + ">--等待连接数--<" + shardPool.getNumWaiters() + ">");
			logger.error("<setRoomUserCounts->Exception>" + e.toString());
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
		return 0;
	}

	/**
	 * 删除某个主播的人数
	 *
	 * @param page
	 * @return
	 */
	public Long delRoomUserCounts(Integer anchorId) {
		ShardedJedisPool shardPool = RedisBucket.getInstance().getShardPool(RedisContant.RedisNameOther);
		ShardedJedis redis = null;
		try {
			redis = shardPool.getResource();
			return redis.zrem(RedisContant.KeyRoomShowUserCounts, anchorId.toString());
			
		} catch (Exception e) {
			logger.info("redis 端口名" + RedisContant.RedisNameOther + " 数据库连接活跃数--<" + shardPool.getNumActive()
					+ ">--空闲连接数--<" + shardPool.getNumIdle() + ">--等待连接数--<" + shardPool.getNumWaiters() + ">");
			logger.error("<delRoomUserCounts->Exception>" + e.toString());
			return null;
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
	}

	/**
	 * 获取最新入驻列表
	 *
	 * @param page
	 * @return
	 */
	public Set<String> getNewJoinRoom(int page) {
		ShardedJedisPool shardPool = RedisBucket.getInstance().getShardPool(RedisContant.RedisNameOther);
		ShardedJedis redis = null;
		try {
			redis = shardPool.getResource();
			if (page > 0) {
				return redis.zrevrange(RedisContant.keyRoomNewJoin, (page - 1) * pageSiz, page * pageSiz - 1);
			} else {
				return redis.zrevrange(RedisContant.keyRoomNewJoin, 0, BaseContant.newJoinSize - 1);
			}
		} catch (Exception e) {
			logger.info("redis 端口名" + RedisContant.RedisNameOther + " 数据库连接活跃数--<" + shardPool.getNumActive()
					+ ">--空闲连接数--<" + shardPool.getNumIdle() + ">--等待连接数--<" + shardPool.getNumWaiters() + ">");
			logger.error("<getNewJoinRoom->Exception>" + e.toString());
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
		return null;
	}

	/**
	 * 获取最新入驻列表中其中的一个用户
	 * 
	 * @param uid
	 * @return
	 */
	public Double getNewJoinRoomInfo(int uid) {
		ShardedJedisPool shardPool = RedisBucket.getInstance().getShardPool(RedisContant.RedisNameOther);
		ShardedJedis redis = null;
		Double dbl = null;
		try {
			redis = shardPool.getResource();
			dbl = redis.zscore(RedisContant.keyRoomNewJoin, String.valueOf(uid));
		} catch (Exception e) {
			logger.info("redis 端口名" + RedisContant.RedisNameOther + " 数据库连接活跃数--<" + shardPool.getNumActive()
					+ ">--空闲连接数--<" + shardPool.getNumIdle() + ">--等待连接数--<" + shardPool.getNumWaiters() + ">");
			logger.error("<getRecommendRoomInfo->Exception>" + e.toString());
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
		return dbl;
	}

	public Set<String> getAllNewJoinRoom(int page) {
		ShardedJedisPool shardPool = RedisBucket.getInstance().getShardPool(RedisContant.RedisNameOther);
		ShardedJedis redis = null;
		try {
			redis = shardPool.getResource();
			if (page > 0) {
				return redis.zrevrange(RedisContant.keyRoomNewJoinAll, (page - 1) * pageSiz, page * pageSiz - 1);
			} else {
				return redis.zrevrange(RedisContant.keyRoomNewJoinAll, 0, BaseContant.newJoinSize - 1);
			}
		} catch (Exception e) {
			logger.info("redis 端口名" + RedisContant.RedisNameOther + " 数据库连接活跃数--<" + shardPool.getNumActive()
					+ ">--空闲连接数--<" + shardPool.getNumIdle() + ">--等待连接数--<" + shardPool.getNumWaiters() + ">");
			logger.error("<keyRoomNewJoinAll->Exception>" + e.toString());
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
		return null;
	}

	/**
	 * 查询用户是否在最新入驻的名单里
	 * @param uid
	 * @return
	 */
	public Double getAllNewJoinRoomInfoByUser(int uid) {
		ShardedJedisPool shardPool = RedisBucket.getInstance().getShardPool(RedisContant.RedisNameOther);
		ShardedJedis redis = null;
		Double dbl = null;
		try {
			redis = shardPool.getResource();
			dbl = redis.zscore(RedisContant.keyRoomNewJoinAll, String.valueOf(uid));
		} catch (Exception e) {
			logger.info("redis 端口名" + RedisContant.RedisNameOther + " 数据库连接活跃数--<" + shardPool.getNumActive()
					+ ">--空闲连接数--<" + shardPool.getNumIdle() + ">--等待连接数--<" + shardPool.getNumWaiters() + ">");
			logger.error("<getAllNewJoinRoomInfoByUser->Exception>" + e.toString());
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
		return dbl;
	}

	/**
	 * 删除最新入驻开播列表
	 *
	 * @param uid
	 * @return
	 */
	public Long delNewJoinRoom(int uid) {
		ShardedJedisPool shardPool = RedisBucket.getInstance().getShardPool(RedisContant.RedisNameOther);
		ShardedJedis redis = null;
		try {
			logger.info("keyRoomNewJoin enter into uid=" + uid);
			redis = shardPool.getResource();
			Long zrem = redis.zrem(RedisContant.keyRoomNewJoin, String.valueOf(uid));
			Set<String> zrevrange = redis.zrevrange(RedisContant.keyRoomNewJoin, 0, -1);
			logger.info("zrevrange-->" + zrevrange);
			return zrem;
		} catch (Exception e) {
			logger.info("redis 端口名" + RedisContant.RedisNameOther + " 数据库连接活跃数--<" + shardPool.getNumActive()
					+ ">--空闲连接数--<" + shardPool.getNumIdle() + ">--等待连接数--<" + shardPool.getNumWaiters() + ">");
			logger.error("<keyRoomNewJoin->Exception>" + e.toString());
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
		return null;

	}

	/**
	 * 获取开播推荐列表
	 *
	 * @param page
	 * @return
	 */
	public Set<String> getRecommendRoom(int start, int rows) {
		ShardedJedisPool shardPool = RedisBucket.getInstance().getShardPool(RedisContant.RedisNameOther);
		ShardedJedis redis = null;
		try {
			redis = shardPool.getResource();
			if (rows > 0) {
				return redis.zrevrange(RedisContant.KeyRoomRecommend, start, start + rows - 1);
			} else {
				return redis.zrevrange(RedisContant.KeyRoomRecommend, 0, -1);
			}
		} catch (Exception e) {
			logger.info("redis 端口名" + RedisContant.RedisNameOther + " 数据库连接活跃数--<" + shardPool.getNumActive()
					+ ">--空闲连接数--<" + shardPool.getNumIdle() + ">--等待连接数--<" + shardPool.getNumWaiters() + ">");
			logger.error("<getRecommendRoom->Exception>" + e.toString());
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
		return null;
	}

	public int getRecommendRoomAll() {
		ShardedJedisPool shardPool = RedisBucket.getInstance().getShardPool(RedisContant.RedisNameOther);
		ShardedJedis redis = null;
		try {
			redis = shardPool.getResource();
			return redis.zcard(RedisContant.KeyRoomRecommend).intValue();
		} catch (Exception e) {
			logger.info("redis 端口名" + RedisContant.RedisNameOther + " 数据库连接活跃数--<" + shardPool.getNumActive()
					+ ">--空闲连接数--<" + shardPool.getNumIdle() + ">--等待连接数--<" + shardPool.getNumWaiters() + ">");
			logger.error("<getRecommendRoomAll->Exception>" + e.toString());
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
		return 0;
	}

	/**
	 * 获取推荐列表中其中的一个用户
	 * 
	 * @param uid
	 * @return
	 */
	public Double getRecommendRoomInfo(int uid) {
		ShardedJedisPool shardPool = RedisBucket.getInstance().getShardPool(RedisContant.RedisNameOther);
		ShardedJedis redis = null;
		Double dbl = null;
		try {
			redis = shardPool.getResource();
			dbl = redis.zscore(RedisContant.KeyRoomRecommend, String.valueOf(uid));
		} catch (Exception e) {
			logger.info("redis 端口名" + RedisContant.RedisNameOther + " 数据库连接活跃数--<" + shardPool.getNumActive()
					+ ">--空闲连接数--<" + shardPool.getNumIdle() + ">--等待连接数--<" + shardPool.getNumWaiters() + ">");
			logger.error("<getRecommendRoomInfo->Exception>" + e.toString());
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
		return dbl;
	}

	/**
	 * 删除开播推荐列表
	 *
	 * @param uid
	 * @return
	 */
	public Long delRecommendRoom(int uid) {
		ShardedJedisPool shardPool = RedisBucket.getInstance().getShardPool(RedisContant.RedisNameOther);
		ShardedJedis redis = null;
		try {
			logger.info("delRecommendRoom enter into uid=" + uid);
			redis = shardPool.getResource();
			Long zrem = redis.zrem(RedisContant.KeyRoomRecommend, String.valueOf(uid));
			Set<String> zrevrange = redis.zrevrange(RedisContant.KeyRoomRecommend, 0, -1);
			logger.info("zrevrange-->" + zrevrange);
			return zrem;
		} catch (Exception e) {
			logger.info("redis 端口名" + RedisContant.RedisNameOther + " 数据库连接活跃数--<" + shardPool.getNumActive()
					+ ">--空闲连接数--<" + shardPool.getNumIdle() + ">--等待连接数--<" + shardPool.getNumWaiters() + ">");
			logger.error("<delRecommendRoom->Exception>" + e.toString());
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
		return null;

	}

	/**
	 * 添加最新开播主播
	 * 
	 * @param uid
	 * @param host
	 * @param rq
	 */
	public void addhotRoom(int uid, int host, double rq, int type) {
		ShardedJedisPool shardPool = RedisBucket.getInstance().getShardPool(RedisContant.RedisNameOther);
		ShardedJedis redis = null;
		try {
			redis = shardPool.getResource();
			Long zadd = redis.zadd(RedisContant.KeyRoomHot, rq, String.valueOf(uid));
			if (type == 0) {
				// 开播
			} else {
				if (zadd == 1) {
					redis.zrem(RedisContant.KeyRoomRecommend, String.valueOf(uid));
				}
			}
		} catch (Exception e) {
			logger.info("redis 端口名" + RedisContant.RedisNameOther + " 数据库连接活跃数--<" + shardPool.getNumActive()
					+ ">--空闲连接数--<" + shardPool.getNumIdle() + ">--等待连接数--<" + shardPool.getNumWaiters() + ">");
			logger.error("<addhotRoom->Exception>" + e.toString());
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
	}

	/**
	 * 添加手机开播主播
	 * 
	 * @param uid
	 * @param host
	 * @param rq
	 */
	public void addMobileRoom(int uid, double rq) {
		ShardedJedisPool shardPool = RedisBucket.getInstance().getShardPool(RedisContant.RedisNameOther);
		ShardedJedis redis = null;
		try {
			redis = shardPool.getResource();
			redis.zadd(RedisContant.keyRoomMobile, rq, String.valueOf(uid));
		} catch (Exception e) {
			logger.info("redis 端口名" + RedisContant.RedisNameOther + " 数据库连接活跃数--<" + shardPool.getNumActive()
					+ ">--空闲连接数--<" + shardPool.getNumIdle() + ">--等待连接数--<" + shardPool.getNumWaiters() + ">");
			logger.error("<addMobileRoom->Exception>" + e.toString());
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
	}

	/**
	 * 获取开播热门列表
	 * 
	 * @param page
	 * @return
	 */
	public Set<String> getHotRoom(int page) {
		ShardedJedisPool shardPool = RedisBucket.getInstance().getShardPool(RedisContant.RedisNameOther);
		ShardedJedis redis = null;
		try {
			redis = shardPool.getResource();
			if (page > 0) {
				return redis.zrevrange(RedisContant.KeyRoomHot, (page - 1) * pageSiz, page * pageSiz - 1);
			}
			return redis.zrevrange(RedisContant.KeyRoomHot, 0, -1);
		} catch (Exception e) {
			logger.info("redis 端口名" + RedisContant.RedisNameOther + " 数据库连接活跃数--<" + shardPool.getNumActive()
					+ ">--空闲连接数--<" + shardPool.getNumIdle() + ">--等待连接数--<" + shardPool.getNumWaiters() + ">");
			logger.error("<getHotRoom->Exception>" + e.toString());
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
		return null;
	}

	/**
	 * 获取开播手机列表
	 * @param page
	 * @return
	 */
	public Set<String> getMobileRoom(int page) {
		ShardedJedisPool shardPool = RedisBucket.getInstance().getShardPool(RedisContant.RedisNameOther);
		ShardedJedis redis = null;
		try {
			redis = shardPool.getResource();
			if (page > 0) {
				return redis.zrevrange(RedisContant.keyRoomMobile, (page - 1) * pageSiz, page * pageSiz - 1);
			}
			return redis.zrevrange(RedisContant.keyRoomMobile, 0, -1);
		} catch (Exception e) {
			logger.info("redis 端口名" + RedisContant.RedisNameOther + " 数据库连接活跃数--<" + shardPool.getNumActive()
					+ ">--空闲连接数--<" + shardPool.getNumIdle() + ">--等待连接数--<" + shardPool.getNumWaiters() + ">");
			logger.error("<getMobileRoom->Exception>" + e.toString());
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
		return null;
	}

	public int getHotRoomAll() {
		ShardedJedisPool shardPool = RedisBucket.getInstance().getShardPool(RedisContant.RedisNameOther);
		ShardedJedis redis = null;
		try {
			redis = shardPool.getResource();
			return redis.zcard(RedisContant.KeyRoomHot).intValue();
		} catch (Exception e) {
			logger.info("redis 端口名" + RedisContant.RedisNameOther + " 数据库连接活跃数--<" + shardPool.getNumActive()
					+ ">--空闲连接数--<" + shardPool.getNumIdle() + ">--等待连接数--<" + shardPool.getNumWaiters() + ">");
			logger.error("<getHotRoomAll->Exception>" + e.toString());
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
		return 0;
	}

	/**
	 * 获取推荐列表中其中的一个用户
	 * 
	 * @param uid
	 * @return
	 */
	public Double getHotRoomInfo(int uid) {
		ShardedJedisPool shardPool = RedisBucket.getInstance().getShardPool(RedisContant.RedisNameOther);
		ShardedJedis redis = null;
		Double dbl = null;
		try {
			redis = shardPool.getResource();
			dbl = redis.zscore(RedisContant.KeyRoomHot, String.valueOf(uid));
		} catch (Exception e) {
			logger.info("redis 端口名" + RedisContant.RedisNameOther + " 数据库连接活跃数--<" + shardPool.getNumActive()
					+ ">--空闲连接数--<" + shardPool.getNumIdle() + ">--等待连接数--<" + shardPool.getNumWaiters() + ">");
			logger.error("<getHotRoomInfo->Exception>" + e.toString());
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
		return dbl;
	}

	/**
	 * 获取手机列表中其中的一个用户
	 * 
	 * @param uid
	 * @return
	 */
	public Double getMobileRoomInfo(int uid) {
		ShardedJedisPool shardPool = RedisBucket.getInstance().getShardPool(RedisContant.RedisNameOther);
		ShardedJedis redis = null;
		Double dbl = null;
		try {
			redis = shardPool.getResource();
			dbl = redis.zscore(RedisContant.keyRoomMobile, String.valueOf(uid));
		} catch (Exception e) {
			logger.info("redis 端口名" + RedisContant.RedisNameOther + " 数据库连接活跃数--<" + shardPool.getNumActive()
					+ ">--空闲连接数--<" + shardPool.getNumIdle() + ">--等待连接数--<" + shardPool.getNumWaiters() + ">");
			logger.error("<getMobileRoomInfo->Exception>" + e.toString());
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
		return dbl;
	}

	/**
	 * 删除开播热门列表
	 *
	 * @param uid
	 * @return
	 */
	public Long delHotRoom(int uid) {
		ShardedJedisPool shardPool = RedisBucket.getInstance().getShardPool(RedisContant.RedisNameOther);
		ShardedJedis redis = null;
		try {
			redis = shardPool.getResource();
			return redis.zrem(RedisContant.KeyRoomHot, String.valueOf(uid));
		} catch (Exception e) {
			logger.info("redis 端口名" + RedisContant.RedisNameOther + " 数据库连接活跃数--<" + shardPool.getNumActive()
					+ ">--空闲连接数--<" + shardPool.getNumIdle() + ">--等待连接数--<" + shardPool.getNumWaiters() + ">");
			logger.error("<delHotRoom->Exception>" + e.toString());
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
		return null;
	}

	/**
	 * 删除开播手机列表
	 *
	 * @param uid
	 * @return
	 */
	public Long delMobileRoom(int uid) {
		ShardedJedisPool shardPool = RedisBucket.getInstance().getShardPool(RedisContant.RedisNameOther);
		ShardedJedis redis = null;
		try {
			redis = shardPool.getResource();
			return redis.zrem(RedisContant.keyRoomMobile, String.valueOf(uid));
		} catch (Exception e) {
			logger.info("redis 端口名" + RedisContant.RedisNameOther + " 数据库连接活跃数--<" + shardPool.getNumActive()
					+ ">--空闲连接数--<" + shardPool.getNumIdle() + ">--等待连接数--<" + shardPool.getNumWaiters() + ">");
			logger.error("<delMobileRoom->Exception>" + e.toString());
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
		return null;
	}

	/**
	 * 添加普通开播主播
	 * 
	 * @param uid
	 * @param host
	 * @param rq
	 */
	public void addBaseRoom(int uid, int host, int rq, int type) {
		ShardedJedisPool shardPool = RedisBucket.getInstance().getShardPool(RedisContant.RedisNameOther);
		ShardedJedis redis = null;
		String str = String.valueOf(host) + String.format("%05d", rq);
		try {
			redis = shardPool.getResource();
			Long zadd = redis.zadd(RedisContant.keyRoomBase, Double.valueOf(str), String.valueOf(uid));
			if (type == 0) {
				// 开播
			} else {
				if (zadd == 1) {
					redis.zrem(RedisContant.KeyRoomRecommend, String.valueOf(uid));
				}
			}
		} catch (Exception e) {
			logger.info("redis 端口名" + RedisContant.RedisNameOther + " 数据库连接活跃数--<" + shardPool.getNumActive()
					+ ">--空闲连接数--<" + shardPool.getNumIdle() + ">--等待连接数--<" + shardPool.getNumWaiters() + ">");
			logger.error("<addBaseRoom->Exception>" + e.toString());
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
	}

	/**
	 * 获取开播普通列表
	 * 
	 * @param page
	 * @return
	 */
	public Set<String> getBaseRoom(int page) {
		ShardedJedisPool shardPool = RedisBucket.getInstance().getShardPool(RedisContant.RedisNameOther);
		ShardedJedis redis = null;
		try {
			redis = shardPool.getResource();
			if (page > 0) {
				return redis.zrevrange(RedisContant.keyRoomBase, (page - 1) * pageSiz, page * pageSiz - 1);
			}
			return redis.zrevrange(RedisContant.keyRoomBase, 0, -1);
		} catch (Exception e) {
			logger.info("redis 端口名" + RedisContant.RedisNameOther + " 数据库连接活跃数--<" + shardPool.getNumActive()
					+ ">--空闲连接数--<" + shardPool.getNumIdle() + ">--等待连接数--<" + shardPool.getNumWaiters() + ">");
			logger.error("<getBaseRoom->Exception>" + e.toString());
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
		return null;
	}

	public int getBaseRoomAll() {
		ShardedJedisPool shardPool = RedisBucket.getInstance().getShardPool(RedisContant.RedisNameOther);
		ShardedJedis redis = null;
		try {
			redis = shardPool.getResource();
			return redis.zcard(RedisContant.keyRoomBase).intValue();
		} catch (Exception e) {
			logger.info("redis 端口名" + RedisContant.RedisNameOther + " 数据库连接活跃数--<" + shardPool.getNumActive()
					+ ">--空闲连接数--<" + shardPool.getNumIdle() + ">--等待连接数--<" + shardPool.getNumWaiters() + ">");
			logger.error("<getBaseRoomAll->Exception>" + e.toString());
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
		return 0;
	}

	/**
	 * 获取推荐列表中其中的一个用户
	 * 
	 * @param uid
	 * @return
	 */
	public Double getBaseRoomInfo(int uid) {
		ShardedJedisPool shardPool = RedisBucket.getInstance().getShardPool(RedisContant.RedisNameOther);
		ShardedJedis redis = null;
		Double dbl = null;
		try {
			redis = shardPool.getResource();
			dbl = redis.zscore(RedisContant.keyRoomBase, String.valueOf(uid));
		} catch (Exception e) {
			logger.info("redis 端口名" + RedisContant.RedisNameOther + " 数据库连接活跃数--<" + shardPool.getNumActive()
					+ ">--空闲连接数--<" + shardPool.getNumIdle() + ">--等待连接数--<" + shardPool.getNumWaiters() + ">");
			logger.error("<getBaseRoomInfo->Exception>" + e.toString());
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
		return dbl;
	}

	/**
	 * 删除普通热门列表
	 *
	 * @param uid
	 * @return
	 */
	public Long delBaseRoom(int uid) {
		ShardedJedisPool shardPool = RedisBucket.getInstance().getShardPool(RedisContant.RedisNameOther);
		ShardedJedis redis = null;
		try {
			redis = shardPool.getResource();
			return redis.zrem(RedisContant.keyRoomBase, String.valueOf(uid));
		} catch (Exception e) {
			logger.info("redis 端口名" + RedisContant.RedisNameOther + " 数据库连接活跃数--<" + shardPool.getNumActive()
					+ ">--空闲连接数--<" + shardPool.getNumIdle() + ">--等待连接数--<" + shardPool.getNumWaiters() + ">");
			logger.error("<delBaseRoom->Exception>" + e.toString());
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
		return null;
	}

	/**
	 * 添加房间举报数
	 *
	 * @param dstUid
	 * @return
	 */
	public int addReport(int dstUid) {
		ShardedJedisPool shardPool = RedisBucket.getInstance().getShardPool(RedisContant.RedisNameOther);
		ShardedJedis redis = null;
		try {
			redis = shardPool.getResource();
			return redis.incr(RedisContant.KeyRoomReports + dstUid).intValue();
		} catch (Exception e) {
			logger.info("redis 端口名" + RedisContant.RedisNameOther + " 数据库连接活跃数--<" + shardPool.getNumActive()
					+ ">--空闲连接数--<" + shardPool.getNumIdle() + ">--等待连接数--<" + shardPool.getNumWaiters() + ">");
			logger.error("<addReport->Exception>" + e.toString());
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
		return 0;
	}

	/**
	 * 处理房间举报数
	 *
	 * @param dstUid
	 * @return
	 */
	public int delReport(int dstUid) {
		ShardedJedisPool shardPool = RedisBucket.getInstance().getShardPool(RedisContant.RedisNameOther);
		ShardedJedis redis = null;
		try {
			redis = shardPool.getResource();
			return redis.del(RedisContant.KeyRoomReports + dstUid).intValue();
		} catch (Exception e) {
			logger.info("redis 端口名" + RedisContant.RedisNameOther + " 数据库连接活跃数--<" + shardPool.getNumActive()
					+ ">--空闲连接数--<" + shardPool.getNumIdle() + ">--等待连接数--<" + shardPool.getNumWaiters() + ">");
			logger.error("<delReport->Exception>" + e.toString());
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
		return 0;
	}

	/**
	 * 缓存ncode
	 *
	 * @param ncode
	 * @param seconds
	 */
	public void setNcode(String ncode, int seconds) {
		ShardedJedisPool shardPool = RedisBucket.getInstance().getShardPool(RedisContant.RedisNameOther);
		ShardedJedis redis = null;
		try {
			redis = shardPool.getResource();
			redis.setex(RedisContant.KeyHttpReq + ncode, seconds, ncode);
		} catch (Exception e) {
			logger.info("redis 端口名" + RedisContant.RedisNameOther + " 数据库连接活跃数--<" + shardPool.getNumActive()
					+ ">--空闲连接数--<" + shardPool.getNumIdle() + ">--等待连接数--<" + shardPool.getNumWaiters() + ">");
			logger.error("<setNcode->Exception>" + e.toString());
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
	}

	/**
	 * 获取ncode
	 *
	 * @param ncode
	 * @return
	 */
	public String getNcode(String ncode) {
		ShardedJedisPool shardPool = RedisBucket.getInstance().getShardPool(RedisContant.RedisNameOther);
		ShardedJedis redis = null;
		try {
			redis = shardPool.getResource();
			return redis.get(RedisContant.KeyHttpReq + ncode);
		} catch (Exception e) {
			logger.info("redis 端口名" + RedisContant.RedisNameOther + " 数据库连接活跃数--<" + shardPool.getNumActive()
					+ ">--空闲连接数--<" + shardPool.getNumIdle() + ">--等待连接数--<" + shardPool.getNumWaiters() + ">");
			logger.error("<getNcode->Exception>" + e.toString());
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
		return null;
	}

	/**
	 * 缓存当天对指定手机发验证码的次数
	 *
	 * @param mobile
	 */
	public void setSendCodeTimes(String mobile, long seconds) {
		ShardedJedisPool shardPool = RedisBucket.getInstance().getShardPool(RedisContant.RedisNameOther);
		ShardedJedis redis = null;
		try {
			redis = shardPool.getResource();
			redis.incr(RedisContant.KeyMobileSendCodeOfDay + mobile);
			if (seconds > 0) {
				redis.expireAt(RedisContant.KeyMobileSendCodeOfDay + mobile, seconds);
			}
		} catch (Exception e) {
			logger.info("redis 端口名" + RedisContant.RedisNameOther + " 数据库连接活跃数--<" + shardPool.getNumActive()
					+ ">--空闲连接数--<" + shardPool.getNumIdle() + ">--等待连接数--<" + shardPool.getNumWaiters() + ">");
			logger.error("<setSendCodeTimes->Exception>" + e.toString());
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
	}

	/**
	 * 获取指定手机验证码发送的次数
	 *
	 * @param mobile
	 * @return
	 */
	public int getSendCodeTimes(String mobile) {
		ShardedJedisPool shardPool = RedisBucket.getInstance().getShardPool(RedisContant.RedisNameOther);
		ShardedJedis redis = null;
		String times = "";
		try {
			redis = shardPool.getResource();
			times = redis.get(RedisContant.KeyMobileSendCodeOfDay + mobile);
		} catch (Exception e) {
			logger.info("redis 端口名" + RedisContant.RedisNameOther + " 数据库连接活跃数--<" + shardPool.getNumActive()
					+ ">--空闲连接数--<" + shardPool.getNumIdle() + ">--等待连接数--<" + shardPool.getNumWaiters() + ">");
			logger.error("<getSendCodeTimes->Exception>" + e.toString());
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
		if (times == null || times == "") {
			return 0;
		} else {
			return Integer.valueOf(times);
		}
	}

	/**
	 * 缓存发送给手机的验证码
	 *
	 * @param mobile
	 * @param code
	 * @param seconds
	 */
	public void setSendCode(String mobile, String code, int seconds) {

		ShardedJedisPool shardPool = RedisBucket.getInstance().getShardPool(RedisContant.RedisNameOther);
		ShardedJedis redis = null;
		try {
			redis = shardPool.getResource();
			redis.setex(RedisContant.KeyMobileSendCode + mobile, seconds, code);
		} catch (Exception e) {
			logger.info("redis 端口名" + RedisContant.RedisNameOther + " 数据库连接活跃数--<" + shardPool.getNumActive()
					+ ">--空闲连接数--<" + shardPool.getNumIdle() + ">--等待连接数--<" + shardPool.getNumWaiters() + ">");
			logger.error("<setSendCodeTimes->Exception>" + e.toString());
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
	}

	/**
	 * 手机验证码 验证
	 *
	 * @param mobile
	 * @return
	 */
	public Boolean getSendCode(String mobile, String code) {
		ShardedJedisPool shardPool = RedisBucket.getInstance().getShardPool(RedisContant.RedisNameOther);
		ShardedJedis redis = null;
		try {
			redis = shardPool.getResource();
			String scode = redis.get(RedisContant.KeyMobileSendCode + mobile);
			if (scode == null || scode == "") {
				return false;
			} else {
				if (scode.equals(code)) {
					redis.del(RedisContant.KeyMobileSendCode + mobile);
					return true;
				}
			}
		} catch (Exception e) {
			logger.info("redis 端口名" + RedisContant.RedisNameOther + " 数据库连接活跃数--<" + shardPool.getNumActive()
					+ ">--空闲连接数--<" + shardPool.getNumIdle() + ">--等待连接数--<" + shardPool.getNumWaiters() + ">");
			logger.error("<getSendCode->Exception>" + e.toString());
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
		return false;
	}

	/**
	 * 记录每个月发送成功的短信数量
	 *
	 * @param mobile
	 * @param code
	 */
	public void setSendMsgs(String mobile, String code) {
		ShardedJedisPool shardPool = RedisBucket.getInstance().getShardPool(RedisContant.RedisNameOther);
		ShardedJedis redis = null;
		try {
			redis = shardPool.getResource();
			redis.incr(RedisContant.KeyMsgtotal + DateUtils.dateToString(null, "YMM"));
		} catch (Exception e) {
			logger.info("redis 端口名" + RedisContant.RedisNameOther + " 数据库连接活跃数--<" + shardPool.getNumActive()
					+ ">--空闲连接数--<" + shardPool.getNumIdle() + ">--等待连接数--<" + shardPool.getNumWaiters() + ">");
			logger.error("<setSendMsgs->Exception>" + e.toString());
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
	}

	/**
	 * 记录uid产生的订单，反刷（订单支付成功后 清空）
	 *
	 * @param uid
	 */
	public void addPayRecord(Integer uid) {
		ShardedJedisPool shardPool = RedisBucket.getInstance().getShardPool(RedisContant.RedisNameOther);
		ShardedJedis redis = null;
		try {
			redis = shardPool.getResource();
			if (redis.exists(RedisContant.KeyPayRecord + uid)) {
				redis.incr(RedisContant.KeyPayRecord + uid);
			} else {
				redis.incr(RedisContant.KeyPayRecord + uid);
				redis.expire(RedisContant.KeyPayRecord + uid, 300);
			}
		} catch (Exception e) {
			logger.info("redis 端口名" + RedisContant.RedisNameOther + " 数据库连接活跃数--<" + shardPool.getNumActive()
					+ ">--空闲连接数--<" + shardPool.getNumIdle() + ">--等待连接数--<" + shardPool.getNumWaiters() + ">");
			logger.error("<addPayRecord->Exception>" + e.toString());
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
	}

	/**
	 * 获取该用户新产生的订单
	 *
	 * @param uid
	 * @return
	 */
	public int getPayRecord(Integer uid) {
		String icnt = "";
		ShardedJedisPool shardPool = RedisBucket.getInstance().getShardPool(RedisContant.RedisNameOther);
		ShardedJedis redis = null;
		try {
			redis = shardPool.getResource();
			icnt = redis.get(RedisContant.KeyPayRecord + uid);
		} catch (Exception e) {
			logger.info("redis 端口名" + RedisContant.RedisNameOther + " 数据库连接活跃数--<" + shardPool.getNumActive()
					+ ">--空闲连接数--<" + shardPool.getNumIdle() + ">--等待连接数--<" + shardPool.getNumWaiters() + ">");
			logger.error("<addPayRecord->Exception>" + e.toString());
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
		if (icnt == null || icnt == "") {
			return 0;
		} else {
			return Integer.valueOf(icnt);
		}
	}

	/**
	 * 删除该用户反刷记录
	 *
	 * @param uid
	 */
	public void delPayRecord(Integer uid) {
		ShardedJedisPool shardPool = RedisBucket.getInstance().getShardPool(RedisContant.RedisNameOther);
		ShardedJedis redis = null;
		try {
			redis = shardPool.getResource();
			redis.del(RedisContant.KeyPayRecord + uid);
		} catch (Exception e) {
			logger.info("redis 端口名" + RedisContant.RedisNameOther + " 数据库连接活跃数--<" + shardPool.getNumActive()
					+ ">--空闲连接数--<" + shardPool.getNumIdle() + ">--等待连接数--<" + shardPool.getNumWaiters() + ">");
			logger.error("<addPayRecord->Exception>" + e.toString());
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
	}

	/**
	 * 新增单笔提现金额限制
	 *
	 * @param limit
	 */
	public void setWithDrawLimit(int limit) {
		ShardedJedisPool shardPool = RedisBucket.getInstance().getShardPool(RedisContant.RedisNameOther);
		ShardedJedis redis = null;
		try {
			redis = shardPool.getResource();
			redis.set(RedisContant.keyWithDrawLimit, String.valueOf(limit));
		} catch (Exception e) {
			logger.info("redis 端口名" + RedisContant.RedisNameOther + " 数据库连接活跃数--<" + shardPool.getNumActive()
					+ ">--空闲连接数--<" + shardPool.getNumIdle() + ">--等待连接数--<" + shardPool.getNumWaiters() + ">");
			logger.error("<addWithDrawLimit->Exception>" + e.toString());
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
	}

	/**
	 * 获取单笔提现金额限制
	 *
	 * @return
	 */
	public int getWithDrawLimit() {
		ShardedJedisPool shardPool = RedisBucket.getInstance().getShardPool(RedisContant.RedisNameOther);
		ShardedJedis redis = null;
		String limit = "";
		try {
			redis = shardPool.getResource();
			limit = redis.get(RedisContant.keyWithDrawLimit);
		} catch (Exception e) {
			logger.info("redis 端口名" + RedisContant.RedisNameOther + " 数据库连接活跃数--<" + shardPool.getNumActive()
					+ ">--空闲连接数--<" + shardPool.getNumIdle() + ">--等待连接数--<" + shardPool.getNumWaiters() + ">");
			logger.error("<getWithDrawLimit->Exception>" + e.toString());
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
		if (limit == null) {
			return 0;
		} else {
			return Integer.valueOf(limit);
		}
	}

	/**
	 * 获取IOS客户端功能是否展示
	 *
	 * @param version 版本
	 * @return iosVersionJson	开关控制开关字符串
	 */
	public String getIOSShow(String version) {
		ShardedJedisPool shardPool = RedisBucket.getInstance().getShardPool(RedisContant.RedisNameOther);
		ShardedJedis redis = null;
		String res = null;
		try {
			redis = shardPool.getResource();
			res = redis.get(RedisContant.verifyIos+":"+version);
		} catch (Exception e) {
			logger.info("redis 端口名" + RedisContant.RedisNameOther + " 数据库连接活跃数--<" + shardPool.getNumActive()
					+ ">--空闲连接数--<" + shardPool.getNumIdle() + ">--等待连接数--<" + shardPool.getNumWaiters() + ">");
			logger.error("<getIOSShow->Exception>" + e.toString());
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
		return res == null?"":res;
	}

	/**
	 * 记录提现次数
	 *
	 * @param uid
	 */
	public void setWithDraw(int uid) {
		ShardedJedisPool shardPool = RedisBucket.getInstance().getShardPool(RedisContant.RedisNameOther);
		ShardedJedis redis = null;
		try {
			redis = shardPool.getResource();
			redis.incr(RedisContant.keyWithDraws + uid);
		} catch (Exception e) {
			logger.info("redis 端口名" + RedisContant.RedisNameOther + " 数据库连接活跃数--<" + shardPool.getNumActive()
					+ ">--空闲连接数--<" + shardPool.getNumIdle() + ">--等待连接数--<" + shardPool.getNumWaiters() + ">");
			logger.error("<setWithDraw->Exception>" + e.toString());
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
	}

	/**
	 * 获取提现次数
	 *
	 * @param uid
	 * @return
	 */
	public int getWithDraw(int uid) {
		ShardedJedisPool shardPool = RedisBucket.getInstance().getShardPool(RedisContant.RedisNameOther);
		ShardedJedis redis = null;
		String times = "";
		try {
			redis = shardPool.getResource();
			times = redis.get(RedisContant.keyWithDraws + uid);
		} catch (Exception e) {
			logger.info("redis 端口名" + RedisContant.RedisNameOther + " 数据库连接活跃数--<" + shardPool.getNumActive()
					+ ">--空闲连接数--<" + shardPool.getNumIdle() + ">--等待连接数--<" + shardPool.getNumWaiters() + ">");
			logger.error("<getWithDraw->Exception>" + e.toString());
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
		if (times == null || times == "") {
			return 0;
		} else {
			return Integer.valueOf(times);
		}
	}

	/**
	 * 记录开播时间，防止频繁开播
	 *
	 * @param uid
	 */
	public void addLivingClick(int uid) {
		ShardedJedisPool shardPool = RedisBucket.getInstance().getShardPool(RedisContant.RedisNameOther);
		ShardedJedis redis = null;
		try {
			redis = shardPool.getResource();
			redis.setex(RedisContant.keyLivingClick + uid, 1, "1");
		} catch (Exception ex) {
			logger.info("redis 端口名" + RedisContant.RedisNameOther + " 数据库连接活跃数--<" + shardPool.getNumActive()
					+ ">--空闲连接数--<" + shardPool.getNumIdle() + ">--等待连接数--<" + shardPool.getNumWaiters() + ">");
			logger.error("<addLivingClick->Exception>" + ex.toString());
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
	}

	/**
	 * 获取该用户开播的频繁记录
	 *
	 * @param uid
	 * @return false 不频繁 true 频繁
	 */
	public boolean getLivingClick(int uid) {
		ShardedJedisPool shardPool = RedisBucket.getInstance().getShardPool(RedisContant.RedisNameOther);
		ShardedJedis redis = null;
		String str = "";
		try {
			redis = shardPool.getResource();
			str = redis.get(RedisContant.keyLivingClick + uid);
		} catch (Exception e) {
			logger.info("redis 端口名" + RedisContant.RedisNameOther + " 数据库连接活跃数--<" + shardPool.getNumActive()
					+ ">--空闲连接数--<" + shardPool.getNumIdle() + ">--等待连接数--<" + shardPool.getNumWaiters() + ">");
			logger.error("<getLivingClick->Exception>" + e.toString());
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
		if (str == null || "".equals(str)) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * 记录用户进房记录，防止同一用户多次进入同一房间 造成的多次提示(15秒内 不再提醒)
	 *
	 * @param srcUid
	 * @param dstUid
	 */
	public void addUserIntoRoom(int srcUid, int dstUid) {
		ShardedJedisPool shardPool = RedisBucket.getInstance().getShardPool(RedisContant.RedisNameOther);
		ShardedJedis redis = null;
		try {
			redis = shardPool.getResource();
			redis.setex(RedisContant.keyUserIntoRoom + srcUid + ":" + dstUid, 15, "1");
		} catch (Exception e) {
			logger.info("redis 端口名" + RedisContant.RedisNameOther + " 数据库连接活跃数--<" + shardPool.getNumActive()
					+ ">--空闲连接数--<" + shardPool.getNumIdle() + ">--等待连接数--<" + shardPool.getNumWaiters() + ">");
			logger.error("<addUserIntoRoom->Exception>" + e.toString());
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
	}

	/**
	 * 判断用户是否存在15秒内进同一房间
	 *
	 * @param srcUid
	 * @param dstUid
	 * @return true 15秒内多次进入同一房间，false 则不是
	 */
	public boolean getUserIntoRoom(int srcUid, int dstUid) {
		ShardedJedisPool shardPool = RedisBucket.getInstance().getShardPool(RedisContant.RedisNameOther);
		ShardedJedis redis = null;
		String str = "";
		try {
			redis = shardPool.getResource();
			str = redis.get(RedisContant.keyUserIntoRoom + srcUid + ":" + dstUid);
		} catch (Exception e) {
			logger.info("redis 端口名" + RedisContant.RedisNameOther + " 数据库连接活跃数--<" + shardPool.getNumActive()
					+ ">--空闲连接数--<" + shardPool.getNumIdle() + ">--等待连接数--<" + shardPool.getNumWaiters() + ">");
			logger.error("<getUserIntoRoom->Exception>" + e.toString());
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
		if (str == null || "".equals(str)) {
			return false;
		} else {
			return true;
		}
	}

	public void pushQueue(Map<String, Object> map) {

		ShardedJedisPool shardPool = RedisBucket.getInstance().getShardPool(RedisContant.RedisNameOther);
		ShardedJedis redis = null;
		try {
			redis = shardPool.getResource();
			ObjectMapper objectMapper = new ObjectMapper();
			redis.lpush("ot:syncsql", objectMapper.writeValueAsString(map));
		} catch (Exception e) {
			logger.info("redis 端口名" + RedisContant.RedisNameOther + " 数据库连接活跃数--<" + shardPool.getNumActive()
					+ ">--空闲连接数--<" + shardPool.getNumIdle() + ">--等待连接数--<" + shardPool.getNumWaiters() + ">");
			logger.error("<pushQueue->Exception>" + e.toString());
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
	}

	public Long getMchBillNo() {

		ShardedJedisPool shardPool = RedisBucket.getInstance().getShardPool(RedisContant.RedisNameOther);
		ShardedJedis redis = null;
		Long no = (long) 0;
		try {
			redis = shardPool.getResource();
			no = redis.incr(RedisContant.MchBillNo);
			if (no > 99999) {
				redis.set(RedisContant.MchBillNo, "1");
			}
		} catch (Exception e) {
			logger.info("redis 端口名" + RedisContant.RedisNameOther + " 数据库连接活跃数--<" + shardPool.getNumActive()
					+ ">--空闲连接数--<" + shardPool.getNumIdle() + ">--等待连接数--<" + shardPool.getNumWaiters() + ">");
			logger.error("<getMchBillNo->Exception>" + e.toString());
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
		return no;
	}

	/**
	 * 设置禁言
	 * 
	 * @param anchorUid
	 * @param uid
	 * @param type
	 *            =on 禁言 ＝ off 解除禁言
	 * @return
	 */
	public int setSilent(int anchorUid, int uid, String type) {
		ShardedJedisPool shardPool = RedisBucket.getInstance().getShardPool(RedisContant.RedisNameOther);
		ShardedJedis redis = null;
		Long lg = 0l;
		try {
			redis = shardPool.getResource();
			if ("on".equals(type)) {
				lg = redis.hset(RedisContant.keyRoomSilent + anchorUid, String.valueOf(uid), "1");
			} else if ("off".equals(type)) {
				lg = redis.hdel(RedisContant.keyRoomSilent + anchorUid, String.valueOf(uid));
			}
		} catch (Exception e) {
			logger.info("redis 端口名" + RedisContant.RedisNameOther + " 数据库连接活跃数--<" + shardPool.getNumActive()
					+ ">--空闲连接数--<" + shardPool.getNumIdle() + ">--等待连接数--<" + shardPool.getNumWaiters() + ">");
			logger.error("<setSilent->Exception>" + e.toString());
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
		return lg == null ? 0 : lg.intValue();
	}

	/**
	 * 判断该用户是否在该房间禁言
	 * 
	 * @param anchorUid
	 * @param uid
	 * @return true 禁言中 false 不在
	 */
	public boolean getSilent(int anchorUid, int uid) {
		String str = "";
		ShardedJedisPool shardPool = RedisBucket.getInstance().getShardPool(RedisContant.RedisNameOther);
		ShardedJedis redis = null;
		try {
			redis = shardPool.getResource();
			str = redis.hget(RedisContant.keyRoomSilent + anchorUid, String.valueOf(uid));
		} catch (Exception e) {
			logger.info("redis 端口名" + RedisContant.RedisNameOther + " 数据库连接活跃数--<" + shardPool.getNumActive()
					+ ">--空闲连接数--<" + shardPool.getNumIdle() + ">--等待连接数--<" + shardPool.getNumWaiters() + ">");
			logger.error("<getSilent->Exception>" + e.toString());
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
		if (StringUtils.isEmpty(str)) {
			return false;
		} else {
			return true;
		}
	}

	public int delSilent(int anchorUid) {
		Long lg = 0L;
		ShardedJedisPool shardPool = RedisBucket.getInstance().getShardPool(RedisContant.RedisNameOther);
		ShardedJedis redis = null;
		try {
			redis = shardPool.getResource();
			lg = redis.del(RedisContant.keyRoomSilent + anchorUid);
		} catch (Exception e) {
			logger.info("redis 端口名" + RedisContant.RedisNameOther + " 数据库连接活跃数--<" + shardPool.getNumActive()
					+ ">--空闲连接数--<" + shardPool.getNumIdle() + ">--等待连接数--<" + shardPool.getNumWaiters() + ">");
			logger.error("<delSilent->Exception>" + e.toString());
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
		return lg.intValue();
	}

	/**
	 * 设置pay公告
	 * 
	 * @param key
	 *            ＝ios =android
	 * @param value
	 * @return
	 */
	public int setPayNotice(String key, String value) {
		ShardedJedisPool shardPool = RedisBucket.getInstance().getShardPool(RedisContant.RedisNameOther);
		ShardedJedis redis = null;
		Long lg = 0l;
		try {
			redis = shardPool.getResource();
			lg = redis.hset(RedisContant.PayNotice, key, value);
		} catch (Exception e) {
			logger.info("redis 端口名" + RedisContant.RedisNameOther + " 数据库连接活跃数--<" + shardPool.getNumActive()
					+ ">--空闲连接数--<" + shardPool.getNumIdle() + ">--等待连接数--<" + shardPool.getNumWaiters() + ">");
			logger.error("<setPayNotice->Exception>" + e.toString());
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
		return lg == null ? 0 : lg.intValue();
	}

	/**
	 * 获取app的pay公告
	 * 
	 * @param key
	 * @return
	 */
	public String getPayNotice(String key) {
		ShardedJedisPool shardPool = RedisBucket.getInstance().getShardPool(RedisContant.RedisNameOther);
		ShardedJedis redis = null;
		String str = "";
		try {
			redis = shardPool.getResource();
			str = redis.hget(RedisContant.PayNotice, key);
		} catch (Exception e) {
			logger.info("redis 端口名" + RedisContant.RedisNameOther + " 数据库连接活跃数--<" + shardPool.getNumActive()
					+ ">--空闲连接数--<" + shardPool.getNumIdle() + ">--等待连接数--<" + shardPool.getNumWaiters() + ">");
			logger.error("<getPayNotice->Exception>" + e.toString());
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
		return str;
	}

	public Map<String, String> getPayNoticeList() {
		ShardedJedisPool shardPool = RedisBucket.getInstance().getShardPool(RedisContant.RedisNameOther);
		ShardedJedis redis = null;
		Map<String, String> map = null;
		try {
			redis = shardPool.getResource();
			map = redis.hgetAll(RedisContant.PayNotice);
		} catch (Exception e) {
			logger.info("redis 端口名" + RedisContant.RedisNameOther + " 数据库连接活跃数--<" + shardPool.getNumActive()
					+ ">--空闲连接数--<" + shardPool.getNumIdle() + ">--等待连接数--<" + shardPool.getNumWaiters() + ">");
			logger.error("<getPayNoticeList->Exception>" + e.toString());
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
		return map;
	}

	/**
	 * 设置微信登陆信息
	 *
	 * @param sid
	 *            sid
	 * @param wxInfo
	 *            {accessToken,openid,unionid,uid}
	 */
	public void setWxInfo(String sid, String wxInfo) {
		ShardedJedisPool shardPool = RedisBucket.getInstance().getShardPool(RedisContant.RedisNameOther);
		ShardedJedis redis = null;
		try {
			redis = shardPool.getResource();
			redis.hset(RedisContant.KeyWxInfo, sid, wxInfo);
		} catch (Exception e) {
			logger.info("redis 端口名" + RedisContant.RedisNameOther + " 数据库连接活跃数--<" + shardPool.getNumActive()
					+ ">--空闲连接数--<" + shardPool.getNumIdle() + ">--等待连接数--<" + shardPool.getNumWaiters() + ">");
			logger.error("<setWxInfo->Exception>" + e.toString());
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
	}

	/**
	 * 获取微信登陆信息
	 * 
	 * @param sid
	 *            sid
	 * @return {accessToken,openid,unionid,uid}
	 */
	public JSONObject getWxInfo(String sid) {
		ShardedJedisPool shardPool = RedisBucket.getInstance().getShardPool(RedisContant.RedisNameOther);
		ShardedJedis redis = null;
		try {
			redis = shardPool.getResource();
			String info = redis.hget(RedisContant.KeyWxInfo, sid);
			if (info == null) {
				return null;
			}
			return JSON.parseObject(info);
		} catch (Exception e) {
			logger.info("redis 端口名" + RedisContant.RedisNameOther + " 数据库连接活跃数--<" + shardPool.getNumActive()
					+ ">--空闲连接数--<" + shardPool.getNumIdle() + ">--等待连接数--<" + shardPool.getNumWaiters() + ">");
			logger.error("<getWxInfo->Exception>" + e.toString());
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
		return null;
	}

	/**
	 * 内购 审核期间有用
	 * 
	 * @param ver
	 *            版本号
	 * @return
	 */
	public String setApplyIAP(String ver, int stampAt) {
		ShardedJedisPool shardPool = RedisBucket.getInstance().getShardPool(RedisContant.RedisNameOther);
		ShardedJedis redis = null;
		String str = "";
		try {
			redis = shardPool.getResource();
			str = redis.set(RedisContant.keyApplyIAP + ver, "1");
			redis.expireAt(RedisContant.keyApplyIAP + ver, stampAt);
		} catch (Exception e) {
			logger.info("redis 端口名" + RedisContant.RedisNameOther + " 数据库连接活跃数--<" + shardPool.getNumActive()
					+ ">--空闲连接数--<" + shardPool.getNumIdle() + ">--等待连接数--<" + shardPool.getNumWaiters() + ">");
			logger.error("<setApplyIAP->Exception>" + e.toString());
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
		return str;
	}

	/**
	 * 内购 审核期间有用
	 * 
	 * @param ver
	 *            版本号
	 * @return
	 */
	public String getApplyIAP(String ver) {
		String str = "";
		ShardedJedisPool shardPool = RedisBucket.getInstance().getShardPool(RedisContant.RedisNameOther);
		ShardedJedis redis = null;
		try {
			redis = shardPool.getResource();
			str = redis.get(RedisContant.keyApplyIAP + ver);
		} catch (Exception e) {
			logger.info("redis 端口名" + RedisContant.RedisNameOther + " 数据库连接活跃数--<" + shardPool.getNumActive()
					+ ">--空闲连接数--<" + shardPool.getNumIdle() + ">--等待连接数--<" + shardPool.getNumWaiters() + ">");
			logger.error("<setApplyIAP->Exception>" + e.toString());
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
		if (StringUtils.isNotEmpty(str)) {
			return str;
		} else {
			return "";
		}
	}

	/**
	 * 判断是否 允许临时开放推荐位
	 * 
	 * @return 空 则不允许，其他则允许
	 */
	public String getTempRecommend() {
		String str = "";
		ShardedJedisPool shardPool = RedisBucket.getInstance().getShardPool(RedisContant.RedisNameOther);
		ShardedJedis redis = null;
		try {
			redis = shardPool.getResource();
			str = redis.get(RedisContant.keyRecommend);
		} catch (Exception e) {
			logger.info("redis 端口名" + RedisContant.RedisNameOther + " 数据库连接活跃数--<" + shardPool.getNumActive()
					+ ">--空闲连接数--<" + shardPool.getNumIdle() + ">--等待连接数--<" + shardPool.getNumWaiters() + ">");
			logger.error("<getRecommend->Exception>" + e.toString());
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
		if (StringUtils.isNotEmpty(str)) {
			return str;
		} else {
			return "";
		}
	}

	/**
	 * 内购 审核期间有用
	 * 
	 * @param ver
	 *            版本号
	 * @return
	 */
	public List<String> getAclTrustedHostList() {
		List<String> trustedHostList = null;
		ShardedJedisPool shardPool = RedisBucket.getInstance().getShardPool(RedisContant.RedisNameOther);
		ShardedJedis redis = null;
		try {
			redis = shardPool.getResource();
			trustedHostList = redis.lrange(RedisContant.keyAclTrustedHostList, 0, -1);
		} catch (Exception e) {
			logger.info("redis 端口名" + RedisContant.RedisNameOther + " 数据库连接活跃数--<" + shardPool.getNumActive()
					+ ">--空闲连接数--<" + shardPool.getNumIdle() + ">--等待连接数--<" + shardPool.getNumWaiters() + ">");
			logger.error("<setApplyIAP->Exception>" + e.toString());
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
		return trustedHostList;
	}

	/**
	 * 获取520开关，
	 * 
	 * @return true 开启 false 关闭
	 */
	public Boolean get520Swith() {
		String str = "";
		ShardedJedisPool shardPool = RedisBucket.getInstance().getShardPool(RedisContant.RedisNameOther);
		ShardedJedis redis = null;
		try {
			redis = shardPool.getResource();
			str = redis.get(RedisContant.Swith520);
		} catch (Exception e) {
			logger.info("redis 端口名" + RedisContant.RedisNameOther + " 数据库连接活跃数--<" + shardPool.getNumActive()
					+ ">--空闲连接数--<" + shardPool.getNumIdle() + ">--等待连接数--<" + shardPool.getNumWaiters() + ">");
			logger.error("<get520Swith->Exception>" + e.toString());
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
		if (StringUtils.isEmpty(str)) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * 获取活动开关，
	 * 
	 * @return true 开启 false 关闭
	 */
	public Boolean getActivitySwith() {
		String str = "";
		ShardedJedisPool shardPool = RedisBucket.getInstance().getShardPool(RedisContant.RedisNameOther);
		ShardedJedis redis = null;
		try {
			redis = shardPool.getResource();
			str = redis.get(RedisContant.SwithActivity);
		} catch (Exception e) {
			logger.info("redis 端口名" + RedisContant.RedisNameOther + " 数据库连接活跃数--<" + shardPool.getNumActive()
					+ ">--空闲连接数--<" + shardPool.getNumIdle() + ">--等待连接数--<" + shardPool.getNumWaiters() + ">");
			logger.error("<getActivitySwith->Exception>" + e.toString());
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
		if (StringUtils.isEmpty(str)) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * 获取520开关，
	 * 
	 * @return true 开启 false 关闭
	 */
	public Boolean getEnvelopeSwith() {
		String str = "";
		ShardedJedisPool shardPool = RedisBucket.getInstance().getShardPool(RedisContant.RedisNameOther);
		ShardedJedis redis = null;
		try {
			redis = shardPool.getResource();
			str = redis.get(RedisContant.SwithEnvelope);
		} catch (Exception e) {
			logger.info("redis 端口名" + RedisContant.RedisNameOther + " 数据库连接活跃数--<" + shardPool.getNumActive()
					+ ">--空闲连接数--<" + shardPool.getNumIdle() + ">--等待连接数--<" + shardPool.getNumWaiters() + ">");
			logger.error("<get520Swith->Exception>" + e.toString());
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
		if (StringUtils.isEmpty(str)) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * 设置用户与使用设备的关系
	 * 
	 * @param uid
	 * @param deviceNum
	 * @param os
	 */
	public void setAppBroadcast(String uid, String deviceNum, String os) {
		ShardedJedisPool shardPool = RedisBucket.getInstance().getShardPool(RedisContant.RedisNameOther);
		ShardedJedis redis = null;
		try {
			redis = shardPool.getResource();
			if ("ios".equalsIgnoreCase(os)) {
				redis.hset(RedisContant.BroadcastIos, uid, deviceNum);
			} else if ("android".equalsIgnoreCase(os)) {
				redis.hset(RedisContant.BroadcastAndroid, uid, deviceNum);
			}
		} catch (Exception e) {
			logger.info("redis 端口名" + RedisContant.RedisNameOther + " 数据库连接活跃数--<" + shardPool.getNumActive()
					+ ">--空闲连接数--<" + shardPool.getNumIdle() + ">--等待连接数--<" + shardPool.getNumWaiters() + ">");
			logger.error("<setAppBroadcast->Exception>" + e.toString());
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
	}

	public String getAppBroadcast(String os, String uid) {
		ShardedJedisPool shardPool = RedisBucket.getInstance().getShardPool(RedisContant.RedisNameOther);
		ShardedJedis redis = null;
		String map = "";
		try {
			redis = shardPool.getResource();
			if ("ios".equalsIgnoreCase(os)) {
				map = redis.hget(RedisContant.BroadcastIos, uid);
			} else if ("android".equalsIgnoreCase(os)) {
				map = redis.hget(RedisContant.BroadcastAndroid, uid);
			}
		} catch (Exception e) {
			logger.info("redis 端口名" + RedisContant.RedisNameOther + " 数据库连接活跃数--<" + shardPool.getNumActive()
					+ ">--空闲连接数--<" + shardPool.getNumIdle() + ">--等待连接数--<" + shardPool.getNumWaiters() + ">");
			logger.error("<getAppBroadcast->Exception>" + e.toString());
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
		return map == null ? "" : map;
	}

	/**
	 * 获取用户已完成的任务
	 * 
	 * @param uid
	 * @param type
	 *            0 每日 1新手
	 * @return
	 */
	public String getUserFinishedTask(String uid, TaskConfigLib.TaskFor taskFor) {
		String str = "";
		ShardedJedisPool shardPool = RedisBucket.getInstance().getShardPool(RedisContant.RedisNameOther);
		ShardedJedis redis = null;
		try {
			redis = shardPool.getResource();
			str = redis.hget(
					taskFor == TaskFor.Daily ? RedisContant.TaskFinishedDaily : RedisContant.TaskFinishedNewBie,
					String.valueOf(uid));
		} catch (Exception e) {
			logger.info("redis 端口名" + RedisContant.RedisNameOther + " 数据库连接活跃数--<" + shardPool.getNumActive()
					+ ">--空闲连接数--<" + shardPool.getNumIdle() + ">--等待连接数--<" + shardPool.getNumWaiters() + ">");
			logger.error("<getUserFinishedTask->Exception>" + e.toString());
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
		if (StringUtils.isNotEmpty(str)) {
			return str;
		} else {
			return "";
		}
	}

	/**
	 * 获取用户已领取奖励的任务
	 * 
	 * @param uid
	 * @param type
	 *            0 每日 1 新手
	 * @return
	 */
	public String getUserCommitedTask(String uid, TaskConfigLib.TaskFor taskFor) {
		String str = "";
		ShardedJedisPool shardPool = RedisBucket.getInstance().getShardPool(RedisContant.RedisNameOther);
		ShardedJedis redis = null;
		try {
			redis = shardPool.getResource();
			str = redis.hget(
					taskFor == TaskFor.Daily ? RedisContant.TaskCommitedDaily : RedisContant.TaskCommitedNewBie,
					String.valueOf(uid));
		} catch (Exception e) {
			logger.info("redis 端口名" + RedisContant.RedisNameOther + " 数据库连接活跃数--<" + shardPool.getNumActive()
					+ ">--空闲连接数--<" + shardPool.getNumIdle() + ">--等待连接数--<" + shardPool.getNumWaiters() + ">");
			logger.error("<getUserCommitedTask->Exception>" + e.toString());
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
		if (StringUtils.isNotEmpty(str)) {
			return str;
		} else {
			return "";
		}
	}

	/**
	 * 清掉用户可以接受的任务
	 * 
	 * @param uid
	 * @param type
	 *            1 每日 0 新手
	 * @return
	 */
	public boolean clearUserAcceptedTask(String uid, TaskFor taskFor) {
		ShardedJedisPool shardPool = RedisBucket.getInstance().getShardPool(RedisContant.RedisNameOther);
		ShardedJedis redis = null;
		try {
			redis = shardPool.getResource();
			redis.hdel(taskFor == TaskFor.Daily ? RedisContant.TaskAcceptedDaily : RedisContant.TaskAcceptedNewBie,
					uid);
		} catch (Exception e) {
			logger.info("redis 端口名" + RedisContant.RedisNameOther + " 数据库连接活跃数--<" + shardPool.getNumActive()
					+ ">--空闲连接数--<" + shardPool.getNumIdle() + ">--等待连接数--<" + shardPool.getNumWaiters() + ">");
			logger.error("<clearUserAcceptedTask->Exception>" + e.toString());
			return false;
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
		return true;
	}

	/**
	 * 清掉用户已经完成的任务
	 * 
	 * @param uid
	 * @param type
	 *            1 每日 0 新手
	 * @return
	 */
	public boolean clearUserFinishedTask(String uid, TaskFor taskFor) {
		ShardedJedisPool shardPool = RedisBucket.getInstance().getShardPool(RedisContant.RedisNameOther);
		ShardedJedis redis = null;
		try {
			redis = shardPool.getResource();
			redis.hdel(taskFor == TaskFor.Daily ? RedisContant.TaskFinishedDaily : RedisContant.TaskFinishedNewBie,
					uid);
		} catch (Exception e) {
			logger.info("redis 端口名" + RedisContant.RedisNameOther + " 数据库连接活跃数--<" + shardPool.getNumActive()
					+ ">--空闲连接数--<" + shardPool.getNumIdle() + ">--等待连接数--<" + shardPool.getNumWaiters() + ">");
			logger.error("<clearUserFinishedTask->Exception>" + e.toString());
			return false;
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
		return true;
	}

	/**
	 * 清掉用户已经领取奖励的任务
	 * 
	 * @param uid
	 * @param type
	 *            1 每日 0 新手
	 * @return
	 */
	public boolean clearUserCommitedTask(String uid, TaskFor taskFor) {
		ShardedJedisPool shardPool = RedisBucket.getInstance().getShardPool(RedisContant.RedisNameOther);
		ShardedJedis redis = null;
		try {
			redis = shardPool.getResource();
			redis.hdel(taskFor == TaskFor.Daily ? RedisContant.TaskCommitedDaily : RedisContant.TaskCommitedNewBie,
					uid);
		} catch (Exception e) {
			logger.info("redis 端口名" + RedisContant.RedisNameOther + " 数据库连接活跃数--<" + shardPool.getNumActive()
					+ ">--空闲连接数--<" + shardPool.getNumIdle() + ">--等待连接数--<" + shardPool.getNumWaiters() + ">");
			logger.error("<clearUserCommitedTask->Exception>" + e.toString());
			return false;
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
		return true;
	}

	/**
	 * 获取用户可以接受的任务
	 * 
	 * @param uid
	 * @param type
	 *            1 每日 0 新手
	 * @return
	 */
	public String getUserAcceptedTask(String uid, TaskFor taskFor) {
		String str = "";
		ShardedJedisPool shardPool = RedisBucket.getInstance().getShardPool(RedisContant.RedisNameOther);
		ShardedJedis redis = null;
		try {
			redis = shardPool.getResource();
			str = redis.hget(
					taskFor == TaskFor.Daily ? RedisContant.TaskAcceptedDaily : RedisContant.TaskAcceptedNewBie, uid);
		} catch (Exception e) {
			logger.info("redis 端口名" + RedisContant.RedisNameOther + " 数据库连接活跃数--<" + shardPool.getNumActive()
					+ ">--空闲连接数--<" + shardPool.getNumIdle() + ">--等待连接数--<" + shardPool.getNumWaiters() + ">");
			logger.error("<getUserAcceptedTask->Exception>" + e.toString());
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
		if (StringUtils.isNotEmpty(str)) {
			return str;
		} else {
			return "";
		}
	}

	/**
	 * 获取用户可以接受的任务
	 * 
	 * @param uid
	 * @param type
	 *            1 每日 0 新手
	 * @return
	 */
	public boolean updateUserAcceptedTask(String uid, TaskFor taskFor, String taskStringfy) {
		ShardedJedisPool shardPool = RedisBucket.getInstance().getShardPool(RedisContant.RedisNameOther);
		ShardedJedis redis = null;
		try {
			redis = shardPool.getResource();

			// logger.error("checkBindPhoneTaks removeSomeTaskFromAccepted in changed " + (taskFor == TaskFor.Daily ?
			// RedisContant.TaskAcceptedDaily : RedisContant.TaskAcceptedNewBie) + " " + uid+ " " + taskStringfy);

			redis.hset(taskFor == TaskFor.Daily ? RedisContant.TaskAcceptedDaily : RedisContant.TaskAcceptedNewBie, uid,
					taskStringfy);
		} catch (Exception e) {
			logger.info("redis 端口名" + RedisContant.RedisNameOther + " 数据库连接活跃数--<" + shardPool.getNumActive()
					+ ">--空闲连接数--<" + shardPool.getNumIdle() + ">--等待连接数--<" + shardPool.getNumWaiters() + ">");
			logger.error("<getUserAcceptedTask->Exception>" + e.toString());
			return false;
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
		return true;
	}

	/**
	 * 是否已经完成新手任务
	 * 
	 * @param uid
	 *            用户
	 * @param money
	 *            奖励的货币
	 * @return
	 */
	public boolean updateFinishedNewBieTask(String uid, int money) {
		ShardedJedisPool shardPool = RedisBucket.getInstance().getShardPool(RedisContant.RedisNameOther);
		ShardedJedis redis = null;
		try {
			redis = shardPool.getResource();
			redis.hset(RedisContant.TaskHasFinishedNewBieTask, String.valueOf(uid), String.valueOf(money));
			return true;
		} catch (Exception e) {
			logger.info("redis 端口名" + RedisContant.RedisNameOther + " 数据库连接活跃数--<" + shardPool.getNumActive()
					+ ">--空闲连接数--<" + shardPool.getNumIdle() + ">--等待连接数--<" + shardPool.getNumWaiters() + ">");
			logger.error("<updateFinishedNewBieTask->Exception>" + e.toString());
			return false;
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
	}

	/**
	 * 是否已经完成新手任务
	 * 
	 * @param uid
	 *            用户
	 * @param money
	 *            奖励的货币
	 * @return
	 */
	public boolean hasFinishedNewBieTask(String uid) {
		ShardedJedisPool shardPool = RedisBucket.getInstance().getShardPool(RedisContant.RedisNameOther);
		ShardedJedis redis = null;
		try {
			redis = shardPool.getResource();
			return redis.hexists(RedisContant.TaskHasFinishedNewBieTask, String.valueOf(uid));
		} catch (Exception e) {
			logger.info("redis 端口名" + RedisContant.RedisNameOther + " 数据库连接活跃数--<" + shardPool.getNumActive()
					+ ">--空闲连接数--<" + shardPool.getNumIdle() + ">--等待连接数--<" + shardPool.getNumWaiters() + ">");
			logger.error("<hasFinishedNewBieTask->Exception>" + e.toString());
			return false;
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
	}

	/**
	 * 添加用户已完成但尚未领取奖励的任务
	 * 
	 * @param uid
	 * @param type
	 *            0 每日 1 新手
	 * @return
	 */
	public boolean updateUserFinishedTask(String uid, TaskConfigLib.TaskFor taskFor, String taskDesc) {
		ShardedJedisPool shardPool = RedisBucket.getInstance().getShardPool(RedisContant.RedisNameOther);
		ShardedJedis redis = null;
		try {
			redis = shardPool.getResource();
			redis.hset(taskFor == TaskFor.Daily ? RedisContant.TaskFinishedDaily : RedisContant.TaskFinishedNewBie,
					String.valueOf(uid), taskDesc);
			return true;
		} catch (Exception e) {
			logger.info("redis 端口名" + RedisContant.RedisNameOther + " 数据库连接活跃数--<" + shardPool.getNumActive()
					+ ">--空闲连接数--<" + shardPool.getNumIdle() + ">--等待连接数--<" + shardPool.getNumWaiters() + ">");
			logger.error("<appendUserFinishedTask->Exception>" + e.toString());
			return false;
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
	}

	/**
	 * 礼物改变 就调用该方法
	 */
	public void setGiftVer() {
		ShardedJedisPool shardPool = RedisBucket.getInstance().getShardPool(RedisContant.RedisNameOther);
		ShardedJedis redis = null;
		try {
			redis = shardPool.getResource();
			redis.incr(RedisContant.giftver);
		} catch (Exception e) {
			logger.info("redis 端口名" + RedisContant.RedisNameOther + " 数据库连接活跃数--<" + shardPool.getNumActive()
					+ ">--空闲连接数--<" + shardPool.getNumIdle() + ">--等待连接数--<" + shardPool.getNumWaiters() + ">");
			logger.error("<setGiftVer->Exception>" + e.toString());
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
	}

	/**
	 * 获取礼物改变的版本
	 */
	public int getGiftVer() {
		String string = "";
		ShardedJedisPool shardPool = RedisBucket.getInstance().getShardPool(RedisContant.RedisNameOther);
		ShardedJedis redis = null;
		try {
			redis = shardPool.getResource();
			string = redis.get(RedisContant.giftver);
			if(string == null) {
				redis.incr(RedisContant.giftver);
				return getGiftVer();
			}
		} catch (Exception e) {
			logger.info("redis 端口名" + RedisContant.RedisNameOther + " 数据库连接活跃数--<" + shardPool.getNumActive()
					+ ">--空闲连接数--<" + shardPool.getNumIdle() + ">--等待连接数--<" + shardPool.getNumWaiters() + ">");
			logger.error("<setGiftVer->Exception>" + e.toString());
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
		return Integer.valueOf(string);
	}

	/**
	 * 添加用户已领取奖励的任务
	 * 
	 * @param uid
	 * @param type
	 *            0 每日 1 新手
	 * @return
	 */
	public boolean appendUserCommitedTask(String uid, TaskConfigLib.TaskFor taskFor, String taskId) {
		ShardedJedisPool shardPool = RedisBucket.getInstance().getShardPool(RedisContant.RedisNameOther);
		ShardedJedis redis = null;
		try {
			redis = shardPool.getResource();
			redis.hset(taskFor == TaskFor.Daily ? RedisContant.TaskCommitedDaily : RedisContant.TaskCommitedNewBie,
					String.valueOf(uid), taskId);
			return true;
		} catch (Exception e) {
			logger.info("redis 端口名" + RedisContant.RedisNameOther + " 数据库连接活跃数--<" + shardPool.getNumActive()
					+ ">--空闲连接数--<" + shardPool.getNumIdle() + ">--等待连接数--<" + shardPool.getNumWaiters() + ">");
			logger.error("<appendUserCommitedTask->Exception>" + e.toString());
			return false;
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
	}

	/**
	 * 获取uid对应第三方流地址
	 * 
	 * @param uid
	 * @return
	 */
	public Map<String, String> getThirdStream() {
		ShardedJedisPool shardPool = RedisBucket.getInstance().getShardPool(RedisContant.RedisNameOther);
		ShardedJedis redis = null;
		Map<String, String> data = null;
		try {
			redis = shardPool.getResource();
			data = redis.hgetAll(RedisContant.keyUsrStream);
		} catch (Exception e) {
			logger.info("redis 端口名" + RedisContant.RedisNameOther + " 数据库连接活跃数--<" + shardPool.getNumActive()
					+ ">--空闲连接数--<" + shardPool.getNumIdle() + ">--等待连接数--<" + shardPool.getNumWaiters() + ">");
			logger.error("<getThirdStream->Exception>" + e.toString());
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
		return data;
	}

	/**
	 * 设置用户是否领取过新手任务
	 * 
	 * @param uidStringfy
	 * @param count
	 *            新手任务数量
	 */
	public void updateUserAcceptedTaskFlagForNewbie(String uidStringfy, int count) {
		ShardedJedisPool shardPool = RedisBucket.getInstance().getShardPool(RedisContant.RedisNameOther);
		ShardedJedis redis = null;
		try {
			redis = shardPool.getResource();
			redis.hset(RedisContant.TaskHasAcceptedNewBieTask, uidStringfy, String.valueOf(count));
		} catch (Exception e) {
			logger.info("redis 端口名" + RedisContant.RedisNameOther + " 数据库连接活跃数--<" + shardPool.getNumActive()
					+ ">--空闲连接数--<" + shardPool.getNumIdle() + ">--等待连接数--<" + shardPool.getNumWaiters() + ">");
			logger.error("<updateUserAcceptedTaskFlagForNewbie->Exception>" + e.toString());
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
	}

	/**
	 * 设置用户是否领取过新手任务
	 * 
	 * @param uidStringfy
	 */
	public boolean hasAcceptedTaskFlagForNewbie(String uidStringfy) {
		ShardedJedisPool shardPool = RedisBucket.getInstance().getShardPool(RedisContant.RedisNameOther);
		ShardedJedis redis = null;
		try {
			redis = shardPool.getResource();
			return redis.hexists(RedisContant.TaskHasAcceptedNewBieTask, uidStringfy);
		} catch (Exception e) {
			logger.info("redis 端口名" + RedisContant.RedisNameOther + " 数据库连接活跃数--<" + shardPool.getNumActive()
					+ ">--空闲连接数--<" + shardPool.getNumIdle() + ">--等待连接数--<" + shardPool.getNumWaiters() + ">");
			logger.error("<hasAcceptedTaskFlagForNewbie->Exception>" + e.toString());
			return false;
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
	}

	/**
	 * 获取指定月份的补签信息
	 * 
	 * @param uid
	 * @param dateKey
	 */
	public int getReSignInfoByMonth(String uidStringfy, String dateKey) {
		ShardedJedisPool shardPool = RedisBucket.getInstance().getShardPool(RedisContant.RedisNameOther);
		ShardedJedis redis = null;
		try {
			redis = shardPool.getResource();
			String storeKey = String.format("%s:%s", RedisContant.TaskReSignByMonth, dateKey);
			String hget = redis.hget(storeKey, uidStringfy);
			if (StringUtils.isEmpty(hget)) {
				setReSignInfoByMonth(uidStringfy, dateKey, 0);
				return 0;
			}
			return Integer.parseInt(hget);
		} catch (Exception e) {
			logger.info("redis 端口名" + RedisContant.RedisNameOther + " 数据库连接活跃数--<" + shardPool.getNumActive()
					+ ">--空闲连接数--<" + shardPool.getNumIdle() + ">--等待连接数--<" + shardPool.getNumWaiters() + ">");
			logger.error("<getSignSummaryInfoByMonth->Exception>" + e.toString());
			return -1;
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
	}

	/**
	 * 指定月份的补签信息
	 * 
	 * @param uid
	 * @param dateKey
	 */
	public void setReSignInfoByMonth(String uidStringfy, String dateKey, int stat) {
		ShardedJedisPool shardPool = RedisBucket.getInstance().getShardPool(RedisContant.RedisNameOther);
		ShardedJedis redis = null;
		try {
			redis = shardPool.getResource();
			String storeKey = String.format("%s:%s", RedisContant.TaskReSignByMonth, dateKey);
			redis.hset(storeKey, uidStringfy, String.valueOf(stat));
		} catch (Exception e) {
			logger.info("redis 端口名" + RedisContant.RedisNameOther + " 数据库连接活跃数--<" + shardPool.getNumActive()
					+ ">--空闲连接数--<" + shardPool.getNumIdle() + ">--等待连接数--<" + shardPool.getNumWaiters() + ">");
			logger.error("<setSignSummaryInfoByMonth->Exception>" + e.toString());
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
	}

	/**
	 * 获取指定月份的签到信息
	 * 
	 * @param uid
	 * @param dateKey
	 */
	public int getSignSummaryInfoByMonth(String uidStringfy, String dateKey) {
		ShardedJedisPool shardPool = RedisBucket.getInstance().getShardPool(RedisContant.RedisNameOther);
		ShardedJedis redis = null;
		try {
			redis = shardPool.getResource();
			String storeKey = String.format("%s:%s", RedisContant.TaskSignByMonth, dateKey);
			String hget = redis.hget(storeKey, uidStringfy);
			if (StringUtils.isEmpty(hget)) {
				setSignSummaryInfoByMonth(uidStringfy, dateKey, 0);
				return 0;
			}
			return Integer.parseInt(hget);
		} catch (Exception e) {
			logger.info("redis 端口名" + RedisContant.RedisNameOther + " 数据库连接活跃数--<" + shardPool.getNumActive()
					+ ">--空闲连接数--<" + shardPool.getNumIdle() + ">--等待连接数--<" + shardPool.getNumWaiters() + ">");
			logger.error("<getSignSummaryInfoByMonth->Exception>" + e.toString());
			return -1;
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
	}

	/**
	 * 获取指定月份的签到信息
	 * 
	 * @param uid
	 * @param dateKey
	 */
	public void setSignSummaryInfoByMonth(String uidStringfy, String dateKey, int stat) {
		ShardedJedisPool shardPool = RedisBucket.getInstance().getShardPool(RedisContant.RedisNameOther);
		ShardedJedis redis = null;
		try {
			redis = shardPool.getResource();
			String storeKey = String.format("%s:%s", RedisContant.TaskSignByMonth, dateKey);
			redis.hset(storeKey, uidStringfy, String.valueOf(stat));
		} catch (Exception e) {
			logger.info("redis 端口名" + RedisContant.RedisNameOther + " 数据库连接活跃数--<" + shardPool.getNumActive()
					+ ">--空闲连接数--<" + shardPool.getNumIdle() + ">--等待连接数--<" + shardPool.getNumWaiters() + ">");
			logger.error("<setSignSummaryInfoByMonth->Exception>" + e.toString());
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
	}

	/**
	 * 获取指定月份的签到信息
	 * 
	 * @param uid
	 */
	public String getSignSummaryInfoByUser(String uidStringfy) {
		ShardedJedisPool shardPool = RedisBucket.getInstance().getShardPool(RedisContant.RedisNameOther);
		ShardedJedis redis = null;
		try {
			redis = shardPool.getResource();
			String hget = redis.hget(RedisContant.TaskSignByUser, uidStringfy);
			if (StringUtils.isEmpty(hget)) {
				// 第一次初始化
				setSignSummaryInfoByUser(uidStringfy, 0, -1);
				return "0#-1";
			}
			return hget;
		} catch (Exception e) {
			logger.info("redis 端口名" + RedisContant.RedisNameOther + " 数据库连接活跃数--<" + shardPool.getNumActive()
					+ ">--空闲连接数--<" + shardPool.getNumIdle() + ">--等待连接数--<" + shardPool.getNumWaiters() + ">");
			logger.error("<getSignSummaryInfoByUser->Exception>" + e.toString());
			return null;
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
	}

	/**
	 * 设置签到信息
	 * 
	 * @param uid
	 * @param continous
	 *            连续值
	 */
	public void setSignSummaryInfoByUser(String uidStringfy, int continous, long takeLastTs) {
		ShardedJedisPool shardPool = RedisBucket.getInstance().getShardPool(RedisContant.RedisNameOther);
		ShardedJedis redis = null;
		try {
			redis = shardPool.getResource();
			redis.hset(RedisContant.TaskSignByUser, uidStringfy, String.valueOf(continous) + "#" + takeLastTs);
		} catch (Exception e) {
			logger.info("redis 端口名" + RedisContant.RedisNameOther + " 数据库连接活跃数--<" + shardPool.getNumActive()
					+ ">--空闲连接数--<" + shardPool.getNumIdle() + ">--等待连接数--<" + shardPool.getNumWaiters() + ">");
			logger.error("<setSignSummaryInfoByUser->Exception>" + e.toString());
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
	}

	public Long setHomeBanner(int bannerid, String josnBannerModel) {

		ShardedJedisPool shardPool = RedisBucket.getInstance().getShardPool(RedisContant.RedisNameOther);
		ShardedJedis redis = null;
		Long lg = 0L;
		try {
			redis = shardPool.getResource();
			lg = redis.hset(RedisContant.homeBanner, String.valueOf(bannerid), josnBannerModel);
		} catch (Exception e) {
			logger.info("redis 端口名" + RedisContant.RedisNameOther + " 数据库连接活跃数--<" + shardPool.getNumActive()
					+ ">--空闲连接数--<" + shardPool.getNumIdle() + ">--等待连接数--<" + shardPool.getNumWaiters() + ">");
			logger.error("<setHomeBanner->Exception>" + e.toString());
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
		return lg;
	}

	/**
	 * 删除轮播图
	 * 
	 * @param bannerid
	 * @return
	 */
	public Long delHomeBanner(int bannerid) {

		ShardedJedisPool shardPool = RedisBucket.getInstance().getShardPool(RedisContant.RedisNameOther);
		ShardedJedis redis = null;
		Long lg = 0L;
		try {
			redis = shardPool.getResource();
			lg = redis.hdel(RedisContant.homeBanner, String.valueOf(bannerid));
		} catch (Exception e) {
			logger.info("redis 端口名" + RedisContant.RedisNameOther + " 数据库连接活跃数--<" + shardPool.getNumActive()
					+ ">--空闲连接数--<" + shardPool.getNumIdle() + ">--等待连接数--<" + shardPool.getNumWaiters() + ">");
			logger.error("<setHomeBanner->Exception>" + e.toString());
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
		return lg;
	}

	/**
	 * 获取轮播图列表
	 * 
	 * @return
	 */
	public Map<String, String> getHomeBannerList() {

		ShardedJedisPool shardPool = RedisBucket.getInstance().getShardPool(RedisContant.RedisNameOther);
		ShardedJedis redis = null;
		Map<String, String> hgetAll = null;
		try {
			redis = shardPool.getResource();
			hgetAll = redis.hgetAll(RedisContant.homeBanner);
		} catch (Exception e) {
			logger.info("redis 端口名" + RedisContant.RedisNameOther + " 数据库连接活跃数--<" + shardPool.getNumActive()
					+ ">--空闲连接数--<" + shardPool.getNumIdle() + ">--等待连接数--<" + shardPool.getNumWaiters() + ">");
			logger.error("<setHomeBanner->Exception>" + e.toString());
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
		return hgetAll;
	}

	/**
	 * gifUserKey 用户和礼物key nSec 过期时间 返回 礼物id
	 */
	public boolean updateGiftTime(String gid, String srcuid, int nSec) {
		boolean brt = false;
		ShardedJedisPool shardPool = RedisBucket.getInstance().getShardPool(RedisContant.RedisNameOther);
		ShardedJedis redis = null;
		try {
			redis = shardPool.getResource();
			String strRt = redis.get(RedisContant.ortherUserGiftEx + srcuid);
			if (null == strRt) {
				strRt = redis.setex(RedisContant.ortherUserGiftEx + srcuid, nSec, gid);
				// if(null != strRt){
				brt = true;
				// }
			}
		} catch (Exception e) {
			logger.info("redis 端口名" + RedisContant.RedisNameOther + " 数据库连接活跃数--<" + shardPool.getNumActive()
					+ ">--空闲连接数--<" + shardPool.getNumIdle() + ">--等待连接数--<" + shardPool.getNumWaiters() + ">");
			logger.error("<getNcode->Exception>" + e.toString());
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
		return brt;
	}

	/**
	 * 设置微信分享的accessToken
	 * 
	 * @param accessToken
	 * @param expires
	 */
	public void setAccessToken(String strKey, String accessToken, int expires) {

		ShardedJedisPool shardPool = RedisBucket.getInstance().getShardPool(RedisContant.RedisNameOther);
		ShardedJedis redis = null;
		try {
			redis = shardPool.getResource();
			redis.hset(RedisContant.weixinShareAccessToken, strKey, accessToken);
			redis.expire(RedisContant.weixinShareAccessToken, expires);
		} catch (Exception e) {
			logger.info("redis 端口名" + RedisContant.RedisNameOther + " 数据库连接活跃数--<" + shardPool.getNumActive()
					+ ">--空闲连接数--<" + shardPool.getNumIdle() + ">--等待连接数--<" + shardPool.getNumWaiters() + ">");
			logger.error("<getNcode->Exception>" + e.toString());
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
	}

	/**
	 * 获取微信分享的accessToken
	 * @param accessToken
	 * @return
	 */
	public String getAccessToken(String strKey) {

		ShardedJedisPool shardPool = RedisBucket.getInstance().getShardPool(RedisContant.RedisNameOther);
		ShardedJedis redis = null;
		String token = null;
		try {
			redis = shardPool.getResource();
			token = redis.hget(RedisContant.weixinShareAccessToken, strKey);
		} catch (Exception e) {
			logger.info("redis 端口名" + RedisContant.RedisNameOther + " 数据库连接活跃数--<" + shardPool.getNumActive()
					+ ">--空闲连接数--<" + shardPool.getNumIdle() + ">--等待连接数--<" + shardPool.getNumWaiters() + ">");
			logger.error("<getNcode->Exception>" + e.toString());
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
		return token;
	}

	/**
	 * 设置微信分享的accessToken
	 * 
	 * @param accessToken
	 * @param expires
	 */
	public void setJsapiTicket(String strKey, String jsapi_ticket, int expires) {

		ShardedJedisPool shardPool = RedisBucket.getInstance().getShardPool(RedisContant.RedisNameOther);
		ShardedJedis redis = null;
		try {
			redis = shardPool.getResource();
			redis.hset(RedisContant.weixinShareJsapiTicket, strKey, jsapi_ticket);
			redis.expire(RedisContant.weixinShareJsapiTicket, expires);
		} catch (Exception e) {
			logger.info("redis 端口名" + RedisContant.RedisNameOther + " 数据库连接活跃数--<" + shardPool.getNumActive()
					+ ">--空闲连接数--<" + shardPool.getNumIdle() + ">--等待连接数--<" + shardPool.getNumWaiters() + ">");
			logger.error("<getNcode->Exception>" + e.toString());
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
	}

	/**
	 * 获取微信分享的accessToken
	 * @param accessToken
	 * @return
	 */
	public String getJsapiTicket(String strKey) {

		ShardedJedisPool shardPool = RedisBucket.getInstance().getShardPool(RedisContant.RedisNameOther);
		ShardedJedis redis = null;
		String token = null;
		try {
			redis = shardPool.getResource();
			token = redis.hget(RedisContant.weixinShareJsapiTicket, strKey);
		} catch (Exception e) {
			logger.info("redis 端口名" + RedisContant.RedisNameOther + " 数据库连接活跃数--<" + shardPool.getNumActive()
					+ ">--空闲连接数--<" + shardPool.getNumIdle() + ">--等待连接数--<" + shardPool.getNumWaiters() + ">");
			logger.error("<getNcode->Exception>" + e.toString());
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
		return token;
	}

	/**
	 * 邀请连麦 mic
	 * 
	 * @param 邀请方		srcuid
	 * @param 被邀请方	dstuid
	 * 
	 * @return null failed
	 */
	public String setInviteMic(int srcuid, int dstuid) {
		ShardedJedisPool shardPool = null;
		ShardedJedis redis = null;
		String str = null;

		try {
			shardPool = RedisBucket.getInstance().getShardPool(RedisContant.RedisNameOther);
			redis = shardPool.getResource();
			str = redis.set(RedisContant.ortherLiveInvite + srcuid, "" + dstuid);
		} catch (Exception e) {
			logger.error("<setInviteMic->Exception>" + e.toString());
		} finally {
			if (redis != null) {
				redis.close();
			}
		}

		return str;
	}

	/**
	 * 获取邀请
	 * 
	 * @param 邀请方		srcuid
	 */
	public String getInviteMic(int srcuid) {
		String str = null;

		ShardedJedisPool shardPool = null;
		ShardedJedis redis = null;
		try {
			shardPool = RedisBucket.getInstance().getShardPool(RedisContant.RedisNameOther);
			redis = shardPool.getResource();
			str = redis.get(RedisContant.ortherLiveInvite + srcuid);
		} catch (Exception e) {
			logger.error("<getInviteMic->Exception>" + e.toString());
		} finally {
			if (redis != null) {
				redis.close();
			}
		}

		return str;
	}

	/**
	 * 删除邀请
	 * 
	 * @param 邀请方		srcuid
	 */
	public void delInviteMic(int srcuid) {
		ShardedJedisPool shardPool = null;
		ShardedJedis redis = null;
		try {
			shardPool = RedisBucket.getInstance().getShardPool(RedisContant.RedisNameOther);
			redis = shardPool.getResource();
			redis.del(RedisContant.ortherLiveInvite + srcuid);
		} catch (Exception e) {
			logger.error("<delInviteMic->Exception>" + e.toString());
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
	}

	/**
	 * set
	 * @param redisName redis的端口号限定
	 * @param strKey 
	 * @param field
	 * @param value
	 * @param expires 大于0 则有时效
	 */
	public Long hset(String redisName, String strKey, String field, String value, int expires) {

		ShardedJedisPool shardPool = null;
		ShardedJedis redis = null;
		Long hset = null;
		try {
			shardPool = RedisBucket.getInstance().getShardPool(redisName);
			redis = shardPool.getResource();
			hset = redis.hset(strKey, field, value);
			if (expires > 0) {
				redis.expire(strKey, expires);
			}
		} catch (Exception e) {
			logger.info("redis 端口名" + redisName + " 数据库连接活跃数--<" + shardPool.getNumActive() + ">--空闲连接数--<"
					+ shardPool.getNumIdle() + ">--等待连接数--<" + shardPool.getNumWaiters() + ">");
			logger.error("<hset->Exception>", e);
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
		return hset;
	}

	public String set(String redisName, String key, String value, int expires) {
		ShardedJedisPool shardPool = null;
		ShardedJedis redis = null;
		String set = null;
		try {
			shardPool = RedisBucket.getInstance().getShardPool(redisName);
			redis = shardPool.getResource();
			set = redis.set(key, value);
			if (expires > 0) {
				redis.expire(key, expires);
			}

		} catch (Exception e) {
			logger.info("redis 端口名" + redisName + " 数据库连接活跃数--<" + shardPool.getNumActive() + ">--空闲连接数--<"
					+ shardPool.getNumIdle() + ">--等待连接数--<" + shardPool.getNumWaiters() + ">");
			logger.error("<set->Exception>", e);
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
		return set;
	}

	public String get(String redisName, String key) {
		ShardedJedisPool shardPool = null;
		ShardedJedis redis = null;
		String get = null;
		try {
			shardPool = RedisBucket.getInstance().getShardPool(redisName);
			redis = shardPool.getResource();
			get = redis.get(key);

		} catch (Exception e) {
			logger.info("redis 端口名" + redisName + " 数据库连接活跃数--<" + shardPool.getNumActive() + ">--空闲连接数--<"
					+ shardPool.getNumIdle() + ">--等待连接数--<" + shardPool.getNumWaiters() + ">");
			logger.error("<get->Exception>", e);
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
		return get;
	}

	/**
	 * hget
	 * @param redisName redis的端口号限定
	 * @param strKey 
	 * @param field
	 */
	public String hget(String redisName, String strKey, String field) {

		ShardedJedisPool shardPool = null;
		ShardedJedis redis = null;
		String hget = null;
		try {
			shardPool = RedisBucket.getInstance().getShardPool(redisName);
			redis = shardPool.getResource();
			hget = redis.hget(strKey, field);

		} catch (Exception e) {
			logger.info("redis 端口名" + redisName + " 数据库连接活跃数--<" + shardPool.getNumActive() + ">--空闲连接数--<"
					+ shardPool.getNumIdle() + ">--等待连接数--<" + shardPool.getNumWaiters() + ">");
			logger.error("<hget->Exception>", e);
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
		return hget;
	}

	public Long hincrBy(String redisName, String strKey, String field, int value, int expires) {

		ShardedJedisPool shardPool = null;
		ShardedJedis redis = null;
		Long hset = null;
		try {
			shardPool = RedisBucket.getInstance().getShardPool(redisName);
			redis = shardPool.getResource();
			hset = redis.hincrBy(strKey, field, value);
			if (hset != null && hset == value && expires > 0) {
				redis.expire(strKey, expires);
			}
		} catch (Exception e) {
			logger.info("redis 端口名" + redisName + " 数据库连接活跃数--<" + shardPool.getNumActive() + ">--空闲连接数--<"
					+ shardPool.getNumIdle() + ">--等待连接数--<" + shardPool.getNumWaiters() + ">");
			logger.error("<hincrBy->Exception>", e);
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
		return hset;
	}

	public Long zadd(String redisName, String strKey, String member, int score, int expires) {

		ShardedJedisPool shardPool = null;
		ShardedJedis redis = null;
		Long zadd = null;
		try {
			shardPool = RedisBucket.getInstance().getShardPool(redisName);
			redis = shardPool.getResource();
			zadd = redis.zadd(strKey, score, member);
			if (zadd != null && zadd == 1 && expires > 0) {
				redis.expire(strKey, expires);
			}
		} catch (Exception e) {
			logger.info("redis 端口名" + redisName + " 数据库连接活跃数--<" + shardPool.getNumActive() + ">--空闲连接数--<"
					+ shardPool.getNumIdle() + ">--等待连接数--<" + shardPool.getNumWaiters() + ">");
			logger.error("<zadd->Exception>", e);
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
		return zadd;
	}

	public Double zscore(String redisName, String strKey, String member) {
		ShardedJedisPool shardPool = null;
		ShardedJedis redis = null;
		Double zscore = null;
		try {
			shardPool = RedisBucket.getInstance().getShardPool(redisName);
			redis = shardPool.getResource();
			zscore = redis.zscore(strKey, member);
		} catch (Exception e) {
			logger.info("redis 端口名" + redisName + " 数据库连接活跃数--<" + shardPool.getNumActive() + ">--空闲连接数--<"
					+ shardPool.getNumIdle() + ">--等待连接数--<" + shardPool.getNumWaiters() + ">");
			logger.error("<zscore->Exception>", e);
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
		return zscore;
	}
	
	public Long zrem(String redisName, String strKey, String ...members) {
		ShardedJedisPool shardPool = null;
		ShardedJedis redis = null;
		try {
			shardPool = RedisBucket.getInstance().getShardPool(redisName);
			redis = shardPool.getResource();
			return redis.zrem(strKey, members);
		} catch (Exception e) {
			logger.info("redis 端口名" + redisName + " 数据库连接活跃数--<" + shardPool.getNumActive() + ">--空闲连接数--<"
					+ shardPool.getNumIdle() + ">--等待连接数--<" + shardPool.getNumWaiters() + ">");
			logger.error("<srem->Exception>", e);
			return null;
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
	}
	
	public Long srem(String redisName, String strKey, String ...members) {
		ShardedJedisPool shardPool = null;
		ShardedJedis redis = null;
		try {
			shardPool = RedisBucket.getInstance().getShardPool(redisName);
			redis = shardPool.getResource();
			return redis.srem(strKey, members);
		} catch (Exception e) {
			logger.info("redis 端口名" + redisName + " 数据库连接活跃数--<" + shardPool.getNumActive() + ">--空闲连接数--<"
					+ shardPool.getNumIdle() + ">--等待连接数--<" + shardPool.getNumWaiters() + ">");
			logger.error("<srem->Exception>", e);
			return null;
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
	}
		
	public Long zrevrank(String redisName, String strKey, String member) {
		ShardedJedisPool shardPool = null;
		ShardedJedis redis = null;
		Long zrevrank = null;
		try {
			shardPool = RedisBucket.getInstance().getShardPool(redisName);
			redis = shardPool.getResource();
			zrevrank = redis.zrevrank(strKey, member);
		} catch (Exception e) {
			logger.info("redis 端口名" + redisName + " 数据库连接活跃数--<" + shardPool.getNumActive() + ">--空闲连接数--<"
					+ shardPool.getNumIdle() + ">--等待连接数--<" + shardPool.getNumWaiters() + ">");
			logger.error("<zrevrank->Exception>", e);
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
		return zrevrank;
	}

	public Set<Tuple> zrevrangeWithScores(String redisName, String key, int page, int size) {

		ShardedJedisPool shardPool = null;
		ShardedJedis redis = null;
		Set<Tuple> zrankWithScores = null;
		try {
			shardPool = RedisBucket.getInstance().getShardPool(redisName);
			redis = shardPool.getResource();
			zrankWithScores = redis.zrevrangeWithScores(key, (page - 1) * size, page * size - 1);
		} catch (Exception e) {
			logger.info("redis 端口名" + redisName + " 数据库连接活跃数--<" + shardPool.getNumActive() + ">--空闲连接数--<"
					+ shardPool.getNumIdle() + ">--等待连接数--<" + shardPool.getNumWaiters() + ">");
			logger.error("<zrevrangeWithScores->Exception>", e);
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
		return zrankWithScores;
	}

	public Set<Tuple> zrangeWithScores(String redisName, String key, int page, int size) {

		ShardedJedisPool shardPool = null;
		ShardedJedis redis = null;
		Set<Tuple> zrankWithScores = null;
		try {
			shardPool = RedisBucket.getInstance().getShardPool(redisName);
			redis = shardPool.getResource();
			zrankWithScores = redis.zrangeWithScores(key, (page - 1) * size, page * size - 1);
		} catch (Exception e) {
			logger.info("redis 端口名" + redisName + " 数据库连接活跃数--<" + shardPool.getNumActive() + ">--空闲连接数--<"
					+ shardPool.getNumIdle() + ">--等待连接数--<" + shardPool.getNumWaiters() + ">");
			logger.error("<zrevrangeWithScores->Exception>", e);
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
		return zrankWithScores;
	}

	public Double zincrby(String redisName, String strKey, String member, int score, int expires) {

		ShardedJedisPool shardPool = null;
		ShardedJedis redis = null;
		Double zincrby = null;
		try {
			shardPool = RedisBucket.getInstance().getShardPool(redisName);
			redis = shardPool.getResource();
			zincrby = redis.zincrby(strKey, score, member);
			if (zincrby != null && zincrby == 1 && expires > 0) {
				redis.expire(strKey, expires);
			}
		} catch (Exception e) {
			logger.info("redis 端口名" + redisName + " 数据库连接活跃数--<" + shardPool.getNumActive() + ">--空闲连接数--<"
					+ shardPool.getNumIdle() + ">--等待连接数--<" + shardPool.getNumWaiters() + ">");
			logger.error("<zincrby->Exception>", e);
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
		return zincrby;
	}

	/**
	 * 第二个流的UID
	 * 
	 * @param roomid	房间id
	 * @param uid2nd	第二个id
	 * 
	 * @return null failed
	 */
	public String setLive2ndUid(int roomid, int uid2nd) {
		ShardedJedisPool shardPool = null;
		ShardedJedis redis = null;
		String str = null;
		try {
			shardPool = RedisBucket.getInstance().getShardPool(RedisContant.RedisNameOther);
			redis = shardPool.getResource();
			str = redis.set(RedisContant.ortherLive2ndUid + roomid, "" + uid2nd);
		} catch (Exception e) {
			logger.error("<setLive2ndUid->Exception>" + e.toString());
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
		return str;
	}

	/**
	 * 获取邀请
	 * 
	 * @param	roomid	房间id	
	 */
	public String getLive2ndUid(int roomid) {
		String str = null;

		ShardedJedisPool shardPool = null;
		ShardedJedis redis = null;
		try {
			shardPool = RedisBucket.getInstance().getShardPool(RedisContant.RedisNameOther);
			redis = shardPool.getResource();
			str = redis.get(RedisContant.ortherLive2ndUid + roomid);
		} catch (Exception e) {
			logger.error("<getLive2ndUid->Exception>" + e.toString());
		} finally {
			if (redis != null) {
				redis.close();
			}
		}

		return str;
	}

	/**
	 * 删除邀请
	 * 
	 * @param	roomid	房间id
	 */
	public void delLive2ndUid(int roomid) {
		ShardedJedisPool shardPool = null;
		ShardedJedis redis = null;
		try {
			shardPool = RedisBucket.getInstance().getShardPool(RedisContant.RedisNameOther);
			redis = shardPool.getResource();
			redis.del(RedisContant.ortherLive2ndUid + roomid);
		} catch (Exception e) {
			logger.error("<delLive2ndUid->Exception>" + e.toString());
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
	}

	/**
	 * 删除uid的key
	 * @param uid
	 */
	public void delUidQnKey(String uid) {
		ShardedJedisPool shardPool = RedisBucket.getInstance().getShardPool(RedisContant.RedisNameOther);
		ShardedJedis redis = null;
		try {
			redis = shardPool.getResource();
			redis.hdel(RedisContant.ortherQnLiveKey, uid);
		} catch (Exception e) {
			logger.info("redis 端口名" + RedisContant.RedisNameOther + " 数据库连接活跃数--<" + shardPool.getNumActive()
					+ ">--空闲连接数--<" + shardPool.getNumIdle() + ">--等待连接数--<" + shardPool.getNumWaiters() + ">");
			logger.error("<getNcode->Exception>" + e.toString());
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
	}

	/**
	 * 保存七牛的开播key
	 */
	public Long setUidQnKey(String strUid, String strQnKey) {
		Long rt = null;
		ShardedJedisPool shardPool = RedisBucket.getInstance().getShardPool(RedisContant.RedisNameOther);
		ShardedJedis redis = null;
		try {
			redis = shardPool.getResource();
			rt = redis.hset(RedisContant.ortherQnLiveKey, strUid, strQnKey);
		} catch (Exception e) {
			logger.info("redis 端口名" + RedisContant.RedisNameOther + " 数据库连接活跃数--<" + shardPool.getNumActive()
					+ ">--空闲连接数--<" + shardPool.getNumIdle() + ">--等待连接数--<" + shardPool.getNumWaiters() + ">");
			logger.error("<getNcode->Exception>" + e.toString());
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
		return rt;
	}

	/**
	 * 获取七牛开播的Key
	 */
	public String getUidQnKey(String strUid) {
		String qnKey = null;
		ShardedJedisPool shardPool = RedisBucket.getInstance().getShardPool(RedisContant.RedisNameOther);
		ShardedJedis redis = null;
		try {
			redis = shardPool.getResource();
			qnKey = redis.hget(RedisContant.ortherQnLiveKey, strUid);
		} catch (Exception e) {
			logger.info("redis 端口名" + RedisContant.RedisNameOther + " 数据库连接活跃数--<" + shardPool.getNumActive()
					+ ">--空闲连接数--<" + shardPool.getNumIdle() + ">--等待连接数--<" + shardPool.getNumWaiters() + ">");
			logger.error("<getNcode->Exception>" + e.toString());
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
		return qnKey;
	}

	/*
	 * 甜橙 check
	 */
	public boolean checkTianCheng(int nSec) {
		boolean brt = false;
		ShardedJedisPool shardPool = RedisBucket.getInstance().getShardPool(RedisContant.RedisNameOther);
		ShardedJedis redis = null;
		try {
			redis = shardPool.getResource();
			String strRt = redis.get(RedisContant.ortherTianChengCheckEx);
			if (null == strRt) {
				strRt = redis.setex(RedisContant.ortherTianChengCheckEx, nSec, RedisContant.ortherTianChengCheckEx);
				brt = true;
			}
		} catch (Exception e) {
			logger.info("redis 端口名" + RedisContant.RedisNameOther + " 数据库连接活跃数--<" + shardPool.getNumActive()
					+ ">--空闲连接数--<" + shardPool.getNumIdle() + ">--等待连接数--<" + shardPool.getNumWaiters() + ">");
			logger.error("<getNcode->Exception>" + e.toString());
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
		return brt;
	}

	/**
	 * TC删除待发送的redis
	 * @param key
	 * @param hkey
	 * @param value
	 * @return
	 */
	public Long delTCRoomOff(String uid) {
		ShardedJedisPool shardPool = RedisBucket.getInstance().getShardPool(RedisContant.RedisNameOther);
		ShardedJedis redis = null;
		try {
			logger.info("delTCRoomOff  uid=" + uid);
			redis = shardPool.getResource();
			Long zrem = redis.zrem(RedisContant.tcOffRoom, uid);
			// Set<String> zrevrange = redis.zrevrange(RedisContant.tcOffRoom, 0, -1);
			// logger.info("zrevrange-->" + zrevrange);
			return zrem;
		} catch (Exception e) {
			logger.info("redis 端口名" + RedisContant.RedisNameOther + " 数据库连接活跃数--<" + shardPool.getNumActive()
					+ ">--空闲连接数--<" + shardPool.getNumIdle() + ">--等待连接数--<" + shardPool.getNumWaiters() + ">");
			logger.error("<delTCRoomOff->Exception>" + e.toString());
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
		return null;
	}

	/**
	 * 添加tc下线
	 * 
	 * @param uid
	 * @param host
	 * @param rq
	 */
	public void addTcRoomOff(String uid) {
		ShardedJedisPool shardPool = RedisBucket.getInstance().getShardPool(RedisContant.RedisNameOther);
		ShardedJedis redis = null;
		try {
			redis = shardPool.getResource();
			Long zadd = redis.zadd(RedisContant.tcOffRoom, 1, uid);
		} catch (Exception e) {
			logger.info("redis 端口名" + RedisContant.RedisNameOther + " 数据库连接活跃数--<" + shardPool.getNumActive()
					+ ">--空闲连接数--<" + shardPool.getNumIdle() + ">--等待连接数--<" + shardPool.getNumWaiters() + ">");
			logger.error("<addTcRoomOff->Exception>" + e.toString());
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
	}

	/**
	 * 获取tc下线失败
	 * 
	 * @param page
	 * @return
	 */
	public Set<String> getTcRoomOff() {
		ShardedJedisPool shardPool = RedisBucket.getInstance().getShardPool(RedisContant.RedisNameOther);
		ShardedJedis redis = null;
		try {
			redis = shardPool.getResource();
			return redis.zrevrange(RedisContant.tcOffRoom, 0, -1);
		} catch (Exception e) {
			logger.info("redis 端口名" + RedisContant.RedisNameOther + " 数据库连接活跃数--<" + shardPool.getNumActive()
					+ ">--空闲连接数--<" + shardPool.getNumIdle() + ">--等待连接数--<" + shardPool.getNumWaiters() + ">");
			logger.error("<getTcRoomOff->Exception>" + e.toString());
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
		return null;
	}

	public Long hset(String key, String hkey, String value) {

		ShardedJedisPool shardPool = null;
		ShardedJedis redis = null;
		try {
			shardPool = RedisBucket.getInstance().getShardPool(RedisContant.RedisNameOther);
			redis = shardPool.getResource();
			return redis.hset(key, hkey, value);
		} catch (Exception e) {
			logger.error(AdStatisticsRedisService.class.getSimpleName() + " <hset->Exception>" + e.toString());
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
		return null;
	}

	public String hget(String key, String hkey) {

		ShardedJedisPool shardPool = null;
		ShardedJedis redis = null;
		try {
			shardPool = RedisBucket.getInstance().getShardPool(RedisContant.RedisNameOther);
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

	public Map<String, String> hgetAll(String key) {
		ShardedJedisPool shardPool = null;
		ShardedJedis redis = null;
		try {
			shardPool = RedisBucket.getInstance().getShardPool(RedisContant.RedisNameOther);
			redis = shardPool.getResource();
			return redis.hgetAll(key);
		} catch (Exception e) {
			logger.error("<hget->Exception>" + e.toString());
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
		return null;
	}

	/**
	 * 缓存耗时长的请求
	 * @param mobile
	 */
	public void saveCostRequest(CostRequestLog createWith) {
		ShardedJedisPool shardPool = RedisBucket.getInstance().getShardPool(RedisContant.RedisNameOther);
		ShardedJedis redis = null;
		try {
			redis = shardPool.getResource();
			redis.zadd(RedisContant.StatCostKeyPrefix, System.currentTimeMillis(), JSON.toJSON(createWith).toString());
			redis.zremrangeByRank(RedisContant.StatCostKeyPrefix, 0, -1 * PerfFilter.MaxStoreCount);
		} catch (Exception e) {
			logger.info("redis 端口名" + RedisContant.RedisNameOther + " 数据库连接活跃数--<" + shardPool.getNumActive()
					+ ">--空闲连接数--<" + shardPool.getNumIdle() + ">--等待连接数--<" + shardPool.getNumWaiters() + ">");
			logger.error("<saveCostRequest->Exception>" + e.toString());
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
	}

	/**
	 * 获取ex临时key 保存一天
	 * @param valueOf
	 * @return
	 */
	public String getUidQnKeyLimitOneDay(String uid) {
		if (null == uid) {
			logger.error("getUidQnKeyLimitOneDay failed uid null");
			return null;
		}
		String data = null;
		ShardedJedisPool shardPool = RedisBucket.getInstance().getShardPool(RedisContant.RedisNameOther);
		ShardedJedis redis = null;
		try {
			redis = shardPool.getResource();
			data = redis.get(RedisContant.ortherQnLiveKeyOneDay + uid);
		} catch (Exception e) {
			logger.info("redis 端口名" + RedisContant.RedisNameOther + " 数据库连接活跃数--<" + shardPool.getNumActive()
					+ ">--空闲连接数--<" + shardPool.getNumIdle() + ">--等待连接数--<" + shardPool.getNumWaiters() + ">");
			logger.error("<getNcode->Exception>" + e.toString());
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
		return data;
	}

	/**
	 * 保存ex临时key 保存一天
	 * @param valueOf
	 * @param qnKey
	 */
	public void setUidQnKeyLimitOneDay(String uid, String qnKey) {
		if (null == qnKey) {
			logger.error("setUidQnKeyLimitOneDay:key null failed");
			return;
		}

		ShardedJedisPool shardPool = RedisBucket.getInstance().getShardPool(RedisContant.RedisNameOther);
		ShardedJedis redis = null;
		try {
			redis = shardPool.getResource();
			redis.setex(RedisContant.ortherQnLiveKeyOneDay + uid, 60 * 60 * 24, qnKey);
		} catch (Exception e) {
			logger.info("redis 端口名" + RedisContant.RedisNameOther + " 数据库连接活跃数--<" + shardPool.getNumActive()
					+ ">--空闲连接数--<" + shardPool.getNumIdle() + ">--等待连接数--<" + shardPool.getNumWaiters() + ">");
			logger.error("<setNcode->Exception>" + e.toString());
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
	}

	/**
	 * 清理动态的key
	 * @param valueOf
	 */
	public void delUidQnKeyLimitOneDay(String uid) {
		if (null == uid) {
			logger.error("delUidQnKeyLimitOneDay failed uid null");
			return;
		}

		ShardedJedisPool shardPool = RedisBucket.getInstance().getShardPool(RedisContant.RedisNameOther);
		ShardedJedis redis = null;
		try {
			redis = shardPool.getResource();
			redis.del(RedisContant.ortherQnLiveKeyOneDay + uid);
		} catch (Exception e) {
			logger.info("redis 端口名" + RedisContant.RedisNameOther + " 数据库连接活跃数--<" + shardPool.getNumActive()
					+ ">--空闲连接数--<" + shardPool.getNumIdle() + ">--等待连接数--<" + shardPool.getNumWaiters() + ">");
			logger.error("<delReport->Exception>" + e.toString());
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
	}

	// 删除需要录制主播的UID
	public void removefromRecordSet(String uid) {
		if (null == uid) {
			logger.error("removefromRecordSet failed uid null");
			return;
		}
		ShardedJedisPool shardPool = RedisBucket.getInstance().getShardPool(RedisContant.RedisNameOther);
		ShardedJedis redis = null;
		try {
			redis = shardPool.getResource();
			redis.srem(RedisContant.recordUidSet, uid);
		} catch (Exception e) {
			logger.info("redis 端口名" + RedisContant.RedisNameOther + " 数据库连接活跃数--<" + shardPool.getNumActive()
					+ ">--空闲连接数--<" + shardPool.getNumIdle() + ">--等待连接数--<" + shardPool.getNumWaiters() + ">");
			logger.error("<delReport->Exception>" + e.toString());
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
	}

	// 增加需要录制主播的UID
	public Long addtoRecordSet(String uid) {
		if (null == uid) {
			logger.error("addtoRecordSet failed uid null");
			return null;
		}
		ShardedJedisPool shardPool = RedisBucket.getInstance().getShardPool(RedisContant.RedisNameOther);
		ShardedJedis redis = null;
		try {
			redis = shardPool.getResource();
			return redis.sadd(RedisContant.recordUidSet, uid);
		} catch (Exception e) {
			logger.info("redis 端口名" + RedisContant.RedisNameOther + " 数据库连接活跃数--<" + shardPool.getNumActive()
					+ ">--空闲连接数--<" + shardPool.getNumIdle() + ">--等待连接数--<" + shardPool.getNumWaiters() + ">");
			logger.error("<delReport->Exception>" + e.toString());
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
		return null;
	}

	// uid是否需要record
	public Boolean isUidinRecordSet(String uid) {
		if (null == uid) {
			logger.error("addtoRecordSet failed uid null");
			return false;
		}
		ShardedJedisPool shardPool = RedisBucket.getInstance().getShardPool(RedisContant.RedisNameOther);
		ShardedJedis redis = null;
		try {
			redis = shardPool.getResource();
			return redis.sismember(RedisContant.recordUidSet, uid);
		} catch (Exception e) {
			logger.info("redis 端口名" + RedisContant.RedisNameOther + " 数据库连接活跃数--<" + shardPool.getNumActive()
					+ ">--空闲连接数--<" + shardPool.getNumIdle() + ">--等待连接数--<" + shardPool.getNumWaiters() + ">");
			logger.error("<delReport->Exception>" + e.toString());
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
		return false;
	}

	// 删除录像信息
	public void delUidRecordByTime(String uid, String time) {
		if (null == uid || null == time) {
			return;
		}
		ShardedJedisPool shardPool = RedisBucket.getInstance().getShardPool(RedisContant.RedisNameOther);
		ShardedJedis redis = null;
		try {
			redis = shardPool.getResource();
			redis.hdel(RedisContant.UidrecHash + uid, time);
		} catch (Exception e) {
			logger.info("redis 端口名" + RedisContant.RedisNameOther + " 数据库连接活跃数--<" + shardPool.getNumActive()
					+ ">--空闲连接数--<" + shardPool.getNumIdle() + ">--等待连接数--<" + shardPool.getNumWaiters() + ">");
			logger.error("<getNcode->Exception>" + e.toString());
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
	}

	// 增加录像信息
	public Long addUidRecordByTime(String uid, String time, String jsondata) {
		Long rt = null;
		if (null == uid || null == time || null == jsondata) {
			return rt;
		}
		ShardedJedisPool shardPool = RedisBucket.getInstance().getShardPool(RedisContant.RedisNameOther);
		ShardedJedis redis = null;
		try {
			redis = shardPool.getResource();
			rt = redis.hset(RedisContant.UidrecHash + uid, time, jsondata);
		} catch (Exception e) {
			logger.info("redis 端口名" + RedisContant.RedisNameOther + " 数据库连接活跃数--<" + shardPool.getNumActive()
					+ ">--空闲连接数--<" + shardPool.getNumIdle() + ">--等待连接数--<" + shardPool.getNumWaiters() + ">");
			logger.error("<getNcode->Exception>" + e.toString());
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
		return rt;
	}

	// 获取全部信息
	public Map<String, String> getUidRecordAll(String uid) {
		Map<String, String> qnKey = null;
		if (null == uid) {
			return qnKey;
		}

		ShardedJedisPool shardPool = RedisBucket.getInstance().getShardPool(RedisContant.RedisNameOther);
		ShardedJedis redis = null;
		try {
			redis = shardPool.getResource();
			qnKey = redis.hgetAll(RedisContant.UidrecHash + uid);
		} catch (Exception e) {
			logger.info("redis 端口名" + RedisContant.RedisNameOther + " 数据库连接活跃数--<" + shardPool.getNumActive()
					+ ">--空闲连接数--<" + shardPool.getNumIdle() + ">--等待连接数--<" + shardPool.getNumWaiters() + ">");
			logger.error("<getNcode->Exception>" + e.toString());
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
		return qnKey;
	}

	// 获取vip本月补签次数
	public Double getVipReSignCount(int uid, String month) {
		ShardedJedisPool shardPool = null;
		ShardedJedis redis = null;
		try {
			shardPool = RedisBucket.getInstance().getShardPool(RedisContant.RedisNameOther);
			redis = shardPool.getResource();
			return redis.zscore(RedisContant.TaskReSignCountByMonth + month, String.valueOf(uid));
		} catch (Exception e) {
			logger.error("<getDstGetNumber->Exception>" + e.toString());
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
		return null;
	}

	// vip本月补签次数+1
	public Double incrVipReSignCount(int uid, String month) {
		ShardedJedisPool shardPool = null;
		ShardedJedis redis = null;
		try {
			shardPool = RedisBucket.getInstance().getShardPool(RedisContant.RedisNameOther);
			redis = shardPool.getResource();
			return redis.zincrby(RedisContant.TaskReSignCountByMonth + month, 1, String.valueOf(uid));
		} catch (Exception e) {
			logger.error("<getDstGetNumber->Exception>" + e.toString());
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
		return null;
	}

	/**
	 * 记录游戏房间当天的声援榜
	 * 
	 * @param uid
	 * @param credit 要增加的声援值
	 * @param gameId
	 */
	public void addGameRoomCreditThis(int uid, int credit, Long gameId) {
		ShardedJedisPool shardPool = null;
		ShardedJedis redis = null;
		try {
			shardPool = RedisBucket.getInstance().getShardPool(RedisContant.RedisNameOther);
			redis = shardPool.getResource();
			redis.zincrby(RedisContant.gameRoomCredit + gameId, credit, String.valueOf(uid));
		} catch (Exception e) {
			logger.error("<addGameRoomCreditThis->Exception>", e);
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
	}

	/**
	 * 删除游戏房间声援记录
	 * 
	 * @param uid
	 * @param gameId
	 */
	public void delGameRoomCreditThis(int uid, Long gameId) {
		ShardedJedisPool shardPool = null;
		ShardedJedis redis = null;
		try {
			shardPool = RedisBucket.getInstance().getShardPool(RedisContant.RedisNameOther);
			redis = shardPool.getResource();
			redis.zrem(RedisContant.gameRoomCredit + gameId, String.valueOf(uid));
		} catch (Exception e) {
			logger.error("<delGameRoomCreditThis->Exception>", e);
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
	}

	/**
	 * 根据游戏ID获取直播间信息
	 * 
	 * @param uid
	 * @param page
	 * @param gameId
	 * @return
	 */
	public Set<String> getGameRoomListByGameId(int page, Long gameId) {
		ShardedJedisPool shardPool = null;
		ShardedJedis redis = null;
		try {
			shardPool = RedisBucket.getInstance().getShardPool(RedisContant.RedisNameOther);
			redis = shardPool.getResource();
			return redis.zrange(RedisContant.gameRoomCredit + gameId, (page - 1) * pageSiz, page * pageSiz - 1);
		} catch (Exception e) {
			logger.error("<getGameRoomListByGameId->Exception>", e);
			return null;
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
	}

	/**
	 * 最热的游戏直播间(前两个)
	 * 
	 * @param uid
	 * @param page
	 * @param gameId
	 * @return
	 */
	public Set<String> getHotGameAnchorList(Long gameId) {
		ShardedJedisPool shardPool = null;
		ShardedJedis redis = null;
		try {
			shardPool = RedisBucket.getInstance().getShardPool(RedisContant.RedisNameOther);
			redis = shardPool.getResource();
			return redis.zrevrangeByScore(RedisContant.gameRoomCredit + gameId, "+inf", "-inf", 0, 2);
		} catch (Exception e) {
			logger.error("<getHotGameAnchorList->Exception>", e);
			return null;
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
	}

	/**
	 * 记录用户当天进入的直播间（每日任务使用）
	 * 
	 * @param srcUid 用户ID
	 * @param dstUid 主播ID
	 * @return
	 */
	public void userEnterRoomDay(Integer srcUid, Integer dstUid) {
		ShardedJedisPool shardPool = null;
		ShardedJedis redis = null;
		try {
			String day = DateUtils.dateToString(null, "yyyyMMdd");
			shardPool = RedisBucket.getInstance().getShardPool(RedisContant.RedisNameOther);
			redis = shardPool.getResource();
			if (redis.sismember(RedisContant.userEnterRoomDay + day + ":" + srcUid, String.valueOf(dstUid))) {
				return;
			}
			redis.sadd(RedisContant.userEnterRoomDay + day + ":" + srcUid, String.valueOf(dstUid));
			if (redis.scard(RedisContant.userEnterRoomDay + day + ":" + srcUid) == 1) {
				redis.expire(RedisContant.userEnterRoomDay + day + ":" + srcUid, 24 * 60 * 60);
			}
		} catch (Exception e) {
			logger.error("<userEnterRoomDay->Exception>", e);
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
	}

	/**
	 * 判断当天用户是否进入该直播间
	 * 
	 * @param srcUid 用户ID
	 * @param dstUid 主播ID
	 * @return 进入返回 true,否则返回false
	 */
	public boolean havUserEnterRoomDay(Integer srcUid, Integer dstUid) {
		ShardedJedisPool shardPool = null;
		ShardedJedis redis = null;
		try {
			String day = DateUtils.dateToString(null, "yyyyMMdd");
			shardPool = RedisBucket.getInstance().getShardPool(RedisContant.RedisNameOther);
			redis = shardPool.getResource();
			return redis.sismember(RedisContant.userEnterRoomDay + day + ":" + srcUid, String.valueOf(dstUid));
		} catch (Exception e) {
			logger.error("<havUserEnterRoomDay->Exception>", e);
			return false;
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
	}

	/**
	 * 记录用户当天关注的主播（每日任务使用）
	 * 
	 * @param srcUid 用户ID
	 * @param dstUid 主播ID
	 * @return
	 */
	public void userFollowDay(Integer srcUid, Integer dstUid) {
		ShardedJedisPool shardPool = null;
		ShardedJedis redis = null;
		try {
			String day = DateUtils.dateToString(null, "yyyyMMdd");
			shardPool = RedisBucket.getInstance().getShardPool(RedisContant.RedisNameOther);
			redis = shardPool.getResource();
			if (redis.sismember(RedisContant.userFollowDay + day + ":" + srcUid, String.valueOf(dstUid))) {
				return;
			}
			redis.sadd(RedisContant.userFollowDay + day + ":" + srcUid, String.valueOf(dstUid));
			if (redis.scard(RedisContant.userFollowDay + day + ":" + srcUid) == 1) {
				redis.expire(RedisContant.userFollowDay + day + ":" + srcUid, 24 * 60 * 60);
			}
		} catch (Exception e) {
			logger.error("<getSrcListInDst->Exception>", e);
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
	}

	/**
	 * 判断当天用户是否关注过主播
	 * 
	 * @param srcUid 用户ID
	 * @param dstUid 主播ID
	 * @return 进入返回 true,否则返回false
	 */
	public boolean havUserFollowDay(Integer srcUid, Integer dstUid) {
		ShardedJedisPool shardPool = null;
		ShardedJedis redis = null;
		try {
			String day = DateUtils.dateToString(null, "yyyyMMdd");
			shardPool = RedisBucket.getInstance().getShardPool(RedisContant.RedisNameOther);
			redis = shardPool.getResource();
			return redis.sismember(RedisContant.userFollowDay + day + ":" + srcUid, String.valueOf(dstUid));
		} catch (Exception e) {
			logger.error("<havUserFollowDay->Exception>", e);
			return false;
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
	}

	public boolean setPKInfo(Integer srcUid, String data) {
		ShardedJedisPool shardPool = null;
		ShardedJedis redis = null;
		try {
			shardPool = RedisBucket.getInstance().getShardPool(RedisContant.RedisNameOther);
			redis = shardPool.getResource();
			Long res = redis.hset(RedisContant.pkInfo, String.valueOf(srcUid), data);
			if (res == null) {
				return false;
			}
			return true;
		} catch (Exception e) {
			logger.error("<setPKInfo->Exception>", e);
			return false;
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
	}
	
	public String getPKInfo(Integer dstUid,Integer srcUid) {
		ShardedJedisPool shardPool = null;
		ShardedJedis redis = null;
		String res = null;
		try {
			shardPool = RedisBucket.getInstance().getShardPool(RedisContant.RedisNameOther);
			redis = shardPool.getResource();
			res = redis.hget(RedisContant.pkInfo, String.valueOf(dstUid));
			if(!StringUtils.isBlank(res)) {
				return res;
			}
			return redis.hget(RedisContant.pkInfo, String.valueOf(srcUid));
		} catch (Exception e) {
			logger.error("<getPKInfo->Exception>", e);
			return null;
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
	}
	
	//发起者
	public Long delPKInfoById(Integer uid) {
		ShardedJedisPool shardPool = null;
		ShardedJedis redis = null;
		try {
			shardPool = RedisBucket.getInstance().getShardPool(RedisContant.RedisNameOther);
			redis = shardPool.getResource();
			return redis.hdel(RedisContant.pkInfo,String.valueOf(uid));
		} catch (Exception e) {
			logger.error("<delPKInfoById->Exception>", e);
			return null;
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
	}
	
	public Map<String, String> getAllPKInfo() {
		ShardedJedisPool shardPool = null;
		ShardedJedis redis = null;
		try {
			shardPool = RedisBucket.getInstance().getShardPool(RedisContant.RedisNameOther);
			redis = shardPool.getResource();
			Map<String, String> pkMap = redis.hgetAll(RedisContant.pkInfo);
			return pkMap;
		} catch (Exception e) {
			logger.error("<getAllPKInfo->Exception>", e);
			return null;
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
	}

	/**
	 * 设置PK关系
	 * @param anchoruid
	 * @param dstuid
	 * @return
	 */
	public boolean setPkAnchorRel(int anchoruid, int dstuid) {
		ShardedJedisPool shardPool = null;
		ShardedJedis redis = null;
		try {
			shardPool = RedisBucket.getInstance().getShardPool(RedisContant.RedisNameOther);
			redis = shardPool.getResource();
			redis.hset(RedisContant.pkAnchorRel, String.valueOf(anchoruid), String.valueOf(dstuid));
			redis.hset(RedisContant.pkAnchorRel, String.valueOf(dstuid), String.valueOf(anchoruid));
			return true;
		} catch (Exception e) {
			logger.error("<setPKInfo->Exception>", e);
			return false;
		} finally {
			if (redis != null) {
				redis.close();
			}
		}		
	}
	
	/**
	 * 删除PK关系
	 * @param uid
	 * @return
	 */
	public boolean delPkAnchorRel(int anchoruid, int dstuid) {
		ShardedJedisPool shardPool = null;
		ShardedJedis redis = null;
		try {
			shardPool = RedisBucket.getInstance().getShardPool(RedisContant.RedisNameOther);
			redis = shardPool.getResource();
			redis.hdel(RedisContant.pkAnchorRel, String.valueOf(anchoruid));
			redis.hdel(RedisContant.pkAnchorRel, String.valueOf(dstuid));
			return true;
		} catch (Exception e) {
			logger.error("<setPKInfo->Exception>", e);
			return false;
		} finally {
			if (redis != null) {
				redis.close();
			}
		}		
	}
	
	/**
	 * 根据ID 查询PK关系
	 * @param uid
	 * @return
	 */
	public String getPkAnchorRel(int uid) {
		ShardedJedisPool shardPool = null;
		ShardedJedis redis = null;
		try {
			shardPool = RedisBucket.getInstance().getShardPool(RedisContant.RedisNameOther);
			redis = shardPool.getResource();
			boolean keyExists = redis.hexists(RedisContant.pkAnchorRel, String.valueOf(uid));
			if(!keyExists) {
				return null;
			}
			return redis.hget(RedisContant.pkAnchorRel, String.valueOf(uid));
		} catch (Exception e) {
			logger.error("<setPKInfo->Exception>", e);
			return null;
		} finally {
			if (redis != null) {
				redis.close();
			}
		}		
	}
	
	/**
	 * 判断是否是在PK中或者惩罚中
	 * @param uid
	 * @return PK或者惩罚中 返回 true 否则返回false
	 */
	public boolean existsPkAnchorById(int anchorUid,int dsUid) {
		ShardedJedisPool shardPool = null;
		ShardedJedis redis = null;
		try {
			shardPool = RedisBucket.getInstance().getShardPool(RedisContant.RedisNameOther);
			redis = shardPool.getResource();
			boolean existsA = redis.hexists(RedisContant.pkAnchorRel, String.valueOf(anchorUid));
			boolean existsB = redis.hexists(RedisContant.pkAnchorRel, String.valueOf(dsUid));
			boolean existsPenaltyTime = redis.exists(RedisContant.pkPenaltyTimeInfo+anchorUid);
			if(!existsA&&!existsB&&!existsPenaltyTime) {
				return false;
			}
			return true;
		} catch (Exception e) {
			logger.error("<existsPkAnchorById->Exception>", e);
			return false;
		} finally {
			if (redis != null) {
				redis.close();
			}
		}		
	}
	
	/**
	 * 记录连麦邀请信息
	 * @param uid
	 * @return
	 */
	public boolean setInviteInfo(int anchoruid, int dstuid) {
		ShardedJedisPool shardPool = null;
		ShardedJedis redis = null;
		try {
			shardPool = RedisBucket.getInstance().getShardPool(RedisContant.RedisNameOther);
			redis = shardPool.getResource();
			redis.hset(RedisContant.userLianmaiInviteInfo, String.valueOf(anchoruid), String.valueOf(dstuid));
			redis.hset(RedisContant.userLianmaiInviteInfo, String.valueOf(dstuid), String.valueOf(anchoruid));
			return true;
		} catch (Exception e) {
			logger.error("<setInviteInfo->Exception>", e);
			return false;
		} finally {
			if (redis != null) {
				redis.close();
			}
		}		
	}
	/**
	 * 查询连麦邀请信息
	 * @param anchoruid
	 * @return
	 */
	public boolean existInviteInfo(int anchoruid){
		ShardedJedisPool shardPool = null;
		ShardedJedis redis = null;
		try {
			shardPool = RedisBucket.getInstance().getShardPool(RedisContant.RedisNameOther);
			redis = shardPool.getResource();
			return redis.hexists(RedisContant.userLianmaiInviteInfo, String.valueOf(anchoruid));
		} catch (Exception e) {
			logger.error("<existInviteInfo->Exception>", e);
			return false;
		} finally {
			if (redis != null) {
				redis.close();
			}
		}		
	}
	/**
	 * 删除连麦邀请信息
	 * @param uid
	 * @return
	 */
	public boolean delInviteInfo(int anchoruid ,int dstuid) {
		ShardedJedisPool shardPool = null;
		ShardedJedis redis = null;
		try {
			shardPool = RedisBucket.getInstance().getShardPool(RedisContant.RedisNameOther);
			redis = shardPool.getResource();
			redis.hdel(RedisContant.userLianmaiInviteInfo, String.valueOf(anchoruid), String.valueOf(dstuid));
			redis.hdel(RedisContant.userLianmaiInviteInfo,  String.valueOf(dstuid),String.valueOf(anchoruid));
			return true;
		} catch (Exception e) {
			logger.error("<delInviteInfo->Exception>", e);
			return false;
		} finally {
			if (redis != null) {
				redis.close();
			}
		}		
	}
	/**
	 * 获取连麦对方id
	 * @param uid
	 * @return
	 */
	public String getInviteOtherId(int anchoruid) {
		ShardedJedisPool shardPool = null;
		ShardedJedis redis = null;
		try {
			shardPool = RedisBucket.getInstance().getShardPool(RedisContant.RedisNameOther);
			redis = shardPool.getResource();
			return redis.hget(RedisContant.userLianmaiInviteInfo, String.valueOf(anchoruid));			
		} catch (Exception e) {
			logger.error("<delInviteInfo->Exception>", e);
			return null;
		} finally {
			if (redis != null) {
				redis.close();
			}
		}		
	}
	
	/**
	 * 缓存当前图片验证码
	 *
	 * @param mobile
	 * @param code
	 * @param seconds
	 */
	public String setImageVerifyCode(String mobile, String code, int seconds) {
		ShardedJedisPool shardPool = RedisBucket.getInstance().getShardPool(RedisContant.RedisNameOther);
		ShardedJedis redis = null;
		try {
			redis = shardPool.getResource();
			return redis.setex(RedisContant.mbImageVerifyCode + mobile, seconds, code);
		} catch (Exception e) {
			logger.error("<setSendCodeTimes->Exception>" + e.toString());
			return null;
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
	}
	
	/**
	 * 根据手机号获取图片验证码
	 *
	 * @param mobile
	 */
	public String getImageVerifyCode(String mobile) {
		ShardedJedisPool shardPool = RedisBucket.getInstance().getShardPool(RedisContant.RedisNameOther);
		ShardedJedis redis = null;
		try {
			redis = shardPool.getResource();
			return redis.get(RedisContant.mbImageVerifyCode + mobile);
		} catch (Exception e) {
			logger.error("<setSendCodeTimes->Exception>" + e.toString());
			return null;
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
	}
	
	/**
	 * 根据手机号删除图片验证码
	 *
	 * @param mobile
	 */
	public Long delImageVerifyCode(String mobile) {
		ShardedJedisPool shardPool = RedisBucket.getInstance().getShardPool(RedisContant.RedisNameOther);
		ShardedJedis redis = null;
		try {
			redis = shardPool.getResource();
			return redis.del(RedisContant.mbImageVerifyCode + mobile);
		} catch (Exception e) {
			logger.error("<setSendCodeTimes->Exception>" + e.toString());
			return null;
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
	}
	
	/**
	 * 缓存微信公众号accessToken
	 *
	 * @param accessToken
	 * @param seconds
	 */
	public String setWebChatAccessToken(String accessToken, int seconds) {
		ShardedJedisPool shardPool = RedisBucket.getInstance().getShardPool(RedisContant.RedisNameOther);
		ShardedJedis redis = null;
		try {
			redis = shardPool.getResource();
			return redis.setex(RedisContant.wxAccessToken , seconds, accessToken);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			return null;
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
	}
	
	/**
	 *获取微信公众号accessToken
	 *
	 */
	public String getWebChatAccessToken() {
		ShardedJedisPool shardPool = RedisBucket.getInstance().getShardPool(RedisContant.RedisNameOther);
		ShardedJedis redis = null;
		try {
			redis = shardPool.getResource();
			return redis.get(RedisContant.wxAccessToken);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			return null;
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
	}
	
	/**
	 * 缓存微信公众号jsapiTicket
	 *
	 * @param jsapiTicket
	 * @param seconds
	 */
	public String setWebchatJsapiTicket(String jsapiTicket, int seconds) {
		ShardedJedisPool shardPool = RedisBucket.getInstance().getShardPool(RedisContant.RedisNameOther);
		ShardedJedis redis = null;
		try {
			redis = shardPool.getResource();
			return redis.setex(RedisContant.wxJsapiTicket , seconds, jsapiTicket);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			return null;
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
	}
	
	/**
	 *获取微信公众号jsapiTicket
	 *
	 */
	public String getWebchatJsapiTicket() {
		ShardedJedisPool shardPool = RedisBucket.getInstance().getShardPool(RedisContant.RedisNameOther);
		ShardedJedis redis = null;
		try {
			redis = shardPool.getResource();
			return redis.get(RedisContant.wxJsapiTicket);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			return null;
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
	}
}