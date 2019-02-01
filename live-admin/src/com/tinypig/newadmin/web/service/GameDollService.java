package com.tinypig.newadmin.web.service;

import java.util.List;
import java.util.Map;

import com.tinypig.newadmin.web.entity.GameDoll;

public interface GameDollService {
	List<GameDoll> getDollList(String name, Integer status, Integer page, Integer rows);

	Map<String, Object> doValid(Long id, Integer status, Long updateUserId, Long updateTime);

	int saveDoll(GameDoll gameDoll);

	int updateDoll(GameDoll gameDoll);

	Map<String, Object> reGameCarConfigRedis();
	
}
