package com.tinypig.newadmin.web.dao;

import java.util.List;

import com.tinypig.newadmin.web.entity.GameGraspdollRecord;
import com.tinypig.newadmin.web.entity.GameGraspdollRecordParamDto;

public interface GameGraspdollRecordDao {
    int deleteByPrimaryKey(Long id);

    int insert(GameGraspdollRecord record);

    int insertSelective(GameGraspdollRecord record);

    GameGraspdollRecord selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(GameGraspdollRecord record);

    int updateByPrimaryKey(GameGraspdollRecord record);
    
    List<GameGraspdollRecord> getGameGraspdollRecordPage(GameGraspdollRecordParamDto param);
    
    Long getGameGraspdollRecordTotalCount(GameGraspdollRecordParamDto param);
    
    GameGraspdollRecord getGameGraspdollRecordTotal(GameGraspdollRecordParamDto param);
}