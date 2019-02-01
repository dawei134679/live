package com.tinypig.newadmin.web.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.tinypig.newadmin.web.entity.GameCar;

public interface GameCarDao {
	
	List<GameCar> getCarList(@Param("name") String name, @Param("status") Integer status,
			@Param("startIndex") Integer startIndex, @Param("pageSize") Integer pageSize);

	int doValid(@Param("id") Long id, @Param("status") Integer status, @Param("updateUserId") Long updateUserId, @Param("updateTime") Long updateTime);

	int saveCar(GameCar gCar);

	int updateByPrimaryKeySelective(GameCar gameCar);

	List<GameCar> getGameCarConfig();
	
	Map<String, Object> getSumProbability(Long id);
}
