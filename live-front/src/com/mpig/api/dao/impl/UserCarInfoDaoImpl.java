package com.mpig.api.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.mpig.api.SqlTemplete;
import com.mpig.api.dao.IUserCarInfoDao;
import com.mpig.api.db.DBHelper;
import com.mpig.api.db.DataSource;
import com.mpig.api.model.UserCarInfoModel;
import com.mpig.api.utils.VarConfigUtils;

@Repository
public class UserCarInfoDaoImpl implements IUserCarInfoDao{
	
	private static final Logger logger = Logger.getLogger(UserCarInfoDaoImpl.class);
	
	@Override
	public List<UserCarInfoModel> getUserValidCars(Integer uid) {
		Connection conn = null;
		PreparedStatement statement = null;
		ResultSet rs = null;
		UserCarInfoModel userCarInfoModel = null;
		List<UserCarInfoModel> userCarInfoModels = new ArrayList<UserCarInfoModel>();
		try {
			conn = DataSource.instance.getPool(VarConfigUtils.dbZhuUser).getConnection();
			statement = conn.prepareStatement(SqlTemplete.SQL_selUserValidCarInfo);
			DBHelper.setPreparedStatementParam(statement, uid);
			rs = statement.executeQuery();
			if (rs != null) {
				while (rs.next()) {
					userCarInfoModel = new UserCarInfoModel().populateFromResultSet(rs);
					userCarInfoModels.add(userCarInfoModel);
				}
			}
		} catch (SQLException e) {
			logger.error("<getUserValidCars->SQLException>" + e.getMessage());
		} catch (Exception e) {
			logger.error("<getUserValidCars->Exception>" + e.getMessage());
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
				logger.error("<getUserValidCars->finally->Exception>" + e2.getMessage());
			}
		}
		return userCarInfoModels;
	}

	@Override
	public UserCarInfoModel getUserCarInfoByGid(Integer uid, Integer gid) {
		Connection conn = null;
		PreparedStatement statement = null;
		ResultSet rs = null;
		UserCarInfoModel userCarInfoModel = null;
		try {
			conn = DataSource.instance.getPool(VarConfigUtils.dbZhuUser).getConnection();
			statement = conn.prepareStatement(SqlTemplete.SQL_selUserCarInfoByGid);
			DBHelper.setPreparedStatementParam(statement, uid, gid);
			rs = statement.executeQuery();
			if (rs != null) {
				while (rs.next()) {
					userCarInfoModel = new UserCarInfoModel().populateFromResultSet(rs);
					break;
				}
			}
		} catch (SQLException e) {
			logger.error("<getUserCarInfoByGid->SQLException>" + e.getMessage());
		} catch (Exception e) {
			logger.error("<getUserCarInfoByGid->Exception>" + e.getMessage());
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
				logger.error("<getUserCarInfoByGid->finally->Exception>" + e2.getMessage());
			}
		}
		return userCarInfoModel;
	}

	@Override
	public int addUserCarInfo(Integer uid, Integer gid, Integer starttime,Integer endtime, Integer status) {
		try {
			int executeResult = DBHelper.execute(VarConfigUtils.dbZhuUser, SqlTemplete.SQL_insUserCarInfo, false, uid, gid, starttime, endtime, status);
			return executeResult;
		} catch (Exception e) {
			logger.error("<addUserCarInfo->Exception>" + e.toString());
		}
		return -1;
	}

	@Override
	public int updUserCarInfo(Integer uid, Integer gid, Integer starttime,Integer endtime, Integer status) {
		try {
			int executeResult = DBHelper.execute(VarConfigUtils.dbZhuUser, SqlTemplete.SQL_updUserCarInfo, false, starttime, endtime, status, uid, gid);
			return executeResult;
		} catch (Exception e) {
			logger.error("<updUserCarInfo->Exception>" + e.toString());
		}
		return -1;
	}

	@Override
	public int updUserCarInfoStatus(Integer uid, Integer gid, Integer status) {
		try {
			int executeResult = DBHelper.execute(VarConfigUtils.dbZhuUser, SqlTemplete.SQL_updUserCarInfoStatus, false, status, uid, gid, status);
			return executeResult;
		} catch (Exception e) {
			logger.error("<updUserCarInfoStatus->Exception>" + e.toString());
		}
		return -1;
	}

	@Override
	public int updUserCarInfoUnStatus(Integer uid) {
		try {
			int executeResult = DBHelper.execute(VarConfigUtils.dbZhuUser, SqlTemplete.SQL_updUserCarInfoUnStatus, false, uid);
			return executeResult;
		} catch (Exception e) {
			logger.error("<updUserCarInfoUnStatus->Exception>" + e.toString());
		}
		return -1;
	}
}
