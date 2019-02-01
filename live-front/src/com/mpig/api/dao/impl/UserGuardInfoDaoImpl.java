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
import com.mpig.api.dao.IUserGuardInfoDao;
import com.mpig.api.db.DBHelper;
import com.mpig.api.db.DataSource;
import com.mpig.api.model.UserGuardInfoModel;
import com.mpig.api.utils.VarConfigUtils;

@Repository
public class UserGuardInfoDaoImpl implements IUserGuardInfoDao {
	private static final Logger logger = Logger.getLogger(UserGuardInfoDaoImpl.class);
	
	@Override
	public int addExpByUid(int uid, int roomid, int exp, int gid) {
		if (exp <= 0) {
			return -1;
		}
		try {
			int executeResult = DBHelper.execute(VarConfigUtils.dbZhuUser, SqlTemplete.SQL_AddGuardExpByUid, false, exp, uid, roomid, gid);
			return executeResult;
		} catch (Exception e) {
			logger.error("<addExpByUid-> Param => exp :"+ exp+" uid :"+uid+" roomid :"+ roomid+" Exception> " + e.toString());
		}
		return -1;
	}
	
	@Override
	public UserGuardInfoModel getUserGuardInfo(int uid, int roomid) {
		Connection conn = null;
		PreparedStatement statement = null;
		ResultSet rs = null;
		UserGuardInfoModel userGuardInfoModel = null;
		try {
			if (userGuardInfoModel == null) {
				// 获取用户基本信息
				conn = DataSource.instance.getPool(VarConfigUtils.dbZhuUser).getConnection();
				statement = conn.prepareStatement(SqlTemplete.SQL_selUserGardInfo);
				DBHelper.setPreparedStatementParam(statement, roomid, uid);
				rs = statement.executeQuery();

				if (rs != null) {
					while (rs.next()) {
						userGuardInfoModel = new UserGuardInfoModel().populateFromResultSet(rs);
						break;
					}
				}
			}
		} catch (SQLException e) {
			logger.error("<getUserGuardInfo->SQLException>" + e.getMessage());
		} catch (Exception e) {
			logger.error("<getUserGuardInfo->Exception>" + e.getMessage());
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
				logger.error("<getUserGuardInfo->finally->Exception>" + e2.getMessage());
			}
		}
		return userGuardInfoModel;
	}
	
	@Override
	public UserGuardInfoModel getUserGuardInfoByUid(int uid, int roomid, int gid) {
		Connection conn = null;
		PreparedStatement statement = null;
		ResultSet rs = null;
		UserGuardInfoModel userGuardInfoModel = null;
		try {
			if (userGuardInfoModel == null) {
				// 获取用户基本信息
				conn = DataSource.instance.getPool(VarConfigUtils.dbZhuUser).getConnection();
				statement = conn.prepareStatement(SqlTemplete.SQL_selUserGardInfoByUid);
				DBHelper.setPreparedStatementParam(statement, roomid, uid, gid);
				rs = statement.executeQuery();

				if (rs != null) {
					while (rs.next()) {
						userGuardInfoModel = new UserGuardInfoModel().populateFromResultSet(rs);
						break;
					}
				}
			}
		} catch (SQLException e) {
			logger.error("<getUserGuardInfoByUid->SQLException>" + e.getMessage());
		} catch (Exception e) {
			logger.error("<getUserGuardInfoByUid->Exception>" + e.getMessage());
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
				logger.error("<getUserGuardInfoByUid->finally->Exception>" + e2.getMessage());
			}
		}
		return userGuardInfoModel;
	}
	
	@Override
	public int updUserGuardLevel(int uid, int roomid, int level, int gid){
		int res = 0;
		try {
			res = DBHelper.execute(VarConfigUtils.dbZhuUser, SqlTemplete.SQL_UpdGuardLevel, false, level, uid, roomid,gid);
			if (res > 0) {
				this.getUserGuardInfo(uid, roomid);
			}
		} catch (Exception e) {
			logger.error("<updUserGuardLevel->Exception>" + e.toString());
		}
		return res;
	}
	
	@Override
	public int addUserGuardInfo(int roomid, int uid, int gid, int level, int exp, int starttime, int endtime, int cushiontime, int isdel) {
		try {
			int executeResult = DBHelper.execute(VarConfigUtils.dbZhuUser, SqlTemplete.SQL_insUserGuardInfo, false, roomid, uid, gid,level,exp,starttime,endtime,cushiontime,isdel);
			return executeResult;
		} catch (Exception e) {
			logger.error("<addUserGuardInfo->Exception>" + e.toString());
		}
		return -1;
	}
	
	@Override
	public int updUserGuardInfo(int exp, int starttime, int endtime, int cushiontime, int isdel, int uid, int roomid, int gid) {
		try {
			int executeResult = DBHelper.execute(VarConfigUtils.dbZhuUser, SqlTemplete.SQL_updUserGuardInfo, false, exp, starttime, endtime, cushiontime,isdel,uid,roomid,gid);
			return executeResult;
		} catch (Exception e) {
			logger.error("<updUserGuardInfo->Exception>" + e.toString());
		}
		return -1;
	}

	@Override
	public List<UserGuardInfoModel> selUserAllGardInfo(int uid) {

		Connection conn = null;
		PreparedStatement statement = null;
		ResultSet rs = null;
		UserGuardInfoModel userGuardInfoModel = null;
		List<UserGuardInfoModel> userGuardInfoList = new ArrayList<UserGuardInfoModel>();
		try {
			if (userGuardInfoModel == null) {
				// 获取用户基本信息
				conn = DataSource.instance.getPool(VarConfigUtils.dbZhuUser).getConnection();
				statement = conn.prepareStatement(SqlTemplete.SQL_selUserAllGardInfo);
				DBHelper.setPreparedStatementParam(statement, uid);
				rs = statement.executeQuery();

				if (rs != null) {
					while (rs.next()) {
						userGuardInfoModel = new UserGuardInfoModel().populateFromResultSet(rs);
						userGuardInfoList.add(userGuardInfoModel);
					}
				}
			}
		} catch (SQLException e) {
			logger.error("<selUserAllGardInfo->SQLException>" + e.getMessage());
		} catch (Exception e) {
			logger.error("<selUserAllGardInfo->Exception>" + e.getMessage());
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
				logger.error("<selUserAllGardInfo->finally->Exception>" + e2.getMessage());
			}
		}
		return userGuardInfoList;
	}
	
	@Override
	public List<UserGuardInfoModel> selUserAllGardInfoByRoomId(int roomid) {
		Connection conn = null;
		PreparedStatement statement = null;
		ResultSet rs = null;
		UserGuardInfoModel userGuardInfoModel = null;
		List<UserGuardInfoModel> userGuardInfoList = new ArrayList<UserGuardInfoModel>();
		try {
			if (userGuardInfoModel == null) {
				// 获取用户基本信息
				conn = DataSource.instance.getPool(VarConfigUtils.dbZhuUser).getConnection();
				statement = conn.prepareStatement(SqlTemplete.SQL_selUserGardInfoByRoomId);
				DBHelper.setPreparedStatementParam(statement, roomid);
				rs = statement.executeQuery();
				if (rs != null) {
					while (rs.next()) {
						userGuardInfoModel = new UserGuardInfoModel().populateFromResultSet(rs);
						userGuardInfoList.add(userGuardInfoModel);
					}
				}
			}
		} catch (SQLException e) {
			logger.error("<selUserAllGardInfoByRoomId->SQLException>" + e.getMessage());
		} catch (Exception e) {
			logger.error("<selUserAllGardInfoByRoomId->Exception>" + e.getMessage());
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
				logger.error("<selUserAllGardInfoByRoomId->finally->Exception>" + e2.getMessage());
			}
		}
		return userGuardInfoList;
	}
}
