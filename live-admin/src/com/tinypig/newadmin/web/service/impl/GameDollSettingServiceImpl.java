package com.tinypig.newadmin.web.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tinypig.admin.redis.service.RedisOperat;
import com.tinypig.admin.util.RedisContant;
import com.tinypig.newadmin.web.dao.GameDollSettingDao;
import com.tinypig.newadmin.web.entity.GameDollSetting;
import com.tinypig.newadmin.web.service.GameDollSettingService;
@Service
public class GameDollSettingServiceImpl implements GameDollSettingService{
	
	@Autowired
	private GameDollSettingDao gameDollSettingDao;

	@Override
	public GameDollSetting getGameDollSetting() {
		return gameDollSettingDao.getGameDollSetting();
	}

	@Override
	public int saveGameDollSetting(GameDollSetting gameDollSetting) {
		return gameDollSettingDao.insertSelective(gameDollSetting);
	}

	@Override
	public int updateGameDollSetting(GameDollSetting gameDollSetting) {
		return gameDollSettingDao.updateByPrimaryKeySelective(gameDollSetting);
	}

	@Override
	public Map<String, Object> reGameDollSettingRedis() {
		Map<String,Object> resMap = new HashMap<String,Object>();
		GameDollSetting gameDollSetting = gameDollSettingDao.getGameDollSetting();
		RedisOperat.getInstance().del(RedisContant.host, RedisContant.port6379, RedisContant.gameDollTypeClaw);
		RedisOperat.getInstance().del(RedisContant.host, RedisContant.port6379, RedisContant.gameDollInformMoney);
		if(gameDollSetting != null) {
			RedisOperat.getInstance().hset(RedisContant.host, RedisContant.port6379,
					RedisContant.gameDollTypeClaw, "claw1", String.valueOf(gameDollSetting.getClaw1()), 0);
			RedisOperat.getInstance().hset(RedisContant.host, RedisContant.port6379,
					RedisContant.gameDollTypeClaw, "claw2", String.valueOf(gameDollSetting.getClaw2()), 0);
			RedisOperat.getInstance().hset(RedisContant.host, RedisContant.port6379,
					RedisContant.gameDollTypeClaw, "claw3", String.valueOf(gameDollSetting.getClaw3()), 0);
			RedisOperat.getInstance().hset(RedisContant.host, RedisContant.port6379, 
					RedisContant.gameDollInformMoney, "roomInformMoney", String.valueOf(gameDollSetting.getRoomInformMoney()), 0);
			RedisOperat.getInstance().hset(RedisContant.host, RedisContant.port6379, 
					RedisContant.gameDollInformMoney, "platformInformMoney", String.valueOf(gameDollSetting.getPlatformInformMoney()), 0);
		}
		resMap.put("code", "200");
		resMap.put("msg", "更新缓存成功");
		return resMap;
	}

}
