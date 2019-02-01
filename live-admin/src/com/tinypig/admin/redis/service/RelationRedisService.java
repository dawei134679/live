package com.tinypig.admin.redis.service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tinypig.admin.redis.core.RedisBucket;
import com.tinypig.admin.util.RedisContant;
import com.tinypig.admin.util.DateUtil;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Tuple;

/**
 * 关系缓存
 *
 * @author fang
 *
 */
public class RelationRedisService {

	private static final Logger logger = LoggerFactory.getLogger(RelationRedisService.class.getSimpleName());

	private static final Long pageSiz = 20L;
	private static final Long audiences = 20L; // 观众列表每页20个
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
		Jedis redis = RedisBucket.getInstance().initialize(RedisContant.host, RedisContant.port6380);
		try {
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
			redis.close();
		}
		return 0;
	}

	/**
	 * 获取用户是否在30秒内关注同一个主播 true 是，false 否
	 */
	public boolean getFollowsTimes(int srcUid, int dstUid) {
		Jedis redis = RedisBucket.getInstance().initialize(RedisContant.host, RedisContant.port6380);
		String str = "";
		try {
			str = redis.get(RedisContant.keyFollowTimes + srcUid + ":" + dstUid);
		} catch (Exception e) {
			logger.error("<getFollowsTimes->Exception>" + e.toString());
		} finally {
			redis.close();
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
		Jedis redis = RedisBucket.getInstance().initialize(RedisContant.host, RedisContant.port6380);
		try {
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
			redis.close();
		}
	}

	/**
	 * 获取该用户与该主播是否屏蔽消息推送
	 *
	 * @param srcUid
	 * @param dstUid
	 * @return
	 */
	public Boolean getPushFollow(Integer srcUid, Integer dstUid) {
		Jedis redis = RedisBucket.getInstance().initialize(RedisContant.host, RedisContant.port6380);
		String strRes = "";
		try {
			strRes = redis.hget(RedisContant.keyPushMsg + srcUid, String.valueOf(dstUid));
		} catch (Exception e) {
			logger.error("<addFollows->Exception>" + e.toString());
		} finally {
			redis.close();
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
	 * 获取该用户进入房间的总次数，利于关注列表的排序
	 *
	 * @param srcUid
	 *            用户
	 * @param dstUid
	 *            主播
	 * @return
	 */
	public Double isFollows(Integer srcUid, Integer dstUid) {
		Jedis redis = RedisBucket.getInstance().initialize(RedisContant.host, RedisContant.port6380);
		try {
			return redis.zscore(RedisContant.keyFollows + srcUid, dstUid.toString());
		} catch (Exception e) {
			logger.error("<isFollows->Exception>" + e.toString());
		} finally {
			redis.close();
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
		Jedis redis = RedisBucket.getInstance().initialize(RedisContant.host, RedisContant.port6380);
		try {
			return redis.zrevrange(RedisContant.keyFollows + srcUid, 0L, -1L);
		} catch (Exception e) {
			logger.error("<getFollows->Exception>" + e.toString());
		} finally {
			redis.close();
		}
		return null;
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
		Jedis redis = RedisBucket.getInstance().initialize(RedisContant.host, RedisContant.port6380);
		try {
			if ("on".equalsIgnoreCase(type)) {
				return redis.zadd(RedisContant.keyFans + dstUid, score, String.valueOf(srcUid));
			} else {
				return redis.zrem(RedisContant.keyFans + dstUid, String.valueOf(srcUid));
			}
		} catch (Exception e) {
			logger.error("<addFans->Exception>" + e.toString());
		} finally {
			redis.close();
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
		Jedis redis = RedisBucket.getInstance().initialize(RedisContant.host, RedisContant.port6380);
		try {
			return redis.zscore(RedisContant.keyFans + dstUid, srcUid.toString());
		} catch (Exception e) {
			logger.error("<isFans->Exception>" + e.toString());
		} finally {
			redis.close();
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
		Jedis redis = RedisBucket.getInstance().initialize(RedisContant.host, RedisContant.port6380);
		try {
			return redis.zcard(RedisContant.keyFans + uid);
		} catch (Exception e) {
			logger.error("<getFansTotal->Exception>" + e.toString());
		} finally {
			redis.close();
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
		Jedis redis = RedisBucket.getInstance().initialize(RedisContant.host, RedisContant.port6380);
		try {
			redis.incrBy(RedisContant.KeyLikes + uid, step).intValue();
		} catch (Exception e) {
			logger.error("<addLikes->Exception>" + e.toString());
		} finally {
			redis.close();
		}
	}

	/**
	 * 获取用户总喜欢数
	 *
	 * @param uid
	 * @return
	 */
	public int getLikes(int uid) {
		Jedis redis = RedisBucket.getInstance().initialize(RedisContant.host, RedisContant.port6380);
		String str = "";
		try {
			str = redis.get(RedisContant.KeyLikes + uid);

		} catch (Exception e) {
			logger.error("<getLikes->Exception>" + e.toString());
		} finally {
			redis.close();
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
		Jedis redis = RedisBucket.getInstance().initialize(RedisContant.host, RedisContant.port6380);
		try {
			if (pages > 0) {
				return redis.zrevrange(RedisContant.keyFans + String.valueOf(uid), (pages - 1) * pageSiz,
						pages * pageSiz - 1);
			} else {
				return redis.zrevrange(RedisContant.keyFans + String.valueOf(uid), 0L, -1L);
			}
		} catch (Exception e) {
			logger.error("<getFans->Exception>" + e.toString());
		} finally {
			redis.close();
		}
		return null;
	}

	/**
	 * 判断是否关注关系
	 *
	 * @param srcUid
	 *            srcUid 是否关注了dstUid
	 * @param dstUid
	 * @return
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
		Jedis redis = RedisBucket.getInstance().initialize(RedisContant.host, RedisContant.port6380);
		try {
			return redis.incrBy(RedisContant.KeyRoomLikes + uid, step);
		} catch (Exception e) {
			logger.error("<addRoomLikes->Exception>" + e.toString());
		} finally {
			redis.close();
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
		Jedis redis = RedisBucket.getInstance().initialize(RedisContant.host, RedisContant.port6380);
		try {
			String str = redis.get(RedisContant.KeyRoomLikes + uid);
			if (str == null || "".equals(str)) {
				return 0;
			} else {
				return Integer.valueOf(str);
			}
		} catch (Exception e) {
			logger.error("<getRoomLikes->Exception>" + e.toString());
		} finally {
			redis.close();
		}
		return 0;
	}

	/**
	 * 开播时 清空数据
	 *
	 * @param uid
	 */
	public void delRoomLikes(int uid) {
		Jedis redis = RedisBucket.getInstance().initialize(RedisContant.host, RedisContant.port6380);
		try {
			redis.del(RedisContant.KeyRoomLikes + uid);
		} catch (Exception e) {
			logger.error("<delRoomLikes->Exception>" + e.toString());
		} finally {
			redis.close();
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
		Jedis redis = RedisBucket.getInstance().initialize(RedisContant.host, RedisContant.port6380);

		try {
			Long times = System.currentTimeMillis() / 1000;

			String srcU = String.valueOf(srcUid);
			String dstU = String.valueOf(dstUid);

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
			redis.close();
		}
	}

	/**
	 * 获取本场直播进入的人数
	 * @param anchor
	 * @return
	 */
	public int getLiveEnterTotal(int anchor) {

        Jedis redis = RedisBucket.getInstance().initialize(RedisContant.host, RedisContant.port6380);
		Map<String, String> map = new HashMap<String, String>();
		try {
			map = redis.hgetAll(RedisContant.keyLiveEnterTotal + anchor);
		} catch (Exception e) {
			logger.error("<delLiveEnterTotal->Exception>" + e.toString());
		} finally {
			redis.close();
		}
		if (map == null) {
			return 0;
		} else {
			return map.size();
		}
	}
	/**
	 * 判断是否为主播
	 *
	 * @param anchor
	 * @return
	 */
	public Boolean checkAnchor(int anchor) {
		Jedis redis = RedisBucket.getInstance().initialize(RedisContant.host, RedisContant.port6380);
		Boolean bl = false;
		try {
			bl = redis.exists(RedisContant.KeyAnchorAndUser + anchor);
		} catch (Exception e) {
			logger.error("<checkAnchor->Exception>" + e.toString());
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
		Jedis redis = RedisBucket.getInstance().initialize(RedisContant.host, RedisContant.port6380);
		try {
			return redis.hgetAll(RedisContant.KeyUserEnterLong + uid);
		} catch (Exception e) {
			logger.error("<getUsersTimeInRoom->Exception>" + e.toString());
		} finally {
			redis.close();
		}
		return null;
	}

	/**
	 * 用户离开房间
	 *
	 * @param srcUid
	 * @param dstUid
	 * @return 进入房间时间
	 */
	public Long userExitRoom(int srcUid, int dstUid) {
		Jedis redis = RedisBucket.getInstance().initialize(RedisContant.host, RedisContant.port6380);

		try {

			// 删除用户跟主播关系(1:1)
			redis.hdel(RedisContant.KeyUserAndAnchor, String.valueOf(srcUid));
			// 获取用户在该房间的时长
			String str = redis.hget(RedisContant.KeyUserEnterLong + dstUid, String.valueOf(srcUid));
			// 删除该房间的该用户
			redis.zrem(RedisContant.KeyAnchorAndUser + dstUid, String.valueOf(srcUid));
			if (str == null) {
				return 0L;
			} else {
				return Long.valueOf(str);
			}
		} catch (Exception e) {
			logger.error("<userExitRoom->Exception>" + e.toString());
		} finally {
			redis.close();
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
	public Set<String> getUserListInAnchor(Integer dstUid, Long page) {
		Jedis redis = RedisBucket.getInstance().initialize(RedisContant.host, RedisContant.port6380);
		try {
			return redis.zrevrange(RedisContant.KeyAnchorAndUser + dstUid.toString(), (page - 1) * audiences,
					page * audiences - 1);
		} catch (Exception e) {
			logger.error("<getUserListInAnchor->Exception>" + e.toString());
		} finally {
			redis.close();
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
		Jedis redis = RedisBucket.getInstance().initialize(RedisContant.host, RedisContant.port6380);
		Long lg = 0L;
		try {
			lg = redis.zcard(RedisContant.KeyAnchorAndUser + dstUid.toString());
		} catch (Exception e) {
			logger.error("<getUsersInAnchor->Exception>" + e.toString());
		} finally {
			redis.close();
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
		Jedis redis = RedisBucket.getInstance().initialize(RedisContant.host, RedisContant.port6380);

		try {
			String strRed = redis.get(RedisContant.KeyRoomTimes + dstUid);
			if (strRed == null || "".equals(strRed)) {
				return 0;
			} else {
				return Integer.valueOf(strRed);
			}
		} catch (Exception e) {
			logger.error("<getEnterRoomTimes->Exception>" + e.toString());
		} finally {
			redis.close();
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
		Jedis redis = RedisBucket.getInstance().initialize(RedisContant.host, RedisContant.port6380);
		try {
			Double dl = redis.zscore(RedisContant.KeyAnchorAndUser + dstUid, String.valueOf(srcUid));
			if (dl == null) {
				redis.zadd(RedisContant.KeyAnchorAndUser + dstUid, gets, String.valueOf(srcUid));
			} else {
				redis.zadd(RedisContant.KeyAnchorAndUser + dstUid, gets + dl, String.valueOf(srcUid));
			}
		} catch (Exception e) {
			logger.error("<sendGift->Exception>" + e.toString());
		} finally {
			redis.close();
		}

	}

	/**
	 * 获取该用户所在的房间
	 *
	 * @param srcUid
	 * @return
	 */
	public String getUserAnchor(Integer srcUid) {
		Jedis redis = RedisBucket.getInstance().initialize(RedisContant.host, RedisContant.port6380);
		String anchor = "";
		try {
			anchor = redis.hget(RedisContant.KeyUserAndAnchor, srcUid.toString());
		} catch (Exception e) {
			logger.error("<delRoomLikes->Exception>" + e.toString());
		} finally {
			redis.close();
		}
		return anchor;

	}

	/**
	 * 开播时 清空数据
	 *
	 * @param uid
	 */
	public void delEnterRoomTimes(int dstUid) {
		Jedis redis = RedisBucket.getInstance().initialize(RedisContant.host, RedisContant.port6380);
		try {
			// 进房间人次
			redis.del(RedisContant.KeyRoomTimes + dstUid);
			// 进房间用户时间列表
			redis.del(RedisContant.KeyUserEnterLong + dstUid);
			// 进房间观众列表
			redis.del(RedisContant.KeyAnchorAndUser + dstUid);

		} catch (Exception e) {
			logger.error("<delRoomLikes->Exception>" + e.toString());
		} finally {
			redis.close();
		}
	}

	/**
	 * 记录主播当天的声援值
	 *
	 * @param uid
	 * @param gets
	 */
	public void addRoomCreditToday(int uid, int gets) {

		Jedis redis = RedisBucket.getInstance().initialize(RedisContant.host, RedisContant.port6380);
		DateFormat df = new SimpleDateFormat("yyyyMMdd");
		String strToday = df.format(new Date());
		Date dt = null;
		try {
			dt = df.parse(strToday);
			String redisKey = RedisContant.KeyRoomGetsToday + uid + ":" + strToday;

			String oldGets = redis.get(redisKey);
			if (oldGets == null || "".equals(oldGets)) {
				redis.set(redisKey, String.valueOf(gets));
				redis.expireAt(redisKey, DateUtil.getTimeStamp(dt) + 86400);
			} else {
				gets = Integer.valueOf(oldGets) + gets;
				redis.set(redisKey, String.valueOf(gets));
			}
		} catch (Exception e) {
			System.out.println("dayerr:" + e.toString());
			logger.error("<addRoomCreditToday->Exception>" + e.toString());
		} finally {
			redis.close();
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
		Jedis redis = RedisBucket.getInstance().initialize(RedisContant.host, RedisContant.port6380);
		try {
			Double olddb = redis.zscore(RedisContant.KeyDstGetsSrcMoney + dstUid, srcUid.toString());
			if (olddb != null) {
				dbl = dbl + olddb;
			}
			// 安装声援值排序
			redis.zadd(RedisContant.KeyDstGetsSrcMoney + dstUid, dbl, srcUid.toString());
		} catch (Exception e) {
			logger.error("<addDstGetSrc->Exception>" + e.toString());
		} finally {
			redis.close();
		}
	}

	/**
	 * 获取该主播有多少人送礼
	 *
	 * @param uid
	 * @return
	 */
	public Long getDstGetNumber(Integer uid) {
		Jedis redis = RedisBucket.getInstance().initialize(RedisContant.host, RedisContant.port6380);
		Long lg = 0L;
		try {
			lg = redis.zcard(RedisContant.KeyDstGetsSrcMoney + uid);
		} catch (Exception e) {
			logger.error("<getDstGetNumber->Exception>" + e.toString());
		} finally {
			redis.close();
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
		Jedis redis = RedisBucket.getInstance().initialize(RedisContant.host, RedisContant.port6380);
		try {
			return redis.zrevrank(RedisContant.KeyDstGetsSrcMoney + dstUid, srcUid.toString());
		} catch (Exception e) {
			logger.error("<getSrcInDstSort->Exception>" + e.toString());
		} finally {
			redis.close();
		}
		return null;
	}

	/**
	 * 获取该主播的声援排榜
	 *
	 * @param uid
	 * @param page
	 * @return
	 */
	public Set<Tuple> getSrcListInDst(Integer uid, int page) {
		Jedis redis = RedisBucket.getInstance().initialize(RedisContant.host, RedisContant.port6380);
		try {
			return redis.zrevrangeWithScores(RedisContant.KeyDstGetsSrcMoney + uid, (page - 1) * pageSiz,
					page * pageSiz - 1);
		} catch (Exception e) {
			logger.error("<getSrcListInDst->Exception>" + e.toString());
		} finally {
			redis.close();
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
		Jedis redis = RedisBucket.getInstance().initialize(RedisContant.host, RedisContant.port6380);
		try {

			Double olddb = redis.zscore(RedisContant.KeySrcSendDstMoney + dstUid, srcUid.toString());
			if (olddb != null) {
				dbl = dbl + olddb;
			}
			// 安装声援值排序
			redis.zadd(RedisContant.KeySrcSendDstMoney + dstUid, dbl, srcUid.toString());
		} catch (Exception e) {
			logger.error("<addSrcSendDst->Exception>" + e.toString());
		} finally {
			redis.close();
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
		Jedis redis = RedisBucket.getInstance().initialize(RedisContant.host, RedisContant.port6380);
		try {
			Double dbl = redis.zscore(RedisContant.KeySrcSendDstMoney + dstUid, srcUid.toString());
			if (dbl == null) {
				return (double) 0;
			} else {
				return dbl;
			}
		} catch (Exception e) {
			logger.error("<getSrcSendDst->Exception>" + e.toString());
		} finally {
			redis.close();
		}
		return null;
	}

	/**
	 * 获取主播当天的声援值
	 *
	 * @param uid
	 * @return
	 */
	public int getRoomCreditToday(int uid) {
		Jedis redis = RedisBucket.getInstance().initialize(RedisContant.host, RedisContant.port6380);

		try {
			DateFormat df = new SimpleDateFormat("yyyyMMdd");
			String strToday = df.format(new Date());
			String redisKey = RedisContant.KeyRoomGetsToday + uid + ":" + strToday;

			String gets = redis.get(redisKey);
			if (gets == null || "".equals(gets)) {
				return 0;
			} else {
				return Integer.valueOf(gets);
			}
		} catch (Exception e) {
			logger.error("<getRoomCreditToday->Exception>" + e.toString());
		} finally {
			redis.close();
		}
		return 0;
	}

	/**
	 * 踢人缓存
	 *
	 * @param anchorUid
	 * @param uid
	 * @return
	 */
	public int setRoomKick(int anchorUid, int uid) {
		Jedis redis = RedisBucket.getInstance().initialize(RedisContant.host, RedisContant.port6380);
		Long lg = 0L;
		try {
			lg = redis.hset(RedisContant.keyRoomKick + anchorUid, String.valueOf(uid), "1");
		} catch (Exception e) {
			logger.error("<setRoomKick->Exception>" + e.toString());
		} finally {
			redis.close();
		}
		return lg.intValue();
	}

	/**
	 * 判断用户是否被踢出房间
	 *
	 * @param anchorUid
	 * @param uid
	 * @return true 是 false 否
	 */
	public boolean getRoomKick(int anchorUid, int uid) {
		Jedis redis = RedisBucket.getInstance().initialize(RedisContant.host, RedisContant.port6380);
		String strRet = "";
		try {
			strRet = redis.hget(RedisContant.keyRoomKick + anchorUid, String.valueOf(uid));
		} catch (Exception e) {
			logger.error("<getRoomKick->Exception>" + e.toString());
		} finally {
			redis.close();
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
		Jedis redis = RedisBucket.getInstance().initialize(RedisContant.host, RedisContant.port6380);
		Long lg = 0L;
		try {
			lg = redis.del(RedisContant.keyRoomKick + anchorUid);
		} catch (Exception e) {
			logger.error("<setRoomKick->Exception>" + e.toString());
		} finally {
			redis.close();
		}
		return lg.intValue();
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
		Jedis redis = RedisBucket.getInstance().initialize(RedisContant.host, RedisContant.port6380);
		try {
			// 房间中的管理员
			lg = redis.hset(RedisContant.keyRoomManage + String.valueOf(anchorUid), String.valueOf(userUid), "1");
			if (lg > 0) {
				// 用户管理的房间
				redis.hset(RedisContant.keyUserManage + userUid, String.valueOf(anchorUid), "1");
			}
		} catch (Exception e) {
			logger.error("<setAnchorManage->Exception>" + e.toString());
		} finally {
			redis.close();
		}
		return lg.intValue();
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
		Jedis redis = RedisBucket.getInstance().initialize(RedisContant.host, RedisContant.port6380);
		try {
			lg = redis.hdel(RedisContant.keyRoomManage + String.valueOf(anchorUid), String.valueOf(userUid));
			if (lg > 0) {
				redis.hdel(RedisContant.keyUserManage + userUid, String.valueOf(anchorUid));
			}
		} catch (Exception e) {
			logger.error("<delAnchorManage->Exception>" + e.toString());
		} finally {
			redis.close();
		}
		return lg.intValue();
	}

	/**
	 * 获取主播的管理列表
	 *
	 * @param anchorUid
	 * @return
	 */
	public Map<String, String> getManagelistOfAnchor(int anchorUid) {
		Map<String, String> map = new HashMap<String, String>();
		Jedis redis = RedisBucket.getInstance().initialize(RedisContant.host, RedisContant.port6380);
		try {
			map = redis.hgetAll(RedisContant.keyRoomManage + String.valueOf(anchorUid));
		} catch (Exception e) {
			logger.error("<getManagelistOfAnchor->Exception>" + e.toString());
		} finally {
			redis.close();
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
		Jedis redis = RedisBucket.getInstance().initialize(RedisContant.host, RedisContant.port6380);
		try {
			map = redis.hgetAll(RedisContant.keyUserManage + String.valueOf(userUid));
		} catch (Exception e) {
			logger.error("<getManagelistByUser->Exception>" + e.toString());
		} finally {
			redis.close();
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
		Jedis redis = RedisBucket.getInstance().initialize(RedisContant.host, RedisContant.port6380);
		try {
			str = redis.hget(RedisContant.keyRoomManage + String.valueOf(anchorUid), String.valueOf(userUid));
		} catch (Exception e) {
			logger.error("<checkManageByAnchorUser->Exception>" + e.toString());
		} finally {
			redis.close();
		}
		if (org.apache.commons.lang.StringUtils.isEmpty(str)) {
			return false;
		} else {
			return true;
		}
	}
}
