package com.tinypig.newadmin.web.service;

import java.util.Map;

import com.tinypig.newadmin.web.entity.ReportInfoParamDto;


public interface IReportInfoService {
    public Map<String,Object> getReportInfoPage(ReportInfoParamDto param);

	public Map<String, Object> handler(Long id, String handlemark,Long adminUid);
}
