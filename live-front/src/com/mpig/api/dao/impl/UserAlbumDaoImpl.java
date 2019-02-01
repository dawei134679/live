package com.mpig.api.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.mpig.api.SqlTemplete;
import com.mpig.api.dao.IUserAlbumDao;
import com.mpig.api.db.DBHelper;
import com.mpig.api.db.DataSource;
import com.mpig.api.model.UserAlbumModel;
import com.mpig.api.utils.VarConfigUtils;

@Repository
public class UserAlbumDaoImpl implements IUserAlbumDao{
	private static final Logger logger = Logger.getLogger(MallDaoImpl.class);
	@Override
	public int addPhoto(Integer uid, String fileName, String photoUrl, String photoThumbUrl) {
		try {
			int executeResult = DBHelper.execute(VarConfigUtils.dbZhuUser, SqlTemplete.SQL_insUserAlbum, false, uid, fileName, photoUrl, photoThumbUrl, System.currentTimeMillis()/1000);
			return executeResult;
		} catch (Exception e) {
			logger.error("<addMallInfo->Exception>" + e.toString());
		}
		return -1;
	}

	@Override
	public int delPhoto(Integer id, Integer uid) {
		try {
			int executeResult = DBHelper.execute(VarConfigUtils.dbZhuUser, SqlTemplete.SQL_delUserAlbum, false, id,uid);
			return executeResult;
		} catch (Exception e) {
			logger.error("<addMallInfo->Exception>" + e.toString());
		}
		return -1;
	}

	@Override
	public List<UserAlbumModel> getUserAlbum(Integer uid) {
		Connection conn = null;
		PreparedStatement statement = null;
		ResultSet rs = null;
		List<UserAlbumModel> albumModels = new ArrayList<UserAlbumModel>();
		try {
			conn = DataSource.instance.getPool(VarConfigUtils.dbZhuUser).getConnection();
			statement = conn.prepareStatement(SqlTemplete.SQL_selUserAlbum);
			DBHelper.setPreparedStatementParam(statement,uid);
			rs = statement.executeQuery();
			if (rs != null) {
				while (rs.next()) {
					UserAlbumModel userAlbumModel = new UserAlbumModel().populateFromResultSet(rs);
					albumModels.add(userAlbumModel);
				}
			}
		} catch (SQLException e) {
			logger.error(e);
		} catch (Exception e) {
			logger.error(e);
		} finally {
			try {
				if (statement != null) {
					statement.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (Exception e2) {
				logger.error(e2);
			}
		}
		return albumModels;
	}

	@Override
	public UserAlbumModel getUserAlbumById(Integer id) {
		Connection conn = null;
		PreparedStatement statement = null;
		ResultSet rs = null;
		UserAlbumModel userAlbumModel = null;
		try {
			conn = DataSource.instance.getPool(VarConfigUtils.dbZhuUser).getConnection();
			statement = conn.prepareStatement(SqlTemplete.SQL_selUserAlbumById);
			DBHelper.setPreparedStatementParam(statement,id);
			rs = statement.executeQuery();
			if (rs != null) {
				while (rs.next()) {
					userAlbumModel = new UserAlbumModel().populateFromResultSet(rs);
					break;
				}
			}
		} catch (SQLException e) {
			logger.error(e);
		} catch (Exception e) {
			logger.error(e);
		} finally {
			try {
				if (statement != null) {
					statement.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (Exception e2) {
				logger.error(e2);
			}
		}
		return userAlbumModel;
	}

	@Override
	public int selPhotoCountByUser(Integer uid) {
		Connection conn = null;
		PreparedStatement statement = null;
		ResultSet rs = null;
		int count = 0;
		try {
			conn = DataSource.instance.getPool(VarConfigUtils.dbZhuUser).getConnection();
			statement = conn.prepareStatement(SqlTemplete.SQL_selUserAlbumCount);
			DBHelper.setPreparedStatementParam(statement,uid);
			rs = statement.executeQuery();
			if (rs != null) {
				while (rs.next()) {
					count = rs.getInt("count");
					break;
				}
			}
		} catch (SQLException e) {
			logger.error(e);
		} catch (Exception e) {
			logger.error(e);
		} finally {
			try {
				if (statement != null) {
					statement.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (Exception e2) {
				logger.error(e2);
			}
		}
		return count;
	}

	@Override
	public int updPhoto(Integer id, String fileName, String photoUrl, String photoThumbUrl) {
		try {
			int executeResult = DBHelper.execute(VarConfigUtils.dbZhuUser, SqlTemplete.SQL_updUserAlbumById, false, fileName, photoUrl, photoThumbUrl, System.currentTimeMillis()/1000, id);
			return executeResult;
		} catch (Exception e) {
			logger.error(e);
		}
		return -1;
	}
	
	@Override
	public UserAlbumModel getUserAlbumLast(Integer uid) {
		Connection conn = null;
		PreparedStatement statement = null;
		ResultSet rs = null;
		UserAlbumModel userAlbumModel = null;
		try {
			conn = DataSource.instance.getPool(VarConfigUtils.dbZhuUser).getConnection();
			statement = conn.prepareStatement(SqlTemplete.SQL_selUserAlbumSortDesc);
			DBHelper.setPreparedStatementParam(statement,uid);
			rs = statement.executeQuery();
			if (rs != null) {
				while (rs.next()) {
					userAlbumModel = new UserAlbumModel().populateFromResultSet(rs);
					break;
				}
			}
		} catch (SQLException e) {
			logger.error(e);
		} catch (Exception e) {
			logger.error(e);
		} finally {
			try {
				if (statement != null) {
					statement.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (Exception e2) {
				logger.error(e2);
			}
		}
		return userAlbumModel;
	}
	
	@Override
	public int getReportAlbumByPid(Integer pid) {
		Connection conn = null;
		PreparedStatement statement = null;
		ResultSet rs = null;
		int id = 0;
		try {
			conn = DataSource.instance.getPool(VarConfigUtils.dbZhuAdmin).getConnection();
			statement = conn.prepareStatement(SqlTemplete.SQL_selReportAlbumByPid);
			DBHelper.setPreparedStatementParam(statement,pid);
			rs = statement.executeQuery();
			if (rs != null) {
				while (rs.next()) {
					id = rs.getInt("id");
					break;
				}
			}
		} catch (SQLException e) {
			logger.error(e);
		} catch (Exception e) {
			logger.error(e);
		} finally {
			try {
				if (statement != null) {
					statement.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (Exception e2) {
				logger.error(e2);
			}
		}
		return id;
	}
	
	@Override
	public int getReportAlbummUserByRid(Integer rid, Integer dstuid) {
		Connection conn = null;
		PreparedStatement statement = null;
		ResultSet rs = null;
		int count = 0;
		try {
			conn = DataSource.instance.getPool(VarConfigUtils.dbZhuAdmin).getConnection();
			statement = conn.prepareStatement(SqlTemplete.SQL_selReportAlbummUserByRid);
			DBHelper.setPreparedStatementParam(statement,rid,dstuid);
			rs = statement.executeQuery();
			if (rs != null) {
				while (rs.next()) {
					count = rs.getInt("count");
					break;
				}
			}
		} catch (SQLException e) {
			logger.error(e);
		} catch (Exception e) {
			logger.error(e);
		} finally {
			try {
				if (statement != null) {
					statement.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (Exception e2) {
				logger.error(e2);
			}
		}
		return count;
	}
	
	@Override
	public int addReportAlbumUser(Integer rid, Integer pid, String reportReason, Integer dstuid) {
		try {
			int executeResult = DBHelper.execute(VarConfigUtils.dbZhuAdmin, SqlTemplete.SQL_insReportAlbumUser, false, rid, pid, reportReason, dstuid, System.currentTimeMillis()/1000);
			return executeResult;
		} catch (Exception e) {
			logger.error("<addReportAlbumUser->Exception>" + e.toString());
		}
		return -1;
	}
	
	@Override
	public int addReportAlbum(Integer reportUid, Integer reportPid,String copyFilename,String copyUrl) {
		try {
			int executeResult = DBHelper.execute(VarConfigUtils.dbZhuAdmin, SqlTemplete.SQL_insReportAlbum, true, reportUid, reportPid, copyFilename, copyUrl, 1, 0, System.currentTimeMillis()/1000, System.currentTimeMillis()/1000);
			return executeResult;
		} catch (Exception e) {
			logger.error("<addReportAlbum->Exception>" + e.toString());
		}
		return -1;
	}
	
	@Override
	public int updReportAlbum(Integer reportPid) {
		try {
			int executeResult = DBHelper.execute(VarConfigUtils.dbZhuAdmin, SqlTemplete.SQL_updReportAlbum, false, reportPid);
			return executeResult;
		} catch (Exception e) {
			logger.error("<updReportAlbum->Exception>" + e.toString());
		}
		return -1;
	}
}
