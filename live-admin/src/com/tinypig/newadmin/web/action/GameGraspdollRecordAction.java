package com.tinypig.newadmin.web.action;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.tinypig.newadmin.web.entity.GameGraspdollRecordParamDto;
import com.tinypig.newadmin.web.service.IGameGraspdollRecordService;

@Controller
@RequestMapping("gameGraspdollRecord")
public class GameGraspdollRecordAction {
	
	@Autowired
	private IGameGraspdollRecordService gameGraspdollRecordService;
	
	@RequestMapping(value = "/getGameGraspdollRecordPage", produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String getGameGraspdollRecordPage(HttpServletRequest req,GameGraspdollRecordParamDto param) {
		Map<String, Object> map = gameGraspdollRecordService.getGameGraspdollRecordPage(param);
		return JSONObject.toJSONString(map);
	}
	
	@RequestMapping(value = "/getGameGraspdollRecordTotal", produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String getBetTotal(HttpServletRequest req,GameGraspdollRecordParamDto param) {
		return JSONObject.toJSONString(gameGraspdollRecordService.getGameGraspdollRecordTotal(param));
	}
}
