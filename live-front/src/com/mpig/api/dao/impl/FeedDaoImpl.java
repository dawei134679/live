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
import com.mpig.api.dao.IFeedDao;
import com.mpig.api.db.DBHelper;
import com.mpig.api.db.DataSource;
import com.mpig.api.model.UserFeedModel;
import com.mpig.api.model.UserFeedReplyModel;
import com.mpig.api.utils.VarConfigUtils;

@Repository
public class FeedDaoImpl implements IFeedDao{
	private static final Logger logger = Logger.getLogger(InviteUserInfoDaoImpl.class);

	@Override
	public List<UserFeedModel> getFeedByUser(Integer uid) {
		Connection conn = null;
		PreparedStatement statement = null;
		ResultSet rs = null;
		UserFeedModel userFeedModel = null;
		List<UserFeedModel> userFeedModels = new ArrayList<UserFeedModel>();
		try {
				conn = DataSource.instance.getPool(VarConfigUtils.dbZhuUser).getConnection();
				statement = conn.prepareStatement(SqlTemplete.SQL_getFeedByUser);
				DBHelper.setPreparedStatementParam(statement, uid);
				rs = statement.executeQuery();

				if (rs != null) {
					while (rs.next()) {
						userFeedModel = new UserFeedModel().populateFromResultSet(rs);
						userFeedModels.add(userFeedModel);
					}
				}
		} catch (SQLException e) {
			logger.error("<getFeedByUser->SQLException>" + e.getMessage());
		} catch (Exception e) {
			logger.error("<getFeedByUser->Exception>" + e.getMessage());
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
				logger.error("<getFeedByUser->finally->Exception>" + e2.getMessage());
			}
		}
		return userFeedModels;
	}

	@Override
	public UserFeedModel getFeed(Integer id) {
		Connection conn = null;
		PreparedStatement statement = null;
		ResultSet rs = null;
		UserFeedModel userFeedModel = null;
		try {
				conn = DataSource.instance.getPool(VarConfigUtils.dbZhuUser).getConnection();
				statement = conn.prepareStatement(SqlTemplete.SQL_getFeed);
				DBHelper.setPreparedStatementParam(statement, id);
				rs = statement.executeQuery();

				if (rs != null) {
					while (rs.next()) {
						userFeedModel = new UserFeedModel().populateFromResultSet(rs);
						break;
					}
				}
		} catch (SQLException e) {
			logger.error("<getFeed->SQLException>" + e.getMessage());
		} catch (Exception e) {
			logger.error("<getFeed->Exception>" + e.getMessage());
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
				logger.error("<getFeed->finally->Exception>" + e2.getMessage());
			}
		}
		return userFeedModel;
	}

	@Override
	public int addFeed(Integer uid, String content) {
		try {
			int executeResult = DBHelper.execute(VarConfigUtils.dbZhuUser, SqlTemplete.SQL_addFeed, true, uid, content, 1, System.currentTimeMillis()/1000);
			return executeResult;
		} catch (Exception e) {
			logger.error("<addFeed->Exception>" + e.toString());
		}
		return -1;
	}

	@Override
	public int addFeedImgs(Integer feedId, String imgs) {
		try {
			int executeResult = DBHelper.execute(VarConfigUtils.dbZhuUser, SqlTemplete.SQL_addFeedImgs, false,imgs, feedId);
			return executeResult;
		} catch (Exception e) {
			logger.error("<addFeedImgs->Exception>" + e.toString());
		}
		return -1;
	}

	@Override
	public int delFeed(Integer feedId) {
		try {
			int executeResult = DBHelper.execute(VarConfigUtils.dbZhuUser, SqlTemplete.SQL_delFeed, false, feedId);
			return executeResult;
		} catch (Exception e) {
			logger.error("<delFeed->Exception>" + e.toString());
		}
		return -1;
	}

	@Override
	public int addFeedLaud(Integer feedId, Integer uid) {
		try {
			int executeResult = DBHelper.execute(VarConfigUtils.dbZhuUser, SqlTemplete.SQL_addFeedLaud, false, uid, feedId, System.currentTimeMillis()/1000);
			return executeResult;
		} catch (Exception e) {
			logger.error("<addFeedLaud->Exception>" + e.toString());
		}
		return -1;
	}

	@Override
	public int delFeedLaud(Integer feedId, Integer uid) {
		try {
			int executeResult = DBHelper.execute(VarConfigUtils.dbZhuUser, SqlTemplete.SQL_delFeedLaud, false, uid, feedId);
			return executeResult;
		} catch (Exception e) {
			logger.error("<delFeedLaud->Exception>" + e.toString());
		}
		return -1;
	}

	@Override
	public UserFeedReplyModel getFeedReplyById(Integer id) {
		Connection conn = null;
		PreparedStatement statement = null;
		ResultSet rs = null;
		UserFeedReplyModel userFeedReplyModel = null;
		try {
				conn = DataSource.instance.getPool(VarConfigUtils.dbZhuUser).getConnection();
				statement = conn.prepareStatement(SqlTemplete.SQL_getFeedReplyById);
				DBHelper.setPreparedStatementParam(statement, id);
				rs = statement.executeQuery();

				if (rs != null) {
					while (rs.next()) {
						userFeedReplyModel = new UserFeedReplyModel().populateFromResultSet(rs);
						break;
					}
				}
		} catch (SQLException e) {
			logger.error("<getFeedReplyById->SQLException>" + e.getMessage());
		} catch (Exception e) {
			logger.error("<getFeedReplyById->Exception>" + e.getMessage());
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
				logger.error("<getFeedReplyById->finally->Exception>" + e2.getMessage());
			}
		}
		return userFeedReplyModel;
	}

	@Override
	public List<UserFeedReplyModel> getFeedReplyByFeedId(Integer feedId) {
		Connection conn = null;
		PreparedStatement statement = null;
		ResultSet rs = null;
		UserFeedReplyModel userFeedReplyModel = null;
		List<UserFeedReplyModel> userFeedReplyModels = new ArrayList<UserFeedReplyModel>();
		try {
				conn = DataSource.instance.getPool(VarConfigUtils.dbZhuUser).getConnection();
				statement = conn.prepareStatement(SqlTemplete.SQL_getFeedReplyByFeedId);
				DBHelper.setPreparedStatementParam(statement, feedId);
				rs = statement.executeQuery();

				if (rs != null) {
					while (rs.next()) {
						userFeedReplyModel = new UserFeedReplyModel().populateFromResultSet(rs);
						userFeedReplyModels.add(userFeedReplyModel);
					}
				}
		} catch (SQLException e) {
			logger.error("<getFeedReplyByFeedId->SQLException>" + e.getMessage());
		} catch (Exception e) {
			logger.error("<getFeedReplyByFeedId->Exception>" + e.getMessage());
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
				logger.error("<getFeedReplyByFeedId->finally->Exception>" + e2.getMessage());
			}
		}
		return userFeedReplyModels;
	}

	@Override
	public int addFeedReply(Integer feedId, Integer parentFrId, Integer fromUid, Integer toUid,
			String content) {
		try {
			int executeResult = DBHelper.execute(VarConfigUtils.dbZhuUser, SqlTemplete.SQL_addFeedReply, true, feedId, parentFrId, fromUid,toUid,content, System.currentTimeMillis()/1000);
			return executeResult;
		} catch (Exception e) {
			logger.error("<addFeedLaud->Exception>" + e.toString());
		}
		return -1;
	}

	@Override
	public int delFeedReply(Integer frId) {
		try {
			int executeResult = DBHelper.execute(VarConfigUtils.dbZhuUser, SqlTemplete.SQL_delFeedReply, false, frId);
			return executeResult;
		} catch (Exception e) {
			logger.error("<delFeedReply->Exception>" + e.toString());
		}
		return -1;
	}

	@Override
	public int addFeedReward(Integer feedId, Integer feedCUid, Integer rewardUid, Integer zhutou) {
		try {
			int executeResult = DBHelper.execute(VarConfigUtils.dbZhuUser, SqlTemplete.SQL_addFeedReward, false, feedId, feedCUid, rewardUid, zhutou, System.currentTimeMillis()/1000);
			return executeResult;
		} catch (Exception e) {
			logger.error("<addFeedReward->Exception>" + e.toString());
		}
		return -1;
	}
	
	@Override
	public int addFeedRewardGift(Integer feedId, Integer feedCUid, Integer rewardUid, Integer gift, Integer count) {
		try {
			int executeResult = DBHelper.execute(VarConfigUtils.dbZhuUser, SqlTemplete.SQL_addFeedRewardGift, false, feedId, feedCUid, rewardUid, gift, count, System.currentTimeMillis()/1000);
			return executeResult;
		} catch (Exception e) {
			logger.error("<addFeedRewardGift->Exception>" + e.toString());
		}
		return -1;
	}

	@Override
	public int getReportFeedByFid(Integer fid) {
		Connection conn = null;
		PreparedStatement statement = null;
		ResultSet rs = null;
		int id = 0;
		try {
			conn = DataSource.instance.getPool(VarConfigUtils.dbZhuAdmin).getConnection();
			statement = conn.prepareStatement(SqlTemplete.SQL_selReportFeedByFid);
			DBHelper.setPreparedStatementParam(statement,fid);
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
	public int getReportFeedUserByRid(Integer rid, Integer dstuid) {
		Connection conn = null;
		PreparedStatement statement = null;
		ResultSet rs = null;
		int count = 0;
		try {
			conn = DataSource.instance.getPool(VarConfigUtils.dbZhuAdmin).getConnection();
			statement = conn.prepareStatement(SqlTemplete.SQL_selReportFeedUserByRid);
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
	public int addReportFeedUser(Integer rid, Integer fid, String reportReason,
			Integer dstuid) {
		try {
			int executeResult = DBHelper.execute(VarConfigUtils.dbZhuAdmin, SqlTemplete.SQL_insReportFeedUser, false, rid, fid, reportReason, dstuid, System.currentTimeMillis()/1000);
			return executeResult;
		} catch (Exception e) {
			logger.error("<addReportFeedUser->Exception>" + e.toString());
		}
		return -1;
	}

	@Override
	public int addReportFeed(Integer reportUid, Integer reportFid) {
		try {
			int executeResult = DBHelper.execute(VarConfigUtils.dbZhuAdmin, SqlTemplete.SQL_insReportFeed, true, reportUid, reportFid, 1, 0, System.currentTimeMillis()/1000, System.currentTimeMillis()/1000);
			return executeResult;
		} catch (Exception e) {
			logger.error("<addReportFeed->Exception>" + e.toString());
		}
		return -1;
	}

	@Override
	public int updReportFeed(Integer reportFid) {
		try {
			int executeResult = DBHelper.execute(VarConfigUtils.dbZhuAdmin, SqlTemplete.SQL_updReportFeed, false, reportFid);
			return executeResult;
		} catch (Exception e) {
			logger.error("<updReportFeed->Exception>" + e.toString());
		}
		return -1;
	}

	@Override
	public List<String> getRecommendAnchorId() {
		Connection conn = null;
		PreparedStatement statement = null;
		ResultSet rs = null;
		List<String> list = new ArrayList<String>();
		try {
				conn = DataSource.instance.getPool(VarConfigUtils.dbZhuWeb).getConnection();
				statement = conn.prepareStatement(SqlTemplete.SQL_getFeedRecommendAnchor);
				DBHelper.setPreparedStatementParam(statement);
				rs = statement.executeQuery();

				if (rs != null) {
					while (rs.next()) {
						list.add(rs.getString("uid"));
					}
				}
		} catch (SQLException e) {
			logger.error("<getRecommendAnchorId->SQLException>" + e.getMessage());
		} catch (Exception e) {
			logger.error("<getRecommendAnchorId->Exception>" + e.getMessage());
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
				logger.error("<getRecommendAnchorId->finally->Exception>" + e2.getMessage());
			}
		}
		return list;
	}
	
}
