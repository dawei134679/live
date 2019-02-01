package com.mpig.api.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
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
import com.google.common.base.Charsets;
import com.google.common.collect.Lists;
import com.mashape.unirest.http.Unirest;
import com.mpig.api.dictionary.lib.GameAppConfigLib;
import com.mpig.api.dictionary.lib.UrlConfigLib;
import com.mpig.api.model.ConfigGiftActivityModel;
import com.mpig.api.model.LiveListModel;
import com.mpig.api.model.ReturnModel;
import com.mpig.api.model.RoomGameInfoModel;
import com.mpig.api.model.UserBaseInfoModel;
import com.mpig.api.model.UserGuardInfoModel;
import com.mpig.api.model.UserVipInfoModel;
import com.mpig.api.model.ValueaddPrivilegeModel;
import com.mpig.api.modelcomet.AnchorOffCMod;
import com.mpig.api.modelcomet.ExitRoomCMod;
import com.mpig.api.modelcomet.KickCMod;
import com.mpig.api.redis.service.OtherRedisService;
import com.mpig.api.redis.service.RedisCommService;
import com.mpig.api.redis.service.RelationRedisService;
import com.mpig.api.redis.service.UserRedisService;
import com.mpig.api.service.IConfigService;
import com.mpig.api.service.ILiveService;
import com.mpig.api.service.IRoomService;
import com.mpig.api.service.IUserService;
import com.mpig.api.service.impl.SensitiveWordsService;
import com.mpig.api.statistics.Statistics;
import com.mpig.api.utils.CodeContant;
import com.mpig.api.utils.DateUtils;
import com.mpig.api.utils.EncryptUtils;
import com.mpig.api.utils.ParamHandleUtils;
import com.mpig.api.utils.RedisContant;
import com.mpig.api.utils.ValueaddServiceUtil;
import com.mpig.api.utils.VarConfigUtils;

/**
 * 用户侧 与房间有关的功能
 *
 * @author fang
 */
@Controller
@Scope("prototype")
@RequestMapping("/room")
public class RoomController extends BaseController {

	private static final Logger logger = Logger.getLogger(RoomController.class);
	@Resource
	private IRoomService roomService;
	@Resource
	private IUserService userService;
	@Resource
	private ILiveService liveService;
	@Resource
	private IConfigService configService;
	
	/**
	 * 应用宝
	 */
	private static String SecretKey = "vCwG1chKYFYjs0YS";
	
	/**
	 * 用户进入直播间
	 *
	 * @param req
	 * @param resp
	 */
	@RequestMapping(value = "enter", method = RequestMethod.GET)
	public void intoRoom(HttpServletRequest req, HttpServletResponse resp) {

		if (ParamHandleUtils.isBlank(req, "ncode", "os", "imei", "reqtime", "token", "artistid")) {
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
			writeJson(resp, returnModel);
			return;
		}

		Integer artistUid = Integer.valueOf(req.getParameter("artistid"));
		int os = Integer.valueOf(req.getParameter("os"));
		String visitor = req.getParameter("visit");
		
		if ("visitor".equals(visitor)) {
			// 游客
			roomService.enterRoomVisitor(artistUid, os, returnModel);
			writeJson(resp, returnModel);
			return;
		}
		String token = req.getParameter("token");// 充值操作对象
		int uid = authService.decryptToken(token, returnModel);
		if (uid <= 0) {
			writeJson(resp, returnModel);
			return;
		}
		
//		if (artistUid.equals(uid)) {
//			returnModel.setCode(CodeContant.enterSelfRoom);
//			returnModel.setMessage("无法进入此房间");
//			writeJson(resp, returnModel);
//			return;
//		}
		
		Set<Integer> allGameIds = GameAppConfigLib.allGameAppRoomIds();
		
		if (!allGameIds.contains(artistUid)) {

//			if (uid != 44) {
//				Double checkUserInAnchor = RelationRedisService.getInstance().checkUserInAnchor(uid, artistUid);
//				if (checkUserInAnchor != null) {
//					returnModel.setCode(CodeContant.liveEnterRepeat);
//					returnModel.setMessage("不能刷屏");
//					writeJson(resp, returnModel);
//					return;
//				}
//			}
			if (!StringUtils.isEmpty(RelationRedisService.getInstance().getBlackUser(artistUid, uid))) {
				returnModel.setCode(CodeContant.user_black);
				returnModel.setMessage("你已被播主拉黑");
				writeJson(resp, returnModel);
				return;
			}
			if (RelationRedisService.getInstance().getRoomKick(artistUid, uid)) {
				returnModel.setCode(CodeContant.LiveKickInto);
				returnModel.setMessage("您已被管理员踢出该房间，请文明发言");
				writeJson(resp, returnModel);
				return;
			}
		}
		
		roomService.enterRoom(uid, artistUid,os, returnModel);
		RelationRedisService.getInstance().hsetRoomUsers(uid, artistUid);
		logger.debug("用户:"+uid+"进入了,主播:"+artistUid+"的直播间.............");

		//增加统计激活。。。第三方安卓
		try{
			Statistics.SendPigAnalysis(-1,String.valueOf(os),req);	
		}catch (Exception e) {
			//handle exception
		}
		writeJson(resp, returnModel);
	}

	
	/**
	 * 用户主动退出房间
	 *
	 * @param req
	 * @param resp
	 */
	@RequestMapping(value = "exit", method = RequestMethod.GET)
	public void leaveRoomByMobile(HttpServletRequest req, HttpServletResponse resp) {

		if (ParamHandleUtils.isBlank(req, "ncode", "os", "imei", "reqtime", "token", "artistid")) {
			returnModel.setCode(CodeContant.ConParamIsEmptyOrNull);
			returnModel.setMessage("缺少参数或参数为空");
			writeJson(resp, returnModel);
			return;
		}
		if (!ParamHandleUtils.isInt(req, "os", "reqtime", "artistid")) {
			returnModel.setCode(CodeContant.ConParamTypeIsErr);
			returnModel.setMessage("参数类型错误");
			writeJson(resp, returnModel);
			return;
		}

		// 验证auth及token
		authToken(req, true);
		if (returnModel.getCode() != 200) {
			// 验证失败
			writeJson(resp, returnModel);
			return;
		}
		String token = req.getParameter("token");// 充值操作对象
		int uid = authService.decryptToken(token, returnModel);
		if (uid <= 0) {
			writeJson(resp, returnModel);
			return;
		}

		UserBaseInfoModel userBaseInfoModel = userService.getUserbaseInfoByUid(uid, false);
		if (userBaseInfoModel == null) {
			returnModel.setCode(CodeContant.USERASSETEXITS);
			returnModel.setMessage("用户不存在");
			writeJson(resp, returnModel);
			return;
		}
		// 获取主播uid
		Integer artistUid = Integer.valueOf(req.getParameter("artistid"));

		roomService.exitRoom(uid, artistUid, returnModel);
		//RelationRedisService.getInstance().setRealUserInRoom(artistUid, -1.0);
		RelationRedisService.getInstance().hdelRoomUsers(uid, artistUid);
		logger.debug("用户:"+uid+"离开了,主播:"+artistUid+"的直播间.............");
		long nowtime = System.currentTimeMillis()/1000;
		// 用户退房通知admin
		try {
			ExitRoomCMod exitRoomCMod = new ExitRoomCMod();
			exitRoomCMod.setAvatar(userBaseInfoModel.getHeadimage());
			exitRoomCMod.setLevel(userBaseInfoModel.getUserLevel());
			exitRoomCMod.setNickname(userBaseInfoModel.getNickname());
			exitRoomCMod.setSex(userBaseInfoModel.getSex());
			exitRoomCMod.setUid(userBaseInfoModel.getUid());
			
			UserGuardInfoModel userGuardInfoModel = ValueaddServiceUtil.getGuardInfo(uid, artistUid);
			if(userGuardInfoModel != null){
				ValueaddPrivilegeModel privilegeModel = ValueaddServiceUtil.getPrivilege(userGuardInfoModel.getGid(), userGuardInfoModel.getLevel());
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("guardId", userGuardInfoModel.getGid());
				map.put("guardLevel", userGuardInfoModel.getLevel());
				int icon = 0;
				if(nowtime >userGuardInfoModel.getEndtime() && nowtime <=userGuardInfoModel.getCushiontime()){
					icon = privilegeModel.getCushionIcon();
				}else{
					icon = privilegeModel.getIconId();
				}
				Long days = DateUtils.getDaysBetweenTime(nowtime, new Long(userGuardInfoModel.getEndtime()));
				map.put("surplusDays", days);
				map.put("guardIcon", icon);
				exitRoomCMod.setGuardInfo(map);
			}

			String signParams = EncryptUtils.signParams(VarConfigUtils.ServiceSecret,
					"appKey=" + VarConfigUtils.ServiceKey, "msgBody=" + JSONObject.toJSONString(exitRoomCMod),
					"roomOwner=" + String.valueOf(artistUid));

			Unirest.post(UrlConfigLib.getUrl("url").getAdminrpc_publish_room())
					.field("appKey", VarConfigUtils.ServiceKey).field("msgBody", JSONObject.toJSONString(exitRoomCMod))
					.field("roomOwner", String.valueOf(artistUid)).field("sign", signParams).asStringAsync();
		} catch (Exception e) {
			logger.error("<Adminrpc_publish_room->Exception>" + e.toString());
		}

		writeJson(resp, returnModel);
	}

	/**
	 * 用户异常掉线
	 *
	 * @param req
	 * @param resp
	 */
	@RequestMapping(value = "offline")
	public void exitRoomByService(HttpServletRequest req, HttpServletResponse resp) {

		int dstUid = Integer.parseInt(req.getParameter("roomOwner"));
		int srcUid = Integer.parseInt(req.getParameter("clientId"));
		logger.debug("offline 聊天服务通知用户掉线：房间主播uid＝" + dstUid + " 用户uid＝" + srcUid);

		// 用户退房通知admin
		try {
			// 主播 
			if (dstUid == srcUid) {
				liveService.closeLiveInvite(dstUid, returnModel);
			} else {
				UserBaseInfoModel userBaseInfoModel = userService.getUserbaseInfoByUid(srcUid, false);
				roomService.exitRoom(srcUid, dstUid, returnModel);
				ExitRoomCMod exitRoomCMod = new ExitRoomCMod();
				exitRoomCMod.setAvatar(userBaseInfoModel.getHeadimage());
				exitRoomCMod.setLevel(userBaseInfoModel.getUserLevel());
				exitRoomCMod.setNickname(userBaseInfoModel.getNickname());
				exitRoomCMod.setSex(userBaseInfoModel.getSex());
				exitRoomCMod.setUid(userBaseInfoModel.getUid());
				UserGuardInfoModel userGuardInfoModel = ValueaddServiceUtil.getGuardInfo(srcUid, dstUid);
				long nowtime = System.currentTimeMillis()/1000;
				if(userGuardInfoModel != null){
					ValueaddPrivilegeModel privilegeModel = ValueaddServiceUtil.getPrivilege(userGuardInfoModel.getGid(), userGuardInfoModel.getLevel());
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("guardId", userGuardInfoModel.getGid());
					map.put("guardLevel", userGuardInfoModel.getLevel());
					int icon = 0;
					if(nowtime >userGuardInfoModel.getEndtime() && nowtime <=userGuardInfoModel.getCushiontime()){
						icon = privilegeModel.getCushionIcon();
					}else{
						icon = privilegeModel.getIconId();
					}
					Long days = DateUtils.getDaysBetweenTime(nowtime, new Long(userGuardInfoModel.getEndtime()));
					map.put("surplusDays", days);
					map.put("guardIcon", icon);
					exitRoomCMod.setGuardInfo(map);
				}
				
				String signParams = EncryptUtils.signParams(VarConfigUtils.ServiceSecret,
						"appKey=" + VarConfigUtils.ServiceKey, "msgBody=" + JSONObject.toJSONString(exitRoomCMod),
						"roomOwner=" + String.valueOf(dstUid));

				Unirest.post(UrlConfigLib.getUrl("url").getAdminrpc_publish_room())
						.field("appKey", VarConfigUtils.ServiceKey)
						.field("msgBody", JSONObject.toJSONString(exitRoomCMod))
						.field("roomOwner", String.valueOf(dstUid)).field("sign", signParams).asStringAsync();
			}
			RelationRedisService.getInstance().hdelRoomUsers(srcUid, dstUid);
		} catch (Exception e) {
			logger.error("<Adminrpc_publish_room->Exception>" + e.toString());
		}
	}

	/**
	 * 获取直播间的用户列表
	 *
	 * @param req
	 * @param resp
	 */
	@RequestMapping(value = "audience", method = RequestMethod.GET)
	public void getUserListOfLive(HttpServletRequest req, HttpServletResponse resp) {

		if (ParamHandleUtils.isBlank(req, "ncode", "os", "imei", "reqtime", "artistid", "page")) {
			returnModel.setCode(CodeContant.ConParamIsEmptyOrNull);
			returnModel.setMessage("缺少参数或参数为空");
			writeJson(resp, returnModel);
			return;
		}
		if (!ParamHandleUtils.isInt(req, "os", "reqtime", "artistid", "page")) {
			returnModel.setCode(CodeContant.ConParamTypeIsErr);
			returnModel.setMessage("参数类型错误");
			writeJson(resp, returnModel);
			return;
		}

		authToken(req, false);
		if (returnModel.getCode() != 200) {
			// 验证失败
			writeJson(resp, returnModel);
			return;
		}
		int srcUid = 0;
		String token = req.getParameter("token");// 充值操作对象
		if (!StringUtils.isEmpty(token)) {
			srcUid = authService.decryptToken(token, returnModel);
			if (srcUid <= 0) {
				writeJson(resp, returnModel);
				return;
			}
		}
		Integer dstUid = Integer.valueOf(req.getParameter("artistid"));
		Integer page = Integer.valueOf(req.getParameter("page"));

		roomService.userlistOfLive(dstUid, srcUid, page, returnModel);

		writeJson(resp, returnModel);
	}
	/**
	 * 获取直播间的用户列表
	 *
	 * @param req
	 * @param resp
	 */
	@RequestMapping(value = "/pc/audience", method = RequestMethod.GET)
	public void getUserListOfLiveForPc(HttpServletRequest req, HttpServletResponse resp) {
		
		if (ParamHandleUtils.isBlank(req, "ncode", "os", "imei", "reqtime", "artistid", "page")) {
			returnModel.setCode(CodeContant.ConParamIsEmptyOrNull);
			returnModel.setMessage("缺少参数或参数为空");
			writeJson(resp, returnModel);
			return;
		}
		if (!ParamHandleUtils.isInt(req, "os", "reqtime", "artistid", "page")) {
			returnModel.setCode(CodeContant.ConParamTypeIsErr);
			returnModel.setMessage("参数类型错误");
			writeJson(resp, returnModel);
			return;
		}
		
		authToken(req, false);
		if (returnModel.getCode() != 200) {
			// 验证失败
			writeJson(resp, returnModel);
			return;
		}
		int srcUid = 0;
		String token = req.getParameter("token");// 充值操作对象
		if (!StringUtils.isEmpty(token)) {
			srcUid = authService.decryptToken(token, returnModel);
			if (srcUid <= 0) {
				writeJson(resp, returnModel);
				return;
			}
		}
		Integer dstUid = Integer.valueOf(req.getParameter("artistid"));
		Integer page = Integer.valueOf(req.getParameter("page"));
		
		roomService.userlistOfLiveForPc(dstUid, srcUid, page, returnModel);
		
		writeJson(resp, returnModel);
	}
	/**
	 * 获取直播间的所有用户列表（包含守护 vip 普通用户）
	 *
	 * @param req
	 * @param resp
	 */
	@RequestMapping(value = "/allAudience", method = RequestMethod.GET)
	public void getAllUserListOfLive(HttpServletRequest req, HttpServletResponse resp) {
		if (ParamHandleUtils.isBlank(req, "ncode", "os", "imei", "reqtime", "artistid", "page")) {
			returnModel.setCode(CodeContant.ConParamIsEmptyOrNull);
			returnModel.setMessage("缺少参数或参数为空");
			writeJson(resp, returnModel);
			return;
		}
		if (!ParamHandleUtils.isInt(req, "os", "reqtime", "artistid", "page")) {
			returnModel.setCode(CodeContant.ConParamTypeIsErr);
			returnModel.setMessage("参数类型错误");
			writeJson(resp, returnModel);
			return;
		}
		
		authToken(req, false);
		if (returnModel.getCode() != 200) {
			// 验证失败
			writeJson(resp, returnModel);
			return;
		}
		int srcUid = 0;
		String token = req.getParameter("token");// 充值操作对象
		if (!StringUtils.isEmpty(token)) {
			srcUid = authService.decryptToken(token, returnModel);
			if (srcUid <= 0) {
				writeJson(resp, returnModel);
				return;
			}
		}
		Integer dstUid = Integer.valueOf(req.getParameter("artistid"));
		Integer page = Integer.valueOf(req.getParameter("page"));
		
		roomService.allUserlistOfLive(dstUid, srcUid, page, returnModel);
		
		writeJson(resp, returnModel);
	}
	
	/**
	 * 获取直播间的守护列表
	 *
	 * @param req
	 * @param resp
	 */
	@RequestMapping(value = "/guardAudience", method = RequestMethod.GET)
	public void getGuardUserListOfLive(HttpServletRequest req, HttpServletResponse resp) {

		if (ParamHandleUtils.isBlank(req, "ncode", "os", "imei", "reqtime", "token", "artistid", "page")) {
			returnModel.setCode(CodeContant.ConParamIsEmptyOrNull);
			returnModel.setMessage("缺少参数或参数为空");
			writeJson(resp, returnModel);
			return;
		}
		if (!ParamHandleUtils.isInt(req, "os", "reqtime", "artistid", "page")) {
			returnModel.setCode(CodeContant.ConParamTypeIsErr);
			returnModel.setMessage("参数类型错误");
			writeJson(resp, returnModel);
			return;
		}

		authToken(req, false);
		if (returnModel.getCode() != 200) {
			// 验证失败
			writeJson(resp, returnModel);
			return;
		}
		int srcUid = 0;
		String token = req.getParameter("token");
		if (!StringUtils.isEmpty(token)) {
			srcUid = authService.decryptToken(token, returnModel);
			if (srcUid <= 0) {
				writeJson(resp, returnModel);
				return;
			}
		}
		Integer dstUid = Integer.valueOf(req.getParameter("artistid"));
		Integer page = Integer.valueOf(req.getParameter("page"));

		roomService.guardUserlistOfLive(dstUid, srcUid, page, returnModel);

		writeJson(resp, returnModel);
	}
	
	/**
	 * 获取直播间的守护列表
	 *
	 * @param req
	 * @param resp
	 */
	@RequestMapping(value = "/guardAudience/v1", method = RequestMethod.GET)
	@ResponseBody
	public ReturnModel getGuardUserListOfLiveV1(HttpServletRequest req, HttpServletResponse resp) {

		long lg1 = System.currentTimeMillis();
		if (ParamHandleUtils.isBlank(req, "ncode", "os", "imei", "reqtime", "artistid", "page")) {
			returnModel.setCode(CodeContant.ConParamIsEmptyOrNull);
			returnModel.setMessage("缺少参数或参数为空");
			return returnModel;
		}
		if (!ParamHandleUtils.isInt(req, "os", "reqtime", "artistid", "page")) {
			returnModel.setCode(CodeContant.ConParamTypeIsErr);
			returnModel.setMessage("参数类型错误");
			return returnModel;
		}

		authToken(req, false);
		if (returnModel.getCode() != 200) {
			return returnModel;
		}

		int srcUid = 0;
		String token = req.getParameter("token");
		if (!StringUtils.isEmpty(token)) {
			srcUid = authService.decryptToken(token, returnModel);
			if (srcUid <= 0) {
				return returnModel;
			}
		}
		Integer dstUid = Integer.valueOf(req.getParameter("artistid"));
		Integer page = Integer.valueOf(req.getParameter("page"));

		long lg2 = System.currentTimeMillis();
		logger.info("/guardAudience/v1 time:验证时长＝" + (lg2 - lg1));

		roomService.guardUserAllList(dstUid, srcUid, page, returnModel);

		long lg3 = System.currentTimeMillis();
		logger.info("/guardAudience/v1 time :获取列表时长＝" + (lg3 - lg2));
		return returnModel;
	}

	/**
	 * 获取正在直播用户列表
	 *
	 * @param req
	 * @param resp
	 */
	@RequestMapping(value = "livelist/homerec", method = RequestMethod.GET)
	public void getHomerecLiveList(HttpServletRequest req, HttpServletResponse resp) {
		if (ParamHandleUtils.isBlank(req, "ncode", "os", "imei", "reqtime")) {
			returnModel.setCode(CodeContant.ConParamIsEmptyOrNull);
			returnModel.setMessage("缺少参数或参数为空");
			writeJson(resp, returnModel);
			return;
		}
		if (!ParamHandleUtils.isInt(req, "os", "reqtime", "page")) {
			returnModel.setCode(CodeContant.ConParamTypeIsErr);
			returnModel.setMessage("参数类型错误");
			writeJson(resp, returnModel);
			return;
		}

		// 验证auth及token
		authToken(req, false);
		if (returnModel.getCode() != 200) {
			// 验证失败
			writeJson(resp, returnModel);
			return;
		}
		
		String token = req.getParameter("token");// 充值操作对象
		Integer srcUid = 0;
		if (StringUtils.isEmpty(token)) {
			srcUid = null;
		}else {
			srcUid = authService.decryptToken(token, returnModel);
			if (srcUid <= 0) {
				writeJson(resp, returnModel);
				return;
			}
		}
		
		Integer page = 0;
		int oString = Integer.valueOf(req.getParameter("os"));
		roomService.getHomeRecLiveingList(oString,page, returnModel, srcUid);
		writeJson(resp, returnModel);
	}
	/**
	 * 获取正在直播最新入驻的列表
	 *
	 * @param req
	 * @param resp
	 */
	@RequestMapping(value = "/newJoin", method = RequestMethod.GET)
	public void getNewJoinLiveList(HttpServletRequest req, HttpServletResponse resp) {
		if (ParamHandleUtils.isBlank(req, "ncode", "os", "imei", "reqtime")) {
			returnModel.setCode(CodeContant.ConParamIsEmptyOrNull);
			returnModel.setMessage("缺少参数或参数为空");
			writeJson(resp, returnModel);
			return;
		}
		if (!ParamHandleUtils.isInt(req, "os", "reqtime", "page")) {
			returnModel.setCode(CodeContant.ConParamTypeIsErr);
			returnModel.setMessage("参数类型错误");
			writeJson(resp, returnModel);
			return;
		}
		
		// 验证auth及token
		authToken(req, false);
		if (returnModel.getCode() != 200) {
			// 验证失败
			writeJson(resp, returnModel);
			return;
		}
		
		String token = req.getParameter("token");// 充值操作对象
		Integer page = Integer.parseInt(req.getParameter("page"));// 充值操作对象
		Integer srcUid = 0;
		if (StringUtils.isEmpty(token)) {
			srcUid = null;
		}else {
			srcUid = authService.decryptToken(token, returnModel);
			if (srcUid <= 0) {
				writeJson(resp, returnModel);
				return;
			}
		}
		
		if(page==0){
			page=1;
		}
		int oString = Integer.valueOf(req.getParameter("os"));
		roomService.getNewJoinLiveingList(oString,page, returnModel, srcUid);
		writeJson(resp, returnModel);
	}
	
	/**
	 * 获取正在直播用户列表
	 *
	 * @param req
	 * @param resp
	 */
	@RequestMapping(value = "livelist/homesquare", method = RequestMethod.GET)
	public void getSquareLiveList(HttpServletRequest req, HttpServletResponse resp) {
		if (ParamHandleUtils.isBlank(req, "ncode", "os", "imei", "reqtime")) {
			returnModel.setCode(CodeContant.ConParamIsEmptyOrNull);
			returnModel.setMessage("缺少参数或参数为空");
			writeJson(resp, returnModel);
			return;
		}
		if (!ParamHandleUtils.isInt(req, "os", "reqtime", "page")) {
			returnModel.setCode(CodeContant.ConParamTypeIsErr);
			returnModel.setMessage("参数类型错误");
			writeJson(resp, returnModel);
			return;
		}

		// 验证auth及token
		authToken(req, false);
		if (returnModel.getCode() != 200) {
			// 验证失败
			writeJson(resp, returnModel);
			return;
		}
		
		String token = req.getParameter("token");// 充值操作对象
		Integer srcUid = 0;
		if (StringUtils.isEmpty(token)) {
			srcUid = null;
		}else {
			srcUid = authService.decryptToken(token, returnModel);
			if (srcUid <= 0) {
				writeJson(resp, returnModel);
				return;
			}
		}
		Integer page = Integer.parseInt(req.getParameter("page"));
		if(page <=0){
			page = 1;
		}
		int oString = Integer.valueOf(req.getParameter("os"));
		roomService.getSquareLiveList(oString,page, returnModel, srcUid);
		writeJson(resp, returnModel);
	}
	
	
	/**
	 * 获取正在直播推荐列表
	 *
	 * @param req
	 * @param resp
	 */
	@RequestMapping(value = "livelist", method = RequestMethod.GET)
	public void getLiveList(HttpServletRequest req, HttpServletResponse resp) {
		
		if (ParamHandleUtils.isBlank(req, "ncode", "os", "imei", "reqtime")) {
			returnModel.setCode(CodeContant.ConParamIsEmptyOrNull);
			returnModel.setMessage("缺少参数或参数为空");
			writeJson(resp, returnModel);
			return;
		}
		if (!ParamHandleUtils.isInt(req, "os", "reqtime", "page")) {
			returnModel.setCode(CodeContant.ConParamTypeIsErr);
			returnModel.setMessage("参数类型错误");
			writeJson(resp, returnModel);
			return;
		}

		// 验证auth及token
		authToken(req, false);
		if (returnModel.getCode() != 200) {
			// 验证失败
			writeJson(resp, returnModel);
			return;
		}
		String token = req.getParameter("token");// 充值操作对象
		Integer srcUid = 0;
		if (StringUtils.isEmpty(token) || "null".equalsIgnoreCase(token) || "(null)".equalsIgnoreCase(token)) {
			srcUid = null;
		}else {
			srcUid = authService.decryptToken(token, returnModel);
			if (srcUid <= 0) {
				writeJson(resp, returnModel);
				return;
			}
		}
		
		Integer page = 0;// Integer.valueOf(req.getParameter("page"));
		int oString = Integer.valueOf(req.getParameter("os"));
		roomService.getLiveingList(oString,page, returnModel, srcUid);
		writeJson(resp, returnModel);
	}

	/**
	 * 获取（手机主播）接口
	 * 
	 * @param req
	 * @param resp
	 */
	@RequestMapping(value = "/mobileList", method = RequestMethod.GET)
	public void getMobileList(HttpServletRequest req, HttpServletResponse resp) {
		if (ParamHandleUtils.isBlank(req, "ncode", "os", "imei", "reqtime")) {
			returnModel.setCode(CodeContant.ConParamIsEmptyOrNull);
			returnModel.setMessage("缺少参数或参数为空");
			writeJson(resp, returnModel);
			return;
		}
		if (!ParamHandleUtils.isInt(req, "os", "reqtime", "page")) {
			returnModel.setCode(CodeContant.ConParamTypeIsErr);
			returnModel.setMessage("参数类型错误");
			writeJson(resp, returnModel);
			return;
		}
		// 验证auth及token
		authToken(req, false);
		if (returnModel.getCode() != 200) {
			// 验证失败
			writeJson(resp, returnModel);
			return;
		}

		String token = req.getParameter("token");
		Integer srcUid = 0;
		if (StringUtils.isEmpty(token) || "null".equalsIgnoreCase(token) || "(null)".equalsIgnoreCase(token)) {
			srcUid = null;
		}else {
			srcUid = authService.decryptToken(token, returnModel);
			if (srcUid <= 0) {
				writeJson(resp, returnModel);
				return;
			}
		}
		int os = Integer.valueOf(req.getParameter("os"));
		Integer page = 0;
		roomService.getMobileList(page, returnModel, srcUid);
		writeJson(resp, returnModel);
	}
	
	/**
	 * 获取（最新主播）接口
	 * 
	 * @param req
	 * @param resp
	 */
	@RequestMapping(value = "hotlist", method = RequestMethod.GET)
	public void getHotList(HttpServletRequest req, HttpServletResponse resp) {
		if (ParamHandleUtils.isBlank(req, "ncode", "os", "imei", "reqtime")) {
			returnModel.setCode(CodeContant.ConParamIsEmptyOrNull);
			returnModel.setMessage("缺少参数或参数为空");
			writeJson(resp, returnModel);
			return;
		}
		if (!ParamHandleUtils.isInt(req, "os", "reqtime", "page")) {
			returnModel.setCode(CodeContant.ConParamTypeIsErr);
			returnModel.setMessage("参数类型错误");
			writeJson(resp, returnModel);
			return;
		}
		// 验证auth及token
		authToken(req, false);
		if (returnModel.getCode() != 200) {
			// 验证失败
			writeJson(resp, returnModel);
			return;
		}

		String token = req.getParameter("token");// 充值操作对象
		Integer srcUid = 0;
		if (StringUtils.isEmpty(token) || "null".equalsIgnoreCase(token) || "(null)".equalsIgnoreCase(token)) {
			srcUid = null;
		}else {
			srcUid = authService.decryptToken(token, returnModel);
			if (srcUid <= 0) {
				writeJson(resp, returnModel);
				return;
			}
		}
		int os = Integer.valueOf(req.getParameter("os"));
		Integer page = 0;
		roomService.getHostList(os,page, returnModel, srcUid);
		writeJson(resp, returnModel);
	}

	/**
	 * 获取关注主播的精彩回放列表（当前主播都没有开播）
	 *
	 * @param req
	 * @param resp
	 */
	@RequestMapping(value = "liveBack", method = RequestMethod.GET)
	public void getLiveBack(HttpServletRequest req, HttpServletResponse resp) {
		// 验证auth及token
		authToken(req, false);

		// Integer page = Integer.valueOf(req.getParameter("page"));
		// Byte status = 0;

		List<LiveListModel> _listLiveListModels = null; // liveService.getLiveListByStatus(status,
		// page);
		returnModel.setData(_listLiveListModels);

		writeJson(resp, returnModel);
	}

	/**
	 * 弹幕
	 *
	 * @param req
	 * @param resp
	 */
	@RequestMapping(value = "danmaku", method = RequestMethod.GET)
	public void sendDanmaku(HttpServletRequest req, HttpServletResponse resp) {
		if (ParamHandleUtils.isBlank(req, "ncode", "os", "imei", "reqtime", "token", "msg", "artistid","gid")) {
			returnModel.setCode(CodeContant.ConParamIsEmptyOrNull);
			returnModel.setMessage("缺少参数或参数为空");
			writeJson(resp, returnModel);
			return;
		}
		if (!ParamHandleUtils.isInt(req, "os", "reqtime", "artistid","gid")) {
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

		String token = req.getParameter("token");// 充值操作对象
		int srcUid = authService.decryptToken(token, returnModel);
		if (srcUid <= 0) {
			writeJson(resp, returnModel);
			return;
		}
		int dstUid = Integer.parseInt(req.getParameter("artistid"));
		int gid = Integer.parseInt(req.getParameter("gid"));
		if (gid == 12) {
			gid = 1;
		}
		Byte os = Byte.valueOf(req.getParameter("os"));
		String msg = req.getParameter("msg");
		
		int ilen = com.mpig.api.utils.StringUtils.length(msg);
		if (ilen > 64) {
			returnModel.setCode(CodeContant.nicknameLen);
			returnModel.setMessage("超过弹幕字数限制");
			writeJson(resp, returnModel);
			return;
		}
		String string = SensitiveWordsService.getInstance().replaceSensitiveWords(msg, msg);
		if ("failed".equalsIgnoreCase(string)) {
			returnModel.setCode(CodeContant.updSignatuerSensit);
			returnModel.setMessage("涉及到敏感词");
			writeJson(resp, returnModel);
			return;
		}
		String bagFlag = req.getParameter("bagflag");
		
		if (bagFlag != null && "1".equals(bagFlag) ) {
			roomService.sendDanmakuBag(srcUid, dstUid, gid, os, msg, returnModel);
		}else {
			roomService.sendDanmaku(srcUid, dstUid, gid, os, msg, returnModel);
		}
		writeJson(resp, returnModel);
	}
	
	/**
	 * 喇叭
	 *
	 * @param req
	 * @param resp
	 */
	@RequestMapping(value = "horn", method = RequestMethod.GET)
	public void sendHorn(HttpServletRequest req, HttpServletResponse resp) {
		if (ParamHandleUtils.isBlank(req, "ncode", "os", "imei", "reqtime", "token", "msg","gid", "anchoruid")) {
			returnModel.setCode(CodeContant.ConParamIsEmptyOrNull);
			returnModel.setMessage("缺少参数或参数为空");
			writeJson(resp, returnModel);
			return;
		}
		if (!ParamHandleUtils.isInt(req, "os", "reqtime", "anchoruid","gid")) {
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

		String token = req.getParameter("token");// 充值操作对象
		int srcUid = authService.decryptToken(token, returnModel);
		if (srcUid <= 0) {
			writeJson(resp, returnModel);
			return;
		}
		int anchoruid = Integer.parseInt(req.getParameter("anchoruid"));
		int gid = Integer.valueOf(req.getParameter("gid"));
		Byte os = Byte.valueOf(req.getParameter("os"));
		String msg = req.getParameter("msg");
		
		int ilen = com.mpig.api.utils.StringUtils.length(msg);
		if (ilen > 64) {
			returnModel.setCode(CodeContant.nicknameLen);
			returnModel.setMessage("超过喇叭字数限制");
			writeJson(resp, returnModel);
			return;
		}
		
		String string = SensitiveWordsService.getInstance().replaceSensitiveWords(msg, msg);
		if ("failed".equalsIgnoreCase(string)) {
			returnModel.setCode(CodeContant.updSignatuerSensit);
			returnModel.setMessage("涉及到敏感词");
			writeJson(resp, returnModel);
			return;
		}
		
		String bagFlag = req.getParameter("bagflag");
		if (bagFlag != null && "1".equals(bagFlag)) {
			roomService.sendHornBag(srcUid, anchoruid, gid, os, msg, returnModel);
		}else {
			roomService.sendHorn(srcUid, anchoruid, gid, os, msg, returnModel);
		}
		writeJson(resp, returnModel);
	}

	/**
	 * 用户送礼
	 *
	 * @param req
	 * @param resp
	 */
	@RequestMapping(value = "sendgift", method = RequestMethod.GET)
	public void sendGift(HttpServletRequest req, HttpServletResponse resp) {

		if (ParamHandleUtils.isBlank(req, "ncode", "os", "imei", "reqtime", "token", "artistid", "gid", "count",
				"combo")) {
			returnModel.setCode(CodeContant.ConParamIsEmptyOrNull);
			returnModel.setMessage("缺少参数或参数为空");
			writeJson(resp, returnModel);
			return;
		}
		if (!ParamHandleUtils.isInt(req, "os", "reqtime", "artistid", "gid", "count", "combo")) {
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
		try {
			String stamp = req.getParameter("stamp");
			if (stamp == null) {
				stamp = "";
			}
			String token = req.getParameter("token");
			int srcUid = authService.decryptToken(token, returnModel);
			if (srcUid <= 0) {
				writeJson(resp, returnModel);
				return;
			}
			// =null或未空 dstuid则给主播送礼，否则 但dstuid=anchoruid给主播送礼，否则 给用户送礼
			int anchoruid = req.getParameter("anchoruid") == null ? 0: Integer.valueOf(req.getParameter("anchoruid"));
			
			// 送礼对象
			int dstUid = Integer.parseInt(req.getParameter("artistid"));
			int gid = Integer.parseInt(req.getParameter("gid"));
			
			// if (gid == 12) {
			// gid = 16;
			// }
			int count = Integer.parseInt(req.getParameter("count"));
			Byte os = Byte.valueOf(req.getParameter("os"));
			// TOSY 增加combo字段
			int combo = Integer.parseInt(req.getParameter("combo"));

			// =1 发送背包里的礼物
			String bagFlag = req.getParameter("bagflag");
			//24礼物是人气猪 时间限制 不能连送
			int mengzhuSurplusCount = 0;
			if(gid == 24 && !"1".equals(bagFlag)){
				if(count > 1){
					logger.error(String.format("stamp:%s,srcUid:%d,dstUid:%d,anchoruid:%d,gid:%d,count:%d,combo:%d", stamp, srcUid, dstUid,anchoruid, gid, count,combo));
					count = 1;
				}
				
				// 获取当天送的数量
				mengzhuSurplusCount = ValueaddServiceUtil.getMengzhuSurplusCount(srcUid);
				if (mengzhuSurplusCount <= 0) {
					returnModel.setCode(CodeContant.ConCodeTimes);
					returnModel.setMessage("今天的萌气猪已经送完了，明天再来吧");
					writeJson(resp, returnModel);
					return;
				}
				//时间限制 服务器5分钟计算 送礼只会大于三分钟 不会小于5分总
				if(false == OtherRedisService.getInstance().updateGiftTime(String.valueOf(gid),String.valueOf(srcUid),300)){
					returnModel.setCode(CodeContant.CONFORMAT);
					returnModel.setMessage("人气猪送的太频繁，歇一歇");
					writeJson(resp, returnModel);
					return;
				}
			}
			Map<Integer, ConfigGiftActivityModel>  actGiftMap = configService.getGiftAct(false);
			ConfigGiftActivityModel giftActivityModel = actGiftMap.get(gid);
			if(giftActivityModel != null){
				if(giftActivityModel.getAtype() == 5){
					UserVipInfoModel vipInfoModel = ValueaddServiceUtil.getVipInfo(srcUid);
					if(vipInfoModel == null){
						returnModel.setCode(CodeContant.userInfo_permission_error);
						returnModel.setMessage("亲，开通VIP才可以赠送哦！");
						writeJson(resp, returnModel);
						return;
					}
				}
				if(giftActivityModel.getAtype() == 6){
					UserGuardInfoModel guardInfoModel = ValueaddServiceUtil.getGuardInfo(srcUid, dstUid);
					if(guardInfoModel == null){
						returnModel.setCode(CodeContant.userInfo_permission_error);
						returnModel.setMessage("亲，开通守护才可以赠送哦！");
						writeJson(resp, returnModel);
						return;
					}
				}
			}
			String iosVer = req.getParameter("iosver");
			if ("1".equals(bagFlag)) {
				roomService.sendGiftBag(stamp, srcUid, dstUid,anchoruid, gid, count, os, combo,iosVer, returnModel);
			}else {
				roomService.sendGift(stamp, srcUid, dstUid,anchoruid, gid, count, os, combo, mengzhuSurplusCount,iosVer, returnModel);
			}
			
			

		} catch (NumberFormatException e) {
			returnModel.setCode(CodeContant.CONFORMAT);
			returnModel.setMessage("参数类型错误");
		} catch (Exception e) {
			returnModel.setCode(CodeContant.CONSYSTEMERR);
			returnModel.setMessage("系统繁忙");
			logger.error("<sendGift->Exception>" + e);
		}
		writeJson(resp, returnModel);
	}
	

	/**
	 * 获取用户关注列表
	 *
	 * @param req
	 * @param resp
	 */
	@RequestMapping(value = "getfollows", method = RequestMethod.GET)
	public void getFollows(HttpServletRequest req, HttpServletResponse resp) {
		if("2".equals(req.getParameter("os"))&&StringUtils.isBlank(req.getParameter("appVersion"))) {
			returnModel.setCode(CodeContant.CONLOGIN);
			returnModel.setMessage("请升级为最新版本");
			writeJson(resp, returnModel);
			return;
		}
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
		// 验证auth及token
		authToken(req, true);
		if (returnModel.getCode() != CodeContant.OK) {
			// 验证失败
			writeJson(resp, returnModel);
			return;
		}

		String token = req.getParameter("token");// 充值操作对象
		int srcUid = authService.decryptToken(token, returnModel);
		if (srcUid <= 0) {
			writeJson(resp, returnModel);
			return;
		}
		roomService.getFollowsHome(srcUid, returnModel);
		writeJson(resp, returnModel);
	}

	/**
	 * 主播切换前后台
	 *
	 * @param req
	 * @param resp
	 */
	@RequestMapping(value = "switch", method = RequestMethod.GET)
	public void switchPlate(HttpServletRequest req, HttpServletResponse resp) {

		if (ParamHandleUtils.isBlank(req, "ncode", "os", "imei", "reqtime", "token", "type")) {
			returnModel.setCode(CodeContant.ConParamIsEmptyOrNull);
			returnModel.setMessage("缺少参数或参数为空");
			writeJson(resp, returnModel);
			return;
		}
		if (!ParamHandleUtils.isInt(req, "os", "reqtime", "type")) {
			returnModel.setCode(CodeContant.ConParamTypeIsErr);
			returnModel.setMessage("参数类型错误");
			writeJson(resp, returnModel);
			return;
		}

		String token = req.getParameter("token");// 充值操作对象
		int uid = authService.decryptToken(token, returnModel);
		if (uid <= 0) {
			writeJson(resp, returnModel);
			return;
		}
		// =1 切换到后台 =2 切换到前台
		int type = Integer.valueOf(req.getParameter("type"));

		UserBaseInfoModel userBaseInfoModel = userService.getUserbaseInfoByUid(uid, false);
		if (userBaseInfoModel == null) {
			returnModel.setCode(CodeContant.USERASSETEXITS);
			returnModel.setMessage("主播不存在");
			writeJson(resp, returnModel);
			return;
		}
		if (type == 2) {
			// 切换到前台
			if (!userBaseInfoModel.getLiveStatus()) {
				returnModel.setCode(CodeContant.LivingStatus);
				returnModel.setMessage("未开播");
				writeJson(resp, returnModel);
				return;
			}
			UserRedisService.getInstance().delSwitchPlate(uid);
		} else {
			UserRedisService.getInstance().setSwitchPlate(uid);
		}

		AnchorOffCMod anchorOffCMod = new AnchorOffCMod();
		anchorOffCMod.setAvatar(userBaseInfoModel.getHeadimage());
		anchorOffCMod.setLevel(userBaseInfoModel.getUserLevel());
		anchorOffCMod.setNickname(userBaseInfoModel.getNickname());
		anchorOffCMod.setSex(userBaseInfoModel.getSex());
		anchorOffCMod.setUid(userBaseInfoModel.getUid());
		anchorOffCMod.setType(type);

		String signParams = EncryptUtils.signParams(VarConfigUtils.ServiceSecret, "appKey=" + VarConfigUtils.ServiceKey,
				"msgBody=" + JSONObject.toJSONString(anchorOffCMod), "roomOwner=" + String.valueOf(uid));

		Unirest.post(UrlConfigLib.getUrl("url").getAdminrpc_publish_room()).field("appKey", VarConfigUtils.ServiceKey)
				.field("msgBody", JSONObject.toJSONString(anchorOffCMod)).field("roomOwner", String.valueOf(uid))
				.field("sign", signParams).asStringAsync();
		writeJson(resp, returnModel);
	}

	/**
	 * 获取推荐列表 搜索下面
	 *
	 * @param req
	 * @param resp
	 */
	@RequestMapping(value = "getrecommend", method = RequestMethod.GET)
	public void getRecommend(HttpServletRequest req, HttpServletResponse resp) {
		// getLiveList(req, resp);
		// return;
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
		// 验证auth及token
		authToken(req, true);
		if (returnModel.getCode() != CodeContant.OK) {
			// 验证失败
			writeJson(resp, returnModel);
			return;
		}

		String token = req.getParameter("token");// 充值操作对象
		int os = Integer.valueOf(req.getParameter("os"));
		int srcUid = authService.decryptToken(token, returnModel);
		if (srcUid <= 0) {
			writeJson(resp, returnModel);
			return;
		}

		roomService.getReCommend(os,srcUid, returnModel);
		writeJson(resp, returnModel);
	}
	
	/**
	 * 获取用户基本信息
	 *
	 * @param req
	 * @param resp
	 */
	@RequestMapping(value = "userinfoinroom", method = RequestMethod.GET)
	public void getUserData(HttpServletRequest req, HttpServletResponse resp) {

		if (ParamHandleUtils.isBlank(req, "ncode", "os", "imei", "reqtime", "token", "dstuid", "anchoruid")) {
			returnModel.setCode(CodeContant.ConParamIsEmptyOrNull);
			returnModel.setMessage("缺少参数或参数为空");
			writeJson(resp, returnModel);
			return;
		}
		if (!ParamHandleUtils.isInt(req, "os", "reqtime", "dstuid", "anchoruid")) {
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

		String token = req.getParameter("token");// 充值操作对象
		int uid = authService.decryptToken(token, returnModel);
		if (uid <= 0) {
			writeJson(resp, returnModel);
			return;
		}
		String dstUid = req.getParameter("dstuid");
		String anchoruid = req.getParameter("anchoruid");

		Map<String, Object> map = userService.getUserDataInRoomMap(uid, Integer.valueOf(dstUid),
				Integer.valueOf(anchoruid));
		if (map == null) {
			returnModel.setCode(CodeContant.ACCOUNTEXCEPT);
			returnModel.setMessage("该用户异常，请重新登录");
		} else {
			returnModel.setData(map);
		}
		writeJson(resp, returnModel);
	}

	/**
	 * 添加黑名单
	 * 
	 * @param req
	 * @param resp
	 * @return
	 */
	@RequestMapping(value = "/setblack", method = RequestMethod.GET)
	@ResponseBody
	public ReturnModel setblackUser(HttpServletRequest req, HttpServletResponse resp) {
		if (ParamHandleUtils.isBlank(req, "ncode", "os", "imei", "reqtime", "token", "dstuid")) {
			returnModel.setCode(CodeContant.ConParamIsEmptyOrNull);
			returnModel.setMessage("缺少参数或参数为空");
			return returnModel;
		}
		if (!ParamHandleUtils.isInt(req, "os", "reqtime", "dstuid")) {
			returnModel.setCode(CodeContant.ConParamTypeIsErr);
			returnModel.setMessage("参数类型错误");
			return returnModel;
		}
		// 验证auth及token
		authToken(req, true);
		if (returnModel.getCode() != CodeContant.OK) {
			// 验证失败
			return returnModel;
		}

		String token = req.getParameter("token");// 充值操作对象
		int srcuid = authService.decryptToken(token, returnModel);
		if (srcuid <= 0) {
			return returnModel;
		}
		int dstuid = Integer.valueOf(req.getParameter("dstuid"));

		// 添加黑名单
		boolean ires = RelationRedisService.getInstance().setBlackUser(srcuid, dstuid,
				String.valueOf(System.currentTimeMillis() / 1000)) == null ? false : true;
		if (ires) {
			userService.addFollows("off", srcuid, dstuid);
			if (RelationRedisService.getInstance().isFollows(srcuid, dstuid) != null) {
				// 解除关注关系
				RelationRedisService.getInstance().addFollows(srcuid, dstuid, (double) 0, "off");
			}
			if (RelationRedisService.getInstance().isFollows(dstuid, srcuid) != null) {
				// 解除关注关系
				RelationRedisService.getInstance().addFollows(dstuid, srcuid, (double) 0, "off");
			}
			if (RelationRedisService.getInstance().checkUserInAnchor(dstuid, srcuid) != null) {
				// 提出房间
				roomService.exitRoom(dstuid, srcuid, returnModel);
				UserBaseInfoModel userBaseInfoModel = userService.getUserbaseInfoByUid(dstuid, false);

				// 用户退房通知admin
				try {

					String signParams = EncryptUtils.signParams(VarConfigUtils.ServiceSecret,
							"appKey=" + VarConfigUtils.ServiceKey, "clientIds=" + dstuid,
							"roomOwner=" + String.valueOf(srcuid));

					Unirest.post(UrlConfigLib.getUrl("url").getKick_out()).field("appKey", VarConfigUtils.ServiceKey)
							.field("clientIds", dstuid).field("roomOwner", String.valueOf(srcuid))
							.field("sign", signParams).asJson();

					// 设置成功 广播
					KickCMod msgBody = new KickCMod();
					msgBody.setLevel(userBaseInfoModel.getUserLevel());
					msgBody.setUid(dstuid);
					msgBody.setNickname(userBaseInfoModel.getNickname());
					msgBody.setAvatar(userBaseInfoModel.getHeadimage());

					signParams = EncryptUtils.signParams(VarConfigUtils.ServiceSecret,
							"appKey=" + VarConfigUtils.ServiceKey, "msgBody=" + JSONObject.toJSONString(msgBody),
							"roomOwner=" + String.valueOf(srcuid));

					Unirest.post(UrlConfigLib.getUrl("url").getAdminrpc_publish_room())
							.field("appKey", VarConfigUtils.ServiceKey)
							.field("msgBody", JSONObject.toJSONString(msgBody))
							.field("roomOwner", String.valueOf(srcuid)).asJsonAsync();

					// ExitRoomCMod exitRoomCMod = new ExitRoomCMod();
					// exitRoomCMod.setAvatar(userBaseInfoModel.getHeadimage());
					// exitRoomCMod.setLevel(userBaseInfoModel.getUserLevel());
					// exitRoomCMod.setNickname(userBaseInfoModel.getNickname());
					// exitRoomCMod.setSex(userBaseInfoModel.getSex());
					// exitRoomCMod.setUid(userBaseInfoModel.getUid());
					//
					// Unirest.post(UrlConfigLib.getUrl("url").getAdminrpc_publish_room())
					// .field("roomOwner",
					// String.valueOf(srcuid)).field("appKey",
					// VarConfigUtils.ServiceKey)
					// .field("msgBody",
					// JSONObject.toJSONString(exitRoomCMod)).asStringAsync();
				} catch (Exception e) {
					logger.error("<setblackUser->Adminrpc_publish_room->Exception>" + e.toString());
				}
			}
			returnModel.setMessage("已拉黑");
		} else {
			returnModel.setCode(CodeContant.user_black_err);
			returnModel.setMessage("网络异常，请稍后重试");
		}
		return returnModel;
	}

	/**
	 * 解除黑名单
	 * 
	 * @param req
	 * @param resp
	 * @return
	 */
	@RequestMapping(value = "/unsetblack", method = RequestMethod.GET)
	@ResponseBody
	public ReturnModel unSetblackUser(HttpServletRequest req, HttpServletResponse resp) {
		if (ParamHandleUtils.isBlank(req, "ncode", "os", "imei", "reqtime", "token", "dstuid")) {
			returnModel.setCode(CodeContant.ConParamIsEmptyOrNull);
			returnModel.setMessage("缺少参数或参数为空");
			return returnModel;
		}
		if (!ParamHandleUtils.isInt(req, "os", "reqtime", "dstuid")) {
			returnModel.setCode(CodeContant.ConParamTypeIsErr);
			returnModel.setMessage("参数类型错误");
			return returnModel;
		}
		// 验证auth及token
		authToken(req, true);
		if (returnModel.getCode() != CodeContant.OK) {
			// 验证失败
			return returnModel;
		}

		String token = req.getParameter("token");// 充值操作对象
		int srcuid = authService.decryptToken(token, returnModel);
		if (srcuid <= 0) {
			return returnModel;
		}
		int dstuid = Integer.valueOf(req.getParameter("dstuid"));
		if (StringUtils.isNotEmpty(RelationRedisService.getInstance().getBlackUser(srcuid, dstuid))) {
			Boolean unBlackUser = RelationRedisService.getInstance().unBlackUser(srcuid, dstuid);
			if (!unBlackUser) {
				returnModel.setCode(CodeContant.user_unblack_err);
				returnModel.setMessage("网络异常，请稍后重试");
			}
		}
		return returnModel;
	}

	/**
	 * 获取黑名单列表
	 * 
	 * @param req
	 * @param resp
	 * @return
	 */
	@RequestMapping(value = "/getblacklist", method = RequestMethod.GET)
	@ResponseBody
	public ReturnModel getBlackList(HttpServletRequest req, HttpServletResponse resp) {
		if (ParamHandleUtils.isBlank(req, "ncode", "os", "imei", "reqtime", "token")) {
			returnModel.setCode(CodeContant.ConParamIsEmptyOrNull);
			returnModel.setMessage("缺少参数或参数为空");
			return returnModel;
		}
		if (!ParamHandleUtils.isInt(req, "os", "reqtime")) {
			returnModel.setCode(CodeContant.ConParamTypeIsErr);
			returnModel.setMessage("参数类型错误");
			return returnModel;
		}
		// 验证auth及token
		authToken(req, true);
		if (returnModel.getCode() != CodeContant.OK) {
			// 验证失败
			return returnModel;
		}

		String token = req.getParameter("token");// 充值操作对象
		int srcuid = authService.decryptToken(token, returnModel);
		if (srcuid <= 0) {
			return returnModel;
		}

		Map<String, String> blackList = RelationRedisService.getInstance().getBlackList(srcuid);
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		if (blackList != null) {
			for (Map.Entry<String, String> _map : blackList.entrySet()) {
				Map<String, Object> blackInfo = new HashMap<String, Object>();
				UserBaseInfoModel userBaseInfoModel = userService.getUserbaseInfoByUid(Integer.valueOf(_map.getKey()),
						false);
				if (userBaseInfoModel != null) {
					blackInfo.put("uid", userBaseInfoModel.getUid());
					blackInfo.put("headimage", userBaseInfoModel.getHeadimage());
					blackInfo.put("nickname", userBaseInfoModel.getNickname());
					blackInfo.put("sex", userBaseInfoModel.getSex());
					blackInfo.put("anchorLevel", userBaseInfoModel.getAnchorLevel());
					blackInfo.put("userLevel", userBaseInfoModel.getUserLevel());
					list.add(blackInfo);
				}
			}
		}

		Map<String, Object> maplist = new HashMap<String, Object>();
		maplist.put("list", list);
		returnModel.setData(maplist);

		return returnModel;
	}

	@RequestMapping(value = "/ranks", method = RequestMethod.GET)
	@ResponseBody
	public ReturnModel getRanks(HttpServletRequest req, HttpServletResponse resp) {
		if (ParamHandleUtils.isBlank(req, "ncode", "os", "imei", "reqtime")) {
			returnModel.setCode(CodeContant.ConParamIsEmptyOrNull);
			returnModel.setMessage("缺少参数或参数为空");
			return returnModel;
		}
		if (!ParamHandleUtils.isInt(req, "os", "reqtime")) {
			returnModel.setCode(CodeContant.ConParamTypeIsErr);
			returnModel.setMessage("参数类型错误");
			return returnModel;
		}
		// 验证auth及token
		authToken(req, false);
		if (returnModel.getCode() != CodeContant.OK) {
			// 验证失败
			return returnModel;
		}
		roomService.getRanks(returnModel);
		return returnModel;
	}

	@RequestMapping(value = "/userrank", method = RequestMethod.GET)
	@ResponseBody
	public ReturnModel getUserRank(HttpServletRequest req, HttpServletResponse resp) {
		if (ParamHandleUtils.isBlank(req, "ncode", "os", "imei", "reqtime", "typeuser", "typetimes")) {
			returnModel.setCode(CodeContant.ConParamIsEmptyOrNull);
			returnModel.setMessage("缺少参数或参数为空");
			return returnModel;
		}
		if (!ParamHandleUtils.isInt(req, "os", "reqtime")) {
			returnModel.setCode(CodeContant.ConParamTypeIsErr);
			returnModel.setMessage("参数类型错误");
			return returnModel;
		}
		// 验证auth及token
		authToken(req, false);
		if (returnModel.getCode() != CodeContant.OK) {
			// 验证失败
			return returnModel;
		}

		String token = req.getParameter("token");// 充值操作对象
		int srcuid = authService.decryptToken(token, returnModel);
//		if (srcuid <= 0) {
//			return returnModel;
//		}
		returnModel = new ReturnModel();
		List<String> userList = Arrays.asList("anchor", "user");
		List<String> timesList = Arrays.asList("day", "week", "month", "all");

		String typeUser = req.getParameter("typeuser");
		String typeTimes = req.getParameter("typetimes");
		if (!userList.contains(typeUser) || !timesList.contains(typeTimes)) {
			returnModel.setCode(CodeContant.ConParamTypeIsErr);
			returnModel.setMessage("参数类型错误");
		} else {
			roomService.getUserRank(srcuid, typeUser, typeTimes, returnModel);
		}
		return returnModel;
	}

	@RequestMapping(value = "/userrankRq", method = RequestMethod.GET)
	@ResponseBody
	public ReturnModel getUserRankRq(HttpServletRequest req, HttpServletResponse resp) {
		if (ParamHandleUtils.isBlank(req, "ncode", "os", "imei", "reqtime", "typetimes")) {
			returnModel.setCode(CodeContant.ConParamIsEmptyOrNull);
			returnModel.setMessage("缺少参数或参数为空");
			return returnModel;
		}
		if (!ParamHandleUtils.isInt(req, "os", "reqtime")) {
			returnModel.setCode(CodeContant.ConParamTypeIsErr);
			returnModel.setMessage("参数类型错误");
			return returnModel;
		}
		// 验证auth及token
		authToken(req, false);
		if (returnModel.getCode() != CodeContant.OK) {
			// 验证失败
			return returnModel;
		}

		String token = req.getParameter("token");// 充值操作对象
		int srcuid = authService.decryptToken(token, returnModel);
//		if (srcuid <= 0) {
//			return returnModel;
//		}
		returnModel = new ReturnModel();
		
		List<String> timesList = Arrays.asList("day", "week", "month", "all");

		String typeTimes = req.getParameter("typetimes");
		if (!timesList.contains(typeTimes)) {
			returnModel.setCode(CodeContant.ConParamTypeIsErr);
			returnModel.setMessage("参数类型错误");
		} else {
			roomService.getUserRankRq(srcuid,typeTimes, returnModel);
		}
		return returnModel;
	}


	@RequestMapping(value = "/userrankRqPC", method = RequestMethod.GET)
	@ResponseBody
	public ReturnModel getUserRankRqPC(HttpServletRequest req, HttpServletResponse resp) {
		if (ParamHandleUtils.isBlank(req, "ncode", "os", "imei", "reqtime")) {
			returnModel.setCode(CodeContant.ConParamIsEmptyOrNull);
			returnModel.setMessage("缺少参数或参数为空");
			return returnModel;
		}
		if (!ParamHandleUtils.isInt(req, "os", "reqtime")) {
			returnModel.setCode(CodeContant.ConParamTypeIsErr);
			returnModel.setMessage("参数类型错误");
			return returnModel;
		}
		// 验证auth及token
		authToken(req, false);
		if (returnModel.getCode() != CodeContant.OK) {
			// 验证失败
			return returnModel;
		}

		roomService.getUserRankRqPC(1,3,returnModel);
		return returnModel;
	}
	
	@RequestMapping(value = "/zhouxing", method = RequestMethod.GET)
	@ResponseBody
	public ReturnModel getZhouxin(HttpServletRequest req, HttpServletResponse resp) {
		if (ParamHandleUtils.isBlank(req, "ncode", "os", "imei", "reqtime", "typeuser", "typetimes")) {
			returnModel.setCode(CodeContant.ConParamIsEmptyOrNull);
			returnModel.setMessage("缺少参数或参数为空");
			return returnModel;
		}
		if (!ParamHandleUtils.isInt(req, "os", "reqtime", "typetimes")) {
			returnModel.setCode(CodeContant.ConParamTypeIsErr);
			returnModel.setMessage("参数类型错误");
			return returnModel;
		}
		// 验证auth及token
		authToken(req, false);
		if (returnModel.getCode() != CodeContant.OK) {
			// 验证失败
			return returnModel;
		}

		List<String> userArr = Arrays.asList("anchor", "user");
		List<Integer> timesArr = Arrays.asList(0, 1);
		String typeUser = req.getParameter("typeuser");
		int typeTimes = Integer.valueOf(req.getParameter("typetimes"));

		if (!userArr.contains(typeUser) || !timesArr.contains(typeTimes)) {
			returnModel.setCode(CodeContant.ConParamTypeIsErr);
			returnModel.setMessage("参数值错误");
		} else {
			roomService.getZhouxin(typeUser, typeTimes, returnModel);
		}
		return returnModel;
	}

	/**
	 * 获取PC端 每个礼物的周星第一名用户
	 * @param req
	 * @param resp
	 * @return
	 */
	@RequestMapping(value = "/zhouxingpc", method = RequestMethod.GET)
	@ResponseBody
	public ReturnModel getPCZhouxin(HttpServletRequest req, HttpServletResponse resp) {
		if (ParamHandleUtils.isBlank(req, "ncode", "os", "imei", "reqtime")) {
			returnModel.setCode(CodeContant.ConParamIsEmptyOrNull);
			returnModel.setMessage("缺少参数或参数为空");
			return returnModel;
		}
		if (!ParamHandleUtils.isInt(req, "os", "reqtime")) {
			returnModel.setCode(CodeContant.ConParamTypeIsErr);
			returnModel.setMessage("参数类型错误");
			return returnModel;
		}
		// 验证auth及token
		authToken(req, false);
		if (returnModel.getCode() != CodeContant.OK) {
			// 验证失败
			return returnModel;
		}

		roomService.getZhouxinPC(returnModel);
		return returnModel;
	}
	
	
	@RequestMapping(value = "/zhouxing/person", method = RequestMethod.GET)
	@ResponseBody
	public ReturnModel getZhouxinOfPerson(HttpServletRequest req, HttpServletResponse resp) {
		if (ParamHandleUtils.isBlank(req, "ncode", "os", "imei", "reqtime","anchor")) {
			returnModel.setCode(CodeContant.ConParamIsEmptyOrNull);
			returnModel.setMessage("缺少参数或参数为空");
			return returnModel;
		}
		if (!ParamHandleUtils.isInt(req, "os", "reqtime", "anchor")) {
			returnModel.setCode(CodeContant.ConParamTypeIsErr);
			returnModel.setMessage("参数类型错误");
			return returnModel;
		}
		// 验证auth及token
		authToken(req, false);
		if (returnModel.getCode() != CodeContant.OK) {
			// 验证失败
			return returnModel;
		}
		
		int anchor = Integer.valueOf(req.getParameter("anchor"));

		roomService.getZhouxinOfPerson(anchor, returnModel);

		return returnModel;
	}
	
	/**
	 * 最新里的推荐列表
	 * @param req
	 * @param resp
	 * @return
	 */
	@RequestMapping("/getrecomment/new")
	@ResponseBody
	public ReturnModel getRecommendByNew(HttpServletRequest req, HttpServletResponse resp){
		if (ParamHandleUtils.isBlank(req, "ncode", "os", "imei", "reqtime")) {
			returnModel.setCode(CodeContant.ConParamIsEmptyOrNull);
			returnModel.setMessage("缺少参数或参数为空");
			return returnModel;
		}
		if (!ParamHandleUtils.isInt(req, "os", "reqtime")) {
			returnModel.setCode(CodeContant.ConParamTypeIsErr);
			returnModel.setMessage("参数类型错误");
			return returnModel;
		}

		String token = req.getParameter("token");
		int srcUid = 0;
		if(StringUtils.isNotBlank(token) && !token.equals("null")){
			// 验证auth及token
			authToken(req, true);
			if (returnModel.getCode() != CodeContant.OK) {
				return returnModel;
			}
			srcUid = authService.decryptToken(token, returnModel);	
		}
		
		
		roomService.getRecommandByNow(srcUid, returnModel);
		
		return returnModel;
	}
	
	/**
	 * 应用宝直播大厅接口
	 * @param req
	 * @param resp
	 * @return
	 */
	@RequestMapping("/yyb/radio_list")
	@ResponseBody
	public Object getLivingForeign(HttpServletRequest req, HttpServletResponse resp){
		HashMap<String, Object> result = new HashMap<String, Object>(){
			private static final long serialVersionUID = -5456877055900324416L;
			{
				put("ret", 0);
				put("msg", "OK");
			}
		};
		
		if (ParamHandleUtils.isBlank(req, "timestamp", "sig")) {
			result.put("ret", CodeContant.ConParamIsEmptyOrNull);
			result.put("msg", "缺少参数或参数为空");
			result.put("radio_list", Lists.newArrayList());
			return result;
		}
		String timestamp = req.getParameter("timestamp");
		String sig = req.getParameter("sig");
		String content = SecretKey+timestamp;
		try {
			String md5 = EncryptUtils.md5ByCharset(content, Charsets.UTF_8);
			if(!md5.equals(sig)){
				result.put("ret", CodeContant.TencentYYBSignatureFail);
				result.put("msg", "请求签名异常");
				result.put("radio_list", Lists.newArrayList());
			}else{
				roomService.getLivingForeign(result);
			}
		} catch (Exception e) {
			logger.error("getLivingForeign>>md5 failed",e);
			result.put("ret", CodeContant.TencentYYBSignatureFail);
			result.put("msg", "请求签名加密异常");
			result.put("radio_list", Lists.newArrayList());
		}
		return result;
	}
	
	/**
	 * 用户注册第一次登陆时推荐10个用户
	 * @param req
	 * @param resp
	 * @return
	 */
	@RequestMapping("/getrecomment/foRegist")
	@ResponseBody
	public ReturnModel getRecommendForReg(HttpServletRequest req, HttpServletResponse resp){
		roomService.getRecommendForRegister(returnModel);
		return returnModel;
	}
	
	/**
	 * check2ndStream
	 */

	@RequestMapping(value = "check2ndStream", method = RequestMethod.GET)
	public void check2ndStream(HttpServletRequest req, HttpServletResponse resp) {
		if (ParamHandleUtils.isBlank(req, "ncode", "os", "imei", "reqtime", "token","roomid")) {
			returnModel.setCode(CodeContant.ConParamIsEmptyOrNull);
			returnModel.setMessage("缺少参数或参数为空");
			writeJson(resp, returnModel);
			return;
		}

		Integer roomid = Integer.valueOf(req.getParameter("roomid"));
		if(null == roomid){
			returnModel.setCode(CodeContant.ConParamIsEmptyOrNull);
			returnModel.setMessage("参数错误");
			writeJson(resp, returnModel);
			return;			
		}
		
		liveService.check2ndStream(roomid,returnModel);
		
		writeJson(resp, returnModel);
	}
	
	/**
	 * PC端 主播下播，推送2个主播信息
	 * @param req
	 * @param resp
	 * @return
	 */
	@RequestMapping(value="recommend2flash")
	@ResponseBody
	public ReturnModel recommendTwoFlash(HttpServletRequest req,HttpServletResponse resp){
		String uid = req.getParameter("uid");
		if (StringUtils.isEmpty(uid)) {
			
		}else {
			roomService.getRecommendTwoFlash(uid,2, returnModel);
		}
		return returnModel;
	}
	
	/**
	 * PC端 随机换主播
	 * @param req
	 * @param resp
	 * @return
	 */
	@RequestMapping(value="randomAnchor")
	@ResponseBody
	public ReturnModel randomAnchor(HttpServletRequest req,HttpServletResponse resp){
		if (ParamHandleUtils.isBlank(req, "ncode", "os", "imei", "reqtime","uid")) {
			returnModel.setCode(CodeContant.ConParamIsEmptyOrNull);
			returnModel.setMessage("缺少参数或参数为空");
			return returnModel;
		}
		if (!ParamHandleUtils.isInt(req, "os", "reqtime","uid")) {
			returnModel.setCode(CodeContant.ConParamTypeIsErr);
			returnModel.setMessage("参数类型错误");
			return returnModel;
		}

		// 验证auth及token
		authToken(req, true);
		if (returnModel.getCode() != CodeContant.OK) {
			return returnModel;
		}
		
		String uid = req.getParameter("uid"); // 上一个主播UID
		if (StringUtils.isEmpty(uid)) {
			
		}else {
			roomService.randomAnchor(uid,returnModel);
		}
		return returnModel;
	}
	
	/**
	 * 周星达人
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/weekStar")
	@ResponseBody
	public ReturnModel weekStar(HttpServletRequest request, HttpServletResponse response){
		Map<String, String> hgetAll = RedisCommService.getInstance().hgetAll(RedisContant.RedisNameUser,RedisContant.WeekTitle);
		List<Object> userList = new ArrayList<Object>();
			for(String gid: hgetAll.keySet()){
				UserBaseInfoModel userBaseInfoModel = userService.getUserbaseInfoByUid(Integer.parseInt(hgetAll.get(gid)), false);
				if(userBaseInfoModel != null){
					Map<String,Object> map = new HashMap<String,Object>();
					map.put("status", userBaseInfoModel.getLiveStatus());
					map.put("uid", userBaseInfoModel.getUid());
					map.put("nickname", userBaseInfoModel.getNickname().trim());
					map.put("headimage", userBaseInfoModel.getHeadimage());
					map.put("anchorLevel", userBaseInfoModel.getAnchorLevel());
					map.put("enters", roomService.getRoomShowUsers(userBaseInfoModel.getUid(), userBaseInfoModel.getContrRq()));
					map.put("mobileliveimg", userBaseInfoModel.getLivimage());
					map.put("opentime", userBaseInfoModel.getOpentime());
					map.put("sex", userBaseInfoModel.getSex());
					map.put("gid", Integer.parseInt(gid));
					map.put("pcimg1", userBaseInfoModel.getPcimg1());
					map.put("pcimg2", userBaseInfoModel.getPcimg2());
					String stream = configService.getThirdStream(userBaseInfoModel.getUid());
					int thirdstream = 0;
					if (null == stream) {
						map.put("domain",
								liveService.getVideoConfig(0, userBaseInfoModel.getUid(), userBaseInfoModel.getVideoline()).get("domain"));
					} else {
						thirdstream = 1;
						map.put("domain", stream);
					}
					map.put("thirdstream", thirdstream);
					map.put("verified", userBaseInfoModel.isVerified());
					userList.add(map);
				}
			}
		Map<String,Object> data = new HashMap<String,Object>();
		data.put("list", userList);
		returnModel.setData(data);
		return returnModel;
	}
	
	/**
	 * 首页抓娃娃、6中3两款游戏 最热的两个直播间
	 * @param req
	 * @param resp
	 * @return
	 */
	@RequestMapping("/hot/gameAnchorList")
	@ResponseBody
	public ReturnModel getHotGameAnchorList(HttpServletRequest req, HttpServletResponse resp){
		if (ParamHandleUtils.isBlank(req, "ncode", "os", "imei", "reqtime")) {
			returnModel.setCode(CodeContant.ConParamIsEmptyOrNull);
			returnModel.setMessage("缺少参数或参数为空");
			return returnModel;
		}
		if (!ParamHandleUtils.isInt(req, "os", "reqtime")) {
			returnModel.setCode(CodeContant.ConParamTypeIsErr);
			returnModel.setMessage("参数类型错误");
			return returnModel;
		}
		// 验证auth及token
		authToken(req, false);
		if (returnModel.getCode() != 200) {
			return returnModel;
		}

		String token = req.getParameter("token");// 充值操作对象
		Integer srcUid = 0;
		if (StringUtils.isEmpty(token) || "null".equalsIgnoreCase(token) || "(null)".equalsIgnoreCase(token)) {
			srcUid = null;
		}else {
			srcUid = authService.decryptToken(token, returnModel);
			if (srcUid <= 0) {
				return returnModel;
			}
		}
		Map<String,Object> data = new HashMap<String,Object>();
		int os = Integer.valueOf(req.getParameter("os"));
		Map<String,String> map =OtherRedisService.getInstance().hgetAll(RedisContant.gameHashList);
		data.put("gameKey_001", new ArrayList<Map<String,Object>>());
		data.put("gameKey_002", new ArrayList<Map<String,Object>>());
		for (Entry<String, String> entry : map.entrySet()) {
			RoomGameInfoModel roomGameInfoModel = JSONObject.parseObject(entry.getValue(), RoomGameInfoModel.class);
			if("gameKey_001".equals(roomGameInfoModel.getGameKey())||"gameKey_002".equals(roomGameInfoModel.getGameKey())) {
				List<Map<String,Object>> list = roomService.getHotGameAnchorList(roomGameInfoModel.getGameId(), srcUid, os);
				data.put(roomGameInfoModel.getGameKey(), list);
			}
		}
		
		returnModel.setData(data);
		return returnModel;
	}
	
	/**
	 * 根据游戏的Key获取直播间信息
	 * @param req
	 * @param resp
	 * @return
	 */
	@RequestMapping("/hot/gameAnchorListByGameKey")
	@ResponseBody
	public ReturnModel gameAnchorListByGameKey(HttpServletRequest req, HttpServletResponse resp){
		if (ParamHandleUtils.isBlank(req, "ncode", "os", "imei", "reqtime","gameKey")) {
			returnModel.setCode(CodeContant.ConParamIsEmptyOrNull);
			returnModel.setMessage("缺少参数或参数为空");
			return returnModel;
		}
		if (!ParamHandleUtils.isInt(req, "os", "reqtime", "page")) {
			returnModel.setCode(CodeContant.ConParamTypeIsErr);
			returnModel.setMessage("参数类型错误");
			return returnModel;
		}
		// 验证auth及token
		authToken(req, false);
		if (returnModel.getCode() != 200) {
			return returnModel;
		}

		String token = req.getParameter("token");// 充值操作对象
		Integer srcUid = 0;
		if (StringUtils.isEmpty(token) || "null".equalsIgnoreCase(token) || "(null)".equalsIgnoreCase(token)) {
			srcUid = null;
		}else {
			srcUid = authService.decryptToken(token, returnModel);
			if (srcUid <= 0) {
				return returnModel;
			}
		}
		int os = Integer.valueOf(req.getParameter("os"));
		String gameKey = req.getParameter("gameKey");
		long gameId = 0;
		Map<String,String> map =OtherRedisService.getInstance().hgetAll(RedisContant.gameHashList);
		for (Entry<String, String> entry : map.entrySet()) {
			RoomGameInfoModel roomGameInfoModel = JSONObject.parseObject(entry.getValue(), RoomGameInfoModel.class);
			if(gameKey.equals(roomGameInfoModel.getGameKey())) {
				gameId = roomGameInfoModel.getGameId();
				break;
			}
		}
		int page = Integer.valueOf(req.getParameter("page"));
		List<Map<String,Object>> list = roomService.gameAnchorListByGameId(gameId, srcUid, os, page);
		returnModel.setData(list);
		return returnModel;
	}

/**
	 * 获取（最新主播）接口
	 * 
	 * @param req
	 * @param resp
	 */
	@RequestMapping(value = "getRoomInfoByAnchorId", method = RequestMethod.GET)
	public void getRoomInfoByAnchorId(HttpServletRequest req, HttpServletResponse resp) {
		if (ParamHandleUtils.isBlank(req, "ncode", "os", "imei", "reqtime","anchorId")) {
			returnModel.setCode(CodeContant.ConParamIsEmptyOrNull);
			returnModel.setMessage("缺少参数或参数为空");
			writeJson(resp, returnModel);
			return;
		}
		if (!ParamHandleUtils.isInt(req, "os", "reqtime", "anchorId")) {
			returnModel.setCode(CodeContant.ConParamTypeIsErr);
			returnModel.setMessage("参数类型错误");
			writeJson(resp, returnModel);
			return;
		}
		authToken(req, false);
		if (returnModel.getCode() != 200) {
			writeJson(resp, returnModel);
			return;
		}
		String token = req.getParameter("token");
		int uid = authService.decryptToken(token, returnModel);
		if (uid <= 0) {
			writeJson(resp, returnModel);
			return;
		}
		int os = Integer.valueOf(req.getParameter("os"));
		Integer anchorId = Integer.parseInt(req.getParameter("anchorId"));
		roomService.getRoomInfoByAnchorId(uid,anchorId,os,returnModel);
		writeJson(resp, returnModel);
	}
	
	/**
	 * 获取主播声援值月榜
	 * 
	 * @param req
	 * @param resp
	 */
	@RequestMapping("/getAnchorMonthSupportRank")
	@ResponseBody
	public ReturnModel getAnchorMonthSupportRank(HttpServletRequest req, HttpServletResponse resp){
		if (ParamHandleUtils.isBlank(req, "ncode", "os", "imei", "reqtime")) {
			returnModel.setCode(CodeContant.ConParamIsEmptyOrNull);
			returnModel.setMessage("缺少参数或参数为空");
			return returnModel;
		}
		if (!ParamHandleUtils.isInt(req, "os", "reqtime")) {
			returnModel.setCode(CodeContant.ConParamTypeIsErr);
			returnModel.setMessage("参数类型错误");
			return returnModel;
		}
		// 验证auth及token
		authToken(req, false);
		if (returnModel.getCode() != 200) {
			return returnModel;
		}

		String token = req.getParameter("token");// 充值操作对象
		Integer srcUid = 0;
		if (StringUtils.isEmpty(token) || "null".equalsIgnoreCase(token) || "(null)".equalsIgnoreCase(token)) {
			srcUid = null;
		}else {
			srcUid = authService.decryptToken(token, returnModel);
			if (srcUid <= 0) {
				return returnModel;
			}
		}
		roomService.getAnchorMonthSupportRank(srcUid, returnModel);
		return returnModel;
	}
}
