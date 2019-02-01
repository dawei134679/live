package com.mpig.api.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.alibaba.fastjson.JSONObject;
import com.mpig.api.redis.service.OtherRedisService;
import com.mpig.api.utils.CodeContant;
import com.mpig.api.utils.ParamHandleUtils;
import com.mpig.api.utils.RedisContant;

@Controller
@Scope("prototype")
@RequestMapping("roomGame")
public class RoomGameController extends BaseController {

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/getAllRoomGame", method = RequestMethod.GET)
	public void getAllRoomCame(HttpServletRequest req, HttpServletResponse resp) {
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
			writeJson(resp, returnModel);
			return;
		}
		String token = req.getParameter("token");
		Integer uid = authService.decryptToken(token, returnModel);
		if (uid==null||uid <= 0) {
			writeJson(resp, returnModel);
			return;
		}
		String json = OtherRedisService.getInstance().get(RedisContant.RedisNameOther,RedisContant.gameList);
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		if(!StringUtils.isBlank(json)) {
			list = JSONObject.parseObject(json, List.class);
			returnModel.setData(list);
		}
		returnModel.setCode(CodeContant.OK);
		returnModel.setMessage("查询成功");
		writeJson(resp, returnModel);
		return;
	}
}
