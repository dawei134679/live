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
import com.mpig.api.dao.IValueaddPrivilegeDao;
import com.mpig.api.db.DataSource;
import com.mpig.api.model.ValueaddPrivilegeModel;
import com.mpig.api.utils.VarConfigUtils;

@Repository
public class ValueaddPrivilegeDaoImpl implements IValueaddPrivilegeDao {
	private static final Logger logger = Logger.getLogger(ValueaddPrivilegeDaoImpl.class);
	
	@Override
	public List<ValueaddPrivilegeModel> getList() {
		Connection conn = null;
		PreparedStatement statement = null;
		ResultSet rs = null;
		List<ValueaddPrivilegeModel> valueaddPrivilegeModels = new ArrayList<ValueaddPrivilegeModel>();
		try {
			conn = DataSource.instance.getPool(VarConfigUtils.dbZhuConfig).getConnection();
			statement = conn.prepareStatement(SqlTemplete.SQL_valueaddPrivilegeList);
			rs = statement.executeQuery();
			if (rs != null && rs.next()) {
				ValueaddPrivilegeModel valueaddPrivilegeModel = new ValueaddPrivilegeModel().populateFromResultSet(rs);
				valueaddPrivilegeModels.add(valueaddPrivilegeModel);
			}
		} catch (SQLException e) {
			logger.error(e.getMessage());
		} catch (Exception e) {
			logger.error(e.getMessage());
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
				logger.error(e2.getMessage());
			}
		}
		return valueaddPrivilegeModels;
	}
}
