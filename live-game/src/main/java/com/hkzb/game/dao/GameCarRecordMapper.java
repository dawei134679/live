package com.hkzb.game.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.hkzb.game.dto.LotteryRecordParamDto;
import com.hkzb.game.dto.WinningResultDto;
import com.hkzb.game.model.GameCarRecord;

public interface GameCarRecordMapper {
    int deleteByPrimaryKey(Long id);

    int insert(GameCarRecord record);

    int insertSelective(GameCarRecord record);

    GameCarRecord selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(GameCarRecord record);

    int updateByPrimaryKey(GameCarRecord record);
    
    Long getMaxPeriods();

	List<GameCarRecord> getLotteryRecord(LotteryRecordParamDto param);

	GameCarRecord getLotteryByPeriods(@Param("periods") Long periods);

	int updateGameCarRecord(GameCarRecord param);

	WinningResultDto getWinningResultByPeriods(@Param("periods") Long periods, @Param("anchorId") Long anchorId,@Param("uid") Long uid);
}