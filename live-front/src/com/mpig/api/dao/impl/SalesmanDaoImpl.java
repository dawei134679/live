package com.mpig.api.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.mpig.api.SqlTemplete;
import com.mpig.api.dao.ISalesmanDao;
import com.mpig.api.db.DBHelper;
import com.mpig.api.db.DataSource;
import com.mpig.api.model.SalesmanModel;
import com.mpig.api.utils.VarConfigUtils;

@Repository
public class SalesmanDaoImpl implements ISalesmanDao, SqlTemplete {
	private static final Logger logger = Logger.getLogger(SalesmanDaoImpl.class);

	@Override
	public SalesmanModel getSalesmanById(long id) {
		Connection conn = null;
		PreparedStatement statement = null;
		ResultSet rs = null;
		SalesmanModel salesmanModel = null;
		try {
			conn = DataSource.instance.getPool(VarConfigUtils.dbZhuAdmin).getConnection();
			statement = conn.prepareStatement(SqlTemplete.SQL_getSalesman);
			DBHelper.setPreparedStatementParam(statement, id);
			rs = statement.executeQuery();

			if (rs != null) {
				while (rs.next()) {
					salesmanModel = new SalesmanModel().populateFromResultSet(rs);
					break;
				}
			}
		} catch (SQLException e) {
			logger.error("<getSalesmanById->SQLException>" + e.getMessage());
		} catch (Exception e) {
			logger.error("<getSalesmanById->Exception>" + e.getMessage());
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
				logger.error("<getSalesmanById->finally->Exception>" + e2.getMessage());
			}
		}
		return salesmanModel;
	}

}
