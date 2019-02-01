package com.hkzb.game.dao;

import com.hkzb.game.model.GameGraspDollRecord;

public interface GameGraspDollRecordMapper {
    int deleteByPrimaryKey(Long id);

    int insert(GameGraspDollRecord record);

    int insertSelective(GameGraspDollRecord record);

    GameGraspDollRecord selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(GameGraspDollRecord record);

    int updateByPrimaryKey(GameGraspDollRecord record);
}