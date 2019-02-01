package com.mpig.api.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mashape.unirest.http.Unirest;
import com.mpig.api.dictionary.lib.TaskConfigLib;
import com.mpig.api.dictionary.lib.UrlConfigLib;
import com.mpig.api.model.ReturnModel;
import com.mpig.api.model.UserBaseInfoModel;
import com.mpig.api.modelcomet.MessageCMod;
import com.mpig.api.redis.service.RedisCommService;
import com.mpig.api.redis.service.RelationRedisService;
import com.mpig.api.service.IUserService;
import com.mpig.api.task.TaskService;
import com.mpig.api.utils.CodeContant;
import com.mpig.api.utils.EncryptUtils;
import com.mpig.api.utils.ParamHandleUtils;
import com.mpig.api.utils.RedisContant;
import com.mpig.api.utils.VarConfigUtils;

@Controller
@Scope("prototype")
@RequestMapping("/social")
public class SocialController extends BaseController {

	@Resource
	private IUserService userService;

	/**
	 * 发私信
	 * 
	 * @param req
	 * @param resp
	 */
	@RequestMapping(value = "/sendprivatemsg", method = RequestMethod.GET)
	@ResponseBody
	public ReturnModel sendPrivateLetter(HttpServletRequest req, HttpServletResponse resp) {
		if (ParamHandleUtils.isBlank(req, "ncode", "os", "imei", "reqtime", "token", "dstuid", "msg")) {
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

		String msg = req.getParameter("msg").trim();
		String dstuid = req.getParameter("dstuid");
		if (StringUtils.isEmpty(msg)) {
			returnModel.setCode(CodeContant.Pri_Msg_Empty);
			returnModel.setMessage("私信内容不能为空");
			return returnModel;
		}
		// 判断dstuid是否拒绝接受srcuid的消息 =false 拒收 =true 没有拒收
//		Boolean ispushFollow = RelationRedisService.getInstance().getPushFollow(Integer.valueOf(dstuid), srcuid);
		
		// 判断srcuid 是否给dstuid拉黑
		String blackUser = RelationRedisService.getInstance().getBlackUser(Integer.valueOf(dstuid),srcuid);
//		if (!ispushFollow || StringUtils.isNotEmpty(blackUser)) {
		if (StringUtils.isNotEmpty(blackUser)) {
			returnModel.setCode(CodeContant.Pri_Msg_pushErr);
			returnModel.setMessage("对方拒绝接收您的消息");
			return returnModel;
		}
		// 判断是否关注或是否为粉丝
		int itype= 0;
		Double isfollows = RelationRedisService.getInstance().isFollows(srcuid, Integer.valueOf(dstuid));
		if (isfollows == null) {
			Double isfan = RelationRedisService.getInstance().isFollows(Integer.valueOf(dstuid),srcuid);
			if (isfan == null) {
				itype = 1;
			}else {
				itype = 2; //dstuid是srcuid粉丝 即dstuid收到关注对象的私信
			}
		}else {
			Double isfan = RelationRedisService.getInstance().isFollows(Integer.valueOf(dstuid),srcuid);
			if (isfan == null) {
				itype = 1; //srcuid 是 dstuid粉丝  即dstuid收到粉丝的私信
			}else {
				itype = 2; //dstuid 是 srcuid粉丝  即dstuid收到关注对象的私信
			}
		}

		UserBaseInfoModel userBaseInfoModel = userService.getUserbaseInfoByUid(srcuid, false);
		UserBaseInfoModel dstUserBase = userService.getUserbaseInfoByUid(Integer.valueOf(dstuid), false);
		if (userBaseInfoModel == null || dstUserBase == null) {
			returnModel.setCode(CodeContant.ACCOUNTEXCEPT);
			returnModel.setMessage("用户信息异常");
			return returnModel;
		}
		
		// 获取消息ID
		Long msgId = RedisCommService.getInstance().incrBy(RedisContant.RedisNameOther, RedisContant.PrivateMsgId, 1, 0); 
		
		if (msgId == null) {
			returnModel.setCode(CodeContant.Pri_Msg_idErr);
			returnModel.setMessage("消息发送失败，请稍后再发");
			return returnModel;
		}
		
		Long lgNow = System.currentTimeMillis() / 1000;
		// 组装消息体
		MessageCMod msgBody = new MessageCMod();
		msgBody.setMsgId(msgId.intValue());
		msgBody.setUid(srcuid);
		msgBody.setNickname(userBaseInfoModel.getNickname());
		msgBody.setHeadimage(userBaseInfoModel.getHeadimage());
		msgBody.setSex(userBaseInfoModel.getSex());
		msgBody.setLevel(userBaseInfoModel.getUserLevel());
		msgBody.setMsg(msg);
		msgBody.setSendtime(lgNow);
		msgBody.setRelation(itype);
		msgBody.setMsgType(0);

		String msgbody = JSONObject.toJSONString(msgBody);
		// 添加redis
		RedisCommService.getInstance().hset(RedisContant.RedisNameUser, RedisContant.PrivateMsg + dstuid, msgId.toString(), msgbody, 30*24*60*60);
				
		String signParams = EncryptUtils.signParams(VarConfigUtils.ServiceSecret,
				"appKey=" + VarConfigUtils.ServiceKey, "msgBody=" + msgbody,
				"users=" + dstuid);
		
		Unirest.post(UrlConfigLib.getUrl("url").getAdminrpc_publish_live()).field("appKey", VarConfigUtils.ServiceKey)
				.field("msgBody", msgbody).field("users", dstuid).field("sign", signParams).asJsonAsync();

		//TOSY TASK
		TaskService.getInstance().taskProcess(srcuid, TaskConfigLib.SendLetter, 1);
		
		Map<String, Object> map = new HashMap<String,Object>();
		map.put("msgId", msgId.toString());
		map.put("dstuid", dstuid);
		map.put("srcuid", srcuid);
		map.put("sendtime", lgNow);
		
		returnModel.setData(map);
		return returnModel;
	}

	/**
	 * 获取用户的消息
	 * 
	 * @param req
	 * @param resp
	 * @return
	 */
	@RequestMapping(value = "/getprivatemsg", method = RequestMethod.GET)
	@ResponseBody
	public ReturnModel getPrivateMsgOfUid(HttpServletRequest req, HttpServletResponse resp) {
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

		Map<String, String> privateMsgs = RedisCommService.getInstance().hgetAll(RedisContant.RedisNameUser, RedisContant.PrivateMsg+srcuid);
		
		List<Object> list = new ArrayList<Object>();
		if (privateMsgs != null) {
			for (Map.Entry<String, String> _map : privateMsgs.entrySet()) {
				list.add(JSONObject.parse(_map.getValue()));
			}
		}
		
		// 检查是否有官方私信   ************** start
		String string = RedisCommService.getInstance().get(RedisContant.RedisNameUser, RedisContant.KeySysMsg);
		if (StringUtils.isNotEmpty(string)) {
			
			List<Map> sysMsgList = JSON.parseArray(string,Map.class);
			// 是否有官方私信
			if (sysMsgList != null && sysMsgList.size() > 0) {
				
				// 记录最新的官方私信ID
				String newMsgId = "";
				
				String sysMsgId = RedisCommService.getInstance().get(RedisContant.RedisNameUser, RedisContant.KeySysMsg + ":" + srcuid);
				
				for(Map<String, Object> map : sysMsgList){
					
					Map<String, Object> _map = new HashMap<String,Object>();
					if (StringUtils.isEmpty(sysMsgId)) {
						// 没有读过官方私信
						_map.put("msgId", map.get("id"));
						_map.put("headimage", "");
						_map.put("uid", 0);
						_map.put("nickname", "官方");
						_map.put("headimage", "");
						_map.put("sex", true);
						_map.put("level", 0);
						_map.put("msg", map.get("content"));
						_map.put("url", map.get("url"));
						_map.put("sendtime", map.get("sendtime"));
						_map.put("relation", 0);
						_map.put("msgType", StringUtils.isEmpty(map.get("url").toString())?0:5);
						newMsgId = map.get("id").toString();
					}else {
						// 有读过官方私信
						if (Integer.valueOf(sysMsgId) < Integer.valueOf(map.get("id").toString())) {
							_map.put("msgId", map.get("id"));
							_map.put("headimage", "");
							_map.put("uid", 0);
							_map.put("nickname", "官方");
							_map.put("headimage", "");
							_map.put("sex", true);
							_map.put("level", 0);
							_map.put("msg", map.get("content"));
							_map.put("url", map.get("url"));
							_map.put("sendtime", map.get("sendtime"));
							_map.put("relation", 0);
							_map.put("msgType", StringUtils.isEmpty(map.get("url").toString())?0:5);
							newMsgId = map.get("id").toString();
						}
					}
					if (_map.size() > 0) {
						list.add(_map);
					}
				}
				if (StringUtils.isNotEmpty(newMsgId)) {
					RedisCommService.getInstance().set(RedisContant.RedisNameUser,RedisContant.KeySysMsg+":"+srcuid, newMsgId, 7*24*3600);
				}
			}
		}
		// 检查是否有官方私信   ************** end
		
		Map<String, Object> map = new HashMap<String,Object>();
		map.put("list", list);
		returnModel.setData(map);
		return returnModel;
	}

	/**
	 * 已读标志
	 * @param req
	 * @param resp
	 * @return
	 */
	@RequestMapping(value = "/readprivatemsg", method = RequestMethod.GET)
	@ResponseBody
	public ReturnModel delPrivateMsg(HttpServletRequest req, HttpServletResponse resp) {
		if (ParamHandleUtils.isBlank(req, "ncode", "os", "imei", "reqtime", "token", "msgIds")) {
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
		String msgIds = req.getParameter("msgIds");
		if (StringUtils.isEmpty(msgIds)) {
			returnModel.setCode(CodeContant.Pri_Msg_IdsEmpty);
			returnModel.setMessage("消息ID不能为空");
			return returnModel;
		}
		
		RedisCommService.getInstance().hdel(RedisContant.RedisNameUser, RedisContant.PrivateMsg+srcuid, msgIds.split(","));

		return returnModel;
	}
}
