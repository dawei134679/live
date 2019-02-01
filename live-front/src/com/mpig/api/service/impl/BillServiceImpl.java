package com.mpig.api.service.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.mpig.api.SqlTemplete;
import com.mpig.api.db.DBHelper;
import com.mpig.api.db.DataSource;
import com.mpig.api.model.BillRedenvelopeModel;
import com.mpig.api.service.IBillService;
import com.mpig.api.utils.DateUtils;
import com.mpig.api.utils.VarConfigUtils;


@Service
public class BillServiceImpl implements IBillService,SqlTemplete {

	private static Logger logger = Logger.getLogger(BillServiceImpl.class);
	/**
	 * 插入送礼账单
	 * @param objs 参数严格按照sql中的占位符顺序
	 * @return
	 */
	@Override
	public int insertBill(Object... objs) {		
		try {
			return DBHelper.execute("zhu_bill",String.format(SQL_InsertBill,"bill_"+DateUtils.dateToString(null, "YMM")), false, objs);
		} catch (Exception e) {
			logger.error("<insertBill->Exception>",e);
		}
		return 0;
	}
	/**
	 * 插入送背包礼物账单
	 * @param objs 参数严格按照sql中的占位符顺序
	 * @return
	 */
	@Override
	public int insertBagBill(Object... objs) {		
		try {
			return DBHelper.execute("zhu_bill",String.format(SQL_InsertBagBill,"bill_"+DateUtils.dateToString(null, "YMM")), false, objs);
		} catch (Exception e) {
			logger.error("<insertBagBill->Exception>",e);
		}
		return 0;
	}
	@Override
	public int insertRedEnvelop(Object... objs) {
		int ires = 0;
		try {
			ires = DBHelper.execute("zhu_bill",SQL_InsertBillRedEnvelope, true, objs);
		} catch (Exception e) {
			logger.error("<insertBill->Exception>",e);
		}
		return ires;
	}

	/**
	 * 根据红包id获取红包信息
	 * @param id
	 * @return
	 */
	@Override
	public BillRedenvelopeModel getRedEnvelopById(int id) {
		BillRedenvelopeModel billRedenvelopeModel = null;
		Connection conn = null;
		PreparedStatement statement = null;
		ResultSet rs = null;

		try {
				// 获取用户基本信息
				conn = DataSource.instance.getPool(VarConfigUtils.dbZhuBill).getConnection();
				statement = conn
						.prepareStatement(SQL_GetBiilRedEnvelopeById);
				DBHelper.setPreparedStatementParam(statement, id);
				rs = statement.executeQuery();

				if (rs != null) {
					while (rs.next()) {
						billRedenvelopeModel = new BillRedenvelopeModel().populateFromResultSet(rs);
						break;
					}
				}
		} catch (SQLException e) {
			logger.error("<getRedEnevlopById->SQLException>" , e);
		} catch (Exception e) {
			logger.error("<getRedEnevlopById->Exception>" , e);
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
				logger.error("<getRedEnevlopById->finally->Exception>" + e2.getMessage());
			}
		}
		return billRedenvelopeModel;
	}
	
	/**
	 * 根据红包id修改信息
	 * @param isfinish
	 * @param getmoney
	 * @param getcnts
	 * @param gettime
	 * @param id
	 * @return
	 */
	@Override
	public int updRedEnvelopeById(int isfinish,int getmoney,int getcnts,int gettime, int id) {
		int ires = 0;
		try {
			ires = DBHelper.execute("zhu_bill",SQL_UpdBillRedEnvelopeById, false, isfinish,getmoney,getcnts,gettime,id);
		} catch (Exception e) {
			logger.error("<updRedEnvelopeById->Exception>"+e.toString());
		}
		return ires;
	}
	
	public int insertPrizeBill(int uid,int gid, int anchoruid, int multiples,int luckyCount, int gprice, int priceTotal, int wealthTotal, int creditTotal, int sendPrice, int sendCount){
		Connection conn = null;
		PreparedStatement statement = null;
		int ires = 0;
		try {
			conn = DataSource.instance.getPool(VarConfigUtils.dbZhuBill).getConnection();
			statement = conn.prepareStatement(SqlTemplete.SQL_insPrize);
			DBHelper.setPreparedStatementParam(statement,uid,gid,anchoruid,multiples,luckyCount,gprice,priceTotal,wealthTotal,creditTotal,sendPrice,sendCount,System.currentTimeMillis()/1000);
			ires = statement.executeUpdate();
		} catch (SQLException e) {
			logger.error("<insertPrizeBill->SQLException>" + e.getMessage());
		} catch (Exception e) {
			logger.error("<insertPrizeBill->Exception>" + e.getMessage());
		} finally {
			try {
				if (statement != null) {
					statement.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (Exception e2) {
				logger.error("<insertPrizeBill->finally->Exception>" + e2.getMessage());
			}
		}
		return ires;
	}
	/**
	 * 查询消费记录
	 * @param uid 消费用户id
	 * @param date 查询消费记录的日期
	 * @return
	 */
	public List<Map<String, Object>> getCostRecord(int uid,String date,int page,int pageSize){
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			String tableName = "bill_"+date.substring(0,6);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
			Date d1 = sdf.parse(date);
			Long t1 = d1.getTime()/1000;
			Long t2 = t1+24*60*60;
			con= DataSource.instance.getPool(VarConfigUtils.dbZhuBill).getConnection();
			pstmt = con.prepareStatement(String.format(SqlTemplete.SQL_getCostRecord, tableName));
			DBHelper.setPreparedStatementParam(pstmt,uid,t1,t2,page*pageSize,pageSize);
			rs = pstmt.executeQuery();
			while (rs != null && rs.next()) {
				Map<String ,Object> map = new HashMap<String, Object>();
				map.put("dstnickname", rs.getString("dstnickname"));
				map.put("giftname", rs.getString("gname"));
				map.put("sumcount", rs.getInt("count"));
				map.put("sumzhutou", (rs.getInt("price"))*(rs.getInt("count")));
				list.add(map);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (pstmt != null) {
					pstmt.close();
				}
				if (con != null) {
					con.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return list;
	}
	
	/**
	 * 查询消费记录
	 * @param uid 消费用户id
	 * @param date 查询消费记录的日期
	 * @return
	 */
	public long getCostRecordTotalCount(int uid,String date){
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		long totalCount = 0;
		try {
			String tableName = "bill_"+date.substring(0,6);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
			Date d1 = sdf.parse(date);
			Long t1 = d1.getTime()/1000;
			Long t2 = t1+24*60*60;
			con= DataSource.instance.getPool(VarConfigUtils.dbZhuBill).getConnection();
			pstmt = con.prepareStatement(String.format(SqlTemplete.SQL_getCostRecordTotalCount, tableName));
			DBHelper.setPreparedStatementParam(pstmt,uid,t1,t2);
			rs = pstmt.executeQuery();
			while (rs != null && rs.next()) {
				totalCount = rs.getLong("totalCount");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (pstmt != null) {
					pstmt.close();
				}
				if (con != null) {
					con.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return totalCount;
	}
	@Override
	public long getConBillSumaryByDate(int uid, String date) {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		long totalCount = 0;
		try {
			String tableName = "bill_"+date.substring(0,6);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
			Date d1 = sdf.parse(date);
			Long t1 = d1.getTime()/1000;
			Long t2 = t1+24*60*60;
			con= DataSource.instance.getPool(VarConfigUtils.dbZhuBill).getConnection();
			pstmt = con.prepareStatement(String.format(SqlTemplete.SQL_getCostRecordTotalSum, tableName));
			DBHelper.setPreparedStatementParam(pstmt,uid,t1,t2);
			rs = pstmt.executeQuery();
			while (rs != null && rs.next()) {
				totalCount = rs.getLong("totalSum");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (pstmt != null) {
					pstmt.close();
				}
				if (con != null) {
					con.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return totalCount;
	}
	@Override
	public List<Map<String, Object>> getRecBillListByDate(int uid, String date, int page, int pageSize) {
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			String tableName = "bill_"+date.substring(0,6);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
			Date d1 = sdf.parse(date);
			Long t1 = d1.getTime()/1000;
			Long t2 = t1+24*60*60;
			con= DataSource.instance.getPool(VarConfigUtils.dbZhuBill).getConnection();
			pstmt = con.prepareStatement(String.format(SqlTemplete.SQL_getRecBillList, tableName));
			DBHelper.setPreparedStatementParam(pstmt,uid,t1,t2,page*pageSize,pageSize);
			rs = pstmt.executeQuery();
			while (rs != null && rs.next()) {
				Map<String ,Object> map = new HashMap<String, Object>();
				map.put("srcnickname", rs.getString("srcnickname"));
				map.put("giftname", rs.getString("gname"));
				map.put("sumcount", rs.getInt("count"));
				map.put("summoney", rs.getString("credit"));
				list.add(map);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (pstmt != null) {
					pstmt.close();
				}
				if (con != null) {
					con.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return list;
	}
	@Override
	public long getRecTotalCount(int uid, String date) {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		long totalCount = 0;
		try {
			String tableName = "bill_"+date.substring(0,6);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
			Date d1 = sdf.parse(date);
			Long t1 = d1.getTime()/1000;
			Long t2 = t1+24*60*60;
			con= DataSource.instance.getPool(VarConfigUtils.dbZhuBill).getConnection();
			pstmt = con.prepareStatement(String.format(SqlTemplete.SQL_getRecBillTotalCount, tableName));
			DBHelper.setPreparedStatementParam(pstmt,uid,t1,t2);
			rs = pstmt.executeQuery();
			while (rs != null && rs.next()) {
				totalCount = rs.getLong("totalCount");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (pstmt != null) {
					pstmt.close();
				}
				if (con != null) {
					con.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return totalCount;
	}
	@Override
	public long getRecBillSumaryByDate(int uid, String date) {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		long totalCredit = 0;
		try {
			String tableName = "bill_"+date.substring(0,6);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
			Date d1 = sdf.parse(date);
			Long t1 = d1.getTime()/1000;
			Long t2 = t1+24*60*60;
			con= DataSource.instance.getPool(VarConfigUtils.dbZhuBill).getConnection();
			pstmt = con.prepareStatement(String.format(SqlTemplete.SQL_getRecBillTotalCredit, tableName));
			DBHelper.setPreparedStatementParam(pstmt,uid,t1,t2);
			rs = pstmt.executeQuery();
			while (rs != null && rs.next()) {
				totalCredit = rs.getLong("totalCredit");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (pstmt != null) {
					pstmt.close();
				}
				if (con != null) {
					con.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return totalCredit;
	}
}