package com.mpig.api.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.mpig.api.SqlTemplete;
import com.mpig.api.dao.IUserItemDao;
import com.mpig.api.db.DBHelper;
import com.mpig.api.db.DataSource;
import com.mpig.api.utils.VarConfigUtils;

@Repository
public class UserItemDaoImpl implements IUserItemDao {

	private static final Logger logger = Logger.getLogger(UserDaoImpl.class);

	@Override
	public int getItemCountByGid(Integer uid, Integer gid) {
		Connection conn = null;
		PreparedStatement statement = null;
		ResultSet rs = null;
		int count = 0;
		try {
			// 获取用户基本信息
			conn = DataSource.instance.getPool(VarConfigUtils.dbZhuItem)
					.getConnection();
			statement = conn
					.prepareStatement(SqlTemplete.SQL_getBadgeItemCount);
			DBHelper.setPreparedStatementParam(statement, uid, gid);
			rs = statement.executeQuery();

			if (rs != null) {
				while (rs.next()) {
					count = rs.getInt("itemCount");
					break;
				}
			}
		} catch (SQLException e) {
			logger.error("<getItemCountByGid->SQLException>" + e.getMessage());
		} catch (Exception e) {
			logger.error("<getItemCountByGid->Exception>" + e.getMessage());
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
				logger.error("<getItemCountByGid->finally->Exception>"
						+ e2.getMessage());
			}
		}
		return count;
	}

	@Override
	public int delItemByGid(Integer uid, Integer gid) {
		try {
			int executeResult = DBHelper.execute(VarConfigUtils.dbZhuItem,
					SqlTemplete.SQL_delBadgeForItem, false, uid, gid);
			return executeResult;
		} catch (Exception e) {
			logger.error("<delItemByGid->Exception>" + e.toString());
		}
		return -1;
	}

}
