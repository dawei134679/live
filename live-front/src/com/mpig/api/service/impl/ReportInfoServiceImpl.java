package com.mpig.api.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mpig.api.dao.IReportInfoDao;
import com.mpig.api.model.ReturnModel;
import com.mpig.api.service.IReportInfoService;
import com.mpig.api.utils.DateUtils;

@Service
public class ReportInfoServiceImpl implements IReportInfoService {

	@Autowired
	private IReportInfoDao reportInfoDao;

	@Override
	public ReturnModel report(Long uid, String content, Long rid, ReturnModel returnModel) {
		int status = 0;
		Long createtime = System.currentTimeMillis() / 1000;
		int res = reportInfoDao.insert(uid, content, rid, status, createtime);
		if (res == 1) {
			returnModel.setCode(200);
			returnModel.setData("举报成功");
			
			return returnModel;
		}
		returnModel.setCode(201);
		returnModel.setData("举报失败");
		return returnModel;
	}

	@Override
	public int isReport(Long uid, Long rid) {
		Long dayBegin = DateUtils.getDayBegin();
		Long dayEnd = DateUtils.getDayEnd();
		return reportInfoDao.isReport(uid, rid, dayBegin, dayEnd);
	}

}
