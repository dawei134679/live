package com.mpig.api.dao.impl;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.mpig.api.SqlTemplete;
import com.mpig.api.dao.IStatisticalInfoDao;
import com.mpig.api.db.DBHelper;
import com.mpig.api.utils.VarConfigUtils;

@Repository
public class StatisticalInfoDaoImpl implements IStatisticalInfoDao {
	private static final Logger logger = Logger.getLogger(StatisticalInfoDaoImpl.class);

	@Override
	public int report(int uid,int anchorId,String os,String appVersion, String msg) {
		try {
			int executeResult = DBHelper.execute(VarConfigUtils.dbZhuAnalysis, SqlTemplete.SQL_insStatisticalInfo, false,uid,anchorId,os,appVersion,msg);
			return executeResult;
		} catch (Exception e) {
			logger.error("<StatisticalInfoDaoImpl - report->Exception>" + e.toString());
		}
		return -1;
	}

}
