package com.tinypig.newadmin.web.service;

import java.util.List;
import java.util.Map;

import com.tinypig.newadmin.web.entity.GameCar;

public interface GameCarService {
	List<GameCar> getCarList(String name, Integer status, Integer page, Integer rows);

	Map<String, Object> doValid(Long id, Integer status, Long updateUserId, Long updateTime);

	Map<String, Object> saveCar(GameCar gCar);

	Map<String, Object> updateCar(GameCar gameCar);

	Map<String, Object> reGameCarConfigRedis();
	
	Map<String, Object> getSumProbability(Long id);
}
