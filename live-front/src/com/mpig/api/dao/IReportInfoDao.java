package com.mpig.api.dao;

public interface IReportInfoDao {

	int insert(Long uid, String content, Long rid, int status, Long createtime);
	
	int isReport(Long uid, Long rid, Long dayBegin, Long dayEnd);
}
