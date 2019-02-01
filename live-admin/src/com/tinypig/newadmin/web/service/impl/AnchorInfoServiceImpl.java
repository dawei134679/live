package com.tinypig.newadmin.web.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tinypig.newadmin.web.dao.AnchorInfoDao;
import com.tinypig.newadmin.web.entity.AnchorInfoParamDto;
import com.tinypig.newadmin.web.service.AnchorInfoService;
@Service
public class AnchorInfoServiceImpl implements AnchorInfoService{

	@Autowired
	private AnchorInfoDao anchorInfoDao;
	
	@Override
	public Map<String, Object>  getAnchorList(AnchorInfoParamDto param) {
		Map<String, Object> resultmap = new HashMap<String, Object>();
		resultmap.put("rows", anchorInfoDao.getAnchorList(param));
		resultmap.put("total", anchorInfoDao.getAnchorTotal(param));
		return resultmap;
	}

	@Override
	public List<Map<String,Object>>  getAnchorAllList(AnchorInfoParamDto param) {
		return anchorInfoDao.getAnchorAllList(param);
	}
}
