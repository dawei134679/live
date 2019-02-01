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
import com.tinypig.newadmin.web.dao.RoomGameManagementDao;
import com.tinypig.newadmin.web.entity.RoomGameManagement;
import com.tinypig.newadmin.web.entity.RoomGameManagementDto;
import com.tinypig.newadmin.web.service.GameManagementService;
@Service
public class GameManagementServiceImpl implements GameManagementService {
	
	@Autowired
	private RoomGameManagementDao roomGameManagementDao;
	
	@Override
	public List<RoomGameManagement> getRoomGameList(String name, Integer status, Integer page, Integer rows) {
		Integer startIndex = (page - 1) * rows;
		return roomGameManagementDao.getRoomGameList(name, status, startIndex, rows);
	}

	@Override
	public int saveRoomGame(RoomGameManagement roomGameManagement) {
		return roomGameManagementDao.insertSelective(roomGameManagement);
	}

	@Override
	public Map<String, Object> doValid(Long id, Integer status, Long updateUserId, Long updateTime) {
		int row = roomGameManagementDao.doValid(id, status, updateUserId, updateTime);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("row", row);
		return map;
	}

	@Override
	public int updateRoomGame(RoomGameManagement roomGameManagement) {
		return roomGameManagementDao.updateByPrimaryKeySelective(roomGameManagement);
	}

	@Override
	public Map<String, Object> reRoomGameRedis() {
		Map<String, Object> map = new HashMap<String, Object>();
		RedisOperat.getInstance().del(RedisContant.host, RedisContant.port6379, RedisContant.gameList);
		RedisOperat.getInstance().del(RedisContant.host, RedisContant.port6379, RedisContant.gameHashList);
		List<Map<String,Object>> result = new ArrayList<Map<String,Object>>();
		List<RoomGameManagement> roomGameManagementList = roomGameManagementDao.getRoomGame();
		for(RoomGameManagement roomGameManagement : roomGameManagementList) {
			RoomGameManagementDto roomGameManagementDto = new RoomGameManagementDto();
			roomGameManagementDto.setGameId(roomGameManagement.getId());
			roomGameManagementDto.setGameKey(roomGameManagement.getGameKey());
			roomGameManagementDto.setName(roomGameManagement.getName());
			roomGameManagementDto.setType(roomGameManagement.getType());
			roomGameManagementDto.setInitUrl(roomGameManagement.getInitUrl());
			roomGameManagementDto.setPageUrl(roomGameManagement.getPageUrl());
			roomGameManagementDto.setImgUrl(roomGameManagement.getImgUrl());
			roomGameManagementDto.setDestoryUrl(roomGameManagement.getDestoryUrl());
			roomGameManagementDto.setServerUrl(roomGameManagement.getServerUrl());
			roomGameManagementDto.setGameIconUrl(roomGameManagement.getGameIconUrl());
			roomGameManagementDto.setGameCommission(roomGameManagement.getGameCommission());
			
			Map<String,Object> data = new HashMap<String,Object>();
			data.put("gameId", roomGameManagement.getId());
			data.put("name", roomGameManagement.getName());
			data.put("type", roomGameManagement.getType());
			data.put("serverUrl", roomGameManagement.getServerUrl());
			data.put("pageUrl", roomGameManagement.getPageUrl());
			data.put("imgUrl", roomGameManagement.getImgUrl());
			data.put("gameKey", roomGameManagement.getGameKey());
			result.add(data);
			RedisOperat.getInstance().hset(RedisContant.host, RedisContant.port6379, 
					RedisContant.gameHashList,String.valueOf(roomGameManagement.getId()),
					JSONObject.toJSONString(roomGameManagementDto),0);
		}
		RedisOperat.getInstance().set(RedisContant.host, RedisContant.port6379, RedisContant.gameList,JSONObject.toJSONString(result));
		map.put("code", "200");
		return map;
	}

}
