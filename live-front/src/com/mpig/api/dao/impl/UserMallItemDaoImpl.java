package com.mpig.api.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.mpig.api.SqlTemplete;
import com.mpig.api.dao.IUserMallItemDao;
import com.mpig.api.db.DBHelper;
import com.mpig.api.db.DataSource;
import com.mpig.api.model.UserMallItemModel;
import com.mpig.api.utils.VarConfigUtils;
@Repository
public class UserMallItemDaoImpl implements IUserMallItemDao{
	private static final Logger logger = Logger.getLogger(UserMallItemDaoImpl.class);
	@Override
	public int addUserMallItem(int uid, int gid, int type, int subtype,int num, int starttime, int endtime) {
		try {
			int executeResult = DBHelper.execute(VarConfigUtils.dbZhuItem, SqlTemplete.SQL_insUserMallItem, false,uid, gid, type, subtype, num, starttime, endtime);
			return executeResult;
		} catch (Exception e) {
			logger.error("<addUserVipInfo->Exception>" + e.toString());
		}
		return -1;
	}

	@Override
	public int updUserMallItem(int uid, int subtype, int starttime, int endtime) {
		try {
			int executeResult = DBHelper.execute(VarConfigUtils.dbZhuItem, SqlTemplete.SQL_updUserMallItem, false,starttime, endtime, uid, subtype);
			return executeResult;
		} catch (Exception e) {
			logger.error("<addUserVipInfo->Exception>" + e.toString());
		}
		return -1;
	}

	@Override
	public UserMallItemModel getItemBySubtype(int uid, int subtype) {
		Connection conn = null;
		PreparedStatement statement = null;
		ResultSet rs = null;
		UserMallItemModel userMallItemModel = null;
		try {
			if (userMallItemModel == null) {
				// 获取用户基本信息
				conn = DataSource.instance.getPool(VarConfigUtils.dbZhuItem).getConnection();
				statement = conn.prepareStatement(SqlTemplete.SQL_getUserMallItem);
				DBHelper.setPreparedStatementParam(statement, uid, subtype);
				rs = statement.executeQuery();

				if (rs != null) {
					while (rs.next()) {
						userMallItemModel = new UserMallItemModel().populateFromResultSet(rs);
						break;
					}
				}
			}
		} catch (SQLException e) {
			logger.error("<getItemBySubtype->SQLException>" + e.getMessage());
		} catch (Exception e) {
			logger.error("<getItemBySubtype->Exception>" + e.getMessage());
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
				logger.error("<getItemBySubtype->finally->Exception>" + e2.getMessage());
			}
		}
		return userMallItemModel;
	}

	@Override
	public UserMallItemModel getAllItemBySubtype(int uid, int subtype) {
		Connection conn = null;
		PreparedStatement statement = null;
		ResultSet rs = null;
		UserMallItemModel userMallItemModel = null;
		try {
			if (userMallItemModel == null) {
				// 获取用户基本信息
				conn = DataSource.instance.getPool(VarConfigUtils.dbZhuItem).getConnection();
				statement = conn.prepareStatement(SqlTemplete.SQL_getUserAllMallItemBySubtype);
				DBHelper.setPreparedStatementParam(statement, uid, subtype);
				rs = statement.executeQuery();

				if (rs != null) {
					while (rs.next()) {
						userMallItemModel = new UserMallItemModel().populateFromResultSet(rs);
						break;
					}
				}
			}
		} catch (SQLException e) {
			logger.error("<getAllItemBySubtype->SQLException>" + e.getMessage());
		} catch (Exception e) {
			logger.error("<getAllItemBySubtype->Exception>" + e.getMessage());
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
				logger.error("<getAllItemBySubtype->finally->Exception>" + e2.getMessage());
			}
		}
		return userMallItemModel;
	}
	
	
}
