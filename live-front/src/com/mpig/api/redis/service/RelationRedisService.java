package com.mpig.api.redis.service;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;
import redis.clients.jedis.Tuple;

import com.mpig.api.redis.core.RedisBucket;
import com.mpig.api.utils.DateUtils;
import com.mpig.api.utils.RedisContant;

/**
 * 关系缓存
 * 
 * @author fang
 *
 */
public class RelationRedisService {

	private static Logger logger = Logger.getLogger("redislog");

	// tosy 声援榜分类
	private static final String paramDay = "day";
	private static final String paramWeek = "week";

	private static final Long pageSiz = 20L;
	private static final Long audiences = 30L; // 观众列表每页30个
	private final static RelationRedisService instance = new RelationRedisService();
	private final static String redisName = "relation";

	public static RelationRedisService getInstance() {
		return instance;
	}

	/**
	 * 新增关注别人数量
	 * 
	 * @param srcUid
	 *            关注始发人
	 * @param dstUid
	 *            关注目标人 srcUid 关注了dstUid
	 * @param type
	 *            =on 关注 =off 取消关注
	 * @return
	 */
	public int addFollows(Integer srcUid, Integer dstUid, Double score, String type) {
		ShardedJedisPool shardPool = null;
		ShardedJedis redis = null;
		try {
			shardPool = RedisBucket.getInstance().getShardPool(redisName);
			redis = shardPool.getResource();
			long ires = 0L;
			if ("on".equalsIgnoreCase(type)) {
				ires = redis.zadd(RedisContant.keyFollows + srcUid, score, String.valueOf(dstUid));
				redis.setex(RedisContant.keyFollowTimes + srcUid + ":" + dstUid, 30, "1");

			} else {
				ires = redis.zrem(RedisContant.keyFollows + srcUid, String.valueOf(dstUid));
			}
			if (ires > 0) {
				addFans(dstUid, srcUid, score, type);
			}
			return (int) ires;
		} catch (Exception e) {
			logger.error("<addFollows->Exception>" + e.toString());
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
		return 0;
	}

	/**
	 * 获取用户是否在30秒内关注同一个主播 true 是，false 否
	 */
	public boolean getFollowsTimes(int srcUid, int dstUid) {
		String str = "";
		ShardedJedisPool shardPool = null;
		ShardedJedis redis = null;
		try {
			shardPool = RedisBucket.getInstance().getShardPool(redisName);
			redis = shardPool.getResource();
			str = redis.get(RedisContant.keyFollowTimes + srcUid + ":" + dstUid);
		} catch (Exception e) {
			logger.error("<getFollowsTimes->Exception>" + e.toString());
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
	 * 设置是否接受关注主播推送的信息
	 * 
	 * @param srcUid
	 * @param dstUid
	 * @param type
	 */
	public void setPushSwitch(Integer srcUid, Integer dstUid, int type) {
		ShardedJedisPool shardPool = null;
		ShardedJedis redis = null;
		try {
			shardPool = RedisBucket.getInstance().getShardPool(redisName);
			redis = shardPool.getResource();
			if (type == 0) {
				// 关闭
				if (dstUid == 0) {
					// 全关闭
					Set<String> set = redis.zrange(RedisContant.keyFollows + srcUid, 0, -1);
					if (set.size() > 0) {
						for (String entry : set) {
							redis.hset(RedisContant.keyPushMsg + srcUid, entry, "1");
						}
					}
				} else {
					redis.hset(RedisContant.keyPushMsg + srcUid, String.valueOf(dstUid), "1");
				}
			} else {
				// 开通
				if (dstUid == 0) {
					// 全开通
					redis.del(RedisContant.keyPushMsg + srcUid);
				} else {
					redis.hdel(RedisContant.keyPushMsg + srcUid, String.valueOf(dstUid));
				}
			}
		} catch (Exception e) {
			logger.error("<addFollows->Exception>" + e.toString());
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
	}

	/**
	 * 获取该用户与该主播是否屏蔽消息推送
	 * 
	 * @param srcUid
	 * @param dstUid
	 * @return =true 没有拒收消息 =false 拒收消息
	 */
	public Boolean getPushFollow(Integer srcUid, Integer dstUid) {
		String strRes = "";
		ShardedJedisPool shardPool = null;
		ShardedJedis redis = null;
		try {
			shardPool = RedisBucket.getInstance().getShardPool(redisName);
			redis = shardPool.getResource();
			strRes = redis.hget(RedisContant.keyPushMsg + srcUid, String.valueOf(dstUid));
		} catch (Exception e) {
			logger.error("<addFollows->Exception>" + e.toString());
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
		if (strRes == null || strRes == "") {
			// 接收消息推送
			return true;
		} else {
			// 不接收消息推送
			return false;
		}
	}

	/**
	 * 判断 srcUid是否关注过dstUid
	 * 
	 * @param srcUid
	 *            用户
	 * @param dstUid
	 *            主播
	 * @return =null 没有关注过 其他则表示关注过
	 */
	public Double isFollows(Integer srcUid, Integer dstUid) {
		ShardedJedisPool shardPool = null;
		ShardedJedis redis = null;
		try {
			shardPool = RedisBucket.getInstance().getShardPool(redisName);
			redis = shardPool.getResource();
			return redis.zscore(RedisContant.keyFollows + srcUid, dstUid.toString());
		} catch (Exception e) {
			logger.error("<isFollows->Exception>" + e.toString());
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
		return null;
	}

	/**
	 * 获取关注数
	 * 
	 * @param srcUid
	 * @return
	 */
	public Set<String> getFollows(Integer srcUid) {
		ShardedJedisPool shardPool = null;
		ShardedJedis redis = null;
		try {
			shardPool = RedisBucket.getInstance().getShardPool(redisName);
			redis = shardPool.getResource();
			return redis.zrevrange(RedisContant.keyFollows + srcUid, 0L, -1L);
		} catch (Exception e) {
			logger.error("<getFollows->Exception>" + e.toString());
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
		return null;
	}

	/**
	 * 获取关注总数
	 * 
	 * @param srcUid
	 * @return
	 */
	public int getFollowsTotal(Integer srcUid) {
		Long lg = 0L;
		ShardedJedisPool shardPool = null;
		ShardedJedis redis = null;
		try {
			shardPool = RedisBucket.getInstance().getShardPool(redisName);
			redis = shardPool.getResource();

			lg = redis.zcard(RedisContant.keyFollows + srcUid);
		} catch (Exception e) {
			logger.error("<getFollows->Exception>" + e.toString());
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
		if (lg == null) {
			return 0;
		} else {
			return lg.intValue();
		}
	}

	/**
	 * 新增粉丝
	 * 
	 * @param dstUid
	 *            该用户新增粉丝
	 * @param srcUid
	 *            该用户成为dstUid的粉丝
	 * @param type
	 *            =on 表示新增 =off 取消
	 * @return
	 */
	public Long addFans(Integer dstUid, Integer srcUid, Double score, String type) {
		ShardedJedisPool shardPool = null;
		ShardedJedis redis = null;
		try {
			shardPool = RedisBucket.getInstance().getShardPool(redisName);
			redis = shardPool.getResource();
			if ("on".equalsIgnoreCase(type)) {
				return redis.zadd(RedisContant.keyFans + dstUid, score, String.valueOf(srcUid));
			} else {
				return redis.zrem(RedisContant.keyFans + dstUid, String.valueOf(srcUid));
			}
		} catch (Exception e) {
			logger.error("<addFans->Exception>" + e.toString());
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
		return null;
	}

	/**
	 * 获取粉丝的score
	 * 
	 * @param dstUid
	 * @param srcUid
	 * @return
	 */
	public Double isFansScore(Integer dstUid, Integer srcUid) {
		ShardedJedisPool shardPool = null;
		ShardedJedis redis = null;
		try {
			shardPool = RedisBucket.getInstance().getShardPool(redisName);
			redis = shardPool.getResource();
			return redis.zscore(RedisContant.keyFans + dstUid, srcUid.toString());
		} catch (Exception e) {
			logger.error("<isFans->Exception>" + e.toString());
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
		return null;
	}

	/**
	 * 获取该用户的粉丝数量
	 * 
	 * @param uid
	 * @return
	 */
	public Long getFansTotal(Integer uid) {
		ShardedJedisPool shardPool = null;
		ShardedJedis redis = null;
		try {
			shardPool = RedisBucket.getInstance().getShardPool(redisName);
			redis = shardPool.getResource();
			return redis.zcard(RedisContant.keyFans + uid);
		} catch (Exception e) {
			logger.error("<getFansTotal->Exception>" + e.toString());
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
		return null;
	}

	/**
	 * 增加用户总喜欢数
	 * 
	 * @param uid
	 * @return
	 */
	public void addLikes(int uid, int step) {
		ShardedJedisPool shardPool = null;
		ShardedJedis redis = null;
		try {
			shardPool = RedisBucket.getInstance().getShardPool(redisName);
			redis = shardPool.getResource();
			redis.incrBy(RedisContant.KeyLikes + uid, step).intValue();
		} catch (Exception e) {
			logger.error("<addLikes->Exception>" + e.toString());
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
	}

	/**
	 * 获取用户总喜欢数
	 * 
	 * @param uid
	 * @return
	 */
	public int getLikes(int uid) {
		String str = "";
		ShardedJedisPool shardPool = null;
		ShardedJedis redis = null;
		try {
			shardPool = RedisBucket.getInstance().getShardPool(redisName);
			redis = shardPool.getResource();
			str = redis.get(RedisContant.KeyLikes + uid);

		} catch (Exception e) {
			logger.error("<getLikes->Exception>" + e.toString());
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
		if (str == null || "".equals(str)) {
			return 0;
		}
		return Integer.parseInt(str);

	}

	/**
	 * 获取粉丝列表
	 * 
	 * @param uid
	 * @return Set value是uid
	 */
	public Set<String> getFans(int uid, int pages) {
		ShardedJedisPool shardPool = null;
		ShardedJedis redis = null;
		try {
			shardPool = RedisBucket.getInstance().getShardPool(redisName);
			redis = shardPool.getResource();
			if (pages > 0) {
				return redis.zrevrange(RedisContant.keyFans + String.valueOf(uid), (pages - 1) * pageSiz,
						pages * pageSiz - 1);
			} else {
				return redis.zrevrange(RedisContant.keyFans + String.valueOf(uid), 0L, -1L);
			}
		} catch (Exception e) {
			logger.error("<getFans->Exception>" + e.toString());
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
		return null;
	}

	/**
	 * 判断是否关注关系
	 * 
	 * @param srcUid
	 *            srcUid 是否关注了dstUid
	 * @param dstUid
	 * @return =false 不是粉丝 =true 是粉丝
	 */
	public Boolean isFan(Integer srcUid, Integer dstUid) {
		Double dl = this.isFansScore(dstUid, srcUid);
		if (dl == null) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * 增加本场用户喜欢数 开播时需要清空
	 * 
	 * @param uid
	 * @return
	 */
	public Long addRoomLikes(int uid, int step) {
		ShardedJedisPool shardPool = null;
		ShardedJedis redis = null;
		try {
			shardPool = RedisBucket.getInstance().getShardPool(redisName);
			redis = shardPool.getResource();
			return redis.incrBy(RedisContant.KeyRoomLikes + uid, step);
		} catch (Exception e) {
			logger.error("<addRoomLikes->Exception>" + e.toString());
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
		return null;
	}

	/**
	 * 获取本场喜欢人数
	 * 
	 * @param uid
	 * @return
	 */
	public int getRoomLikes(int uid) {
		ShardedJedisPool shardPool = null;
		ShardedJedis redis = null;
		try {
			shardPool = RedisBucket.getInstance().getShardPool(redisName);
			redis = shardPool.getResource();
			String str = redis.get(RedisContant.KeyRoomLikes + uid);
			if (str == null || "".equals(str)) {
				return 0;
			} else {
				return Integer.valueOf(str);
			}
		} catch (Exception e) {
			logger.error("<getRoomLikes->Exception>" + e.toString());
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
		return 0;
	}

	/**
	 * 开播时 清空数据
	 * 
	 * @param uid
	 */
	public void delRoomLikes(int uid) {
		ShardedJedisPool shardPool = null;
		ShardedJedis redis = null;
		try {
			shardPool = RedisBucket.getInstance().getShardPool(redisName);
			redis = shardPool.getResource();
			redis.del(RedisContant.KeyRoomLikes + uid);
		} catch (Exception e) {
			logger.error("<delRoomLikes->Exception>" + e.toString());
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
	}

	/**
	 * 用户进房缓存
	 * 
	 * @param srcUid
	 *            用户
	 * @param dstUid
	 *            主播
	 */
	public void userIntoRoom(int srcUid, int dstUid, Double score) {
		ShardedJedisPool shardPool = null;
		ShardedJedis redis = null;
		try {

			shardPool = RedisBucket.getInstance().getShardPool(redisName);
			redis = shardPool.getResource();

			Long times = System.currentTimeMillis() / 1000;

			String srcU = String.valueOf(srcUid);
			String dstU = String.valueOf(dstUid);

			// 记录本场主播进入的人数
			Long hset = redis.hset(RedisContant.keyLiveEnterTotal + dstU, srcU, "1");

			if (srcUid < 900000000) {
				if (hset == 1) {
					// 真人进房次数
					redis.incr(RedisContant.keyRoomUserTimes + dstU);
					// 真人在房数
					redis.incr(RedisContant.KeyRoomUserCount + dstU);
				}
			}

			String hget = redis.hget(RedisContant.KeyUserAndAnchor, srcU);
			if (StringUtils.isNotEmpty(hget)) {
				redis.incrBy(RedisContant.KeyRoomTimes + hget, -1);
				redis.hdel(RedisContant.KeyUserEnterLong + hget, srcU);
				redis.zrem(RedisContant.KeyAnchorAndUser + hget, srcU);
				redis.hdel(RedisContant.KeyUserAndAnchor, srcU);
			}

			// 进房人次(总数)
			redis.incr(RedisContant.KeyRoomTimes + dstU);

			// 用户进房时间
			redis.hset(RedisContant.KeyUserEnterLong + dstU, srcU, times.toString());

			// 进房用户列表 排序：声援榜前10+用户等级+时间
			redis.zadd(RedisContant.KeyAnchorAndUser + dstU, score, srcU);

			// 用户与主播的关系(1:1)
			redis.hset(RedisContant.KeyUserAndAnchor, srcU, dstU);

		} catch (Exception e) {
			logger.error("<userIntoRoom->Exception>" + e.toString());
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
	}

	/**
	 * 守护进房
	 * @param srcUid
	 * @param dstUid
	 * @param score
	 */
	public void guardUserIntoRoom(int srcUid, int dstUid, Double score) {
		ShardedJedisPool shardPool = null;
		ShardedJedis redis = null;
		try {

			shardPool = RedisBucket.getInstance().getShardPool(redisName);
			redis = shardPool.getResource();
			Long times = System.currentTimeMillis() / 1000;

			String srcU = String.valueOf(srcUid);
			String dstU = String.valueOf(dstUid);

			// 记录本场主播进入的人数
			Long hset = redis.hset(RedisContant.keyLiveEnterTotal + dstU, srcU, "1");

			if (srcUid < 900000000) {

				if (hset == 1) {
					// 真人进房次数
					redis.incr(RedisContant.keyRoomUserTimes + dstU);
					// 真人在房数
					redis.incr(RedisContant.KeyRoomUserCount + dstU);
				}
			}

			// 进房人次(总数)
			redis.incr(RedisContant.KeyRoomTimes + dstU);

			// 守护进房人次+1
			redis.incr(RedisContant.KeyRoomGuards + dstU);

			// 用户进房时间
			redis.hset(RedisContant.KeyUserEnterLong + dstU, srcU, times.toString());

			// 进房用户列表 排序：等级分数
			redis.zadd(RedisContant.KeyAnchorAndGuardUser + dstU, score, srcU);

			// 用户与主播的关系(1:1)
			redis.hset(RedisContant.KeyUserAndAnchor, srcU, dstU);

		} catch (Exception e) {
			logger.error("<userIntoRoom->Exception>" + e.toString());
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
	}

	/**
	 * 所有用户进房
	 * @param srcUid
	 * @param dstUid
	 * @param score
	 */
	public void allUserIntoRoom(int srcUid, int dstUid, Double score) {
		ShardedJedisPool shardPool = null;
		ShardedJedis redis = null;
		try {

			shardPool = RedisBucket.getInstance().getShardPool(redisName);
			redis = shardPool.getResource();

			String srcU = String.valueOf(srcUid);
			String dstU = String.valueOf(dstUid);
			// 进房用户列表 排序：等级分数
			redis.zadd(RedisContant.KeyAnchorAndAllUser + dstU, score, srcU);

		} catch (Exception e) {
			logger.error("<allUserIntoRoom->Exception>" + e.toString());
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
	}

	/**
	 * 获取本场直播进入的人数 包含机器人
	 * 
	 * @param anchor
	 * @return
	 */
	public int getLiveEnterTotal(int anchor) {

		Long hlen = 0L;
		ShardedJedisPool shardPool = null;
		ShardedJedis redis = null;
		try {
			shardPool = RedisBucket.getInstance().getShardPool(redisName);
			redis = shardPool.getResource();
			hlen = redis.hlen(RedisContant.keyLiveEnterTotal + anchor);
		} catch (Exception e) {
			logger.error("<delLiveEnterTotal->Exception>" + e.toString());
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
		return hlen == null ? 0 : hlen.intValue();
	}

	/**
	 * 获取真实的人进入房间 排重的
	 * 
	 * @param anchor
	 * @return
	 */
	public int getLiveRealEnterTotal(int anchor) {
		String hlen = "";
		ShardedJedisPool shardPool = null;
		ShardedJedis redis = null;
		try {
			shardPool = RedisBucket.getInstance().getShardPool(redisName);
			redis = shardPool.getResource();
			hlen = redis.get(RedisContant.KeyRoomUserCount + anchor);
		} catch (Exception e) {
			logger.error("<getLiveRealEnterTotal->Exception>" + e.toString());
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
		return hlen == null ? 0 : Integer.valueOf(hlen);
	}

	/**
	 * 开播前 删除上次房间记录的用户数
	 * 
	 * @param anchor
	 * @return
	 */
	public int delLiveEnterTotal(int anchor) {
		Long lg = 0L;
		ShardedJedisPool shardPool = null;
		ShardedJedis redis = null;
		try {
			shardPool = RedisBucket.getInstance().getShardPool(redisName);
			redis = shardPool.getResource();
			lg = redis.del(RedisContant.keyLiveEnterTotal + anchor);
			redis.del(RedisContant.KeyRoomUserCount + anchor);
		} catch (Exception e) {
			logger.error("<delLiveEnterTotal->Exception>" + e.toString());
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
		return lg == null ? 0 : lg.intValue();
	}

	/**
	 * 判断是否为主播
	 * 
	 * @param anchor
	 * @return
	 */
	public Boolean checkAnchor(int anchor) {
		Boolean bl = false;
		ShardedJedisPool shardPool = null;
		ShardedJedis redis = null;
		try {
			shardPool = RedisBucket.getInstance().getShardPool(redisName);
			redis = shardPool.getResource();
			bl = redis.exists(RedisContant.KeyAnchorAndUser + anchor);
		} catch (Exception e) {
			logger.error("<checkAnchor->Exception>" + e.toString());
		} finally {

			if (redis != null) {
				redis.close();
			}
		}
		return bl;
	}

	/**
	 * 获取房间里所有用户的进房时间
	 * 
	 * @param uid
	 * @return
	 */
	public Map<String, String> getUsersTimeInRoom(Integer uid) {
		ShardedJedisPool shardPool = null;
		ShardedJedis redis = null;
		try {
			shardPool = RedisBucket.getInstance().getShardPool(redisName);
			redis = shardPool.getResource();
			return redis.hgetAll(RedisContant.KeyUserEnterLong + uid);
		} catch (Exception e) {
			logger.error("<getUsersTimeInRoom->Exception>" + e.toString());
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
		return null;
	}

	/**
	 * 检查 用户srcUid是否在dstUid房间内
	 * 
	 * @param srcUid
	 * @param dstUid
	 * @return
	 */
	public Double checkUserInAnchor(int srcUid, int dstUid) {
		Double zscore = 0.0;
		ShardedJedisPool shardPool = null;
		ShardedJedis redis = null;
		try {
			shardPool = RedisBucket.getInstance().getShardPool(redisName);
			redis = shardPool.getResource();
			// 删除该房间的该用户
			zscore = redis.zscore(RedisContant.KeyAnchorAndUser + dstUid, String.valueOf(srcUid));
			if (null == zscore) {
				zscore = redis.zscore(RedisContant.KeyAnchorAndGuardUser + dstUid, String.valueOf(srcUid));
			}

		} catch (Exception e) {
			logger.error("<userExitRoom->Exception>" + e.toString());
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
		return zscore;
	}

	/**
	 * 用户离开房间
	 * 
	 * @param srcUid
	 * @param dstUid
	 * @return 进入房间时间
	 */
	public Long userExitRoom(int srcUid, int dstUid) {
		ShardedJedisPool shardPool = null;
		ShardedJedis redis = null;
		try {
			shardPool = RedisBucket.getInstance().getShardPool(redisName);
			redis = shardPool.getResource();

			// 删除用户跟主播关系(1:1)
			redis.hdel(RedisContant.KeyUserAndAnchor, String.valueOf(srcUid));
			// 获取用户在该房间的时长
			String str = redis.hget(RedisContant.KeyUserEnterLong + dstUid, String.valueOf(srcUid));
			// 删除该房间的该用户
			Long flag = redis.zrem(RedisContant.KeyAnchorAndUser + dstUid, String.valueOf(srcUid));
			redis.zrem(RedisContant.KeyAnchorAndAllUser + dstUid, String.valueOf(srcUid));
			if (flag == 0) {
				flag = redis.zrem(RedisContant.KeyAnchorAndGuardUser + dstUid, String.valueOf(srcUid));
				if (flag == 1) {
					// 守护进房人次-1
					redis.decr(RedisContant.KeyRoomGuards + dstUid);
				}
			}
			if (str == null) {
				return 0L;
			} else {
				return Long.valueOf(str);
			}
		} catch (Exception e) {
			logger.error("<userExitRoom->Exception>" + e.toString());
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
		return 0L;
	}

	/**
	 * 守护退房
	 * @param srcUid
	 * @param dstUid
	 * @return
	 */
	public Long guardUserExitRoom(int srcUid, int dstUid) {
		ShardedJedisPool shardPool = null;
		ShardedJedis redis = null;
		try {
			shardPool = RedisBucket.getInstance().getShardPool(redisName);
			redis = shardPool.getResource();

			// 删除用户跟主播关系(1:1)
			redis.hdel(RedisContant.KeyUserAndAnchor, String.valueOf(srcUid));
			// 获取用户在该房间的时长
			String str = redis.hget(RedisContant.KeyUserEnterLong + dstUid, String.valueOf(srcUid));
			// 删除该房间的该用户
			redis.zrem(RedisContant.KeyAnchorAndGuardUser + dstUid, String.valueOf(srcUid));
			if (str == null) {
				return 0L;
			} else {
				return Long.valueOf(str);
			}
		} catch (Exception e) {
			logger.error("<userExitRoom->Exception>" + e.toString());
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
		return 0L;
	}

	/**
	 * 获取房间内的观众列表
	 * 
	 * @param dstUid
	 * @param page
	 * @return
	 */
	public Set<Tuple> getUserListInAnchor(Integer dstUid, Long page) {
		ShardedJedisPool shardPool = null;
		ShardedJedis redis = null;
		try {
			shardPool = RedisBucket.getInstance().getShardPool(redisName);
			redis = shardPool.getResource();
			if (page == 0) {
				return redis.zrevrangeWithScores(RedisContant.KeyAnchorAndUser + dstUid.toString(), 0, -1);
			} else {
				return redis.zrevrangeWithScores(RedisContant.KeyAnchorAndUser + dstUid.toString(),
						(page - 1) * audiences, page * audiences - 1);
			}

		} catch (Exception e) {
			logger.error("<getUserListInAnchor->Exception>" + e.toString());
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
		return null;
	}

	/**
	 * 获取房间内的守护观众列表
	 * 
	 * @param dstUid
	 * @param page
	 * @return
	 */
	public Set<Tuple> getGuardUserListInAnchor(Integer dstUid, Long page) {
		ShardedJedisPool shardPool = null;
		ShardedJedis redis = null;
		try {
			shardPool = RedisBucket.getInstance().getShardPool(redisName);
			redis = shardPool.getResource();
			if (page == 0) {
				return redis.zrevrangeWithScores(RedisContant.KeyAnchorAndGuardUser + dstUid.toString(), 0, -1);
			} else {
				return redis.zrevrangeWithScores(RedisContant.KeyAnchorAndGuardUser + dstUid.toString(),
						(page - 1) * audiences, page * audiences - 1);
			}

		} catch (Exception e) {
			logger.error("<getUserListInAnchor->Exception>" + e.toString());
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
		return null;
	}

	/**
	 * 获取房间内的守护观众列表（按付款价格排序）
	 * 
	 * @param dstUid
	 * @param page
	 * @return
	 */
	public Set<Tuple> getRoomAllGuardSortByMoney(Integer dstUid, Long page) {
		ShardedJedisPool shardPool = null;
		ShardedJedis redis = null;
		try {
			shardPool = RedisBucket.getInstance().getShardPool(redisName);
			redis = shardPool.getResource();
			if (page == 0) {
				return redis.zrevrangeWithScores(RedisContant.roomAllGuardSort + dstUid.toString(), 0, -1);
			} else {
				return redis.zrevrangeWithScores(RedisContant.roomAllGuardSort + dstUid.toString(),
						(page - 1) * audiences, page * audiences - 1);
			}

		} catch (Exception e) {
			logger.error("<getRoomAllGuardSortByMoney->Exception>" + e.toString());
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
		return null;
	}

	/**
	 * 获取房间内的所有观众列表
	 * 
	 * @param dstUid
	 * @param page
	 * @return
	 */
	public Set<Tuple> getAllUserListInAnchor(Integer dstUid, Long page) {
		ShardedJedisPool shardPool = null;
		ShardedJedis redis = null;
		try {
			shardPool = RedisBucket.getInstance().getShardPool(redisName);
			redis = shardPool.getResource();
			if (page == 0) {
				return redis.zrevrangeWithScores(RedisContant.KeyAnchorAndAllUser + dstUid.toString(), 0, -1);
			} else {
				return redis.zrevrangeWithScores(RedisContant.KeyAnchorAndAllUser + dstUid.toString(),
						(page - 1) * audiences, page * audiences - 1);
			}

		} catch (Exception e) {
			logger.error("<getAllUserListInAnchor->Exception>" + e.toString());
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
		return null;
	}

	/**
	 * 获取房间内的观众数量
	 * 
	 * @param dstUid
	 * @return
	 */
	public Long getUsersInAnchor(Integer dstUid) {
		Long lg = 0L;
		ShardedJedisPool shardPool = null;
		ShardedJedis redis = null;
		try {
			shardPool = RedisBucket.getInstance().getShardPool(redisName);
			redis = shardPool.getResource();
			lg = redis.zcard(RedisContant.KeyAnchorAndUser + dstUid.toString());
		} catch (Exception e) {
			logger.error("<getUsersInAnchor->Exception>" + e.toString());
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
		if (lg == null) {
			return 0L;
		} else {
			return lg;
		}
	}

	/**
	 * 本场直播 共进房人次 随人员进入而变化
	 * 
	 * @param dstUid
	 * @return
	 */
	public int getEnterRoomTimes(int dstUid) {
		ShardedJedisPool shardPool = null;
		ShardedJedis redis = null;
		try {
			shardPool = RedisBucket.getInstance().getShardPool(redisName);
			redis = shardPool.getResource();
			String strRed = redis.get(RedisContant.KeyRoomTimes + dstUid);
			if (strRed == null || "".equals(strRed)) {
				return 0;
			} else {
				return Integer.valueOf(strRed);
			}
		} catch (Exception e) {
			logger.error("<getEnterRoomTimes->Exception>" + e.toString());
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
		return 0;
	}

	/**
	 * 本场直播 共进房人次 随人员进入而变化（真人）
	 * 
	 * @param dstUid
	 * @return
	 */
	public int getRealEnterRoomTimes(int dstUid) {
		ShardedJedisPool shardPool = null;
		ShardedJedis redis = null;
		try {
			shardPool = RedisBucket.getInstance().getShardPool(redisName);
			redis = shardPool.getResource();
			String strRed = redis.get(RedisContant.keyRoomUserTimes + dstUid);
			if (strRed == null || "".equals(strRed)) {
				return 0;
			} else {
				return Integer.valueOf(strRed);
			}
		} catch (Exception e) {
			logger.error("<getRealEnterRoomTimes->Exception>" + e.toString());
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
		return 0;
	}

	/**
	 * 本场直播 共进房的守护人次
	 * 
	 * @param dstUid
	 * @return
	 */
	public int getRealGuardEnterRoom(int dstUid) {
		ShardedJedisPool shardPool = null;
		ShardedJedis redis = null;
		try {
			shardPool = RedisBucket.getInstance().getShardPool(redisName);
			redis = shardPool.getResource();
			String strRed = redis.get(RedisContant.KeyRoomGuards + dstUid);
			if (strRed == null || "".equals(strRed)) {
				return 0;
			} else {
				return Integer.valueOf(strRed);
			}
		} catch (Exception e) {
			logger.error("<getRealGuardEnterRoom->Exception>" + e.toString());
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
		return 0;
	}

	/**
	 * 修改用户送给主播的声援值 房间~用户(1:N) 声援榜
	 * 
	 * @param srcUid
	 * @param dstUid
	 * @param gets
	 */
	public void sendGift(int srcUid, int dstUid, Double gets) {
		ShardedJedisPool shardPool = null;
		ShardedJedis redis = null;
		try {
			shardPool = RedisBucket.getInstance().getShardPool(redisName);
			redis = shardPool.getResource();
			Double dl = redis.zscore(RedisContant.KeyAnchorAndUser + dstUid, String.valueOf(srcUid));
			if (dl == null) {
				redis.zadd(RedisContant.KeyAnchorAndUser + dstUid, gets, String.valueOf(srcUid));
			} else {
				redis.zadd(RedisContant.KeyAnchorAndUser + dstUid, gets + dl, String.valueOf(srcUid));
			}
		} catch (Exception e) {
			logger.error("<sendGift->Exception>" + e.toString());
		} finally {
			if (redis != null) {
				redis.close();
			}
		}

	}

	/**
	 * 获取该用户所在的房间
	 * 
	 * @param srcUid
	 * @return
	 */
	public String getUserAnchor(Integer srcUid) {
		String anchor = "";
		ShardedJedisPool shardPool = null;
		ShardedJedis redis = null;
		try {
			shardPool = RedisBucket.getInstance().getShardPool(redisName);
			redis = shardPool.getResource();
			anchor = redis.hget(RedisContant.KeyUserAndAnchor, srcUid.toString());
		} catch (Exception e) {
			logger.error("<delRoomLikes->Exception>" + e.toString());
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
		return anchor == null ? "0" : anchor;

	}

	/**
	 * 开播时 清空数据
	 * 
	 * @param uid
	 */
	public void delEnterRoomTimes(int dstUid) {
		ShardedJedisPool shardPool = null;
		ShardedJedis redis = null;
		try {
			shardPool = RedisBucket.getInstance().getShardPool(redisName);
			redis = shardPool.getResource();
			// 进房间人次(包含机器人)
			redis.del(RedisContant.KeyRoomTimes + dstUid);
			// 进房间人次(只有真人)
			redis.del(RedisContant.keyRoomUserTimes + dstUid);
			// 进房间用户时间列表
			redis.del(RedisContant.KeyUserEnterLong + dstUid);
			// 进房间观众列表
			redis.del(RedisContant.KeyAnchorAndUser + dstUid);
			// del guardlist
			redis.del(RedisContant.KeyAnchorAndGuardUser + dstUid);
			// 删除所有观众
			redis.del(RedisContant.KeyAnchorAndAllUser + dstUid);
		} catch (Exception e) {
			logger.error("<delRoomLikes->Exception>" + e.toString());
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
	}

	/**
	 * 记录主播当天的声援值
	 * 
	 * @param uid
	 * @param gets
	 */
	public void addRoomCreditThis(int uid, int gets) {

		ShardedJedisPool shardPool = null;
		ShardedJedis redis = null;
		try {
			shardPool = RedisBucket.getInstance().getShardPool(redisName);
			redis = shardPool.getResource();
			redis.incrBy(RedisContant.KeyRoomGetsToday + uid, gets);

		} catch (Exception e) {
			System.out.println("dayerr:" + e.toString());
			logger.error("<addRoomCreditToday->Exception>" + e.toString());
		} finally {
			if (redis != null) {
				redis.close();
			}
		}

	}

	/**
	 * 删除上一场的声援值
	 * 
	 * @param uid
	 * @return
	 */
	public int delRoomCreditThis(int anchor) {
		ShardedJedisPool shardPool = null;
		ShardedJedis redis = null;
		try {
			shardPool = RedisBucket.getInstance().getShardPool(redisName);
			redis = shardPool.getResource();
			redis.del(RedisContant.KeyRoomGetsToday + anchor);
		} catch (Exception e) {
			logger.error("<delRoomCreditThis->Exception>" + e.toString());
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
		return 0;
	}

	/**
	 * 返回本场获得的声援值
	 * @param anchor
	 * @return
	 */
	public int getRoomCreditThis(int anchor) {

		ShardedJedisPool shardPool = null;
		ShardedJedis redis = null;
		String credit = null;
		try {
			shardPool = RedisBucket.getInstance().getShardPool(redisName);
			redis = shardPool.getResource();
			credit = redis.get(RedisContant.KeyRoomGetsToday + anchor);

		} catch (Exception e) {
			System.out.println("dayerr:" + e.toString());
			logger.error("<addRoomCreditToday->Exception>" + e.toString());
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
		if (StringUtils.isEmpty(credit)) {
			return 0;
		} else {
			return Integer.valueOf(credit);
		}
	}

	/**
	 * 记录主播的声援榜(主播N:用户N)
	 * 
	 * @param srcUid
	 *            送礼物给dstUid
	 * @param dstUid
	 * @param dbl
	 *            礼物价值
	 */
	public void addDstGetSrc(Integer srcUid, Integer dstUid, Double dbl) {
		ShardedJedisPool shardPool = null;
		ShardedJedis redis = null;
		double dblday = dbl.doubleValue();
		double dblweek = dbl.doubleValue();

		try {
			shardPool = RedisBucket.getInstance().getShardPool(redisName);
			redis = shardPool.getResource();
			// all
			{
				Double olddb = redis.zscore(RedisContant.KeyDstGetsSrcMoney + dstUid, srcUid.toString());
				if (olddb != null) {
					dbl = dbl + olddb;
				}
				// 安装声援值排序
				redis.zadd(RedisContant.KeyDstGetsSrcMoney + dstUid, dbl, srcUid.toString());
			}
			// day
			{
				String day = DateUtils.dateToString(null, "yyyyMMdd");
				String key = RedisContant.KeyDstGetsSrcMoneyDay + dstUid + day;
				Double olddb = redis.zscore(key, srcUid.toString());
				if (olddb != null) {
					dblday = dblday + olddb;
				}
				// tosy 安装声援值排序 day
				redis.zadd(key, dblday, srcUid.toString());

				Long ex = redis.ttl(key);
				if (null == ex || 0 >= ex) {
					redis.expire(key, 60 * 60 * 24 * 7);
				}
			}
			// week
			{
				String week = DateUtils.getWeekStart(0);
				String key = RedisContant.KeyDstGetsSrcMoneyWeek + dstUid + week;
				Double olddb = redis.zscore(key, srcUid.toString());
				if (olddb != null) {
					dblweek = dblweek + olddb;
				}
				// tosy 安装声援值排序 week
				redis.zadd(key, dblweek, srcUid.toString());

				Long ex = redis.ttl(key);
				if (null == ex || 0 >= ex) {
					redis.expire(key, 60 * 60 * 24 * 7 * 4);
				}
			}
		} catch (Exception e) {
			logger.error("<addDstGetSrc->Exception>" + e.toString());
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
	}

	/**
	 * 获取该主播有多少人送礼
	 * 
	 * @param uid
	 * @param mode	/day/week/all(null)
	 * @return
	 */
	public Long getDstGetNumber(Integer uid, String mode) {
		Long lg = 0L;
		ShardedJedisPool shardPool = null;
		ShardedJedis redis = null;
		try {
			shardPool = RedisBucket.getInstance().getShardPool(redisName);
			redis = shardPool.getResource();
			// tosy add mode

			if (null == mode) {
				lg = redis.zcard(RedisContant.KeyDstGetsSrcMoney + uid);
			} else if (mode.equalsIgnoreCase(paramDay)) {
				String day = DateUtils.dateToString(null, "yyyyMMdd");
				lg = redis.zcard(RedisContant.KeyDstGetsSrcMoneyDay + uid + day);
			} else if (mode.equalsIgnoreCase(paramWeek)) {
				String week = DateUtils.getWeekStart(0);
				lg = redis.zcard(RedisContant.KeyDstGetsSrcMoneyWeek + uid + week);
			}
		} catch (Exception e) {
			logger.error("<getDstGetNumber->Exception>" + e.toString());
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
		if (lg == null) {
			return 0L;
		} else {
			return lg;
		}
	}

	/**
	 * 获取在声援排行榜中的位置
	 * 
	 * @param srcUid
	 *            用户
	 * @param dstUid
	 *            主播
	 * @return
	 */
	public Long getSrcInDstSort(Integer srcUid, Integer dstUid) {
		ShardedJedisPool shardPool = null;
		ShardedJedis redis = null;
		try {
			shardPool = RedisBucket.getInstance().getShardPool(redisName);
			redis = shardPool.getResource();
			return redis.zrevrank(RedisContant.KeyDstGetsSrcMoney + dstUid, srcUid.toString());
		} catch (Exception e) {
			logger.error("<getSrcInDstSort->Exception>" + e.toString());
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
		return null;
	}

	/**
	 * 获取该主播的声援排榜
	 * 
	 * @param uid
	 * @param page
	 * @param mode /day/week/all
	 * @return
	 */
	public Set<Tuple> getSrcListInDst(Integer uid, int page, String mode) {
		ShardedJedisPool shardPool = null;
		ShardedJedis redis = null;
		try {
			shardPool = RedisBucket.getInstance().getShardPool(redisName);
			redis = shardPool.getResource();
			// tosy add mode
			if (StringUtils.isEmpty(mode)) {
				return redis.zrevrangeWithScores(RedisContant.KeyDstGetsSrcMoney + uid, (page - 1) * pageSiz,
						page * pageSiz - 1);
			} else if (mode.equalsIgnoreCase(paramDay)) {
				String day = DateUtils.dateToString(null, "yyyyMMdd");
				return redis.zrevrangeWithScores(RedisContant.KeyDstGetsSrcMoneyDay + uid + day, (page - 1) * pageSiz,
						page * pageSiz - 1);
			} else if (mode.equalsIgnoreCase(paramWeek)) {
				String week = DateUtils.getWeekStart(0);
				return redis.zrevrangeWithScores(RedisContant.KeyDstGetsSrcMoneyWeek + uid + week, (page - 1) * pageSiz,
						page * pageSiz - 1);
			}
			logger.error("<getSrcListInDst->wrong arg mode string> : " + mode);
		} catch (Exception e) {
			logger.error("<getSrcListInDst->Exception>" + e.toString());
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
		return null;
	}

	/**
	 * 获取该主播的声援排榜
	 * 
	 * @param uid
	 * @param start
	 * @param rows
	 * @return
	 */
	public Set<Tuple> getSrcListInDstNew(Integer uid, int start, int rows) {
		ShardedJedisPool shardPool = null;
		ShardedJedis redis = null;
		try {
			shardPool = RedisBucket.getInstance().getShardPool(redisName);
			redis = shardPool.getResource();
			return redis.zrevrangeWithScores(RedisContant.KeyDstGetsSrcMoney + uid, start, start - 1 + rows);
		} catch (Exception e) {
			logger.error("<getSrcListInDst->Exception>" + e.toString());
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
		return null;
	}

	/**
	 * 记录用户送礼给主播列表(用户N:主播N)
	 * 
	 * @param srcUid
	 * @param dstUid
	 * @param dbl
	 */
	public void addSrcSendDst(Integer srcUid, Integer dstUid, Double dbl) {
		ShardedJedisPool shardPool = null;
		ShardedJedis redis = null;
		try {
			shardPool = RedisBucket.getInstance().getShardPool(redisName);
			redis = shardPool.getResource();
			Double olddb = redis.zscore(RedisContant.KeySrcSendDstMoney + dstUid, srcUid.toString());
			if (olddb != null) {
				dbl = dbl + olddb;
			}
			// 安装声援值排序
			redis.zadd(RedisContant.KeySrcSendDstMoney + dstUid, dbl, srcUid.toString());
		} catch (Exception e) {
			logger.error("<addSrcSendDst->Exception>" + e.toString());
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
	}

	/**
	 * 添加本场获取的猪头
	 * @param dstUid
	 * @param sends
	 */
	public void addNowLiveSends(Integer dstUid, long sends) {

		ShardedJedisPool shardPool = null;
		ShardedJedis redis = null;
		try {
			shardPool = RedisBucket.getInstance().getShardPool(redisName);
			redis = shardPool.getResource();
			redis.incrBy(RedisContant.KeyRoomAnchorGetSends + dstUid, sends);
		} catch (Exception e) {
			logger.error("<addNowLiveSends->Exception>" + e.toString());
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
	}

	/**
	 * 清除本场获取的猪头
	 * @param dstUid
	 * @param sends
	 */
	public void delNowLiveSends(Integer dstUid) {

		ShardedJedisPool shardPool = null;
		ShardedJedis redis = null;
		try {
			shardPool = RedisBucket.getInstance().getShardPool(redisName);
			redis = shardPool.getResource();
			redis.del(RedisContant.KeyRoomAnchorGetSends + dstUid);
		} catch (Exception e) {
			logger.error("<delNowLiveSends->Exception>" + e.toString());
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
	}

	/**
	 * 添加本场获取的人气猪
	 * @param dstUid
	 * @param rq
	 */
	public void addNowLiveRq(Integer dstUid, long rq) {

		ShardedJedisPool shardPool = null;
		ShardedJedis redis = null;
		try {
			shardPool = RedisBucket.getInstance().getShardPool(redisName);
			redis = shardPool.getResource();
			redis.incrBy(RedisContant.KeyRoomAnchorGetRq + dstUid, rq);
		} catch (Exception e) {
			logger.error("<addNowLiveRq->Exception>" + e.toString());
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
	}

	/**
	 * 清除本场获取的人气猪
	 * @param dstUid
	 * @param rq
	 */
	public void delNowLiveRq(Integer dstUid) {

		ShardedJedisPool shardPool = null;
		ShardedJedis redis = null;
		try {
			shardPool = RedisBucket.getInstance().getShardPool(redisName);
			redis = shardPool.getResource();
			redis.del(RedisContant.KeyRoomAnchorGetRq + dstUid);
		} catch (Exception e) {
			logger.error("<delNowLiveRq->Exception>" + e.toString());
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
	}

	/**
	 * 获取用户送给主播礼物价值总数
	 * 
	 * @param srcUid
	 * @param dstUid
	 * @return
	 */
	public Double getSrcSendDst(Integer srcUid, Integer dstUid) {
		ShardedJedisPool shardPool = null;
		ShardedJedis redis = null;
		try {
			shardPool = RedisBucket.getInstance().getShardPool(redisName);
			redis = shardPool.getResource();
			Double dbl = redis.zscore(RedisContant.KeySrcSendDstMoney + dstUid, srcUid.toString());
			if (dbl == null) {
				return (double) 0;
			} else {
				return dbl;
			}
		} catch (Exception e) {
			logger.error("<getSrcSendDst->Exception>" + e.toString());
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
		return null;
	}

	/**
	 * 踢人缓存
	 * 
	 * @param anchorUid
	 * @param uid
	 * @return
	 */
	public int setRoomKick(int anchorUid, int uid) {
		Long lg = 0L;

		ShardedJedisPool shardPool = null;
		ShardedJedis redis = null;
		try {
			shardPool = RedisBucket.getInstance().getShardPool(redisName);
			redis = shardPool.getResource();
			lg = redis.hset(RedisContant.keyRoomKick + anchorUid, String.valueOf(uid), "1");
		} catch (Exception e) {
			logger.error("<setRoomKick->Exception>" + e.toString());
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
		return lg == null ? 0 : lg.intValue();
	}

	/**
	 * 判断用户是否被踢出房间
	 * 
	 * @param anchorUid
	 * @param uid
	 * @return true 是 false 否
	 */
	public boolean getRoomKick(int anchorUid, int uid) {
		String strRet = "";

		ShardedJedisPool shardPool = null;
		ShardedJedis redis = null;
		try {
			shardPool = RedisBucket.getInstance().getShardPool(redisName);
			redis = shardPool.getResource();
			strRet = redis.hget(RedisContant.keyRoomKick + anchorUid, String.valueOf(uid));
		} catch (Exception e) {
			logger.error("<getRoomKick->Exception>" + e.toString());
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
		if ("1".equals(strRet)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 开播或停播 清理缓存
	 * 
	 * @param anchorUid
	 * @return
	 */
	public int delRoomKick(int anchorUid) {
		Long lg = 0L;

		ShardedJedisPool shardPool = null;
		ShardedJedis redis = null;
		try {
			shardPool = RedisBucket.getInstance().getShardPool(redisName);
			redis = shardPool.getResource();
			lg = redis.del(RedisContant.keyRoomKick + anchorUid);
		} catch (Exception e) {
			logger.error("<setRoomKick->Exception>" + e.toString());
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
		return lg == null ? 0 : lg.intValue();
	}

	/**
	 * 设置管理员
	 * 
	 * @param anchorUid
	 *            主播uid
	 * @param userUid
	 *            用户uid
	 * @return ＝1成功 其他失败
	 */
	public int setAnchorManage(int anchorUid, int userUid) {
		Long lg = 0L;

		ShardedJedisPool shardPool = null;
		ShardedJedis redis = null;
		try {
			shardPool = RedisBucket.getInstance().getShardPool(redisName);
			redis = shardPool.getResource();
			// 房间中的管理员
			lg = redis.hset(RedisContant.keyRoomManage + String.valueOf(anchorUid), String.valueOf(userUid), "1");
			if (lg > 0) {
				// 用户管理的房间
				redis.hset(RedisContant.keyUserManage + userUid, String.valueOf(anchorUid), "1");
			}
		} catch (Exception e) {
			logger.error("<setAnchorManage->Exception>" + e.toString());
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
		return lg == null ? 0 : lg.intValue();
	}

	/**
	 * 解除管理员
	 * 
	 * @param anchorUid
	 * @param userUid
	 * @return 大于 0 成功 其他失败
	 */
	public int delAnchorManage(int anchorUid, int userUid) {

		Long lg = 0L;

		ShardedJedisPool shardPool = null;
		ShardedJedis redis = null;
		try {
			shardPool = RedisBucket.getInstance().getShardPool(redisName);
			redis = shardPool.getResource();
			lg = redis.hdel(RedisContant.keyRoomManage + String.valueOf(anchorUid), String.valueOf(userUid));
			if (lg > 0) {
				redis.hdel(RedisContant.keyUserManage + userUid, String.valueOf(anchorUid));
			}
		} catch (Exception e) {
			logger.error("<delAnchorManage->Exception>" + e.toString());
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
		return lg == null ? 0 : lg.intValue();
	}

	/**
	 * 获取主播的管理列表
	 * 
	 * @param anchorUid
	 * @return
	 */
	public Map<String, String> getManagelistOfAnchor(int anchorUid) {
		Map<String, String> map = new HashMap<String, String>();

		ShardedJedisPool shardPool = null;
		ShardedJedis redis = null;
		try {
			shardPool = RedisBucket.getInstance().getShardPool(redisName);
			redis = shardPool.getResource();
			map = redis.hgetAll(RedisContant.keyRoomManage + String.valueOf(anchorUid));
		} catch (Exception e) {
			logger.error("<getManagelistOfAnchor->Exception>" + e.toString());
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
		return map;
	}

	/**
	 * 获取用户管理的主播列表
	 * 
	 * @param userUid
	 * @return
	 */
	public Map<String, String> getManagelistByUser(int userUid) {
		Map<String, String> map = new HashMap<String, String>();

		ShardedJedisPool shardPool = null;
		ShardedJedis redis = null;
		try {
			shardPool = RedisBucket.getInstance().getShardPool(redisName);
			redis = shardPool.getResource();
			map = redis.hgetAll(RedisContant.keyUserManage + String.valueOf(userUid));
		} catch (Exception e) {
			logger.error("<getManagelistByUser->Exception>" + e.toString());
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
		return map;
	}

	/**
	 * 判断用户是否未主播的管理员
	 * 
	 * @param anchorUid
	 *            主播uid
	 * @param userUid
	 *            用户uid
	 * @return false 不是 true 是
	 */
	public Boolean checkManageByAnchorUser(int anchorUid, int userUid) {
		String str = "";

		ShardedJedisPool shardPool = null;
		ShardedJedis redis = null;
		try {
			shardPool = RedisBucket.getInstance().getShardPool(redisName);
			redis = shardPool.getResource();
			str = redis.hget(RedisContant.keyRoomManage + String.valueOf(anchorUid), String.valueOf(userUid));
		} catch (Exception e) {
			logger.error("<checkManageByAnchorUser->Exception>" + e.toString());
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
		if (org.apache.commons.lang.StringUtils.isEmpty(str)) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * 添加黑名单
	 * 
	 * @param srcUid
	 *            操作者
	 * @param dstUid
	 *            被拉黑
	 * @param timeAt
	 *            拉黑时间
	 * @return =0 设置失败 其他则设置成功
	 */
	public Long setBlackUser(int srcUid, int dstUid, String timeAt) {

		Long lg = 0L;

		ShardedJedisPool shardPool = null;
		ShardedJedis redis = null;
		try {
			shardPool = RedisBucket.getInstance().getShardPool(redisName);
			redis = shardPool.getResource();
			lg = redis.hset(RedisContant.blacklist + srcUid, String.valueOf(dstUid), timeAt);
		} catch (Exception e) {
			logger.error("<setBlackUser->Exception>" + e.toString());
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
		return lg;
	}

	/**
	 * 判断是否拉黑
	 * 
	 * @param srcUid
	 *            检查 dstUid 是否被 srcUid拉黑
	 * @param dstUid
	 * @return 空 则没有拉黑，其他则说明dstUid被srcUid拉黑
	 */
	public String getBlackUser(int srcUid, int dstUid) {

		String hget = "";

		ShardedJedisPool shardPool = null;
		ShardedJedis redis = null;
		try {
			shardPool = RedisBucket.getInstance().getShardPool(redisName);
			redis = shardPool.getResource();
			hget = redis.hget(RedisContant.blacklist + srcUid, String.valueOf(dstUid));
		} catch (Exception e) {
			logger.error("<getBlackUser->Exception>" + e.toString());
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
		return hget == null ? "" : hget;
	}

	/**
	 * 解除黑名单
	 * 
	 * @param srcUid
	 *            解除 dstUid 被 srcUid拉黑
	 * @param dstUid
	 * @return
	 */
	public Boolean unBlackUser(int srcUid, int dstUid) {
		Long hdel = 0L;
		ShardedJedisPool shardPool = null;
		ShardedJedis redis = null;
		try {
			shardPool = RedisBucket.getInstance().getShardPool(redisName);
			redis = shardPool.getResource();
			hdel = redis.hdel(RedisContant.blacklist + srcUid, String.valueOf(dstUid));
		} catch (Exception e) {
			logger.error("<getBlackUser->Exception>" + e.toString());
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
		if (hdel == null) {
			return false;
		} else {
			return hdel == 0 ? false : true;
		}
	}

	/**
	 * 获取srcUid的黑名单列表
	 * 
	 * @param srcUid
	 * @return
	 */
	public Map<String, String> getBlackList(int srcUid) {
		Map<String, String> hgetAll = null;
		ShardedJedisPool shardPool = null;
		ShardedJedis redis = null;
		try {
			shardPool = RedisBucket.getInstance().getShardPool(redisName);
			redis = shardPool.getResource();
			hgetAll = redis.hgetAll(RedisContant.blacklist + srcUid);
		} catch (Exception e) {
			logger.error("<getBlackList->Exception>" + e.toString());
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
		return hgetAll;
	}

	/**
	 * 本场直播真实人数
	 * 
	 * @param dstUid
	 * @return
	 */
	public void setRealUserInRoom(int dstUid, Double count) {
		ShardedJedisPool shardPool = null;
		ShardedJedis redis = null;
		try {
			shardPool = RedisBucket.getInstance().getShardPool(redisName);
			redis = shardPool.getResource();
			redis.zincrby(RedisContant.roomRealUserNum, count, String.valueOf(dstUid));
		} catch (Exception e) {
			logger.error("计算房间人数异常", e);
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
	}

	/**
	 * 主播关闭房间是清空redis上直播间真实人数
	 * 
	 * @param dstUid
	 * @return
	 */
	public void cleanUsersInRedis(int dstUid) {
		ShardedJedisPool shardPool = null;
		ShardedJedis redis = null;
		try {
			shardPool = RedisBucket.getInstance().getShardPool(redisName);
			redis = shardPool.getResource();
			redis.del(RedisContant.roomUsers + String.valueOf(dstUid));
		} catch (Exception e) {
			logger.error("清除房间人数异常", e);
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
	}

	/**
	 * 主播关闭房间清除机器人信息
	 * 
	 * @param 
	 * @return
	 */
	public void cleanRoomRobots(Integer uid) {
		ShardedJedisPool shardPool = null;
		ShardedJedis redis = null;
		try {
			shardPool = RedisBucket.getInstance().getShardPool(redisName);
			redis = shardPool.getResource();
			redis.del(RedisContant.roomRobots + String.valueOf(uid));
		} catch (Exception e) {
			logger.error("清除房间人数异常", e);
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
	}

	/**
	 * 添加直播间真人信息
	 * 
	 * @param 
	 * @return
	 */
	public void hsetRoomUsers(Integer uid, Integer dstUid) {
		ShardedJedisPool shardPool = null;
		ShardedJedis redis = null;
		try {
			shardPool = RedisBucket.getInstance().getShardPool(redisName);
			redis = shardPool.getResource();
			redis.hset(RedisContant.roomUsers + String.valueOf(dstUid), String.valueOf(uid), String.valueOf(uid));
		} catch (Exception e) {
			logger.error("添加直播间真人信息异常", e);
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
	}
	
	/**
	 * 获取直播间内某个真人
	 * 
	 * @param 
	 * @return
	 */
	public String hgetRoomUser(Integer userId, Integer anchoruid) {
		ShardedJedisPool shardPool = null;
		ShardedJedis redis = null;
		try {
			shardPool = RedisBucket.getInstance().getShardPool(redisName);
			redis = shardPool.getResource();
			return redis.hget(RedisContant.roomUsers + String.valueOf(anchoruid), String.valueOf(userId));
		} catch (Exception e) {
			logger.error("获取直播间内某个真人异常", e);
			return null;
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
	}

	/**
	 * 删除直播间真人信息
	 * 
	 * @param uid
	 * @return
	 */
	public void hdelRoomUsers(Integer uid, Integer dstUid) {
		ShardedJedisPool shardPool = null;
		ShardedJedis redis = null;
		try {
			shardPool = RedisBucket.getInstance().getShardPool(redisName);
			redis = shardPool.getResource();
			redis.hdel(RedisContant.roomUsers + String.valueOf(dstUid), String.valueOf(uid));
		} catch (Exception e) {
			logger.error("删除直播间真人信息异常", e);
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
	}

	/**
	 * 获取微信支付通道
	 * @return
	 */
	public String getWechatPayChanel() {
		String res = null;
		ShardedJedisPool shardPool = null;
		ShardedJedis redis = null;
		try {
			shardPool = RedisBucket.getInstance().getShardPool(redisName);
			redis = shardPool.getResource();
			res = redis.get(RedisContant.wxPayWay);
		} catch (Exception e) {
			logger.error("<getWechatPayChanel->Exception>" + e.toString());
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
		return res;
	}
}