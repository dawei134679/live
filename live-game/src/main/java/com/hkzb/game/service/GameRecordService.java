package com.hkzb.game.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hkzb.game.dao.GameRecordMapper;
import com.hkzb.game.model.GameRecord;

@Service
@Transactional
public class GameRecordService implements IGameRecordService {

	@Autowired
	private GameRecordMapper gameRecordDao;

	@Override
	public int saveGameRecord(GameRecord gameRecord) {
		return gameRecordDao.insertSelective(gameRecord);
	}

}
