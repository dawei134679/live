package com.hkzb.game.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hkzb.game.dao.GameGraspDollRecordMapper;
import com.hkzb.game.model.GameGraspDollRecord;

@Service
@Transactional
public class GameGraspDollRecordService implements IGameGraspDollRecordService {

	@Autowired
	private GameGraspDollRecordMapper gameGraspDollRecordMapper;

	@Override
	public int saveGameGraspDollRecord(GameGraspDollRecord gameGraspDollRecord) {
		return gameGraspDollRecordMapper.insertSelective(gameGraspDollRecord);
	}

	@Override
	public GameGraspDollRecord selectByPrimaryKey(Long id) {
		return gameGraspDollRecordMapper.selectByPrimaryKey(id);
	}

}
