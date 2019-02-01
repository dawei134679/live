package com.tinypig.newadmin.web.service;

import java.util.List;
import java.util.Map;

import com.tinypig.newadmin.web.entity.RoomGameManagement;

public interface GameManagementService {
	List<RoomGameManagement> getRoomGameList(String name, Integer status, Integer page, Integer rows);

	int saveRoomGame(RoomGameManagement roomGameManagement);

	Map<String, Object> doValid(Long id, Integer status, Long updateUserId, Long updateTime);

	int updateRoomGame(RoomGameManagement roomGameManagement);

	Map<String, Object> reRoomGameRedis();
}
