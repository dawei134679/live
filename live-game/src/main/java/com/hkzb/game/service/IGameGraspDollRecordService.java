package com.hkzb.game.service;

import com.hkzb.game.model.GameGraspDollRecord;

public interface IGameGraspDollRecordService {

	public int saveGameGraspDollRecord(GameGraspDollRecord gameGraspDollRecord);

	public GameGraspDollRecord selectByPrimaryKey(Long id);

}
