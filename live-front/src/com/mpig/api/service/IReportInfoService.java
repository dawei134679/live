package com.mpig.api.service;

import com.mpig.api.model.ReturnModel;

public interface IReportInfoService {

	ReturnModel report(Long uid, String content, Long rid,ReturnModel returnModel);
	
	int isReport(Long uid, Long rid);
}
