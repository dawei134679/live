package com.tinypig.newadmin.web.action;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.tinypig.admin.model.AdminUserModel;
import com.tinypig.newadmin.common.ConstantsAction;
import com.tinypig.newadmin.web.entity.GameCarSetting;
import com.tinypig.newadmin.web.service.GameCarSettingService;

@Controller
@RequestMapping("/gcs")
public class GameCarSettingAction {

	@Autowired
	private GameCarSettingService gameCarSettingService;
	
	@RequestMapping(value="/gameCarSettingList")
	@ResponseBody
	public String gameCarSettingList() {
		List<GameCarSetting> list = gameCarSettingService.getGameCarSettingList();
		return JSONObject.toJSONString(list);
	}
	
	@RequestMapping(value="/saveGameCarSetting",produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String saveGameCarConfig(HttpServletRequest request,GameCarSetting gameCarSetting) {
		Map<String, Object> resultMap = new HashMap<String,Object>();
		gameCarSetting.setCreateTime(new Date().getTime());
		AdminUserModel adminUser = (AdminUserModel) request.getSession().getAttribute("currentUser");
		gameCarSetting.setCreateUserId(Long.valueOf(adminUser == null ? 0 : adminUser.getUid()));
		int row = gameCarSettingService.saveGameCarConfig(gameCarSetting);
		if(row==1){
			resultMap.put(ConstantsAction.RESULT, true);
			resultMap.put(ConstantsAction.MSG, "保存成功");
		}else {
			resultMap.put(ConstantsAction.RESULT, false);
			resultMap.put(ConstantsAction.MSG, "保存失败");
		}
		return JSONObject.toJSONString(resultMap);
	}
	
	@RequestMapping(value="/updateGameCarSetting",produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String updateGameCarConfig(HttpServletRequest request,GameCarSetting gameCarSetting) {
		Map<String, Object> resultMap = new HashMap<String,Object>();
		gameCarSetting.setUpdateTime(new Date().getTime());
		AdminUserModel adminUser = (AdminUserModel) request.getSession().getAttribute("currentUser");
		gameCarSetting.setUpdateUserId(Long.valueOf(adminUser == null ? 0 : adminUser.getUid()));
		int row = gameCarSettingService.updateGameCarConfig(gameCarSetting);
		if(row==1){
			resultMap.put(ConstantsAction.RESULT, true);
			resultMap.put(ConstantsAction.MSG, "保存成功");
		}else {
			resultMap.put(ConstantsAction.RESULT, false);
			resultMap.put(ConstantsAction.MSG, "保存失败");
		}
		return JSONObject.toJSONString(resultMap);
	}
	
	@RequestMapping(value="/reGameCarSettingRedis",produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String reGameCarSettingRedis() {
		Map<String, Object> map = gameCarSettingService.reGameCarSettingRedis();
		return JSONObject.toJSONString(map);
	}
	
}

