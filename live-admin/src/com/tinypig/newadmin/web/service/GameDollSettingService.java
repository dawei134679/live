package com.tinypig.newadmin.web.service;

import java.util.Map;

import com.tinypig.newadmin.web.entity.GameDollSetting;

public interface GameDollSettingService {
	
	GameDollSetting getGameDollSetting();
	
	int saveGameDollSetting(GameDollSetting gameDollSetting);
	
	int updateGameDollSetting(GameDollSetting gameDollSetting);
	
	Map<String, Object> reGameDollSettingRedis();

}
