package com.hkzb.game.dao;

import org.apache.ibatis.annotations.Param;

import com.hkzb.game.model.BetRecord;

public interface AllBetRecordMapper {
	
	int insertSelective(BetRecord betRecord);
	
	BetRecord getBetRecord(@Param("periods") Long periods,@Param("uid") Long uid);
	
}
