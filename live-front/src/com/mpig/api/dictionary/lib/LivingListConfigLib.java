package com.mpig.api.dictionary.lib;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.commons.lang.StringUtils;

import com.mpig.api.model.LiveMicTimeModel;
import com.mpig.api.model.UserBaseInfoModel;
import com.mpig.api.redis.service.OtherRedisService;
import com.mpig.api.redis.service.RedisCommService;
import com.mpig.api.service.IConfigService;
import com.mpig.api.service.ILiveService;
import com.mpig.api.service.IRoomService;
import com.mpig.api.service.IUserService;
import com.mpig.api.service.impl.ConfigServiceImpl;
import com.mpig.api.service.impl.LiveServiceImpl;
import com.mpig.api.service.impl.RoomServiceImpl;
import com.mpig.api.service.impl.UserServiceImpl;
import com.mpig.api.utils.BaseContant;
import com.mpig.api.utils.RedisContant;
import com.mpig.api.utils.VarConfigUtils;

//@Scope("prototype")
public class LivingListConfigLib {

	private static String recommendKey = "recommend";
	private static String hotKey = "hot";
	private static String baseKey = "base";
	private static String newJoinKey = "newJoin";
	private static String mobileKey = "mobile";
	

	private static AtomicLong recommenddate = new AtomicLong(0);
	private static AtomicLong hotdate = new AtomicLong(0);
	private static AtomicLong basedate = new AtomicLong(0);
	private static AtomicLong newJoinDate = new AtomicLong(0);
	private static AtomicLong mobileDate = new AtomicLong(0);
	private static final Map<String, List<Map<String, Object>>> recommendlist = new ConcurrentHashMap<String, List<Map<String, Object>>>();
	private static final Map<String, List<Map<String, Object>>> hotlist = new ConcurrentHashMap<String, List<Map<String, Object>>>();
	private static final Map<String, List<Map<String, Object>>> baselist = new ConcurrentHashMap<String, List<Map<String, Object>>>();
	private static final Map<String, List<Map<String, Object>>> newJoinList = new ConcurrentHashMap<String, List<Map<String, Object>>>();
	private static final Map<String, List<Map<String, Object>>> mobileList = new ConcurrentHashMap<String, List<Map<String, Object>>>();

	private static void updatehotdate() {
		hotdate.set(System.currentTimeMillis());
	}

	private static void updateRecommenddate() {
		recommenddate.set(System.currentTimeMillis());
	}
	
	private static void updatebasedate() {
		basedate.set(System.currentTimeMillis());
	}
	private static void updateNewJoinDate() {
		newJoinDate.set(System.currentTimeMillis());
	}
	
	private static void updatemobileDate() {
		mobileDate.set(System.currentTimeMillis());
	}
	
	public static synchronized List<Map<String, Object>> getMobileLivingList(int page) {
		if ((System.currentTimeMillis() - mobileDate.get()) > 60000) {
			return setMobileLivingList( page);
		} else {
			List<Map<String, Object>> list = mobileList.get(mobileKey);
			List<Map<String, Object>> copy = new ArrayList<Map<String, Object>>(
					list.size());
			for (Map<String, Object> each : list) {
				Map<String, Object> one = new HashMap<String, Object>();
				Set<String> keySet = each.keySet();
				for (String key : keySet) {
					one.put(key, each.get(key));
				}
				copy.add(one);
			}
			return copy;
		}
	}

	public static synchronized List<Map<String, Object>> getLivingList(int os, int recommend, int page) {
		if (recommend == 1) {
			if ((System.currentTimeMillis() - hotdate.get()) > 60000) {
				return setLivingList(os, recommend, page);
			} else {
				List<Map<String, Object>> list = hotlist.get(hotKey);
				List<Map<String, Object>> copy = new ArrayList<Map<String, Object>>(list.size());
				for(Map<String, Object> each:list){
					Map<String, Object> one = new HashMap<String, Object>();
					Set<String> keySet = each.keySet();
					for(String key:keySet){
						one.put(key, each.get(key));
					}
					copy.add(one);
				}
				return copy;
			}
		} else if(recommend == 2) {
			if ((System.currentTimeMillis() - recommenddate.get()) > 60000) {
				return setLivingList(os, recommend, page);
			} else {
				List<Map<String, Object>> list = recommendlist.get(recommendKey);
				List<Map<String, Object>> copy = new ArrayList<Map<String, Object>>(list.size());
				for(Map<String, Object> each:list){
					Map<String, Object> one = new HashMap<String, Object>();
					Set<String> keySet = each.keySet();
					for(String key:keySet){
						one.put(key, each.get(key));
					}
					copy.add(one);
				}
				return copy;
			}
		}else if(recommend == 9){
			if ((System.currentTimeMillis() - newJoinDate.get()) > 60000) {
				return setLivingList(os, recommend, page);
			} else {
				List<Map<String, Object>> list = newJoinList.get(newJoinKey);
				List<Map<String, Object>> copy = new ArrayList<Map<String, Object>>(list.size());
				for(Map<String, Object> each:list){
					Map<String, Object> one = new HashMap<String, Object>();
					Set<String> keySet = each.keySet();
					for(String key:keySet){
						one.put(key, each.get(key));
					}
					copy.add(one);
				}
				return copy;
			}
		} else {
			if ((System.currentTimeMillis() - basedate.get()) > 60000) {
				return setLivingList(os, recommend, page);
			} else {
				List<Map<String, Object>> list = baselist.get(baseKey);
				List<Map<String, Object>> copy = new ArrayList<Map<String, Object>>(list.size());
				for(Map<String, Object> each:list){
					Map<String, Object> one = new HashMap<String, Object>();
					Set<String> keySet = each.keySet();
					for(String key:keySet){
						one.put(key, each.get(key));
					}
					copy.add(one);
				}
				return copy;
			}
		}
	}
	
	public static List<Map<String, Object>> setMobileLivingList(int page) {

		Set<String> setLivingList = OtherRedisService.getInstance().getMobileRoom(page);

		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> listResult = new ArrayList<Map<String, Object>>();
		Map<String, Object> map;

		if (setLivingList != null && setLivingList.size() > 0) {

			ILiveService liveService = new LiveServiceImpl();
			IUserService userService = new UserServiceImpl();
			IConfigService configService = new ConfigServiceImpl();
			IRoomService roomService = new RoomServiceImpl();

			Map<String, UserBaseInfoModel> userbaseMap = userService.getUserbaseInfoByUid(setLivingList.toArray(new String[0]));
			Map<String, LiveMicTimeModel> livingMap = liveService.getLiveIngByUid(setLivingList.toArray(new String[0]));

			if (userbaseMap == null || livingMap == null) {
			} else {
				UserBaseInfoModel userBaseInfoModel;
				LiveMicTimeModel liveMicTimeModel;
				Map<String, String> hgetAll = RedisCommService.getInstance().hgetAll(RedisContant.RedisNameUser,RedisContant.WeekTitle);
				for (String suid : setLivingList) {
					map = new HashMap<String, Object>();
					int uid = Integer.valueOf(suid);
					userBaseInfoModel = userbaseMap.get(suid);
					liveMicTimeModel = livingMap.get(suid);

					if (userBaseInfoModel == null || liveMicTimeModel == null || liveMicTimeModel.getType()|| !userBaseInfoModel.getLiveStatus()) {
						continue;
					} else {
						if (hgetAll != null && hgetAll.containsValue(suid)) {
							map.put("title", "周星达人");
						}else {
							map.put("title", "");
						}
						map.put("status", userBaseInfoModel.getLiveStatus());
						map.put("uid", suid);
						map.put("nickname", userBaseInfoModel.getNickname().trim());
						map.put("headimage", userBaseInfoModel.getHeadimage());
						map.put("anchorLevel", userBaseInfoModel.getAnchorLevel());
						map.put("slogan", liveMicTimeModel.getSlogan().trim());
						map.put("city", StringUtils.isEmpty(liveMicTimeModel.getCity().trim()) ? VarConfigUtils.Location : liveMicTimeModel.getCity());
						map.put("enters", roomService.getRoomShowUsers(uid, userBaseInfoModel.getContrRq()));
						map.put("mobileliveimg", userBaseInfoModel.getLivimage());
						map.put("opentime", System.currentTimeMillis()/1000-userBaseInfoModel.getOpentime());
						map.put("sex", userBaseInfoModel.getSex());
						map.put("pcimg1", userBaseInfoModel.getPcimg1());
						map.put("pcimg2", userBaseInfoModel.getPcimg2());
						
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
						listResult.add(map);
					}
				}
			}
		}
		if (list != null && list.size() > 0) {
			mobileList.put(mobileKey, list);
			updatemobileDate();
		}
		return listResult;
	}

	public static List<Map<String, Object>> setLivingList(int os, int recommend, int page) {

		Set<String> setLivingList = null;
		if (recommend == 0) {
			setLivingList = OtherRedisService.getInstance().getBaseRoom(page);
		} else if (recommend == 1) {
			setLivingList = OtherRedisService.getInstance().getHotRoom(page);
		} else if (recommend == 2) {
			setLivingList = OtherRedisService.getInstance().getRecommendRoom(page);
		}else if(recommend ==9){ //最新入驻
			setLivingList = OtherRedisService.getInstance().getNewJoinRoom(page);
		}

		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> listResult = new ArrayList<Map<String, Object>>();
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
				Map<String, String> hgetAll = RedisCommService.getInstance().hgetAll(RedisContant.RedisNameUser,RedisContant.WeekTitle);
				for (String suid : setLivingList) {

					map = new HashMap<String, Object>();
					int uid = Integer.valueOf(suid);
					userBaseInfoModel = userbaseMap.get(suid);
					liveMicTimeModel = livingMap.get(suid);

					if (userBaseInfoModel == null || liveMicTimeModel == null || liveMicTimeModel.getType()
							|| !userBaseInfoModel.getLiveStatus()) {
						continue;
					} else {
						if (hgetAll != null && hgetAll.containsValue(suid)) {
							map.put("title", "周星达人");
						}else {
							map.put("title", "");
						}
						map.put("status", userBaseInfoModel.getLiveStatus());
						map.put("uid", suid);
						map.put("nickname", userBaseInfoModel.getNickname().trim());
						map.put("headimage", userBaseInfoModel.getHeadimage());
						map.put("anchorLevel", userBaseInfoModel.getAnchorLevel());
						map.put("slogan", liveMicTimeModel.getSlogan().trim());
						map.put("city", StringUtils.isEmpty(liveMicTimeModel.getCity().trim()) ? VarConfigUtils.Location
								: liveMicTimeModel.getCity());
						map.put("enters", roomService.getRoomShowUsers(uid, userBaseInfoModel.getContrRq()));
						map.put("mobileliveimg", userBaseInfoModel.getLivimage());
						map.put("opentime", System.currentTimeMillis()/1000-userBaseInfoModel.getOpentime());
						map.put("sex", userBaseInfoModel.getSex());
						map.put("pcimg1", userBaseInfoModel.getPcimg1());
						map.put("pcimg2", userBaseInfoModel.getPcimg2());
						
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
						listResult.add(map);
					}
				}
			}
		}
		
		if (list != null && list.size() > 0) {
			if (recommend == 2 || recommend == 3) {
				recommendlist.put(recommendKey, list);
				updateRecommenddate();
			} else if (recommend == 1) {
				hotlist.put(hotKey, list);
				updatehotdate();
			} else if( recommend == 0){
				baselist.put(baseKey, list);
				updatebasedate();
			}
		}
		 if( recommend ==9){
				if(list.size()<BaseContant.newJoinSize){
					List<String> setLivingList1 = new ArrayList<String>();
					for(String ac : setLivingList){
						setLivingList1.add(ac);
					}
					List<Map<String, Object>> newList = new ArrayList<Map<String, Object>>();
					Set<String> allNewJoinRoom = OtherRedisService.getInstance().getAllNewJoinRoom(0);
					List<String> allNewJoinRoomList = new ArrayList<String>();
					for(String ac : allNewJoinRoom){
						allNewJoinRoomList.add(ac);
					}
					allNewJoinRoomList.removeAll(setLivingList1);
					newList = getUserData(allNewJoinRoomList);
					list.addAll(newList);
					listResult.addAll(newList);
				}
				newJoinList.put(newJoinKey, list);
				updateNewJoinDate();
			}
		return listResult;
	}
	
	public static List<Map<String, Object>> getUserData(List<String> setLivingList){
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Map<String, Object> map;

		if (setLivingList != null && setLivingList.size() > 0) {

			ILiveService liveService = new LiveServiceImpl();
			IUserService userService = new UserServiceImpl();
			IConfigService configService = new ConfigServiceImpl();
			IRoomService roomService = new RoomServiceImpl();

			Map<String, UserBaseInfoModel> userbaseMap = userService
					.getUserbaseInfoByUid(setLivingList.toArray(new String[0]));

			if (userbaseMap != null){

				UserBaseInfoModel userBaseInfoModel;

				for (String suid : setLivingList) {

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
						map.put("enters", roomService.getRoomShowUsers(uid, userBaseInfoModel.getContrRq()));
						map.put("mobileliveimg", userBaseInfoModel.getLivimage());
						map.put("opentime", userBaseInfoModel.getOpentime());
						map.put("sex", userBaseInfoModel.getSex());
						map.put("pcimg1", userBaseInfoModel.getPcimg1());
						map.put("pcimg2", userBaseInfoModel.getPcimg2());

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
}
