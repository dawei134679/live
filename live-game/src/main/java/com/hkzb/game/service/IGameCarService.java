package com.hkzb.game.service;

import java.util.List;
import java.util.Map;

import com.hkzb.game.dto.GameCarConfigResultDto;
import com.hkzb.game.dto.GameCarStakeRecordDto;
import com.hkzb.game.dto.LotteryRecordParamDto;
import com.hkzb.game.dto.ResultDto;
import com.hkzb.game.dto.StakeRecordParamDto;
import com.hkzb.game.dto.WinningResultDto;
import com.hkzb.game.model.GameCarRecord;
import com.hkzb.game.model.GameCarStakeRecord;

public interface IGameCarService {

	public ResultDto<List<GameCarConfigResultDto>> getGameCarConfig();
	
	public ResultDto<Map<String,Long>> getGameCarTime();
	
	public Map<String,Object> getUserStakeAndTotalStake(Long uid);
	
	public ResultDto<Object> stake(GameCarStakeRecord param);
	
	public ResultDto<Object> saveGameCarRecord(GameCarRecord param);
	
	public ResultDto<Object> saveGameCarRecordSelective(GameCarRecord param);

	public Long getMaxPeriods();

	public int updateCarStakeRecordById(List<GameCarStakeRecord> list);
	
	public void lottery();

	public ResultDto<List<GameCarStakeRecordDto>> getStakeRecord(StakeRecordParamDto param);

	public ResultDto<List<GameCarRecord>> getLotteryRecord(LotteryRecordParamDto param);

	public ResultDto<WinningResultDto> getLotteryByPeriods(Long periods, Long uid);

	public ResultDto<Object> updateGameCarRecord(GameCarRecord param);
}
