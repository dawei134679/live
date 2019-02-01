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
import com.mpig.api.dao.IBillPrizeListDao;
import com.mpig.api.db.DBHelper;
import com.mpig.api.db.DataSource;
import com.mpig.api.model.BillPrizeListModel;
import com.mpig.api.utils.VarConfigUtils;

/**
 * @author zyl
 * @time 2016-7-25
 */
@Repository
public class BillPrizeListDaoImpl implements IBillPrizeListDao{
	private static final Logger logger = Logger.getLogger(BillPrizeListDaoImpl.class);

	@Override
	public List<BillPrizeListModel> getListByUser(int uid) {
		Connection conn = null;
		PreparedStatement statement = null;
		ResultSet rs = null;
		List<BillPrizeListModel> billPrizeListModels = new ArrayList<BillPrizeListModel>();
		try {
			conn = DataSource.instance.getPool(VarConfigUtils.dbZhuBill).getConnection();
			statement = conn.prepareStatement(SqlTemplete.SQL_selPrizeList);
			DBHelper.setPreparedStatementParam(statement,uid);
			rs = statement.executeQuery();
			if (rs != null) {
				while (rs.next()) {
					BillPrizeListModel billPrizeListModel = new BillPrizeListModel().populateFromResultSet(rs);
					billPrizeListModels.add(billPrizeListModel);
				}
			}
		} catch (SQLException e) {
			logger.error(e.getMessage());
		} catch (Exception e) {
			logger.error(e.getMessage());
		} finally {
			try {
				if (statement != null) {
					statement.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (Exception e2) {
				logger.error(e2.getMessage());
			}
		}
		return billPrizeListModels;
	}
}
