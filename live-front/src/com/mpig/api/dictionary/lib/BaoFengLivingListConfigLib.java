package com.mpig.api.dictionary.lib;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.commons.lang.StringUtils;

import redis.clients.jedis.Tuple;

import com.mpig.api.model.LiveMicTimeModel;
import com.mpig.api.model.UserBaseInfoModel;
import com.mpig.api.redis.service.OtherRedisService;
import com.mpig.api.redis.service.RedisCommService;
import com.mpig.api.redis.service.UserRedisService;
import com.mpig.api.service.IConfigService;
import com.mpig.api.service.ILiveService;
import com.mpig.api.service.IRoomService;
import com.mpig.api.service.IUserService;
import com.mpig.api.service.impl.ConfigServiceImpl;
import com.mpig.api.service.impl.LiveServiceImpl;
import com.mpig.api.service.impl.RoomServiceImpl;
import com.mpig.api.service.impl.UserServiceImpl;
import com.mpig.api.utils.BaseContant;
import com.mpig.api.utils.DateUtils;
import com.mpig.api.utils.RedisContant;
import com.mpig.api.utils.VarConfigUtils;

/**
 * 暴风相关列表缓存
 * @author zyl
 * @date 2016-12-29 下午7:40:35
 */
//@Scope("prototype")
public class BaoFengLivingListConfigLib {

	private static AtomicLong bf_recommenddate = new AtomicLong(0);
	private static AtomicLong bf_SDKRecommenddate = new AtomicLong(0);
	private static AtomicLong bf_SDKSquaredate = new AtomicLong(0);
	private static final List<Map<String, Object>> bf_recommendlist  = new ArrayList<Map<String, Object>>();
	private static final Map<String,List<Map<String, Object>>> bf_SDKrecommendlist  = new HashMap<String,List<Map<String, Object>>>();
	private static final List<Map<String, Object>> bf_SDKSquarelist  = new ArrayList<Map<String, Object>>();

	private static void updateRecommenddate() {
		bf_recommenddate.set(System.currentTimeMillis());
	}
	private static void updateSDKRecommenddate() {
		bf_SDKRecommenddate.set(System.currentTimeMillis());
	}
	private static void updateSDKSquaredate() {
		bf_SDKSquaredate.set(System.currentTimeMillis());
	}
	@SuppressWarnings("unchecked")
	public static synchronized List<Map<String, Object>> getLivingList(int page) {
			if ((System.currentTimeMillis() - bf_recommenddate.get()) > 60000) {
				return setLivingList(page);
			} else {
				return bf_recommendlist;
			}
	}
	
	@SuppressWarnings("unchecked")
	public static synchronized Map<String,List<Map<String, Object>>> getSDKRecommendList() {
			if ((System.currentTimeMillis() - bf_SDKRecommenddate.get()) > 60000) {
				return setSDKRecommendList();
			} else {
				return bf_SDKrecommendlist;
			}
	}
	@SuppressWarnings("unchecked")
	public static synchronized List<Map<String, Object>> getSDKHomeSquareList(int page) {
		if ((System.currentTimeMillis() - bf_SDKSquaredate.get()) > 60000) {
			return setSDKHomeSquareList(page);
		} else {
			return bf_SDKSquarelist;
		}
	}
	
	private static List<Map<String, Object>> setSDKHomeSquareList(int page){
		Set<String> setLivingList = OtherRedisService.getInstance().getHotRoom(page);
		List<Map<String, Object>> sdkHomeSquareData = getSDKHomeSquareData(setLivingList);
		if(sdkHomeSquareData.size()>0){
			bf_SDKSquarelist.clear();
			bf_SDKSquarelist.addAll(sdkHomeSquareData);
			updateSDKSquaredate();
		}
		return bf_SDKSquarelist;
	}

	private static Map<String, List<Map<String, Object>>> setSDKRecommendList() {
		// 获取热门推荐的主播
		Set<String> setLivingList = OtherRedisService.getInstance().getRecommendRoom(0);
		List<String> livingList = new ArrayList<String>();
		List<String> noLivingList = new ArrayList<String>();
		for (String set : setLivingList) {
			livingList.add(set);
		}
		// 热门推荐不足20时的补足操作
		if (livingList.size() < 10) {
			Date date = new Date();
			date = DateUtils.getNDaysAfterDate(date, -1);
			Set<Tuple> userRank = UserRedisService.getInstance().getUserDayRankForDate(date, 9);
			for (Tuple tuple : userRank) {
				noLivingList.add(tuple.getElement());
			}
			noLivingList.removeAll(livingList);
			livingList.addAll(noLivingList);
		}
		
		//所有的推荐结果
		if(bf_SDKrecommendlist.get("bannerLiveList")==null){
			bf_SDKrecommendlist.put("bannerLiveList",new ArrayList());
		}
		if(bf_SDKrecommendlist.get("bannerLiveList")==null){
			bf_SDKrecommendlist.put("recommLiveList",new ArrayList());
		}
		if(bf_SDKrecommendlist.get("newJoinList")==null){
			bf_SDKrecommendlist.put("newJoinList",new ArrayList());
		}
		
		List<Map<String, Object>> sdkRecommendData = getSDKRecommendData(livingList);
		if(sdkRecommendData.size()>2){
			bf_SDKrecommendlist.put("bannerLiveList", sdkRecommendData.subList(0, 2));
			updateSDKRecommenddate();
		}
		if(sdkRecommendData.size()>6){
			bf_SDKrecommendlist.put("recommLiveList", sdkRecommendData.subList(2, 6));
			updateSDKRecommenddate();
		}else{
			bf_SDKrecommendlist.put("recommLiveList", sdkRecommendData.subList(2, sdkRecommendData.size()));
			updateSDKRecommenddate();
		}
		

		Set<String> newJoinList = OtherRedisService.getInstance().getNewJoinRoom(0);
		List<String> setLivingList1 = new ArrayList<String>();
		for(String ac : newJoinList){
			setLivingList1.add(ac);
		}
		if(setLivingList1.size()<BaseContant.newJoinSize){
			Set<String> allNewJoinRoom = OtherRedisService.getInstance().getAllNewJoinRoom(0);
			List<String> allNewJoinRoomList = new ArrayList<String>();
			for(String ac : allNewJoinRoom){
				allNewJoinRoomList.add(ac);
			}
			allNewJoinRoomList.removeAll(setLivingList1);
			setLivingList1 = allNewJoinRoomList;
		}
		List<Map<String, Object>> newjoinList = getSDKRecommendData(setLivingList1);
		if(sdkRecommendData.size()>4){
			bf_SDKrecommendlist.put("newJoinList", newjoinList.subList(0, 4));
			updateSDKRecommenddate();
		}
		
		return bf_SDKrecommendlist;
	}
	
	private static List<Map<String, Object>> getSDKHomeSquareData(Set<String> setLivingList){
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Map<String, Object> map;

		if (setLivingList != null && setLivingList.size() > 0) {

			ILiveService liveService = new LiveServiceImpl();
			IUserService userService = new UserServiceImpl();
			IConfigService configService = new ConfigServiceImpl();
			IRoomService roomService = new RoomServiceImpl();

			Map<String, UserBaseInfoModel> userbaseMap = userService
					.getUserbaseInfoByUid(setLivingList.toArray(new String[0]));
			Map<String, LiveMicTimeModel> livingMap = liveService.getLiveIngByUid(setLivingList.toArray(new String[0]));

			if (userbaseMap == null || livingMap == null) {

			} else {

				UserBaseInfoModel userBaseInfoModel;
				LiveMicTimeModel liveMicTimeModel;
				for (String suid : setLivingList) {

					map = new HashMap<String, Object>();
					int uid = Integer.valueOf(suid);
					userBaseInfoModel = userbaseMap.get(suid);
					liveMicTimeModel = livingMap.get(suid);

					if (userBaseInfoModel == null || liveMicTimeModel == null || liveMicTimeModel.getType()
							|| !userBaseInfoModel.getLiveStatus()) {
						continue;
					} else {
						map.put("status", userBaseInfoModel.getLiveStatus());
						map.put("uid", suid);
						map.put("nickname", userBaseInfoModel.getNickname().trim());
						map.put("headimage", userBaseInfoModel.getHeadimage());
						map.put("anchorLevel", userBaseInfoModel.getAnchorLevel());
						map.put("slogan", liveMicTimeModel.getSlogan().trim());
						map.put("city", StringUtils.isEmpty(liveMicTimeModel.getCity().trim()) ? VarConfigUtils.Location
								: liveMicTimeModel.getCity());
						map.put("enters", roomService.getRoomShowUsers(uid, userBaseInfoModel.getContrRq()));
						map.put("mobileliveimg", userBaseInfoModel.getPcimg2());
						map.put("opentime", System.currentTimeMillis()/1000-userBaseInfoModel.getOpentime());
						map.put("sex", userBaseInfoModel.getSex());
						
						String stream = configService.getThirdStream(uid);
						int thirdstream = 0;
						if (null == stream) {
							map.put("domain",
									liveService.getVideoConfig(0, uid, userBaseInfoModel.getVideoline()).get("domain"));
						} else {
							thirdstream = 1;
							map.put("domain", stream);
						}
						map.put("thirdstream", thirdstream);
						map.put("verified", userBaseInfoModel.isVerified());
					}
					if (map.size() > 0) {
						list.add(map);
					}
				}
			}
		}
		return list;
	}
	private static List<Map<String, Object>> getSDKRecommendData(List<String> livingList){
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Map<String, Object> map;
		if (livingList.size() > 0) {
			ILiveService liveService = new LiveServiceImpl();
			IUserService userService = new UserServiceImpl();
			IConfigService configService = new ConfigServiceImpl();
			IRoomService roomService = new RoomServiceImpl();

			Map<String, UserBaseInfoModel> userbaseMap = userService.getUserbaseInfoByUid(livingList.toArray(new String[0]));
			Map<String, LiveMicTimeModel> livingMap = liveService.getLiveIngByUid(livingList.toArray(new String[0]));

			if (userbaseMap == null || livingMap == null) {

			} else {

				UserBaseInfoModel userBaseInfoModel;
				for (String suid : livingList) {

					map = new HashMap<String, Object>();
					int uid = Integer.valueOf(suid);
					userBaseInfoModel = userbaseMap.get(suid);

					if (userBaseInfoModel == null) {
						continue;
					} else {
						map.put("status", userBaseInfoModel.getLiveStatus());
						map.put("uid", suid);
						map.put("nickname", userBaseInfoModel.getNickname().trim());
						map.put("headimage", userBaseInfoModel.getHeadimage());
						map.put("anchorLevel", userBaseInfoModel.getAnchorLevel());
						map.put("city", userBaseInfoModel.getCity());
						map.put("enters", roomService.getRoomShowUsers(uid, userBaseInfoModel.getContrRq()));
						map.put("mobileliveimg", userBaseInfoModel.getPcimg2());
						map.put("opentime", System.currentTimeMillis()/1000-userBaseInfoModel.getOpentime());
						map.put("sex", userBaseInfoModel.getSex());
						
						String stream = configService.getThirdStream(uid);
						int thirdstream = 0;
						if (null == stream) {
							map.put("domain", liveService.getVideoConfig(0, uid, userBaseInfoModel.getVideoline()).get("domain"));
						} else {
							thirdstream = 1;
							map.put("domain", stream);
						}
						map.put("thirdstream", thirdstream);
						map.put("verified", userBaseInfoModel.isVerified());
					}
					if (map.size() > 0) {
						list.add(map);
					}
				}
			}
		}
		return list;
	}
	public static List<Map<String, Object>> setLivingList(int page) {
		List<Map<String, Object>>  list = new ArrayList<Map<String, Object>>();
		//获取热门推荐的主播
		Set<String> setLivingList = OtherRedisService.getInstance().getRecommendRoom(page);
		List<String> livingList = new ArrayList<String>();
		List<String> noLivingList = new ArrayList<String>();
		for(String set : setLivingList){
			livingList.add(set);
		}
		//热门推荐不足20时的补足操作
		if(livingList.size()<20){
			Date date = new Date();
			date = DateUtils.getNDaysAfterDate(date, -1);
			String day = DateUtils.dateToString(date, "yyyyMMdd");
			Set<Tuple> userRank = RedisCommService.getInstance().zrevrangeWithScores(RedisContant.RedisNameUser, RedisContant.anchorDay+day, 0, 20);
			for(Tuple tuple : userRank){
				noLivingList.add(tuple.getElement());
			}
			noLivingList.removeAll(livingList);
			livingList.addAll(noLivingList);
		}
		
		Map<String, Object> map;
		if (livingList.size() > 0) {
			IUserService userService = new UserServiceImpl();
			IRoomService roomService = new RoomServiceImpl();
			Map<String, UserBaseInfoModel> userbaseMap = userService.getUserbaseInfoByUid(livingList.toArray(new String[0]));
			if (userbaseMap.size()>0) {
				UserBaseInfoModel userBaseInfoModel;
				for (String suid : livingList) {
					map = new HashMap<String, Object>();
					int uid = Integer.valueOf(suid);
					userBaseInfoModel = userbaseMap.get(suid);
					if (userBaseInfoModel == null) {
						continue;
					} else {
						map.put("status", userBaseInfoModel.getLiveStatus());
						map.put("uid", suid);
						map.put("name", userBaseInfoModel.getNickname().trim());
						int rq = roomService.getRoomShowUsers(uid, userBaseInfoModel.getContrRq());
						map.put("rq", rq);
						String city = userBaseInfoModel.getCity();
						if(StringUtils.isEmpty(city) || city.equals("猪圈")){
							city = "东莞";
						}
						map.put("city", city);
						map.put("livimage", userBaseInfoModel.getLivimage());
						map.put("pcimg1", userBaseInfoModel.getPcimg1());
						map.put("pcimg2", userBaseInfoModel.getPcimg2());
					}
					if (map.size() > 0) {
						list.add(map);
					}
				}
			}
		}
		if (list.size() > 0) {
			bf_recommendlist.clear();
			bf_recommendlist.addAll(list);
			updateRecommenddate();
		}
		return bf_recommendlist;
	}
	
}
