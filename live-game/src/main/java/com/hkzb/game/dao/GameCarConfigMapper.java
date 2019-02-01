package com.hkzb.game.dao;

import java.util.List;

import com.hkzb.game.model.GameCarConfig;

public interface GameCarConfigMapper {
	
    int deleteByPrimaryKey(Long id);

    int insert(GameCarConfig record);

    int insertSelective(GameCarConfig record);

    GameCarConfig selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(GameCarConfig record);

    int updateByPrimaryKey(GameCarConfig record);
    
    List<GameCarConfig> getGameCarConfig();
}