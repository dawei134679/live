package com.mpig.api.service.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.mpig.api.SqlTemplete;
import com.mpig.api.db.DBHelper;
import com.mpig.api.db.DataSource;
import com.mpig.api.model.BillRedenvelopeModel;
import com.mpig.api.model.PayGetRedenvelopModel;
import com.mpig.api.model.UserBaseInfoModel;
import com.mpig.api.service.IBillService;
import com.mpig.api.service.IRedEnvelope;
import com.mpig.api.service.IUserService;
import com.mpig.api.utils.VarConfigUtils;

@Service
public class RedEnvelopeImpl implements IRedEnvelope, SqlTemplete {

	private static final Logger logger = Logger.getLogger(RedEnvelopeImpl.class);

	@Resource
	private IBillService billService;
	@Resource
	private IUserService userService;
	
	@Override
	public int insertPayGetRedenvelop(Object... objects) {
		int res = 0;
		try {
			res = DBHelper.execute(VarConfigUtils.dbZhuPay, SQL_InsertPayGetRedEnvelop, false, objects);
		} catch (Exception e) {
			logger.error("<InsertUserAccount->Exception>" + e.getMessage());
		}
		return res;
	}

	@Override
	public Map<String, Object> triedRedEnvelopeList(int envelopeId) {
		Connection conn = null;
		PreparedStatement statement = null;
		ResultSet rs = null;
		UserBaseInfoModel userBaseInfoModel = null;
		Map<String, Object> map = new HashMap<String,Object>();
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

		try {

			// 获取红包信息
			BillRedenvelopeModel billRedenvelopeModel = billService.getRedEnvelopById(envelopeId);
			if (billRedenvelopeModel == null) {
				return map;
			}
			map.put("sendCount", billRedenvelopeModel.getSendcnts());
			map.put("sendMoney", billRedenvelopeModel.getSendmoney());
			map.put("getCount", billRedenvelopeModel.getGetcnts());
			map.put("getMoney", billRedenvelopeModel.getGetmoney());
			
			// 获取红包被抢记录
			conn = DataSource.instance.getPool(VarConfigUtils.dbZhuPay).getConnection();
			statement = conn.prepareStatement(SQL_GetPayGetRedEnvelopByenvelopId);
			DBHelper.setPreparedStatementParam(statement, envelopeId);
			rs = statement.executeQuery();

			int row = 0;
			int incr = 0;
			int money = 0;

			if (rs != null) {
				while (rs.next()) {
					userBaseInfoModel = userService.getUserbaseInfoByUid(rs.getInt("dstUid"), false);
					if (userBaseInfoModel != null) {
						Map<String, Object> _map = new HashMap<String, Object>();
						_map.put("uid", rs.getInt("dstUid"));
						_map.put("nickname", userBaseInfoModel.getNickname());
						_map.put("headimage", userBaseInfoModel.getHeadimage());
						_map.put("money", rs.getInt("money"));
						_map.put("max", false);

						if (money < rs.getInt("money")) {
							row = incr;
							money = rs.getInt("money");
						}
						list.add(_map);
						incr++;
					}
				}
				if (list.size() > 0) {
					if (billRedenvelopeModel.getIsfinish() == 1) {
						list.get(row).put("max", true);
					}
				}
			}
			map.put("list", list);
		} catch (SQLException e) {
			logger.error("<triedRedEnvelopeList->SQLException>" + e.getMessage());
		} catch (Exception e) {
			logger.error("<triedRedEnvelopeList->Exception>" + e.getMessage());
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
				logger.error("<triedRedEnvelopeList->finally->Exception>" + e2.getMessage());
			}
		}
		return map;
	}

	@Override
	public PayGetRedenvelopModel getGetRedenvelopInfo(int envelopeId, int uid) {
		Connection conn = null;
		PreparedStatement statement = null;
		ResultSet rs = null;
		PayGetRedenvelopModel payGetRedenvelopModel = null;
		
		try {
			// 获取红包被抢记录
			conn = DataSource.instance.getPool(VarConfigUtils.dbZhuPay).getConnection();
			statement = conn.prepareStatement(SQL_GetPayGetRedEnvelopByEIdUid);
			DBHelper.setPreparedStatementParam(statement, envelopeId,uid);
			rs = statement.executeQuery();

			if (rs != null) {
				while (rs.next()) {
					payGetRedenvelopModel = new PayGetRedenvelopModel().populateFromResultSet(rs);
				}
			}
		} catch (SQLException e) {
			logger.error("<getGetRedenvelopInfo->SQLException>" + e.getMessage());
		} catch (Exception e) {
			logger.error("<getGetRedenvelopInfo->Exception>" + e.getMessage());
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
				logger.error("<getGetRedenvelopInfo->finally->Exception>" + e2.getMessage());
			}
		}
		return payGetRedenvelopModel;
	}
}
