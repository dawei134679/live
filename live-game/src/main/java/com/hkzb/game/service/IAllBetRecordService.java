package com.hkzb.game.service;

import com.hkzb.game.model.BetRecord;

public interface IAllBetRecordService {
	/**
	 * 押注异常的记录统计
	 */
	public void insertBetRecord(BetRecord betRecord);
	
	BetRecord getBetRecord(Long periods, Long uid);
}
