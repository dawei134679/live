package com.hkzb.game.service;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.hkzb.game.dao.AllBetRecordMapper;
import com.hkzb.game.model.BetRecord;

@Service
public class AllBetRecordService implements IAllBetRecordService{

	@Autowired
	private AllBetRecordMapper allBetRecordMapper;
	
	private static Logger log = Logger.getLogger(AllBetRecordService.class);
	
	@Override
	@Transactional(propagation=Propagation.REQUIRES_NEW)
	public void insertBetRecord(BetRecord betExceptionRecord) {
		try {
			allBetRecordMapper.insertSelective(betExceptionRecord);
		} catch (Exception e) {
			log.error("中奖加币失败统计异常",e);
		}
	}

	@Override
	public BetRecord getBetRecord(Long periods,Long uid) {
		return allBetRecordMapper.getBetRecord(periods, uid);
	}
}
