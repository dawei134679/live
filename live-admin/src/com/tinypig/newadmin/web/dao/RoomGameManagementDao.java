package com.tinypig.newadmin.web.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.tinypig.newadmin.web.entity.RoomGameManagement;

public interface RoomGameManagementDao {
	
	List<RoomGameManagement> getRoomGameList(@Param("name") String name, @Param("status") Integer status,
			@Param("startIndex") Integer startIndex, @Param("pageSize") Integer pageSize);;
	
    int deleteByPrimaryKey(Long id);

    int insert(RoomGameManagement record);

    int insertSelective(RoomGameManagement record);

    RoomGameManagement selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(RoomGameManagement record);

    int updateByPrimaryKey(RoomGameManagement record);
    
    int doValid(@Param("id") Long id, @Param("status") Integer status, @Param("updateUserId") Long updateUserId, @Param("updateTime") Long updateTime);

    List<RoomGameManagement> getRoomGame();
}



