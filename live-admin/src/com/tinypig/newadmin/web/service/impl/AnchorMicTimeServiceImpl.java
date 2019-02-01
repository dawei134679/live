package com.tinypig.newadmin.web.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tinypig.newadmin.web.dao.AnchorMicTimeDao;
import com.tinypig.newadmin.web.entity.AnchorMicTimeParamDto;
import com.tinypig.newadmin.web.entity.LiveMicTime;
import com.tinypig.newadmin.web.service.AnchorMicTimeService;

@Service
public class AnchorMicTimeServiceImpl implements AnchorMicTimeService{
	
	@Autowired
	private AnchorMicTimeDao anchorMicTimeDao;
	

	@Override
	public Map<String, Object> getAnchorMicTimeList(AnchorMicTimeParamDto param) {
		Map<String, Object> resultmap = new HashMap<String, Object>();
		resultmap.put("rows", anchorMicTimeDao.getAnchorMicTimeList(param));
		resultmap.put("total", anchorMicTimeDao.getAnchorMicTimeTotal(param));
		return resultmap;

	}


	@Override
	public List<Map<String, Object>> getAllAnchorMicTimeList(AnchorMicTimeParamDto param) {
		return anchorMicTimeDao.getAllAnchorMicTimeList(param);
	}


	@Override
	public List<LiveMicTime> getAnchorLiveSta(AnchorMicTimeParamDto param) {
		return anchorMicTimeDao.getAnchorLiveSta(param);
	}


	@Override
	public Integer getAllAnchorTime(AnchorMicTimeParamDto param) {
		return anchorMicTimeDao.getAllAnchorTime(param);
	}
	

}
