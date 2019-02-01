package com.tinypig.newadmin.web.service;

import java.util.Map;

import com.tinypig.newadmin.web.entity.BetRecord;

public interface BetListService {
	Map<String, Object> getBetList(Long periods, Long anchorId,Long uid, Integer rows, Integer page);
	
	BetRecord getBetTotal(Long periods, Long anchorId,Long uid);
}
