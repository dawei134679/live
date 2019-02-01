package com.tinypig.newadmin.web.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.tinypig.newadmin.web.entity.ReportInfo;
import com.tinypig.newadmin.web.entity.ReportInfoParamDto;

public interface ReportInfoDao {
	int deleteByPrimaryKey(Long id);

	int insert(ReportInfo record);

	int insertSelective(ReportInfo record);

	ReportInfo selectByPrimaryKey(Long id);

	int updateByPrimaryKeySelective(ReportInfo record);

	int updateByPrimaryKey(ReportInfo record);

	List<ReportInfo> getReportInfoList(ReportInfoParamDto params);

	Integer getReportInfoTotal(ReportInfoParamDto params);

	int updateHandlemark(@Param("id") Long id, @Param("handlemark") String handlemark,
			@Param("handletime") Long handletime, @Param("handleUid") Long handleUid);
}