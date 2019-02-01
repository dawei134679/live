package com.mpig.api.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.mpig.api.SqlTemplete;
import com.mpig.api.dao.IReportInfoDao;
import com.mpig.api.db.DBHelper;
import com.mpig.api.db.DataSource;
import com.mpig.api.utils.VarConfigUtils;

@Repository
public class ReportInfoDaoImpl implements IReportInfoDao {
	private static final Logger logger = Logger.getLogger(ReportInfoDaoImpl.class);

	@Override
	public int insert(Long uid, String content, Long rid, int status, Long createtime) {
		try {
			int executeResult = DBHelper.execute(VarConfigUtils.dbZhuAdmin, SqlTemplete.SQL_insReportInfo, false, uid,
					content, rid, status, createtime);
			return executeResult;
		} catch (Exception e) {
			logger.error("<addInviteUserInfo->Exception>" + e.toString());
		}
		return -1;
	}

	@Override
	public int isReport(Long uid, Long rid, Long dayBegin, Long dayEnd) {
		Connection conn = null;
		ResultSet rs = null;
		PreparedStatement statement = null;
		int nums = 0;
		try {
			conn = DataSource.instance.getPool(VarConfigUtils.dbZhuAdmin).getConnection();
			String sql_Select = SqlTemplete.SQL_isReport;
			statement = conn.prepareStatement(sql_Select);
			DBHelper.setPreparedStatementParam(statement,uid,rid,dayBegin,dayEnd);
			rs = statement.executeQuery();
			if (rs != null && rs.next()) {
				nums = rs.getInt("nums");
			}
		} catch (Exception e) {
			logger.error("<isReport->Exception>" + e.toString());
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (statement != null) {
					statement.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (Exception e2) {
				logger.error(e2.getMessage(),e2);
			}
		}
		return nums;
	}
}
