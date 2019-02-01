package com.mpig.api.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.mpig.api.SqlTemplete;
import com.mpig.api.dao.IPayMallListDao;
import com.mpig.api.db.DBHelper;
import com.mpig.api.db.DataSource;
import com.mpig.api.utils.VarConfigUtils;
@Repository
public class PayMallListDaoImpl implements IPayMallListDao{
	private static final Logger logger = Logger.getLogger(PayMallListDaoImpl.class);
	@Override
	public int insert(int gid, int gname, int srcuid, String srcnickname, int dstuid, String dstnickname, int count, int price, int realprice, int pricetotal, int realpricetotal, int credit) {
		Connection conn = null;
		PreparedStatement statement = null;
		int ires = 0;
		try {
			conn = DataSource.instance.getPool(VarConfigUtils.dbZhuPay).getConnection();
			statement = conn.prepareStatement(SqlTemplete.SQL_insBillMall);
			DBHelper.setPreparedStatementParam(statement, gid, gname, srcuid, srcnickname, dstuid, dstnickname, count, price, realprice, pricetotal, realpricetotal, credit, System.currentTimeMillis()/1000);
			ires = statement.executeUpdate();
		} catch (SQLException e) {
			logger.error("<insert->SQLException>" + e.getMessage());
		} catch (Exception e) {
			logger.error("<insert->Exception>" + e.getMessage());
		} finally {
			try {
				if (statement != null) {
					statement.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (Exception e2) {
				logger.error("<insert->finally->Exception>" + e2.getMessage());
			}
		}
		return ires;
	}

	
}
