package com.mpig.api.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.json.JSONException;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mpig.api.async.AsyncManager;
import com.mpig.api.async.IAsyncTask;
import com.mpig.api.dictionary.VideoLineConfig;
import com.mpig.api.dictionary.lib.UrlConfigLib;
import com.mpig.api.dictionary.lib.VideoLineConfigLib;
import com.mpig.api.model.LiveMicTimeModel;
import com.mpig.api.model.RoomGameInfoModel;
import com.mpig.api.model.UserBaseInfoModel;
import com.mpig.api.model.UserGuardInfoModel;
import com.mpig.api.modelcomet.BeginLiveMulti;
import com.mpig.api.modelcomet.ExitRoomCMod;
import com.mpig.api.modelcomet.KickCMod;
import com.mpig.api.modelcomet.ManageRoomCMod;
import com.mpig.api.modelcomet.SilentCMod;
import com.mpig.api.redis.service.OtherRedisService;
import com.mpig.api.redis.service.RelationRedisService;
import com.mpig.api.redis.service.UserRedisService;
import com.mpig.api.service.ILiveService;
import com.mpig.api.service.IRedEnvelope;
import com.mpig.api.service.IUserGuardInfoService;
import com.mpig.api.service.IUserService;
import com.mpig.api.service.impl.SensitiveWordsService;
import com.mpig.api.utils.CodeContant;
import com.mpig.api.utils.EncryptUtils;
import com.mpig.api.utils.GameServerUtil;
import com.mpig.api.utils.ParamHandleUtils;
import com.mpig.api.utils.RedisContant;
import com.mpig.api.utils.ValueaddServiceUtil;
import com.mpig.api.utils.VarConfigUtils;
import com.qiniu.pili.QnStreamUtil;
import com.qiniu.pili.QnStreamUtilv1;

@Controller
@Scope("prototype")
@RequestMapping("/live")
public class LiveController extends BaseController {
	private static final Logger logger = Logger.getLogger(LiveController.class);

	@Resource
	private ILiveService liveService;
	@Resource
	private IUserService userService;
	@Resource
	private IRedEnvelope redEnvelope;
	@Resource
	private IUserGuardInfoService userGuardInfoService;

	private Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");

	@RequestMapping(value = "badHeart")
	public void badHeart(HttpServletRequest req, final HttpServletResponse resp) {
		// TODO AUTH
		String livingUid = req.getParameter("uid");
		String micId = req.getParameter("micid");

		logger.debug("badHeart 心跳超时 房间主播uid＝" + livingUid + " 麦时id＝" + micId);

		if (null == livingUid || null == micId || livingUid.isEmpty() || micId.isEmpty()) {
			returnModel.setCode(CodeContant.LIVECLOSE);
			returnModel.setMessage("停播失败");
			writeJson(resp, returnModel);
			return;
		}
		if (!liveService.exitLive(Integer.valueOf(livingUid), returnModel)) {
			returnModel.setCode(CodeContant.LIVECLOSE);
			returnModel.setMessage("停播失败");
			logger.info("badHeart exitLive failed");
		} else {
			// 下播通知admin
			try {

				String signParams = EncryptUtils.signParams(VarConfigUtils.ServiceSecret,
						"appKey=" + VarConfigUtils.ServiceKey, "msgBody=" + returnModel.getData().toString(),
						"roomOwner=" + livingUid);

				Unirest.post(UrlConfigLib.getUrl("url").getAdminrpc_end_live())
						.field("appKey", VarConfigUtils.ServiceKey).field("msgBody", returnModel.getData())
						.field("roomOwner", livingUid).field("sign", signParams).asJson();

				UserBaseInfoModel userBaseInfoModel = userService.getUserbaseInfoByUid(Integer.valueOf(livingUid),
						false);
				@SuppressWarnings("unchecked")
				Map<String, Object> map = (Map<String, Object>) returnModel.getData();

				ExitRoomCMod exitRoomCMod = new ExitRoomCMod();
				exitRoomCMod.setAvatar(userBaseInfoModel.getHeadimage());
				exitRoomCMod.setLevel(userBaseInfoModel.getUserLevel());
				exitRoomCMod.setNickname(userBaseInfoModel.getNickname());
				exitRoomCMod.setSex(userBaseInfoModel.getSex());
				exitRoomCMod.setUid(Integer.valueOf(livingUid));
				exitRoomCMod.setCreditTotal(Integer.valueOf(map.get("creditTotal").toString()));
				exitRoomCMod.setPersontimes(Integer.valueOf(map.get("persontimes").toString()));
				exitRoomCMod.setRoomLikes(Integer.valueOf(map.get("roomlikes").toString()));
				exitRoomCMod.setTimeslong(Long.valueOf(map.get("timeslong").toString()));

				signParams = EncryptUtils.signParams(VarConfigUtils.ServiceSecret,
						"appKey=" + VarConfigUtils.ServiceKey, "msgBody=" + returnModel.getData().toString(),
						"roomOwner=" + livingUid);
				Unirest.post(UrlConfigLib.getUrl("url").getAdminrpc_publish_room())
						.field("appKey", VarConfigUtils.ServiceKey)
						.field("msgBody", JSONObject.toJSONString(exitRoomCMod)).field("roomOwner", livingUid)
						.field("sign", signParams).asStringAsync();
			} catch (Exception e) {
				logger.error("<AdminRpc_END_LIVE->Exception>" + e.toString());
			}
		}

		writeJson(resp, returnModel);
		return;
	}

	/**
	 * 获取主播开播信息
	 *
	 * @param req
	 * @param resp
	 */
	@RequestMapping(value = "liveinfo", method = RequestMethod.GET)
	public void liveingBefore(HttpServletRequest req, HttpServletResponse resp) {

		if (ParamHandleUtils.isBlank(req, "ncode", "os", "imei", "reqtime", "token")) {
			returnModel.setCode(CodeContant.ConParamIsEmptyOrNull);
			returnModel.setMessage("缺少参数或参数为空");
			writeJson(resp, returnModel);
			return;
		}
		if (!ParamHandleUtils.isInt(req, "os", "reqtime")) {
			returnModel.setCode(CodeContant.ConParamTypeIsErr);
			returnModel.setMessage("参数类型错误");
			writeJson(resp, returnModel);
			return;
		}
		authToken(req, true);
		if (returnModel.getCode() != CodeContant.OK) {
			// 验证失败
			writeJson(resp, returnModel);
			return;
		}
		String token = req.getParameter("token");
		int uid = authService.decryptToken(token, returnModel);
		if (uid <= 0) {
			writeJson(resp, returnModel);
			return;
		}
		
		UserBaseInfoModel userBaseInfoModel = userService.getUserbaseInfoByUid(uid, false);
		if (userBaseInfoModel == null) {
			returnModel.setCode(CodeContant.ACCOUNTEXCEPT);
			returnModel.setMessage("帐号异常，请重新登录");
			writeJson(resp, returnModel);
			return; 
		} 
		if (userBaseInfoModel.getIdentity() == 2) {
			returnModel.setCode(CodeContant.BaseInfo_identity);
			returnModel.setMessage("您只能观看，不能开播，如有疑问，请联系客服");
			writeJson(resp, returnModel);
			return;
		}
		
		if(!UserRedisService.getInstance().exists(RedisContant.AllAuth+uid)){
			returnModel.setCode(CodeContant.BaseInfo_identity);
			returnModel.setMessage("您只能观看，不能开播，请先完成实名认证");
			writeJson(resp, returnModel);
			return;
		}
		String userAllAuthStr = UserRedisService.getInstance().get(RedisContant.AllAuth+uid);
		JSONObject userAllAuthJson = JSONObject.parseObject(userAllAuthStr);
		Integer realnameAuth = userAllAuthJson.getInteger("realnameAuth");
		if(realnameAuth == null || realnameAuth.intValue() != 3) {
			returnModel.setCode(CodeContant.BaseInfo_identity);
			returnModel.setMessage("您只能观看，不能开播，请先完成实名认证");
			writeJson(resp, returnModel);
			return;
		}
		
		// 默认网宿 videoline 之前的代码默认 2   
		int videoline = userBaseInfoModel.getVideoline();
		
		Map<String, Object> map = liveService.getVideoConfig(uid, uid, videoline);
		if (map.size() <= 0) {
			returnModel.setCode(CodeContant.LIVESERVICECONFIG);
			returnModel.setMessage("获取服务器配置出错");
			writeJson(resp, returnModel);
			return;
		} 
		map.put("uid", uid);
		String roomToken = QnStreamUtil.getInstance().getRoomToken(uid,uid);
		if(StringUtils.isNotBlank(roomToken)){
			map.put("roomToken", roomToken);
		}
		String qnKey = null;
		try{
			qnKey = OtherRedisService.getInstance().getUidQnKey(String.valueOf(uid));
			if(qnKey!=null) {
				//清理临时key
				OtherRedisService.getInstance().delUidQnKeyLimitOneDay(String.valueOf(uid));
				logger.debug("qn update fix beforelive 1... uid: " + uid + ":key:"+qnKey);
				qnKey = QnStreamUtilv1.getInstance().updateStreamKeyV1(String.valueOf(uid), qnKey,false);
				logger.debug("qn update fix beforelive 2... uid: " + uid + ":key:"+qnKey);	
			}else {
				qnKey = OtherRedisService.getInstance().getUidQnKeyLimitOneDay(String.valueOf(uid));
				if(null == qnKey){
					//过期 更新动态key
					qnKey = QnStreamUtilv1.getInstance().updateStreamKeyV1(String.valueOf(uid), qnKey,false);
					OtherRedisService.getInstance().setUidQnKeyLimitOneDay(String.valueOf(uid),qnKey);
				}
			}
			map.put("key", qnKey);
			returnModel.setData(map);
			writeJson(resp, returnModel);
		}catch (Exception e){
			logger.error("获取qnKey异常",e);
			returnModel.setCode(CodeContant.LIVESERVICECONFIG);
			returnModel.setMessage("获取服务器配置出错");
			writeJson(resp, returnModel);
		}
	}

	/**
	 * push 异步
	 * @author zyl
	 * @time 2017-1-20
	 */
	private class PushTask implements IAsyncTask{
		private int uid;
		private UserBaseInfoModel anchorModel;
		
		public PushTask(int uid, UserBaseInfoModel anchorModel){
			this.uid = uid;
			this.anchorModel = anchorModel;
		}
		@Override
		public void runAsync() {
			try{
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("uid", uid);
				map.put("anchorLevel", anchorModel.getAnchorLevel());
				map.put("nickname", anchorModel.getNickname());
				map.put("verified", anchorModel.isVerified());
				map.put("userLevel", anchorModel.getUserLevel());
				map.put("recommend", anchorModel.getRecommend());
				map.put("opentime", anchorModel.getOpentime());
				map.put("contrRq", anchorModel.getContrRq());

				String signParams = EncryptUtils.signParams(VarConfigUtils.ServiceSecret,
						"appKey=" + VarConfigUtils.ServiceKey, "detail=" + JSONObject.toJSONString(map),
						"roomOwner=" + String.valueOf(uid));
				try {
					Unirest.post(UrlConfigLib.getUrl("url").getAdminrpc_begin_live())
							.field("appKey", VarConfigUtils.ServiceKey).field("detail", JSONObject.toJSONString(map))
							.field("roomOwner", String.valueOf(uid)).field("sign", signParams).asJson();
				} catch (UnirestException e) {
					logger.error("PushTask:", e);
				}
			}catch(Exception ex){
				logger.error("PushTask:", ex);
			}
		}
		@Override
		public void afterOk() {
			
		}
		@Override
		public void afterError(Exception e) {
			
		}
		@Override
		public String getName() {
			return "PassFeedTask";
		}
	}
	/**
	 * 开播
	 *
	 * @param req
	 * @param resp
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "live", method = RequestMethod.GET)
	public void Liveing(HttpServletRequest req, final HttpServletResponse resp) throws JSONException {

		if (ParamHandleUtils.isBlank(req, "ncode", "os", "imei", "reqtime", "token", "type")) {
			returnModel.setCode(CodeContant.ConParamIsEmptyOrNull);
			returnModel.setMessage("缺少参数或参数为空");
			writeJson(resp, returnModel);
			return;
		}
		if (!ParamHandleUtils.isInt(req, "os", "reqtime")) {
			returnModel.setCode(CodeContant.ConParamTypeIsErr);
			returnModel.setMessage("参数类型错误");
			writeJson(resp, returnModel);
			return;
		}

		// 验证auth及token
		authToken(req, true);
		if (returnModel.getCode() != CodeContant.OK) {
			// 验证失败
			writeJson(resp, returnModel);
			return;
		}

		String token = req.getParameter("token");
		int uid = authService.decryptToken(token, returnModel);
		if (uid <= 0) {
			writeJson(resp, returnModel);
			return;
		}
		String type = req.getParameter("type");
		if ("open".equalsIgnoreCase(type)) {
			if (OtherRedisService.getInstance().getLivingClick(uid)) {
				returnModel.setCode(CodeContant.LiveAgain);
				returnModel.setMessage("频繁开播");
				writeJson(resp, returnModel);
				return;
			}
			String slogan = req.getParameter("slogan");
			if (slogan == null) {
				slogan = "";
			} else {
				String string = SensitiveWordsService.getInstance().replaceSensitiveWords(slogan, slogan);
				if ("failed".equalsIgnoreCase(string)) {

				}
			}

			String strOs = req.getParameter("os");
			
			String os = "web";
			if ("1".equals(strOs)) {
				os = "android";
			}else if ("2".equals(strOs)) {
				os = "ios";
			}

			String province = req.getParameter("province");
			if (province == null) {
				province = "";
			}
			String city = req.getParameter("city");
			if (city == null) {
				city = "";
			}
			if (slogan.length() > 40) {
				returnModel.setCode(CodeContant.STRINGLENTH);
				returnModel.setMessage("slogan不能超过20个字");
			} else {
				// 通知admin
				HttpResponse<JsonNode> respone = null;
				UserBaseInfoModel anchorModel = userService.getUserbaseInfoByUid(uid, false);
				
				if (anchorModel == null) {
					returnModel.setCode(CodeContant.ACCOUNTEXCEPT);
					returnModel.setMessage("帐号异常，请重新登录");
					writeJson(resp, returnModel);
					return;
				} else if (anchorModel.getIdentity() == 2) {
					returnModel.setCode(CodeContant.BaseInfo_identity);
					returnModel.setMessage("您只能观看，不能开播，如有疑问，请联系客服");
					writeJson(resp, returnModel);
					return;
				}

				try{
					AsyncManager.getInstance().execute(new PushTask(uid, anchorModel));
				}catch (Exception ex){
					logger.error("PushTask>>>",ex);
				}

//				if (null == respone) {
//					logger.debug("debug->Liveing->open->getAdminrpc_begin_live: respone=null");
//					returnModel.setCode(CodeContant.LIVEOPEN);
//					returnModel.setMessage("开播失败null");
//					writeJson(resp, returnModel);
//					return;
//				}
//				// get respone
//				org.json.JSONObject jsonObjectRt = respone.getBody().getObject();
//				if (null == jsonObjectRt || false == jsonObjectRt.has("ok") || false == jsonObjectRt.getBoolean("ok")) {
//
//					logger.debug(
//							"debug->Liveing->open->getAdminrpc_begin_live: jsonObjectRt=" + jsonObjectRt.toString());
//					returnModel.setCode(CodeContant.LIVEOPEN);
//					returnModel.setMessage("开播失败null");
//					writeJson(resp, returnModel);
//					return;
//				}
				if (!liveService.startLive(os, uid, slogan, province, city)) {
					logger.debug("debug->Liveing->open->startLive open is err uid=" + uid);
					returnModel.setCode(CodeContant.LIVEOPEN);
					returnModel.setMessage("开播失败");
					writeJson(resp, returnModel);
					return;
				}
				OtherRedisService.getInstance().addLivingClick(uid);
				//  推送给粉丝主播开播了 TEST
				Set<String> setFans = RelationRedisService.getInstance().getFans(uid, 0);
				if (0 != setFans.size()) {
					StringBuffer fans = new StringBuffer();
					for (String stringUid : setFans) {
//						if (!RelationRedisService.getInstance().getPushFollow(Integer.valueOf(stringUid), uid)) {
//							continue;
//						}
						fans.append(stringUid + "-");
					}
					if (fans.length() > 0) {
						fans.deleteCharAt(fans.length() - 1);
						BeginLiveMulti msgBody = new BeginLiveMulti();
						UserBaseInfoModel userinfo = userService.getUserbaseInfoByUid(uid, false);
						VideoLineConfig videoLineConfig = VideoLineConfigLib.getForNormal(2);
						msgBody.setUid(uid);
						msgBody.setVideoDomain(videoLineConfig.getDomainPrefix());
						msgBody.setLevel(userinfo.getAnchorLevel());
						msgBody.setNickname(userinfo.getNickname());
						msgBody.setSex(userinfo.getSex());
						msgBody.setAvatar(userinfo.getHeadimage());
						msgBody.setMsg("您的关注：" + userinfo.getNickname() + "正在直播，邀您加入");
						String jsonString = JSONObject.toJSONString(msgBody);

						String signParams = EncryptUtils.signParams(VarConfigUtils.ServiceSecret,
								"appKey=" + VarConfigUtils.ServiceKey, "msgBody=" + jsonString,
								"users=" + fans.toString());
						Unirest.post(UrlConfigLib.getUrl("url").getAdminrpc_publish_live())
								.field("appKey", VarConfigUtils.ServiceKey).field("msgBody", jsonString)
								.field("users", fans.toString()).field("sign", signParams).asJsonAsync();
					}
				}
				// 成功!!
				Map<String, String> map = liveService.getManagelistOfAnchor(uid, false);
				List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

				UserBaseInfoModel userBaseInfoModel = null;
				for (String manage : map.keySet()) {
					userBaseInfoModel = userService.getUserbaseInfoByUid(Integer.valueOf(manage), false);
					if (userBaseInfoModel != null) {
						Map<String, Object> temp = new HashMap<String, Object>();
						temp.put("uid", manage);
						temp.put("nickname", userBaseInfoModel.getNickname());
						temp.put("headimage", userBaseInfoModel.getHeadimage());
						temp.put("userLevel", userBaseInfoModel.getUserLevel());
						temp.put("verified", userBaseInfoModel.isVerified());
						list.add(temp);
					}
				}
				//插入房间所有的守护信息
				List<UserGuardInfoModel> guardList = userGuardInfoService.selUserAllGardInfoByRoomId(uid);
				UserRedisService.getInstance().set(RedisContant.roomAllGuard+uid, JSONArray.toJSONString(guardList));
				
				HashMap<String, Object> retMap = new HashMap<String, Object>();
				retMap.put("list", list);
				retMap.put("rq", UserRedisService.getInstance().getUserAllRankRqScore(String.valueOf(uid)));
				String redisStr = OtherRedisService.getInstance().getRoomEndTime(uid);
				if(!StringUtils.isBlank(redisStr)) {
					retMap.put("micId", redisStr.split(",")[0]);
				}
				String gameId = req.getParameter("gameId");
				if(StringUtils.isBlank(gameId)||"0".equals(gameId)) {
					retMap.put("gameStatus", 0);
					retMap.put("gameKey", "");
				}else {
					boolean result = GameServerUtil.initGame(gameId,uid,redisStr.split(",")[0]);
					if(result) {
						int i = userService.updUserBaseGameStatusById(uid, 1, Long.valueOf(gameId));
						if(i>0) {
							retMap.put("gameStatus", 1);
							OtherRedisService.getInstance().addGameRoomCreditThis(uid, 0, Long.valueOf(gameId));
							RoomGameInfoModel game = GameServerUtil.getGameInfoById(Long.valueOf(gameId));
							if(game==null) {
								retMap.put("gameKey", "");
							}else {
								retMap.put("gameKey", game.getGameKey());
							}
						}else {
							retMap.put("gameStatus", 0);
							retMap.put("gameKey", "");
						}
					}else {
						retMap.put("gameStatus", 0);
						retMap.put("gameKey", "");
					}
				}
				returnModel.setCode(CodeContant.OK);
				returnModel.setMessage("开播成功");
				returnModel.setData(retMap);
				writeJson(resp, returnModel);
				return;
			}
		} else if ("close".equalsIgnoreCase(type)) {
			if (!liveService.exitLive(uid, returnModel)) {
				logger.debug("debug->Liveing exitLive exit err  uid is " + uid);
				returnModel.setCode(CodeContant.LIVECLOSE);
				returnModel.setMessage("停播失败");
			} else {
				UserBaseInfoModel userBaseInfoModel = userService.getUserbaseInfoByUid(uid, false);

				// 下播通知admin
				try {
					String signParams = EncryptUtils.signParams(VarConfigUtils.ServiceSecret,
							"appKey=" + VarConfigUtils.ServiceKey, "msgBody=" + returnModel.getData().toString(),
							"roomOwner=" + String.valueOf(uid));
					Unirest.post(UrlConfigLib.getUrl("url").getAdminrpc_end_live())
							.field("appKey", VarConfigUtils.ServiceKey).field("msgBody", returnModel.getData())
							.field("roomOwner", String.valueOf(uid)).field("sign", signParams).asStringAsync();

					Map<String, Object> map = (Map<String, Object>) returnModel.getData();
					ExitRoomCMod exitRoomCMod = new ExitRoomCMod();
					exitRoomCMod.setAvatar(userBaseInfoModel.getHeadimage());
					exitRoomCMod.setLevel(userBaseInfoModel.getUserLevel());
					exitRoomCMod.setNickname(userBaseInfoModel.getNickname());
					exitRoomCMod.setSex(userBaseInfoModel.getSex());
					exitRoomCMod.setUid(uid);
					exitRoomCMod.setCreditTotal(Integer.valueOf(map.get("creditTotal").toString()));
					exitRoomCMod.setPersontimes(Integer.valueOf(map.get("persontimes").toString()));
					exitRoomCMod.setRoomLikes(Integer.valueOf(map.get("roomlikes").toString()));
					exitRoomCMod.setTimeslong(Long.valueOf(map.get("timeslong").toString()));

					signParams = EncryptUtils.signParams(VarConfigUtils.ServiceSecret,
							"appKey=" + VarConfigUtils.ServiceKey, "msgBody=" + JSONObject.toJSONString(exitRoomCMod),
							"roomOwner=" + String.valueOf(uid));
					Unirest.post(UrlConfigLib.getUrl("url").getAdminrpc_publish_room())
							.field("appKey", VarConfigUtils.ServiceKey)
							.field("msgBody", JSONObject.toJSONString(exitRoomCMod))
							.field("roomOwner", String.valueOf(uid)).field("sign", signParams).asStringAsync();
				} catch (Exception e) {
					logger.error("<AdminRpc_END_LIVE->Exception>" + e.toString());
				}
			}
			OtherRedisService.getInstance().delRoomUserCounts(uid);
			RelationRedisService.getInstance().cleanUsersInRedis(uid);
			RelationRedisService.getInstance().cleanRoomRobots(uid);
		} else if ("heartbeat".equalsIgnoreCase(type)) {
			if (!liveService.heartBeatLive(uid)) {
				logger.debug("debug->Liveing param is err  type is " + type + " uid is " + uid);
				returnModel.setCode(CodeContant.LIVEHEARTBEAT);
				returnModel.setMessage("心跳失败");
			}
		} else {
			logger.debug("debug->Liveing param is err  type is " + type + " uid is " + uid);
			returnModel.setCode(CodeContant.CONPARAMSEXCEPT);
			returnModel.setMessage("参数有错");
		}
		writeJson(resp, returnModel);
	}

	/**
	 * 主播异常退出房间 由服务器调用
	 *
	 * @param srcUid
	 * @param dstUid
	 * @param token
	 */
	@RequestMapping(value = "off", method = RequestMethod.POST)
	public void leaveRoomByService(HttpServletRequest req, HttpServletResponse resp) {

		String strUid = req.getParameter("roomOwner");
		if (strUid == null || "".equals(strUid)) {
			return;
		} else {
			liveService.exitLive(Integer.valueOf(strUid), returnModel);
		}
	}

	/**
	 * 举报
	 *
	 * @param req
	 * @param resp
	 */
	@RequestMapping(value = "report", method = RequestMethod.GET)
	public void addReport(HttpServletRequest req, HttpServletResponse resp) {

		if (ParamHandleUtils.isBlank(req, "ncode", "os", "imei", "reqtime", "token", "dstuid")) {
			returnModel.setCode(CodeContant.ConParamIsEmptyOrNull);
			returnModel.setMessage("缺少参数或参数为空");
			writeJson(resp, returnModel);
			return;
		}
		if (!ParamHandleUtils.isInt(req, "os", "reqtime", "dstuid")) {
			returnModel.setCode(CodeContant.ConParamTypeIsErr);
			returnModel.setMessage("参数类型错误");
			writeJson(resp, returnModel);
			return;
		}

		authToken(req, true);
		if (returnModel.getCode() != CodeContant.OK) {
			// 验证失败
			writeJson(resp, returnModel);
			return;
		}
		int dstUid = Integer.valueOf(req.getParameter("dstuid"));
		if (dstUid <= 0) {
			returnModel.setCode(CodeContant.CONPARAMSEXCEPT);
			returnModel.setMessage("参数错误");
			writeJson(resp, returnModel);
			return;
		}

		UserBaseInfoModel userBaseInfoModel = userService.getUserbaseInfoByUid(dstUid, false);

		if (userBaseInfoModel == null || !userBaseInfoModel.getLiveStatus()) {
			returnModel.setCode(CodeContant.LiveReportExist);
			returnModel.setMessage("举报对象不存在或未开播");
		} else {
			OtherRedisService.getInstance().addReport(dstUid);
		}
		writeJson(resp, returnModel);
	}

	/**
	 * 用户点赞 前端异步调用无返回
	 *
	 * @param req
	 * @param resp
	 */
	@RequestMapping(value = "likes", method = RequestMethod.GET)
	public void addLikes(HttpServletRequest req, HttpServletResponse resp) {

		if (ParamHandleUtils.isBlank(req, "ncode", "os", "imei", "reqtime", "token", "dstuid", "num")) {
			returnModel.setCode(CodeContant.ConParamIsEmptyOrNull);
			returnModel.setMessage("缺少参数或参数为空");
			writeJson(resp, returnModel);
			return;
		}
		if (!ParamHandleUtils.isInt(req, "os", "reqtime", "dstuid", "num")) {
			returnModel.setCode(CodeContant.ConParamTypeIsErr);
			returnModel.setMessage("参数类型错误");
			writeJson(resp, returnModel);
			return;
		}

		authToken(req, true);
		if (returnModel.getCode() != CodeContant.OK) {
			// 验证失败
			writeJson(resp, returnModel);
			return;
		}

		int dstUid = Integer.valueOf(req.getParameter("dstuid"));
		int num = Integer.valueOf(req.getParameter("num"));

		if (dstUid <= 0) {
			returnModel.setCode(CodeContant.CONPARAMSEXCEPT);
			returnModel.setMessage("参数错误");
			writeJson(resp, returnModel);
			return;
		}
		if (num <= 0) {
			num = 1;
		}

		UserBaseInfoModel userBaseInfoModel = userService.getUserbaseInfoByUid(dstUid, false);

		if (userBaseInfoModel != null && userBaseInfoModel.getLiveStatus()) {
			RelationRedisService.getInstance().addLikes(dstUid, num);
			RelationRedisService.getInstance().addRoomLikes(dstUid, num);
		}
	}

	/**
	 * 设置房间管理员
	 * 
	 * @param req
	 * @param resp
	 */
	@RequestMapping(value = "anchormanage", method = RequestMethod.GET)
	public void setAnchorManage(HttpServletRequest req, HttpServletResponse resp) {
		if (ParamHandleUtils.isBlank(req, "ncode", "os", "imei", "reqtime", "token", "dstuid", "type")) {
			returnModel.setCode(CodeContant.ConParamIsEmptyOrNull);
			returnModel.setMessage("缺少参数或参数为空");
			writeJson(resp, returnModel);
			return;
		}

		authToken(req, true);
		if (returnModel.getCode() != CodeContant.OK) {
			// 验证失败
			writeJson(resp, returnModel);
			return;
		}

		String token = req.getParameter("token");// 充值操作对象
		int anchorUid = authService.decryptToken(token, returnModel); // 主播
		if (anchorUid <= 0) {
			writeJson(resp, returnModel);
			return;
		}

		int dstUid = Integer.valueOf(req.getParameter("dstuid")); // 用户 被设置管理对象

		String type = req.getParameter("type"); // =on 新增管理员 =off 解除管理员
		// 获取正在直播的数据
		LiveMicTimeModel liveMicTimeModel = liveService.getLiveMicInfoByUid(anchorUid, false);
		// 用户信息
		UserBaseInfoModel userBaseInfoModel = userService.getUserbaseInfoByUid(anchorUid, false);
		if (liveMicTimeModel == null || userBaseInfoModel == null || !userBaseInfoModel.getLiveStatus()) {
			// 未开播或开播数据异常
			returnModel.setCode(CodeContant.LivingStatus);
			returnModel.setMessage("主播开播异常");
		} else {
			int ires = liveService.setAnchorManage(anchorUid, dstUid, type);
			if (ires == 1) {
				userBaseInfoModel = userService.getUserbaseInfoByUid(dstUid, false);
				ManageRoomCMod msgBody = new ManageRoomCMod();
				msgBody.setLevel(userBaseInfoModel.getUserLevel());
				msgBody.setUid(dstUid);
				msgBody.setNickname(userBaseInfoModel.getNickname());
				msgBody.setAvatar(userBaseInfoModel.getHeadimage());
				msgBody.setStatus("on".equals(type) ? 1 : 0);

				if ("on".equals(type)) {
					// 设置成功 广播

					String signParams = EncryptUtils.signParams(VarConfigUtils.ServiceSecret,
							"appKey=" + VarConfigUtils.ServiceKey, "msgBody=" + JSONObject.toJSONString(msgBody),
							"roomOwner=" + String.valueOf(anchorUid));

					Unirest.post(UrlConfigLib.getUrl("url").getAdminrpc_publish_room())
							.field("appKey", VarConfigUtils.ServiceKey)
							.field("msgBody", JSONObject.toJSONString(msgBody))
							.field("roomOwner", String.valueOf(anchorUid)).field("sign", signParams).asJsonAsync();
				} else {
					userService.pushUserMessage(dstUid, msgBody);
				}
			} else {
				returnModel.setCode(CodeContant.liveManageErr);
				returnModel.setMessage("设置管理员失败");
			}
		}
		writeJson(resp, returnModel);
	}

	/**
	 * 设置禁言
	 * 
	 * @param req
	 * @param resp
	 */
	@RequestMapping(value = "silent", method = RequestMethod.GET)
	public void bannedUserInRoom(HttpServletRequest req, HttpServletResponse resp) {
		if (ParamHandleUtils.isBlank(req, "ncode", "os", "imei", "reqtime", "token", "dstuid", "anchoruid", "type")) {
			returnModel.setCode(CodeContant.ConParamIsEmptyOrNull);
			returnModel.setMessage("缺少参数或参数为空");
			writeJson(resp, returnModel);
			return;
		}
		authToken(req, true);
		if (returnModel.getCode() != CodeContant.OK) {
			// 验证失败
			writeJson(resp, returnModel);
			return;
		}
		String token = req.getParameter("token");// 充值操作对象
		int userUid = authService.decryptToken(token, returnModel); // 操作着UID
		if (userUid <= 0) {
			writeJson(resp, returnModel);
			return;
		}
		int dstUid = Integer.valueOf(req.getParameter("dstuid")); // 用户 被设置管理对象
		int anchorUid = Integer.valueOf(req.getParameter("anchoruid"));// 主播
		String type = req.getParameter("type"); // =on 设置禁言 =off 解除禁言

		if (RelationRedisService.getInstance().checkManageByAnchorUser(anchorUid, dstUid)) {
			returnModel.setCode(CodeContant.liveSilenOnErr);
			returnModel.setMessage("请先取消该用户管理权限");
		} else if (anchorUid == dstUid) {
			returnModel.setCode(CodeContant.liveSilenOnErr);
			returnModel.setMessage("对房间主播不能设置禁言");
		} else {
			// 获取正在直播的数据
			LiveMicTimeModel liveMicTimeModel = liveService.getLiveMicInfoByUid(anchorUid, false);
			// 主播信息
			UserBaseInfoModel anchorModel = userService.getUserbaseInfoByUid(anchorUid, false);
			// 用户信息
			UserBaseInfoModel userModel = userService.getUserbaseInfoByUid(userUid, false);
			// 操作对象信息
			UserBaseInfoModel dstUserModel = userService.getUserbaseInfoByUid(dstUid, false);
			if (dstUserModel.getIdentity() == 3) {
				returnModel.setCode(CodeContant.liveUserManage);
				returnModel.setMessage("不能对超管禁言");
			} else if (liveMicTimeModel == null || anchorModel == null || !anchorModel.getLiveStatus()) {
				// 未开播或开播数据异常
				returnModel.setCode(CodeContant.LivingStatus);
				returnModel.setMessage("主播开播异常或停播");
			} else {
				boolean bl = false;

				if (userUid == anchorUid) {
					bl = true;
				} else {
					if (userModel != null && userModel.getIdentity() == 3) {
						// 超管
						bl = true;
					} else {
						bl = ValueaddServiceUtil.getSilentAndKickPrivilege(anchorUid, userUid, dstUid);
					}
				}
				if (bl) {
					int ires = 1;// OtherRedisService.getInstance().setSilent(anchorUid,
									// dstUid, type);
					
					if (ires == 0) {
						returnModel.setCode(CodeContant.liveSilenOnErr);
						returnModel.setMessage("操作失败");
					} else {
						HttpResponse<JsonNode> httpRes = null;
						String strSilent = "";
						try {

							String signParams = "";

							if ("on".equals(type)) {

								signParams = EncryptUtils.signParams(VarConfigUtils.ServiceSecret,
										"appKey=" + VarConfigUtils.ServiceKey, "duration=" + 48 * 3600,
										"clientIds=" + dstUid, "roomOwner=" + String.valueOf(anchorUid));

								httpRes = Unirest.post(UrlConfigLib.getUrl("url").getSilent_on())
										.field("appKey", VarConfigUtils.ServiceKey).field("duration", 48 * 3600)
										.field("clientIds", dstUid).field("roomOwner", String.valueOf(anchorUid))
										.field("sign", signParams).asJson();
								strSilent = "用户禁言";
							} else {

								signParams = EncryptUtils.signParams(VarConfigUtils.ServiceSecret,
										"appKey=" + VarConfigUtils.ServiceKey, "clientIds=" + dstUid,
										"roomOwner=" + String.valueOf(anchorUid));

								httpRes = Unirest.post(UrlConfigLib.getUrl("url").getSilent_off())
										.field("appKey", VarConfigUtils.ServiceKey).field("clientIds", dstUid)
										.field("roomOwner", String.valueOf(anchorUid)).field("sign", signParams)
										.asJson();
								strSilent = "用户解除禁言";
							}

							String strResult = "";
							if (httpRes == null || httpRes.getBody().getObject() == null
									|| !httpRes.getBody().getObject().has("result")) {
								returnModel.setCode(CodeContant.liveSilenOnErr);
								returnModel.setMessage(strSilent + "g失败");
							} else {
								strResult = httpRes.getBody().getObject().get("result").toString();
								if ("ok".equalsIgnoreCase(strResult)) {

									OtherRedisService.getInstance().setSilent(anchorUid, dstUid, type);
									if ("on".equals(type)) {
										// 设置成功 广播
										SilentCMod msgBody = new SilentCMod();
										msgBody.setLevel(dstUserModel.getUserLevel());
										msgBody.setUid(dstUid);
										msgBody.setNickname(dstUserModel.getNickname());
										msgBody.setAvatar(dstUserModel.getHeadimage());
										msgBody.setStatus("on".equals(type) ? 1 : 0);

										signParams = EncryptUtils.signParams(VarConfigUtils.ServiceSecret,
												"appKey=" + VarConfigUtils.ServiceKey,
												"msgBody=" + JSONObject.toJSONString(msgBody),
												"roomOwner=" + String.valueOf(anchorUid));

										Unirest.post(UrlConfigLib.getUrl("url").getAdminrpc_publish_room())
												.field("appKey", VarConfigUtils.ServiceKey)
												.field("msgBody", JSONObject.toJSONString(msgBody))
												.field("roomOwner", String.valueOf(anchorUid)).field("sign", signParams)
												.asJsonAsync();
									}
								} else {
									returnModel.setCode(CodeContant.liveSilenOnErr);
									returnModel.setMessage(strSilent + "b失败:");
								}
							}
						} catch (Exception e) {
							returnModel.setCode(CodeContant.liveSilenOnErr);
							returnModel.setMessage(strSilent + "e失败");
							logger.error("<bannedUserInRoom->Exception:" + e.toString());
						}
					}
				} else {
					returnModel.setCode(CodeContant.liveSilenOnErr);
					returnModel.setMessage("没有权限操作");
				}
			}
		}
		writeJson(resp, returnModel);
	}

	/**
	 * 踢出房间
	 * 
	 * @param req
	 * @param resp
	 */
	@RequestMapping(value = "kick", method = RequestMethod.GET)
	public void kickUserRoom(HttpServletRequest req, HttpServletResponse resp) {
		if (ParamHandleUtils.isBlank(req, "ncode", "os", "imei", "reqtime", "token", "dstuid", "anchoruid")) {
			returnModel.setCode(CodeContant.ConParamIsEmptyOrNull);
			returnModel.setMessage("缺少参数或参数为空");
			writeJson(resp, returnModel);
			return;
		}
		authToken(req, true);
		if (returnModel.getCode() != CodeContant.OK) {
			// 验证失败
			writeJson(resp, returnModel);
			return;
		}
		String token = req.getParameter("token");// 充值操作对象
		int userUid = authService.decryptToken(token, returnModel); // 操作着UID
		if (userUid <= 0) {
			writeJson(resp, returnModel);
			return;
		}
		int dstUid = Integer.valueOf(req.getParameter("dstuid")); // 用户 被设置管理对象
		int anchorUid = Integer.valueOf(req.getParameter("anchoruid"));// 主播

		UserBaseInfoModel dstUserModel = userService.getUserbaseInfoByUid(dstUid, false);
		if (RelationRedisService.getInstance().checkManageByAnchorUser(anchorUid, dstUid)) {
			returnModel.setCode(CodeContant.LiveKickErr);
			returnModel.setMessage("请先取消该用户管理权限");
		} else if (dstUserModel.getIdentity() == 3) {
			returnModel.setCode(CodeContant.liveUserManage);
			returnModel.setMessage("不能踢超管");
		} else {
			if (anchorUid == dstUid) {
				returnModel.setCode(CodeContant.LiveKickErr);
				returnModel.setMessage("不能对房间主播操作");
			} else if (userUid == dstUid) {
				returnModel.setCode(CodeContant.LiveKickErr);
				returnModel.setMessage("不能对自己操作");
			} else {
				boolean bl = false;
				if (userUid == anchorUid) {
					// 主播操作
					bl = true;
				} else {
					UserBaseInfoModel userBaseInfoModel = userService.getUserbaseInfoByUid(userUid, false);
					if (userBaseInfoModel != null && userBaseInfoModel.getIdentity() == 3) {
						bl = true;
					} else {
						bl = ValueaddServiceUtil.getSilentAndKickPrivilege(anchorUid, userUid, dstUid);
					}
				}
				if (bl) {
					try {

						String signParams = EncryptUtils.signParams(VarConfigUtils.ServiceSecret,
								"appKey=" + VarConfigUtils.ServiceKey, "clientIds=" + dstUid,
								"roomOwner=" + String.valueOf(anchorUid));

						HttpResponse<JsonNode> httpRes = Unirest.post(UrlConfigLib.getUrl("url").getKick_out())
								.field("appKey", VarConfigUtils.ServiceKey).field("clientIds", dstUid)
								.field("roomOwner", String.valueOf(anchorUid)).field("sign", signParams).asJson();

						String strResult = "";
						if (httpRes == null || httpRes.getBody().getObject() == null
								|| !httpRes.getBody().getObject().has("result")) {
							returnModel.setCode(CodeContant.LiveKickErr);
							returnModel.setMessage("踢出操作失败");
						} else {
							strResult = httpRes.getBody().getObject().get("result").toString();
							if ("ok".equalsIgnoreCase(strResult)) {
								RelationRedisService.getInstance().setRoomKick(anchorUid, dstUid);
								UserBaseInfoModel userBaseInfoModel = userService.getUserbaseInfoByUid(dstUid, false);
								// 设置成功 广播
								KickCMod msgBody = new KickCMod();
								msgBody.setLevel(userBaseInfoModel.getUserLevel());
								msgBody.setUid(dstUid);
								msgBody.setNickname(userBaseInfoModel.getNickname());
								msgBody.setAvatar(userBaseInfoModel.getHeadimage());

								signParams = EncryptUtils.signParams(VarConfigUtils.ServiceSecret,
										"appKey=" + VarConfigUtils.ServiceKey,
										"msgBody=" + JSONObject.toJSONString(msgBody),
										"roomOwner=" + String.valueOf(anchorUid));

								Unirest.post(UrlConfigLib.getUrl("url").getAdminrpc_publish_room())
										.field("roomOwner", String.valueOf(anchorUid))
										.field("msgBody", JSONObject.toJSONString(msgBody))
										.field("appKey", VarConfigUtils.ServiceKey).field("sign", signParams)
										.asJsonAsync();
							} else {
								returnModel.setCode(CodeContant.LiveKickErr);
								returnModel.setMessage("踢出操作失败");
							}
						}
					} catch (Exception e) {
						returnModel.setCode(CodeContant.LiveKickErr);
						returnModel.setMessage("踢出操作失败");
						logger.error("<kickUserRoom->Exception>" + e.toString());
					}
				} else {
					returnModel.setCode(CodeContant.LiveKickErr);
					returnModel.setMessage("踢出操作失败");
				}
			}
		}
		writeJson(resp, returnModel);
	}

	/**
	 * 禁播
	 * 
	 * @param req
	 * @param resp
	 */
	@RequestMapping(value = "closeroom", method = RequestMethod.GET)
	public void closeRoom(HttpServletRequest req, HttpServletResponse resp) {
		if (ParamHandleUtils.isBlank(req, "ncode", "os", "imei", "reqtime", "token", "anchoruid")) {
			returnModel.setCode(CodeContant.ConParamIsEmptyOrNull);
			returnModel.setMessage("缺少参数或参数为空");
			writeJson(resp, returnModel);
			return;
		}
		authToken(req, true);
		if (returnModel.getCode() != CodeContant.OK) {
			// 验证失败
			writeJson(resp, returnModel);
			return;
		}
		String token = req.getParameter("token");// 充值操作对象
		int userUid = authService.decryptToken(token, returnModel); // 操作着UID
		if (userUid <= 0) {
			writeJson(resp, returnModel);
			return;
		}
		int anchorUid = Integer.valueOf(req.getParameter("anchoruid"));// 主播

		String cause = req.getParameter("cause");
		if (cause == null) {
			cause = "";
		}

		liveService.closeRoom(userUid, anchorUid, cause, returnModel);

		writeJson(resp, returnModel);
	}

	/**
	 * 解禁播
	 * 
	 * @param req
	 * @param resp
	 */
	@RequestMapping(value = "openroom", method = RequestMethod.GET)
	public void openRoom(HttpServletRequest req, HttpServletResponse resp) {
		if (ParamHandleUtils.isBlank(req, "ncode", "os", "imei", "reqtime", "token", "anchoruid")) {
			returnModel.setCode(CodeContant.ConParamIsEmptyOrNull);
			returnModel.setMessage("缺少参数或参数为空");
			writeJson(resp, returnModel);
			return;
		}
		authToken(req, true);
		if (returnModel.getCode() != CodeContant.OK) {
			// 验证失败
			writeJson(resp, returnModel);
			return;
		}
		String token = req.getParameter("token");// 充值操作对象
		int userUid = authService.decryptToken(token, returnModel); // 操作着UID
		if (userUid <= 0) {
			writeJson(resp, returnModel);
			return;
		}
		UserBaseInfoModel userBaseInfoModel = userService.getUserbaseInfoByUid(userUid, false);
		if (userBaseInfoModel == null || userBaseInfoModel.getIdentity() != 3) {
			returnModel.setCode(CodeContant.BaseInfo_identity);
			returnModel.setMessage("你没有这个权限");
		} else {
			int anchorUid = Integer.valueOf(req.getParameter("anchoruid"));// 主播
			liveService.unBanRoomByManage(anchorUid, returnModel);
		}

		writeJson(resp, returnModel);
	}

	/**
	 * 关闭本场直播
	 * 
	 * @param req
	 * @param resp
	 */
	@RequestMapping(value = "closeliving", method = RequestMethod.GET)
	public void closeliving(HttpServletRequest req, HttpServletResponse resp) {
		if (ParamHandleUtils.isBlank(req, "ncode", "os", "imei", "reqtime", "token", "anchoruid")) {
			returnModel.setCode(CodeContant.ConParamIsEmptyOrNull);
			returnModel.setMessage("缺少参数或参数为空");
			writeJson(resp, returnModel);
			return;
		}
		authToken(req, true);
		if (returnModel.getCode() != CodeContant.OK) {
			// 验证失败
			writeJson(resp, returnModel);
			return;
		}
		String token = req.getParameter("token");// 充值操作对象
		int userUid = authService.decryptToken(token, returnModel); // 操作着UID
		if (userUid <= 0) {
			writeJson(resp, returnModel);
			return;
		}
		int anchorUid = Integer.valueOf(req.getParameter("anchoruid"));// 主播
		UserBaseInfoModel userBaseInfoModel = userService.getUserbaseInfoByUid(userUid, false);
		if (userBaseInfoModel == null || userBaseInfoModel.getIdentity() != 3) {
			returnModel.setCode(CodeContant.BaseInfo_identity);
			returnModel.setMessage("你没有这个权限");
		} else {
			liveService.closeLive(anchorUid, returnModel);
		}
		
		writeJson(resp, returnModel);
	}

	/**
	 * 发红包
	 * 
	 * @param req
	 * @param resp
	 */
	@RequestMapping(value = "sendenvelope", method = RequestMethod.GET)
	public void sendRedEnvelope(HttpServletRequest req, HttpServletResponse resp) {
		if (ParamHandleUtils.isBlank(req, "ncode", "os", "imei", "reqtime", "token", "type", "count", "money","roomid")) {
			returnModel.setCode(CodeContant.ConParamIsEmptyOrNull);
			returnModel.setMessage("缺少参数或参数为空");
			writeJson(resp, returnModel);
			return;
		}
		authToken(req, true);
		if (returnModel.getCode() != CodeContant.OK) {
			// 验证失败
			writeJson(resp, returnModel);
			return;
		}

		String token = req.getParameter("token");
		int srcUid = authService.decryptToken(token, returnModel);
		if (srcUid <= 0) {
			writeJson(resp, returnModel);
			return;
		}
		
		if(UserRedisService.getInstance().existsSp(srcUid)) {
			returnModel.setCode(CodeContant.CONSYSTEMERR);
			returnModel.setMessage("扶持号不能发送红包");
			writeJson(resp, returnModel);
			return;
		}
		// 红包数量
		int count = 0;
		if (pattern.matcher(req.getParameter("count")).matches()) {
			count = Integer.valueOf(req.getParameter("count"));
		}
		// 红包总金额
		int money = 0;
		if (pattern.matcher(req.getParameter("money")).matches()) {
			money = Integer.valueOf(req.getParameter("money"));
		}
		int roomId = 0;
		if (pattern.matcher(req.getParameter("roomid")).matches()) {
			roomId = Integer.valueOf(req.getParameter("roomid"));
		}

		if (count <= 0 || count > 20) {
			returnModel.setCode(CodeContant.liveRedEnvelopCnts);
			returnModel.setMessage("请输入正确的红包数量(1～20之间)");
			writeJson(resp, returnModel);
			return;
		}
		if (money < 100 || money > 5000) {
			returnModel.setCode(CodeContant.liveRedEnvMoney);
			returnModel.setMessage("请输入正确的红包金额(100～5000之间)");
			writeJson(resp, returnModel);
			return;
		}
		if (roomId == 0) {
			returnModel.setCode(CodeContant.liveRoomIdErr);
			returnModel.setMessage("房间号异常");
			writeJson(resp, returnModel);
			return;
		}
		String type = req.getParameter("type");// =1送给主播 ＝2送给用户 ＝3送给大众
		// 红包祝福语
		String reddesc = req.getParameter("reddesc") == null ? "" : req.getParameter("reddesc");

		int dstUid = 0;
		if ("1".equals(type) || "2".equals(type)) {
			if (count != 1) {
				count = 1;
			}
			if (pattern.matcher(req.getParameter("dstuid")).matches()) {
				dstUid = Integer.valueOf(req.getParameter("dstuid"));
			} else {
				returnModel.setCode(CodeContant.USERASSETEXITS);
				returnModel.setMessage("送红包对象异常");
				writeJson(resp, returnModel);
				return;
			}
			liveService.sendRedEnvelopeToOne(srcUid, dstUid, money, count, reddesc, roomId, returnModel);
		} else {
			liveService.sendRedEnvelope(srcUid, dstUid, money, count, reddesc, roomId,returnModel);
		}

		writeJson(resp, returnModel);
	}

	/**
	 * 抢红包
	 * 
	 * @param req
	 * @param resp
	 */
	@RequestMapping(value = "tryenvelope", method = RequestMethod.GET)
	public void getEnvelope(HttpServletRequest req, HttpServletResponse resp) {
		if (ParamHandleUtils.isBlank(req, "ncode", "os", "imei", "reqtime", "token", "envelopid")) {
			returnModel.setCode(CodeContant.ConParamIsEmptyOrNull);
			returnModel.setMessage("缺少参数或参数为空");
			writeJson(resp, returnModel);
			return;
		}
		authToken(req, true);
		if (returnModel.getCode() != CodeContant.OK) {
			// 验证失败
			writeJson(resp, returnModel);
			return;
		}

		String token = req.getParameter("token");// 操作对象
		int dstUid = authService.decryptToken(token, returnModel); // 抢红包的人
		if (dstUid <= 0) {
			writeJson(resp, returnModel);
			return;
		}
		int envelopeId = Integer.valueOf(req.getParameter("envelopid"));
		liveService.getRedEnvelope(dstUid, envelopeId, returnModel);

		writeJson(resp, returnModel);
	}

	/**
	 * 获取抢红包记录
	 * 
	 * @param req
	 * @param resp
	 */
	@RequestMapping(value = "getenvelopelist", method = RequestMethod.GET)
	public void getEnvelopeList(HttpServletRequest req, HttpServletResponse resp) {
		if (ParamHandleUtils.isBlank(req, "ncode", "os", "imei", "reqtime", "token", "envelopid")) {
			returnModel.setCode(CodeContant.ConParamIsEmptyOrNull);
			returnModel.setMessage("缺少参数或参数为空");
			writeJson(resp, returnModel);
			return;
		}
		authToken(req, true);
		if (returnModel.getCode() != CodeContant.OK) {
			// 验证失败
			writeJson(resp, returnModel);
			return;
		}
		int envelopeId = Integer.valueOf(req.getParameter("envelopid"));
		Map<String, Object> map = redEnvelope.triedRedEnvelopeList(envelopeId);

		returnModel.setData(map);
		writeJson(resp, returnModel);
	}

	/**
	 * 判断是否抢过红包
	 * 
	 * @param req
	 * @param resp
	 */
	@RequestMapping(value = "checktriedenvelopelist", method = RequestMethod.GET)
	public void checkTriedEnvelope(HttpServletRequest req, HttpServletResponse resp) {
		if (ParamHandleUtils.isBlank(req, "ncode", "os", "imei", "reqtime", "token", "envelopid")) {
			returnModel.setCode(CodeContant.ConParamIsEmptyOrNull);
			returnModel.setMessage("缺少参数或参数为空");
			writeJson(resp, returnModel);
			return;
		}
		authToken(req, true);
		if (returnModel.getCode() != CodeContant.OK) {
			// 验证失败
			writeJson(resp, returnModel);
			return;
		}
		String token = req.getParameter("token");// 操作对象
		int uid = authService.decryptToken(token, returnModel); // 抢红包的人
		if (uid <= 0) {
			writeJson(resp, returnModel);
			return;
		}
		int envelopeId = Integer.valueOf(req.getParameter("envelopid"));
		liveService.checkTriedEnvelope(envelopeId, uid, returnModel);

		writeJson(resp, returnModel);
	}
	
	
	/**
	 * INVITE			邀请连麦	只能发起一路连麦
	 * arg:dstuid		被邀请的主播
	 * arg:isCanncel	是否撤销		1撤销
	 */
	@RequestMapping(value = "liveinvite", method = RequestMethod.GET)
	public void liveInvite(HttpServletRequest req, HttpServletResponse resp) {
		if (ParamHandleUtils.isBlank(req, "ncode", "os", "imei", "reqtime", "token","dstuid","isCanncel")) {
			returnModel.setCode(CodeContant.ConParamIsEmptyOrNull);
			returnModel.setMessage("缺少参数或参数为空");
			writeJson(resp, returnModel);
			return;
		}
		authToken(req, true);
		if (returnModel.getCode() != CodeContant.OK) {
			// 验证失败
			writeJson(resp, returnModel);
			return;
		}
		String token = req.getParameter("token");
		int uid = authService.decryptToken(token, returnModel);
		if (uid <= 0) {
			writeJson(resp, returnModel);
			return;
		}
		Integer dstuid = Integer.valueOf(req.getParameter("dstuid"));
		if(null == dstuid){
			returnModel.setCode(CodeContant.ConParamIsEmptyOrNull);
			writeJson(resp, returnModel);
			return;			
		}
		Integer isCanncel = Integer.valueOf(req.getParameter("isCanncel"));
		
		liveService.liveInvite(uid,dstuid,isCanncel,returnModel);
		writeJson(resp, returnModel);
	}
	

	/**
	 * ACKINVITE	确认连麦
	 * arg:dstuid	发起人
	 */
	@RequestMapping(value = "liveackinvite", method = RequestMethod.GET)
	public void liveAckInvite(HttpServletRequest req, HttpServletResponse resp) {
		if (ParamHandleUtils.isBlank(req, "ncode", "os", "imei", "reqtime", "token","dstuid","isjoin")) {
			returnModel.setCode(CodeContant.ConParamIsEmptyOrNull);
			returnModel.setMessage("缺少参数或参数为空");
			writeJson(resp, returnModel);
			return;
		}
		authToken(req, true);
		if (returnModel.getCode() != CodeContant.OK) {
			// 验证失败
			writeJson(resp, returnModel);
			return;
		}
		String token = req.getParameter("token");
		int uid = authService.decryptToken(token, returnModel);
		if (uid <= 0) {
			writeJson(resp, returnModel);
			return;
		}
		Integer dstuid = Integer.valueOf(req.getParameter("dstuid"));
		Integer isjoin = Integer.valueOf(req.getParameter("isjoin"));
		liveService.liveAckInvite(uid,dstuid,isjoin,returnModel);
		
		writeJson(resp, returnModel);
	}
	
	/**
	 * liveGetRoomToken	申请连麦的token
	 * arg:roomid	连麦的房间
	 */
	@RequestMapping(value = "livegetroomtoken", method = RequestMethod.GET)
	public void liveGetRoomToken(HttpServletRequest req, HttpServletResponse resp) {
		if (ParamHandleUtils.isBlank(req, "ncode", "os", "imei", "reqtime", "token","roomid")) {
			returnModel.setCode(CodeContant.ConParamIsEmptyOrNull);
			returnModel.setMessage("缺少参数或参数为空");
			writeJson(resp, returnModel);
			return;
		}
		
		authToken(req, true);
		if (returnModel.getCode() != CodeContant.OK) {
			// 验证失败
			writeJson(resp, returnModel);
			return;
		}
		
		String token = req.getParameter("token");
		int uid = authService.decryptToken(token, returnModel);
		if (uid <= 0) {
			writeJson(resp, returnModel);
			return;
		}
		
		Integer roomid = Integer.valueOf(req.getParameter("roomid"));
		if(null == roomid){
			returnModel.setCode(CodeContant.ConParamIsEmptyOrNull);
			writeJson(resp, returnModel);
			return;			
		}
		
		String roomToken = QnStreamUtil.getInstance().getRoomToken(roomid, uid);
		if(null == roomToken){
			returnModel.setCode(CodeContant.qiniu_gettoken_err);
		}else{
			returnModel.setCode(CodeContant.OK);
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("roomToken", roomToken);
			returnModel.setData(map);
		}
		writeJson(resp, returnModel);
	}
	
	/**
	 * closeLiveInvite	关闭连麦
	 */
	@RequestMapping(value = "closeliveinvite", method = RequestMethod.GET)
	public void closeLiveInvite(HttpServletRequest req, HttpServletResponse resp) {
		if (ParamHandleUtils.isBlank(req, "ncode", "os", "imei", "reqtime", "token")) {
			returnModel.setCode(CodeContant.ConParamIsEmptyOrNull);
			returnModel.setMessage("缺少参数或参数为空");
			writeJson(resp, returnModel);
			return;
		}
		
		authToken(req, true);
		if (returnModel.getCode() != CodeContant.OK) {
			// 验证失败
			writeJson(resp, returnModel);
			return;
		}
		
		String token = req.getParameter("token");
		int uid = authService.decryptToken(token, returnModel);
		if (uid <= 0) {
			writeJson(resp, returnModel);
			return;
		}
		
		liveService.closeLiveInvite(uid, returnModel);
		writeJson(resp, returnModel);
	}
}