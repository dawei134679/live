package com.mpig.api.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;
import redis.clients.jedis.Tuple;

import com.mpig.api.model.BillPrizeListModel;
import com.mpig.api.redis.core.RedisBucket;
import com.mpig.api.service.BillPrizeListService;
import com.mpig.api.service.IDemoService;
import com.mpig.api.utils.DateUtils;
import com.mpig.api.utils.RedisContant;

@Controller
@Scope("prototype")
@RequestMapping("/user")
public class DemoController {
	private static final Logger logger = Logger.getLogger(DemoController.class);

	@Resource
	private IDemoService demoService;

	@Resource
	private BillPrizeListService prizeListService;

	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public void demo(HttpServletRequest req, HttpServletResponse resp) {

		logger.info("demo executed");
		int d = demoService.demo(); 
		logger.info("demo service return:" + d);
		writeJson(resp, "03-21 18:35 v.10.1.46 onport:" + req.getServerPort() + " ");
	}

	@RequestMapping(value = "/updateAvatar", method = RequestMethod.GET)
	public String demo2(HttpServletRequest req) {
		logger.info("demo executed");
		int d = demoService.demo();
		logger.info("demo service return:" + d);
		return "index";
	}

	// @RequestMapping("/getPrizeList")
	@ResponseBody
	public Map<String, Object> getPrizeList(Integer uid, HttpServletRequest req, HttpServletResponse resp) {
		Map<String, Object> model = new HashMap<String, Object>();
		List<BillPrizeListModel> billPrizeListModels = prizeListService.getListByUser(uid);
		model.put("data", billPrizeListModels);
		return model;
	}

	protected void writeJson(HttpServletResponse resp, String str) {
		try {
			logger.info("rsp json:" + str);
			resp.setHeader("Content-type", "text/plain;charset=UTF-8");
			resp.getOutputStream().write(str.getBytes("UTF-8"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// @RequestMapping("/updatetop")
	@ResponseBody
	public void updateTop() {
		getUserDayRank();
		getUserMonthRank();
		getUserWeekRank();
		getUserAllRank();
		updateAnchorShengyuanForUser();
	}

	/**
	 * 获取日榜 数据
	 * 
	 * @param typeUser
	 *            =anchor 主播 =user 用户
	 * @return
	 */
	public void getUserDayRank() {
		Set<Tuple> dayRank = null;

		ShardedJedisPool shardPool = null;
		ShardedJedis redis = null;
		try {
			shardPool = RedisBucket.getInstance().getShardPool("user");
			redis = shardPool.getResource();
			String day = DateUtils.dateToString(null, "yyyyMMdd");

			dayRank = redis.zrevrangeWithScores(RedisContant.anchorDay + day, 0, -1);

			if (dayRank != null) {
				int i = 0;
				for (Tuple tuple : dayRank) {
					redis.zadd(RedisContant.anchorDay + day, tuple.getScore() * 10, tuple.getElement());
				}
				logger.info("getUserDayRank Size : " + i);
			}
		} catch (Exception e) {
			logger.error("<getUserDayRank->Exception>" + e.toString());
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
	}

	/**
	 * 获取周榜 数据
	 * 
	 * @param typeUser
	 *            =anchor 主播 =user 用户
	 * @return
	 */
	public void getUserWeekRank() {
		ShardedJedis redis = RedisBucket.getInstance().getShardPool("user").getResource();
		Set<Tuple> dayRank = null;
		try {
			String week = DateUtils.getWeekStart(0);

			dayRank = redis.zrevrangeWithScores(RedisContant.anchorWeek + week, 0, -1);
			if (dayRank != null) {
				int i = 0;
				for (Tuple tuple : dayRank) {
					redis.zadd(RedisContant.anchorWeek + week, tuple.getScore() * 10, tuple.getElement());
					i++;
				}
				logger.info("getUserWeekRank Size : " + i);
			}
		} catch (Exception e) {
			logger.error("<getUserWeekRank->Exception>" + e.toString());
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
	}

	/**
	 * 获取月榜 数据
	 * 
	 * @param typeUser
	 *            =anchor 主播 =user 用户
	 * @return
	 */
	public void getUserMonthRank() {
		ShardedJedis redis = RedisBucket.getInstance().getShardPool("user").getResource();
		Set<Tuple> dayRank = null;
		try {
			String month = DateUtils.getTimesMonthmorning("yyyyMM");
			dayRank = redis.zrevrangeWithScores(RedisContant.anchorMonth + month, 0, -1);
			if (dayRank != null) {
				int i = 0;
				for (Tuple tuple : dayRank) {
					redis.zadd(RedisContant.anchorMonth + month, tuple.getScore() * 10, tuple.getElement());
					i++;
				}
				logger.info("getUserMonthRank Size : " + i);
			}
		} catch (Exception e) {
			logger.error("<getUserMonthRank->Exception>" + e.toString());
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
	}

	/**
	 * 获取总榜 数据
	 * 
	 * @param typeUser
	 *            =anchor 主播 =user 用户
	 * @return
	 */
	public void getUserAllRank() {
		ShardedJedis redis = RedisBucket.getInstance().getShardPool("user").getResource();
		Set<Tuple> dayRank = null;
		try {
			dayRank = redis.zrevrangeWithScores(RedisContant.anchorAll, 0, -1);
			if (dayRank != null) {
				int i = 0;
				for (Tuple tuple : dayRank) {
					redis.zadd(RedisContant.anchorAll, tuple.getScore() * 10, tuple.getElement());
					i++;
				}
				logger.info("getUserAllRank Size : " + i);
			}
		} catch (Exception e) {
			logger.error("<getUserAllRank->Exception>" + e.toString());
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
	}

	public void updateAnchorShengyuanForUser() {
		Jedis redis = RedisBucket.getInstance().getNoShardPool("relationoff").getResource();
		Set<Tuple> dayRank = null;
		Set<String> keysSet = null;
		try {
			keysSet = redis.keys("rm:gets*");
			if (keysSet != null) {
				int keysize = 0;
				for (String key : keysSet) {
					keysize++;
					dayRank = redis.zrevrangeWithScores(key, 0, -1);
					if (dayRank != null) {
						int i = 0;
						for (Tuple tuple : dayRank) {
							redis.zadd(key, tuple.getScore() * 10, tuple.getElement());
							i++;
						}
						logger.info("update user shengyuan for anchor : " + i);
					}

				}
				logger.info("update keys size : " + keysize);
			}

		} catch (Exception e) {
			logger.error("<updateAnchorShengyuanForUser->Exception>" + e.toString());
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
	}
}
