package com.mpig.api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mpig.api.dao.GameRecordDao;

@Service
public class GameRecordService implements IGameRecordService {

	@Autowired
	private GameRecordDao gameRecordDao;
	
	@Override
	public int addGameRecord(Long uid, Long roomId, int type, Long price, Long giftTotalPrice, long ctime) {
		return gameRecordDao.saveGameRecord(uid,roomId,type,price,giftTotalPrice,ctime);
	}

}
