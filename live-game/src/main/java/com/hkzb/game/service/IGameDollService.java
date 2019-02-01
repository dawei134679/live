package com.hkzb.game.service;

import java.util.List;
import java.util.Map;

import com.hkzb.game.dto.GameDollConfigDto;
import com.hkzb.game.dto.ResultDto;
import com.hkzb.game.model.GameGraspDollRecord;

public interface IGameDollService {

	public ResultDto<List<GameDollConfigDto>> getGameDollConfig();

	public ResultDto<Object> grab(GameGraspDollRecord gameGraspDollRecord);

	public ResultDto<Map<String, Object>> getGameDollMoney();

	public ResultDto<Object> pushGameDollMsg(Long gameDollRecordId);

}
