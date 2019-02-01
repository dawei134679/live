package com.tinypig.newadmin.web.dao;

import java.util.List;

import com.tinypig.newadmin.web.entity.GameCarSetting;

public interface GameCarSettingDao {
    int deleteByPrimaryKey(Integer id);

    int insert(GameCarSetting record);

    int insertSelective(GameCarSetting record);

    GameCarSetting selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(GameCarSetting record);

    int updateByPrimaryKey(GameCarSetting record);
    
    List<GameCarSetting> getGameCarSettingList();
}