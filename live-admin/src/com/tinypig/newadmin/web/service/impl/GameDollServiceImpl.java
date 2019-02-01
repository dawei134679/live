package com.tinypig.newadmin.web.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.tinypig.admin.redis.service.RedisOperat;
import com.tinypig.admin.util.RedisContant;
import com.tinypig.newadmin.web.dao.GameDollDao;
import com.tinypig.newadmin.web.entity.GameDoll;
import com.tinypig.newadmin.web.entity.GameDollConfigDto;
import com.tinypig.newadmin.web.service.GameDollService;

@Service
public class GameDollServiceImpl implements GameDollService{
	
	@Autowired
	private GameDollDao gameDollDao;
	
	@Override
	public List<GameDoll> getDollList(String name, Integer status, Integer page, Integer rows) {
		Integer startIndex = (page - 1) * rows;
		return gameDollDao.getDollList(name, status, startIndex, rows);
	}

	@Override
	public Map<String, Object> doValid(Long id, Integer status, Long updateUserId, Long updateTime) {
		int row = gameDollDao.doValid(id, status, updateUserId, updateTime);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("row", row);
		return map;
	}

	@Override
	public int saveDoll(GameDoll gameDoll) {
		return gameDollDao.insertSelective(gameDoll);
	}

	@Override
	public int updateDoll(GameDoll gameDoll) {
		return gameDollDao.updateByPrimaryKeySelective(gameDoll);
	}

	@Override
	public Map<String, Object> reGameCarConfigRedis() {
		Map<String, Object> map = new HashMap<String, Object>();
		RedisOperat.getInstance().del(RedisContant.host, RedisContant.port6379, RedisContant.gameDollConfig);
		List<GameDollConfigDto> result = new ArrayList<GameDollConfigDto>();
		List<GameDoll> gameDollList = gameDollDao.getAllDollList();
		for(GameDoll gDoll : gameDollList) {
			GameDollConfigDto gameDollConfigDto = new GameDollConfigDto();
			gameDollConfigDto.setId(gDoll.getId());
			gameDollConfigDto.setName(gDoll.getName());
			gameDollConfigDto.setImageUrl(gDoll.getImageUrl());
			gameDollConfigDto.setProbability(gDoll.getProbability());
			gameDollConfigDto.setMultiple(gDoll.getMultiple());
			result.add(gameDollConfigDto);
		}
		RedisOperat.getInstance().set(RedisContant.host, RedisContant.port6379, RedisContant.gameDollConfig,JSONObject.toJSONString(result));
		map.put("code", "200");
		return map;
	}
}
