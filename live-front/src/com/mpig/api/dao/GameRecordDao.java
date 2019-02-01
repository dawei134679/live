package com.mpig.api.dao;

public interface GameRecordDao {

	int saveGameRecord(Long uid, Long roomId, int type, Long price, Long giftTotalPrice, long ctime);

}
