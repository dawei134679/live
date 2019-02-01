package com.tinypig.newadmin.web.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tinypig.admin.redis.service.RedisOperat;
import com.tinypig.admin.util.RedisContant;
import com.tinypig.newadmin.web.dao.GameCarSettingDao;
import com.tinypig.newadmin.web.entity.GameCarSetting;
import com.tinypig.newadmin.web.service.GameCarSettingService;

@Service
public class GameCarSettingServiceImpl implements GameCarSettingService {

	@Autowired
	private GameCarSettingDao gameCarSettingDao;

	@Override
	public List<GameCarSetting> getGameCarSettingList() {
		return gameCarSettingDao.getGameCarSettingList();
	}

	@Override
	public int updateGameCarConfig(GameCarSetting gameCarSetting) {
		return gameCarSettingDao.updateByPrimaryKeySelective(gameCarSetting);
	}

	@Override
	public Map<String, Object> reGameCarSettingRedis() {
		Map<String,Object> resMap = new HashMap<String,Object>();
		List<GameCarSetting> list = gameCarSettingDao.getGameCarSettingList();
		RedisOperat.getInstance().del(RedisContant.host, RedisContant.port6379, RedisContant.gameCarCommission);
		RedisOperat.getInstance().del(RedisContant.host, RedisContant.port6379, RedisContant.gameCarLotteryTypePR);
		RedisOperat.getInstance().del(RedisContant.host, RedisContant.port6379, RedisContant.gameInformMoney);
		for (GameCarSetting bean : list) {
			if (bean != null) {
				RedisOperat.getInstance().set(RedisContant.host, RedisContant.port6379, RedisContant.gameCarCommission,
						String.valueOf(bean.getGameCommission()));
				RedisOperat.getInstance().hset(RedisContant.host, RedisContant.port6379,
						RedisContant.gameCarLotteryTypePR, "probability1", String.valueOf(bean.getProbability1()), 0);
				RedisOperat.getInstance().hset(RedisContant.host, RedisContant.port6379,
						RedisContant.gameCarLotteryTypePR, "probability2", String.valueOf(bean.getProbability2()), 0);
				RedisOperat.getInstance().hset(RedisContant.host, RedisContant.port6379,
						RedisContant.gameCarLotteryTypePR, "probability3", String.valueOf(bean.getProbability3()), 0);
				RedisOperat.getInstance().hset(RedisContant.host, RedisContant.port6379, 
						RedisContant.gameInformMoney, "roomInformMoney", String.valueOf(bean.getRoomInformMoney()), 0);
				RedisOperat.getInstance().hset(RedisContant.host, RedisContant.port6379, 
						RedisContant.gameInformMoney, "platformInformMoney", String.valueOf(bean.getPlatformInformMoney()), 0);
				break;
			}
		}
		resMap.put("code", "200");
		resMap.put("msg", "更新缓存成功");
		return resMap;
	}

	@Override
	public int saveGameCarConfig(GameCarSetting gameCarSetting) {
		return gameCarSettingDao.insertSelective(gameCarSetting);
	}

}