package com.tinypig.newadmin.web.action;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.tinypig.admin.model.AdminUserModel;
import com.tinypig.newadmin.common.ConstantsAction;
import com.tinypig.newadmin.web.entity.GameDollSetting;
import com.tinypig.newadmin.web.service.GameDollSettingService;

@Controller
@RequestMapping("/gds")
public class GameDollSettingAction {
	
	@Autowired
	private GameDollSettingService gameDollSettingService;
	
	@RequestMapping(value="/getGameDollSetting")
	@ResponseBody
	public String getGameDollSetting() {
		GameDollSetting gameDollSetting = gameDollSettingService.getGameDollSetting();
		return JSONObject.toJSONString(gameDollSetting);
	}
	
	@RequestMapping(value="/saveGameDollSetting",produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String saveGameDollSetting(HttpServletRequest request, GameDollSetting gameDollSetting) {
		Map<String, Object> resultMap = new HashMap<String,Object>();
		gameDollSetting.setCreateTime(new Date().getTime());
		AdminUserModel adminUser = (AdminUserModel) request.getSession().getAttribute("currentUser");
		gameDollSetting.setCreateUserId(Long.valueOf(adminUser == null ? 0 : adminUser.getUid()));
		int row = gameDollSettingService.saveGameDollSetting(gameDollSetting);
		if(row==1){
			resultMap.put(ConstantsAction.RESULT, true);
			resultMap.put(ConstantsAction.MSG, "保存成功");
		}else {
			resultMap.put(ConstantsAction.RESULT, false);
			resultMap.put(ConstantsAction.MSG, "保存失败");
		}
		return JSONObject.toJSONString(resultMap);
	}
	
	@RequestMapping(value="/updateGameDollSetting",produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String updateGameDollSetting(HttpServletRequest request, GameDollSetting gameDollSetting) {
		Map<String, Object> resultMap = new HashMap<String,Object>();
		gameDollSetting.setUpdateTime(new Date().getTime());
		AdminUserModel adminUser = (AdminUserModel) request.getSession().getAttribute("currentUser");
		gameDollSetting.setUpdateUserId(Long.valueOf(adminUser == null ? 0 : adminUser.getUid()));
		int row = gameDollSettingService.updateGameDollSetting(gameDollSetting);
		if(row==1){
			resultMap.put(ConstantsAction.RESULT, true);
			resultMap.put(ConstantsAction.MSG, "修改成功");
		}else {
			resultMap.put(ConstantsAction.RESULT, false);
			resultMap.put(ConstantsAction.MSG, "修改失败");
		}
		return JSONObject.toJSONString(resultMap);
	}
	@RequestMapping(value="/reGameDollSettingRedis",produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String reGameDollSettingRedis() {
		Map<String, Object> map = gameDollSettingService.reGameDollSettingRedis();
		return JSONObject.toJSONString(map);
	}
}







