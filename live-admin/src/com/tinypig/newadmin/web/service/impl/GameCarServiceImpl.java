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
import com.tinypig.newadmin.web.dao.GameCarDao;
import com.tinypig.newadmin.web.entity.GameCar;
import com.tinypig.newadmin.web.entity.GameCarConfigDto;
import com.tinypig.newadmin.web.service.GameCarService;

@Service
public class GameCarServiceImpl implements GameCarService {

	@Autowired
	private GameCarDao gameCarDao;
	
	@Override
	public List<GameCar> getCarList(String name, Integer status, Integer page, Integer rows) {
		Integer startIndex = (page - 1) * rows;
		return gameCarDao.getCarList(name, status, startIndex, rows);
	}

	@Override
	public Map<String, Object> doValid(Long id, Integer status, Long updateUserId, Long updateTime) {
		int row = gameCarDao.doValid(id, status, updateUserId, updateTime);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("row", row);
		return map;
	}

	@Override
	public Map<String, Object> saveCar(GameCar gCar) {
		int row = gameCarDao.saveCar(gCar);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("row", row);
		return map;
	}

	@Override
	public Map<String, Object> updateCar(GameCar gameCar) {
		int row = gameCarDao.updateByPrimaryKeySelective(gameCar);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("row", row);
		return map;
	}

	@Override
	public Map<String, Object> reGameCarConfigRedis() {
		Map<String, Object> map = new HashMap<String, Object>();
		List<GameCarConfigDto> result = new ArrayList<GameCarConfigDto>();
		List<GameCar> gameCarConfigList = gameCarDao.getGameCarConfig();
		Map<String,Map<Long,Double>> gameCarPRAndBS = new HashMap<String,Map<Long,Double>>();
		Map<Long,Double> pr1 = new HashMap<Long,Double>();
		Map<Long,Double> pr2 = new HashMap<Long,Double>();
		Map<Long,Double> pr3 = new HashMap<Long,Double>();
		Map<Long,Double> bs1 = new HashMap<Long,Double>();
		Map<Long,Double> bs2 = new HashMap<Long,Double>();
		Map<Long,Double> bs3 = new HashMap<Long,Double>();
		Double sumProbability1 = 0d,sumProbability2 = 0d,sumProbability3 = 0d;//记录概率相加
		for (GameCar gameCar : gameCarConfigList) {
			GameCarConfigDto gameCarConfigDto = new GameCarConfigDto();
			gameCarConfigDto.setId(gameCar.getId());
			gameCarConfigDto.setName(gameCar.getName());
			gameCarConfigDto.setImg(gameCar.getImg());
			result.add(gameCarConfigDto);
			pr1.put(gameCar.getId(),gameCar.getProbability1());
			pr2.put(gameCar.getId(),gameCar.getProbability2());
			pr3.put(gameCar.getId(),gameCar.getProbability3());
			bs1.put(gameCar.getId(),gameCar.getMultiple1());
			bs2.put(gameCar.getId(),gameCar.getMultiple2());
			bs3.put(gameCar.getId(),gameCar.getMultiple3());
			sumProbability1+=gameCar.getProbability1();
			sumProbability2+=gameCar.getProbability2();
			sumProbability3+=gameCar.getProbability3();
		}
		if(sumProbability1 != 1 || sumProbability2 != 1 || sumProbability3 != 1) {
			map.put("code", 201);
			if(sumProbability2 != 1) {
				map.put("msg", "概率2之和必须为1");
			}else if(sumProbability3 != 1){
				map.put("msg", "概率3之和必须为1");
			}else {
				map.put("msg", "概率1之和必须为1");
			}
			return map;
		}
		RedisOperat.getInstance().del(RedisContant.host, RedisContant.port6379, RedisContant.gameCarConfig);
		RedisOperat.getInstance().del(RedisContant.host, RedisContant.port6379, RedisContant.gameCarPRAndBS);
		gameCarPRAndBS.put("probability1", pr1);
		gameCarPRAndBS.put("probability2", pr2);
		gameCarPRAndBS.put("probability3", pr3);
		gameCarPRAndBS.put("multiple1", bs1);
		gameCarPRAndBS.put("multiple2", bs2);
		gameCarPRAndBS.put("multiple3", bs3);
		RedisOperat.getInstance().set(RedisContant.host, RedisContant.port6379, RedisContant.gameCarPRAndBS,JSONObject.toJSONString(gameCarPRAndBS));
		RedisOperat.getInstance().set(RedisContant.host, RedisContant.port6379, RedisContant.gameCarConfig,JSONObject.toJSONString(result));
		map.put("code", "200");
		return map;
	}

	@Override
	public Map<String, Object> getSumProbability(Long id) {
		return gameCarDao.getSumProbability(id);
	}
}
