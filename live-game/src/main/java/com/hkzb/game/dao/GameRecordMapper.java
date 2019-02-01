package com.hkzb.game.dao;

import com.hkzb.game.model.GameRecord;

public interface GameRecordMapper {
    int deleteByPrimaryKey(Long id);

    int insert(GameRecord record);

    int insertSelective(GameRecord record);

    GameRecord selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(GameRecord record);

    int updateByPrimaryKey(GameRecord record);
}