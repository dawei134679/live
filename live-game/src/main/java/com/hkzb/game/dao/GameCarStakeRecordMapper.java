package com.hkzb.game.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.hkzb.game.dto.GameCarStakeRecordDto;
import com.hkzb.game.dto.StakeRecordParamDto;
import com.hkzb.game.model.GameCarStakeRecord;

public interface GameCarStakeRecordMapper {
    int deleteByPrimaryKey(Long id);

    int insert(GameCarStakeRecord record);

    int insertSelective(GameCarStakeRecord record);

    GameCarStakeRecord selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(GameCarStakeRecord record);
    
    int updateByPrimaryKeyAndStakeTimeSelective(@Param("record") GameCarStakeRecord record,@Param("oldStakeTime") Long oldStakeTime);

    int updateByPrimaryKey(GameCarStakeRecord record);
    
    int updateBatchByPrimaryKey(List<GameCarStakeRecord> list);
    
    List<GameCarStakeRecordDto> getStakeRecord(StakeRecordParamDto param);
    
    GameCarStakeRecord getStakeRecordByParam(GameCarStakeRecord param);
}