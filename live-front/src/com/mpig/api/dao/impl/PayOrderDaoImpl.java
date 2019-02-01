package com.mpig.api.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.mpig.api.SqlTemplete;
import com.mpig.api.dao.IPayOrderDao;
import com.mpig.api.db.DBHelper;
import com.mpig.api.db.DataSource;
import com.mpig.api.utils.VarConfigUtils;

@Repository
public class PayOrderDaoImpl implements IPayOrderDao,SqlTemplete{
	private static final Logger logger = Logger.getLogger(PayOrderDaoImpl.class);
	      
	@Override
	public int getAnchorSalaryCountForMonth(Integer uid, Integer ymtime) {
		Connection conn = null;
		PreparedStatement statement = null;
		ResultSet rs = null;
		int rec = 0;
		try {
				conn = DataSource.instance.getPool(VarConfigUtils.dbZhuPay).getConnection();
				statement = conn.prepareStatement(SqlTemplete.SQL_getAnchorSalaryByTime);
				DBHelper.setPreparedStatementParam(statement, uid, ymtime);
				rs = statement.executeQuery();

				if (rs != null) {
					while (rs.next()) {
						rec = rs.getInt("count");
					}
				}
		} catch (SQLException e) {
			logger.error("<getAnchorSalaryCountForMonth->SQLException>" + e.getMessage());
		} catch (Exception e) {
			logger.error("<getAnchorSalaryCountForMonth->Exception>" + e.getMessage());
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
				logger.error("<getAnchorSalaryCountForMonth->finally->Exception>" + e2.getMessage());
			}
		}
		return rec;
	}

	@Override
	public List<Object> getAnchorSalaryByUid(Integer uid) {
		Connection conn = null;
		PreparedStatement statement = null;
		ResultSet rs = null;
		List<Object> dataList = new ArrayList<Object>();
		try {
				conn = DataSource.instance.getPool(VarConfigUtils.dbZhuPay).getConnection();
				statement = conn.prepareStatement(SqlTemplete.SQL_getAnchorSalaryByUid);
				DBHelper.setPreparedStatementParam(statement, uid);
				rs = statement.executeQuery();
				if (rs != null) {
					while (rs.next()) {
						Map<String,Object> map = new HashMap<String,Object>();
						int id = rs.getInt("id");
						int ymtime = rs.getInt("addtime");
						int credits = rs.getInt("credits");
						int status  = rs.getInt("status");
						Long newDate = ymtime*1000l;
						String date = new SimpleDateFormat("yyyy年MM月dd日").format(new Date(newDate));
						map.put("id", id);
						map.put("ymtime", date);
						map.put("credits", credits);
						map.put("status", status);
						dataList.add(map);
					}
				}
		} catch (SQLException e) {
			logger.error("<getAnchorSalaryByUid->SQLException>" + e.getMessage());
		} catch (Exception e) {
			logger.error("<getAnchorSalaryByUid->Exception>" + e.getMessage());
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
				logger.error("<getAnchorSalaryByUid->finally->Exception>" + e2.getMessage());
			}
		}
		return dataList;
	}
	@Override
	public Map<String, Object> getUnionAnchorByAnchorid(Integer anchorId) {
		Connection conn = null;
		PreparedStatement statement = null;
		ResultSet rs = null;
		Map<String, Object> map = new HashMap<String, Object>();
		try {
				conn = DataSource.instance.getPool(VarConfigUtils.dbZhuUnion).getConnection();
				statement = conn.prepareStatement(SqlTemplete.SQL_getUnionAnchorByAnchorid);
				DBHelper.setPreparedStatementParam(statement, anchorId);
				rs = statement.executeQuery();
				if (rs != null) {
					while (rs.next()) {
						map.put("unionid", rs.getInt("unionid"));
						map.put("salary", rs.getInt("salary"));
						map.put("rate", rs.getDouble("rate"));
						map.put("remarks", rs.getString("remarks"));
						break;
					}
				}
		} catch (SQLException e) {
			logger.error("<getUnionAnchorByAnchorid->SQLException>" + e.getMessage());
		} catch (Exception e) {
			logger.error("<getUnionAnchorByAnchorid->Exception>" + e.getMessage());
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
				logger.error("<getUnionAnchorByAnchorid->finally->Exception>" + e2.getMessage());
			}
		}
		return map;
	}

	@Override
	public int insPayAnchorSalary(Integer ymtime, Integer uid, Integer unionid,
			Integer credits) {
		int res = 0;
		try {
			res = DBHelper.execute(VarConfigUtils.dbZhuPay, SQL_insPayAnchorSalary, false, uid, unionid, credits,System.currentTimeMillis()/1000);
		} catch (Exception e) {
			logger.error("<insPayAnchorSalary->Exception>" + e.getMessage());
		}
		return res;
	}

}
