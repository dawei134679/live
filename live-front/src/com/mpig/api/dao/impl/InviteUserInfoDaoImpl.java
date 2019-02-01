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
import com.mpig.api.dao.IInviteUserInfoDao;
import com.mpig.api.db.DBHelper;
import com.mpig.api.db.DataSource;
import com.mpig.api.model.InviteUserInfoModel;
import com.mpig.api.model.InviteUserPeckLogModel;
import com.mpig.api.model.InviteUserRewardInfoModel;
import com.mpig.api.utils.VarConfigUtils;
@Repository
public class InviteUserInfoDaoImpl implements IInviteUserInfoDao{
	private static final Logger logger = Logger.getLogger(InviteUserInfoDaoImpl.class);
	
	@Override
	public InviteUserInfoModel selInviteUserInfoByUid(Integer uid) {
		Connection conn = null;
		PreparedStatement statement = null;
		ResultSet rs = null;
		InviteUserInfoModel inviteUserInfoModel = null;
		try {
			if (inviteUserInfoModel == null) {
				conn = DataSource.instance.getPool(VarConfigUtils.dbZhuUser).getConnection();
				statement = conn.prepareStatement(SqlTemplete.SQL_selInviteUserInfoByUid);
				DBHelper.setPreparedStatementParam(statement, uid);
				rs = statement.executeQuery();

				if (rs != null) {
					while (rs.next()) {
						inviteUserInfoModel = new InviteUserInfoModel().populateFromResultSet(rs);
						break;
					}
				}
			}
		} catch (SQLException e) {
			logger.error("<selInviteUserInfoByUid->SQLException>" + e.getMessage());
		} catch (Exception e) {
			logger.error("<selInviteUserInfoByUid->Exception>" + e.getMessage());
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
				logger.error("<selInviteUserInfoByUid->finally->Exception>" + e2.getMessage());
			}
		}
		return inviteUserInfoModel;
	}

	@Override
	public int addInviteUserInfo(Integer uid, Integer inviteCount, Integer gets) {
		try {
			int executeResult = DBHelper.execute(VarConfigUtils.dbZhuUser, SqlTemplete.SQL_insInviteUserInfo, false, uid, inviteCount, gets, System.currentTimeMillis()/1000);
			return executeResult;
		} catch (Exception e) {
			logger.error("<addInviteUserInfo->Exception>" + e.toString());
		}
		return -1;
	}

	@Override
	public int updInviteCount(Integer uid, Integer inviteCount) {
		try {
			int executeResult = DBHelper.execute(VarConfigUtils.dbZhuUser, SqlTemplete.SQL_updInviteUserInfo, false,  inviteCount,uid);
			return executeResult;
		} catch (Exception e) {
			logger.error("<updInviteCount->Exception>" + e.toString());
		}
		return -1;
	}
	
	@Override
	public int updInviteGets(Integer uid, Integer gets) {
		try {
			int executeResult = DBHelper.execute(VarConfigUtils.dbZhuUser, SqlTemplete.SQL_updInviteUserInfoGets, false,  gets,uid);
			return executeResult;
		} catch (Exception e) {
			logger.error("<updInviteGets->Exception>" + e.toString());
		}
		return -1;
	}

	@Override
	public int addInviteUserRewardInfo(Integer uid, Integer inviteUid, int status) {
		try {
			int executeResult = DBHelper.execute(VarConfigUtils.dbZhuUser, SqlTemplete.SQL_insInviteUserRewardInfo, false, uid, inviteUid, status,System.currentTimeMillis()/1000,System.currentTimeMillis()/1000);
			return executeResult;
		} catch (Exception e) {
			logger.error("<addInviteUserRewardInfo->Exception>" + e.toString());
		}
		return -1;
	}

	@Override
	public int updInviteUserRewardStatus(Integer uid, int status) {
		try {
			int executeResult = DBHelper.execute(VarConfigUtils.dbZhuUser, SqlTemplete.SQL_updInviteUserRewardInfo, false, status,uid,status);
			return executeResult;
		} catch (Exception e) {
			logger.error("<updInviteUserRewardStatus->Exception>" + e.toString());
		}
		return -1;
	}
	@Override
	public int updInviteUserRewardStatusByInviteUid(Integer inviteUid, int status) {
		try {
			int executeResult = DBHelper.execute(VarConfigUtils.dbZhuUser, SqlTemplete.SQL_updInviteUserRewardInfoByInviteUid, false, status, inviteUid,status);
			return executeResult;
		} catch (Exception e) {
			logger.error("<updInviteUserRewardStatusByInviteUid->Exception>" + e.toString());
		}
		return -1;
	}

	@Override
	public List<InviteUserRewardInfoModel> selInviteUserRewardByUid(Integer inviteUid) {
		Connection conn = null;
		PreparedStatement statement = null;
		ResultSet rs = null;
		InviteUserRewardInfoModel inviteUserRewardInfoModel = null;
		List<InviteUserRewardInfoModel> inviteUserRewardInfoModels = new ArrayList<InviteUserRewardInfoModel>();
		try {
				conn = DataSource.instance.getPool(VarConfigUtils.dbZhuUser).getConnection();
				statement = conn.prepareStatement(SqlTemplete.SQL_selInviteUserRewardInfoByInviteUid);
				DBHelper.setPreparedStatementParam(statement, inviteUid);
				rs = statement.executeQuery();

				if (rs != null) {
					while (rs.next()) {
						inviteUserRewardInfoModel = new InviteUserRewardInfoModel().populateFromResultSet(rs);
						inviteUserRewardInfoModels.add(inviteUserRewardInfoModel);
					}
				}
		} catch (SQLException e) {
			logger.error("<selInviteUserRewardByUid->SQLException>" + e.getMessage());
		} catch (Exception e) {
			logger.error("<selInviteUserRewardByUid->Exception>" + e.getMessage());
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
				logger.error("<selInviteUserRewardByUid->finally->Exception>" + e2.getMessage());
			}
		}
		return inviteUserRewardInfoModels;
	}
	
	@Override
	public List<InviteUserPeckLogModel> getInviteUserPeckLog(Integer uid) {
		Connection conn = null;
		PreparedStatement statement = null;
		ResultSet rs = null;
		InviteUserPeckLogModel inviteUserPeckLogModel = null;
		List<InviteUserPeckLogModel> inviteUserPeckLogModels = new ArrayList<InviteUserPeckLogModel>();
		try {
			conn = DataSource.instance.getPool(VarConfigUtils.dbZhuUser).getConnection();
			statement = conn.prepareStatement(SqlTemplete.SQL_selInviteUserPeckLog);
			DBHelper.setPreparedStatementParam(statement, uid);
			rs = statement.executeQuery();

			if (rs != null) {
				while (rs.next()) {
					inviteUserPeckLogModel = new InviteUserPeckLogModel().populateFromResultSet(rs);
					inviteUserPeckLogModels.add(inviteUserPeckLogModel);
				}
			}
		} catch (SQLException e) {
			logger.error("<getInviteUserPeckLog->SQLException>" + e.getMessage());
		} catch (Exception e) {
			logger.error("<getInviteUserPeckLog->Exception>" + e.getMessage());
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
				logger.error("<getInviteUserPeckLog->finally->Exception>" + e2.getMessage());
			}
		}
		return inviteUserPeckLogModels;
	}
	
	@Override
	public int selInviteUserPeckLogByInvitationId(Integer uid, Integer taskRewardId) {
		Connection conn = null;
		PreparedStatement statement = null;
		ResultSet rs = null;
		int i = 0;
		try {
				conn = DataSource.instance.getPool(VarConfigUtils.dbZhuUser).getConnection();
				statement = conn.prepareStatement(SqlTemplete.SQL_selInviteUserPeckLogByInvitationId);
				DBHelper.setPreparedStatementParam(statement, uid,taskRewardId);
				rs = statement.executeQuery();

				if (rs != null) {
					while (rs.next()) {
						i++;
						break;
					}
				}
		} catch (SQLException e) {
			logger.error("<selInviteUserPeckLogByInvitationId->SQLException>" + e.getMessage());
		} catch (Exception e) {
			logger.error("<selInviteUserPeckLogByInvitationId->Exception>" + e.getMessage());
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
				logger.error("<selInviteUserPeckLogByInvitationId->finally->Exception>" + e2.getMessage());
			}
		}
		return i;
	}
	@Override
	public int insInviteUserPeckLog(Integer uid, Integer invitationId, int status) {
		try {
			int executeResult = DBHelper.execute(VarConfigUtils.dbZhuUser, SqlTemplete.SQL_insInviteUserPeckLog, false, uid, invitationId, status,System.currentTimeMillis()/1000);
			return executeResult;
		} catch (Exception e) {
			logger.error("<insInviteUserPeckLog->Exception>" + e.toString());
		}
		return -1;
	}
}
