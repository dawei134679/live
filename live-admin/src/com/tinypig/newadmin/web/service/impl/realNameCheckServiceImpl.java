package com.tinypig.newadmin.web.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tinypig.newadmin.web.dao.RealNameCheckDao;
import com.tinypig.newadmin.web.entity.RealNameParamDto;
import com.tinypig.newadmin.web.service.realNameCheckService;

@Service
public class realNameCheckServiceImpl implements realNameCheckService{

	@Autowired
	private RealNameCheckDao realNameCheckDao;
	
	@Override
	public Map<String, Object> getRealNameListPage(RealNameParamDto param) {
		Map<String, Object> resultmap = new HashMap<String, Object>();
		resultmap.put("rows", realNameCheckDao.getRealNameList(param));
		resultmap.put("total", realNameCheckDao.getRealNameTotal(param));
		return resultmap;
	}

	@Override
	public List<Map<String, Object>> getAllRealNameList(RealNameParamDto param) {
		return realNameCheckDao.getAllRealNameList(param);
	}
	
}
