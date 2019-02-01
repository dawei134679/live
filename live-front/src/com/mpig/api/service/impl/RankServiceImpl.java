package com.mpig.api.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.mpig.api.model.UserBaseInfoModel;
import com.mpig.api.redis.service.OtherRedisService;
import com.mpig.api.redis.service.RedisCommService;
import com.mpig.api.service.IRankService;
import com.mpig.api.service.IUserService;
import com.mpig.api.utils.DateUtils;
import com.mpig.api.utils.RedisContant;

import redis.clients.jedis.Tuple;

@Service
public class RankServiceImpl implements IRankService {

	private static final Logger logger = Logger.getLogger(RankServiceImpl.class);
	
	@Resource
	private IUserService userService;

	@Override
	public List<Map<String, Object>> getOlypicRank(String type) {

		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

		Set<Tuple> zrevrangeWithScores = OtherRedisService.getInstance().zrevrangeWithScores(
				RedisContant.RedisNameOther,
				"user".equalsIgnoreCase(type) ? RedisContant.olympicHeatUser : RedisContant.olympicHeatAnchor, 1, 10);
		if (zrevrangeWithScores != null) {

			for (Tuple tuple : zrevrangeWithScores) {
				Map<String, Object> map = new HashMap<String, Object>();
				UserBaseInfoModel userBaseInfoModel = userService
						.getUserbaseInfoByUid(Integer.valueOf(tuple.getElement()), false);
				if (userBaseInfoModel != null) {
					map.put("headimage", userBaseInfoModel.getHeadimage());
					map.put("nickname", userBaseInfoModel.getNickname());
					map.put("level", "user".equalsIgnoreCase(type) ? userBaseInfoModel.getUserLevel()
							: userBaseInfoModel.getAnchorLevel());
					map.put("uid", userBaseInfoModel.getUid());
					map.put("score", tuple.getScore());
					map.put("sex", userBaseInfoModel.getSex());
					list.add(map);

				}
			}
		} else {
			logger.info("getOlypicRank: type:" + type + " 还没有人获得热度值");
		}
		return list;
	}

	@Override
	public Map<String, Object> getOlypicStar(int dstuid) {

		String gid1 = "105";// 奥运加油
		String gid2 = "106";// 奥运金牌
		String times = DateUtils.dateToString(null, "yyyyMMdd");
		
		// 主播的热度值
		Double zscore = OtherRedisService.getInstance().zscore(RedisContant.RedisNameOther,
				RedisContant.olympicHeatAnchor, String.valueOf(dstuid));
		if (zscore == null) {
			zscore = (double) 0;
		}
		// 主播热度值排名
		Long zrevrank = OtherRedisService.getInstance().zrevrank(RedisContant.RedisNameOther,
				RedisContant.olympicHeatAnchor, String.valueOf(dstuid));
		if (zrevrank == null || zrevrank > 89) {
			zrevrank = (long) 99;
		} else {
			zrevrank = zrevrank + 1;
		}

		// 获取为奥运加油 礼物的数量
		int ijiayou = 0;
		String gift23 = OtherRedisService.getInstance().hget(RedisContant.RedisNameOther,
				RedisContant.olympicGift + gid1 + ":" + times, String.valueOf(dstuid));
		if (gift23 != null) {
			ijiayou = Integer.valueOf(gift23);
		}
		// 获取为奥运金牌 礼物数量
		int goldMedal = 0;
		String gift24 = OtherRedisService.getInstance().hget(RedisContant.RedisNameOther,
				RedisContant.olympicGift + gid2 + ":" + times, String.valueOf(dstuid));
		if (gift24 != null) {
			goldMedal = Integer.valueOf(gift24);
		}

		int retJiayou = 0;
		int base = 0;
		int retGoldMedal = 0;

		int grade = 1;
		if (ijiayou / 66 == 0 || goldMedal / 66 == 0) {

			grade = 1;
			base = 66;
			retJiayou = ijiayou / 66 > 0 ? 66 : ijiayou;
			retGoldMedal = goldMedal / 66 > 0 ? 66 : goldMedal;

		} else if (ijiayou / 99 == 0 || goldMedal / 99 == 0) {

			grade = 2;
			base = 99;
			retJiayou = ijiayou / 99 > 0 ? 99 : ijiayou;
			retGoldMedal = goldMedal / 99 > 0 ? 99 : goldMedal;

		} else if (ijiayou / 188 == 0 || goldMedal / 188 == 0) {

			grade = 3;
			base = 188;
			retJiayou = ijiayou / 188 > 0 ? 188 : ijiayou;
			retGoldMedal = goldMedal / 188 > 0 ? 188 : goldMedal;

		} else if (ijiayou / 520 == 0 || goldMedal / 520 == 0) {

			grade = 4;
			base = 520;
			retJiayou = ijiayou / 520 > 0 ? 520 : ijiayou;
			retGoldMedal = goldMedal / 520 > 0 ? 520 : goldMedal;

		} else if (ijiayou / 1314 == 0 || goldMedal / 1314 == 0) {

			grade = 5;
			base = 1314;
			retJiayou = ijiayou / 1314 > 0 ? 1314 : ijiayou;
			retGoldMedal = goldMedal / 1314 > 0 ? 1314 : goldMedal;

		} else if (ijiayou / 1314 > 0 && goldMedal / 1314 > 0) {
			grade = 6;
		} 
		
		Map<String, Object> map = new HashMap<String,Object>();
		
		map.put("grade", grade);
		
		Map<String, Object> task = new HashMap<String,Object>();
		task.put("jindou", retJiayou);
		task.put("longka", retGoldMedal);
		task.put("base", base);
		
		
		Map<String, Object> rank = new HashMap<String,Object>();
		rank.put("rank", zrevrank > 90 ? "99+" : zrevrank.toString());
		rank.put("scores", zscore.intValue());
		
		map.put("task", task);
		map.put("ranklist", rank);
		
		return map;
	}

	@Override
	public Map<String, Object> getMidAutumn(String uid) {

		Map<String, Object> map = new HashMap<String,Object>();
		// 收到月饼数量排名
		int receivedRank = RedisCommService.getInstance().zrevrank(RedisContant.RedisNameUser, RedisContant.midAutumn, uid);
		int autumnScore = RedisCommService.getInstance().zscore(RedisContant.RedisNameUser, RedisContant.midAutumn, uid);
		
		Set<Tuple> multi = RedisCommService.getInstance().zrevrangeWithScores(RedisContant.RedisNameUser, RedisContant.midMultiple,0,0);
		if (multi == null) {
			map.put("nickName", "虚位以待");
			map.put("sendNum", 0);
		}else {
			for(Tuple tuple:multi){
				UserBaseInfoModel userbase = userService.getUserbaseInfoByUid(Integer.valueOf(tuple.getElement()),false);

				map.put("nickName", userbase.getNickname());
				map.put("sendNum", tuple.getScore());
			}
		}
		
		map.put("rankNum", receivedRank>99?"99+":receivedRank);
		map.put("giftNum", autumnScore);
		return map;
	}

	@Override
	public List<Map<String, Object>> getMidReceivedRank() {
		
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		
		Set<Tuple> zrevrangeWithScores = RedisCommService.getInstance().zrevrangeWithScores(RedisContant.RedisNameUser, RedisContant.midAutumn,0,9);
		
		if (zrevrangeWithScores != null) {
			for(Tuple tuple:zrevrangeWithScores){

				Map<String, Object> map = new HashMap<String,Object>();
				UserBaseInfoModel userbase = userService.getUserbaseInfoByUid(Integer.valueOf(tuple.getElement()), false);
				map.put("headimage", userbase.getHeadimage());
				map.put("nickname", userbase.getNickname());
				map.put("level", userbase.getAnchorLevel());
				map.put("uid", userbase.getUid());
				map.put("score", tuple.getScore());
				list.add(map);
				
			}
		}
		return list;
	}

	@Override
	public List<Map<String, Object>> getMidMutilRank() {
		
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		
		Set<Tuple> zrevrangeWithScores = RedisCommService.getInstance().zrevrangeWithScores(RedisContant.RedisNameUser, RedisContant.midMultiple,0,9);
		
		if (zrevrangeWithScores != null) {
			for(Tuple tuple:zrevrangeWithScores){

				Map<String, Object> map = new HashMap<String,Object>();
				UserBaseInfoModel userbase = userService.getUserbaseInfoByUid(Integer.valueOf(tuple.getElement()), false);
				map.put("headimage", userbase.getHeadimage());
				map.put("nickname", userbase.getNickname());
				map.put("level", userbase.getAnchorLevel());
				map.put("uid", userbase.getUid());
				map.put("score", tuple.getScore());
				list.add(map);
			}
		}
		return list;
	}

	@Override
	public Map<String, Object> getNationalDay(String uid) {

		Map<String, Object> map = new HashMap<String,Object>();
		// 收到月饼数量排名
		int receivedRank = RedisCommService.getInstance().zrevrank(RedisContant.RedisNameUser, RedisContant.nationalCharmRank, uid);
		int autumnScore = RedisCommService.getInstance().zscore(RedisContant.RedisNameUser, RedisContant.nationalCharmRank, uid);
		
		Set<Tuple> multi = RedisCommService.getInstance().zrevrangeWithScores(RedisContant.RedisNameUser, RedisContant.nationalGloryRank,0,0);
		if (multi == null) {
			map.put("nickName", "虚位以待");
			map.put("sendNum", 0);
		}else {
			for(Tuple tuple:multi){
				UserBaseInfoModel userbase = userService.getUserbaseInfoByUid(Integer.valueOf(tuple.getElement()),false);

				map.put("nickName", userbase.getNickname());
				map.put("sendNum", tuple.getScore());
			}
		}
		
		map.put("rankNum", receivedRank>99?"99+":receivedRank);
		map.put("giftNum", autumnScore);
		return map;
	}

	@Override
	public List<Map<String, Object>> getNationalDayRank(String type) {

		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		
		Set<Tuple> zrevrangeWithScores = RedisCommService.getInstance().zrevrangeWithScores(RedisContant.RedisNameUser, "1".equals(type)?RedisContant.nationalGloryRank:RedisContant.nationalCharmRank,0,9);
		
		if (zrevrangeWithScores != null) {
			for(Tuple tuple:zrevrangeWithScores){

				Map<String, Object> map = new HashMap<String,Object>();
				UserBaseInfoModel userbase = userService.getUserbaseInfoByUid(Integer.valueOf(tuple.getElement()), false);
				map.put("headimage", userbase.getHeadimage());
				map.put("nickname", userbase.getNickname());
				map.put("level", "1".equals(type)?userbase.getUserLevel():userbase.getAnchorLevel());
				map.put("uid", userbase.getUid());
				map.put("score", tuple.getScore());
				list.add(map);
			}
		}
		return list;
	}

	@Override
	public Map<String, Object> getHalloween(String uid) {

		Map<String, Object> map = new HashMap<String,Object>();
		// 收到月饼数量排名
		int receivedRank = RedisCommService.getInstance().zrevrank(RedisContant.RedisNameUser, RedisContant.halloweenAnchor, uid);
		int autumnScore = RedisCommService.getInstance().zscore(RedisContant.RedisNameUser, RedisContant.halloweenAnchor, uid);
		
		Set<Tuple> multi = RedisCommService.getInstance().zrevrangeWithScores(RedisContant.RedisNameUser, RedisContant.halloweenUser,0,0);
		if (multi == null) {
			map.put("nickName", "虚位以待");
			map.put("sendNum", 0);
		}else {
			for(Tuple tuple:multi){
				UserBaseInfoModel userbase = userService.getUserbaseInfoByUid(Integer.valueOf(tuple.getElement()),false);

				map.put("nickName", userbase.getNickname());
				map.put("sendNum", tuple.getScore());
			}
		}
		
		map.put("rankNum", receivedRank>99?"99+":receivedRank);
		map.put("giftNum", autumnScore);
		return map;
	}

	@Override
	public List<Map<String, Object>> getHalloweenRank(String type) {

		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		
		Set<Tuple> zrevrangeWithScores = RedisCommService.getInstance().zrevrangeWithScores(RedisContant.RedisNameUser, "1".equals(type)?RedisContant.halloweenUser:RedisContant.halloweenAnchor,0,9);
		
		if (zrevrangeWithScores != null) {
			for(Tuple tuple:zrevrangeWithScores){

				Map<String, Object> map = new HashMap<String,Object>();
				UserBaseInfoModel userbase = userService.getUserbaseInfoByUid(Integer.valueOf(tuple.getElement()), false);
				map.put("headimage", userbase.getHeadimage());
				map.put("nickname", userbase.getNickname());
				map.put("level", "1".equals(type)?userbase.getUserLevel():userbase.getAnchorLevel());
				map.put("uid", userbase.getUid());
				map.put("score", tuple.getScore());
				list.add(map);
			}
		}
		return list;
	}

	@Override
	public Map<String, Object> getXiaozhuRun(String uid) {

		Map<String, Object> map = new HashMap<String,Object>();
		// 收到月饼数量排名
		int receivedRank = RedisCommService.getInstance().zrevrank(RedisContant.RedisNameUser, RedisContant.AnchorXiaozhuRun, uid);
		int autumnScore = RedisCommService.getInstance().zscore(RedisContant.RedisNameUser, RedisContant.AnchorXiaozhuRun, uid);
		
		Set<Tuple> multi = RedisCommService.getInstance().zrevrangeWithScores(RedisContant.RedisNameUser, RedisContant.UserXiaozhuRun,0,0);
		if (multi == null) {
			map.put("nickName", "虚位以待");
			map.put("sendNum", 0);
		}else {
			for(Tuple tuple:multi){
				UserBaseInfoModel userbase = userService.getUserbaseInfoByUid(Integer.valueOf(tuple.getElement()),false);

				map.put("nickName", userbase.getNickname());
				map.put("sendNum", tuple.getScore());
			}
		}
		
		map.put("rankNum", receivedRank>99?"99+":receivedRank);
		map.put("giftNum", autumnScore);
		return map;
	}

	@Override
	public List<Map<String, Object>> getXiaozhuRunRank(String type) {

		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		
		Set<Tuple> zrevrangeWithScores = RedisCommService.getInstance().zrevrangeWithScores(RedisContant.RedisNameUser, "1".equals(type)?RedisContant.UserXiaozhuRun:RedisContant.AnchorXiaozhuRun,0,9);
		
		if (zrevrangeWithScores != null) {
			for(Tuple tuple:zrevrangeWithScores){

				Map<String, Object> map = new HashMap<String,Object>();
				UserBaseInfoModel userbase = userService.getUserbaseInfoByUid(Integer.valueOf(tuple.getElement()), false);
				map.put("headimage", userbase.getHeadimage());
				map.put("nickname", userbase.getNickname());
				map.put("level", "1".equals(type)?userbase.getUserLevel():userbase.getAnchorLevel());
				map.put("uid", userbase.getUid());
				map.put("score", tuple.getScore());
				list.add(map);
			}
		}
		return list;
	}
	


	@Override
	public Map<String, Object> getCommen(String uid,String anchorKey,String userKey) {

		Map<String, Object> map = new HashMap<String,Object>();
		// 收到月饼数量排名
		int receivedRank = RedisCommService.getInstance().zrevrank(RedisContant.RedisNameUser, anchorKey, uid);
		int autumnScore = RedisCommService.getInstance().zscore(RedisContant.RedisNameUser, anchorKey, uid);
		
		Set<Tuple> multi = RedisCommService.getInstance().zrevrangeWithScores(RedisContant.RedisNameUser, userKey,0,0);
		if (multi == null) {
			map.put("nickName", "虚位以待");
			map.put("sendNum", 0);
		}else {
			for(Tuple tuple:multi){
				UserBaseInfoModel userbase = userService.getUserbaseInfoByUid(Integer.valueOf(tuple.getElement()),false);

				map.put("nickName", userbase.getNickname());
				map.put("sendNum", tuple.getScore());
			}
		}
		
		map.put("rankNum", receivedRank>99?"99+":receivedRank);
		map.put("giftNum", autumnScore);
		return map;
	}

	@Override
	public List<Map<String, Object>> getCommenRank(String type,String anchorKey,String userKey) {

		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		
		Set<Tuple> zrevrangeWithScores = RedisCommService.getInstance().zrevrangeWithScores(RedisContant.RedisNameUser, "1".equals(type)?userKey:anchorKey,0,9);
		
		if (zrevrangeWithScores != null) {
			for(Tuple tuple:zrevrangeWithScores){

				Map<String, Object> map = new HashMap<String,Object>();
				UserBaseInfoModel userbase = userService.getUserbaseInfoByUid(Integer.valueOf(tuple.getElement()), false);
				map.put("headimage", userbase.getHeadimage());
				map.put("nickname", userbase.getNickname());
				map.put("level", "1".equals(type)?userbase.getUserLevel():userbase.getAnchorLevel());
				map.put("uid", userbase.getUid());
				map.put("score", tuple.getScore());
				list.add(map);
			}
		}
		return list;
	}
}
