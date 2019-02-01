package com.tinypig.newadmin.web.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.tinypig.newadmin.web.entity.GameDoll;

public interface GameDollDao {
	
	List<GameDoll> getDollList(@Param("name") String name, @Param("status") Integer status,
			@Param("startIndex") Integer startIndex, @Param("pageSize") Integer pageSize);
	
	int doValid(@Param("id") Long id, @Param("status") Integer status, @Param("updateUserId") Long updateUserId, @Param("updateTime") Long updateTime);
	
    int deleteByPrimaryKey(Integer id);

    int insert(GameDoll record);

    int insertSelective(GameDoll record);

    GameDoll selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(GameDoll record);

    int updateByPrimaryKey(GameDoll record);
    
    List<GameDoll> getAllDollList();
}