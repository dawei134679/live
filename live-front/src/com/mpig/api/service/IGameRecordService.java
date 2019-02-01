package com.mpig.api.service;

public interface IGameRecordService {

	int addGameRecord(Long uid, Long roomId, int type, Long price, Long giftTotalPrice, long ctime);

}
