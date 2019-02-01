package com.mpig.api.dao.impl;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.mpig.api.SqlTemplete;
import com.mpig.api.dao.GameRecordDao;
import com.mpig.api.db.DBHelper;
import com.mpig.api.utils.VarConfigUtils;

@Repository
public class GameRecordDaoImpl implements GameRecordDao {
	private static final Logger logger = Logger.getLogger(GameRecordDaoImpl.class);

	@Override
	public int saveGameRecord(Long uid, Long roomId, int type, Long price, Long giftTotalPrice, long ctime) {
		try {
			int executeResult = DBHelper.execute(VarConfigUtils.dbLiveGame, SqlTemplete.SQL_insGameRecord,false,uid,roomId,type,price,giftTotalPrice,ctime);
			return executeResult;
		} catch (Exception e) {
			logger.error("<addMallInfo->Exception>" + e.toString());
		}
		return -1;
	}

}
