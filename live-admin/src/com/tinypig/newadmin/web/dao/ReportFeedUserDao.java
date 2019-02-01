package com.tinypig.newadmin.web.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.tinypig.newadmin.web.entity.ReportFeedUser;

public interface ReportFeedUserDao {
	int deleteByPrimaryKey(Integer id);

	int insert(ReportFeedUser record);

	int insertSelective(ReportFeedUser record);

	ReportFeedUser selectByPrimaryKey(Integer id);

	List<ReportFeedUser> selectByReportFeedId(Integer rid);

	int updateByPrimaryKeySelective(ReportFeedUser record);

	int updateStatusByRid(@Param("rid") Integer rid, @Param("status") Integer status);

	int updateByPrimaryKey(ReportFeedUser record);
}