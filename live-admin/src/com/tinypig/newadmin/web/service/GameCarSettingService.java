package com.tinypig.newadmin.web.service;

import java.util.List;
import java.util.Map;

import com.tinypig.newadmin.web.entity.GameCarSetting;

public interface GameCarSettingService {
	List<GameCarSetting> getGameCarSettingList();
	
	int saveGameCarConfig(GameCarSetting gameCarSetting);
	
	int updateGameCarConfig(GameCarSetting gameCarSetting);
	
	Map<String, Object> reGameCarSettingRedis();
	
}
