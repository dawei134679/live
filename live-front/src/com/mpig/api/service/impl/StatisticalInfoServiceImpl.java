package com.mpig.api.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mpig.api.dao.IStatisticalInfoDao;
import com.mpig.api.service.IStatisticalInfoService;

@Service
public class StatisticalInfoServiceImpl implements IStatisticalInfoService {

	@Autowired
	private IStatisticalInfoDao statisticalInfoDao;
	
	@Override
	public int report(int uid,int anchorId,String os,String appVersion, String msg) {
		return statisticalInfoDao.report(uid,anchorId,os,appVersion,msg);
	}

}
