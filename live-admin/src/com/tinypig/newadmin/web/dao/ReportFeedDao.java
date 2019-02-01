package com.tinypig.newadmin.web.dao;

import java.util.List;

import com.tinypig.newadmin.web.entity.ReportFeed;
import com.tinypig.newadmin.web.entity.ReportFeedParamDto;

public interface ReportFeedDao {
	int deleteByPrimaryKey(Integer id);

	int insert(ReportFeed record);

	int insertSelective(ReportFeed record);

	ReportFeed selectByPrimaryKey(Integer id);

	int updateByPrimaryKeySelective(ReportFeed record);

	int updateByPrimaryKey(ReportFeed record);

	List<ReportFeed> pageList(ReportFeedParamDto reportFeedDto);
}