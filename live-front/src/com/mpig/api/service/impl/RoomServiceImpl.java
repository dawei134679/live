package com.mpig.api.service.impl;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.mashape.unirest.http.Unirest;
import com.mpig.api.SqlTemplete;
import com.mpig.api.async.AsyncManager;
import com.mpig.api.async.IAsyncTask;
import com.mpig.api.dictionary.LevelsConfig;
import com.mpig.api.dictionary.lib.ActivitiesModeConfigLib;
import com.mpig.api.dictionary.lib.BaseConfigLib;
import com.mpig.api.dictionary.lib.GameAppConfigLib;
import com.mpig.api.dictionary.lib.GiftPromotionConfigLib;
import com.mpig.api.dictionary.lib.LevelConfigLib;
import com.mpig.api.dictionary.lib.LevelsConfigLib;
import com.mpig.api.dictionary.lib.LivingListConfigLib;
import com.mpig.api.dictionary.lib.TaskConfigLib;
import com.mpig.api.dictionary.lib.UrlConfigLib;
import com.mpig.api.dictionary.lib.ValueaddServiceConfigLib;
import com.mpig.api.model.ConfigGiftModel;
import com.mpig.api.model.IosVersionModel;
import com.mpig.api.model.LiveMicTimeModel;
import com.mpig.api.model.ReturnModel;
import com.mpig.api.model.RobotBaseInfoModel;
import com.mpig.api.model.RoomGameInfoModel;
import com.mpig.api.model.UserAccountModel;
import com.mpig.api.model.UserAssetModel;
import com.mpig.api.model.UserBaseInfoModel;
import com.mpig.api.model.UserCarInfoModel;
import com.mpig.api.model.UserGuardInfoModel;
import com.mpig.api.model.UserItemModel;
import com.mpig.api.model.UserVipInfoModel;
import com.mpig.api.model.ValueaddPrivilegeModel;
import com.mpig.api.modelcomet.DanmakuCMod;
import com.mpig.api.modelcomet.EnterRoomCMod;
import com.mpig.api.modelcomet.GiftSendCMod;
import com.mpig.api.modelcomet.HornCMod;
import com.mpig.api.modelcomet.LevelUpCMod;
import com.mpig.api.modelcomet.MessageCMod;
import com.mpig.api.modelcomet.PKNumOfVotesCMod;
import com.mpig.api.modelcomet.RunWayCMod;
import com.mpig.api.redis.service.OtherRedisService;
import com.mpig.api.redis.service.RedisCommService;
import com.mpig.api.redis.service.RelationRedisService;
import com.mpig.api.redis.service.UserRedisService;
import com.mpig.api.service.IBillService;
import com.mpig.api.service.IConfigService;
import com.mpig.api.service.ILiveService;
import com.mpig.api.service.IPkService;
import com.mpig.api.service.IRoomService;
import com.mpig.api.service.IUserGuardInfoService;
import com.mpig.api.service.IUserItemService;
import com.mpig.api.service.IUserItemService.ItemSource;
import com.mpig.api.service.IUserService;
import com.mpig.api.service.IUserTransactionHisService;
import com.mpig.api.service.IWebService;
import com.mpig.api.task.TaskService;
import com.mpig.api.utils.BaseContant;
import com.mpig.api.utils.ChatMessageUtil;
import com.mpig.api.utils.CodeContant;
import com.mpig.api.utils.Constant;
import com.mpig.api.utils.DateUtils;
import com.mpig.api.utils.EncryptUtils;
import com.mpig.api.utils.GameServerUtil;
import com.mpig.api.utils.MathRandomUtil;
import com.mpig.api.utils.RedisContant;
import com.mpig.api.utils.ValueaddServiceUtil;
import com.mpig.api.utils.VarConfigUtils;

import redis.clients.jedis.Tuple;

@Service
public class RoomServiceImpl implements IRoomService, SqlTemplete {

	private static final Logger logger = Logger.getLogger(RoomServiceImpl.class);

	private final static RoomServiceImpl instance = new RoomServiceImpl();

	public static RoomServiceImpl getInstance() {
		return instance;
	}

	@Resource
	private ILiveService liveService;
	@Resource
	private IUserService userService;
	@Resource
	private IConfigService configService;
	@Resource
	private IBillService billService;
	@Resource
	private IUserItemService userItemService;
	@Resource
	private IUserGuardInfoService userGuardInfoService;
	@Resource
	private IPkService pkService;
	@Resource
	private IUserTransactionHisService userTransactionHisService;
	@Resource
	private IWebService webService;

	@Override
	public Boolean robotEnterRoom(int srcUid, int dstUid, int level, String nick, String avatar, Boolean sex) {

		// 获取主播信息
		UserBaseInfoModel userBaseInfoModel = userService.getUserbaseInfoByUid(dstUid, false);
		if (userBaseInfoModel == null) {
			return false;
		} else if (!userBaseInfoModel.getLiveStatus()) {
			return false;
		}
		//获取用户当前的守护身份
		UserGuardInfoModel userGuardInfoModel = ValueaddServiceUtil.getGuardInfo(srcUid, dstUid);
		//获取用户当前的VIP身份
		UserVipInfoModel userVipInfoModel = ValueaddServiceUtil.getVipInfo(srcUid);

		
		int joinPlace = 1; //加入的位置
		long nowtime = System.currentTimeMillis()/1000;
		if(userGuardInfoModel != null){
			if(nowtime <= userGuardInfoModel.getEndtime()){ //保护期的座驾 和icon
				joinPlace = 2;
			}
		}
		// TOSY 用户进房广播
		if (!OtherRedisService.getInstance().getUserIntoRoom(srcUid, dstUid)) {
			
			rpcAdminUserEnterRoom(level, srcUid, dstUid, nick, avatar, sex, userGuardInfoModel, userVipInfoModel,1);

			Double dlFollows = RelationRedisService.getInstance().isFollows(srcUid, dstUid);
			if (dlFollows != null) {
				// 修改关注列表的score值
				RelationRedisService.getInstance().addFollows(srcUid, dstUid, dlFollows + 1, "on");
			}
			// 1分钟之内
			OtherRedisService.getInstance().addUserIntoRoom(srcUid, dstUid);
		}

		try{
			AsyncManager.getInstance().execute(
					new UpdRankAsyncTask(srcUid, dstUid, userBaseInfoModel.getRecommend(), userBaseInfoModel.getContrRq(), joinPlace,"1"));
		}catch (Exception ex){
			logger.error("robotEnterRoom>>>",ex);
		}

		return true;
	}

	/**
	 * 用户进房间
	 * 
	 * @param srcUid
	 *            用户uid
	 * @param dstUser
	 *            主播uid
	 * @param returnModel
	 *            返回
	 */
	@Override
	public void enterRoom(Integer srcUid, Integer dstUid,int os, ReturnModel returnModel) {
		Map<String, Object> mapList = new HashMap<String, Object>();
		Map<String, Object> map = new HashMap<String, Object>();

		try {
			Long nowtime = System.currentTimeMillis()/1000;
			Set<Integer> gameIds = GameAppConfigLib.allGameAppRoomIds();
			// 获取主播信息
			UserBaseInfoModel userBaseInfoModel = userService.getUserbaseInfoByUid(dstUid, false);
			UserBaseInfoModel srcUserBaseInfoModel = userService.getUserbaseInfoByUid(srcUid, false);
			if (!gameIds.contains(dstUid)) {
				if (userBaseInfoModel == null) {
	
					returnModel.setCode(CodeContant.ARTISTIDEXCEPT);
					returnModel.setMessage("主播异常");
	
					map = new HashMap<String, Object>();
					map.put("timeslong", 0);
					map.put("persontimes", 0);
					map.put("roomlikes", 0);
					map.put("creditTotal", 0);
					map.put("rq", 0);
					returnModel.setData(map);
					return;
				}
				if (!userBaseInfoModel.getLiveStatus() && os !=5 ) {
					returnModel.setCode(CodeContant.ARTISTIDEXCEPT);
					returnModel.setMessage("主播已关闭直播间");
	
					LiveMicTimeModel liveMicTimeModel = liveService.getLiveMicInfoByUid(dstUid, false);
					if (liveMicTimeModel == null) {
						map.put("timeslong", 0);
						map.put("persontimes", 0);
						map.put("roomlikes", 0);
						map.put("creditTotal", 0);
						map.put("rq", UserRedisService.getInstance().getUserAllRankRqScore(String.valueOf(dstUid)));
						returnModel.setData(map);
					} else {
						if (liveMicTimeModel.getEndtime() == 0) {
							map.put("timeslong", 300);
						} else {
							map.put("timeslong", liveMicTimeModel.getEndtime() - liveMicTimeModel.getStarttime());
						}
						map.put("persontimes", liveMicTimeModel.getAudience());
						map.put("roomlikes", liveMicTimeModel.getLikes());
						map.put("creditTotal", liveMicTimeModel.getCredit());
						map.put("rq", UserRedisService.getInstance().getUserAllRankRqScore(String.valueOf(dstUid)));
					}
					returnModel.setData(map);
					return;
				}
	
				map = userService.getLivIngInfo(srcUid, dstUid);
				if (map == null) {
					returnModel.setCode(CodeContant.USERASSETEXITS);
					returnModel.setMessage("用户异常");
					return;
				}
				
				map.put("liveStatus",userBaseInfoModel.getLiveStatus());
				map.put("gamePageUrl", userBaseInfoModel.getGamePageUrl());
				map.put("gameServerUrl", userBaseInfoModel.getGameServerUrl());
				RoomGameInfoModel game = GameServerUtil.getGameInfoById(userBaseInfoModel.getGameId());
				if(game==null) {
					map.put("gameKey", "");
				}else {
					map.put("gameKey", game.getGameKey());
				}
				//直播间声援值总榜（monthSupport）
				map.put("monthSupport", UserRedisService.getInstance().getSupportByUid(dstUid, 4));
				if (UserRedisService.getInstance().getSwitchPlate(dstUid)) {
					map.put("plate", 1);
				} else {
					map.put("plate", 2);
				}
	
				map.put("rq", UserRedisService.getInstance().getUserAllRankRqScore(String.valueOf(dstUid)));
				mapList.put("artistInfo", map);
				// 视频流配置
				map = liveService.getVideoConfig(srcUid, dstUid, userBaseInfoModel.getVideoline());
	
				if (map.size() <= 0) {
					returnModel.setCode(CodeContant.LIVESERVICECONFIG);
					returnModel.setMessage("获取服务器配置出错");
					return;
				}
				// TOSY 第三方流判断
				String stream = configService.getThirdStream(dstUid);
				if (null != stream) {
					map.put("domain", stream);
				}
	
				mapList.put("videoStream", map);
				mapList.put("isJoin", 0);	//是否在连麦状态
				//TOSY ADD NEW STREAM2
				mapList.put("micUserLv", ConfigServiceImpl.getMicUserLv());//连麦最低等级
				try{
					String uid2nd = OtherRedisService.getInstance().getLive2ndUid(dstUid);
					if(null != uid2nd){
						Integer nUid2nd = Integer.valueOf(uid2nd);
						if(null != nUid2nd){
							UserBaseInfoModel userBaseInfoModel2 = userService.getUserbaseInfoByUid(nUid2nd, false);
							if(null != userBaseInfoModel2){
								mapList.put("isJoin", 1);	//是否在连麦状态
								
								map = liveService.getVideoConfig(dstUid, nUid2nd, userBaseInfoModel2.getVideoline());
								map.put("uid2nd",uid2nd);
								map.put("headimage", userBaseInfoModel2.getHeadimage());
								map.put("nickname", userBaseInfoModel2.getNickname());
								
								stream = configService.getThirdStream(nUid2nd);
								if (null != stream) {
									map.put("domain", stream);
								}
								mapList.put("videoStream2nd", map);	
							}
						}
					}
					//RelationRedisService.getInstance().setRealUserInRoom(dstUid, 1.0);
				}catch(Exception e){
					logger.debug("<error-enterRoom OtherRedisService.getInstance().getLive2ndUid Exception is >", e);
				}
			}
			
			map.put("lianmaiStatus", userBaseInfoModel.getLianmaiStatus());
			map.put("lianmaiAnchorId", userBaseInfoModel.getLianmaiAnchorId());
			if(userBaseInfoModel.getLianmaiStatus()==1) {
				UserBaseInfoModel lianmaiUserBaseInfoModel = userService.getUserbaseInfoByUid(userBaseInfoModel.getLianmaiAnchorId(), false);
				String lianmaiAnchorstream = configService.getThirdStream(lianmaiUserBaseInfoModel.getUid());
				if (null == lianmaiAnchorstream) {
					map.put("lianmaiAnchorDoamin",liveService.getVideoConfig(0, lianmaiUserBaseInfoModel.getUid(), lianmaiUserBaseInfoModel.getVideoline()).get("domain"));
				} else {
					map.put("lianmaiAnchorDoamin", lianmaiAnchorstream);
				}
			}else {
				map.put("lianmaiAnchorDoamin", "");
			}
			
			//获取用户当前的守护身份
			UserGuardInfoModel userGuardInfoModel = ValueaddServiceUtil.getGuardInfo(srcUid, dstUid);
			//获取用户当前的VIP身份
			UserVipInfoModel userVipInfoModel = ValueaddServiceUtil.getVipInfo(srcUid);
			int carId = 0;
			if(userVipInfoModel != null && nowtime.intValue() <=userVipInfoModel.getEndtime().intValue()){
				ValueaddPrivilegeModel privilegeModel = ValueaddServiceUtil.getPrivilege(userVipInfoModel.getGid(), 1);
				map = new HashMap<String, Object>();
				map.put("vipId", userVipInfoModel.getGid());
				map.put("vipLevel",1);
				int icon = 0;
				icon = privilegeModel.getIconId();
				carId = privilegeModel.getCarId();
				map.put("vipIcon", icon);
				mapList.put("vipInfo", map);
				mapList.put("joinEffects", privilegeModel.getJoinEffects()); //进场特效 聊天区上方的文字颜色
			}
			if(userGuardInfoModel != null && nowtime.intValue() <=userGuardInfoModel.getCushiontime().intValue()){
				ValueaddPrivilegeModel privilegeModel = ValueaddServiceUtil.getPrivilege(userGuardInfoModel.getGid(), userGuardInfoModel.getLevel());
				map = new HashMap<String, Object>();
				map.put("guardId", userGuardInfoModel.getGid());
				map.put("guardLevel", userGuardInfoModel.getLevel());
				int icon = 0;
				if(nowtime >userGuardInfoModel.getEndtime() && nowtime <=userGuardInfoModel.getCushiontime()){
					icon = privilegeModel.getCushionIcon();
					carId = privilegeModel.getCushionCarid();
				}else{
					icon = privilegeModel.getIconId();
					carId = privilegeModel.getCarId();
				}
				Long days = DateUtils.getDaysBetweenTime(nowtime, new Long(userGuardInfoModel.getEndtime()));
				map.put("surplusDays", days);
				map.put("guardIcon", icon);
				mapList.put("guardInfo", map);
				mapList.put("joinEffects", privilegeModel.getJoinEffects()); //进场特效 聊天区上方的文字颜色
			}
			UserCarInfoModel carInfo = ValueaddServiceUtil.getCarInfo(srcUid);
			if(carInfo != null && nowtime.intValue() <=carInfo.getEndtime().intValue()){
				carId = carInfo.getGid();
			}
			mapList.put("carId", carId);
			int mengzhuSurplusCount = ValueaddServiceUtil.getMengzhuSurplusCount(srcUid);
			mapList.put("mengzhucount", mengzhuSurplusCount);
			
			mapList.put("roomManage",RelationRedisService.getInstance().checkManageByAnchorUser(dstUid, srcUid) ? 1 : 0);
			

			//获取当前用户的 权限的加入位置
			int gid = 0;
			int vaLevel = 0;
			int joinPlace = 1; //加入的位置
			ValueaddPrivilegeModel vipPrivilegeModel = null;
			if(userVipInfoModel != null){
				gid = userVipInfoModel.getGid();
				vaLevel = 1;
				vipPrivilegeModel = ValueaddServiceUtil.getPrivilege(gid, vaLevel);
			}
			ValueaddPrivilegeModel guardPrivilegeModel= null;
			if(userGuardInfoModel != null){
				if(nowtime <= userGuardInfoModel.getEndtime()){ 
					joinPlace = 2;
				}
				gid = userGuardInfoModel.getGid();
				vaLevel = userGuardInfoModel.getLevel();
				guardPrivilegeModel = ValueaddServiceUtil.getPrivilege(gid, vaLevel);
			}
			//计算用户的排名分数
			Integer sort = 1;
			Double dscore = 0.0;
			if (srcUid >= 900000000) {
				sort = 1;
			} else {
					float vipDScore = 0.01f;
					int userlevel = srcUserBaseInfoModel.getUserLevel();
					userlevel = userlevel+1;
					if(vipPrivilegeModel != null){
						userlevel = userlevel+vipPrivilegeModel.getAddRankScore();
						vipDScore = vipPrivilegeModel.getAddRankScore()/100f;
					}
					float guardDScore = 0.0001f;
					if(guardPrivilegeModel != null){
						userlevel = userlevel+guardPrivilegeModel.getAddRankScore();
						guardDScore = guardPrivilegeModel.getAddRankScore()/10000f;
					}
					sort = userlevel;
					DecimalFormat df = new DecimalFormat("0.0000");
					dscore = Double.parseDouble(df.format(sort + vipDScore + guardDScore));
			}
			
			//如果是守护
			if(userGuardInfoModel != null && nowtime.intValue() <=userGuardInfoModel.getCushiontime().intValue()){
				Set<Tuple> allSet = RelationRedisService.getInstance().getRoomAllGuardSortByMoney(dstUid, 0l);
				int k = 0;
				for (Tuple tuple : allSet) {
					k++;
					if(tuple.getElement().equals(String.valueOf(srcUid))) {
						sort = k;
						break;
					}
				}
			}
			
			mapList.put("sort", sort);
			returnModel.setData(mapList);
			// TOSY 用户进房广播
			if (!OtherRedisService.getInstance().getUserIntoRoom(srcUid, dstUid)) {
				UserBaseInfoModel userBaseinfo = userService.getUserbaseInfoByUid(srcUid, false);
				rpcAdminUserEnterRoom(userBaseinfo.getUserLevel(), srcUid, dstUid, userBaseinfo.getNickname(),
						userBaseinfo.getHeadimage(), userBaseinfo.getSex(), userGuardInfoModel, userVipInfoModel, sort);

				Double dlFollows = RelationRedisService.getInstance().isFollows(srcUid, dstUid);
				if (dlFollows != null) {
					// 修改关注列表的score值
					RelationRedisService.getInstance().addFollows(srcUid, dstUid, dlFollows + 1, "on");
				}
				// 15秒之内
				OtherRedisService.getInstance().addUserIntoRoom(srcUid, dstUid);
				
				//是否进入过直播间
				boolean havEnterRoom = OtherRedisService.getInstance().havUserEnterRoomDay(srcUid, dstUid);
				if(!havEnterRoom) {
					OtherRedisService.getInstance().userEnterRoomDay(srcUid, dstUid);
					TaskService.getInstance().taskProcess(srcUid, TaskConfigLib.RoomsEntered, 1);
				}
				// TOSY TASK

				try{
					AsyncManager.getInstance().execute(new UpdRankAsyncTask(srcUid, dstUid, userBaseInfoModel.getRecommend(),
							userBaseInfoModel.getContrRq(),joinPlace,dscore.toString()));
				}catch (Exception ex){
					logger.error("UpdRankAsyncTask>>>",ex);
				}

			}

		} catch (Exception e) {
			returnModel.setCode(CodeContant.USERASSETEXITS);
			returnModel.setMessage("进房失败");
			logger.error("<error-enterRoom Exception is >", e);
		}
	}
	
	@Override
	public void enterRoomVisitor(Integer dstUid,int os, ReturnModel returnModel) {
		Map<String, Object> mapList = new HashMap<String, Object>();
		Map<String, Object> map = new HashMap<String, Object>();

		try {
			Set<Integer> gameIds = GameAppConfigLib.allGameAppRoomIds();
			// 获取主播信息
			UserBaseInfoModel userBaseInfoModel = userService.getUserbaseInfoByUid(dstUid, false);
			if (!gameIds.contains(dstUid)) {
				if (userBaseInfoModel == null) {
	
					returnModel.setCode(CodeContant.ARTISTIDEXCEPT);
					returnModel.setMessage("主播异常");
	
					map = new HashMap<String, Object>();
					map.put("timeslong", 0);
					map.put("persontimes", 0);
					map.put("roomlikes", 0);
					map.put("creditTotal", 0);
					map.put("rq", 0);
					returnModel.setData(map);
					return;
				}
				if (!userBaseInfoModel.getLiveStatus() && os !=5 ) {
					returnModel.setCode(CodeContant.ARTISTIDEXCEPT);
					returnModel.setMessage("主播已关闭直播间");
	
					LiveMicTimeModel liveMicTimeModel = liveService.getLiveMicInfoByUid(dstUid, false);
					if (liveMicTimeModel == null) {
						map.put("timeslong", 0);
						map.put("persontimes", 0);
						map.put("roomlikes", 0);
						map.put("creditTotal", 0);
						map.put("rq", UserRedisService.getInstance().getUserAllRankRqScore(String.valueOf(dstUid)));
						returnModel.setData(map);
					} else {
						if (liveMicTimeModel.getEndtime() == 0) {
							map.put("timeslong", 300);
						} else {
							map.put("timeslong", liveMicTimeModel.getEndtime() - liveMicTimeModel.getStarttime());
						}
						map.put("persontimes", liveMicTimeModel.getAudience());
						map.put("roomlikes", liveMicTimeModel.getLikes());
						map.put("creditTotal", liveMicTimeModel.getCredit());
						map.put("rq", UserRedisService.getInstance().getUserAllRankRqScore(String.valueOf(dstUid)));
					}
					returnModel.setData(map);
					return;
				}

				map = userService.getLivIngInfo(0, dstUid);
				if (map == null) {
					returnModel.setCode(CodeContant.USERASSETEXITS);
					returnModel.setMessage("用户异常");
					return;
				}
				map.put("liveStatus",userBaseInfoModel.getLiveStatus());
				if (UserRedisService.getInstance().getSwitchPlate(dstUid)) {
					map.put("plate", 1);
				} else {
					map.put("plate", 2);
				}
	
				map.put("rq", UserRedisService.getInstance().getUserAllRankRqScore(String.valueOf(dstUid)));
				mapList.put("artistInfo", map);
				// 视频流配置
				map = liveService.getVideoConfig(0, dstUid, userBaseInfoModel.getVideoline());
	
				if (map.size() <= 0) {
					returnModel.setCode(CodeContant.LIVESERVICECONFIG);
					returnModel.setMessage("获取服务器配置出错");
					return;
				}
				// TOSY 第三方流判断
				String stream = configService.getThirdStream(dstUid);
				if (null != stream) {
					map.put("domain", stream);
				}
				mapList.put("videoStream", map);

				returnModel.setData(mapList);
			}

		} catch (Exception e) {
			returnModel.setCode(CodeContant.USERASSETEXITS);
			returnModel.setMessage("进房失败");
			logger.error("<error-enterRoom Exception is >", e);
		}
	}

	public class UpdRankAsyncTask implements IAsyncTask {

		private int srcUid;
		private int dstUid;
		private int recommend;
		private int rq;
		private int joinPlace; //加入列表 1普通用户列表 2守护列表
		private String sort; //排序分数

		public UpdRankAsyncTask(int srcUid, int dstUid, int recommend, int rq, int joinPlace, String sort) {
			this.srcUid = srcUid;
			this.dstUid = dstUid;
			this.recommend = recommend;
			this.rq = rq;
			this.joinPlace = joinPlace;
			this.sort = sort;
		}

		@Override
		public void runAsync() {
			// 直播间中的用户列表排序设值
			if(joinPlace==1){
				RelationRedisService.getInstance().userIntoRoom(srcUid, dstUid, Double.valueOf(sort));	
			}else{
				RelationRedisService.getInstance().guardUserIntoRoom(srcUid, dstUid, Double.valueOf(sort));	
			}
			RelationRedisService.getInstance().allUserIntoRoom(srcUid, dstUid, Double.valueOf(sort));

			// 热门主播排序
//			if (recommend == 2 || recommend == 3) {
//
//				int roomShowUsers = getRoomShowUsers(dstUid, rq);
//				OtherRedisService.getInstance().addRecommendRoom(dstUid, recommend, roomShowUsers, 1);
//			}
		}

		@Override
		public void afterOk() {
		}

		@Override
		public void afterError(Exception e) {
		}

		@Override
		public String getName() {
			return "UpdRankAsyncTask";
		}

	}

	/**
	 * 用户退出房间
	 * 
	 * @param srcUid
	 *            用户uid
	 * @param dstUid
	 *            主播uid
	 * @param returnModel
	 *            返回
	 */
	@Override
	public void exitRoom(Integer srcUid, Integer dstUid, ReturnModel returnModel) {
		// 获取进房时间
		Long lg = RelationRedisService.getInstance().userExitRoom(srcUid, dstUid);
		if(null == lg){
			return;
		}
		
		UserBaseInfoModel userBaseInfoModel  = userService.getUserbaseInfoByUid(dstUid, false);
		// 热门主播排序
//		if (userBaseInfoModel != null
//				&& (userBaseInfoModel.getRecommend() == 2 || userBaseInfoModel.getRecommend() == 3)) {
//			int roomShowUsers = getRoomShowUsers(dstUid, userBaseInfoModel.getContrRq());
//			OtherRedisService.getInstance().addRecommendRoom(dstUid, userBaseInfoModel.getRecommend(),
//					roomShowUsers, 1);
//		}

		long duration = System.currentTimeMillis() / 1000 - lg;
		if (duration > (30 * 60)) {
			// TOSY TASK
			TaskService.getInstance().taskProcess(srcUid, TaskConfigLib.Watching30Mins, 1);
		}

		if (duration > 5 * 60) {
			TaskService.getInstance().taskProcess(srcUid, TaskConfigLib.WatchLive5Mins, 1);
		}

		//TOSY ADD 2nd Stream
		try{
			if(null != userBaseInfoModel){
				UserBaseInfoModel uSrc = userService.getUserbaseInfoByUid(srcUid, false);
				if(null != uSrc){
					liveService.updateLiveMic(uSrc,userBaseInfoModel,0);
					liveService.liveInviteCanncel(uSrc);
				}	
			}
		}catch(Exception e){
			logger.error("<exitRoom updateLiveMic>异常" + e.toString());
		}
		
	}
	
	
	/**
	 * 获取主播房间中的所有用户列表包含守护 vip 
	 * 
	 * @param dstUid
	 *            //主播uid
	 * @param returnModel
	 */
	@Override
	public void allUserlistOfLive(int dstUid, int srcUid, Integer page, ReturnModel returnModel) {

		Set<Tuple> redSet = new HashSet<Tuple>();
		Map<String, Object> map = new HashMap<String, Object>();
		List<Map<String, Object>> listRobot = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> listReal = new ArrayList<Map<String, Object>>();

		List<String> uidsRobot = new ArrayList<String>();
		List<String> uidsReal = new ArrayList<String>();

		UserBaseInfoModel userBaseInfoModel = userService.getUserbaseInfoByUid(dstUid, false);
		if (userBaseInfoModel == null) {
			returnModel.setCode(CodeContant.ARTISTIDEXCEPT);
			returnModel.setMessage("该主播不存在");
			return;
		}

		if (page <= 0) {
			page = 1;
		}
		//获取所有用户
		redSet = RelationRedisService.getInstance().getAllUserListInAnchor(dstUid, Long.valueOf(page));
		Map<String,Object> scoreMap = new HashMap<String,Object>();
		if (redSet != null && redSet.size() > 0) {
			for (Tuple tuple : redSet) {
				Integer iuid = Integer.valueOf(tuple.getElement());
				if (iuid >= 900000000) {
					uidsRobot.add(iuid.toString());
				} else {
					uidsReal.add(iuid.toString());
				}
				scoreMap.put(iuid.toString(), tuple.getScore());
			}
		}
		
		if (uidsReal.size() > 0) {
			listReal = userService.getUserProfile(uidsReal.toArray(new String[0]));
		}
		if (uidsRobot.size() > 0) {
			listRobot = userService.getRobotProfile(uidsRobot.toArray(new String[0]));
		}
		listReal.addAll(listRobot);
		
		//获取房间所有的管理员列表
		Map<String, String> manageMap = RelationRedisService.getInstance().getManagelistOfAnchor(dstUid);
		//循环所有的用户信息 补齐管理员	，vip，守护信息
		for(Map<String, Object> userMap : listReal){
			Map<String, Object> vipmap = new HashMap<String, Object>();
			Map<String, Object> guardmap = new HashMap<String, Object>();
			int manage = 0;
			//管理员信息
			if(manageMap != null){
				if(manageMap.get(userMap.get("uid").toString()) != null){
					manage = 1;
				}
			}
			userMap.put("roomManage", manage);
			String dscore = scoreMap.get(userMap.get("uid").toString()).toString();
			String[] split = dscore.split("\\.");
			if(split.length==2){ //根据进房传入的double后续值判断用户当前的身份
				String string = split[1];
				if(string.length()>3){
					int vipValue = Integer.parseInt(string.substring(0,2)); //前两位为VIP
					int guardValue = Integer.parseInt(string.substring(2,4)); //后两位为守护
					Map<Integer, Map<String, Object>> addSocreConfig = ValueaddServiceConfigLib.getAddSocreConfig(); //获取身份附加值对应的身份和等级配置信息
					Map<String, Object> vipConfigMap = addSocreConfig.get(vipValue);
					Map<String, Object> guardConfigMap = addSocreConfig.get(guardValue);
					if(vipConfigMap != null){
						int gid = (int)vipConfigMap.get("gid");
						int level = (int)vipConfigMap.get("level");
						if(gid > 0){
							ValueaddPrivilegeModel privilegeModel = ValueaddServiceUtil.getPrivilege(gid, level);
							if(gid == 43 || gid == 44){
								vipmap.put("vipIcon", privilegeModel.getIconId());
								userMap.put("vipInfo", vipmap);
							}
						}
					}
					if(guardConfigMap != null){
						int gid = (int)guardConfigMap.get("gid");
						int level = (int)guardConfigMap.get("level");
						if(gid > 0){
							ValueaddPrivilegeModel privilegeModel = ValueaddServiceUtil.getPrivilege(gid, level);
							if(gid == 45 || gid == 46){
								guardmap.put("guardIcon", privilegeModel.getIconId());
								userMap.put("guardInfo", guardmap);
							}
						}
					}
					
				}
			}
			userMap.put("sort", (int)Double.parseDouble(dscore));
		}
		map = new HashMap<String, Object>();
		map.put("list", listReal);
		map.put("total", this.getRoomShowUsers(dstUid, userBaseInfoModel.getContrRq()));
		returnModel.setData(map);
	}

	/**
	 * 获取主播房间中的用户列表包含守护（PC）
	 * 
	 * @param dstUid
	 *            //主播uid
	 * @param returnModel
	 */
	@Override
	public void userlistOfLiveForPc(int dstUid, int srcUid, Integer page, ReturnModel returnModel) {

		Set<Tuple> redSet = new HashSet<Tuple>();
		Map<String, Object> map = new HashMap<String, Object>();
		List<Map<String, Object>> listRobot = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> listReal = new ArrayList<Map<String, Object>>();

		List<String> uidsRobot = new ArrayList<String>();
		List<String> uidsReal = new ArrayList<String>();

		UserBaseInfoModel userBaseInfoModel = userService.getUserbaseInfoByUid(dstUid, false);
		if (userBaseInfoModel == null) {
			returnModel.setCode(CodeContant.ARTISTIDEXCEPT);
			returnModel.setMessage("该主播不存在");
			return;
		}

		if (page <= 0) {
			page = 1;
		}
		//获取守护列表
		redSet = RelationRedisService.getInstance().getGuardUserListInAnchor(dstUid, Long.valueOf(page));
		Map<String,Object> guardMap = new HashMap<String,Object>();
		if (redSet != null && redSet.size() > 0) {
			for (Tuple tuple : redSet) {
				Integer iuid = Integer.valueOf(tuple.getElement());
				if (iuid >= 900000000) {
					uidsRobot.add(iuid.toString());
				} else {
					uidsReal.add(iuid.toString());
				}
				guardMap.put(iuid.toString(), tuple.getScore());
			}
		}
		
		//获取用户列表
		redSet = RelationRedisService.getInstance().getUserListInAnchor(dstUid, Long.valueOf(page));
		Map<String,Object> vipMap = new HashMap<String,Object>();
		if (redSet != null && redSet.size() > 0) {
			for (Tuple tuple : redSet) {
				Integer iuid = Integer.valueOf(tuple.getElement());
				if (iuid >= 900000000) {
					uidsRobot.add(iuid.toString());
				} else {
					uidsReal.add(iuid.toString());
				}
				vipMap.put(iuid.toString(), tuple.getScore());
			}
		}
		if (uidsReal.size() > 0) {
			listReal = userService.getUserProfile(uidsReal.toArray(new String[0]));
		}
		if (uidsRobot.size() > 0) {
			listRobot = userService.getRobotProfile(uidsRobot.toArray(new String[0]));
		}
		listReal.addAll(listRobot);
		
		
		//获取房间所有的管理员列表
		Map<String, String> manageMap = RelationRedisService.getInstance().getManagelistOfAnchor(dstUid);
		//循环所有的用户信息 补齐管理员	，vip，守护信息
		for(Map<String, Object> userMap : listReal){
			Map<String, Object> vipmap = new HashMap<String, Object>();
			Map<String, Object> guardmap = new HashMap<String, Object>();
			int manage = 0;
			//管理员信息
			if(manageMap != null){
				if(manageMap.get(userMap.get("uid").toString()) != null){
					manage = 1;
				}
			}
			userMap.put("roomManage", manage);
			//vip信息
			if(vipMap.containsKey(userMap.get("uid").toString())){
				String dscore = vipMap.get(userMap.get("uid").toString()).toString();
				String[] split = dscore.split("\\.");
				if(split.length==2){ //根据进房传入的double后续值判断用户当前的身份
					String string = split[1];
					if(string.length()==4){
						int vipValue = Integer.parseInt(string.substring(0,2)); //前两位为VIP
						Map<Integer, Map<String, Object>> addSocreConfig = ValueaddServiceConfigLib.getAddSocreConfig(); //获取身份附加值对应的身份和等级配置信息
						Map<String, Object> vipConfigMap = addSocreConfig.get(vipValue);
						if(vipConfigMap != null){
							int gid = (int)vipConfigMap.get("gid");
							int level = (int)vipConfigMap.get("level");
							if(gid > 0){
								ValueaddPrivilegeModel privilegeModel = ValueaddServiceUtil.getPrivilege(gid, level);
								if(gid == 43 || gid == 44){
									vipmap.put("vipIcon", privilegeModel.getIconId());
									userMap.put("vipInfo", vipmap);
								}
							}
						}
					}
				}
				userMap.put("sort", (int)Double.parseDouble(dscore));
			}
			//守护信息
			if(guardMap.containsKey(userMap.get("uid").toString())){
				String dscore = guardMap.get(userMap.get("uid").toString()).toString();
				String[] split = dscore.split("\\.");
				if(split.length==2){ //根据进房传入的double后续值判断用户当前的身份
					String string = split[1];
					if(string.length()==4){
						int guardValue = Integer.parseInt(string.substring(2,4)); //后两位为守护
						Map<Integer, Map<String, Object>> addSocreConfig = ValueaddServiceConfigLib.getAddSocreConfig(); //获取身份附加值对应的身份和等级配置信息
						Map<String, Object> guardConfigMap = addSocreConfig.get(guardValue);
						if(guardConfigMap != null){
							int gid = (int)guardConfigMap.get("gid");
							int level = (int)guardConfigMap.get("level");
							if(gid > 0){
								ValueaddPrivilegeModel privilegeModel = ValueaddServiceUtil.getPrivilege(gid, level);
								if(gid == 45 || gid == 46){
									guardmap.put("guardIcon", privilegeModel.getIconId());
									userMap.put("guardInfo", guardmap);
								}
							}
						}
						
					}
				}
				userMap.put("sort", (int)Double.parseDouble(dscore));
			}
		}
		map = new HashMap<String, Object>();
		map.put("list", listReal);
		map.put("total", this.getRoomShowUsers(dstUid, userBaseInfoModel.getContrRq()));
		returnModel.setData(map);
	}
	/**
	 * 获取主播房间中的用户列表
	 * 
	 * @param dstUid
	 *            //主播uid
	 * @param returnModel
	 */
	@Override
	public void userlistOfLive(int dstUid, int srcUid, Integer page, ReturnModel returnModel) {

		Set<Tuple> redSet = new HashSet<Tuple>();
		Map<String, Object> map = new HashMap<String, Object>();
		List<Map<String, Object>> listRobot = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> listReal = new ArrayList<Map<String, Object>>();

		List<String> uidsRobot = new ArrayList<String>();
		List<String> uidsReal = new ArrayList<String>();

		UserBaseInfoModel userBaseInfoModel = userService.getUserbaseInfoByUid(dstUid, false);
		if (userBaseInfoModel == null) {
			returnModel.setCode(CodeContant.ARTISTIDEXCEPT);
			returnModel.setMessage("该主播不存在");
			return;
		}

		if (page <= 0) {
			page = 1;
		}
		redSet = RelationRedisService.getInstance().getUserListInAnchor(dstUid, Long.valueOf(page));
		Map<String,Object> vipMap = new HashMap<String,Object>();
		if (redSet != null && redSet.size() > 0) {
			for (Tuple tuple : redSet) {
				Integer iuid = Integer.valueOf(tuple.getElement());
				if (iuid >= 900000000) {
					uidsRobot.add(iuid.toString());
				} else {
					uidsReal.add(iuid.toString());
				}
				vipMap.put(iuid.toString(), tuple.getScore());
			}
		}
		if (uidsReal.size() > 0) {
			listReal = userService.getUserProfile(uidsReal.toArray(new String[0]));
		}
		if (uidsRobot.size() > 0) {
			listRobot = userService.getRobotProfile(uidsRobot.toArray(new String[0]));
		}

		listReal.addAll(listRobot);
		
		
		Map<String, String> manageMap = RelationRedisService.getInstance().getManagelistOfAnchor(dstUid);
		for(Map<String, Object> userMap : listReal){
			Map<String, Object> vipmap = new HashMap<String, Object>();
			int manage = 0;
			if(manageMap != null){
				if(manageMap.get(userMap.get("uid").toString()) != null){
					manage = 1;
				}
			}
			userMap.put("roomManage", manage);
			if(vipMap.containsKey(userMap.get("uid").toString())){
				String dscore = vipMap.get(userMap.get("uid").toString()).toString();
				String[] split = dscore.split("\\.");
				if(split.length==2){ //根据进房传入的double后续值判断用户当前的身份
					String string = split[1];
					if(string.length()>3){
						int vipValue = Integer.parseInt(string.substring(0,2)); //前两位为VIP
						Map<Integer, Map<String, Object>> addSocreConfig = ValueaddServiceConfigLib.getAddSocreConfig(); //获取身份附加值对应的身份和等级配置信息
						Map<String, Object> vipConfigMap = addSocreConfig.get(vipValue);
						if(vipConfigMap != null){
							int gid = (int)vipConfigMap.get("gid");
							int level = (int)vipConfigMap.get("level");
							if(gid > 0){
								ValueaddPrivilegeModel privilegeModel = ValueaddServiceUtil.getPrivilege(gid, level);
								if(gid == 43 || gid == 44){
									vipmap.put("vipIcon", privilegeModel.getIconId());
									userMap.put("vipInfo", vipmap);
								}
							}
						}
						
					}
				}
				userMap.put("sort", (int)Double.parseDouble(dscore));
			}
		}
		map = new HashMap<String, Object>();
		map.put("list", listReal);
		map.put("total", this.getRoomShowUsers(dstUid, userBaseInfoModel.getContrRq()));

		returnModel.setData(map);
	}
	
	/**
	 * 获取主播房间中的守护列表
	 * 
	 * @param dstUid
	 *            //主播uid
	 * @param returnModel
	 */
	@Override
	public void guardUserlistOfLive(int dstUid, int srcUid, Integer page, ReturnModel returnModel) {

		Set<Tuple> redSet = new HashSet<Tuple>();
		Map<String, Object> map = new HashMap<String, Object>();
		List<Map<String, Object>> listRobot = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> listReal = new ArrayList<Map<String, Object>>();

		List<String> uidsRobot = new ArrayList<String>();
		List<String> uidsReal = new ArrayList<String>();

		UserBaseInfoModel userBaseInfoModel = userService.getUserbaseInfoByUid(dstUid, false);
		if (userBaseInfoModel == null) {
			returnModel.setCode(CodeContant.ARTISTIDEXCEPT);
			returnModel.setMessage("该主播不存在");
			return;
		}

		if (page <= 0) {
			page = 1;
		}
		redSet = RelationRedisService.getInstance().getGuardUserListInAnchor(dstUid, Long.valueOf(page));
		Map<String,Object> guardMap = new HashMap<String,Object>();
		if (redSet != null && redSet.size() > 0) {
			for (Tuple tuple : redSet) {
				Integer iuid = Integer.valueOf(tuple.getElement());
				if (iuid >= 900000000) {
					uidsRobot.add(iuid.toString());
				} else {
					uidsReal.add(iuid.toString());
				}
				guardMap.put(iuid.toString(), tuple.getScore());
			}
		}
		if (uidsReal.size() > 0) {
			listReal = userService.getUserProfile(uidsReal.toArray(new String[0]));
		}
		if (uidsRobot.size() > 0) {
			listRobot = userService.getRobotProfile(uidsRobot.toArray(new String[0]));
		}

		listReal.addAll(listRobot);
		map = new HashMap<String, Object>();
		for(Map<String, Object> userMap : listReal){
			if(guardMap.containsKey(userMap.get("uid").toString())){
				
				String dscore = guardMap.get(userMap.get("uid").toString()).toString();
				String[] split = dscore.split("\\.");
				if(split.length==2){ //根据进房传入的double后续值判断用户当前的身份
					String string = split[1];
					if(string.length()==4){
						int guardValue = Integer.parseInt(string.substring(2,4)); //后两位为守护
						Map<Integer, Map<String, Object>> addSocreConfig = ValueaddServiceConfigLib.getAddSocreConfig(); //获取身份附加值对应的身份和等级配置信息
						Map<String, Object> guardConfigMap = addSocreConfig.get(guardValue);
						if(guardConfigMap != null){
							int gid = (int)guardConfigMap.get("gid");
							int level = (int)guardConfigMap.get("level");
							if(gid > 0){
								ValueaddPrivilegeModel privilegeModel = ValueaddServiceUtil.getPrivilege(gid, level);
								if(gid == 45 || gid == 46){
									userMap.put("guardIcon", privilegeModel.getIconId());
								}
							}
						}
					}
				}
				userMap.put("sort", (int)Double.parseDouble(dscore));
			}
		}
		map.put("list", listReal);
		map.put("total", RelationRedisService.getInstance().getRealGuardEnterRoom(dstUid));

		returnModel.setData(map);
	}
	
	/**
	 * 获取主播房间中的守护列表 (所有的，包括当前不在房间里的)
	 * 
	 * @param dstUid
	 *            //主播uid
	 * @param returnModel
	 * @deprecated
	 */
	//@Override
	public void guardUserAllList_back(int dstUid, int srcUid, Integer page, ReturnModel returnModel) {

		Set<Tuple> redSet = new HashSet<Tuple>();
		Map<String, Object> map = new HashMap<String, Object>();
		List<Map<String, Object>> listRobot = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> listReal = new ArrayList<Map<String, Object>>();

		List<String> uidsRobot = new ArrayList<String>();
		List<String> uidsReal = new ArrayList<String>();

		UserBaseInfoModel userBaseInfoModel = userService.getUserbaseInfoByUid(dstUid, false);
		if (userBaseInfoModel == null) {
			returnModel.setCode(CodeContant.ARTISTIDEXCEPT);
			returnModel.setMessage("该主播不存在");
			return;
		}

		if (page <= 0) {
			page = 1;
		}
		redSet = RelationRedisService.getInstance().getGuardUserListInAnchor(dstUid, Long.valueOf(page));
		Map<String,Object> guardMap = new HashMap<String,Object>();
		if (redSet != null && redSet.size() > 0) {
			for (Tuple tuple : redSet) {
				Integer iuid = Integer.valueOf(tuple.getElement());
				if (iuid >= 900000000) {
					uidsRobot.add(iuid.toString());
				} else {
					uidsReal.add(iuid.toString());
				}
				guardMap.put(iuid.toString(), tuple.getScore());
			}
		}
		if (uidsReal.size() > 0) {
			listReal = userService.getUserProfile(uidsReal.toArray(new String[0]));
		}
		if (uidsRobot.size() > 0) {
			listRobot = userService.getRobotProfile(uidsRobot.toArray(new String[0]));
		}

		listReal.addAll(listRobot);
		map = new HashMap<String, Object>();
		for(Map<String, Object> userMap : listReal){
			if(guardMap.containsKey(userMap.get("uid").toString())){
				String dscore = guardMap.get(userMap.get("uid").toString()).toString();
				String[] split = dscore.split("\\.");
				if(split.length==2){ //根据进房传入的double后续值判断用户当前的身份
					String string = split[1];
					if(string.length()==4){
						int guardValue = Integer.parseInt(string.substring(2,4)); //后两位为守护
						Map<Integer, Map<String, Object>> addSocreConfig = ValueaddServiceConfigLib.getAddSocreConfig(); //获取身份附加值对应的身份和等级配置信息
						Map<String, Object> guardConfigMap = addSocreConfig.get(guardValue);
						if(guardConfigMap != null){
							int gid = (int)guardConfigMap.get("gid");
							int level = (int)guardConfigMap.get("level");
							if(gid > 0){
								ValueaddPrivilegeModel privilegeModel = ValueaddServiceUtil.getPrivilege(gid, level);
								if(gid == 45 || gid == 46){
									userMap.put("guardIcon", privilegeModel.getIconId());
									userMap.put("guardLevel", level);
									long nowtime = System.currentTimeMillis()/1000;
									UserGuardInfoModel userGuardInfoModel = ValueaddServiceUtil.getGuardInfo((int)userMap.get("uid"), dstUid);
									Long days = DateUtils.getDaysBetweenTime(nowtime, new Long(userGuardInfoModel.getEndtime()));
									userMap.put("surplusDays", days);
									userMap.put("sort", (int)Double.parseDouble(dscore));
									userMap.put("inRoom", 1);
								}
							}
						}
					}
				}
			}
		}
		
		//所有的守护的业务代码
		//获取本房间所有的守护信息
		String roomAllGuardStr = UserRedisService.getInstance().get(RedisContant.roomAllGuard+dstUid);
		List<UserGuardInfoModel> roomAllGuardList = new ArrayList<UserGuardInfoModel>();
		if (StringUtils.isNotEmpty(roomAllGuardStr)) {
			roomAllGuardList = (List<UserGuardInfoModel>) JSONObject.parseArray(roomAllGuardStr, UserGuardInfoModel.class);
		}
		//如果所有的守护数量大于当前的守护数量才查找不在房间的守护
		if(roomAllGuardList.size() > listReal.size()){
			Map<Integer,UserGuardInfoModel> guardAllUidMap = new HashMap<Integer,UserGuardInfoModel>();
			//roomAllGuardList转成guardAllUidMap
			for(UserGuardInfoModel guardInfoModel : roomAllGuardList){
				guardAllUidMap.put(guardInfoModel.getUid(), guardInfoModel);
			}
			//移除已经在房间里的守护 获得不在本房间的守护信息
			for(Map<String, Object> userMap : listReal){
				guardAllUidMap.remove((Integer)userMap.get("uid"));
			}
			List<Map<String, Object>> allList = new ArrayList<Map<String, Object>>();
			List<Map<String, Object>> allListRobot = new ArrayList<Map<String, Object>>();
			//遍历不在本房间的守护 并分别获取用户和机器人的信息
			Iterator<Map.Entry<Integer, UserGuardInfoModel>> entries = guardAllUidMap.entrySet().iterator();  
			while (entries.hasNext()){
				Map.Entry<Integer, UserGuardInfoModel> entry = entries.next();
				Integer guardUid = entry.getKey();
				uidsRobot = new ArrayList<String>();
				uidsReal = new ArrayList<String>();
				if (guardUid >= 900000000) {
					uidsRobot.add(guardUid.toString());
				} else {
					uidsReal.add(guardUid.toString());
				}
				if (uidsReal.size() > 0) {
					allList.add(userService.getUserProfile(guardUid));
				}
				if (uidsRobot.size() > 0) {
					allListRobot.add(userService.getRobotProfile(guardUid));
				}
			}
			//把机器人和用户的信息汇总
			allList.addAll(allListRobot);
			//遍历所有离线守护信息 并增加相应的基本字段
			long nowtime = System.currentTimeMillis()/1000;
			for(Map<String, Object> userMap : allList){
				if(guardAllUidMap.containsKey((Integer)userMap.get("uid"))){
					UserGuardInfoModel guardInfoModel = guardAllUidMap.get(userMap.get("uid"));
					ValueaddPrivilegeModel privilegeModel = ValueaddServiceUtil.getPrivilege(guardInfoModel.getGid(), guardInfoModel.getLevel());
					userMap.put("guardIcon", privilegeModel.getIconId());
					int level = (int)userMap.get("userLevel");
					userMap.put("sort", level+privilegeModel.getAddRankScore());
					userMap.put("guardLevel", guardInfoModel.getLevel());
					
					Long days = DateUtils.getDaysBetweenTime(nowtime, new Long(guardInfoModel.getEndtime()));
					userMap.put("surplusDays", days);
					userMap.put("inRoom", 0);
				}
			}
			listReal.addAll(allList);
		}
		
		map.put("list", listReal);

		returnModel.setData(map);
	}
	
	/**
	 * 获取主播房间中的守护列表 (所有的，包括当前不在房间里的)
	 * 
	 * @param dstUid
	 *            //主播uid
	 * @param returnModel
	 */
	@Override
	public void guardUserAllList(int dstUid, int srcUid, Integer page, ReturnModel returnModel) {

		Set<Tuple> allSet = new HashSet<Tuple>();
		Map<String, Object> map = new HashMap<String, Object>();
		List<Map<String, Object>> listReal = new ArrayList<Map<String, Object>>();

		UserBaseInfoModel userBaseInfoModel = userService.getUserbaseInfoByUid(dstUid, false);
		if (userBaseInfoModel == null) {
			returnModel.setCode(CodeContant.ARTISTIDEXCEPT);
			returnModel.setMessage("该主播不存在");
			return;
		}
		if (page <= 0) {
			page = 1;
		}
		//获取房间内所有守护观众
		allSet = RelationRedisService.getInstance().getRoomAllGuardSortByMoney(dstUid, Long.valueOf(page));
		//获取房间内所有在线的守护观众
		Set<Tuple> onlineRedSet = RelationRedisService.getInstance().getGuardUserListInAnchor(dstUid, 0L);
		//房间内在线的守护观众uids
		List<String> onlineUids = new ArrayList<String>();
		for (Tuple tuple : onlineRedSet) {
			onlineUids.add(tuple.getElement());
		}
		
		int k = 0;
		//房间内所有的守护观众uids
		String[] uids = new String[allSet.size()];
		for (Tuple tuple : allSet) {
			uids[k++] = tuple.getElement();
		}
		//获取所有守护观众的基本信息
		List<Map<String, Object>> users = userService.getUserProfile(uids);
		k = 0;
		for (Map<String, Object> userMap : users) {
			UserGuardInfoModel userGuardInfoModel = ValueaddServiceUtil.getGuardInfo(Integer.parseInt(userMap.get("uid").toString()), dstUid);
			if(userGuardInfoModel==null) {
				continue;
			}
			k++;
			int level = 1;//永远默认1级
			ValueaddPrivilegeModel privilegeModel = ValueaddServiceUtil.getPrivilege(userGuardInfoModel.getGid(), level);
			userMap.put("guardIcon", privilegeModel.getIconId());
			userMap.put("guardLevel", level);
			long nowtime = System.currentTimeMillis()/1000;
			Long days = DateUtils.getDaysBetweenTime(nowtime, new Long(userGuardInfoModel.getEndtime()));
			userMap.put("surplusDays", days);
			userMap.put("sort", k);
			userMap.put("inRoom", onlineUids.contains(userMap.get("uid").toString())?1:0);
			listReal.add(userMap);
		}
		map.put("list", listReal);
		returnModel.setData(map);
	}

	/**
	 * 送礼功能
	 * 
	 * @param srcUid
	 *            送礼人uid
	 * @param dstUid
	 *            收礼人uid
	 * @param anchoruid
	 *            收礼人anchoruid =null或未空 dstuid则给主播送礼，否则
	 *            但dstuid=anchoruid给主播送礼，否则 给用户送礼
	 * @param gid
	 *            礼物id
	 * @param count
	 *            礼物数量
	 */
	@Override
	public void sendGift(String stamp, Integer srcUid, Integer dstUid, Integer anchoruid, int gid, int count, Byte os,
			int combo, int mengzhuSurplushCount,String iosVer, ReturnModel returnModel) {
		long lg1 = System.currentTimeMillis();

		if (count <= 0) {
			count = 1;
		}
		ConfigGiftModel giftConfigModel = configService.getGiftInfoByGidNew(gid);
		if (giftConfigModel == null) {
			returnModel.setCode(CodeContant.GIFTCONFIGEXIST);
			returnModel.setMessage("礼物不存在");
			return;
		}
		Long lgNow = System.currentTimeMillis() / 1000;
		
		String gname = giftConfigModel.getGname();
		//int price = giftConfigModel.getGprice();
		int price = giftConfigModel.getGprice();
		//根据版本 控制是否显示审核金额
		if(StringUtils.isNotBlank(iosVer)) {
			IosVersionModel iosVersionModel = webService.getIosShow(iosVer);
			if(null != iosVersionModel) {
				if(iosVersionModel.getAudit() == 1) {//1审核版本 0非审核版本
					price = giftConfigModel.getGpriceaudit();
				}
			}
		}//end 根据版本 控制是否显示审核金额
		
		int credit = giftConfigModel.getCredit();
		int wealth = giftConfigModel.getWealth();
		UserAssetModel sendUserAssetModel = userService.getUserAssetByUid(srcUid, false);
		if (sendUserAssetModel == null) {
			returnModel.setCode(CodeContant.USERASSETEXITS);
			returnModel.setMessage("用户不存在");
			return;
		}
		BigDecimal b_price = new BigDecimal(price);
		BigDecimal b_count = new BigDecimal(count);
		BigDecimal b_all_price = b_price.multiply(b_count);
		if(b_all_price.compareTo(new BigDecimal(Integer.MAX_VALUE/100)) > 0) {
			returnModel.setCode(CodeContant.CONSYSTEMERR);
			returnModel.setMessage("非法请求");
			return;
		}
		if (price * count > sendUserAssetModel.getMoney()) {
			returnModel.setCode(CodeContant.MONEYLESS);
			returnModel.setMessage("金额不足");
			return;
		}
		long lg2 = System.currentTimeMillis();
		System.out.println("timelong 基础判断:lg2-lg1=" + (lg2 - lg1));
		// 礼物总价值
		int sendTotal = price * count;
		// 主播获得价值

		double gets = credit * count;
		double getsTotal = credit * count;
		int wealths = wealth * count;
		// 送礼者 扣费
		int res = userService.updUserAssetBySendUid(srcUid, sendTotal, wealths);
		if (res == 0) {
			returnModel.setCode(CodeContant.MONEYDEDUCT);
			returnModel.setMessage("扣费失败");
			return;
		}
		// 收礼者资产更新
		UserBaseInfoModel dstUserBaseinfo = userService.getUserbaseInfoByUid(dstUid, false);
		if(lgNow>1483200000){
			Map<String, String> support = GiftPromotionConfigLib.getSupport();
			String unionId = support.get(srcUid.toString());
			if(StringUtils.isNotEmpty(unionId)){
				if(dstUserBaseinfo != null && unionId.equals(dstUserBaseinfo.getFamilyId().toString())){
					gets = 0;
				}
			}
		}
		
		res = userService.updUserAssetByGetUid(dstUid, gets,getsTotal);
		if (res == 0) {
			returnModel.setCode(CodeContant.MONEYACCEPT);
			returnModel.setMessage("收礼失败");
			return;
		}
		long lg3 = System.currentTimeMillis();
		System.out.println("timelong 扣费、更新操作:lg3-lg2=" + (lg3 - lg2));
		UserBaseInfoModel userBaseinfo = userService.getUserbaseInfoByUid(srcUid, false);
		
		// 收礼者的资产
		UserAssetModel getUserAssetModel = userService.getUserAssetByUid(dstUid, false);
		res = billService.insertBill(gid, count, price, srcUid, sendUserAssetModel.getMoney(),
				sendUserAssetModel.getWealth() + sendTotal, sendUserAssetModel.getCredit(), dstUid,
				getUserAssetModel.getMoney(), getUserAssetModel.getWealth(), getUserAssetModel.getCredit(),
				lgNow, 1, gets, os, "", userBaseinfo.getNickname(),
				dstUserBaseinfo.getNickname(),dstUserBaseinfo.getFamilyId());

		if (res == 0) {
			logger.error("<sendGift>插入账单失败: gid="+gid+" count="+count+" price="+price+" srcUid="+srcUid+" surplus=" + sendUserAssetModel.getMoney() +" wealth="+
					(sendUserAssetModel.getWealth() + sendTotal)+" credit="+sendUserAssetModel.getCredit()+" dstUid="+dstUid+" dstmoney="+
					getUserAssetModel.getMoney()+" dstWealth="+getUserAssetModel.getWealth()+" dstCredit="+getUserAssetModel.getCredit()+"  addtime="+
					lgNow+" type="+1+" dstGets="+gets+" os="+os+" bak="+" srcNickname="+ userBaseinfo.getNickname()+" dstNickname="+
					dstUserBaseinfo.getNickname());
		}
		
		long time = System.currentTimeMillis();
		userTransactionHisService.saveUserTransactionHis(11, srcUid, 0.00, (long)sendTotal, time, "", 1);
		userTransactionHisService.saveUserTransactionHis(10, dstUid, 0.00, (long)sendTotal, time, "", 1);
		// 中奖相关time
		if (anchoruid == null || dstUid.equals(anchoruid)) {
			luckyGift(gid, gname, userBaseinfo.getUid(),
					userBaseinfo.getNickname(), anchoruid,
					dstUserBaseinfo.getNickname(), price, count, wealth,
					credit, true);
		} else {
			luckyGift(gid, gname, userBaseinfo.getUid(),
					userBaseinfo.getNickname(), anchoruid,
					dstUserBaseinfo.getNickname(), price, count, wealth,
					credit, false);
		}

		UserAssetModel userAssetModel = userService.getUserAssetByUid(srcUid, false);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("consume", sendTotal);
		map.put("surplus", userAssetModel.getMoney());
		map.put("creditTotal", getUserAssetModel.getCreditTotal());
		
		int curRq = UserRedisService.getInstance().getUserAllRankRqScore(String.valueOf(dstUid));
		if (gid == 24) {
			// 记录当天送的萌气猪数量
			RedisCommService.getInstance().incrBy(RedisContant.RedisNameOther,RedisContant.mengzhuOfDay+srcUid+":"+DateUtils.dateToString(null, "yyyyMMdd"), count, DateUtils.getSecondeToNextDay());
			map.put("rq", curRq + 1);
			map.put("mengzhuSurplushCount", mengzhuSurplushCount-1);
			rqZhuActivity(srcUid,dstUid);
		} else {
			map.put("rq", curRq);
		}
		returnModel.setCode(CodeContant.OK);
		returnModel.setMessage("送礼成功");

		returnModel.setData(map);
		long lg4 = System.currentTimeMillis();
		System.out.println("timelong 添加送礼记录:lg4-lg3=" + (lg4 - lg3));
		// TOSY 拼装发送房间广播

		GiftSendCMod msgBody = new GiftSendCMod();
		msgBody.setStamp(stamp);
		msgBody.setGid(gid);
		msgBody.setCombo(combo);
		msgBody.setType(giftConfigModel.getGtype());
		msgBody.setName(giftConfigModel.getGname());
		msgBody.setGpctype(giftConfigModel.getGpctype());
		msgBody.setCount(count);
		msgBody.setDstuid(dstUid);
		if (gid == 24) {
			msgBody.setRq(curRq + 1);
		} else {
			msgBody.setRq(curRq);
		}
		msgBody.setUid(srcUid);
		msgBody.setGets((int) gets);
		msgBody.setGetTotal(getUserAssetModel.getCreditTotal());
		msgBody.setAvatar(userBaseinfo.getHeadimage());
		msgBody.setNickname(userBaseinfo.getNickname());
		msgBody.setSex(userBaseinfo.getSex());
		msgBody.setLevel(userBaseinfo.getUserLevel());
		msgBody.setDstNickname(dstUserBaseinfo.getNickname());
		msgBody.setMonthSupport(UserRedisService.getInstance().getSupportByUid(dstUid, 4));
		
		if (anchoruid == 0) {
			anchoruid = dstUid;
		}
		String signParams = EncryptUtils.signParams(VarConfigUtils.ServiceSecret, "appKey=" + VarConfigUtils.ServiceKey,
				"msgBody=" + JSONObject.toJSONString(msgBody), "roomOwner=" + anchoruid);

		Unirest.post(UrlConfigLib.getUrl("url").getAdminrpc_publish_room()).field("appKey", VarConfigUtils.ServiceKey)
				.field("msgBody", JSONObject.toJSONString(msgBody)).field("roomOwner", anchoruid)
				.field("sign", signParams).asJsonAsync();
		long lg5 = System.currentTimeMillis();
		System.out.println("timelong 房间广播:lg5-lg4=" + (lg5 - lg4));

		try{
			// 异步调用处理用户等级问题
			AsyncManager.getInstance().execute(
					new UpdateUserLevelAsyncTask(srcUid, dstUid, anchoruid, sendTotal, (int) gets, gid, count, gname, wealths));
		}catch (Exception ex){
			logger.error("UpdateUserLevelAsyncTask>>>",ex);
		}

		if (price > 0) {
			// TOSY TASK
			TaskService.getInstance().taskProcess(srcUid, TaskConfigLib.SendGift, 1);
		}

		if (gid == 128) {
			TaskService.getInstance().taskProcess(srcUid, TaskConfigLib.SendPopular5, 1);
			TaskService.getInstance().taskProcess(srcUid, TaskConfigLib.SendPopularGift, 1);
		}
		
		try{
			if(CodeContant.OK == returnModel.getCode()){
				long secNow = System.currentTimeMillis()/1000;
				ActivitiesModeConfigLib.getIns().actModeSendGiftCheckPoint(srcUid,anchoruid,dstUserBaseinfo.getFamilyId(),gid,count,secNow);	
			}	
		}catch(Exception e){
			logger.error("actModeSendGiftCheckPoint " + e.toString());
		}
		
		//赠送活动礼物相关
		sendActivityGift(srcUid,userBaseinfo.getNickname(), anchoruid,dstUserBaseinfo.getNickname(), gid, count,price * count);
		
		return;
	}

	/**
	 * 奥运活动
	 * 
	 * @param gid
	 *            礼物gid =38为奥运加油 =37奥运金牌
	 * @param count
	 *            礼物数量
	 * @param srcuid
	 *            送礼人uid
	 * @param dstuid
	 *            收礼人uid
	 */
	public void Olympic(int gid, int count, int srcuid, int dstuid,int anchor) {
		int heatvalue = 0;// 热度值
		if (gid == 105) {
			// 为奥运加油
			heatvalue = count * 1;
		} else {
			// 奥运金牌
			heatvalue = count * 88;
		}

		// 添加用户热度值 保存60天
		OtherRedisService.getInstance().zincrby(RedisContant.RedisNameOther, RedisContant.olympicHeatUser,
				String.valueOf(srcuid), heatvalue, 60 * 24 * 60 * 60);
		// 添加主播热度值
		OtherRedisService.getInstance().zincrby(RedisContant.RedisNameOther, RedisContant.olympicHeatAnchor,
				String.valueOf(dstuid), heatvalue, 60 * 24 * 60 * 60);

		String times = DateUtils.dateToString(null, "yyyyMMdd");
		// 添加礼物的获取数量
		OtherRedisService.getInstance().hincrBy(RedisContant.RedisNameOther,
				RedisContant.olympicGift + gid + ":" + times, String.valueOf(dstuid), count, 30 * 24 * 60 * 60);

		int jindou = 105;
		int longka = 106;
		int ijiayou = 0;
		int goldMedal = 0;
		String ijiayouStr = OtherRedisService.getInstance().hget(RedisContant.olympicGift + jindou + ":" + times,String.valueOf(dstuid));
		String goldMedalStr = OtherRedisService.getInstance().hget(RedisContant.olympicGift + longka + ":" + times,String.valueOf(dstuid));
		if(ijiayouStr != null){
			ijiayou = Integer.parseInt(ijiayouStr);
		}
		if(goldMedalStr != null){
			goldMedal = Integer.parseInt(goldMedalStr);
		}
		int grade = 1;
		if (ijiayou / 66 == 0 || goldMedal / 66 == 0) {
			grade = 1;

		} else if (ijiayou / 99 == 0 || goldMedal / 99 == 0) {
			grade = 2;
		} else if (ijiayou / 188 == 0 || goldMedal / 188 == 0) {
			grade = 3;
		} else if (ijiayou / 520 == 0 || goldMedal / 520 == 0) {
			grade = 4;
		} else if (ijiayou / 1314 == 0 || goldMedal / 1314 == 0) {
			grade = 5;
		} else if (ijiayou / 1314 > 0 && goldMedal / 1314 > 0) {
			grade = 6;
		} 
		
		String haveStars = OtherRedisService.getInstance().get(RedisContant.RedisNameOther, RedisContant.olympicStar+ dstuid + ":" + times);
		if(haveStars != null){
			String todayFixedStr = OtherRedisService.getInstance().hget(RedisContant.olympicDayFixed + ":" + times,String.valueOf(dstuid));
			if(grade>5 && todayFixedStr == null){
				OtherRedisService.getInstance().zincrby(RedisContant.RedisNameOther, RedisContant.olympicHeatAnchor, String.valueOf(dstuid), 10000, 60 * 24 * 60 * 60);
				OtherRedisService.getInstance().zincrby(RedisContant.RedisNameOther, RedisContant.olympicDayFixed, String.valueOf(dstuid), 1, 60 * 24 * 60 * 60);
				OtherRedisService.getInstance().hincrBy(RedisContant.RedisNameOther, RedisContant.olympicDayFixed + ":" + times, String.valueOf(dstuid), 1, 30 * 24 * 60 * 60);
			}
		}
		OtherRedisService.getInstance().set(RedisContant.RedisNameOther, RedisContant.olympicStar + dstuid + ":" + times,String.valueOf(grade), 60 * 24 * 60 * 60);
	}
	
	public void luckyGift(int gid, String giftname, int uid, String username, int anchoruid, String anchorname, int gprice, int count, int wealth, int credit, boolean isAnchor){
		ConcurrentHashMap<String, Object> luckyGift = BaseConfigLib.getLuckyGiftConfig();
		if(luckyGift.containsKey(gid+"")){
			long start = System.currentTimeMillis();
			Random random = new Random();
			//获取幸运礼物的倍率
			Map<String,Object> probas = BaseConfigLib.getprobasConfig();
			logger.debug("内存中的倍率 : "+probas);
			List<Object> probasList = new ArrayList<Object>(); 
			//组装新云数字
			for (Map.Entry<String, Object> probaObj : probas.entrySet()) {
				Map<String,Object> proba = (Map<String, Object>)probaObj.getValue();
				Map<Integer, Object> luckyNumMap = new HashMap<Integer, Object>();
				int divisor = Integer.parseInt(proba.get("divisor").toString());
				for (int i = 0; i < divisor; i++) {
					int luckyNum = random.nextInt(Integer.parseInt(proba.get("dividend").toString()));
					luckyNumMap.put(luckyNum, luckyNum);
				}
				proba.put("luckyNums", luckyNumMap);
				probasList.add(proba);
			}
			logger.debug("倍率加入幸运数字之后的结果 : "+probasList);
			//总共获得的奖励金额
			int allMoney = 0;
			for(Object probaObj : probasList){
				Map<String,Object> proba = (Map<String, Object>)probaObj;
				int luckyCount = 0;
				int maxcount = Integer.parseInt(proba.get("maxcount").toString());
				int max = 0;
				int dividend = Integer.parseInt(proba.get("dividend").toString());
				Map<Integer,Object> luckyNumMap =  (Map<Integer,Object>)proba.get("luckyNums");
				for (int i = 0; i < count; i++) {
					if(max==maxcount){
						break;
					}
					int luckyNum = random.nextInt(dividend);
					if(luckyNumMap.get(luckyNum)!=null){
						max ++;
						luckyCount++;
					}
				}
				if(luckyCount>0){
					//中奖倍率
					int multiples = Integer.parseInt(proba.get("multiples").toString());
					//总价值
					int priceTotal = gprice * luckyCount * multiples;
					//总财务
					int wealthTotal = wealth * luckyCount * multiples;
					//总声援
					int creditTotal = credit * luckyCount * multiples;
					//送出价格
					int sendPrice = gprice * count;
					allMoney += priceTotal;
					logger.debug("中奖倍率 : "+proba.get("multiples")+"   中奖次数: "+luckyCount +"  中奖总倍率 : "+ luckyCount*Integer.parseInt(proba.get("multiples").toString()));
					//增加用户资产
					billService.insertPrizeBill(uid, gid, anchoruid, multiples, luckyCount, gprice, priceTotal, wealthTotal, creditTotal,sendPrice,count);
					//增加跑道
					List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
					HashMap<String, Object> map = new HashMap<String, Object>();
					
					List<Map<String, Object>> toUserList = new ArrayList<Map<String, Object>>();
					HashMap<String, Object> toUserMap = new HashMap<String, Object>();
					String isRunWay = proba.get("isRunWay").toString();
					// 跑道数据组装
					
						map.put("name", "喜讯：");
						map.put("color", "#ff2d55");
						list.add(map);
						toUserList.add(map);

						map = new HashMap<String, Object>();
						map.put("name", " 恭喜 ");
						map.put("color", "");
						list.add(map);
						toUserList.add(map);

						map = new HashMap<String, Object>();
						map.put("name", username);
						map.put("color", "#fff08c");
						list.add(map);
						
						toUserMap.put("name", "您");
						toUserMap.put("color", "");
						toUserList.add(toUserMap);
						
						if(isAnchor){
							map = new HashMap<String, Object>();
							map.put("name", "在");
							map.put("color", "");
							list.add(map);
							map = new HashMap<String, Object>();
							map.put("name", anchorname);
							map.put("color", "#fff08c");
							list.add(map);
							map = new HashMap<String, Object>();
							map.put("name", " 直播间喜中");
							map.put("color", "");
							list.add(map);
						}else{
							map = new HashMap<String, Object>();
							map.put("name", " 喜中");
							map.put("color", "");
							list.add(map);
						}
						toUserMap = new HashMap<String, Object>();
						toUserMap.put("name", " 喜中");
						toUserMap.put("color", "");
						toUserList.add(toUserMap);
						

						map = new HashMap<String, Object>();
						map.put("gid", gid);
						map.put("color", "");
						list.add(map);
						toUserList.add(map);
						
						map = new HashMap<String, Object>();
						map.put("name", multiples);
						map.put("color", "#fff08c");
						list.add(map);
						toUserList.add(map);
						
						map = new HashMap<String, Object>();
						map.put("name", "倍大奖");
						map.put("color", "");
						list.add(map);
						toUserList.add(map);
						
						map = new HashMap<String, Object>();
						map.put("name", luckyCount);
						map.put("color", "#fff08c");
						list.add(map);
						toUserList.add(map);
						
						map = new HashMap<String, Object>();
						map.put("name", "次 , ");
						map.put("color", "");
						list.add(map);
						toUserList.add(map);
						
						map = new HashMap<String, Object>();
						map.put("name", "获得"+priceTotal+"金币 ");
						map.put("color", "");
						list.add(map);
						toUserList.add(map);
						
						if(!proba.get("decoratedWord").equals("")){
							map = new HashMap<String, Object>();
							map.put("name", proba.get("decoratedWord"));
							map.put("color", "");
						}
						list.add(map);

						// 跑道 
						
						RunWayCMod msgBody = new RunWayCMod();
						map = new HashMap<String, Object>();
						map.put("list", list);
						msgBody.setData(map);
						msgBody.setAnchorUid(anchoruid);
						msgBody.setAnchorName(anchorname);
					if(isRunWay.endsWith("1")){
						String signParams = EncryptUtils.signParams(VarConfigUtils.ServiceSecret,
								"appKey=" + VarConfigUtils.ServiceKey,
								"msgBody=" + JSONObject.toJSONString(msgBody));

						Unirest.post(UrlConfigLib.getUrl("url").getAdminrpc_publish_room_all())
								.field("appKey", VarConfigUtils.ServiceKey)
								.field("msgBody", JSONObject.toJSONString(msgBody))
								.field("sign", signParams).asJsonAsync();
					}
					
					//资产更新通知
					toUserMap = new HashMap<String, Object>();
					toUserMap.put("list", toUserList);
					List<Map<String, Object>> giftList = new ArrayList<Map<String, Object>>();
					ChatMessageUtil.sendGiftUpdateAssetAndBag(uid,username, null, toUserMap,giftList,priceTotal, multiples,luckyCount);
					//房间推送
					if(multiples>=500){
						
						if (gid == 66 &&  start >= 1473782400000L && start < 1474214400000L) {
							// 中秋月饼
							if (multiples == 1000) {
								RedisCommService.getInstance().zincrby(RedisContant.RedisNameUser, RedisContant.midMultiple, 2*luckyCount, String.valueOf(uid), 864000);
							}else {
								RedisCommService.getInstance().zincrby(RedisContant.RedisNameUser, RedisContant.midMultiple, luckyCount, String.valueOf(uid), 864000);
							}
						}
						
						List<Map<String, Object>> luckyList = new ArrayList<Map<String, Object>>();
						HashMap<String, Object> luckymap = new HashMap<String, Object>();
						if(luckyCount>1){
							luckymap.put("name", "恭喜 "+username+" 喜中 "+giftname+multiples);
							luckymap.put("color", "");
							luckyList.add(luckymap);
						}else{
							luckymap.put("name", "恭喜 "+username+" 喜中 "+giftname+multiples+"倍"+luckyCount+"次");
							luckymap.put("color", "");
							luckyList.add(luckymap);
						}
						ChatMessageUtil.roomLuckyNotice(anchoruid, list,Integer.parseInt(proba.get("gid").toString()),luckyList,uid,username,priceTotal,multiples,luckyCount);
					}else{
						ChatMessageUtil.roomLuckyNotice(anchoruid, list,Integer.parseInt(proba.get("gid").toString()),null,uid,username,priceTotal,multiples,luckyCount);
					}
					
				}
				
			}
			//增加用户资产
			userService.updAssetMoneyByUid(uid, (double)allMoney);
			long end = System.currentTimeMillis();
			long x = end-start;
			logger.debug("本次抽奖共耗时 : "+x);
		}
	}	
	
	/**
	 * 赠送活动礼品相关
	 * @param uid
	 * @param userName
	 * @param anchorId
	 * @param anchorName
	 * @param gid
	 * @param count 礼物数量
	 * @param amount  礼物一共金币数
	 */
	public void sendActivityGift(int uid, String userName, int anchorId, String anchorName, int gid, int count,int amount){
		//用户赠送喜鹊 
		Long lg = System.currentTimeMillis() / 1000;
		//1470412800  1470758400 七夕活动
		int queqId = 40;
		if(lg >= 1470412800 && lg <= 1470758400){
			if(gid==39){ //赠送喜鹊一定概率获得鹊桥
				int luckyCount = 0;
				for (int i = 0; i < count; i++) {
					int islucky = MathRandomUtil.RandomQueq();
					if(islucky==1){
						luckyCount++;
					}
				}
				if(luckyCount>0){
	    			userItemService.insertUserItem(uid, queqId, luckyCount, ItemSource.Activity);
	    			//发送私信通知
	    			//个人中奖提示信息组装
	    			List<Map<String, Object>> toUserList = new ArrayList<Map<String, Object>>();
					HashMap<String, Object> toUserMap = new HashMap<String, Object>();
					toUserMap.put("name", "喜从天降，你幸运获得"+luckyCount+"座鹊桥，快与佳人相约！");
					toUserMap.put("color", "");
					toUserList.add(toUserMap);
					//背包礼物信息组装
					List<Map<String, Object>> giftList = new ArrayList<Map<String, Object>>();
					HashMap<String, Object> giftMap = new HashMap<String, Object>();
					giftMap.put("gid", "40");
					giftMap.put("num", luckyCount);
					giftList.add(giftMap);
	    			ChatMessageUtil.sendGiftUpdateAssetAndBag(uid,null, null, toUserMap, giftList,null, null,null);
	    			
	    			//增加跑道
	    			List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
					HashMap<String, Object> map = new HashMap<String, Object>();
					map = new HashMap<String, Object>();
					map.put("name", "人间乞巧又佳期，鹊桥相会啪啪啪，");
					map.put("color", "");
					list.add(map);
					
					map.put("name", userName);
					map.put("color", "#fff08c");
					list.add(map);
					
					map = new HashMap<String, Object>();
					map.put("name", "幸运获得");
					map.put("color", "");
					list.add(map);
					
					map = new HashMap<String, Object>();
					map.put("name", luckyCount);
					map.put("color", "#fff08c");
					list.add(map);
					
					map = new HashMap<String, Object>();
					map.put("name", "座鹊桥与佳人相约！");
					map.put("color", "");
					list.add(map);
					
	    			RunWayCMod msgBody = new RunWayCMod();
					map = new HashMap<String, Object>();
					map.put("list", list);
					msgBody.setData(map);
					msgBody.setAnchorUid(anchorId);
					msgBody.setAnchorName(anchorName);

					String signParams = EncryptUtils.signParams(VarConfigUtils.ServiceSecret,
							"appKey=" + VarConfigUtils.ServiceKey,
							"msgBody=" + JSONObject.toJSONString(msgBody));

					Unirest.post(UrlConfigLib.getUrl("url").getAdminrpc_publish_room_all())
							.field("appKey", VarConfigUtils.ServiceKey)
							.field("msgBody", JSONObject.toJSONString(msgBody))
							.field("sign", signParams).asJsonAsync();
				}
			}
			if(gid==queqId){ //赠送鹊桥 增加排行榜分数
				String niuZTopKey = "nz";
				String niuTopKey = "niu";
				String zhinvTopKey = "zhinv";
				UserRedisService.getInstance().setRank(RedisContant.RankActivityTop+niuTopKey, uid+"", (double) count);
				UserRedisService.getInstance().setRank(RedisContant.RankActivityTop+niuZTopKey, uid+":"+anchorId, (double) count);
				UserRedisService.getInstance().setRank(RedisContant.RankActivityTop+zhinvTopKey, anchorId+"", (double) count);
			}
		}

		//女人节活动
		if (lg >= 1488816000 && lg < 1489075200) { //正式活动时间 lg >= 1488816000 && lg < 1489075200
			if (gid == 2 || gid== 109) {
				//主播排名
				RedisCommService.getInstance().zincrby(RedisContant.RedisNameUser, RedisContant.ActivityNRAnchor, amount, String.valueOf(anchorId), 15*24*3600);
				//用户排名
				RedisCommService.getInstance().zincrby(RedisContant.RedisNameUser, RedisContant.ActivityNRUser, amount, String.valueOf(uid), 15*24*3600);
			}
		}
		
		if ((gid == 105 || gid == 106) && lg >=1488038400 && lg < 1488297599) { //正式活动时间  lg >=1488038400 && lg < 1488297599
			// 奥运活动
			this.Olympic(gid, count, uid, anchorId,anchorId);
		}
		
		
	}

	/**
	 * 送礼功能
	 * 
	 * @param srcUid
	 *            送礼人uid
	 * @param dstUid
	 *            收礼人uid
	 * @param anchoruid
	 *            收礼人anchoruid =null或未空 dstuid则给主播送礼，否则
	 *            但dstuid=anchoruid给主播送礼，否则 给用户送礼
	 * @param gid
	 *            礼物id 
	 * @param count
	 *            礼物数量
	 */
	@Override
	public void sendGiftBag(String stamp, Integer srcUid, Integer dstUid, Integer anchoruid, int gid, int count,
			Byte os, int combo,String iosVer, ReturnModel returnModel) {
		long lg1 = System.currentTimeMillis();

		if (count <= 0) {
			count = 1;
		}
		ConfigGiftModel giftConfigModel = configService.getGiftInfoByGidNew(gid);
		if (giftConfigModel == null) {
			returnModel.setCode(CodeContant.GIFTCONFIGEXIST);
			returnModel.setMessage("礼物不存在");
			return;
		}

		String gname = giftConfigModel.getGname();
		//int price = giftConfigModel.getGprice();
		int price = giftConfigModel.getGprice();
		//根据版本 控制是否显示审核金额
		if(StringUtils.isNotBlank(iosVer)) {
			IosVersionModel iosVersionModel = webService.getIosShow(iosVer);
			if(null != iosVersionModel) {
				if(iosVersionModel.getAudit() == 1) {//1审核版本 0非审核版本
					price = giftConfigModel.getGpriceaudit();
				}
			}
		}//end 根据版本 控制是否显示审核金额
		int charm = giftConfigModel.getCharm();
		int wealth = giftConfigModel.getWealth();

		UserAssetModel sendUserAssetModel = userService.getUserAssetByUid(srcUid, false);
		if (sendUserAssetModel == null) {
			returnModel.setCode(CodeContant.USERASSETEXITS);
			returnModel.setMessage("送礼失败:" + CodeContant.USERASSETEXITS);
			return;
		}

		Map<Integer, UserItemModel> baglist = userItemService.getItemListByUid(srcUid, false);
		if (!baglist.containsKey(gid)) {
			returnModel.setCode(CodeContant.baggidless);
			returnModel.setMessage("背包礼物不足:" + CodeContant.baggidless);
			return;
		} else {
			if (baglist.get(gid).getNum() < count) {
				returnModel.setCode(CodeContant.baggidless);
				returnModel.setMessage("背包礼物不足:" + CodeContant.baggidless);
				return;
			}
		}

		long lg2 = System.currentTimeMillis();
		System.out.println("timelong 基础判断:lg2-lg1=" + (lg2 - lg1));
		// 礼物总价值
		int sendTotal = price * count;
		int charmTotal = charm * count;
		int wealths = wealth * count;
		int res = userItemService.updUserItemBySendUid(srcUid, gid, count);
		if (res == 0) {
			returnModel.setCode(CodeContant.baggiddeduct);
			returnModel.setMessage("送礼失败:" + CodeContant.baggiddeduct);
			return;
		}

		// 送礼者 扣费
		res = userService.updUserAssetBySendUid(srcUid, 0, wealths);
		if (res == 0) {
			returnModel.setCode(CodeContant.MONEYDEDUCT);
			returnModel.setMessage("送礼失败:" + CodeContant.MONEYDEDUCT);
			return;
		}
		// 收礼者资产更新
		UserBaseInfoModel dstUserBaseinfo = userService.getUserbaseInfoByUid(dstUid, false);
		double gets = 0;
		//设置声援值（新）
		if(charmTotal>0) {
			UserRedisService.getInstance().setSupportRank(srcUid, dstUid, charmTotal);
			UserRedisService.getInstance().setSupportRankByDstuid(srcUid, dstUid, charmTotal);
		}
		long lg3 = System.currentTimeMillis();
		System.out.println("timelong 扣费、更新操作:lg3-lg2=" + (lg3 - lg2));
		
		UserBaseInfoModel userBaseinfo = userService.getUserbaseInfoByUid(srcUid, false);
		// 收礼者的资产
		UserAssetModel getUserAssetModel = userService.getUserAssetByUid(dstUid, false);
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("consume", 0);
		map.put("surplus", sendUserAssetModel.getMoney());
		map.put("creditTotal", getUserAssetModel.getCreditTotal());
		map.put("bagflag", baglist.get(gid).getNum() - count);
		int curRq = UserRedisService.getInstance().getUserAllRankRqScore(String.valueOf(dstUid));
		if (gid == 24 || gid == 29) {
			map.put("rq", curRq + 1);
			rqZhuActivity(srcUid,dstUid);
		} else {
			map.put("rq", curRq);
		}
		map.put("monthSupport", UserRedisService.getInstance().getSupportByUid(dstUid, 4));
		returnModel.setCode(CodeContant.OK);
		returnModel.setMessage("送礼成功");
		
		Long lgNow = System.currentTimeMillis() / 1000;
		res = billService.insertBagBill(gid, count, price, srcUid, sendUserAssetModel.getMoney(), 
				sendUserAssetModel.getWealth() + sendTotal, sendUserAssetModel.getCredit(), dstUid, getUserAssetModel.getMoney(),
				getUserAssetModel.getWealth(), UserRedisService.getInstance().getSupportByUid(dstUid, 4), lgNow, 1, gets, os, "", userBaseinfo.getNickname(),
				dstUserBaseinfo.getNickname(), dstUserBaseinfo.getFamilyId(), charm, charmTotal);
		if (res == 0) {
			logger.error("<sendGiftBag>插入背包礼物账单失败: gid="+gid+" count="+count+" price="+price+" srcUid="+srcUid+" surplus=" + sendUserAssetModel.getMoney() +" wealth="+
					(sendUserAssetModel.getWealth() + sendTotal)+" credit="+sendUserAssetModel.getCredit()+" dstUid="+dstUid+" dstmoney="+
					getUserAssetModel.getMoney()+" dstWealth="+getUserAssetModel.getWealth()+" dstSupport="+UserRedisService.getInstance().getSupportByUid(dstUid, 4)+"  addtime="+
					lgNow+" type="+1+" dstGets="+gets+" os="+os+" bak="+" srcNickname="+ userBaseinfo.getNickname()+" dstNickname="+
					dstUserBaseinfo.getNickname()+" support="+charm+"totalsupport="+charmTotal);
		}
		returnModel.setData(map);
		long lg4 = System.currentTimeMillis();
		System.out.println("timelong 添加送礼记录:lg4-lg3=" + (lg4 - lg3));
		// TOSY 拼装发送房间广播

		GiftSendCMod msgBody = new GiftSendCMod();
		msgBody.setStamp(stamp);
		msgBody.setGid(gid);
		msgBody.setCombo(combo);
		msgBody.setType(giftConfigModel.getGtype());
		msgBody.setName(giftConfigModel.getGname());
		msgBody.setGpctype(giftConfigModel.getGpctype());
		msgBody.setCount(count);
		msgBody.setDstuid(dstUid);
		if (gid == 24) {
			msgBody.setRq(curRq + 1);
		} else {
			msgBody.setRq(curRq);
		}
		msgBody.setUid(srcUid);
		msgBody.setGets((int) gets);
		msgBody.setGetTotal(getUserAssetModel.getCreditTotal());
		msgBody.setAvatar(userBaseinfo.getHeadimage());
		msgBody.setNickname(userBaseinfo.getNickname());
		msgBody.setSex(userBaseinfo.getSex());
		msgBody.setLevel(userBaseinfo.getUserLevel());
		msgBody.setDstNickname(dstUserBaseinfo.getNickname());
		msgBody.setMonthSupport(UserRedisService.getInstance().getSupportByUid(dstUid, 4));
		
		if (anchoruid == 0) {
			anchoruid = dstUid;
		}
		String signParams = EncryptUtils.signParams(VarConfigUtils.ServiceSecret, "appKey=" + VarConfigUtils.ServiceKey,
				"msgBody=" + JSONObject.toJSONString(msgBody), "roomOwner=" + anchoruid);

		Unirest.post(UrlConfigLib.getUrl("url").getAdminrpc_publish_room()).field("appKey", VarConfigUtils.ServiceKey)
				.field("msgBody", JSONObject.toJSONString(msgBody)).field("roomOwner", anchoruid)
				.field("sign", signParams).asJsonAsync();
		long lg5 = System.currentTimeMillis();
		System.out.println("timelong 房间广播:lg5-lg4=" + (lg5 - lg4));

		try{
			// 异步调用处理用户等级问题
			AsyncManager.getInstance().execute(
					new UpdateUserLevelAsyncTask(srcUid, dstUid, anchoruid, sendTotal, (int) gets, gid, count, gname, wealths));
		}catch (Exception ex){
			logger.error("UpdateUserLevelAsyncTask>>>",ex);
		}

		if (price > 0) {
			// TOSY TASK
			TaskService.getInstance().taskProcess(srcUid, TaskConfigLib.SendGift, 1);
		}

		if (gid == 128) {
			TaskService.getInstance().taskProcess(srcUid, TaskConfigLib.SendPopular5, 1);
			TaskService.getInstance().taskProcess(srcUid, TaskConfigLib.SendPopularGift, 1);
		}

//		if ((gid == 37 || gid == 38) && lgNow >=1470326400 && lgNow < 1471795200) {
//			// 奥运活动
//			this.Olympic(gid, count, srcUid, dstUid,anchoruid);
//		}
		
		//TOSY ADD activities mode...
		try{
			if(CodeContant.OK == returnModel.getCode()){
				long secNow = System.currentTimeMillis()/1000;
				ActivitiesModeConfigLib.getIns().actModeSendGiftCheckPoint(srcUid,anchoruid,dstUserBaseinfo.getFamilyId(),gid,count,secNow);	
			}	
		}catch(Exception e){
			logger.error("actModeSendGiftCheckPoint " + e.toString());
		}
		
		//赠送活动礼物相关
		sendActivityGift(srcUid,userBaseinfo.getNickname(), anchoruid,dstUserBaseinfo.getNickname(), gid, count,price * count);
				
		return;
	}
	
	/**
	 * 后台添加声援值 业务
	 * @param uid
	 * @param credit
	 */
	public void addCreditByAdmin(int anchoruid,int credit){
		// 用户等级
		updUserLevel(0, anchoruid, anchoruid, 2, 1);
		// 日、周、月、总版
		UserRedisService.getInstance().setRank("", String.valueOf(anchoruid), 0,credit);
	}

	/**
	 * 更新用户等级数据
	 */
	private class UpdateUserLevelAsyncTask implements IAsyncTask {

		private int srcUid;
		private int dstUid;
		private int anchoruid;
		private int sends;
		private int gets;
		private int gid;
		private String gname;
		private int count;
		private int wealth;

		/**
		 * 初始化
		 * 
		 * @param srcUid
		 *            送礼人uid
		 * @param dstUid
		 *            收礼人uid
		 * @param sends
		 *            送出数
		 * @param gets
		 *            收到数
		 */
		public UpdateUserLevelAsyncTask(final Integer srcUid, Integer dstUid, int anchoruid, int sends, int gets,
				int gid, int count, String gname, int wealth) {
			this.srcUid = srcUid;
			this.dstUid = dstUid;
			this.anchoruid = anchoruid;
			this.sends = sends;
			this.gets = gets;
			this.gid = gid;
			this.gname = gname;
			this.count = count;
			this.wealth = wealth;
		}

		@SuppressWarnings("unused")
		@Override
		public void runAsync() {

			if (gets > 0) {
				RelationRedisService.getInstance().addRoomCreditThis(dstUid, gets);

				UserBaseInfoModel anchorModel = userService.getUserbaseInfoByUid(anchoruid, false);
				if(anchorModel != null&&anchorModel.getGameStatus()==1) {
					OtherRedisService.getInstance().addGameRoomCreditThis(anchorModel.getUid(), gets, anchorModel.getGameId());
				}
				
				// 热门主播排序
//				if (anchorModel != null && (anchorModel.getRecommend() == 2 || anchorModel.getRecommend() == 3)) {
//					int roomShowUsers = getRoomShowUsers(dstUid, anchorModel.getContrRq());
//					OtherRedisService.getInstance().addRecommendRoom(dstUid, anchorModel.getRecommend(), roomShowUsers,
//							1);
//				}
			}
			
			//每日消费增加经验
			userGuardInfoService.firstSpendUpdExp(srcUid, anchoruid);
			//用户送礼经验加成
			UserVipInfoModel vipInfoModel = ValueaddServiceUtil.getVipInfo(srcUid);
			if(vipInfoModel != null){
				ValueaddPrivilegeModel privilegeModel = ValueaddServiceUtil.getPrivilege(vipInfoModel.getGid(), 1);
				double speedup = privilegeModel.getLevelSpeedup();
				double exp = wealth * speedup;
				UserServiceImpl.getInstance().addUserExpByTask(srcUid, (int)exp);	
			}
			
			UserBaseInfoModel userModel = userService.getUserbaseInfoByUid(srcUid, false);

			UserBaseInfoModel anchorModel = null;
			RobotBaseInfoModel robotModel = null;
			if (dstUid >= 900000000) {
				robotModel = userService.getRobotbaseInfoByUid(dstUid, false);
			} else {
				anchorModel = userService.getUserbaseInfoByUid(dstUid, false);
			}

			if (dstUid != anchoruid && dstUid > 0) {

				// 送礼对象 非主播
				Double usersInAnchor = RelationRedisService.getInstance().checkUserInAnchor(dstUid, anchoruid);
				if (usersInAnchor == null) {
					// 送礼对象不在相同房间
					Long msgId = UserRedisService.getInstance().setPrivateMsgId(Integer.valueOf(dstUid));
					if (msgId != null) {
						Double follows = RelationRedisService.getInstance().isFollows(dstUid, srcUid);
						MessageCMod msgBody = new MessageCMod();
						msgBody.setMsgId(msgId.intValue());
						msgBody.setGid(gid);
						msgBody.setUid(srcUid);
						msgBody.setNickname(userModel.getNickname());
						msgBody.setHeadimage(userModel.getHeadimage());
						msgBody.setSex(userModel.getSex());
						msgBody.setLevel(userModel.getUserLevel());
						msgBody.setMsg("收到" + count + gname + ", 获取" + gets + "个魅力值, ");
						msgBody.setSendtime(System.currentTimeMillis() / 1000);
						msgBody.setRelation(follows == null ? 1 : 2);
						msgBody.setMsgType(4);
						msgBody.setDstNickname(
								anchorModel == null ? robotModel.getNickname() : anchorModel.getNickname());

						String msgbody = JSONObject.toJSONString(msgBody);
						// 添加redis
						Boolean blSetMsg = UserRedisService.getInstance().setPrivateMsg(Integer.valueOf(dstUid),
								msgId.toString(), msgbody);
						if (!blSetMsg) {
							// 消息保持失败
						} else {

							String signParams = EncryptUtils.signParams(VarConfigUtils.ServiceSecret,
									"appKey=" + VarConfigUtils.ServiceKey, "msgBody=" + msgbody, "users=" + dstUid);

							Unirest.post(UrlConfigLib.getUrl("url").getAdminrpc_publish_live())
									.field("appKey", VarConfigUtils.ServiceKey).field("msgBody", msgbody)
									.field("users", dstUid).field("sign", signParams).asJsonAsync();
						}
					}
				}
			}

			if (dstUid > 0) {
				// 记录当天收到的声援值
				if (gets > 0) {
					// 添加主播与用户关系
					RelationRedisService.getInstance().addDstGetSrc(srcUid, dstUid, (double) gets);
					// 修改粉丝的排序score 送礼值排序
					Double db = RelationRedisService.getInstance().isFansScore(dstUid, srcUid);
					if (db != null) {
						RelationRedisService.getInstance().addFans(dstUid, srcUid, db + gets, "on");
					}
					// 添加用户与主播的关系
					RelationRedisService.getInstance().addSrcSendDst(srcUid, dstUid, (double) sends);
					
					//添加本场获取的猪头数
					RelationRedisService.getInstance().addNowLiveSends(dstUid, sends);
					// 关注主播排序:送礼值+登录次数
					Double dlFollows = RelationRedisService.getInstance().isFollows(srcUid, dstUid);
					if (dlFollows == null) {
						// RelationRedisService.getInstance().addFollows(srcUid,
						// dstUid, (double) (sends * 1000), "on");
					} else {
						RelationRedisService.getInstance().addFollows(srcUid, dstUid, sends * 1000 + dlFollows, "on");
					}
				}
				
				if(gets>0 && dstUid==anchoruid) {
					UserBaseInfoModel userBaseInfoModel = userService.getUserbaseInfoByUid(anchoruid, false);
					if(userBaseInfoModel.getLianmaiStatus() == Constant.lianmai_status_1) {
						String pkAnchorId = OtherRedisService.getInstance().getPkAnchorRel(anchoruid);
						if(StringUtils.isNotBlank(pkAnchorId)) {
							Double firstUserVotes =  OtherRedisService.getInstance().zincrby(RedisContant.RedisNameUser, RedisContant.pkAnchorCharm, String.valueOf(anchoruid), gets, 0);
							Double secodUserVotes =  OtherRedisService.getInstance().zscore(RedisContant.RedisNameUser, RedisContant.pkAnchorCharm, pkAnchorId);
							PKNumOfVotesCMod msgBody = new PKNumOfVotesCMod();
							msgBody.setFirstUid(anchoruid);
							msgBody.setFirstUserVotes(firstUserVotes==null?0:firstUserVotes.longValue());
							msgBody.setSecodUid(Integer.valueOf(pkAnchorId));
							msgBody.setSecodUserVotes(secodUserVotes==null?0:secodUserVotes.longValue());
							pkService.sendRoomMessage(anchoruid, msgBody);
							pkService.sendRoomMessage(Integer.valueOf(pkAnchorId), msgBody);
						}
					}
				}
				
				//添加本场获得的人气猪数量
				if (gid == 24 || gid == 29) {
					RelationRedisService.getInstance().addNowLiveRq(dstUid, count);
				}
			
				if (sends >= 999) {
					
					String str1 = null;
					String str2 = null;
					if( dstUid == anchoruid) {
						str1 = " 在";
						str2 = " 直播间中怒刷 ";
					}else {
						str1 = " 为";
						str2 = " 怒刷";
					}
					
					List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
					Map<String, Object> map = new HashMap<String, Object>();

					// 礼物跑道
					if (sends >= 999 && sends < 5000) {

						map.put("name", userModel.getNickname());
						map.put("color", "#fff08c");
						list.add(map);

						map = new HashMap<String, Object>();
						map.put("name", str1);
						map.put("color", "");
						list.add(map);

						map = new HashMap<String, Object>();
						map.put("name", anchorModel == null ? robotModel.getNickname() : anchorModel.getNickname());
						map.put("color", "#fff08c");
						list.add(map);
						
						map = new HashMap<String, Object>();
						map.put("name", str2);
						map.put("color", "");
						list.add(map);

						map = new HashMap<String, Object>();
						map.put("gid", gid);
						list.add(map);

						map = new HashMap<String, Object>();
						map.put("name", " X ");
						map.put("color", "");
						list.add(map);

						map = new HashMap<String, Object>();
						map.put("name", count);
						map.put("color", "#ffff00");
						list.add(map);

						map = new HashMap<String, Object>();
						map.put("name", "，为土豪打call！");
						list.add(map);

					} else if (sends >= 5000 && sends < 10000) {

						map.put("name", userModel.getNickname());
						map.put("color", "#fff08c");
						list.add(map);
						
						map = new HashMap<String, Object>();
						map.put("name", str1);
						map.put("color", "");
						list.add(map);

						map = new HashMap<String, Object>();
						map.put("name", anchorModel == null ? robotModel.getNickname() : anchorModel.getNickname());
						map.put("color", "#fff08c");
						list.add(map);
						
						map = new HashMap<String, Object>();
						map.put("name", str2);
						map.put("color", "");
						list.add(map);

						map = new HashMap<String, Object>();
						map.put("gid", gid);
						list.add(map);

						map = new HashMap<String, Object>();
						map.put("name", " X ");
						map.put("color", "");
						list.add(map);

						map = new HashMap<String, Object>();
						map.put("name", count);
						map.put("color", "#ffff00");
						list.add(map);

						map = new HashMap<String, Object>();
						map.put("name", "，为土豪打call！");
						map.put("color", "");
						list.add(map);
					} else if (sends >= 10000) {

						map.put("name", userModel.getNickname());
						map.put("color", "#fff08c");
						list.add(map);

						map = new HashMap<String, Object>();
						map.put("name", str1);
						map.put("color", "");
						list.add(map);

						map = new HashMap<String, Object>();
						map.put("name", anchorModel == null ? robotModel.getNickname() : anchorModel.getNickname());
						map.put("color", "#fff08c");
						list.add(map);
						
						map = new HashMap<String, Object>();
						map.put("name", str2);
						map.put("color", "");
						list.add(map);

						map = new HashMap<String, Object>();
						map.put("gid", gid);
						list.add(map);

						map = new HashMap<String, Object>();
						map.put("name", " X ");
						map.put("color", "");
						list.add(map);

						map = new HashMap<String, Object>();
						map.put("name", count);
						map.put("color", "#ffff00");
						list.add(map);

						map = new HashMap<String, Object>();
						map.put("name", "，为土豪打call！");
						map.put("color", "");
						list.add(map);
					}

					if (list != null && list.size() > 0) {
						// 跑道
						map = new HashMap<String, Object>();
						map.put("list", list);
						RunWayCMod msgBody = new RunWayCMod();
						msgBody.setData(map);
						msgBody.setAnchorUid(anchorModel.getUid());
						msgBody.setAnchorName(
								anchorModel == null ? robotModel.getNickname() : anchorModel.getNickname());

						String signParams = EncryptUtils.signParams(VarConfigUtils.ServiceSecret,
								"appKey=" + VarConfigUtils.ServiceKey, "msgBody=" + JSONObject.toJSONString(msgBody));
						
						if (sends >= 18000) {
						//if (sends >= 10000000) {//审核版本
							Unirest.post(UrlConfigLib.getUrl("url").getAdminrpc_publish_room_all())
							.field("appKey", VarConfigUtils.ServiceKey)
							.field("msgBody", JSONObject.toJSONString(msgBody)).field("sign", signParams)
							.asJsonAsync();
						}
					}
				}
			}

			// 用户侧
			updUserLevel(srcUid, dstUid, anchoruid, 1, 1);

			if (dstUid > 0) {
				// 主播侧
				updUserLevel(srcUid, dstUid, anchoruid, 2, 1);
			}

			if (gid == 24 || gid == 29) {
				// 人气礼物
				// 日、周、月、总版
				UserRedisService.getInstance().setRankRQ(String.valueOf(dstUid), count);
			} else {
				Long pre_UserRank = UserRedisService.getInstance().getRankOfUserDay(srcUid, "user");
				Long pre_AnchorRank = UserRedisService.getInstance().getRankOfUserDay(dstUid, "anchor");

				// 日、周、月、总版
				UserRedisService.getInstance().setRank(String.valueOf(srcUid), String.valueOf(dstUid), sends,gets);

				Long cur_UserRank = UserRedisService.getInstance().getRankOfUserDay(srcUid, "user");
				Long cur_AnchorRank = UserRedisService.getInstance().getRankOfUserDay(dstUid, "anchor");
				if (cur_UserRank != null && cur_UserRank <= 9) {
					// 进前10
					if (pre_UserRank == null || cur_UserRank < pre_UserRank) {
						Set<String> userPosition = UserRedisService.getInstance().getUserPosition("user",
								cur_UserRank.intValue() + 1, cur_UserRank.intValue() + 1);
						if (userPosition != null) {
							for (String suid : userPosition) {
								UserBaseInfoModel userBaseInfoModel = userService
										.getUserbaseInfoByUid(Integer.valueOf(suid), false);
								if (userBaseInfoModel != null) {

									List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
									HashMap<String, Object> map = new HashMap<String, Object>();

									// 跑道数据组装
									map.put("name", "恭喜 ");
									map.put("color", "");
									list.add(map);

									map = new HashMap<String, Object>();
									map.put("name", userModel.getNickname());
									map.put("color", "#fff08c");
									list.add(map);

									map = new HashMap<String, Object>();
									map.put("name", " 击败了 ");
									map.put("color", "");
									list.add(map);

									map = new HashMap<String, Object>();
									map.put("name", userBaseInfoModel.getNickname());
									map.put("color", "#fff08c");
									list.add(map);

									map = new HashMap<String, Object>();
									map.put("name", " 富豪榜上升到第 ");
									map.put("color", "");
									list.add(map);

									map = new HashMap<String, Object>();
									map.put("name", cur_UserRank + 1);
									map.put("color", "#ffff00");
									list.add(map);

									if (cur_UserRank == 0) {
										map = new HashMap<String, Object>();
										map.put("name", " 名，君临天下！ ");
										map.put("color", "");
										list.add(map);
									} else {
										map = new HashMap<String, Object>();
										map.put("name", " 名，一鸣惊人！ ");
										map.put("color", "");
										list.add(map);
									}

									// 跑道
									RunWayCMod msgBody = new RunWayCMod();
									map = new HashMap<String, Object>();
									map.put("list", list);
									msgBody.setData(map);
									msgBody.setAnchorUid(anchorModel.getUid());
									msgBody.setAnchorName(anchorModel.getNickname());

									String signParams = EncryptUtils.signParams(VarConfigUtils.ServiceSecret,
											"appKey=" + VarConfigUtils.ServiceKey,
											"msgBody=" + JSONObject.toJSONString(msgBody));

									Unirest.post(UrlConfigLib.getUrl("url").getAdminrpc_publish_room_all())
											.field("appKey", VarConfigUtils.ServiceKey)
											.field("msgBody", JSONObject.toJSONString(msgBody))
											.field("sign", signParams).asJsonAsync();

									if (cur_UserRank == 0) {
										// 获得第一名
										String dayAlternate = UserRedisService.getInstance()
												.getDayAlternate(userBaseInfoModel.getUid().toString(), "user");
										if (StringUtils.isNotEmpty(dayAlternate)
												&& dayAlternate.equals(String.valueOf(srcUid))) {

											map = new HashMap<String, Object>();
											map.put("name", userModel.getNickname());
											map.put("color", "#fff08c");
											list.add(map);

											map = new HashMap<String, Object>();
											map.put("name", " 与 ");
											map.put("color", "");
											list.add(map);

											map = new HashMap<String, Object>();
											map.put("name", userBaseInfoModel.getNickname());
											map.put("color", "#fff08c");
											list.add(map);

											map = new HashMap<String, Object>();
											map.put("name", " 激烈争逐富豪榜， ");
											map.put("color", "");
											list.add(map);

											map = new HashMap<String, Object>();
											map.put("name", userModel.getNickname());
											map.put("color", "#fff08c");
											list.add(map);

											map = new HashMap<String, Object>();
											map.put("name", " 暂居第一，谁可以君临天下 ？");
											map.put("color", "");
											list.add(map);

											msgBody = new RunWayCMod();
											map = new HashMap<String, Object>();
											map.put("list", list);
											msgBody.setData(map);
											msgBody.setAnchorUid(anchorModel.getUid());
											msgBody.setAnchorName(anchorModel.getNickname());

											signParams = EncryptUtils.signParams(VarConfigUtils.ServiceSecret,
													"appKey=" + VarConfigUtils.ServiceKey,
													"msgBody=" + JSONObject.toJSONString(msgBody));
											System.out.println(
													"2 url:" + UrlConfigLib.getUrl("url").getAdminrpc_publish_room_all()
															+ "  msgBody=" + JSONObject.toJSONString(msgBody)
															+ "  sign=" + signParams);
											Unirest.post(UrlConfigLib.getUrl("url").getAdminrpc_publish_room_all())
													.field("appKey", VarConfigUtils.ServiceKey)
													.field("msgBody", JSONObject.toJSONString(msgBody))
													.field("sign", signParams).asJsonAsync();
										}
										UserRedisService.getInstance().setDayAlternate(String.valueOf(srcUid),
												userBaseInfoModel.getUid().toString(), "user");
									}
								}
								break;
							}
						}
					}
				}
				if (cur_AnchorRank != null && cur_AnchorRank <= 9) {
					if (pre_AnchorRank == null || cur_AnchorRank < pre_AnchorRank) {
						Set<String> userPosition = UserRedisService.getInstance().getUserPosition("anchor",
								cur_AnchorRank.intValue() + 1, cur_AnchorRank.intValue() + 1);
						if (userPosition != null) {
							for (String suid : userPosition) {
								UserBaseInfoModel userBaseInfoModel = userService
										.getUserbaseInfoByUid(Integer.valueOf(suid), false);
								if (userBaseInfoModel != null) {

									List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
									HashMap<String, Object> map = new HashMap<String, Object>();

									// 跑道数据组装
									map.put("name", "恭喜 ");
									map.put("color", "");
									list.add(map);

									map = new HashMap<String, Object>();
									map.put("name", anchorModel.getNickname());
									map.put("color", "#fff08c");
									list.add(map);

									map = new HashMap<String, Object>();
									map.put("name", " 击败了 ");
									map.put("color", "");
									list.add(map);

									map = new HashMap<String, Object>();
									map.put("name", userBaseInfoModel.getNickname());
									map.put("color", "#fff08c");
									list.add(map);

									map = new HashMap<String, Object>();
									map.put("name", " 播主榜上升到第 ");
									map.put("color", "");
									list.add(map);

									map = new HashMap<String, Object>();
									map.put("name", cur_AnchorRank + 1);
									map.put("color", "#ffff00");
									list.add(map);

									if (cur_AnchorRank == 0) {
										map = new HashMap<String, Object>();
										map.put("name", " 名，守的住才是王者！ ");
										map.put("color", "");
										list.add(map);
									} else {
										map = new HashMap<String, Object>();
										map.put("name", " 名，再接再厉！ ");
										map.put("color", "");
										list.add(map);
									}

									// 跑道

									RunWayCMod msgBody = new RunWayCMod();
									map = new HashMap<String, Object>();
									map.put("list", list);
									msgBody.setData(map);
									msgBody.setAnchorUid(anchorModel.getUid());
									msgBody.setAnchorName(anchorModel.getNickname());

									String signParams = EncryptUtils.signParams(VarConfigUtils.ServiceSecret,
											"appKey=" + VarConfigUtils.ServiceKey,
											"msgBody=" + JSONObject.toJSONString(msgBody));

									System.out.println("3 url:"
											+ UrlConfigLib.getUrl("url").getAdminrpc_publish_room_all() + "  msgBody="
											+ JSONObject.toJSONString(msgBody) + "  sign=" + signParams);

									Unirest.post(UrlConfigLib.getUrl("url").getAdminrpc_publish_room_all())
											.field("appKey", VarConfigUtils.ServiceKey)
											.field("msgBody", JSONObject.toJSONString(msgBody))
											.field("sign", signParams).asJsonAsync();

									if (cur_AnchorRank == 0) {
										// 获得第一名
										String dayAlternate = UserRedisService.getInstance()
												.getDayAlternate(userBaseInfoModel.getUid().toString(), "anchor");
										if (StringUtils.isNotEmpty(dayAlternate)
												&& dayAlternate.equals(String.valueOf(dstUid))) {

											map = new HashMap<String, Object>();
											map.put("name", anchorModel.getNickname());
											map.put("color", "#fff08c");
											list.add(map);

											map = new HashMap<String, Object>();
											map.put("name", " 与 ");
											map.put("color", "");
											list.add(map);

											map = new HashMap<String, Object>();
											map.put("name", userBaseInfoModel.getNickname());
											map.put("color", "#fff08c");
											list.add(map);

											map = new HashMap<String, Object>();
											map.put("name", " 激烈争逐播主榜， ");
											map.put("color", "");
											list.add(map);

											map = new HashMap<String, Object>();
											map.put("name", userModel.getNickname());
											map.put("color", "#fff08c");
											list.add(map);

											map = new HashMap<String, Object>();
											map.put("name", " 暂居第一，前往支援 ");
											map.put("color", "");
											list.add(map);

											msgBody = new RunWayCMod();
											map = new HashMap<String, Object>();
											map.put("list", list);
											msgBody.setData(map);
											msgBody.setAnchorUid(anchorModel.getUid());
											msgBody.setAnchorName(anchorModel.getNickname());

											signParams = EncryptUtils.signParams(VarConfigUtils.ServiceSecret,
													"appKey=" + VarConfigUtils.ServiceKey,
													"msgBody=" + JSONObject.toJSONString(msgBody));

											Unirest.post(UrlConfigLib.getUrl("url").getAdminrpc_publish_room_all())
													.field("appKey", VarConfigUtils.ServiceKey)
													.field("msgBody", JSONObject.toJSONString(msgBody))
													.field("sign", signParams).asJsonAsync();
										}
										UserRedisService.getInstance().setDayAlternate(String.valueOf(srcUid),
												userBaseInfoModel.getUid().toString(), "anchor");
									}
								}
								break;
							}
						}
					}
				}

				String week = DateUtils.getWeekStart(0);
				Double zhouxinGift = UserRedisService.getInstance().getZhouxinGift(week, gid);
				if (zhouxinGift != null) {
					Long pre_user_zhouxin = UserRedisService.getInstance().getZhouxinRankUid(srcUid, "user", week,
							String.valueOf(gid));
					Long pre_anchor_zhouxin = UserRedisService.getInstance().getZhouxinRankUid(dstUid, "user", week,
							String.valueOf(gid));
					// 周星礼物
					ConfigGiftModel giftConfigModel = configService.getGiftInfoByGidNew(gid);
					int gprice = giftConfigModel.getGprice();
					int sum = gprice*count;
					UserRedisService.getInstance().setZhouxin(gid, String.valueOf(srcUid), String.valueOf(dstUid),
							sum);

					Long cur_user_zhouxin = UserRedisService.getInstance().getZhouxinRankUid(srcUid, "user", week,
							String.valueOf(gid));
					Long cur_anchor_zhouxin = UserRedisService.getInstance().getZhouxinRankUid(dstUid, "user", week,
							String.valueOf(gid));
					if (cur_user_zhouxin != null && cur_user_zhouxin <= 9) {
						if (pre_user_zhouxin == null || cur_user_zhouxin < pre_user_zhouxin) {
							Set<String> userPosition = UserRedisService.getInstance().getZhouxinRankUidPosition("user",
									cur_user_zhouxin.intValue() + 1, cur_user_zhouxin.intValue() + 1, week,
									String.valueOf(gid));
							if (userPosition != null) {
								for (String suid : userPosition) {
									UserBaseInfoModel userBaseInfoModel = userService
											.getUserbaseInfoByUid(Integer.valueOf(suid), false);
									if (userBaseInfoModel != null) {

										List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
										HashMap<String, Object> map = new HashMap<String, Object>();

										// 跑道数据组装
										map.put("name", "恭喜 ");
										map.put("color", "");
										list.add(map);

										map = new HashMap<String, Object>();
										map.put("name", userModel.getNickname());
										map.put("color", "#fff08c");
										list.add(map);

										map = new HashMap<String, Object>();
										map.put("name", " 击败了 ");
										map.put("color", "");
										list.add(map);

										map = new HashMap<String, Object>();
										map.put("name", userBaseInfoModel.getNickname());
										map.put("color", "#fff08c");
										list.add(map);

										map = new HashMap<String, Object>();
										map.put("name", " 周星 ");
										map.put("color", "");
										list.add(map);

										map = new HashMap<String, Object>();
										map.put("gid", gid);
										map.put("color", "");
										list.add(map);

										map = new HashMap<String, Object>();
										map.put("name", " 上升到第 ");
										map.put("color", "");
										list.add(map);

										map = new HashMap<String, Object>();
										map.put("name", cur_user_zhouxin + 1);
										map.put("color", "#ffff00");
										list.add(map);

										if (cur_AnchorRank >= 5) {
											map = new HashMap<String, Object>();
											map.put("name", " 名，再接再厉！ ");
											map.put("color", "");
											list.add(map);
										} else {
											map = new HashMap<String, Object>();
											map.put("name", " 名，这个是我的！ ");
											map.put("color", "");
											list.add(map);
										}

										// 跑道

										RunWayCMod msgBody = new RunWayCMod();
										map = new HashMap<String, Object>();
										map.put("list", list);
										msgBody.setData(map);
										msgBody.setAnchorUid(anchorModel.getUid());
										msgBody.setAnchorName(anchorModel.getNickname());

										String signParams = EncryptUtils.signParams(VarConfigUtils.ServiceSecret,
												"appKey=" + VarConfigUtils.ServiceKey,
												"msgBody=" + JSONObject.toJSONString(msgBody));

										Unirest.post(UrlConfigLib.getUrl("url").getAdminrpc_publish_room_all())
												.field("appKey", VarConfigUtils.ServiceKey)
												.field("msgBody", JSONObject.toJSONString(msgBody))
												.field("sign", signParams).asJsonAsync();
										if (cur_anchor_zhouxin == 0) {
											// 获得第一名
											String dayAlternate = UserRedisService.getInstance().getZhouxinAlternate(
													userBaseInfoModel.getUid().toString(), "user", week,
													String.valueOf(gid));
											if (StringUtils.isNotEmpty(dayAlternate)
													&& dayAlternate.equals(String.valueOf(dstUid))) {

												map = new HashMap<String, Object>();
												map.put("name", userModel.getNickname());
												map.put("color", "#fff08c");
												list.add(map);

												map = new HashMap<String, Object>();
												map.put("name", " 与 ");
												map.put("color", "");
												list.add(map);

												map = new HashMap<String, Object>();
												map.put("name", userBaseInfoModel.getNickname());
												map.put("color", "#fff08c");
												list.add(map);

												map = new HashMap<String, Object>();
												map.put("name", " 激烈争逐周星 ");
												map.put("color", "");
												list.add(map);

												map = new HashMap<String, Object>();
												map.put("gid", gid);
												map.put("color", "");
												list.add(map);

												map = new HashMap<String, Object>();
												map.put("name", userModel.getNickname());
												map.put("color", "#fff08c");
												list.add(map);

												map = new HashMap<String, Object>();
												map.put("name", " 暂居第一，谁可以拔得头筹？");
												map.put("color", "");
												list.add(map);

												msgBody = new RunWayCMod();
												map = new HashMap<String, Object>();
												map.put("list", list);
												msgBody.setData(map);
												msgBody.setAnchorUid(anchorModel.getUid());
												msgBody.setAnchorName(anchorModel.getNickname());

												signParams = EncryptUtils.signParams(VarConfigUtils.ServiceSecret,
														"appKey=" + VarConfigUtils.ServiceKey,
														"msgBody=" + JSONObject.toJSONString(msgBody));

												Unirest.post(UrlConfigLib.getUrl("url").getAdminrpc_publish_room_all())
														.field("appKey", VarConfigUtils.ServiceKey)
														.field("msgBody", JSONObject.toJSONString(msgBody))
														.field("sign", signParams).asJsonAsync();
											}
											UserRedisService.getInstance().setZhouxinAlternate(String.valueOf(srcUid),
													userBaseInfoModel.getUid().toString(), "user", week,
													String.valueOf(gid));
										}
										break;
									}
								}
							}
						}
						if (cur_anchor_zhouxin != null && cur_anchor_zhouxin <= 9) {
							// 播主周星排名进入前10
							if (pre_anchor_zhouxin == null || cur_anchor_zhouxin < pre_anchor_zhouxin) {
								Set<String> userPosition = UserRedisService.getInstance().getZhouxinRankUidPosition(
										"anchor", cur_anchor_zhouxin.intValue() + 1, cur_anchor_zhouxin.intValue() + 1,
										week, String.valueOf(gid));
								if (userPosition != null) {
									for (String suid : userPosition) {
										UserBaseInfoModel userBaseInfoModel = userService
												.getUserbaseInfoByUid(Integer.valueOf(suid), false);
										if (userBaseInfoModel != null) {

											List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
											HashMap<String, Object> map = new HashMap<String, Object>();

											// 跑道数据组装
											map.put("name", anchorModel.getNickname());
											map.put("color", "#fff08c");
											list.add(map);

											map = new HashMap<String, Object>();
											map.put("name", " 击败了 ");
											map.put("color", "");
											list.add(map);

											map = new HashMap<String, Object>();
											map.put("name", userBaseInfoModel.getNickname());
											map.put("color", "#fff08c");
											list.add(map);

											map = new HashMap<String, Object>();
											map.put("name", " 周星 ");
											map.put("color", "");
											list.add(map);

											map = new HashMap<String, Object>();
											map.put("gid", gid);
											map.put("color", "");
											list.add(map);

											map = new HashMap<String, Object>();
											map.put("name", " 上升到第 ");
											map.put("color", "");
											list.add(map);

											map = new HashMap<String, Object>();
											map.put("name", cur_anchor_zhouxin + 1);
											map.put("color", "#ffff00");
											list.add(map);

											if (cur_anchor_zhouxin >= 5) {
												map = new HashMap<String, Object>();
												map.put("name", " 名，再接再厉！ ");
												map.put("color", "");
												list.add(map);
											} else {
												map = new HashMap<String, Object>();
												map.put("name", " 名，这个是我的！ ");
												map.put("color", "");
												list.add(map);
											}

											// 跑道

											RunWayCMod msgBody = new RunWayCMod();
											map = new HashMap<String, Object>();
											map.put("list", list);
											msgBody.setData(map);
											msgBody.setAnchorUid(anchorModel.getUid());
											msgBody.setAnchorName(anchorModel.getNickname());

											String signParams = EncryptUtils.signParams(VarConfigUtils.ServiceSecret,
													"appKey=" + VarConfigUtils.ServiceKey,
													"msgBody=" + JSONObject.toJSONString(msgBody));

											Unirest.post(UrlConfigLib.getUrl("url").getAdminrpc_publish_room_all())
													.field("appKey", VarConfigUtils.ServiceKey)
													.field("msgBody", JSONObject.toJSONString(msgBody))
													.field("sign", signParams).asJsonAsync();

											if (cur_anchor_zhouxin == 0) {
												// 获得第一名
												String dayAlternate = UserRedisService.getInstance()
														.getZhouxinAlternate(userBaseInfoModel.getUid().toString(),
																"anchor", week, String.valueOf(gid));
												if (StringUtils.isNotEmpty(dayAlternate)
														&& dayAlternate.equals(String.valueOf(dstUid))) {

													map = new HashMap<String, Object>();
													map.put("name", anchorModel.getNickname());
													map.put("color", "#fff08c");
													list.add(map);

													map = new HashMap<String, Object>();
													map.put("name", " 与 ");
													map.put("color", "");
													list.add(map);

													map = new HashMap<String, Object>();
													map.put("name", userBaseInfoModel.getNickname());
													map.put("color", "#fff08c");
													list.add(map);

													map = new HashMap<String, Object>();
													map.put("name", " 激烈争逐周星 ");
													map.put("color", "");
													list.add(map);

													map = new HashMap<String, Object>();
													map.put("gid", gid);
													map.put("color", "");
													list.add(map);

													map = new HashMap<String, Object>();
													map.put("name", anchorModel.getNickname());
													map.put("color", "#fff08c");
													list.add(map);

													map = new HashMap<String, Object>();
													map.put("name", " 暂居第一，谁可以拔得头筹？");
													map.put("color", "");
													list.add(map);

													msgBody = new RunWayCMod();
													map = new HashMap<String, Object>();
													map.put("list", list);
													msgBody.setData(map);
													msgBody.setAnchorUid(anchorModel.getUid());
													msgBody.setAnchorName(anchorModel.getNickname());

													signParams = EncryptUtils.signParams(VarConfigUtils.ServiceSecret,
															"appKey=" + VarConfigUtils.ServiceKey,
															"msgBody=" + JSONObject.toJSONString(msgBody));

													Unirest.post(
															UrlConfigLib.getUrl("url").getAdminrpc_publish_room_all())
															.field("appKey", VarConfigUtils.ServiceKey)
															.field("msgBody", JSONObject.toJSONString(msgBody))
															.field("sign", signParams).asJsonAsync();
												}
												UserRedisService.getInstance().setZhouxinAlternate(
														String.valueOf(dstUid), userBaseInfoModel.getUid().toString(),
														"anchor", week, String.valueOf(gid));
											}
										}
										break;
									}
								}
							}
						}
					}

				}
			}

		}

		@Override
		public String getName() {
			return "UpdateUserLevelAsyncTask";
		}

		@Override
		public void afterOk() {
		}

		@Override
		public void afterError(Exception e) {
		}
	}

	/**
	 * 升级通知房间广播
	 * 
	 * @param userLevel
	 *            等级
	 * @param uid
	 * @param roomOwnerUid
	 *            主播id==房间id
	 * @param type
	 *            见LevelUpCMod定义 0用户等级 1主播等级
	 */
	void rpcAdminUserUpLevel(int level, int uid, int roomOwnerUid, String nick, String avatar, Boolean sex, int type) {

		LevelUpCMod msgBody = new LevelUpCMod();
		msgBody.setLevel(level);
		msgBody.setUid(uid);
		msgBody.setType(type);
		msgBody.setSex(sex);
		msgBody.setNickname(nick);
		msgBody.setAvatar(avatar);

		try {
			String signParams = EncryptUtils.signParams(VarConfigUtils.ServiceSecret,
					"appKey=" + VarConfigUtils.ServiceKey, "msgBody=" + JSONObject.toJSONString(msgBody),
					"roomOwner=" + String.valueOf(roomOwnerUid));

			Unirest.post(UrlConfigLib.getUrl("url").getAdminrpc_publish_room())
					.field("appKey", VarConfigUtils.ServiceKey).field("msgBody", JSONObject.toJSONString(msgBody))
					.field("roomOwner", String.valueOf(roomOwnerUid)).field("sign", signParams).asJson();
		} catch (Exception e) {
			logger.error("rpcAdminUserUpLevel->Exception:" + e.getMessage());
		}
	}

	/**
	 * 
	 * 用户进房通知Amdin，房间广播进房
	 */
	void rpcAdminUserEnterRoom(int level, int uid, int roomOwnerUid, String nick, String avatar, Boolean sex, UserGuardInfoModel userGuardInfoModel, UserVipInfoModel userVipInfoModel, Integer sort) {

		List<Integer> badgeList = userItemService.getBadgeListByUid(uid, false);

		EnterRoomCMod msgBody = new EnterRoomCMod();
		msgBody.setLevel(level);
		msgBody.setUid(uid);
		msgBody.setSex(sex);
		msgBody.setNickname(nick);
		msgBody.setAvatar(avatar);
		msgBody.setManage(RelationRedisService.getInstance().checkManageByAnchorUser(roomOwnerUid, uid) ? 1 : 0);

		msgBody.setBadges(badgeList);
		
		//增加守护相关
		long nowtime = System.currentTimeMillis()/1000;
		int carId = 0;
		if(userGuardInfoModel != null){ //如果是守护则优先显示守护相关权限
			msgBody.setType(2);
			Map<String, String> map = new HashMap<String, String>();
			Integer icon = 0;
			ValueaddPrivilegeModel privilegeModel = ValueaddServiceUtil.getPrivilege(userGuardInfoModel.getGid(), userGuardInfoModel.getLevel());
			if(nowtime >userGuardInfoModel.getEndtime() && nowtime <=userGuardInfoModel.getCushiontime()){ //保护期的座驾 和icon
				icon = privilegeModel.getCushionIcon();
				carId = privilegeModel.getCushionCarid();
				msgBody.setJoinPlace(1);
			}else{
				icon = privilegeModel.getIconId();
				carId = privilegeModel.getCarId();
				msgBody.setJoinPlace(2);
			}
			Long days = DateUtils.getDaysBetweenTime(nowtime, new Long(userGuardInfoModel.getEndtime()));
			map.put("surplusDays", days.toString());
			map.put("guardIcon", icon.toString()); //进场icon
			map.put("guardLevel", userGuardInfoModel.getLevel().toString());
			map.put("joinEffects", privilegeModel.getJoinEffects()); //进场特效 聊天区上方的文字颜色
			msgBody.setGuardInfo(map);
			
		}else{
			if(userVipInfoModel != null){ //如果有VIP权限则使用 VIP相关座驾
				msgBody.setType(3);
				Map<String, String> map = new HashMap<String, String>();
				ValueaddPrivilegeModel privilegeModel = ValueaddServiceUtil.getPrivilege(userVipInfoModel.getGid(), 1);
				map.put("vipIcon", privilegeModel.getIconId().toString());
				msgBody.setVipInfo(map);
				msgBody.setJoinPlace(1);
				carId = privilegeModel.getCarId();
			}else{
				msgBody.setType(1);	
				msgBody.setJoinPlace(1);
			}
			
		}
		UserCarInfoModel carInfo = ValueaddServiceUtil.getCarInfo(uid);
		if(carInfo != null){
			carId = carInfo.getGid();
		}
		msgBody.setCarId(carId);
		msgBody.setSort(sort);
		String signParams = EncryptUtils.signParams(VarConfigUtils.ServiceSecret, "appKey=" + VarConfigUtils.ServiceKey,
				"msgBody=" + JSONObject.toJSONString(msgBody), "roomOwner=" + String.valueOf(roomOwnerUid));

		Unirest.post(UrlConfigLib.getUrl("url").getAdminrpc_publish_room()).field("appKey", VarConfigUtils.ServiceKey)
				.field("msgBody", JSONObject.toJSONString(msgBody)).field("roomOwner", String.valueOf(roomOwnerUid))
				.field("sign", signParams).asJsonAsync();
	}
	

	/**
	 * 获取开播列表
	 * 
	 * @param page
	 */
	@Override
	public void getLiveingList(int os, int page, ReturnModel returnModel, Integer srcuid) {
		List<Map<String, Object>> list = this.livingList(os, page, 2, srcuid);
		if (srcuid == null) {
			if (list == null) {
				list = new ArrayList<Map<String, Object>>();
				getWeekStar(30, list);
			}else if (list.size() < 30) {
				getWeekStar(30, list);
			}
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("list", list);
		returnModel.setData(map);
	}
	
	/**
	 * 获取手机列表
	 * 
	 * @param page
	 */
	@Override
	public void getMobileList( int page, ReturnModel returnModel, Integer srcuid) {
		List<Map<String, Object>> list = this.mobileLivingList(page, srcuid);
		if (srcuid == null) {
			if(list != null){
				if(list.size()<5){
					getWeekStar(5-list.size(), list);	
				}
			}else{
				list = new ArrayList<Map<String, Object>>();
				getWeekStar(5, list);
			}
			
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("list", list);
		returnModel.setData(map);
	}
	
	/**
	 * 获取pc端 首页热门主播列表
	 * 
	 * @param page
	 */
	@Override
	public void getNewJoinLiveingList(int os, int page, ReturnModel returnModel, Integer srcuid) {
		int pageForCount = 6;
		int countForPage = BaseContant.newJoinSize/pageForCount;
		page = page%countForPage;
		if(page==0){
			page = countForPage;
		}
		List<Map<String, Object>> list = this.livingList(os, 0, 9, srcuid);
		if(list.size() >= countForPage){
			if(pageForCount*page>list.size()){
				list = list.subList(0, pageForCount);
			}else{
				list = list.subList((page - 1) * pageForCount, page * pageForCount);	
			}
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("list", list);
		returnModel.setData(map);
	}
	
	/**
	 * 获取pc端 首页热门主播列表
	 * 
	 * @param page
	 */
	@Override
	public void getHomeRecLiveingList(int os, int page, ReturnModel returnModel, Integer srcuid) {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>(); 
		list = this.livingList(os, page, 2, srcuid);
		if (list.size() >= 9) {
			list = list.subList(0, 9);
			
//			for (Map<String,Object> map:list) {
//				try {
//					Long time = System.currentTimeMillis()/1000 - Long.parseLong(map.get("opentime").toString());
//					map.put("opentime",time );
//				} catch (Exception e) {
//					logger.error("getHomeRecLiveingList:",e);
//					logger.debug("getHomeRecLiveingList class:"+ map.get("opentime").getClass()+"  value:" + map.get("opentime"));
//					map.put("opentime", System.currentTimeMillis()/1000 - ((Long)map.get("opentime")).longValue());
//				}
//				
//				
//			}
		}else if (list.size() < 9) {
			
//			for (Map<String,Object> map:list) {
//				try {
//					Long time = System.currentTimeMillis()/1000 - Long.parseLong(map.get("opentime").toString());
//					map.put("opentime",time );
//				} catch (Exception e) {
//					logger.error("getHomeRecLiveingList:",e);
//					logger.debug("getHomeRecLiveingList class:"+ map.get("opentime").getClass()+"  value:" + map.get("opentime"));
//					map.put("opentime", System.currentTimeMillis()/1000 -Long.parseLong(map.get("opentime").toString()));
//				}
//			}
			int less = 9-list.size();
			// 用日榜补
			Set<Tuple> userDayRank = UserRedisService.getInstance().getUserDayRank("anchor", 9);
			
			boolean bl = false;
			if (userDayRank != null && userDayRank.size() > 0) {
				for(Tuple tuple:userDayRank){
					for(Map<String, Object> map:list){
						if (map.get("uid").toString().equals(tuple.getElement())) {
							// 已存在
							bl = true;
							break;
						}
					}
					if (!bl) {
							Map<String, Object> recommandStation = this.getRecommandStation(Integer.valueOf(tuple.getElement()));
							if (recommandStation != null && recommandStation.size() > 0) {
								list.add(recommandStation);
								less--;
							}
					}
					bl = false;
					if (less == 0) {
						// 已塞满
						break;
					}
				}
			}
			if (less > 0) {
				// 用周榜补
				Set<Tuple> userWeekRank = UserRedisService.getInstance().getUserWeekRank("anchor",24);
				if (userWeekRank != null && userWeekRank.size() > 0) {
					
					for(Tuple tuple:userWeekRank){
						
						for(Map<String, Object> map:list){
							if (map.get("uid").toString().equals(tuple.getElement())) {
								// 已存在
								bl = true;
								break;
							}
						}
						if (!bl) {
								Map<String, Object> recommandStation = this.getRecommandStation(Integer.valueOf(tuple.getElement()));
								if (recommandStation != null && recommandStation.size() > 0) {
									list.add(recommandStation);
									less--;
								}
						}
						bl = false;
						if (less == 0) {
							// 已塞满
							break;
						}
					}
				}
			}
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("list", list);
		returnModel.setData(map);
	}
	
	/**
	 * 获取PC端 首页上广场主播
	 * 
	 * @param page
	 */
	@Override
	public void getSquareLiveList(int os, int page, ReturnModel returnModel, Integer srcuid) {
		
		List<Map<String, Object>> liveList = new ArrayList<Map<String, Object>>(); 
		liveList = this.livingList(os, 0, 1, srcuid);
		int pageCount = 25;
		int less = 0 ;
		if(page ==1){
			if(liveList.size()<pageCount){
				less = pageCount-liveList.size();		
			}else{
				liveList = liveList.subList((page - 1) * pageCount, page * pageCount);
			}
		}else{
			if(liveList.size() > page * pageCount){
				liveList = liveList.subList((page - 1) * pageCount, page * pageCount);	
			}else{
				if((page - 1) * pageCount < liveList.size()){
					liveList = liveList.subList((page - 1) * pageCount, liveList.size());	
				}else{
					liveList = new ArrayList<>();
				}
			}
			
		}
		
		if (less > 0) {
			// 用日榜补
			boolean bl = false;
			Set<Tuple> userDayRank = UserRedisService.getInstance().getUserDayRank("anchor", 30);
			if (userDayRank != null && userDayRank.size() > 0) {
				
				for(Tuple tuple:userDayRank){
					for(Map<String, Object> map:liveList){
						if (map.get("uid").toString().equals(tuple.getElement())) {
							// 已存在
							bl = true;
							break;
						}
					}
					if (!bl) {
							Map<String, Object> recommandStation = this.getRecommandStation(Integer.valueOf(tuple.getElement()));
							if (recommandStation != null && recommandStation.size() > 0) {
								liveList.add(recommandStation);
								less--;
							}
					}
					if (less == 0) {
						// 已塞满
						break;
					}
					bl = false;
				}
			}
			if (less > 0) {
				bl = false;
				// 用周榜补
				Set<Tuple> userWeekRank = UserRedisService.getInstance().getUserWeekRank("anchor",24);
				if (userWeekRank != null && userWeekRank.size() > 0) {
					
					for(Tuple tuple:userWeekRank){
						for(Map<String, Object> map:liveList){
							if (map.get("uid").toString().equals(tuple.getElement())) {
								// 已存在
								bl = true;
								break;
							}
						}
						if (!bl) {
								Map<String, Object> recommandStation = this.getRecommandStation(Integer.valueOf(tuple.getElement()));
								if (recommandStation != null && recommandStation.size() > 0) {
									liveList.add(recommandStation);
									less--;
								}
						}
						if (less == 0) {
							// 已塞满
							break;
						}

						bl = false;
					}
				}
			}
		}

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("list", liveList);
		returnModel.setData(map);
	}
	
	/**
	 * 获取补位主播(未开播的)
	 * @param uid
	 * @return
	 */
	private Map<String, Object> getRecommandStation(int uid){
		
		Map<String, Object> map = new HashMap<String, Object>();
		UserBaseInfoModel userBaseInfoModel = userService.getUserbaseInfoByUid(uid, false);
		
		if (userBaseInfoModel == null) {
			return map;
		} else {
			if(userBaseInfoModel.getRecommend()==0){
				return map;
			}
			Map<String, String> hgetAll = RedisCommService.getInstance().hgetAll(RedisContant.RedisNameUser,RedisContant.WeekTitle);
			if (hgetAll != null && hgetAll.containsValue(String.valueOf(uid))) {
				map.put("title", "周星达人");
			}else {
				map.put("title", "");
			}
			map.put("status", false);
			map.put("uid", uid);
			map.put("nickname", userBaseInfoModel.getNickname().trim());
			map.put("headimage", userBaseInfoModel.getHeadimage());
			map.put("anchorLevel", userBaseInfoModel.getAnchorLevel());
			map.put("slogan", userBaseInfoModel.getSignature());
			map.put("city", userBaseInfoModel.getCity());
			map.put("enters", this.getRoomShowUsers(uid, userBaseInfoModel.getContrRq()));
			map.put("mobileliveimg", userBaseInfoModel.getLivimage());
			map.put("sex", userBaseInfoModel.getSex());
			map.put("domain", "");
			map.put("opentime", 0);
			map.put("pcimg1", userBaseInfoModel.getPcimg1());
			map.put("pcimg2", userBaseInfoModel.getPcimg2());
			map.put("verified", userBaseInfoModel.isVerified());
		}
		
		return map;
	}

	/**
	 * 获取最新列表
	 * 
	 * @param page
	 * @param returnModel
	 */
	@Override
	public void getHostList(int os, int page, ReturnModel returnModel, Integer srcuid) {
		List<Map<String, Object>> list = this.livingList(os, page, 1, srcuid);
		if (srcuid == null) {
			// 未登录
			if (list == null) {
				list = new ArrayList<Map<String, Object>>();
				getWeekStar(40, list);
			}else if (list.size() < 40) {
				getWeekStar(40, list);
			}
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("list", list);
		returnModel.setData(map);
	}

	/**
	 * 未登录 不足主播数
	 * @param count
	 * @param list
	 */
	private void getWeekStar(int count, List<Map<String, Object>> list){
		
		Set<Tuple> userWeekRank = UserRedisService.getInstance().getUserWeekRank("anchor",count -1);
		
		for(Tuple tuple:userWeekRank){
			boolean blExist = false;
			for(Map<String, Object> map : list){
				if (map.get("uid").toString().equals(tuple.getElement())) {
					blExist = true;
					break;
				}
			}
			if (!blExist) {
				// 不存在
				UserBaseInfoModel userBaseInfoModel = userService.getUserbaseInfoByUid(Integer.valueOf(tuple.getElement()), false);
				if (userBaseInfoModel == null) {
					continue;
				}
				Map<String, Object> mapAdd = new HashMap<String,Object>();
				
				mapAdd.put("status", userBaseInfoModel.getLiveStatus());
				mapAdd.put("uid", tuple.getElement());
				mapAdd.put("nickname", userBaseInfoModel.getNickname().trim());
				mapAdd.put("headimage", userBaseInfoModel.getHeadimage());
				mapAdd.put("anchorLevel", userBaseInfoModel.getAnchorLevel());
				mapAdd.put("slogan", userBaseInfoModel.getSignature());
				mapAdd.put("city", userBaseInfoModel.getCity());
				mapAdd.put("enters", this.getRoomShowUsers(Integer.valueOf(tuple.getElement()), userBaseInfoModel.getContrRq()));
				mapAdd.put("mobileliveimg", userBaseInfoModel.getLivimage());
				mapAdd.put("opentime", userBaseInfoModel.getOpentime());
				mapAdd.put("sex", userBaseInfoModel.getSex());
				mapAdd.put("pcimg1", userBaseInfoModel.getPcimg1());
				mapAdd.put("pcimg2", userBaseInfoModel.getPcimg2());
				String stream = configService.getThirdStream(Integer.valueOf(tuple.getElement()));
				int thirdstream = 0;
				if (null == stream) {
					mapAdd.put("domain",
							liveService.getVideoConfig(0, Integer.valueOf(tuple.getElement()), userBaseInfoModel.getVideoline()).get("domain"));
				} else {
					thirdstream = 1;
					mapAdd.put("domain", stream);
				}
				mapAdd.put("thirdstream", thirdstream);
				

				mapAdd.put("verified", userBaseInfoModel.isVerified());
				list.add(mapAdd);
				if (list.size() >= count) {
					break;
				}
			}
		}
	}

	/**
	 * 获取开播列表
	 * 
	 * @param page
	 * @param recommend
	 *            ＝0普通开播 ＝1最新开播 ＝2热门开播
	 * @return
	 */
	public List<Map<String, Object>> livingList(int os, int page, int recommend, Integer useruid) {
		if (null == useruid) {
			return LivingListConfigLib.getLivingList(os, recommend, page);
		}

		Set<String> setLivingList = null;
		if (recommend == 0) {
			setLivingList = OtherRedisService.getInstance().getBaseRoom(page);
		} else if (recommend == 1) {
			setLivingList = OtherRedisService.getInstance().getHotRoom(page);
		} else if (recommend == 2) {
			setLivingList = OtherRedisService.getInstance().getRecommendRoom(page);
		}else if (recommend == 9){
			return LivingListConfigLib.getLivingList(os, recommend, page);
		}

		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>(); 
		list = getUserData(setLivingList, useruid, os);
		return list;
	}
	
	/**
	 * 获取手机开播列表
	 * 
	 * @param page
	 * @param recommend
	 *           
	 * @return
	 */
	public List<Map<String, Object>> mobileLivingList(int page, Integer useruid) {
		return LivingListConfigLib.getMobileLivingList(page);
	}

	
	public List<Map<String, Object>> getUserData(Set<String> setLivingList, Integer useruid, int os){
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

		if (setLivingList != null && setLivingList.size() > 0) {

			Map<String, UserBaseInfoModel> userbaseMap = userService.getUserbaseInfoByUid(setLivingList.toArray(new String[0]));
			Map<String, LiveMicTimeModel> livingMap = liveService.getLiveIngByUid(setLivingList.toArray(new String[0]));

			if (userbaseMap == null || livingMap == null) {
				return list;
			}

			UserBaseInfoModel userBaseInfoModel;
			LiveMicTimeModel liveMicTimeModel;

			// 获取上周周星
			Map<String, String> hgetAll = RedisCommService.getInstance().hgetAll(RedisContant.RedisNameUser,RedisContant.WeekTitle);
			
			for (String suid : setLivingList) {

				Map<String, Object> map = new HashMap<String, Object>();
				int uid = Integer.valueOf(suid);
				userBaseInfoModel = userbaseMap.get(suid);
				liveMicTimeModel = livingMap.get(suid);

				if (userBaseInfoModel == null || liveMicTimeModel == null || liveMicTimeModel.getType()
						|| !userBaseInfoModel.getLiveStatus()) {
					continue;
				} else {

					// for search...
					if (null != useruid) {
						Boolean isAttention = RelationRedisService.getInstance().isFan(useruid, uid);
						map.put("isAttention", isAttention);
						Boolean isLiving = userBaseInfoModel.getLiveStatus();
						map.put("living", isLiving);
						map.put("avatar", userBaseInfoModel.getHeadimage());
						map.put("userLevel", userBaseInfoModel.getUserLevel());
					}

					if (os > 1) {
						map.put("status", 1);
					}
					if (hgetAll != null && hgetAll.containsValue(suid)) {
						map.put("title", "周星达人");
					}else {
						map.put("title", "");
					}
					map.put("uid", suid);
					map.put("nickname", userBaseInfoModel.getNickname().trim());
					map.put("headimage", userBaseInfoModel.getHeadimage());
					map.put("anchorLevel", userBaseInfoModel.getAnchorLevel());
					map.put("slogan", liveMicTimeModel.getSlogan().trim());
					map.put("city", StringUtils.isEmpty(liveMicTimeModel.getCity().trim()) ? VarConfigUtils.Location
							: liveMicTimeModel.getCity());
					map.put("enters", this.getRoomShowUsers(uid, userBaseInfoModel.getContrRq()));
					map.put("mobileliveimg", userBaseInfoModel.getLivimage());
					map.put("sex", userBaseInfoModel.getSex());
					map.put("opentime",System.currentTimeMillis()/1000-userBaseInfoModel.getOpentime());
					map.put("pcimg1", userBaseInfoModel.getPcimg1());
					map.put("pcimg2", userBaseInfoModel.getPcimg2());
					map.put("gameId", userBaseInfoModel.getGameId());
					map.put("gameStatus", userBaseInfoModel.getGameStatus());
					map.put("gameIconUrl", userBaseInfoModel.getGameIconUrl());
					String stream = configService.getThirdStream(uid);
					if (null == stream) {
						map.put("domain",
								liveService.getVideoConfig(0, uid, userBaseInfoModel.getVideoline()).get("domain"));
					} else {
						map.put("domain", stream);
					}
					map.put("verified", userBaseInfoModel.isVerified());
					//PK标识
					map.put("pkStatus", userBaseInfoModel.getLianmaiStatus()==null?0:userBaseInfoModel.getLianmaiStatus());
				}
				if (map.size() > 0) {
					list.add(map);
				}
			}
		}
		return list;
	}
	/**
	 * 获取大家都在搜 即 推荐列表
	 */
	@Override
	public void getReCommend(int os, int uid, ReturnModel returnModel) {
		Map<String, Object> map = new HashMap<String, Object>();
		// map.put("recommend", this.recommendList(uid, 0));
		map.put("recommend", this.livingList(os, 0, 2, uid));
		returnModel.setData(map);
	}

	// public List<Map<String, Object>> recommendList(int useruid, int page) {
	// Set<String> setLivingList =
	// OtherRedisService.getInstance().getRecommendRoom(page);
	// List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
	//
	// if (setLivingList != null && setLivingList.size() > 0) {
	//
	// Map<String, UserBaseInfoModel> userbaseMap = userService
	// .getUserbaseInfoByUid(setLivingList.toArray(new String[0]));
	// Map<String, LiveMicTimeModel> livingMap =
	// liveService.getLiveIngByUid(setLivingList.toArray(new String[0]));
	//
	// if (userbaseMap == null || livingMap == null) {
	// return list;
	// }
	//
	// for (String suid : setLivingList) {
	// Map<String, Object> map = new HashMap<String, Object>();
	// int uid = Integer.valueOf(suid);
	// UserBaseInfoModel userBaseInfoModel = userbaseMap.get(suid);
	// LiveMicTimeModel liveMicTimeModel = livingMap.get(suid);
	//
	// if (userBaseInfoModel == null || liveMicTimeModel == null ||
	// liveMicTimeModel.getType()) {
	// continue;
	// } else {
	//
	// Boolean isAttention = RelationRedisService.getInstance().isFan(useruid,
	// uid);
	// map.put("isAttention", isAttention);
	// Boolean isLiving = userBaseInfoModel.getLiveStatus();
	// map.put("living", isLiving);
	//
	// map.put("uid", suid);
	// map.put("nickname", userBaseInfoModel.getNickname());
	// map.put("avatar", userBaseInfoModel.getHeadimage()); // same
	// // as
	// // search
	// map.put("anchorLevel", userBaseInfoModel.getAnchorLevel());
	// map.put("slogan", liveMicTimeModel.getSlogan());
	// map.put("city",
	// liveMicTimeModel.getCity() == "" ? VarConfigUtils.Location :
	// liveMicTimeModel.getCity());
	// map.put("enters",
	// RelationRedisService.getInstance().getEnterRoomTimes(uid));
	// map.put("mobileliveimg", userBaseInfoModel.getLivimage());
	// map.put("sex", userBaseInfoModel.getSex());
	//
	// // map.put("domain",
	// // liveService.getVideoConfig(0, uid,
	// // userBaseInfoModel.getVideoline()).get("domain"));
	// String stream = configService.getThirdStream(uid);
	// if (null == stream) {
	// map.put("domain",
	// liveService.getVideoConfig(0, uid,
	// userBaseInfoModel.getVideoline()).get("domain"));
	// } else {
	// map.put("domain", stream);
	// }
	//
	// map.put("verified", userBaseInfoModel.isVerified());
	// map.put("userLevel", userBaseInfoModel.getUserLevel());
	// }
	// if (map.size() > 0) {
	// list.add(map);
	// }
	// }
	// }
	// return list;
	// }

	/**
	 * 首页关注列表
	 * 
	 * @param uid
	 */
	@Override
	public void getFollowsHome(Integer uid, ReturnModel returnModel) {

		UserBaseInfoModel userBaseInfoModel = null;
		LiveMicTimeModel liveMicTimeModel = null;
		UserAccountModel userAccountModel = null;

		Map<String, Object> map = new HashMap<String, Object>();
		List<Map<String, Object>> livingList = new ArrayList<Map<String, Object>>();
		// List<Map<String, Object>> livedList = new ArrayList<Map<String,
		// Object>>();

		Set<String> set = RelationRedisService.getInstance().getFollows(uid);


		if (set != null && set.size() > 0) {
			// Object[] array = set.toArray();

			// tosy change
			Map<String, UserBaseInfoModel> userbaseMap = userService.getUserbaseInfoByUid(set.toArray(new String[0]));
			Map<String, LiveMicTimeModel> livingMap = liveService.getLiveIngByUid(set.toArray(new String[0]));
			Map<String, UserAccountModel> accountMap = userService.getUserAccountByUid(set.toArray(new String[0]));
			if (null == userbaseMap || null == accountMap) {
				returnModel.setData(map);
				return;
			}

			int pages = set.size();
			
			for (String strUid : set) {
				int suid = Integer.valueOf(strUid);

				map = new HashMap<String, Object>();

				userBaseInfoModel = userbaseMap.get(strUid);

				if (null != livingMap) {
					liveMicTimeModel = livingMap.get(strUid);
				} else {
					liveMicTimeModel = null;
				} 

				userAccountModel = accountMap.get(strUid);

				if (null == userBaseInfoModel || null == userAccountModel || userAccountModel.getStatus() == 0) {
					continue;
				} else {
					map.put("uid", strUid);
					map.put("headimage", userBaseInfoModel.getHeadimage());
					map.put("anchorLevel", userBaseInfoModel.getAnchorLevel());
					map.put("userLevel", userBaseInfoModel.getUserLevel());
					map.put("nickname", userBaseInfoModel.getNickname());
					map.put("sex", userBaseInfoModel.getSex());
					map.put("verified", userBaseInfoModel.isVerified());

					if (userBaseInfoModel.getLiveStatus() && liveMicTimeModel != null) {
						// 正在开播
						map.put("city", liveMicTimeModel.getCity() == "" ? VarConfigUtils.Location
								: liveMicTimeModel.getCity());
						map.put("mobileliveimg", userBaseInfoModel.getLivimage());
						map.put("enters", this.getRoomShowUsers(suid, userBaseInfoModel.getContrRq()));

						map.put("gameId", userBaseInfoModel.getGameId());
						map.put("gameStatus", userBaseInfoModel.getGameStatus());
						map.put("gameIconUrl", userBaseInfoModel.getGameIconUrl());
						map.put("pkStatus", userBaseInfoModel.getLianmaiStatus()==null?0:userBaseInfoModel.getLianmaiStatus());
						// map.put("domain",
						// liveService.getVideoConfig(uid, suid,
						// userBaseInfoModel.getVideoline()).get("domain"));
						// TOSY 第三方流
						String stream = configService.getThirdStream(suid);
						if (null == stream) {
							map.put("domain", liveService.getVideoConfig(uid, suid, userBaseInfoModel.getVideoline())
									.get("domain"));
						} else {
							map.put("domain", stream);
						}

						map.put("slogan", liveMicTimeModel.getSlogan());
						livingList.add(map);
						pages++;
					}
				}
			}
			map = new HashMap<String, Object>();
			map.put("living", livingList);
			map.put("lived", new ArrayList<String>());
			map.put("recommend", new ArrayList<String>());
			map.put("pages", 1);

		} else {
			map.put("living", new ArrayList<String>());
			map.put("lived", new ArrayList<String>());
			map.put("recommend", new ArrayList<String>());

		}
		returnModel.setData(map);
	}

	@Override
	public List<Map<String, Object>> getNotice() {

		return null;
	}

	/**
	 * 弹幕功能
	 * 
	 * @param srcUid
	 *            送礼人uid
	 * @param dstUid
	 *            房间uid
	 * @param gid
	 *            礼物id
	 */
	@Override
	public void sendDanmaku(Integer srcUid, Integer dstUid, int gid, Byte os, String msg, ReturnModel returnModel) {

		int count = 1;
		if (OtherRedisService.getInstance().getSilent(dstUid, srcUid)) {
			returnModel.setCode(CodeContant.liveSilenting);
			returnModel.setMessage("你已被管理员禁言");
			return;
		}
		if(gid != 1){
			returnModel.setCode(CodeContant.GIFTCONFIGEXIST);
			returnModel.setMessage("礼物不存在");
			return;
		}
		ConfigGiftModel giftConfigModel = configService.getGiftInfoByGidNew(gid);
		if (giftConfigModel == null) {
			returnModel.setCode(CodeContant.GIFTCONFIGEXIST);
			returnModel.setMessage("礼物不存在");
			return;
		}
		String gname = giftConfigModel.getGname();
		int price = giftConfigModel.getGprice();
		int wealth = giftConfigModel.getWealth();

		UserAssetModel sendUserAssetModel = userService.getUserAssetByUid(srcUid, false);
		UserBaseInfoModel userBaseinfo = userService.getUserbaseInfoByUid(srcUid, false);
		
		UserBaseInfoModel anchorModel = userService.getUserbaseInfoByUid(dstUid, false);

		if (sendUserAssetModel == null || userBaseinfo == null) {
			returnModel.setCode(CodeContant.USERASSETEXITS);
			returnModel.setMessage("用户不存在");
			return;
		}
		if (price * count > sendUserAssetModel.getMoney()) {
			returnModel.setCode(CodeContant.MONEYLESS);
			returnModel.setMessage("金额不足");
			return;
		}
		// 礼物总价值
		int sendTotal = price * count;
		// 主播获得价值
		double gets = sendTotal;

		// 礼物总价值
		int weakths = wealth * count;
		// 送礼者 扣费
		int res = userService.updUserAssetBySendUid(srcUid, sendTotal, weakths);
		if (res == 0) {
			returnModel.setCode(CodeContant.MONEYDEDUCT);
			returnModel.setMessage("扣费失败");
			return;
		}

		res = billService.insertBill(gid, count, price, srcUid, sendUserAssetModel.getMoney() - sendTotal,
				sendUserAssetModel.getWealth() + sendTotal, sendUserAssetModel.getCredit(), dstUid, 0, 0, 0,
				System.currentTimeMillis() / 1000, 1, gets, os, "", userBaseinfo.getNickname(), anchorModel.getNickname(),anchorModel.getFamilyId());

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("consume", sendTotal);
		map.put("surplus", sendUserAssetModel.getMoney() - sendTotal);
		returnModel.setCode(CodeContant.OK);
		returnModel.setMessage("发送弹幕成功");
		returnModel.setData(map);

		long time = System.currentTimeMillis();
		userTransactionHisService.saveUserTransactionHis(11, srcUid, 0.00, (long)sendTotal, time, "", 1);
		userTransactionHisService.saveUserTransactionHis(10, dstUid, 0.00, (long)sendTotal, time, "", 1);
		
		// TOSY 拼装发送房间广播
		DanmakuCMod msgBody = new DanmakuCMod();
		msgBody.setMsg(msg);
		msgBody.setAvatar(userBaseinfo.getHeadimage());
		msgBody.setNickname(userBaseinfo.getNickname());
		msgBody.setUid(userBaseinfo.getUid());
		msgBody.setLevel(userBaseinfo.getUserLevel());

		String signParams = EncryptUtils.signParams(VarConfigUtils.ServiceSecret, "appKey=" + VarConfigUtils.ServiceKey,
				"msgBody=" + JSONObject.toJSONString(msgBody), "roomOwner=" + dstUid);

		Unirest.post(UrlConfigLib.getUrl("url").getAdminrpc_publish_room()).field("appKey", VarConfigUtils.ServiceKey)
				.field("msgBody", JSONObject.toJSONString(msgBody)).field("roomOwner", dstUid).field("sign", signParams)
				.asJsonAsync();

		try{
			// 异步调用处理用户等级问题
			AsyncManager.getInstance()
					.execute(new UpdateUserLevelAsyncTask(srcUid, 0, dstUid, sendTotal, (int) gets, gid, count, gname, weakths));
		}catch (Exception ex){
			logger.error("UpdateUserLevelAsyncTask>>>",ex);
		}

		// TOSY TASK
		TaskService.getInstance().taskProcess(srcUid, TaskConfigLib.SendDanmaku, 1);

		if (res == 0) {
			logger.info("<sendGift>插入账单失败");
		}
		return;

	}

	/**
	 * 弹幕功能
	 * 
	 * @param srcUid
	 *            送礼人uid
	 * @param dstUid
	 *            收礼人uid
	 * @param gid
	 *            礼物id
	 */
	@Override
	public void sendDanmakuBag(Integer srcUid, Integer dstUid, int gid, Byte os, String msg, ReturnModel returnModel) {

		int count = 1;
		if (OtherRedisService.getInstance().getSilent(dstUid, srcUid)) {
			returnModel.setCode(CodeContant.liveSilenting);
			returnModel.setMessage("你已被管理员禁言");
			return;
		}

		ConfigGiftModel giftConfigModel = configService.getGiftInfoByGidNew(gid);
		if (giftConfigModel == null) {
			returnModel.setCode(CodeContant.GIFTCONFIGEXIST);
			returnModel.setMessage("礼物不存在");
			return;
		}
		String gname = giftConfigModel.getGname();
		int price = giftConfigModel.getGprice();

		UserAssetModel sendUserAssetModel = userService.getUserAssetByUid(srcUid, false);
		UserBaseInfoModel userBaseinfo = userService.getUserbaseInfoByUid(srcUid, false);
		
		UserBaseInfoModel anchorModel = userService.getUserbaseInfoByUid(dstUid, false);

		if (sendUserAssetModel == null || userBaseinfo == null) {
			returnModel.setCode(CodeContant.USERASSETEXITS);
			returnModel.setMessage("用户不存在:" + CodeContant.USERASSETEXITS);
			return;
		}

		Map<Integer, UserItemModel> baglist = userItemService.getItemListByUid(srcUid, false);
		if (!baglist.containsKey(gid)) {
			returnModel.setCode(CodeContant.baggidless);
			returnModel.setMessage("背包礼物不足");
			return;
		} else {
			if (baglist.get(gid).getNum() < count) {
				returnModel.setCode(CodeContant.baggidless);
				returnModel.setMessage("背包礼物不足");
				return;
			}
		}
		// 礼物总价值
		int sendTotal = price * count;
		// 主播获得价值
		double gets = giftConfigModel.getCredit() * count;

		int wealths = giftConfigModel.getWealth() * count;

		// 扣背包礼物
		int res = userItemService.updUserItemBySendUid(srcUid, gid, count);
		if (res == 0) {
			returnModel.setCode(CodeContant.baggiddeduct);
			returnModel.setMessage("送礼失败:" + CodeContant.baggiddeduct);
			return;
		}
		// 送礼者 扣费
		res = userService.updUserAssetBySendUid(srcUid, 0, wealths);
		if (res == 0) {
			returnModel.setCode(CodeContant.MONEYDEDUCT);
			returnModel.setMessage("扣费失败");
			return;
		}

		res = billService.insertBill(gid, count, price, srcUid, sendUserAssetModel.getMoney(),
				sendUserAssetModel.getWealth() + sendTotal, sendUserAssetModel.getCredit(), dstUid, 0, 0, 0,
				System.currentTimeMillis() / 1000, 1, gets, os, "bag", userBaseinfo.getNickname(), anchorModel.getNickname(),anchorModel.getFamilyId());

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("consume", 0);
		map.put("surplus", sendUserAssetModel.getMoney());
		map.put("bagflag", baglist.get(gid).getNum() - count);
		returnModel.setCode(CodeContant.OK);
		returnModel.setMessage("发送弹幕成功");
		returnModel.setData(map);

		// TOSY 拼装发送房间广播
		DanmakuCMod msgBody = new DanmakuCMod();
		msgBody.setMsg(msg);
		msgBody.setAvatar(userBaseinfo.getHeadimage());
		msgBody.setNickname(userBaseinfo.getNickname());
		msgBody.setUid(userBaseinfo.getUid());
		msgBody.setLevel(userBaseinfo.getUserLevel());

		String signParams = EncryptUtils.signParams(VarConfigUtils.ServiceSecret, "appKey=" + VarConfigUtils.ServiceKey,
				"msgBody=" + JSONObject.toJSONString(msgBody), "roomOwner=" + dstUid);

		Unirest.post(UrlConfigLib.getUrl("url").getAdminrpc_publish_room()).field("appKey", VarConfigUtils.ServiceKey)
				.field("msgBody", JSONObject.toJSONString(msgBody)).field("roomOwner", dstUid).field("sign", signParams)
				.asJsonAsync();

		try{
			// 异步调用处理用户等级问题
			AsyncManager.getInstance()
					.execute(new UpdateUserLevelAsyncTask(srcUid, 0, dstUid, sendTotal, (int) gets, gid, count, gname, wealths));
		}catch (Exception ex){
			logger.error("UpdateUserLevelAsyncTask>>>",ex);
		}


		// TOSY TASK
		TaskService.getInstance().taskProcess(srcUid, TaskConfigLib.SendDanmaku, 1);

		if (res == 0) {
			logger.info("<sendGift>插入账单失败");
		}
		return;

	}

	/**
	 * 喇叭功能
	 * 
	 * @param srcUid
	 *            送礼人uid
	 * @param anchoruid
	 *            房间主播uid
	 * @param gid
	 *            礼物id
	 * @param count
	 *            礼物数量
	 */
	@Override
	public void sendHorn(Integer srcUid, Integer anchoruid, int gid, Byte os, String msg, ReturnModel returnModel) {

		int count = 1;
		if (OtherRedisService.getInstance().getSilent(anchoruid, srcUid)) {
			returnModel.setCode(CodeContant.liveSilenting);
			returnModel.setMessage("你已被管理员禁言");
			return;
		}
		if(gid != 23){
			returnModel.setCode(CodeContant.GIFTCONFIGEXIST);
			returnModel.setMessage("礼物不存在");
			return;
		}
		ConfigGiftModel giftConfigModel = configService.getGiftInfoByGidNew(gid);
		if (giftConfigModel == null) {
			returnModel.setCode(CodeContant.GIFTCONFIGEXIST);
			returnModel.setMessage("礼物不存在");
			return;
		}
		String gname = giftConfigModel.getGname();
		int price = giftConfigModel.getGprice();
		int wealth = giftConfigModel.getWealth();

		UserAssetModel sendUserAssetModel = userService.getUserAssetByUid(srcUid, false);
		UserBaseInfoModel userBaseinfo = userService.getUserbaseInfoByUid(srcUid, false);
		
		UserBaseInfoModel anchorModel = userService.getUserbaseInfoByUid(anchoruid, false);

		if (sendUserAssetModel == null || userBaseinfo == null) {
			returnModel.setCode(CodeContant.USERASSETEXITS);
			returnModel.setMessage("用户不存在");
			return;
		}
		if (price * count > sendUserAssetModel.getMoney()) {
			returnModel.setCode(CodeContant.MONEYLESS);
			returnModel.setMessage("金额不足");
			return;
		}
		// 礼物总价值
		int sendTotal = price * count;
		// 主播获得价值
		double gets = sendTotal;
		int wealths = wealth * count;
		// 送礼者 扣费
		int res = userService.updUserAssetBySendUid(srcUid, sendTotal, wealths);
		if (res == 0) {
			returnModel.setCode(CodeContant.MONEYDEDUCT);
			returnModel.setMessage("扣费失败");
			return;
		}

		res = billService.insertBill(gid, count, price, srcUid, sendUserAssetModel.getMoney() - sendTotal,
				sendUserAssetModel.getWealth() + sendTotal, sendUserAssetModel.getCredit(), anchoruid, 0, 0, 0,
				System.currentTimeMillis() / 1000, 1, gets, os, "", userBaseinfo.getNickname(), anchorModel.getNickname(),anchorModel.getFamilyId());

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("consume", sendTotal);
		map.put("surplus", sendUserAssetModel.getMoney() - sendTotal);
		returnModel.setCode(CodeContant.OK);
		returnModel.setMessage("发送喇叭成功");
		returnModel.setData(map);
		
		long time = System.currentTimeMillis();
		userTransactionHisService.saveUserTransactionHis(10, srcUid, 0.00, (long)sendTotal, time, "", 1);

//		UserBaseInfoModel anchorModel = userService.getUserbaseInfoByUid(anchoruid, false);
		// TOSY 拼装发送房间广播
		HornCMod msgBody = new HornCMod();
		msgBody.setMsg(msg);
		msgBody.setAvatar(userBaseinfo.getHeadimage());
		msgBody.setNickname(userBaseinfo.getNickname());
		msgBody.setUid(userBaseinfo.getUid());
		msgBody.setLevel(userBaseinfo.getUserLevel());
		msgBody.setAnchoruid(anchoruid);
		msgBody.setAnchorName(anchorModel.getNickname());

		String signParams = EncryptUtils.signParams(VarConfigUtils.ServiceSecret, "appKey=" + VarConfigUtils.ServiceKey,
				"msgBody=" + JSONObject.toJSONString(msgBody));

		Unirest.post(UrlConfigLib.getUrl("url").getAdminrpc_publish_room_all())
				.field("appKey", VarConfigUtils.ServiceKey).field("msgBody", JSONObject.toJSONString(msgBody))
				.field("sign", signParams).asJsonAsync();

		try{
			// 异步调用处理用户等级问题
			AsyncManager.getInstance()
					.execute(new UpdateUserLevelAsyncTask(srcUid, 0, anchoruid, sendTotal, (int) gets, gid, count, gname, wealths));
		}catch (Exception ex){
			logger.error("UpdateUserLevelAsyncTask>>>",ex);
		}


		// TOSY TASK
		TaskService.getInstance().taskProcess(srcUid, TaskConfigLib.SendLouder, 1);

		if (res == 0) {
			logger.info("<sendGift>插入账单失败");
		}
		return;

	}

	/**
	 * 喇叭功能
	 * 
	 * @param srcUid
	 *            送礼人uid
	 * @param anchoruid
	 *            房间主播uid
	 * @param gid
	 *            礼物id
	 * @param count
	 *            礼物数量
	 */
	@Override
	public void sendHornBag(Integer srcUid, Integer anchoruid, int gid, Byte os, String msg, ReturnModel returnModel) {

		int count = 1;
		if (OtherRedisService.getInstance().getSilent(anchoruid, srcUid)) {
			returnModel.setCode(CodeContant.liveSilenting);
			returnModel.setMessage("你已被管理员禁言");
			return;
		}

		ConfigGiftModel giftConfigModel = configService.getGiftInfoByGidNew(gid);
		if (giftConfigModel == null) {
			returnModel.setCode(CodeContant.GIFTCONFIGEXIST);
			returnModel.setMessage("礼物不存在");
			return;
		}
		String gname = giftConfigModel.getGname();
		int price = giftConfigModel.getGprice();
		int wealth = giftConfigModel.getWealth();

		UserAssetModel sendUserAssetModel = userService.getUserAssetByUid(srcUid, false);
		UserBaseInfoModel userBaseinfo = userService.getUserbaseInfoByUid(srcUid, false);

		UserBaseInfoModel anchorModel = userService.getUserbaseInfoByUid(anchoruid, false);
		
		if (sendUserAssetModel == null || userBaseinfo == null) {
			returnModel.setCode(CodeContant.USERASSETEXITS);
			returnModel.setMessage("用户不存在:" + CodeContant.USERASSETEXITS);
			return;
		}

		Map<Integer, UserItemModel> baglist = userItemService.getItemListByUid(srcUid, false);
		if (!baglist.containsKey(gid)) {
			returnModel.setCode(CodeContant.baggidless);
			returnModel.setMessage("背包礼物不足:" + CodeContant.baggidless);
			return;
		} else {
			if (baglist.get(gid).getNum() < count) {
				returnModel.setCode(CodeContant.baggidless);
				returnModel.setMessage("背包礼物不足:" + CodeContant.baggidless);
				return;
			}
		}

		// 礼物总价值
		int sendTotal = price * count;
		// 主播获得价值
		double gets = giftConfigModel.getCredit() * count;
		int wealths = wealth * count;

		int res = userItemService.updUserItemBySendUid(srcUid, gid, count);
		if (res == 0) {
			returnModel.setCode(CodeContant.baggiddeduct);
			returnModel.setMessage("送礼失败:" + CodeContant.baggiddeduct);
			return;
		}
		// 送礼者 扣费
		res = userService.updUserAssetBySendUid(srcUid, 0, wealths);
		if (res == 0) {
			returnModel.setCode(CodeContant.MONEYDEDUCT);
			returnModel.setMessage("扣费失败");
			return;
		}

		res = billService.insertBill(gid, count, price, srcUid, sendUserAssetModel.getMoney(),
				sendUserAssetModel.getWealth() + sendTotal, sendUserAssetModel.getCredit(), anchoruid, 0, 0, 0,
				System.currentTimeMillis() / 1000, 1, gets, os, "bag", userBaseinfo.getNickname(), anchorModel.getNickname(),anchorModel.getFamilyId());

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("consume", 0);
		map.put("surplus", sendUserAssetModel.getMoney());
		map.put("bagflag", baglist.get(gid).getNum() - count);
		returnModel.setCode(CodeContant.OK);
		returnModel.setMessage("发送喇叭成功");
		returnModel.setData(map);

		// TOSY 拼装发送房间广播
		HornCMod msgBody = new HornCMod();
		msgBody.setMsg(msg);
		msgBody.setAvatar(userBaseinfo.getHeadimage());
		msgBody.setNickname(userBaseinfo.getNickname());
		msgBody.setUid(userBaseinfo.getUid());
		msgBody.setLevel(userBaseinfo.getUserLevel());
		msgBody.setAnchoruid(anchoruid);
		msgBody.setAnchorName(anchorModel.getNickname());

		String signParams = EncryptUtils.signParams(VarConfigUtils.ServiceSecret, "appKey=" + VarConfigUtils.ServiceKey,
				"msgBody=" + JSONObject.toJSONString(msgBody));

		Unirest.post(UrlConfigLib.getUrl("url").getAdminrpc_publish_room_all())
				.field("appKey", VarConfigUtils.ServiceKey).field("msgBody", JSONObject.toJSONString(msgBody))
				.field("sign", signParams).asJsonAsync();

		try{
			// 异步调用处理用户等级问题
			AsyncManager.getInstance()
					.execute(new UpdateUserLevelAsyncTask(srcUid, 0, anchoruid, sendTotal, (int) gets, gid, count, gname, wealths));
		}catch (Exception ex){
			logger.error("UpdateUserLevelAsyncTask>>>",ex);
		}


		// TOSY TASK
		TaskService.getInstance().taskProcess(srcUid, TaskConfigLib.SendLouder, 1);

		if (res == 0) {
			logger.info("<sendGift>插入账单失败");
		}
		return;

	}

	/**
	 * 获取排行榜
	 * 
	 * @param typeUser
	 *            =anchor主播 =user用户
	 * @param typeTimes
	 *            =day日榜 =week周榜 =month月榜 =all总榜
	 * @param returnModel
	 */
	@Override
	public void getUserRank(int srcuid, String typeUser, String typeTimes, ReturnModel returnModel) {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Map<String, Object> _map = new HashMap<String, Object>();
		Set<Tuple> userRank = null;
		if ("day".equals(typeTimes)) {
			userRank = UserRedisService.getInstance().getUserDayRank(typeUser, 1);
		} else if ("week".equals(typeTimes)) {
			userRank = UserRedisService.getInstance().getUserWeekRank(typeUser,9);
		} else if ("month".equals(typeTimes)) {
			userRank = UserRedisService.getInstance().getUserMonthRank(typeUser);
		} else if ("all".equals(typeTimes)) {
			userRank = UserRedisService.getInstance().getUserAllRank(typeUser);
		}
		for (Tuple tuple : userRank) {
			Map<String, Object> map = new HashMap<String, Object>();
			UserBaseInfoModel userBaseInfoModel = userService.getUserbaseInfoByUid(Integer.valueOf(tuple.getElement()),
					false);
			if (userBaseInfoModel != null) {
				map.put("uid", userBaseInfoModel.getUid());
				map.put("headimage", userBaseInfoModel.getHeadimage());
				map.put("nickname", userBaseInfoModel.getNickname());
				map.put("sex", userBaseInfoModel.getSex());
				map.put("status", userBaseInfoModel.getLiveStatus());
				if (userBaseInfoModel.getLiveStatus()) {
					map.put("domain", liveService
							.getVideoConfig(srcuid, userBaseInfoModel.getUid(), userBaseInfoModel.getVideoline())
							.get("domain"));
				} else {
					map.put("domain", "");
				}

				map.put("amount", (long) tuple.getScore());
				if ("anchor".equalsIgnoreCase(typeUser)) {
					map.put("level", userBaseInfoModel.getAnchorLevel());
				}else {
					map.put("level", userBaseInfoModel.getUserLevel());
				}
				
				list.add(map);
			}
		}
		_map.put("list", list);
		returnModel.setData(_map);
	}

	/**
	 * 获取排行榜
	 * 
	 * @param typeUser
	 *            =anchor主播 =user用户
	 * @param typeTimes
	 *            =day日榜 =week周榜 =month月榜 =all总榜
	 * @param returnModel
	 */
	@Override
	public void getUserRankRq(int srcuid, String typeTimes, ReturnModel returnModel) {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Map<String, Object> _map = new HashMap<String, Object>();
		Set<Tuple> userRank = null;
		if ("day".equals(typeTimes)) {
			userRank = UserRedisService.getInstance().getUserDayRankRq(1);
		} else if ("week".equals(typeTimes)) {
			userRank = UserRedisService.getInstance().getUserWeekRankRq();
		} else if ("month".equals(typeTimes)) {
			userRank = UserRedisService.getInstance().getUserMonthRankRq();
		} else if ("all".equals(typeTimes)) {
			userRank = UserRedisService.getInstance().getUserAllRankRq();
		}
		for (Tuple tuple : userRank) {
			Map<String, Object> map = new HashMap<String, Object>();
			UserBaseInfoModel userBaseInfoModel = userService.getUserbaseInfoByUid(Integer.valueOf(tuple.getElement()),
					false);
			if (userBaseInfoModel != null) {
				map.put("uid", userBaseInfoModel.getUid());
				map.put("headimage", userBaseInfoModel.getHeadimage());
				map.put("nickname", userBaseInfoModel.getNickname());
				map.put("sex", userBaseInfoModel.getSex());
				map.put("amount", (long) tuple.getScore());
				map.put("status", userBaseInfoModel.getLiveStatus());
				if (userBaseInfoModel.getLiveStatus()) {
					map.put("domain", liveService
							.getVideoConfig(srcuid, userBaseInfoModel.getUid(), userBaseInfoModel.getVideoline())
							.get("domain"));
				} else {
					map.put("domain", "");
				}
				map.put("level", userBaseInfoModel.getAnchorLevel());
				list.add(map);
			}
		}
		_map.put("list", list);
		returnModel.setData(_map);
	}

	@Override
	public void getUserRankRqPC(Integer type,int num,ReturnModel returnModel) {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Map<String, Object> _map = new HashMap<String, Object>();
		Set<Tuple> userRank = null;
		String day = "";
		if (type == 0) {
			day = DateUtils.getDate(null, 0, "yyyyMMdd");
		}else {
			day = DateUtils.getDate(null, -1, "yyyyMMdd");
		}
		userRank = UserRedisService.getInstance().getUserDayRankRq(day,num);
		
		for (Tuple tuple : userRank) {
			Map<String, Object> map = new HashMap<String, Object>();
			UserBaseInfoModel userBaseInfoModel = userService.getUserbaseInfoByUid(Integer.valueOf(tuple.getElement()),
					false);
			if (userBaseInfoModel != null) {
				map.put("uid", userBaseInfoModel.getUid());
				map.put("headimage", userBaseInfoModel.getHeadimage());
				map.put("nickname", userBaseInfoModel.getNickname());
				map.put("status", userBaseInfoModel.getLiveStatus());
				map.put("level", userBaseInfoModel.getAnchorLevel());
				list.add(map);
			}
		}
		_map.put("list", list);
		returnModel.setData(_map);
	}

	/**
	 * 获取周星
	 * 
	 * @param typeUser
	 *            =anchor主播 =user用户
	 * @param times
	 *            =0本周 =1上周
	 * @param returnModel
	 */
	@Override
	public void getZhouxin(String typeUser, int times, ReturnModel returnModel) {
		List<Map<String, Object>> zhouxinList = new ArrayList<Map<String, Object>>();
		Map<String, Object> _map = new HashMap<String, Object>();

		String week = DateUtils.getWeekStart(times);
		Set<String> zhouxinGift = UserRedisService.getInstance().getZhouxinGiftlist(week, 1);
		if (zhouxinGift != null) {

			for (String gid : zhouxinGift) {
				List<Map<String, Object>> giftRank = new ArrayList<Map<String, Object>>();

				Set<Tuple> zhouxin = UserRedisService.getInstance().getZhouxin(gid, typeUser, week, 2);
				int i = 0;
				for (Tuple tuple : zhouxin) {
					Map<String, Object> usrMap = new HashMap<String, Object>();
					UserBaseInfoModel userBaseInfoModel = userService
							.getUserbaseInfoByUid(Integer.valueOf(tuple.getElement()), false);
					if (userBaseInfoModel == null) {
						continue;
					}
					i++;
					usrMap.put("uid", userBaseInfoModel.getUid());
					usrMap.put("nickname", userBaseInfoModel.getNickname());
					usrMap.put("headimage", userBaseInfoModel.getHeadimage());
					usrMap.put("sex", userBaseInfoModel.getSex());
					usrMap.put("count", (long) tuple.getScore());
					usrMap.put("rank", i);
					if ("anchor".equalsIgnoreCase(typeUser)) {
						usrMap.put("level", userBaseInfoModel.getAnchorLevel());
					} else {
						usrMap.put("level", userBaseInfoModel.getUserLevel());
					}
					giftRank.add(usrMap);
				}

				ConfigGiftModel giftConfigModel = configService.getGiftInfoByGidNew(Integer.valueOf(gid));
				
				Map<String, Object> gidMap = new HashMap<String, Object>();
				gidMap.put("gid", gid);
				gidMap.put("gname", giftConfigModel.getGname());
				gidMap.put("icon", giftConfigModel.getIcon());
				
				gidMap.put("userlist", giftRank);

				zhouxinList.add(gidMap);
			}
		}
		_map.put("list", zhouxinList);
		returnModel.setData(_map);
	}

	@Override
	public void getZhouxinPC(ReturnModel returnModel) {
		
		List<Map<String, Object>> zhouxinList = new ArrayList<Map<String, Object>>();
		Map<String, Object> _map = new HashMap<String, Object>();

		String week = DateUtils.getWeekStart(0);
		Set<String> zhouxinGift = UserRedisService.getInstance().getZhouxinGiftlist(week, 1);
		if (zhouxinGift != null) {

			for (String gid : zhouxinGift) {

				Set<Tuple> zhouxin = UserRedisService.getInstance().getZhouxin(gid, "anchor", week, 0);
				if (zhouxin == null || zhouxin.size() == 0) {
					continue;
				}
				for (Tuple tuple : zhouxin) {
					Map<String, Object> usrMap = new HashMap<String, Object>();
					UserBaseInfoModel userBaseInfoModel = userService
							.getUserbaseInfoByUid(Integer.valueOf(tuple.getElement()), false);
					if (userBaseInfoModel == null) {
						continue;
					}
					usrMap.put("uid", userBaseInfoModel.getUid());
					usrMap.put("nickname", userBaseInfoModel.getNickname());
					usrMap.put("headimage", userBaseInfoModel.getHeadimage());
					usrMap.put("level", userBaseInfoModel.getAnchorLevel());
					usrMap.put("status", userBaseInfoModel.getLiveStatus());
					zhouxinList.add(usrMap);
				}
			}
		}
		_map.put("list", zhouxinList);
		returnModel.setData(_map);
	}

	/**
	 * 获取主播获取本周星的情况
	 * 
	 * @param anchor
	 *            主播UID
	 * 
	 * @param returnModel
	 */
	@Override
	public void getZhouxinOfPerson(int anchor, ReturnModel returnModel) {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Map<String, Object> _map = new HashMap<String, Object>();

		// 本周时间
		String week = DateUtils.getWeekStart(0);
		Set<String> zhouxinGift = UserRedisService.getInstance().getZhouxinGiftlist(week, 1);

		if (zhouxinGift != null) {
			for (String gid : zhouxinGift) {

				Map<String, Object> zhouxinRankAndScoreUid = UserRedisService.getInstance()
						.getZhouxinRankAndScoreUid(anchor, week, gid);
				list.add(zhouxinRankAndScoreUid);
			}
		}
		_map.put("list", list);
		returnModel.setData(_map);
	}

	@Override
	public void getRanks(ReturnModel returnModel) {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Map<String, Object> _map = new HashMap<String, Object>();
		Set<Tuple> anchorRank = null;
		Set<Tuple> userRank = null;
		Set<Tuple> userRq = null;

		anchorRank = UserRedisService.getInstance().getUserDayRank("anchor", 0L);
		userRank = UserRedisService.getInstance().getUserDayRank("user", 0L);
		userRq = UserRedisService.getInstance().getAnchorMonthSupportRank(0L);

		if (anchorRank != null && anchorRank.size() > 0) {
			for (Tuple tuple : anchorRank) {
				Map<String, Object> map = new HashMap<String, Object>();
				UserBaseInfoModel userBaseInfoModel = userService
						.getUserbaseInfoByUid(Integer.valueOf(tuple.getElement()), false);
				// jackzhang add 500
				if (userBaseInfoModel == null) {
					logger.error(String.format("anchorRank>>uid:%s", tuple.getElement()));
				} else {
					map.put("headimage", userBaseInfoModel.getHeadimage());
					map.put("nickname", userBaseInfoModel.getNickname());
					map.put("uid", userBaseInfoModel.getUid());
					map.put("level", userBaseInfoModel.getAnchorLevel());
					map.put("type", "anchor");
					list.add(map);
					break;
				}
			}
		}

		if (userRank != null && userRank.size() > 0) {
			for (Tuple tuple : userRank) {
				Map<String, Object> map = new HashMap<String, Object>();
				UserBaseInfoModel userBaseInfoModel = userService
						.getUserbaseInfoByUid(Integer.valueOf(tuple.getElement()), false);
				// jackzhang add 500
				if (userBaseInfoModel == null) {
					logger.error(String.format("userRank>>uid:%s", tuple.getElement()));
				} else {
					map.put("headimage", userBaseInfoModel.getHeadimage());
					map.put("nickname", userBaseInfoModel.getNickname());
					map.put("uid", userBaseInfoModel.getUid());
					map.put("level", userBaseInfoModel.getUserLevel());
					map.put("type", "user");
					list.add(map);
					break;
				}
			}
		}

		if (userRq != null && userRq.size() > 0) {
			for (Tuple tuple : userRq) {
				Map<String, Object> map = new HashMap<String, Object>();
				UserBaseInfoModel userBaseInfoModel = userService
						.getUserbaseInfoByUid(Integer.valueOf(tuple.getElement()), false);
				// jackzhang add 500
				if (userBaseInfoModel == null) {
					logger.error(String.format("userRq>>uid:%s", tuple.getElement()));
				} else {
					map.put("headimage", userBaseInfoModel.getHeadimage());
					map.put("nickname", userBaseInfoModel.getNickname());
					map.put("uid", userBaseInfoModel.getUid());
					map.put("level", userBaseInfoModel.getAnchorLevel());
					map.put("type", "rq");
					list.add(map);
					break;
				}
			}
		}

		String week = DateUtils.getWeekStart(0);
		Set<String> zhouxinGift = UserRedisService.getInstance().getZhouxinGiftlist(week, 1);
		if (zhouxinGift != null && zhouxinGift.size() > 0) {
			boolean bl = false;
			for (String gid : zhouxinGift) {
				Set<Tuple> zhouxin = UserRedisService.getInstance().getZhouxin(gid, "anchor", week, 0);
				for (Tuple tuple : zhouxin) {
					Map<String, Object> usrMap = new HashMap<String, Object>();
					UserBaseInfoModel userBaseInfoModel = userService
							.getUserbaseInfoByUid(Integer.valueOf(tuple.getElement()), false);
					if (userBaseInfoModel == null) {
						logger.error(String.format("zhouxinGift>>uid:%s", tuple.getElement()));
					} else {
						usrMap.put("uid", userBaseInfoModel.getUid());
						usrMap.put("nickname", userBaseInfoModel.getNickname());
						usrMap.put("headimage", userBaseInfoModel.getHeadimage());
						usrMap.put("level", userBaseInfoModel.getAnchorLevel());
						usrMap.put("type", "zhouxin");
						list.add(usrMap);
						bl = true;
						break;
					}
				}
				if (bl) {
					break;
				}
			}
		}
		_map.put("list", list);
		returnModel.setData(_map);
	}

	/**
	 * 用户等级变更
	 * 
	 * @param srcuid
	 *            送出礼物/获取经验值uid
	 * @param dstuid
	 *            收到礼物uid
	 * @param type
	 *            =1用户 =2主播
	 * @param source
	 *            =1送礼或收礼 =2活动获取经验值
	 * @return
	 */
	@Override
	public boolean updUserLevel(int srcuid, int dstuid, int anchoruid, int type, int source) {
		int ires = 0;
		if (type == 1) {

			UserBaseInfoModel sendUserBaseInfoModel = null;
			String userbaseinfo = UserRedisService.getInstance().getUserBaseInfo(srcuid);
			
			if (org.apache.commons.lang.StringUtils.isNotEmpty(userbaseinfo)) {
				sendUserBaseInfoModel = (UserBaseInfoModel) JSONObject.parseObject(userbaseinfo,
						UserBaseInfoModel.class);
			}
			
			UserAssetModel sendUserAssetModel = null; 
			String userAsset = UserRedisService.getInstance().getUserAsset(srcuid);
			
			if (userAsset != null && !"".equals(userAsset)) {
				sendUserAssetModel = (UserAssetModel) JSONObject.parseObject(userAsset, UserAssetModel.class);
			}
			
			if (sendUserBaseInfoModel == null || sendUserAssetModel == null) {
				return false;
			}
			
			int userLevel = sendUserBaseInfoModel.getUserLevel();
			Long wealth = sendUserAssetModel.getWealth();
			int curLevel = LevelConfigLib.getSuitableLevel(wealth + sendUserBaseInfoModel.getExp());

			System.err.println("updUserLevel    userLevel=" + userLevel + " curLevel" + curLevel + " exp="
					+ sendUserBaseInfoModel.getExp());

			if (userLevel < curLevel) {
				// 升级
				ires = UserServiceImpl.getInstance().updUserBaseInfoLevelByUid(curLevel, srcuid, 1);
				// System.err.println(String.format("UserServiceImpl.getInstance().updUserBaseInfoLevelByUid>>>%d",userLevel));
				if (source == 1) {
					for (int iLevel = userLevel + 1; iLevel <= curLevel; iLevel++) {
						// 用户升级
						rpcAdminUserUpLevel(iLevel, srcuid, anchoruid, sendUserBaseInfoModel.getNickname(),
								sendUserBaseInfoModel.getHeadimage(), sendUserBaseInfoModel.getSex(), 0);
					}
				}
				return ires == 1 ? true : false;
			} else {
				// 未升级
				return true;
			}
		} else if (type == 2) {

			if (dstuid != anchoruid) {
				// 送礼对象非主播
				type = 0;
			} else {
				// 送礼对象是主播
				type = 1;
			}
			UserBaseInfoModel getUserBaseInfoModel = null;// userService.getUserbaseInfoByUid(dstuid,
															// false);

			String userbaseinfo = UserRedisService.getInstance().getUserBaseInfo(dstuid);
			if (org.apache.commons.lang.StringUtils.isNotEmpty(userbaseinfo)) {
				getUserBaseInfoModel = (UserBaseInfoModel) JSONObject.parseObject(userbaseinfo,
						UserBaseInfoModel.class);
			}

			UserAssetModel getUserAssetModel = null;// userService.getUserAssetByUid(dstuid,
													// false);
			String userAsset = UserRedisService.getInstance().getUserAsset(dstuid);
			if (userAsset != null && !"".equals(userAsset)) {
				getUserAssetModel = (UserAssetModel) JSONObject.parseObject(userAsset, UserAssetModel.class);
			}
			if (getUserBaseInfoModel == null || getUserAssetModel == null) {
				return false;
			}
			int anchorLevel = getUserBaseInfoModel.getAnchorLevel();
			int creditTotal = getUserAssetModel.getCreditTotal();

			int loopLimit = 100;
			int curLevel = anchorLevel;
			while (curLevel < 70 && loopLimit-- > 0) {
				LevelsConfig config = LevelsConfigLib.getForAdvanced(curLevel + 1);
				if (config != null) {
					if (config.getNumber() <= creditTotal) {
						curLevel = curLevel + 1;
						// 主播升级
						rpcAdminUserUpLevel(curLevel, dstuid, anchoruid, getUserBaseInfoModel.getNickname(),
								getUserBaseInfoModel.getHeadimage(), getUserBaseInfoModel.getSex(), type);
					} else {
						break;
					}
				} else {
					break;
				}
			}
			if (curLevel > anchorLevel) {
				// 升级
				ires = UserServiceImpl.getInstance().updUserBaseInfoLevelByUid(curLevel, dstuid, 2);
				return ires == 1 ? true : false;
			} else {
				// 未升级
				return true;
			}
		}
		return false;
	}

	/**
	 * 获取最新列表的推荐结果
	 * 
	 * @param typeUser
	 *            =anchor主播 =user用户
	 * @param returnModel
	 */
	@Override
	public void getRecommandByNow(int srcuid, ReturnModel returnModel) {
		List<Map<String, Object>> notlivelist = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> livelist = new ArrayList<Map<String, Object>>();
		List<Object> list = new ArrayList<Object>();
		Map<String, Object> _map = new HashMap<String, Object>();

		Date date = new Date();
		Set<Tuple> userRank = UserRedisService.getInstance()
				.getUserDayRankForDate(date, 50);
		if (userRank.size() < 5) {
			userRank = null;
			date = DateUtils.getNDaysAfterDate(date, -1);
			userRank = UserRedisService.getInstance().getUserDayRankForDate(
					date, 9);
		}
		for (Tuple tuple : userRank) {
			Map<String, Object> map = new HashMap<String, Object>();
			UserBaseInfoModel userBaseInfoModel = userService
					.getUserbaseInfoByUid(Integer.valueOf(tuple.getElement()),
							false);
			if (userBaseInfoModel != null) {
				map.put("uid", userBaseInfoModel.getUid());
				map.put("headimage", userBaseInfoModel.getHeadimage());
				map.put("nickname", userBaseInfoModel.getNickname());
				map.put("status", userBaseInfoModel.getLiveStatus());
				map.put("verified", userBaseInfoModel.isVerified());
				map.put("enters", this.getRoomShowUsers(userBaseInfoModel.getUid(), userBaseInfoModel.getContrRq()));
				if (userBaseInfoModel.getLiveStatus() && srcuid != 0) {
					map.put("domain",
							liveService.getVideoConfig(srcuid,
									userBaseInfoModel.getUid(),
									userBaseInfoModel.getVideoline()).get(
									"domain"));
				} else {
					map.put("domain", "");
				}
				if (userBaseInfoModel.getLiveStatus()) {
					livelist.add(map);
				} else {
					notlivelist.add(map);
				}
			}
		}
		list.addAll(livelist);
		list.addAll(notlivelist);
		if (list.size() > 5) {
			_map.put("list", list.subList(0, 5));
		} else {
			_map.put("list", list);
		}
		returnModel.setData(_map);
	}


	/**
	 * 获取最新列表的推荐结果
	 * 
	 * @param typeUser
	 *            =anchor主播 =user用户
	 * @param returnModel
	 */
	@Override
	public void getLivingForeign(HashMap<String, Object> returnModel) {
		List<Map<String, Object>> livelist = new ArrayList<Map<String, Object>>();
		Map<String, Object> map = null;

		Set<String> hotRoom = OtherRedisService.getInstance().getHotRoom(0);
		
		for(String suid : hotRoom){
			int uid = Integer.valueOf(suid);
			UserBaseInfoModel userBaseInfoModel = userService.getUserbaseInfoByUid(uid, false);
			
			if (userBaseInfoModel != null) {
				map = new HashMap<String, Object>();
				map.put("radio_id", uid);
				map.put("radio_name", userBaseInfoModel.getNickname());
				map.put("radio_subname", userBaseInfoModel.getSignature());
				map.put("radio_city", userBaseInfoModel.getCity());
				map.put("radio_fan_number", RelationRedisService.getInstance().getFansTotal(uid));// 粉丝数
				map.put("radio_show_number", this.getRoomShowUsers(uid, userBaseInfoModel.getContrRq()));// 观看数
				map.put("radio_label", ""); // slogan
				map.put("radio_pic_url", userBaseInfoModel.getLivimage());
				map.put("radio_action_url", "http://www.xiaozhutv.com/yyb/share.html?uid=" + uid);
				map.put("is_showing_now", 1);
				livelist.add(map);
			}
		}
		returnModel.put("radio_list", livelist);
	}

	/**
	 * 注册时推荐10个用户
	 * 
	 * @param page
	 * @return
	 */
	public void getRecommendForRegister(ReturnModel returnModel) {
		Map<String, Object> returnmap = new HashMap<String, Object>();
		Set<String> setLivingList = OtherRedisService.getInstance().getRecommendRoom(0);
		logger.info("当前热门推荐列表条数 : " + setLivingList.size());
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Set<Integer> uids = new HashSet<Integer>();
		Map<String, Object> map;
		if (setLivingList != null && setLivingList.size() > 0) {
			for (String suid : setLivingList) {
				uids.add(Integer.parseInt(suid));
			}
		}
		logger.info("第一次循环uids count : " + uids.size());
		if (uids.size() < 10) {
			int allcount = 10;
			int nextCount = allcount - uids.size();
			logger.info("nextCount : " + nextCount);
			Date date = new Date();
			Set<Tuple> userRank = UserRedisService.getInstance().getUserDayRankForDate(date, 15);
			logger.info("UserDayRankForDate size : " + userRank.size());
			if (userRank.size() < nextCount) {
				userRank = null;
				date = DateUtils.getNDaysAfterDate(date, -1);
				userRank = UserRedisService.getInstance().getUserDayRankForDate(date, 15);
				logger.info("yesterday UserDayRankForDate size : " + userRank.size());
			}
			for (Tuple tuple : userRank) {
				uids.add(Integer.parseInt(tuple.getElement()));
			}
		}
		logger.info("all uids size : " + uids.size());
		UserBaseInfoModel userBaseInfoModel;
		LiveMicTimeModel liveMicTimeModel;
		for (Integer suid : uids) {
			map = new HashMap<String, Object>();
			int uid = Integer.valueOf(suid);
			userBaseInfoModel = userService.getUserbaseInfoByUid(uid, false);
			liveMicTimeModel = liveService.getLiveMicInfoByUid(uid, false);
			if (userBaseInfoModel == null || liveMicTimeModel == null || liveMicTimeModel.getType()) {
				continue;
			} else {
				Boolean isLiving = userBaseInfoModel.getLiveStatus();
				map.put("living", isLiving);

				map.put("uid", suid);
				map.put("nickname", userBaseInfoModel.getNickname());
				map.put("avatar", userBaseInfoModel.getHeadimage());
				map.put("anchorLevel", userBaseInfoModel.getAnchorLevel());
				map.put("slogan", liveMicTimeModel.getSlogan());
				map.put("city",
						liveMicTimeModel.getCity() == "" ? VarConfigUtils.Location : liveMicTimeModel.getCity());
				map.put("enters", this.getRoomShowUsers(uid, userBaseInfoModel.getContrRq()));
				map.put("mobileliveimg", userBaseInfoModel.getLivimage());
				map.put("sex", userBaseInfoModel.getSex());

				String stream = configService.getThirdStream(uid);
				if (null == stream) {
					map.put("domain",
							liveService.getVideoConfig(0, uid, userBaseInfoModel.getVideoline()).get("domain"));
				} else {
					map.put("domain", stream);
				}

				map.put("verified", userBaseInfoModel.isVerified());
				map.put("userLevel", userBaseInfoModel.getUserLevel());
			}
			if (map.size() > 0) {
				list.add(map);
			}
		}
		if (list.size() > 10) {
			returnmap.put("recommend", list.subList(0, 10));
		} else {
			returnmap.put("recommend", list);
		}
		returnModel.setData(returnmap);
	}

	/**
	 * 显示房间中的人数
	 * 
	 * @param anchoruid
	 *            主播UID
	 * @param virtuals
	 *            后台设置的虚拟用户数
	 * @return
	 */
	@Override
	public int getRoomShowUsers(int anchoruid, int virtuals) {
		return OtherRedisService.getInstance().getRoomUserCounts(anchoruid);
//		int realtimes = RelationRedisService.getInstance().getRealEnterRoomTimes(anchoruid);
//		int realcout = RelationRedisService.getInstance().getLiveRealEnterTotal(anchoruid);
//
//		int roomCreditThis = RelationRedisService.getInstance().getRoomCreditThis(anchoruid);
//
//		return (int) (virtuals + (realcout + 1) * 3 + (realtimes + 1) * 8 + Math.ceil(roomCreditThis / 20));
	}

	@Override
	public void getRecommendTwoFlash(String uid,int num, ReturnModel returnModel) {

		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		
		Set<Integer> recommendUid = new HashSet<Integer>();
		
		Set<String> recommendRoom = OtherRedisService.getInstance().getRecommendRoom(1);
		if (recommendRoom != null && recommendRoom.size() > 0) {
			for(String suid : recommendRoom){
				if (uid.equals(suid)) {
					continue;
				}
				//tosy debug
				UserBaseInfoModel uinfo = userService.getUserbaseInfoByUid(Integer.valueOf(suid), false);
				if(null == uinfo || false == uinfo.getLiveStatus()){
					continue;
				}
				
				recommendUid.add(Integer.valueOf(suid));
				if (recommendUid.size() == num) {
					break;
				}
			}
		}
		
		if (recommendUid.size() < num) {
			Set<String> hotRoom = OtherRedisService.getInstance().getHotRoom(1);
			if (hotRoom != null && hotRoom.size() > 0) {
				for(String suid : hotRoom){
					if (uid.equals(suid)) {
						continue;
					}else if (recommendUid.contains(Integer.valueOf(suid))) {
						continue;
					}

					//tosy debug
					UserBaseInfoModel uinfo = userService.getUserbaseInfoByUid(Integer.valueOf(suid), false);
					if(null == uinfo || false == uinfo.getLiveStatus()){
						continue;
					}
					
					recommendUid.add(Integer.valueOf(suid));
					if (recommendUid.size() == num) {
						break;
					}
				}
			}
		}
		if (recommendUid.size() > 0) {
			UserBaseInfoModel userBaseInfoModel = null;
			for(Integer iuid:recommendUid){
				userBaseInfoModel = userService.getUserbaseInfoByUid(iuid, false);
				
				Map<String, Object> map = new HashMap<String,Object>();
				if (userBaseInfoModel != null) {
					map.put("rid", userBaseInfoModel.getUid());
					map.put("nickname", userBaseInfoModel.getNickname());
					map.put("headimage", StringUtils.isEmpty(userBaseInfoModel.getPcimg1())?userBaseInfoModel.getLivimage():userBaseInfoModel.getPcimg1());
					map.put("usercount", this.getRoomShowUsers(iuid, userBaseInfoModel.getContrRq()));
					list.add(map);
				}
			}
		}
		if (list.size() > 0) {
			returnModel.setData(list);
		}
	}


	@Override
	public void randomAnchor(String uid,ReturnModel returnModel) {

		Integer recommendUid = null;
		
		Set<String> recommendRoom = OtherRedisService.getInstance().getRecommendRoom(20);
		if (recommendRoom != null && recommendRoom.size() > 0) {
			int min = 1;
			int max = recommendRoom.size();
			int randomIndex = min + (int)(Math.random() * ((max - min) + 1));
			List<String> list = new ArrayList<String>(recommendRoom); 
			recommendUid  = Integer.parseInt(list.get(randomIndex-1));
//			for(String suid : recommendRoom){
//				if (uid.equals(suid)) {
//					continue;
//				}
//				recommendUid = Integer.valueOf(suid);
//				break;
//			}
		}
		
		if (recommendUid == null) {
			Set<String> hotRoom = OtherRedisService.getInstance().getHotRoom(0);
			if (hotRoom != null && hotRoom.size() > 0) {
				int min = 1;
				int max = hotRoom.size();
				int randomIndex = min + (int)(Math.random() * ((max - min) + 1));
				List<String> list = new ArrayList<String>(hotRoom); 
				recommendUid  = Integer.parseInt(list.get(randomIndex-1));
//				for(String suid : hotRoom){
//					if (uid.equals(suid)) {
//						continue;
//					}
//					recommendUid = Integer.valueOf(suid);
//					break;
//				}
			}
		}
		if ( recommendUid == null) {
			returnModel.setCode(201);
			returnModel.setMessage("主播休息去了");
		}else {
			Map<String, Object> map = new HashMap<String,Object>();
			map.put("uid", recommendUid);
			returnModel.setData(map);
		}
	}
	
	public void rqZhuActivity(Integer uid, Integer dstUid){
		Long lg = System.currentTimeMillis() / 1000;
		if(lg >= 1475164800 && lg < 1475856000){ //10.1 人气猪活动 lg >= 1475164800 && lg < 1475856000
			RedisCommService.getInstance().zincrby(RedisContant.RedisNameUser, RedisContant.AnchorXiaozhuRun, 1, String.valueOf(dstUid), 1728000);
			//国庆 荣耀排名
			RedisCommService.getInstance().zincrby(RedisContant.RedisNameUser, RedisContant.UserXiaozhuRun, 1, String.valueOf(uid), 1728000);
		}
	}
	
	public List<Map<String,Object>> getHotGameAnchorList(Long gameId, Integer useruid,int os) {
		Set<String> set = OtherRedisService.getInstance().getHotGameAnchorList(gameId);
		return getUserData(set, useruid, os);
	}

	@Override
	public List<Map<String, Object>> gameAnchorListByGameId(Long gameId, Integer useruid, int os,int page) {
		Set<String> set = OtherRedisService.getInstance().getGameRoomListByGameId(page, gameId);
		return getUserData(set, useruid, os);
	}

	@Override
	public void getRoomInfoByAnchorId(Integer uid ,Integer anchorId ,int os,ReturnModel returnModel) {
		Set<String> set = new HashSet<String>();
		set.add(anchorId.toString());
		List<Map<String,Object>> list = getUserData(set, uid, os);
		if(list==null||list.size()==0) {
			returnModel.setCode(CodeContant.CONPARAMSEXCEPT);
			returnModel.setMessage("主播信息不存在");
			return;
		}
		returnModel.setData(list.get(0));
	}

	@Override
	public void getAnchorMonthSupportRank(int srcuid,ReturnModel returnModel) {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Map<String, Object> _map = new HashMap<String, Object>();
		Set<Tuple> anchorMonthRank = UserRedisService.getInstance().getAnchorMonthSupportRank(19L);
		for (Tuple tuple : anchorMonthRank) {
			Map<String, Object> map = new HashMap<String, Object>();
			UserBaseInfoModel userBaseInfoModel = userService.getUserbaseInfoByUid(Integer.valueOf(tuple.getElement()),false);
			if (userBaseInfoModel != null) {
				map.put("uid", userBaseInfoModel.getUid());
				map.put("headimage", userBaseInfoModel.getHeadimage());
				map.put("nickname", userBaseInfoModel.getNickname());
				map.put("sex", userBaseInfoModel.getSex());
				map.put("status", userBaseInfoModel.getLiveStatus());
				if (userBaseInfoModel.getLiveStatus()) {
					map.put("domain", liveService
							.getVideoConfig(srcuid, userBaseInfoModel.getUid(), userBaseInfoModel.getVideoline())
							.get("domain"));
				} else {
					map.put("domain", "");
				}
				map.put("amount", (long) tuple.getScore());
				map.put("level", userBaseInfoModel.getAnchorLevel());
				list.add(map);
			}
		}
		_map.put("list", list);
		returnModel.setData(_map);
	}
}
