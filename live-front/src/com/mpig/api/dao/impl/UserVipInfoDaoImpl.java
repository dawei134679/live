package com.mpig.api.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.mpig.api.SqlTemplete;
import com.mpig.api.dao.IUserVipInfoDao;
import com.mpig.api.db.DBHelper;
import com.mpig.api.db.DataSource;
import com.mpig.api.model.UserVipInfoModel;
import com.mpig.api.utils.VarConfigUtils;
@Repository
public class UserVipInfoDaoImpl implements IUserVipInfoDao{
	private static final Logger logger = Logger.getLogger(UserVipInfoDaoImpl.class);
	@Override
	public UserVipInfoModel getUserVipInfo(int uid) {
		Connection conn = null;
		PreparedStatement statement = null;
		ResultSet rs = null;
		UserVipInfoModel userVipInfoModel = null;
		try {
			if (userVipInfoModel == null) {
				// 获取用户基本信息
				conn = DataSource.instance.getPool(VarConfigUtils.dbZhuUser).getConnection();
				statement = conn.prepareStatement(SqlTemplete.SQL_selUserVipInfo);
				DBHelper.setPreparedStatementParam(statement, uid);
				rs = statement.executeQuery();

				if (rs != null) {
					while (rs.next()) {
						userVipInfoModel = new UserVipInfoModel().populateFromResultSet(rs);
						break;
					}
				}
			}
		} catch (SQLException e) {
			logger.error("<getUserVipInfo->SQLException>" + e.getMessage());
		} catch (Exception e) {
			logger.error("<getUserVipInfo->Exception>" + e.getMessage());
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
				logger.error("<getUserVipInfo->finally->Exception>" + e2.getMessage());
			}
		}
		return userVipInfoModel;
	}
	
	@Override
	public UserVipInfoModel getUserVipInfoByUid(int uid, int gid) {
		Connection conn = null;
		PreparedStatement statement = null;
		ResultSet rs = null;
		UserVipInfoModel userVipInfoModel = null;
		try {
			if (userVipInfoModel == null) {
				// 获取用户基本信息
				conn = DataSource.instance.getPool(VarConfigUtils.dbZhuUser).getConnection();
				statement = conn.prepareStatement(SqlTemplete.SQL_selUserVipInfoByUid);
				DBHelper.setPreparedStatementParam(statement, uid, gid);
				rs = statement.executeQuery();

				if (rs != null) {
					while (rs.next()) {
						userVipInfoModel = new UserVipInfoModel().populateFromResultSet(rs);
						break;
					}
				}
			}
		} catch (SQLException e) {
			logger.error("<getUserVipInfoByUid->SQLException>" + e.getMessage());
		} catch (Exception e) {
			logger.error("<getUserVipInfoByUid->Exception>" + e.getMessage());
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
				logger.error("<getUserVipInfoByUid->finally->Exception>" + e2.getMessage());
			}
		}
		return userVipInfoModel;
	}
	@Override
	public int addUserVipInfo(int uid, int gid, int starttime,int endtime, int isdel) {
		try {
			int executeResult = DBHelper.execute(VarConfigUtils.dbZhuUser, SqlTemplete.SQL_insUserVipInfo, false, uid,gid,starttime,endtime,isdel);
			return executeResult;
		} catch (Exception e) {
			logger.error("<addUserVipInfo->Exception>" + e.toString());
		}
		return -1;
	}
	
	@Override
	public int updUserVipInfo(int uid, int starttime, int endtime, int isdel, int gid) {
		try {
			int executeResult = DBHelper.execute(VarConfigUtils.dbZhuUser, SqlTemplete.SQL_updUserVipInfo, false, starttime, endtime, isdel, uid,gid);
			return executeResult;
		} catch (Exception e) {
			logger.error("<updUserVipInfo->Exception>" + e.toString());
		}
		return -1;
	}
}
