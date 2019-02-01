package com.tinypig.newadmin.web.dao;

import com.tinypig.newadmin.web.entity.GameDollSetting;

public interface GameDollSettingDao {
    int deleteByPrimaryKey(Integer id);

    int insert(GameDollSetting record);

    int insertSelective(GameDollSetting record);

    GameDollSetting selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(GameDollSetting record);

    int updateByPrimaryKey(GameDollSetting record);
    
    GameDollSetting getGameDollSetting();
}