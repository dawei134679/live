package com.tinypig.newadmin.web.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tinypig.newadmin.web.dao.BillcvgInfoDao;
import com.tinypig.newadmin.web.entity.BillcvgInfo;
import com.tinypig.newadmin.web.service.BillcvgInfoService;
import com.tinypig.newadmin.web.vo.BillcvgParamVo;

@Service
public class BillcvgInfoServiceImpl implements BillcvgInfoService {
	
	@Autowired
	private BillcvgInfoDao billcvgInfoDao;

	@Override
	public Map<String, Object> getcvgStaList(BillcvgParamVo billcvgParamVo) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		List<BillcvgInfo> list = billcvgInfoDao.getcvgStaList(billcvgParamVo);
		Map<String, Object> map = billcvgInfoDao.getgetcvgStaCount(billcvgParamVo);
		resultMap.put("rows", list);
		resultMap.putAll(map);
		return resultMap;
	}

	@Override
	public List<Map<String, Object>> getAllCvgStaList(BillcvgParamVo param) {
		return billcvgInfoDao.getAllCvgStaList(param);
	}

}
