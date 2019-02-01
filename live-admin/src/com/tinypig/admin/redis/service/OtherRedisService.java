package com.tinypig.admin.redis.service;

import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;
import com.tinypig.admin.redis.core.RedisBucket;
import com.tinypig.admin.util.RedisContant;

import redis.clients.jedis.Jedis;

/**
 * 缓存其他的
 *
 * @author fang
 */
public class OtherRedisService {
	private static final Logger logger = LoggerFactory.getLogger(OtherRedisService.class.getSimpleName());

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
		Jedis redis = RedisBucket.getInstance().initialize(RedisContant.host, RedisContant.port6381);
		try {
			redis.hset(RedisContant.keyStartLive, uid.toString(), micid + "," + times);
		} catch (Exception e) {
			logger.error("<delRoomLikes->Exception>" + e.toString());
		} finally {
			redis.close();
		}
	}

	/**
	 * 获取主播最后一次心跳时间
	 *
	 * @param uid
	 * @return
	 */
	public String getRoomEndTime(Integer uid) {
		Jedis redis = RedisBucket.getInstance().initialize(RedisContant.host, RedisContant.port6381);
		try {
			return redis.hget(RedisContant.keyStartLive, uid.toString());
		} catch (Exception e) {
			logger.error("<getRoomEndTime->Exception>" + e.toString());
		} finally {
			redis.close();
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
		Jedis redis = RedisBucket.getInstance().initialize(RedisContant.host, RedisContant.port6381);
		try {
			return redis.hdel(RedisContant.keyStartLive, uid.toString());
		} catch (Exception e) {
			logger.error("<delRoomLikes->Exception>" + e.toString());
		} finally {
			redis.close();
		}
		return null;

	}
	/**
	 * 开播头牌列表
	 */
	
	public Set<String> getHeadRoom(int page) {
        Jedis redis = RedisBucket.getInstance().initialize(RedisContant.host, RedisContant.port6381);
        try {
        	if (page > 0) {
				return redis.zrevrange(RedisContant.keyRoomHead, (page-1)*pageSiz, page*pageSiz-1);
			}else{
	            return redis.zrevrange(RedisContant.keyRoomHead, 0, -1);
			}
        } catch (Exception e) {
            logger.error("<getHeadRoom->Exception>" + e.toString());
        } finally {
            redis.close();
        }
        return null;
    }
	/**
	 * 添加头牌列表
	 */
	public void addHeadRoom(int uid, int recommend, int rq) {

		Jedis redis = RedisBucket.getInstance().initialize(RedisContant.host, RedisContant.port6381);

		String str = String.valueOf(recommend) + String.format("%07d", rq);
		try {
			redis.zadd(RedisContant.keyRoomHead, Double.valueOf(str), String.valueOf(uid));
		} catch (Exception e) {
			logger.error("<addHeadRoom->Exception>" + e.toString());
		} finally {
			redis.close();
		}

	}
	/**
	 * 头牌开播数量
	 * @return
	 */
	public int getHeadRoomAll(){
        Jedis redis = RedisBucket.getInstance().initialize(RedisContant.host, RedisContant.port6381);
        try {
        	return redis.zcard(RedisContant.keyRoomHead).intValue();
        } catch (Exception e) {
            logger.error("<getHeadRoomAll->Exception>" + e.toString());
        } finally {
            redis.close();
        }
        return 0;
    }

	/**
	 * 获取头牌列表中其中的一个用户
	 * 
	 * @param uid
	 * @return
	 */
	public Double getHeadRoomInfo(int uid) {
		Jedis redis = RedisBucket.getInstance().initialize(RedisContant.host, RedisContant.port6381);
		Double dbl = null;
		try {
			dbl = redis.zscore(RedisContant.keyRoomHead, String.valueOf(uid));
		} catch (Exception e) {
			logger.error("<getHeadRoomInfo->Exception>" + e.toString());
		} finally {
			redis.close();
		}
		return dbl;
	}

	/**
	 * 删除开播头牌列表
	 *
	 * @param uid
	 * @return
	 */
	public Long delHeadRoom(int uid) {
		Jedis redis = RedisBucket.getInstance().initialize(RedisContant.host, RedisContant.port6381);
		try {
			return redis.zrem(RedisContant.keyRoomHead, String.valueOf(uid));
		} catch (Exception e) {
			logger.error("<delHeadRoom->Exception>" + e.toString());
		} finally {
			redis.close();
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
	 */
	public void addRecommendRoom(int uid, int recommend, int rq) {

		Jedis redis = RedisBucket.getInstance().initialize(RedisContant.host, RedisContant.port6381);

		String str = String.valueOf(recommend) + String.format("%07d", rq);
		try {
			redis.zadd(RedisContant.KeyRoomRecommend, Double.valueOf(str), String.valueOf(uid));
		} catch (Exception e) {
			logger.error("<addRecommendRoom->Exception>" + e.toString());
		} finally {
			redis.close();
		}

	}

	/**
     * 获取开播推荐列表
     *
     * @param page
     * @return
     */
    public Set<String> getRecommendRoom(int page) {
        Jedis redis = RedisBucket.getInstance().initialize(RedisContant.host, RedisContant.port6381);
        try {
        	if (page > 0) {
				return redis.zrevrange(RedisContant.KeyRoomRecommend, (page-1)*pageSiz, page*pageSiz-1);
			}else{
	            return redis.zrevrange(RedisContant.KeyRoomRecommend, 0, -1);
			}
        } catch (Exception e) {
            logger.error("<getRecommendRoom->Exception>" + e.toString());
        } finally {
            redis.close();
        }
        return null;
    }
    
    public int getRecommendRoomAll(){
        Jedis redis = RedisBucket.getInstance().initialize(RedisContant.host, RedisContant.port6381);
        try {
        	return redis.zcard(RedisContant.KeyRoomRecommend).intValue();
        } catch (Exception e) {
            logger.error("<getRecommendRoomAll->Exception>" + e.toString());
        } finally {
            redis.close();
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
		Jedis redis = RedisBucket.getInstance().initialize(RedisContant.host, RedisContant.port6381);
		Double dbl = null;
		try {
			dbl = redis.zscore(RedisContant.KeyRoomRecommend, String.valueOf(uid));
		} catch (Exception e) {
			logger.error("<getRecommendRoomInfo->Exception>" + e.toString());
		} finally {
			redis.close();
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
		Jedis redis = RedisBucket.getInstance().initialize(RedisContant.host, RedisContant.port6381);
		try {
			return redis.zrem(RedisContant.KeyRoomRecommend, String.valueOf(uid));
		} catch (Exception e) {
			logger.error("<delRecommendRoom->Exception>" + e.toString());
		} finally {
			redis.close();
		}
		return null;

	}

	/**
	 * 添加热门开播主播
	 * 
	 * @param uid
	 * @param host
	 * @param rq
	 */
	public void addhotRoom(int uid, int host, int rq) {
		Jedis redis = RedisBucket.getInstance().initialize(RedisContant.host, RedisContant.port6381);
		String str = String.valueOf(host) + String.format("%07d", rq);
		try {
			redis.zadd(RedisContant.KeyRoomHot, Double.valueOf(str), String.valueOf(uid));
		} catch (Exception e) {
			logger.error("<addhotRoom->Exception>" + e.toString());
		} finally {
			redis.close();
		}
	}

	/**
	 * 获取开播最新列表
	 * 
	 * @param page
	 * @return
	 */
	public Set<String> getHotRoom(int page) {
		Jedis redis = RedisBucket.getInstance().initialize(RedisContant.host, RedisContant.port6381);
		try {
			if (page > 0) {
				return redis.zrevrange(RedisContant.KeyRoomHot, (page-1)*pageSiz, page*pageSiz-1);
			}
			return redis.zrevrange(RedisContant.KeyRoomHot, 0, -1);
		} catch (Exception e) {
			logger.error("<getHotRoom->Exception>" + e.toString());
		} finally {
			redis.close();
		}
		return null;
	}
    
    public int getHotRoomAll(){
        Jedis redis = RedisBucket.getInstance().initialize(RedisContant.host, RedisContant.port6381);
        try {
        	return redis.zcard(RedisContant.KeyRoomHot).intValue();
        } catch (Exception e) {
            logger.error("<getHotRoomAll->Exception>" + e.toString());
        } finally {
            redis.close();
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
		Jedis redis = RedisBucket.getInstance().initialize(RedisContant.host, RedisContant.port6381);
		Double dbl = null;
		try {
			dbl = redis.zscore(RedisContant.KeyRoomHot, String.valueOf(uid));
		} catch (Exception e) {
			logger.error("<getHotRoomInfo->Exception>" + e.toString());
		} finally {
			redis.close();
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
		Jedis redis = RedisBucket.getInstance().initialize(RedisContant.host, RedisContant.port6381);
		try {
			return redis.zrem(RedisContant.KeyRoomHot, String.valueOf(uid));
		} catch (Exception e) {
			logger.error("<delHotRoom->Exception>" + e.toString());
		} finally {
			redis.close();
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
	public void addBaseRoom(int uid, int host, int rq) {
		Jedis redis = RedisBucket.getInstance().initialize(RedisContant.host, RedisContant.port6381);
		String str = String.valueOf(host) + String.format("%07d", rq);
		try {
			redis.zadd(RedisContant.keyRoomBase, Double.valueOf(str), String.valueOf(uid));
		} catch (Exception e) {
			logger.error("<addBaseRoom->Exception>" + e.toString());
		} finally {
			redis.close();
		}
	}

	/**
	 * 获取开播普通列表
	 * 
	 * @param page
	 * @return
	 */
	public Set<String> getBaseRoom(int page) {
		Jedis redis = RedisBucket.getInstance().initialize(RedisContant.host, RedisContant.port6381);
		try {
			if (page > 0) {
				return redis.zrevrange(RedisContant.keyRoomBase, (page-1)*pageSiz, page*pageSiz-1);
			}
			return redis.zrevrange(RedisContant.keyRoomBase, 0, -1);
		} catch (Exception e) {
			logger.error("<getBaseRoom->Exception>" + e.toString());
		} finally {
			redis.close();
		}
		return null;
	}
	
    public int getBaseRoomAll(){
        Jedis redis = RedisBucket.getInstance().initialize(RedisContant.host, RedisContant.port6381);
        try {
        	return redis.zcard(RedisContant.keyRoomBase).intValue();
        } catch (Exception e) {
            logger.error("<getBaseRoomAll->Exception>" + e.toString());
        } finally {
            redis.close();
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
		Jedis redis = RedisBucket.getInstance().initialize(RedisContant.host, RedisContant.port6381);
		Double dbl = null;
		try {
			dbl = redis.zscore(RedisContant.keyRoomBase, String.valueOf(uid));
		} catch (Exception e) {
			logger.error("<getBaseRoomInfo->Exception>" + e.toString());
		} finally {
			redis.close();
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
		Jedis redis = RedisBucket.getInstance().initialize(RedisContant.host, RedisContant.port6381);
		try {
			return redis.zrem(RedisContant.keyRoomBase, String.valueOf(uid));
		} catch (Exception e) {
			logger.error("<delBaseRoom->Exception>" + e.toString());
		} finally {
			redis.close();
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
		Jedis redis = RedisBucket.getInstance().initialize(RedisContant.host, RedisContant.port6381);
		try {
			return redis.incr(RedisContant.KeyRoomReports + dstUid).intValue();
		} catch (Exception e) {
			logger.error("<addReport->Exception>" + e.toString());
		} finally {
			redis.close();
		}
		return 0;
	}
	
	/**
	 * 获取房间举报数
	 *
	 * @param dstUid
	 * @return
	 */
	public String getReports(int dstUid) {
		Jedis redis = RedisBucket.getInstance().initialize(RedisContant.host, RedisContant.port6381);
		String strCnt = "";
		try {
			strCnt = redis.get(RedisContant.KeyRoomReports + dstUid);
		} catch (Exception e) {
			logger.error("<delReport->Exception>" + e.toString());
		} finally {
			redis.close();
		}
		if (StringUtils.isEmpty(strCnt)) {
			strCnt = "";
		}
		return strCnt;
	}

	/**
	 * 处理房间举报数
	 *
	 * @param dstUid
	 * @return
	 */
	public int delReport(int dstUid) {
		Jedis redis = RedisBucket.getInstance().initialize(RedisContant.host, RedisContant.port6381);
		try {
			return redis.del(RedisContant.KeyRoomReports + dstUid).intValue();
		} catch (Exception e) {
			logger.error("<delReport->Exception>" + e.toString());
		} finally {
			redis.close();
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
		Jedis redis = RedisBucket.getInstance().initialize(RedisContant.host, RedisContant.port6381);
		try {
			redis.setex(RedisContant.KeyHttpReq + ncode, seconds, ncode);
		} catch (Exception e) {
			logger.error("<setNcode->Exception>" + e.toString());
		} finally {
			redis.close();
		}
	}

	/**
	 * 获取ncode
	 *
	 * @param ncode
	 * @return
	 */
	public String getNcode(String ncode) {
		Jedis redis = RedisBucket.getInstance().initialize(RedisContant.host, RedisContant.port6381);
		try {
			return redis.get(RedisContant.KeyHttpReq + ncode);
		} catch (Exception e) {
			logger.error("<getNcode->Exception>" + e.toString());
		} finally {
			redis.close();
		}
		return null;
	}

	/**
	 * 缓存当天对指定手机发验证码的次数
	 *
	 * @param mobile
	 */
	public void setSendCodeTimes(String mobile, int seconds) {
		Jedis redis = RedisBucket.getInstance().initialize(RedisContant.host, RedisContant.port6381);
		try {
			redis.incr(RedisContant.KeyMobileSendCodeOfDay + mobile);
			if (seconds > 0) {
				redis.expire(RedisContant.KeyMobileSendCodeOfDay + mobile, seconds);
			}
		} catch (Exception e) {
			logger.error("<setSendCodeTimes->Exception>" + e.toString());
		} finally {
			redis.close();
		}
	}

	/**
	 * 获取指定手机验证码发送的次数
	 *
	 * @param mobile
	 * @return
	 */
	public int getSendCodeTimes(String mobile) {
		Jedis redis = RedisBucket.getInstance().initialize(RedisContant.host, RedisContant.port6381);
		String times = "";
		try {
			times = redis.get(RedisContant.KeyMobileSendCodeOfDay + mobile);
		} catch (Exception e) {
			logger.error("<getSendCodeTimes->Exception>" + e.toString());
		} finally {
			redis.close();
		}
		if (times == null || times == "") {
			return 0;
		} else {
			return Integer.valueOf(times);
		}
	}

	/**
	 * 缓慢发送给手机的验证码
	 *
	 * @param mobile
	 * @param code
	 * @param seconds
	 */
	public void setSendCode(String mobile, String code, int seconds) {

		Jedis redis = RedisBucket.getInstance().initialize(RedisContant.host, RedisContant.port6381);
		try {
			redis.setex(RedisContant.KeyMobileSendCode + mobile, seconds, code);
		} catch (Exception e) {
			logger.error("<setSendCodeTimes->Exception>" + e.toString());
		} finally {
			redis.close();
		}
	}

	/**
	 * 手机验证码 验证
	 *
	 * @param mobile
	 * @return
	 */
	public Boolean getSendCode(String mobile, String code) {
		Jedis redis = RedisBucket.getInstance().initialize(RedisContant.host, RedisContant.port6381);
		try {
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
			logger.error("<getSendCode->Exception>" + e.toString());
		} finally {
			redis.close();
		}
		return false;
	}

	/**
	 * 记录uid产生的订单，反刷（订单支付成功后 清空）
	 *
	 * @param uid
	 */
	public void addPayRecord(Integer uid) {
		Jedis redis = RedisBucket.getInstance().initialize(RedisContant.host, RedisContant.port6381);
		try {
			if (redis.exists(RedisContant.KeyPayRecord + uid)) {
				redis.incr(RedisContant.KeyPayRecord + uid);
			} else {
				redis.incr(RedisContant.KeyPayRecord + uid);
				redis.expire(RedisContant.KeyPayRecord + uid, 300);
			}
		} catch (Exception e) {
			logger.error("<addPayRecord->Exception>" + e.toString());
		} finally {
			redis.close();
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
		Jedis redis = RedisBucket.getInstance().initialize(RedisContant.host, RedisContant.port6381);
		try {
			icnt = redis.get(RedisContant.KeyPayRecord + uid);
		} catch (Exception e) {
			logger.error("<addPayRecord->Exception>" + e.toString());
		} finally {
			redis.close();
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
		Jedis redis = RedisBucket.getInstance().initialize(RedisContant.host, RedisContant.port6381);
		try {
			redis.del(RedisContant.KeyPayRecord + uid);
		} catch (Exception e) {
			logger.error("<addPayRecord->Exception>" + e.toString());
		} finally {
			redis.close();
		}
	}

	/**
	 * 新增单笔提现金额限制
	 *
	 * @param limit
	 */
	public void setWithDrawLimit(int limit) {
		Jedis redis = RedisBucket.getInstance().initialize(RedisContant.host, RedisContant.port6381);
		try {
			redis.set(RedisContant.keyWithDrawLimit, String.valueOf(limit));
		} catch (Exception e) {
			logger.error("<addWithDrawLimit->Exception>" + e.toString());
		} finally {
			redis.close();
		}
	}

	/**
	 * 获取单笔提现金额限制
	 *
	 * @return
	 */
	public int getWithDrawLimit() {
		Jedis redis = RedisBucket.getInstance().initialize(RedisContant.host, RedisContant.port6381);
		String limit = "";
		try {
			limit = redis.get(RedisContant.keyWithDrawLimit);
		} catch (Exception e) {
			logger.error("<getWithDrawLimit->Exception>" + e.toString());
		} finally {
			redis.close();
		}
		if (limit == null) {
			return 0;
		} else {
			return Integer.valueOf(limit);
		}
	}

	/**
	 * 记录提现次数
	 *
	 * @param uid
	 */
	public void setWithDraw(int uid) {
		Jedis redis = RedisBucket.getInstance().initialize(RedisContant.host, RedisContant.port6381);
		try {
			redis.incr(RedisContant.keyWithDraws + uid);
		} catch (Exception e) {
			logger.error("<setWithDraw->Exception>" + e.toString());
		} finally {
			redis.close();
		}
	}

	/**
	 * 获取提现次数
	 *
	 * @param uid
	 * @return
	 */
	public int getWithDraw(int uid) {
		Jedis redis = RedisBucket.getInstance().initialize(RedisContant.host, RedisContant.port6381);
		String times = "";
		try {
			times = redis.get(RedisContant.keyWithDraws + uid);
		} catch (Exception e) {
			logger.error("<getWithDraw->Exception>" + e.toString());
		} finally {
			redis.close();
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
		Jedis redis = RedisBucket.getInstance().initialize(RedisContant.host, RedisContant.port6381);
		try {
			redis.setex(RedisContant.keyLivingClick + uid, 1, "1");
		} catch (Exception ex) {
			logger.error("<addLivingClick->Exception>" + ex.toString());
		} finally {
			redis.close();
		}
	}

	/**
	 * 获取该用户开播的频繁记录
	 *
	 * @param uid
	 * @return false 不频繁 true 频繁
	 */
	public boolean getLivingClick(int uid) {
		Jedis redis = RedisBucket.getInstance().initialize(RedisContant.host, RedisContant.port6381);
		String str = "";
		try {
			str = redis.get(RedisContant.keyLivingClick + uid);
		} catch (Exception e) {
			logger.error("<getLivingClick->Exception>" + e.toString());
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
	 * 记录用户进房记录，防止同一用户多次进入同一房间 造成的多次提示(1分钟内 不再提醒)
	 *
	 * @param srcUid
	 * @param dstUid
	 */
	public void addUserIntoRoom(int srcUid, int dstUid) {
		Jedis redis = RedisBucket.getInstance().initialize(RedisContant.host, RedisContant.port6381);
		try {
			redis.setex(RedisContant.keyUserIntoRoom + srcUid + ":" + dstUid, 60, "1");
		} catch (Exception e) {
			logger.error("<addUserIntoRoom->Exception>" + e.toString());
		} finally {
			redis.close();
		}
	}

	/**
	 * 判断用户是否存在1分钟内进同一房间
	 *
	 * @param srcUid
	 * @param dstUid
	 * @return true 1分钟内多次进入同一房间，false 则不是
	 */
	public boolean getUserIntoRoom(int srcUid, int dstUid) {
		Jedis redis = RedisBucket.getInstance().initialize(RedisContant.host, RedisContant.port6381);
		String str = "";
		try {
			str = redis.get(RedisContant.keyUserIntoRoom + srcUid + ":" + dstUid);
		} catch (Exception e) {
			logger.error("<getUserIntoRoom->Exception>" + e.toString());
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
	 * 设置禁言
	 * 
	 * @param anchorUid
	 * @param uid
	 * @param type
	 *            =on 禁言 ＝ off 解除禁言
	 * @return
	 */
	public int setSilent(int anchorUid, int uid, String type) {
		Jedis redis = RedisBucket.getInstance().initialize(RedisContant.host, RedisContant.port6381);
		Long lg = 0l;
		try {
			if ("on".equals(type)) {
				lg = redis.hset(RedisContant.keyRoomSilent + anchorUid, String.valueOf(uid), "1");
			} else if ("off".equals(type)) {
				lg = redis.hdel(RedisContant.keyRoomSilent + anchorUid, String.valueOf(uid));
			}
		} catch (Exception e) {
			logger.error("<setSilent->Exception>" + e.toString());
		} finally {
			redis.close();
		}
		return lg.intValue();
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
		Jedis redis = RedisBucket.getInstance().initialize(RedisContant.host, RedisContant.port6381);
		try {
			str = redis.hget(RedisContant.keyRoomSilent + anchorUid, String.valueOf(uid));
		} catch (Exception e) {
			logger.error("<getSilent->Exception>" + e.toString());
		} finally {
			redis.close();
		}
		if (StringUtils.isEmpty(str)) {
			return false;
		} else {
			return true;
		}
	}

	public int delSilent(int anchorUid) {
		Long lg = 0L;
		Jedis redis = RedisBucket.getInstance().initialize(RedisContant.host, RedisContant.port6381);
		try {
			lg = redis.del(RedisContant.keyRoomSilent + anchorUid);
		} catch (Exception e) {
			logger.error("<delSilent->Exception>" + e.toString());
		} finally {
			redis.close();
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
		Jedis redis = RedisBucket.getInstance().initialize(RedisContant.host, RedisContant.port6381);
		Long lg = 0l;
		try {
			lg = redis.hset(RedisContant.PayNotice, key, value);
		} catch (Exception e) {
			logger.error("<setPayNotice->Exception>" + e.toString());
		} finally {
			redis.close();
		}
		return lg.intValue();
	}

	/**
	 * 获取app的pay公告
	 * 
	 * @param key
	 * @return
	 */
	public String getPayNotice(String key) {
		Jedis redis = RedisBucket.getInstance().initialize(RedisContant.host, RedisContant.port6381);
		String str = "";
		try {
			str = redis.hget(RedisContant.PayNotice, key);
		} catch (Exception e) {
			logger.error("<getPayNotice->Exception>" + e.toString());
		} finally {
			redis.close();
		}
		return str;
	}

	public Map<String, String> getPayNoticeList() {
		Jedis redis = RedisBucket.getInstance().initialize(RedisContant.host, RedisContant.port6381);
		Map<String, String> map = null;
		try {
			map = redis.hgetAll(RedisContant.PayNotice);
		} catch (Exception e) {
			logger.error("<getPayNoticeList->Exception>" + e.toString());
		} finally {
			redis.close();
		}
		return map;
	}
	
	 /**
     * 内购 审核期间有用
     * @param ver 版本号
     * @return
     */
    public Boolean setApplyIAP(String ver,int timeAt){
    	String str = "";
		Jedis redis = RedisBucket.getInstance().initialize(RedisContant.host, RedisContant.port6381);
		try {
			str = redis.set(RedisContant.keyApplyIAP+ver, "1");
			redis.expireAt(RedisContant.keyApplyIAP+ver, timeAt);
		} catch (Exception e) {
			logger.error("<setApplyIAP->Exception>" + e.toString());
		} finally {
			redis.close();
		}
		if (StringUtils.isNotEmpty(str)) {
			return true;
		}else{
			return false;
		}
    }
    /**
     * 礼物改变 就调用该方法
     */
    public void setGiftVer(){
		Jedis redis = RedisBucket.getInstance().initialize(RedisContant.host, RedisContant.port6381);
		try {
			redis.incr(RedisContant.giftver);
		} catch (Exception e) {
			logger.error("<setGiftVer->Exception>" + e.toString());
		} finally {
			redis.close();
		}
    }
    
    /**
     * 设置首页的轮播图
     * @param bannerid
     * @param josnBannerModel
     * @return null 失败 其他则成功
     */
    public Long setHomeBanner(int bannerid,String josnBannerModel){

		Jedis redis = RedisBucket.getInstance().initialize(RedisContant.host, RedisContant.port6381);
		Long lg = 0L;
		try {
			lg = redis.hset(RedisContant.homeBanner, String.valueOf(bannerid), josnBannerModel);
		} catch (Exception e) {
			logger.error("<setHomeBanner->Exception>" + e.toString());
		} finally {
			redis.close();
		}
		return lg;
    }
    
    /**
     * 删除轮播图
     * @param bannerid
     * @return
     */
    public Long delHomeBanner(int bannerid){

		Jedis redis = RedisBucket.getInstance().initialize(RedisContant.host, RedisContant.port6381);
		Long lg = 0L;
		try {
			lg = redis.hdel(RedisContant.homeBanner, String.valueOf(bannerid));
		} catch (Exception e) {
			logger.error("<setHomeBanner->Exception>" + e.toString());
		} finally {
			redis.close();
		}
		return lg;
    }
    
    /**
     * 获取轮播图列表
     * @return
     */
    public Map<String, String> getHomeBannerList(){

		Jedis redis = RedisBucket.getInstance().initialize(RedisContant.host, RedisContant.port6381);
		Map<String, String> hgetAll = null;
		try {
			hgetAll = redis.hgetAll(RedisContant.homeBanner);
		} catch (Exception e) {
			logger.error("<setHomeBanner->Exception>" + e.toString());
		} finally {
			redis.close();
		}
		return hgetAll;
    }
    
    public Long updateXiaozhuAuth(int uid, int status){

		Jedis redis = RedisBucket.getInstance().initialize(RedisContant.host, RedisContant.port6379);
		Long lg = 0L;
		try {
			String auths = redis.get(RedisContant.AllAuth+uid);
			JSONObject jsonObject = JSONObject.parseObject(auths);
			jsonObject.put("xiaozhuAuth", status);
			redis.set(RedisContant.AllAuth+uid, jsonObject.toJSONString());
			lg = 1L;
		} catch (Exception e) {
			logger.error("<setHomeBanner->Exception>" + e.toString());
		} finally {
			redis.close();
		}
		return lg;
    }
    
    public Long updateRealnameAuth(String uid, int status){

		Jedis redis = RedisBucket.getInstance().initialize(RedisContant.host, RedisContant.port6379);
		Long lg = 0L;
		try {
			String auths = redis.get(RedisContant.AllAuth+uid);
			JSONObject jsonObject = JSONObject.parseObject(auths);
			jsonObject.put("realnameAuth", status);
			redis.set(RedisContant.AllAuth+uid, jsonObject.toJSONString());
			lg = 1L;
		} catch (Exception e) {
			logger.error("<setHomeBanner->Exception>" + e.toString());
		} finally {
			redis.close();
		}
		return lg;
    }
}
