package com.tinypig.newadmin.web.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tinypig.newadmin.web.dao.BetListDao;
import com.tinypig.newadmin.web.entity.BetRecord;
import com.tinypig.newadmin.web.service.BetListService;

@Service
public class BetListServiceImpl implements BetListService {
	
	@Autowired
	private BetListDao betListDao;
	
	@Override
	public Map<String, Object> getBetList(Long periods, Long anchorId,Long uid, Integer rows, Integer page) {
		Integer startIndex = (page - 1) * rows;
		Map<String, Object> map = new HashMap<String,Object>();
		List<BetRecord> list = betListDao.getBetList(periods, anchorId,uid, startIndex, rows);
		Long total = betListDao.getBetTotalCount(periods, anchorId, uid);
		map.put("total", total);
		map.put("rows", list);
		return map;
	}

	@Override
	public BetRecord getBetTotal(Long periods, Long anchorId, Long uid) {
		return betListDao.getBetTotal(periods, anchorId, uid);
	}
}
