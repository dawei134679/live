package com.tinypig.newadmin.web.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tinypig.newadmin.web.dao.ReportInfoDao;
import com.tinypig.newadmin.web.entity.ReportInfoParamDto;
import com.tinypig.newadmin.web.service.IReportInfoService;

@Service
@Transactional
public class ReportInfoService implements IReportInfoService {

	@Autowired
	private ReportInfoDao reportInfoDao;

	@Override
	public Map<String, Object> getReportInfoPage(ReportInfoParamDto params) {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("rows", reportInfoDao.getReportInfoList(params));
		result.put("total", reportInfoDao.getReportInfoTotal(params));
		return result;
	}

	@Override
	public Map<String, Object> handler(Long id, String handlemark,Long adminUid) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		do {
			if (StringUtils.isBlank(handlemark)) {
				resultMap.put("success", 201);
				resultMap.put("msg", "处理意见不能为空！");
				break;
			}
			if (id <= 0) {
				resultMap.put("success", 201);
				resultMap.put("msg", "参数不正确");
				break;
			}
			Long handletime = System.currentTimeMillis()/1000;
			int res = reportInfoDao.updateHandlemark(id, handlemark, handletime, adminUid);
			if (res == 1) {
				resultMap.put("success", 200);
				resultMap.put("msg", "处理成功");
			} else {
				resultMap.put("success", 201);
				resultMap.put("msg", "处理失败");
			}
		} while (false);
		return resultMap;
	}

}
