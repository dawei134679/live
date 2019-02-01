package com.mpig.api.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.mashape.unirest.http.Unirest;
import com.mpig.api.dictionary.GlobalConfig;
import com.mpig.api.dictionary.lib.BaseConfigLib;
import com.mpig.api.dictionary.lib.GiftPromotionConfigLib;
import com.mpig.api.dictionary.lib.LevelConfigLib;
import com.mpig.api.dictionary.lib.PushTempeleConfigLib;
import com.mpig.api.dictionary.lib.SignConfigLib;
import com.mpig.api.dictionary.lib.TaskConfigLib;
import com.mpig.api.dictionary.lib.UrlConfigLib;
import com.mpig.api.dictionary.lib.VersionConfigLib;
import com.mpig.api.model.LiveMicTimeModel;
import com.mpig.api.model.RecordItem;
import com.mpig.api.model.ReturnModel;
import com.mpig.api.model.SearchModel;
import com.mpig.api.model.TaskConfigModel;
import com.mpig.api.model.UserAccountModel;
import com.mpig.api.model.UserAlbumModel;
import com.mpig.api.model.UserAssetModel;
import com.mpig.api.model.UserBaseInfoModel;
import com.mpig.api.modelcomet.ExitRoomCMod;
import com.mpig.api.modelcomet.UpdateAccountStatusCMod;
import com.mpig.api.modelcomet.UpdateIdentityCMod;
import com.mpig.api.modelcomet.ViolationWarningCMod;
import com.mpig.api.redis.core.RedisBucket;
import com.mpig.api.redis.service.OtherRedisService;
import com.mpig.api.redis.service.RelationRedisService;
import com.mpig.api.service.IAdminService;
import com.mpig.api.service.ILiveService;
import com.mpig.api.service.IRoomService;
import com.mpig.api.service.ISearchService;
import com.mpig.api.service.IUserAlbumService;
import com.mpig.api.service.IUserService;
import com.mpig.api.service.impl.ConfigServiceImpl;
import com.mpig.api.service.impl.IPaynowServiceImpl;
import com.mpig.api.service.impl.SearchServiceImpl;
import com.mpig.api.service.impl.WebServiceImpl;
import com.mpig.api.task.TaskService;
import com.mpig.api.utils.BaseContant;
import com.mpig.api.utils.ChatMessageUtil;
import com.mpig.api.utils.CodeContant;
import com.mpig.api.utils.Constant;
import com.mpig.api.utils.EncryptUtils;
import com.mpig.api.utils.JsonUtil;
import com.mpig.api.utils.ParamHandleUtils;
import com.mpig.api.utils.RedisContant;
import com.mpig.api.utils.VarConfigUtils;
import com.qiniu.common.QiniuException;
import com.qiniu.storage.BucketManager;
import com.qiniu.util.Auth;

import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

@Controller
@Scope("prototype")
@RequestMapping("/admin")
public class AdminController extends BaseController {

	private static final Logger logger = Logger.getLogger(AdminController.class);
	@Resource
	private IUserService userService;
	@Resource
	private ILiveService liveService;
	@Resource
	private IRoomService roomService;
	@Resource 
	private IPaynowServiceImpl payService;
	@Resource
	private IAdminService adminService;
	@Resource
	private IUserAlbumService userAlbumService;
	@Resource
	private ISearchService searchService;
	

	//编辑录像信息
	@RequestMapping(value = "/editRec", method = RequestMethod.GET)
	@ResponseBody
	public ReturnModel editRec(HttpServletRequest req) {
		String adminid = req.getParameter("adminid");
		if (null == adminid || adminid.isEmpty() || !adminid.contentEquals("admin")) {
			returnModel.setCode(CodeContant.ERROR);
			returnModel.setMessage("更新失败");
			return returnModel;
		}

		String uid = req.getParameter("uid");
		String time = req.getParameter("time");
		String isShow = req.getParameter("bshow");		//null  false 
		if(null == time || null == uid){
			returnModel.setCode(CodeContant.ERROR);
			returnModel.setMessage("更新失败");
			return returnModel;
		}
		
		if(null != isShow && 0 == isShow.compareToIgnoreCase("false")){
			//隐藏录像
			Map<String,String> rt = userService.getRecordAllByUid(uid, null);
			for(String key : rt.keySet()){
				if(0 == time.compareToIgnoreCase(key)){
					String jsonstr = rt.get(key);
					RecordItem item = JSONObject.parseObject(jsonstr, RecordItem.class);
					item.setShow(false);
					OtherRedisService.getInstance().addUidRecordByTime(uid,time,JSONObject.toJSONString(item));
					
					returnModel.setCode(CodeContant.OK);
					returnModel.setMessage("更新成功");
					return returnModel;
				}
			}
			returnModel.setCode(CodeContant.ERROR);
			returnModel.setMessage("无ID");
			return returnModel;
		}else{
			//更新录像，添加录像
			String mp4Path = req.getParameter("mp4Path");
			String m3u8Path = req.getParameter("m3u8Path");
			String coverPic = req.getParameter("coverPic");
			String argtime = req.getParameter("time");
			String title = req.getParameter("title");
			if(null == mp4Path || null == argtime || null == title){
				returnModel.setCode(CodeContant.ERROR);
				returnModel.setMessage("少参数");
				return returnModel;
			}
			RecordItem data = new RecordItem(argtime,title,mp4Path,m3u8Path,0,coverPic);
			OtherRedisService.getInstance().addUidRecordByTime(uid, argtime, JSONObject.toJSONString(data));
			returnModel.setCode(CodeContant.OK);
			returnModel.setMessage("更新成功");
			return returnModel;
		}
	}
	
	@RequestMapping(value = "/setRecordUid", method = RequestMethod.GET)
	@ResponseBody
	public ReturnModel setRecordUid(HttpServletRequest req) {
		String adminid = req.getParameter("adminid");
		if (null == adminid || adminid.isEmpty() || !adminid.contentEquals("admin")) {
			returnModel.setCode(CodeContant.ERROR);
			returnModel.setMessage("更新失败");
			return returnModel;
		}

		String uid = req.getParameter("uid");
		String isRecord = req.getParameter("isRecord");		//null  remove   not null  add 

		if (ConfigServiceImpl.updateRecordUid(uid,isRecord)) {
			returnModel.setCode(CodeContant.OK);
			returnModel.setMessage("更新成功");
		} else {
			returnModel.setCode(CodeContant.ERROR);
			returnModel.setMessage("更新失败");
		}
		return returnModel;
	}
	
	@RequestMapping(value = "/fixStreamKey", method = RequestMethod.GET)
	@ResponseBody
	public ReturnModel fixStreamKey(HttpServletRequest req) {
		String adminid = req.getParameter("adminid");
		if (null == adminid || adminid.isEmpty() || !adminid.contentEquals("admin")) {
			returnModel.setCode(CodeContant.ERROR);
			returnModel.setMessage("更新失败");
			return returnModel;
		}

		String uid = req.getParameter("uid");
		String key = req.getParameter("key");

		if (ConfigServiceImpl.updateStreamKey(uid,key)) {
			returnModel.setCode(CodeContant.OK);
			returnModel.setMessage("更新成功");
		} else {
			returnModel.setCode(CodeContant.ERROR);
			returnModel.setMessage("更新失败");
		}
		return returnModel;
	}
	
	@RequestMapping(value = "/updateThirdStream", method = RequestMethod.GET)
	@ResponseBody
	public ReturnModel updateThirdStream(HttpServletRequest req) {
		String adminid = req.getParameter("adminid");
		if (null == adminid || adminid.isEmpty() || !adminid.contentEquals("admin")) {
			returnModel.setCode(CodeContant.ERROR);
			returnModel.setMessage("更新失败");
			return returnModel;
		}
		// TODO AUTH!!!
		if (ConfigServiceImpl.updateThirdStream()) {
			returnModel.setCode(CodeContant.OK);
			returnModel.setMessage("更新成功");
		} else {
			returnModel.setCode(CodeContant.ERROR);
			returnModel.setMessage("更新失败");
		}
		return returnModel;
	}
	
	//更新连麦最低等级
	@RequestMapping(value = "/updateMicUserLv", method = RequestMethod.GET)
	@ResponseBody
	public ReturnModel updateMicUserLv(HttpServletRequest req) {
		String adminid = req.getParameter("adminid");
		if (null == adminid || adminid.isEmpty() || !adminid.contentEquals("admin")) {
			returnModel.setCode(CodeContant.ERROR);
			returnModel.setMessage("更新失败");
			return returnModel;
		}
		if (GlobalConfig.getInstance().loadMicUserLv()) {
			returnModel.setCode(CodeContant.OK);
			returnModel.setMessage("更新成功");
		} else {
			returnModel.setCode(CodeContant.ERROR);
			returnModel.setMessage("更新失败");
		}
		return returnModel;
	}
	
	
	@RequestMapping(value = "/taskProcess", method = RequestMethod.GET)
	@ResponseBody
	public ReturnModel taskProcess(HttpServletRequest req) {
		String adminid = req.getParameter("adminid");
		if (null == adminid || adminid.isEmpty() || !adminid.contentEquals("admin")) {
			returnModel.setCode(CodeContant.ERROR);
			returnModel.setMessage("验证失败");
			return returnModel;
		}
		
		String uid = req.getParameter("uid");
		TaskService.getInstance().taskProcess(Integer.parseInt(uid), TaskConfigLib.Authentication, 1);
		returnModel.setCode(CodeContant.OK);
		returnModel.setMessage("更新成功");
		return returnModel;
	}

	@RequestMapping(value = "/updateGift", method = RequestMethod.GET)
	@ResponseBody
	public ReturnModel updateGift(HttpServletRequest req) {
		String adminid = req.getParameter("adminid");
		if (null == adminid || adminid.isEmpty() || !adminid.contentEquals("admin")) {
			returnModel.setCode(CodeContant.ERROR);
			returnModel.setMessage("更新失败");
			return returnModel;
		}
		// TODO AUTH!!!
		if (ConfigServiceImpl.updateGiftListNew()) {
			returnModel.setCode(CodeContant.OK);
			returnModel.setMessage("更新成功");
		} else {
			returnModel.setCode(CodeContant.ERROR);
			returnModel.setMessage("更新失败");
		}
		return returnModel;
	}
	
	@RequestMapping(value = "/updateBanner", method = RequestMethod.GET)
	@ResponseBody
	public ReturnModel updateBanner(HttpServletRequest req) {
		String adminid = req.getParameter("adminid");
		if (null == adminid || adminid.isEmpty() || !adminid.contentEquals("admin")) {
			returnModel.setCode(CodeContant.ERROR);
			returnModel.setMessage("更新失败");
			return returnModel;
		}
		// TODO AUTH!!!
		if (WebServiceImpl.updateBannerlist()) {
			returnModel.setCode(CodeContant.OK);
			returnModel.setMessage("更新成功");
		} else {
			returnModel.setCode(CodeContant.ERROR);
			returnModel.setMessage("更新失败");
		}
		return returnModel;
	}
	

	@RequestMapping(value = "/updateGiftAct", method = RequestMethod.GET)
	@ResponseBody
	public ReturnModel updateGiftAct(HttpServletRequest req) {
		String adminid = req.getParameter("adminid");
		if (null == adminid || adminid.isEmpty() || !adminid.contentEquals("admin")) {
			returnModel.setCode(CodeContant.ERROR);
			returnModel.setMessage("更新失败");
			return returnModel;
		}
		// TODO AUTH!!!
		if (ConfigServiceImpl.updateGiftListAct()) {
			returnModel.setCode(CodeContant.OK);
			returnModel.setMessage("更新成功");
		} else {
			returnModel.setCode(CodeContant.ERROR);
			returnModel.setMessage("更新失败");
		}
		return returnModel;
	}

	@RequestMapping(value = "/updateTask", method = RequestMethod.GET)
	@ResponseBody
	public ReturnModel updateTask(HttpServletRequest req) {
		String adminid = req.getParameter("adminid");
		if (null == adminid || adminid.isEmpty() || !adminid.contentEquals("admin")) {
			returnModel.setCode(CodeContant.ERROR);
			returnModel.setMessage("更新失败");
			return returnModel;
		}
		// TODO AUTH!!!
		if (TaskConfigLib.getInstance().loadTaskConfigFromDb()) {
			returnModel.setCode(CodeContant.OK);
			returnModel.setMessage("更新成功");
		} else {
			returnModel.setCode(CodeContant.ERROR);
			returnModel.setMessage("更新失败");
		}
		return returnModel;
	}

	
	@RequestMapping(value = "/updateSign", method = RequestMethod.GET)
	@ResponseBody
	public ReturnModel updateSign(HttpServletRequest req) {
		String adminid = req.getParameter("adminid");
		if (null == adminid || adminid.isEmpty() || !adminid.contentEquals("admin")) {
			returnModel.setCode(CodeContant.ERROR);
			returnModel.setMessage("更新失败");
			return returnModel;
		}
		if (SignConfigLib.loadSignConfigFromDb()) {
			returnModel.setCode(CodeContant.OK);
			returnModel.setMessage("更新成功");
		} else {
			returnModel.setCode(CodeContant.ERROR);
			returnModel.setMessage("更新失败");
		}
		return returnModel;
	}
	
	@RequestMapping(value = "/updateLvExp", method = RequestMethod.GET)
	@ResponseBody
	public ReturnModel updateLvExp(HttpServletRequest req) {
		String adminid = req.getParameter("adminid");
		if (null == adminid || adminid.isEmpty() || !adminid.contentEquals("admin")) {
			returnModel.setCode(CodeContant.ERROR);
			returnModel.setMessage("更新失败");
			return returnModel;
		}
		if (LevelConfigLib.loadLevelConfigFromDb()) {
			returnModel.setCode(CodeContant.OK);
			returnModel.setMessage("更新成功");
		} else {
			returnModel.setCode(CodeContant.ERROR);
			returnModel.setMessage("更新失败");
		}
		return returnModel;
	}
	
	@RequestMapping(value = "/updateGlobal", method = RequestMethod.GET)
	@ResponseBody
	public ReturnModel updateGlobal(HttpServletRequest req) {
		String adminId = req.getParameter("adminid");

		ShardedJedis redis = null;
		try {
			ShardedJedisPool shardPool = RedisBucket.getInstance().getShardPool(RedisContant.RedisNameOther);
			redis = shardPool.getResource();
			List<String> adminListTrusted = redis.lrange(RedisContant.RedisAdminList, 0, -1);
			if (adminListTrusted != null && adminListTrusted.contains(adminId)) {
				String byPipelined = req.getParameter("byPipelined");
				if (byPipelined.equalsIgnoreCase("true")) {
					GlobalConfig.getInstance().setByPipelined(true);
				} else if (byPipelined.equalsIgnoreCase("false")) {
					GlobalConfig.getInstance().setByPipelined(false);
				} else {
					returnModel.setCode(CodeContant.ERROR);
					returnModel.setMessage("更新失败 非法参数");
					return returnModel;
				}
				returnModel.setCode(CodeContant.OK);
				returnModel.setMessage("更新成功");
			} else {
				returnModel.setCode(CodeContant.ERROR);
				returnModel.setMessage("更新失败 不是管理员");
				return returnModel;
			}

		} catch (Exception e) {
			logger.error("<updateGlobal->Exception>" + e.toString());
		} finally {
			if (redis != null) {
				redis.close();
			}
		}
		return returnModel;
	}

	@RequestMapping(value = "/updateES", method = RequestMethod.GET)
	@ResponseBody
	public ReturnModel updateES(HttpServletRequest req) {

		String adminid = req.getParameter("adminid");
		if (null == adminid || adminid.isEmpty() || !adminid.contentEquals("admin")) {
			returnModel.setCode(CodeContant.ERROR);
			returnModel.setMessage("更新失败");
			return returnModel;
		}
		// TODO AUTH!!!
		SearchServiceImpl.updateEsData();
		returnModel.setCode(CodeContant.OK);
		returnModel.setMessage("更新成功");

		return returnModel;
	}
	
	@RequestMapping(value="/abnormal/exit")
	@ResponseBody
	public ReturnModel noticeAbnormalExit(HttpServletRequest req){
		// 用户退房通知admin
		try {
			String adminid = req.getParameter("admin");
			// 获取主播uid
			Integer anchorid = Integer.valueOf(req.getParameter("anchorid"));
			Integer uid = Integer.valueOf(req.getParameter("uid"));
			if (!"abnormal".equalsIgnoreCase(adminid) || anchorid<=0 || uid <=0 ) {
				return null;
			}
			UserBaseInfoModel userBaseInfoModel = userService.getUserbaseInfoByUid(uid, false);
			if (userBaseInfoModel == null) {
				return null;
			}
			
			roomService.exitRoom(uid, anchorid, returnModel);
			ExitRoomCMod exitRoomCMod = new ExitRoomCMod();
			exitRoomCMod.setAvatar(userBaseInfoModel.getHeadimage());
			exitRoomCMod.setLevel(userBaseInfoModel.getUserLevel());
			exitRoomCMod.setNickname(userBaseInfoModel.getNickname());
			exitRoomCMod.setSex(userBaseInfoModel.getSex());
			exitRoomCMod.setUid(userBaseInfoModel.getUid());
			

			String signParams = EncryptUtils.signParams(VarConfigUtils.ServiceSecret,
					"appKey=" + VarConfigUtils.ServiceKey, "msgBody=" + JSONObject.toJSONString(exitRoomCMod),
					"roomOwner=" + String.valueOf(anchorid));

			Unirest.post(UrlConfigLib.getUrl("url").getAdminrpc_publish_room())
					.field("appKey", VarConfigUtils.ServiceKey).field("msgBody", JSONObject.toJSONString(exitRoomCMod))
					.field("roomOwner", String.valueOf(anchorid)).field("sign", signParams).asStringAsync();
		} catch (Exception e) {
			logger.error("noticeAbnormalExit:", e);
		}
		
		return null;
	}

	/**
	 * 修改用户身份
	 *
	 * @param req
	 * @return
	 */
	@RequestMapping(value = "/updateUserIdentity", method = RequestMethod.GET)
	@ResponseBody
	public ReturnModel updateUserIdentity(HttpServletRequest req) {
		String adminid = req.getParameter("adminid");
		if (null == adminid || adminid.isEmpty() || !adminid.contentEquals("admin")) {
			returnModel.setCode(CodeContant.ERROR);
			returnModel.setMessage("更新失败");
			return returnModel;
		}

		if (ParamHandleUtils.isBlank(req, "uid", "identity")) {
			returnModel.setCode(CodeContant.ConParamIsEmptyOrNull);
			returnModel.setMessage("缺少参数或参数为空");
			return returnModel;
		}

		String uid = req.getParameter("uid");
		String identity = req.getParameter("identity");

		UserBaseInfoModel userbaseInfoByUid = userService.getUserbaseInfoByUid(Integer.parseInt(uid), true);

		if (userbaseInfoByUid == null) {
			returnModel.setCode(CodeContant.ERROR);
			returnModel.setMessage("未找到用户");
			return returnModel;
		}

		if (userbaseInfoByUid.getIdentity().toString().equals(identity)) {
			returnModel.setCode(CodeContant.ERROR);
			returnModel.setMessage("用户已经是此状态");
			return returnModel;
		}

		int i = userService.updateUserIdentity(Integer.valueOf(uid), Integer.valueOf(identity));

		if (i != 1) {
			returnModel.setCode(CodeContant.ERROR);
			returnModel.setMessage("更新失败");
			return returnModel;
		}

		UpdateIdentityCMod updateIdentityCMod = new UpdateIdentityCMod();
		updateIdentityCMod.setIdentity(Integer.parseInt(identity));
		userService.pushUserMessage(Integer.valueOf(uid), updateIdentityCMod);

		return returnModel;
	}

	/**
	 * 修改账号状态
	 *
	 * @param req
	 * @return
	 */
	@RequestMapping(value = "/updateUserAccountStatus", method = RequestMethod.GET)
	@ResponseBody
	public ReturnModel updateUserAccountStatus(HttpServletRequest req) {
		String adminid = req.getParameter("adminid");
		if (null == adminid || adminid.isEmpty() || !adminid.contentEquals("admin")) {
			returnModel.setCode(CodeContant.ERROR);
			returnModel.setMessage("更新失败");
			return returnModel;
		}

		if (ParamHandleUtils.isBlank(req, "uid", "status")) {
			returnModel.setCode(CodeContant.ConParamIsEmptyOrNull);
			returnModel.setMessage("缺少参数或参数为空");
			return returnModel;
		}

		String uid = req.getParameter("uid");
		String status = req.getParameter("status");
		UpdateAccountStatusCMod updateAccountStatusCMod = new UpdateAccountStatusCMod();
		updateAccountStatusCMod.setStatus(Integer.parseInt(status));
		userService.pushUserMessage(Integer.valueOf(uid), updateAccountStatusCMod);

		if (liveService.exitLive(Integer.valueOf(uid), returnModel)) {

			String signParams = EncryptUtils.signParams(VarConfigUtils.ServiceSecret,
					"appKey=" + VarConfigUtils.ServiceKey, "msgBody=" + returnModel.getData().toString(),
					"roomOwner=" + uid);

			Unirest.post(UrlConfigLib.getUrl("url").getAdminrpc_end_live())
					.field("appKey", VarConfigUtils.ServiceKey).field("msgBody", returnModel.getData()).field("roomOwner", uid)
					.field("sign", signParams).asStringAsync();

			@SuppressWarnings("unchecked")
			Map<String, Object> map = (Map<String, Object>) returnModel.getData();
			UserBaseInfoModel userBaseInfoModel = new UserBaseInfoModel();
			ExitRoomCMod exitRoomCMod = new ExitRoomCMod();
			exitRoomCMod.setAvatar(userBaseInfoModel.getHeadimage());
			exitRoomCMod.setLevel(userBaseInfoModel.getUserLevel());
			exitRoomCMod.setNickname(userBaseInfoModel.getNickname());
			exitRoomCMod.setSex(userBaseInfoModel.getSex());
			exitRoomCMod.setUid(Integer.valueOf(uid));
			exitRoomCMod.setCreditTotal(Integer.valueOf(map.get("creditTotal").toString()));
			exitRoomCMod.setPersontimes(Integer.valueOf(map.get("persontimes").toString()));
			exitRoomCMod.setRoomLikes(Integer.valueOf(map.get("roomlikes").toString()));
			exitRoomCMod.setTimeslong(Long.valueOf(map.get("timeslong").toString()));

			signParams = EncryptUtils.signParams(VarConfigUtils.ServiceSecret,
					"appKey=" + VarConfigUtils.ServiceKey, "msgBody=" + JSONObject.toJSONString(exitRoomCMod),
					"roomOwner=" + uid);
			
			Unirest.post(UrlConfigLib.getUrl("url").getAdminrpc_publish_room())
					.field("appKey", VarConfigUtils.ServiceKey).field("msgBody", JSONObject.toJSONString(exitRoomCMod)).field("roomOwner", uid).field("sign", signParams)
					.asStringAsync();
		}

		UserAccountModel userAccountByUid = userService.getUserAccountByUid(Integer.parseInt(uid), true);

		if (userAccountByUid == null) {
			returnModel.setCode(CodeContant.ERROR);
			returnModel.setMessage("未找到用户");
			return returnModel;
		}

		if (String.valueOf(userAccountByUid.getStatus()).equals(status)) {
			returnModel.setCode(CodeContant.ERROR);
			returnModel.setMessage("用户已经是此状态");
			return returnModel;
		}

		int i = userService.updateUserAccountStatus(Integer.valueOf(uid), Integer.valueOf(status), returnModel);

		if (i != 1) {
			returnModel.setCode(CodeContant.ERROR);
			returnModel.setMessage("更新失败");
			return returnModel;
		}
		return returnModel;
	}

	/**
	 * 违规警告
	 * 
	 * @param req
	 * @return
	 */
	@RequestMapping(value = "/violationWarning", method = RequestMethod.GET)
	@ResponseBody
	public ReturnModel violationWarning(HttpServletRequest req) {
		String adminid = req.getParameter("adminid");
		if (null == adminid || adminid.isEmpty() || !adminid.contentEquals("admin")) {
			returnModel.setCode(CodeContant.ERROR);
			returnModel.setMessage("更新失败");
			return returnModel;
		}

		if (ParamHandleUtils.isBlank(req, "uid")) {
			returnModel.setCode(CodeContant.ConParamIsEmptyOrNull);
			returnModel.setMessage("缺少参数或参数为空");
			return returnModel;
		}

		userService.pushUserMessage(Integer.valueOf(req.getParameter("uid")), new ViolationWarningCMod());
		return returnModel;
	}

	@RequestMapping(value = "/refreshUserBaseInfo", method = RequestMethod.GET)
	@ResponseBody
	public ReturnModel refreshUserBaseInfo(int uid) {
		UserBaseInfoModel userBaseInfoModel = userService.getUserbaseInfoByUid(uid, true);
		if (userBaseInfoModel == null) {
			returnModel.setCode(400);
		}
		return returnModel;
	}

	@RequestMapping(value = "/refreshUserAccount", method = RequestMethod.GET)
	@ResponseBody
	public ReturnModel refreshUserAccount(int uid) {
		UserAccountModel userAccountModel = userService.getUserAccountByUid(uid, true);
		if (userAccountModel == null) {
			returnModel.setCode(400);
		}
		return returnModel;
	}

	@RequestMapping(value = "/refreshUserAsset", method = RequestMethod.GET)
	@ResponseBody
	public ReturnModel refreshUserAsset(int uid) {
		UserAssetModel userAssetModel = userService.getUserAssetByUid(uid, true);
		if (userAssetModel == null) {
			returnModel.setCode(400);
		}
		return returnModel;
	}

	@RequestMapping(value = "/livinglist", method = RequestMethod.GET)
	@ResponseBody
	public ReturnModel getLivingList() {
		Set<String> setRecommend = OtherRedisService.getInstance().getRecommendRoom(0);
		Set<String> setHot = OtherRedisService.getInstance().getHotRoom(0);
		Set<String> setBase = OtherRedisService.getInstance().getBaseRoom(0);

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("recommend", setRecommend);
		map.put("hot", setHot);
		map.put("base", setBase);
		returnModel.setData(map);
		return returnModel;
	}

	/**
	 * 清楚异常下播数据
	 * 
	 * @param uid
	 * @return
	 */
	@RequestMapping(value = "/clearLiving", method = RequestMethod.GET)
	@ResponseBody
	public int clearLiving() {
		int ires = 0;
		UserBaseInfoModel userBaseInfoModel = null;
		LiveMicTimeModel liveMicTimeModel = null;

		Set<String> RecommendRoom = OtherRedisService.getInstance().getRecommendRoom(0);
		if (RecommendRoom!= null && RecommendRoom.size() > 0) {
			
			for (String uid : RecommendRoom) {
				
				userBaseInfoModel = userService.getUserbaseInfoByUid(Integer.valueOf(uid), false);
				liveMicTimeModel = liveService.getLiveMicInfoByUid(Integer.valueOf(uid), false);
				
				if (!userBaseInfoModel.getLiveStatus()) {
					// 未开播
					if (liveMicTimeModel == null) {
						ires++;
						OtherRedisService.getInstance().delRecommendRoom(Integer.valueOf(uid));
					} else if (System.currentTimeMillis() / 1000 - liveMicTimeModel.getStarttime() > 5 * 60 * 60) {
						ires++;
						OtherRedisService.getInstance().delRecommendRoom(Integer.valueOf(uid));
						liveService.updLiveMicTime(liveMicTimeModel.getUid(), liveMicTimeModel.getStarttime() + 300,
								true, 0, 0, 0, 0, liveMicTimeModel.getId());
					}
				}
			}
		}

		Set<String> setHot = OtherRedisService.getInstance().getHotRoom(0);
		if (setHot.size() > 0) {
			for (String uid : setHot) {
				userBaseInfoModel = userService.getUserbaseInfoByUid(Integer.valueOf(uid), false);
				liveMicTimeModel = liveService.getLiveMicInfoByUid(Integer.valueOf(uid), false);
				if (!userBaseInfoModel.getLiveStatus()) {
					// 未开播
					if (liveMicTimeModel == null) {
						ires++;
						OtherRedisService.getInstance().delHotRoom(Integer.valueOf(uid));
						OtherRedisService.getInstance().delMobileRoom(Integer.valueOf(uid));
					} else if (System.currentTimeMillis() / 1000 - liveMicTimeModel.getStarttime() > 5 * 60 * 60) {
						ires++;
						OtherRedisService.getInstance().delHotRoom(Integer.valueOf(uid));
						liveService.updLiveMicTime(liveMicTimeModel.getUid(), liveMicTimeModel.getStarttime() + 300,
								true, 0, 0, 0, 0, liveMicTimeModel.getId());
					}
				}
			}
		}
		Set<String> setBaseRoom = OtherRedisService.getInstance().getBaseRoom(0);
		if (setBaseRoom.size() > 0) {
			for (String uid : setBaseRoom) {
				userBaseInfoModel = userService.getUserbaseInfoByUid(Integer.valueOf(uid), false);
				liveMicTimeModel = liveService.getLiveMicInfoByUid(Integer.valueOf(uid), false);
				if (!userBaseInfoModel.getLiveStatus()) {
					// 未开播
					if (liveMicTimeModel == null) {
						ires++;
						OtherRedisService.getInstance().delBaseRoom(Integer.valueOf(uid));
					} else if (System.currentTimeMillis() / 1000 - liveMicTimeModel.getStarttime() > 5 * 60 * 60) {
						ires++;
						OtherRedisService.getInstance().delBaseRoom(Integer.valueOf(uid));
						liveService.updLiveMicTime(liveMicTimeModel.getUid(), liveMicTimeModel.getStarttime() + 300,
								true, 0, 0, 0, 0, liveMicTimeModel.getId());
					}
				}
			}
		}
		return ires;
	}

	/**
	 * 禁播 后台控制
	 * 
	 * @param anchoruid
	 * @return
	 */
	@RequestMapping(value = "/ban", method = RequestMethod.GET)
	@ResponseBody
	public ReturnModel banRoomByManage(int anchoruid) {
		liveService.banRoomByManage(anchoruid, returnModel);
		return returnModel;
	}

	/**
	 * 解禁 后台控制
	 * 
	 * @param anchoruid
	 * @return
	 */
	@RequestMapping(value = "/unban", method = RequestMethod.GET)
	@ResponseBody
	public ReturnModel unBanRoomByManage(int anchoruid) {
		liveService.unBanRoomByManage(anchoruid, returnModel);
		return returnModel;
	}

	/**
	 * 封号 后台控制
	 * 
	 * @param uid
	 * @return
	 */
	@RequestMapping(value = "/forbid", method = RequestMethod.GET)
	@ResponseBody
	public ReturnModel forbidAccountByManage(int uid) {
		userService.forbidAccountByManage(uid, returnModel);
		return returnModel;
	}

	/**
	 * 解封 后台控制
	 * 
	 * @param uid
	 * @return
	 */
	@RequestMapping(value = "/unforbid", method = RequestMethod.GET)
	@ResponseBody
	public ReturnModel unForbidAccountByManage(int uid) {
		userService.unForbidAccountByManage(uid, returnModel);
		return returnModel;
	}

	/**
	 * 关闭本场直播 后台控制
	 * 
	 * @param uid
	 * @return
	 */
	@RequestMapping(value = "/closelive", method = RequestMethod.GET)
	@ResponseBody
	public ReturnModel closeLive(int uid) {
		liveService.closeLive(uid, returnModel);
		return returnModel;
	}

	/**
	 * 公告推送
	 * 
	 * @param content
	 */
	public void PushMsgToRoomAll(String content) {

	}

	/**
	 * 获取机器人
	 * 
	 * @return
	 */
	@RequestMapping("/robotlist")
	@ResponseBody
	public Map<String, Object> getRobotList() {
		Map<String, Object> map = new HashMap<String, Object>();
		List<Map<String, Object>> robotList = userService.getRobotList();
		map.put("list", robotList);
		return map;
	}

	/**
	 * 获取最新主播列表
	 * 
	 * @return
	 */
	@RequestMapping("/anchor_list")
	@ResponseBody
	public Map<String, Object> getAnchorList() {
		Map<String, Object> map = new HashMap<String, Object>();
		List<Map<String, Object>> anchorList = userService.getAnchorList();

		map.put("list", anchorList);
		return map;
	}

	/**
	 * 设置用户推荐值和主播评级
	 * 
	 * @param uid
	 * @param recommend
	 *            ＝0 普通主播 ＝1最新主播 ＝2热门主播
	 * @param rq
	 *            人气
	 * @return
	 */
	@RequestMapping("/recommend_rq")
	@ResponseBody
	public ReturnModel setRecommendRQ(int uid, int recommend, int rq,Integer grade) {
		if(grade ==null){
			grade = 0;
		}
		userService.setRecomendRQ(uid, recommend, rq, grade,returnModel);
		return returnModel;
	}

	@RequestMapping("/setapplyiap")
	@ResponseBody
	public ReturnModel setApplyIAP(String ver, int stampAt) {
		String setApplyIAP = OtherRedisService.getInstance().setApplyIAP(ver, stampAt);
		if (!StringUtils.isNotEmpty(setApplyIAP)) {
			returnModel.setCode(201);
			returnModel.setMessage("添加错误");
		}
		return returnModel;
	}

	@RequestMapping("/getapplyiap")
	@ResponseBody
	public ReturnModel getApplyIAP(String ver, int stampAt) {
		String setApplyIAP = OtherRedisService.getInstance().getApplyIAP(ver);
		if (!StringUtils.isNotEmpty(setApplyIAP)) {
			returnModel.setMessage("该版本没有设置");
		}
		return returnModel;
	}

	@RequestMapping("/updateVersion")
	@ResponseBody
	public ReturnModel updateVersion(HttpServletRequest req) {
		String adminid = req.getParameter("adminid");
		if (null == adminid || adminid.isEmpty() || !adminid.equals("admin")) {
			returnModel.setCode(CodeContant.ERROR);
			returnModel.setMessage("update failed,previlage invalid");
			return returnModel;
		}
		String result = VersionConfigLib.readFromRedis();
		if (!result.equals("ok")) {
			returnModel.setMessage("update failed");
		}
		return returnModel;
	}

	@RequestMapping("/updatePushTemplete")
	@ResponseBody
	public ReturnModel updatePushTemplete(HttpServletRequest req) {
		String adminid = req.getParameter("adminid");
		if (null == adminid || adminid.isEmpty() || !adminid.equals("admin")) {
			returnModel.setCode(CodeContant.ERROR);
			returnModel.setMessage("update failed,previlage invalid");
			return returnModel;
		}
		String result = PushTempeleConfigLib.readFromRedis();
		if (!result.equals("ok")) {
			returnModel.setMessage("update failed");
		}
		return returnModel;
	}
	
	/**
	 * 修改开屏
	 * @param req
	 * @return
	 */
	@RequestMapping(value = "/updateOpenScreem", method = RequestMethod.GET)
	@ResponseBody
	public ReturnModel updateOpenScreem(HttpServletRequest req) {
		String adminid = req.getParameter("adminid");
		if (null == adminid || adminid.isEmpty() || !adminid.contentEquals("admin")) {
			returnModel.setCode(CodeContant.ERROR);
			returnModel.setMessage("更新失败");
			return returnModel;
		}
		if (BaseConfigLib.updateOpenScreen()) {
			returnModel.setCode(CodeContant.OK);
			returnModel.setMessage("更新成功");
		} else {
			returnModel.setCode(CodeContant.ERROR);
			returnModel.setMessage("更新失败");
		}
		return returnModel;
	}
	
	/**
	 * 修改直播公告
	 * @param req
	 * @return
	 */
	@RequestMapping(value = "/updateLiveNotice", method = RequestMethod.GET)
	@ResponseBody
	public ReturnModel updateLiveNotice(HttpServletRequest req) {
		String adminid = req.getParameter("adminid");
		if (null == adminid || adminid.isEmpty() || !adminid.contentEquals("admin")) {
			returnModel.setCode(CodeContant.ERROR);
			returnModel.setMessage("更新失败");
			return returnModel;
		}
		if (BaseConfigLib.updateLiveNotice()) {
			returnModel.setCode(CodeContant.OK);
			returnModel.setMessage("更新成功");
		} else {
			returnModel.setCode(CodeContant.ERROR);
			returnModel.setMessage("更新失败");
		}
		return returnModel;
	}
	
	
	/**
	 * 修改幸运礼物中奖概率
	 * @param req
	 * @return
	 */
	@RequestMapping(value = "/updateProbabilitys", method = RequestMethod.GET)
	@ResponseBody
	public ReturnModel updateProbabilitys(HttpServletRequest req) {
		String adminid = req.getParameter("adminid");
		if (null == adminid || adminid.isEmpty() || !adminid.contentEquals("admin")) {
			returnModel.setCode(CodeContant.ERROR);
			returnModel.setMessage("更新失败");
			return returnModel;
		}
		if (BaseConfigLib.updateProbabilitys()) {
			returnModel.setCode(CodeContant.OK);
			returnModel.setMessage("更新成功");
		} else {
			returnModel.setCode(CodeContant.ERROR);
			returnModel.setMessage("更新失败");
		}
		return returnModel;
	}
	/**
	 * 机器人进入直播间
	 *
	 * @param req
	 * @param resp
	 */
	@RequestMapping(value = "/enter_room")
	public void robotintoRoom(HttpServletRequest req, HttpServletResponse resp) {

		try {
			int anchoruid = Integer.valueOf(req.getParameter("dstUid"));
			int srcUid = Integer.valueOf(req.getParameter("srcUid"));
			int robotLevel = Integer.valueOf(req.getParameter("level"));
			String robotNick = req.getParameter("nick");
			String rbotAvatar = req.getParameter("avatar");
			Boolean sex = Boolean.valueOf(req.getParameter("sex"));
			
			UserBaseInfoModel userBaseInfo = userService.getUserbaseInfoByUid(anchoruid, false);
			if (!userBaseInfo.getLiveStatus()) {
				return;
			}
			roomService.robotEnterRoom(srcUid, anchoruid, robotLevel, robotNick, rbotAvatar, sex);
		} catch (Exception e) {
			logger.error("robotintoRoom-exception:", e);
		}
	}

	/**
	 * 机器人退房
	 * @param req
	 * @param resp
	 * @return
	 */
	@RequestMapping(value = "/exit_room")
	public void robotExitRoom(HttpServletRequest req, HttpServletResponse resp){

		int dstUid = Integer.valueOf(req.getParameter("dstUid"));
		int srcUid = Integer.valueOf(req.getParameter("srcUid"));
		
		Long lg = RelationRedisService.getInstance().userExitRoom(srcUid, dstUid);
//		if (lg != null) {
//			UserBaseInfoModel userBaseInfoModel = userService.getUserbaseInfoByUid(dstUid, false);
//			// 热门主播排序
//			if (userBaseInfoModel != null
//					&& (userBaseInfoModel.getRecommend() == 2 || userBaseInfoModel.getRecommend() == 3)) {
//				int roomShowUsers = roomService.getRoomShowUsers(dstUid, userBaseInfoModel.getContrRq());
//				OtherRedisService.getInstance().addRecommendRoom(dstUid, userBaseInfoModel.getRecommend(),
//						roomShowUsers, 1);
//			}
//		}
	}
	
	/**
	 * 官方私信推送告知
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="/sysnotice")
	public void SysNotice(HttpServletRequest request,HttpServletResponse response){

		String msg = request.getParameter("sysMsgPush");
		if ("allUsers".equals(msg)) {
			ChatMessageUtil.sendSysMsg();
		}
	}
	
	/**
	 * 重新加载促销 配置
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="/updGiftPromotion")
	@ResponseBody
	public ReturnModel updGiftPromotion(HttpServletRequest request,HttpServletResponse response){
		String adminid = request.getParameter("adminid");
		if (null == adminid || adminid.isEmpty() || !adminid.contentEquals("admin")) {
			returnModel.setCode(CodeContant.ERROR);
			returnModel.setMessage("更新失败");
			return returnModel;
		}
		if (GiftPromotionConfigLib.loadGiftPromotionConfigFromDb()) {
			returnModel.setCode(CodeContant.OK);
			returnModel.setMessage("更新成功");
		} else {
			returnModel.setCode(CodeContant.ERROR);
			returnModel.setMessage("更新失败");
		}
		return returnModel;
	}
	
	/**
	 * 重新加载扶持号配置
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="/updUnionSupport")
	@ResponseBody
	public ReturnModel updUnionSupport(HttpServletRequest request,HttpServletResponse response){
		String adminid = request.getParameter("adminid");
		if (null == adminid || adminid.isEmpty() || !adminid.contentEquals("admin")) {
			returnModel.setCode(CodeContant.ERROR);
			returnModel.setMessage("更新失败");
			return returnModel;
		}
		if (GiftPromotionConfigLib.loadSupportForRedis()) {
			returnModel.setCode(CodeContant.OK);
			returnModel.setMessage("更新成功");
		} else {
			returnModel.setCode(CodeContant.ERROR);
			returnModel.setMessage("更新失败");
		}
		return returnModel;
	}
	
	/**
	 * 后台 提现审核通过 接口
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/verifyWithdraw")
	@ResponseBody
	public ReturnModel verifyWithdraw(HttpServletRequest request,HttpServletResponse response){
		
		String withdraw = request.getParameter("withdraw");
		if (null == withdraw || withdraw.isEmpty() || !withdraw.contentEquals("withdrwa")) {
			returnModel.setCode(CodeContant.ERROR);
			returnModel.setMessage("参数不对001");
			return returnModel;
		}

		int id = Integer.valueOf(request.getParameter("id"));
		String billno = request.getParameter("billno");
		int adminid = Integer.valueOf(request.getParameter("adminid"));
		
		if (StringUtils.isEmpty(billno) || id <= 0 || adminid <= 0) {
			returnModel.setCode(CodeContant.ERROR);
			returnModel.setMessage("参数不对002");
			return returnModel;
		}
		
		payService.verifyWithdraw(id, billno,adminid,returnModel);
		return returnModel;
	}
	
	/**
	 * 后台添加声援值
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/addCredit",method = RequestMethod.POST)
	@ResponseBody
	public ReturnModel addCreditByAdmin(HttpServletRequest request,HttpServletResponse response){
		
		try {
			String addCredit = request.getParameter("addCredit");
			if (null == addCredit || addCredit.isEmpty() || !addCredit.contentEquals("crdeitdda")) {
				returnModel.setCode(CodeContant.ERROR);
				returnModel.setMessage("参数不对001");
				return returnModel;
			}
			
			int uid = Integer.valueOf(request.getParameter("uid"));
			int credit = Integer.valueOf(request.getParameter("credit"));
			
			roomService.addCreditByAdmin(uid, credit);
			
		} catch (Exception e) {
			returnModel.setCode(CodeContant.ERROR);
			returnModel.setMessage("except:" + e.getMessage());
		}
		
		return returnModel;
	}
	
	
	/**
	 * 添加VIP
	 * @param dstuid
	 * @param gid
	 * @param count 单位/天
	 * @param returnModel
	 * @return
	 */
	@RequestMapping("/addVip")
	@ResponseBody
	ReturnModel addVip(Integer dstuid,Integer gid,Integer count, HttpServletRequest request, HttpServletResponse response){
		if (null == dstuid || gid == null || count == null) {
			returnModel.setCode(CodeContant.ERROR);
			returnModel.setMessage("参数不对001");
			return returnModel;
		}
		returnModel = adminService.addVip(dstuid, gid, count, returnModel);
		return returnModel;
	}
	
	/**
	 * 添加守护
	 * @param srcuid 用户id
	 * @param dstuid 房间id
	 * @param gid
	 * @param count 单位/天
	 * @param returnModel
	 * @return
	 */
	@RequestMapping("/addGuard")
	@ResponseBody
	ReturnModel addGuard(Integer srcuid, Integer dstuid, Integer gid, Integer count, HttpServletRequest request, HttpServletResponse response){
		if (srcuid == null || null == dstuid || gid == null || count == null) {
			returnModel.setCode(CodeContant.ERROR);
			returnModel.setMessage("参数不对001");
			return returnModel;
		}
		returnModel = adminService.addGuard(srcuid, dstuid, gid, count, returnModel);
		return returnModel;
	}
	
	/**
	 * 添加座驾
	 * @param dstuid
	 * @param gid
	 * @param count 单位/天
	 * @param returnModel
	 * @return
	 */
	@RequestMapping("/addCar")
	@ResponseBody
	ReturnModel addCar(Integer dstuid,Integer gid,Integer count, HttpServletRequest request, HttpServletResponse response){
		if (null == dstuid || gid == null || count == null) {
			returnModel.setCode(CodeContant.ERROR);
			returnModel.setMessage("参数不对001");
			return returnModel;
		}
		returnModel = adminService.addCar(dstuid, gid, count, returnModel);
		return returnModel;
	}
	
	/**
	 * 添加经验
	 * @param dstuid
	 * @param exp 任务经验值
	 * @param wealths 用户财富值
	 * @param returnModel
	 * @return
	 */
	@RequestMapping("/addExp")
	@ResponseBody
	ReturnModel addExp(Integer dstuid,Integer exp,HttpServletRequest request, HttpServletResponse response){
		if (null == dstuid) {
			returnModel.setCode(CodeContant.ERROR);
			returnModel.setMessage("参数不对001");
			return returnModel;
		}
		returnModel = adminService.addExp(dstuid, exp, returnModel);
		return returnModel;
	}
	
	/**
	 * 添加背包礼物
	 * @param dstuid
	 * @param gid
	 * @param count
	 * @param returnModel
	 * @return
	 */
	@RequestMapping("/addGift")
	@ResponseBody
	ReturnModel addGift(Integer dstuid, Integer gid, Integer count, HttpServletRequest request, HttpServletResponse response){
		if (null == dstuid || gid == null || count == null) {
			returnModel.setCode(CodeContant.ERROR);
			returnModel.setMessage("参数不对001");
			return returnModel;
		}
		returnModel = adminService.addGift(dstuid, gid, count, returnModel);
		return returnModel;
	}
	
	@RequestMapping(value = "/sendMsg",method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> sendMsgByMobile(HttpServletRequest request,HttpServletResponse response){
		Map<String, Object> mapResult = new HashMap<String,Object>();
		String strCheck = request.getParameter("msg");
		if (!"smesngd".equals(strCheck)) {
			mapResult.put("success", 201);
			mapResult.put("msg", "参数错误201");
		}else {
			String mobileNum = request.getParameter("phone");
			String content = request.getParameter("content");
			Boolean sendMobileCode = userService.sendMobileCode(mobileNum, content);
			if (sendMobileCode) {
				mapResult.put("success", 200);
			}else {
				mapResult.put("success", 202);
				mapResult.put("msg", "短信发送失败");
			}
		}
		return mapResult;
	}
	
	@RequestMapping(value = "/taskProcessForce")
	@ResponseBody
	public Map<String, Object> taskProcess(HttpServletRequest request,HttpServletResponse response){
		String secret = request.getParameter("secret");
		Map<String, Object> mapResult = new HashMap<String,Object>(){{
			put("code", 300);
			put("msg", "ok");
		}};
		if(secret.equals("jacklovekevinbaby")){
			String uid = request.getParameter("uid");
			String tid = request.getParameter("tid");
			TaskConfigModel taskConfigModel = TaskConfigLib.getInstance().getTaskConfigFor(Integer.parseInt(tid));
			if(taskConfigModel != null){
				boolean hasSomeTaskForInAccepetedBucket = TaskService.getInstance().hasSomeTaskFor(
						uid,taskConfigModel.getType(),taskConfigModel.getState(), taskConfigModel.getId());
				if (hasSomeTaskForInAccepetedBucket) {
					TaskService.getInstance().taskProcess(Integer.parseInt(uid), taskConfigModel.getWhat(), 1);
					mapResult.put("code", 200);
					mapResult.put("msg", "ok");
				}else{
					mapResult.put("code", 204);
					mapResult.put("msg", "可接任务桶里没有这个任务");
				}
			}else{
				mapResult.put("code", 201);
				mapResult.put("msg", "参数错误,没有相关的任务");
			}
		}else{
			mapResult.put("code", 202);
			mapResult.put("msg", "无话可说了");
		}

		return mapResult;
	}
	
	/**
	 * 添加金币
	 */
	@RequestMapping(value = "/modifyMoney",method = RequestMethod.POST)
	@ResponseBody
	public ReturnModel modifyMoney(HttpServletRequest request,HttpServletResponse response){
		try{
			String adminid = request.getParameter("adminid");
			if (null == adminid || adminid.isEmpty() || !adminid.contentEquals("admin")) {
				returnModel.setCode(CodeContant.ERROR);
				returnModel.setMessage("更新失败");
				return returnModel;
			}

			String strUid = request.getParameter("uid");	//要被操作的uid
			String strZhutou = request.getParameter("zhutou");//要增加|消费的资产数量
			String strCredit = request.getParameter("credit");//要增加的声援值（type=0此参数无效）
			String strType = request.getParameter("type");//1增加 消费0
			String desc = request.getParameter("desc");//备注
			
			int uid = StringUtils.isEmpty(strUid) ? 0 : Integer.valueOf(strUid);
			int zhutou = StringUtils.isEmpty(strZhutou) ? 0 : Integer.valueOf(strZhutou);
			int credit = StringUtils.isEmpty(strCredit) ? 0 : Integer.valueOf(strCredit);
			int type = StringUtils.isEmpty(strType) ? 0 : Integer.valueOf(strType);

			returnModel = adminService.modifyMoney(uid, zhutou, credit,type,desc,returnModel);
		}catch(Exception ex){
			logger.error("modifyMoney-Exception:",ex);
			returnModel.setCode(501);
			returnModel.setMessage(ex.getMessage());
		}
		logger.debug("execute modifyMoney: "+JsonUtil.toJson(returnModel));
		return returnModel;
	}
	
	/**
	 * 照片删除
	 */
	@RequestMapping(value = "/delPhoto",method = RequestMethod.POST)
	@ResponseBody
	public ReturnModel delPhoto( HttpServletRequest request, HttpServletResponse response) {
		String adminid = request.getParameter("adminid");
		if (null == adminid || adminid.isEmpty() || !adminid.contentEquals("admin")) {
			returnModel.setCode(CodeContant.ERROR);
			returnModel.setMessage("更新失败");
			return returnModel;
		}
		String strUid = request.getParameter("uid");	
		int uid = StringUtils.isEmpty(strUid) ? 0 : Integer.valueOf(strUid);
		String strPid = request.getParameter("pid");	
		int pid = StringUtils.isEmpty(strUid) ? 0 : Integer.valueOf(strPid);
		UserAlbumModel userAlbumModel = userAlbumService.getUserAlbumById(pid);
		if (userAlbumModel != null) {
			Auth auth = Auth.create(BaseContant.accessKey, BaseContant.secretKey);
			BucketManager bucketManager = new BucketManager(auth);
			try {
				int rsc = userAlbumService.delPhoto(pid, uid);
				if (rsc <= 0) {
					returnModel.setCode(CodeContant.delPhotoError);
					returnModel.setMessage("删除失败");
				} else {
					bucketManager.delete(Constant.qn_photo_bucket, userAlbumModel.getFileName());
				}
				userAlbumService.getUserAlbumDate(uid, true);
			} catch (QiniuException e) {
				returnModel.setCode(CodeContant.delPhotoError);
				returnModel.setMessage("删除失败");
				logger.error(e);
			}
		}
		return returnModel;
	}
	
	/**
	 * 修改用户基本信息
	 */
	@RequestMapping(value = "/modifyUserInfoByUid",method = RequestMethod.POST)
	@ResponseBody
	public ReturnModel modifyUserInfoByUid( HttpServletRequest request, HttpServletResponse response) {
		try {
			String adminid = request.getParameter("adminid");
			if (StringUtils.isBlank(adminid)||!adminid.contentEquals("admin")) {
				returnModel.setCode(CodeContant.ERROR);
				returnModel.setMessage("更新失败");
				return returnModel;
			}
			
			if (ParamHandleUtils.isBlank(request, "uid", "nickname","sex","headimage", "signature")) {
				returnModel.setCode(CodeContant.ConParamIsEmptyOrNull);
				returnModel.setMessage("缺少参数或参数为空");
				return returnModel;
			}
			
			Integer uid = Integer.parseInt(request.getParameter("uid"));
			String nickname = request.getParameter("nickname");
			//0 女  1 男
			int sex = Integer.valueOf(request.getParameter("sex"));
			String headimage = request.getParameter("headimage");
			String livimage = request.getParameter("livimage")==null?"":request.getParameter("livimage");
			String hobby = request.getParameter("hobby")==null?"":request.getParameter("hobby");
			String signature = request.getParameter("signature");
			
			int nicknameLen = com.mpig.api.utils.StringUtils.length(nickname);
			if (nicknameLen > 16) {
				returnModel.setCode(CodeContant.nicknameLen);
				returnModel.setMessage("昵称超长");
				return returnModel;
			}
			int signatureLen = com.mpig.api.utils.StringUtils.length(signature);
			if (signatureLen  > 74) {
				returnModel.setCode(CodeContant.constellationIsAuth);
				returnModel.setMessage("签名超长");
				return returnModel;
			}
			int hobbyLen = com.mpig.api.utils.StringUtils.length(hobby);
			if (hobbyLen  > 74) {
				returnModel.setCode(CodeContant.constellationIsAuth);
				returnModel.setMessage("爱好超长");
				return returnModel;
			}
			
			int i = userService.updUserBaseInfoByUid(uid, nickname,sex == 1 ? true : false,signature,hobby,headimage,livimage);
			if(i==0) {
				returnModel.setCode(CodeContant.ERROR);
				returnModel.setMessage("更新失败");
				return returnModel;
			}
			SearchModel data = new SearchModel();
			data.setNickname(nickname);
			data.setUid(uid);
			data.setNumb(uid.toString());
			data.setAvatar(headimage);
			data.setSex(sex == 1 ? true : false);
			data.setSlogan(signature);
			searchService.updateUsersAnsyc(data);
			return returnModel;
		} catch (Exception e) {
			logger.error(e);
			returnModel.setCode(CodeContant.ERROR);
			returnModel.setMessage("删除失败");
			return returnModel;
		}
	}
	/**
	 * 修改用户密码
	 */
	@RequestMapping(value = "/updateUserPassword",method = RequestMethod.POST)
	@ResponseBody
	public ReturnModel updateUserPassword( HttpServletRequest request, HttpServletResponse response) {
		try {
			String adminid = request.getParameter("adminid");
			if (StringUtils.isBlank(adminid)||!adminid.contentEquals("admin")) {
				returnModel.setCode(CodeContant.ERROR);
				returnModel.setMessage("更新失败");
				return returnModel;
			}
			
			if (ParamHandleUtils.isBlank(request, "uid", "passWord")) {
				returnModel.setCode(CodeContant.ConParamIsEmptyOrNull);
				returnModel.setMessage("缺少参数或参数为空");
				return returnModel;
			}
			Integer uid = Integer.parseInt(request.getParameter("uid"));
			String passWord = request.getParameter("passWord").toUpperCase();

			int ires = userService.updUserPwordByUid(uid, passWord);
			if (ires == 0) {
				returnModel.setCode(CodeContant.MobileCodeErr);
				returnModel.setMessage("验证码不正确");
				return returnModel;
			}
			return returnModel;
		} catch (Exception e) {
			logger.error(e);
			returnModel.setCode(CodeContant.ERROR);
			returnModel.setMessage("删除失败");
			return returnModel;
		}
	}

}